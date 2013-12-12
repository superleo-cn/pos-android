package com.android.component;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.R;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.common.SystemHelper;
import com.android.domain.User;
import com.android.mapping.UserMapping;
import com.android.singaporeanorderingsystem.MainActivity;
import com.android.singaporeanorderingsystem.MyProcessDialog;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.res.StringRes;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
@EBean
public class LoginComponent {

	// 注入 MyApp
	@App
	MyApp myApp;

	// 注入 Context 变量
	@RootContext
	Context context;

	// 注入 Activity 变量
	@RootContext
	Activity activity;

	MyProcessDialog dialog;

	@StringRes(R.string.login_wait)
	String title;

	@AfterInject
	public void init() {
		dialog = new MyProcessDialog(context, title);
	}

	public void executeLogin(String username, String password, String loginType) {
		new LoginTask().execute(new String[] { username, password, loginType });
	}

	private Integer loginLocal(String username, String password, String loginType) {
		// 得到IP和Mac地址
		String str_ip = WifiComponent.getIPAddress();
		String str_mac = WifiComponent.getMacAddress(context);

		if (!StringUtils.equalsIgnoreCase("SUPERADMIN", loginType)) {
			User user = User.checkLogin(username, myApp.getSettingShopId());
			if (user != null) {
				if (StringUtils.equalsIgnoreCase(password, user.password)) {
					setLoginInfo(user);
					// login_audit(user_bean, "Login");
					startMain();
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
			params.put("user.shop.id", myApp.getSettingShopId());
			params.put("user.userIp", str_ip);
			params.put("user.userMac", str_mac);
			return loginRemote(loginType, params);
		}
		return Constants.STATUS_NETWORK_ERROR;
	}

	private Integer loginRemote(String loginType, Map<String, String> params) {
		if (StringUtils.equalsIgnoreCase(loginType, "SUPERADMIN")) {
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
			if (!StringUtils.equalsIgnoreCase(remoteUser.usertype, "SUPERADMIN") && StringUtils.equalsIgnoreCase(loginType, "SUPERADMIN")) {
				Toast.makeText(context, context.getString(R.string.login_quanxian), Toast.LENGTH_SHORT).show();
				return Constants.STATUS_FAILED;
			}
			// login_audit(user_bean, "Login");
			if (!StringUtils.equalsIgnoreCase("SUPERADMIN", loginType)) {
				User user = new User();
				user.uid = remoteUser.id;
				user.username = remoteUser.username;
				user.password = params.get("user.password");
				user.usertype = remoteUser.usertype;
				user.status = remoteUser.status;
				user.shopId = remoteUser.shop.id;
				user.shopName = remoteUser.shop.name;
				user.shopCode = remoteUser.shop.code;
				user.save();
				setLoginInfo(user);
			} else {
				setLoginInfo(remoteUser);
			}
			// 启动主窗口
			startMain();
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
				Toast.makeText(activity, context.getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
				break;
			case Constants.STATUS_SUCCESS:
				Toast.makeText(activity, context.getString(R.string.login_succ), Toast.LENGTH_SHORT).show();
				break;
			case Constants.STATUS_SERVER_FAILED:
				Toast.makeText(activity, context.getString(R.string.login_service_err), Toast.LENGTH_SHORT).show();
				break;
			case Constants.STATUS_NETWORK_ERROR:
				Toast.makeText(activity, context.getString(R.string.login_wifi_err), Toast.LENGTH_SHORT).show();
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

	public void startMain() {
		Intent intent = new Intent();
		intent.setClass(context, MainActivity.class);
		context.startActivity(intent);
		activity.finish();
	}

	public void setLoginInfo(User user) {
		myApp.setU_name(user.username);
		myApp.setUser_id(user.uid);
		myApp.setU_type(user.usertype);
		myApp.setShop_name(user.shopName);
		myApp.setShop_code(user.shopCode);
	}

	public void setLoginInfo(UserMapping.User user) {
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
		setLoginInfo(user);
	}

}
