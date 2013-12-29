package com.android.component.ui.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.component.ActivityComponent;
import com.android.component.AuditComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.component.WifiComponent;
import com.android.dialog.MyProcessDialog;
import com.android.domain.User;
import com.android.mapping.UserMapping;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
@EBean
public class LoginComponent {

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@App
	MyApp myApp;

	// 注入 Context 变量
	@RootContext
	Context context;

	// 注入 Activity 变量
	@RootContext
	Activity activity;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	WifiComponent wifiComponent;

	@Bean
	AuditComponent auditComponent;

	@Bean
	ActivityComponent activityComponent;

	MyProcessDialog dialog;

	@AfterInject
	public void initLogin() {
		dialog = new MyProcessDialog(context, stringResComponent.loginWait);
	}

	/**
	 * 登录操作
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public void executeLogin(String username, String password, String userType) {
		new Login().execute(username, password, userType);
	}

	/**
	 * 注销操作
	 * 
	 * @param userId
	 *            用户ID
	 * @param shopId
	 *            店铺ID
	 */
	public void executeLogout(String userId, String shopId) {
		new Logout().execute(userId, shopId);
	}

	private Integer loginLocal(String username, String password, String userType) {
		// 得到IP和Mac地址
		String strIP = wifiComponent.getIPAddress();
		String strMAC = wifiComponent.getMacAddress(context);

		if (!StringUtils.equalsIgnoreCase(Constants.ROLE_SUPERADMIN, userType)) {
			User user = User.checkLogin(username, myPrefs.shopId().get());
			if (user != null) {
				if (StringUtils.equalsIgnoreCase(password, user.password)) {
					setLoginInfo(user);
					auditComponent.logAudit(user, Constants.LOGIN);
					// 启动主窗口
					activityComponent.startMain();
					return Constants.STATUS_SUCCESS;
				} else {
					return Constants.STATUS_FAILED;
				}

			}
		}
		if (wifiComponent.isConnected()) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("user.username", username);
			params.put("user.password", password);
			params.put("user.shop.id", myPrefs.shopId().get());
			params.put("user.userIp", strIP);
			params.put("user.userMac", strMAC);
			return loginRemote(userType, params);
		}
		return Constants.STATUS_NETWORK_ERROR;
	}

	private Integer loginRemote(String loginType, Map<String, String> params) {
		if (StringUtils.equalsIgnoreCase(loginType, Constants.ROLE_SUPERADMIN)) {
			return loginRemote(Constants.URL_LOGIN_ADMIN_PATH, loginType, params);
		} else {
			return loginRemote(Constants.URL_LOGIN_PATH, loginType, params);
		}
	}

	private Integer loginRemote(String url, String loginType, Map<String, String> params) {
		// ResponseData data = RemoteDataHandler.post(url, params);
		UserMapping data = UserMapping.postJSON(url, params);
		if (data.code == Constants.STATUS_SUCCESS) {
			UserMapping.User remoteUser = data.datas.get(0);
			// 如果是SUPERADMIN登录，而且登录的用户却没有SUPERADMIN的权限，则禁止登录
			if (!StringUtils.equalsIgnoreCase(remoteUser.usertype, Constants.ROLE_SUPERADMIN)
					&& StringUtils.equalsIgnoreCase(loginType, Constants.ROLE_SUPERADMIN)) {
				return Constants.STATUS_FAILED;
			}
			// login_audit(user_bean, "Login");
			User user = new User();
			if (!StringUtils.equalsIgnoreCase(Constants.ROLE_SUPERADMIN, loginType)) {
				user.uid = remoteUser.id;
				user.username = remoteUser.username;
				user.password = params.get("user.password");
				user.usertype = remoteUser.usertype;
				user.status = remoteUser.status;
				user.shopId = remoteUser.shop.id;
				user.shopName = remoteUser.shop.name;
				user.shopCode = remoteUser.shop.code;
				user.save();
			} else {
				user = getDbUser(remoteUser);
			}
			setLoginInfo(user);
			auditComponent.logAudit(user, "Login");
			// 启动主窗口
			activityComponent.startMain();
		}
		return data.code;
	}

	public Integer logout(String userId, String shopId) {
		try {
			User user = new User();
			user.uid = userId;
			user.shopId = shopId;
			auditComponent.logAudit(user, Constants.LOGOUT);
			return Constants.STATUS_SUCCESS;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Constants.STATUS_FAILED;
	}

	abstract class LogTaskBasic extends AsyncTask<String, Void, Integer> {
		abstract Integer doExecute(String... objs);

		@Override
		protected Integer doInBackground(String... objs) {
			return doExecute(objs);
		}

		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			switch (result) {
			case Constants.STATUS_FAILED:
				toastComponent.show(stringResComponent.loginFail);
				break;
			case Constants.STATUS_SUCCESS:
				toastComponent.show(stringResComponent.loginSucc);
				break;
			case Constants.STATUS_SERVER_FAILED:
				toastComponent.show(stringResComponent.serviceErr);
				break;
			case Constants.STATUS_NETWORK_ERROR:
				toastComponent.show(stringResComponent.wifiErr);
				break;
			}
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	class Login extends LogTaskBasic {
		@Override
		Integer doExecute(String... objs) {
			return loginLocal(objs[0], objs[1], objs[2]);
		}
	}

	class Logout extends LogTaskBasic {
		@Override
		Integer doExecute(String... objs) {
			return logout(objs[0], objs[1]);
		}
	}

	public void setLoginInfo(User user) {
		myApp.setUsername(user.username);
		myApp.setUserId(user.uid);
		myApp.setUserType(user.usertype);
		myApp.setShopName(user.shopName);
		myApp.setShopCode(user.shopCode);

	}

	public User getDbUser(UserMapping.User user) {
		User obj = new User();
		obj.uid = user.id;
		obj.username = user.username;
		obj.usertype = user.usertype;
		// obj.realname = user.realname;
		// obj.password = user.password;
		// obj.status = user.status;
		if (user.shop != null) {
			obj.shopName = user.shop.name;
			obj.shopId = user.shop.id;
			obj.shopCode = user.shop.code;
		}
		return obj;
	}

}
