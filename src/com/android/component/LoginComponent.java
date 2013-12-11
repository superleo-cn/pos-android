package com.android.component;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.R;
import com.android.bean.LoginUserBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.common.SystemHelper;
import com.android.dao.UserDao2;
import com.android.handler.RemoteDataHandler;
import com.android.model.ResponseData;
import com.android.singaporeanorderingsystem.MainActivity;
import com.android.singaporeanorderingsystem.MyProcessDialog;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
public class LoginComponent {

	private Context context;
	private Activity activity;
	private MyProcessDialog dialog;
	private MyApp myApp;

	public LoginComponent(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
		myApp = (MyApp) this.activity.getApplication();
		dialog = new MyProcessDialog(context, this.context.getResources().getString(R.string.logout_wait));
	}

	public void executeLogin(String username, String password, String loginType) {
		new LoginTask().execute(new String[] { username, password, loginType });
	}

	private Integer loginLocal(String username, String password, String loginType) {
		String str_login_name = username;
		final String str_login_password = password;
		String str_ip = "0";
		String str_mac = "0";
		try {
			str_ip = SystemHelper.getLocalIPAddress() == null ? "0" : SystemHelper.getLocalIPAddress();
			str_mac = SystemHelper.getLocalMacAddress(activity) == null ? "0" : SystemHelper.getLocalMacAddress(activity);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		if (!StringUtils.equalsIgnoreCase("SUPERADMIN", loginType)) {
			// final UserDao user_dao=myApp.getUserdao();
			// LoginUserBean user_bean = user_dao.select(str_login_name);
			final UserDao2 userDao = UserDao2.getInatance(context);
			ArrayList<LoginUserBean> u_datas = userDao.getList(str_login_name, myApp.getSettingShopId());
			LoginUserBean user_bean = new LoginUserBean();
			if (CollectionUtils.isNotEmpty(u_datas)) {
				user_bean = u_datas.get(0);
			}
			if (StringUtils.isNotEmpty(user_bean.getUsername())) {
				if (StringUtils.equalsIgnoreCase(str_login_password, user_bean.getPasswrod())) {
					setLoginInfo(user_bean);
					// login_audit(user_bean, "Login");
					startMain();
					return 1;
				} else {
					return 0;
				}

			}

		}
		boolean wifi_flag = SystemHelper.isConnected(activity);
		if (wifi_flag) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("user.username", str_login_name);
			params.put("user.password", str_login_password);
			params.put("user.shop.id", myApp.getSettingShopId());
			params.put("user.userIp", str_ip);
			params.put("user.userMac", str_mac);
			return loginRemote(loginType, params);
		}
		return -2;
	}

	private Integer loginRemote(String loginType, Map<String, String> params) {
		if (StringUtils.equalsIgnoreCase(loginType, "SUPERADMIN")) {
			return loginRemote(Constants.URL_LOGIN_ADMIN_PATH, loginType, params);
		} else {
			return loginRemote(Constants.URL_LOGIN_PATH, loginType, params);
		}
	}

	private Integer loginRemote(String url, String loginType, Map<String, String> params) {
		ResponseData data = RemoteDataHandler.post(url, params);
		if (data.getCode() == 1) {
			String json = data.getJson();
			ArrayList<LoginUserBean> datas = LoginUserBean.newInstanceList(json);
			LoginUserBean userBean = datas.get(0);
			// 如果是SUPERADMIN登录，而且登录的用户却没有SUPERADMIN的权限，则禁止登录
			if (!StringUtils.equalsIgnoreCase(userBean.getUsertype(), "SUPERADMIN")
					&& StringUtils.equalsIgnoreCase(loginType, "SUPERADMIN")) {
				Toast.makeText(context, context.getString(R.string.login_quanxian), Toast.LENGTH_SHORT).show();
				return 0;
			}
			// login_audit(user_bean, "Login");
			setLoginInfo(userBean);
			startMain();
			return 1;
		} else if (data.getCode() == 0) {
			return 0;
		} else if (data.getCode() == -1) {
			return -1;
		}
		return -2;
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
			case 0:
				Toast.makeText(activity, context.getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(activity, context.getString(R.string.login_succ), Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(activity, context.getString(R.string.login_service_err), Toast.LENGTH_SHORT).show();
				break;
			case -2:
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

	public void setLoginInfo(LoginUserBean user_bean) {
		myApp.setU_name(user_bean.getUsername());
		myApp.setUser_id(user_bean.getId());
		myApp.setU_type(user_bean.getUsertype());
		myApp.setShop_name(user_bean.getShop_name());
		myApp.setShop_code(user_bean.getShop_code());
	}

}
