package com.android.component;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.common.SystemHelper;
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

	public void executeLogin(String username, String password, String loginType) {
		new LoginTask().execute(new String[] { username, password, loginType });
	}

	private Integer loginLocal(String username, String password, String loginType) {
		// 得到IP和Mac地址
		String strIP = wifiComponent.getIPAddress();
		String strMAC = wifiComponent.getMacAddress(context);

		if (!StringUtils.equalsIgnoreCase(Constants.ROLE_SUPERADMIN, loginType)) {
			User user = User.checkLogin(username, myPrefs.shopId().get());
			if (user != null) {
				if (StringUtils.equalsIgnoreCase(password, user.password)) {
					setLoginInfo(user);
					auditComponent.logAudit(user, "Login");
					// 启动主窗口
					activityComponent.startMain();
					return Constants.STATUS_SUCCESS;
				} else {
					return Constants.STATUS_FAILED;
				}

			}
		}
		boolean wifi_flag = SystemHelper.isConnected(activity);
		if (wifi_flag) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("user.username", username);
			params.put("user.password", password);
			params.put("user.shop.id", myPrefs.shopId().get());
			params.put("user.userIp", strIP);
			params.put("user.userMac", strMAC);
			return loginRemote(loginType, params);
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
				toastComponent.show(stringResComponent.loginQuanxian);
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

	private class LoginTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... objs) {
			return loginLocal(objs[0], objs[1], objs[2]);
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

	public void setLoginInfo(User user) {
		myApp.setU_name(user.username);
		myApp.setUser_id(user.uid);
		myApp.setU_type(user.usertype);
		myApp.setShop_name(user.shopName);
		myApp.setShop_code(user.shopCode);

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
