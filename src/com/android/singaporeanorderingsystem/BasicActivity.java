package com.android.singaporeanorderingsystem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.OrientationEventListener;

import com.android.R;
import com.android.bean.LoginAuditBean;
import com.android.bean.LoginUserBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.dao.LoginAuditDao;
import com.android.dao.UserDao2;
import com.android.dialog.DialogBuilder;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;

public class BasicActivity extends Activity {

	private MyApp myApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																																// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// init_wifiReceiver();
		// m=new MyOrientationDetector2(MainActivity.this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		myApp = (MyApp) BasicActivity.this.getApplication();
	}

	public void login_audit(LoginUserBean login_user, String action) {
		final LoginAuditDao dao = LoginAuditDao.getInatance(BasicActivity.this);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		LoginAuditBean login_audit = new LoginAuditBean();
		login_audit.setUser_id(login_user.getId());
		login_audit.setShop_id(login_user.getShop_id());
		login_audit.setActionDate(df.format(new Date()));
		login_audit.setAction(action);
		login_audit.setFood_flag("0");
		dao.save(login_audit);
		ArrayList<LoginAuditBean> u_datas = dao.getList("0");
		if (u_datas != null && u_datas.size() != 0) {
			HashMap<String, String> params = new HashMap<String, String>();
			for (int i = 0; i < u_datas.size(); i++) {
				LoginAuditBean login_a_bean = u_datas.get(i);
				params.put("audits[" + i + "].androidId", login_a_bean.getAndroid_id());
				params.put("audits[" + i + "].shop.id", login_a_bean.getShop_id());
				params.put("audits[" + i + "].user.id", login_a_bean.getUser_id());
				params.put("audits[" + i + "].actionDate", login_a_bean.getActionDate());
				params.put("audits[" + i + "].action", login_a_bean.getAction());
			}
			try {
				ResponseData data = RemoteDataHandler.post(Constants.URL_LOGIN_AUDIT, params);
				if (data.getCode() == 1) {
					dao.update_all_type("0");
				} else if (data.getCode() == 0) {
					String json = data.getJson();
					json = json.replaceAll("\\[", "");
					json = json.replaceAll("\\]", "");
					String[] str = json.split(",");
					for (int i = 0; i < str.length; i++) {
						dao.update_type(str[i]);
					}
				} else if (data.getCode() == -1) {

				}
			} catch (Exception e) {
				Log.e("error", "LogMessage", e);
			}
		}
	}

	public DialogBuilder CreatedDialog() {
		DialogBuilder builder = new DialogBuilder(this);
		builder.setTitle(R.string.message_title);
		builder.setMessage(R.string.message_exit);
		builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				logUserAction();
			}
		});
		builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return builder;
	}
	
	public void logUserAction(){
		final UserDao2 u_dao = UserDao2.getInatance(BasicActivity.this);
		LoginUserBean user_bean = new LoginUserBean();
		ArrayList<LoginUserBean> u_datas = null;
		if (StringUtils.equalsIgnoreCase("SUPERADMIN", myApp.getU_type())) {
			user_bean.setId(myApp.getUser_id());
		} else {
			u_datas = u_dao.getList(myApp.getU_name(), myApp.getSettingShopId());
			if (u_datas != null && u_datas.size() != 0) {
				user_bean = u_datas.get(0);
			}
		}
		login_audit(user_bean, "Logout");
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), LoginActivity.class);// 当前Activity重新打开
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		System.exit(0);
	
		
	}

}

class MyOrientationDetector extends OrientationEventListener {
	private Context context;

	public MyOrientationDetector(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void onOrientationChanged(int orientation) {
		if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
			return; // 手机平放时，检测不到有效的角度
		}
		// 只检测是否有四个角度的改变
		if (orientation > 350 || orientation < 10) { // 0度
			orientation = 0;
		} else if (orientation > 80 && orientation < 100) { // 90度
			orientation = 90;
			((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		} else if (orientation > 170 && orientation < 190) { // 180度
			orientation = 180;
		} else if (orientation > 260 && orientation < 280) { // 270度
			orientation = 270;
			((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			return;
		}
		Log.i("MyOrientationDetector ", "onOrientationChanged:" + orientation);
	}

}
