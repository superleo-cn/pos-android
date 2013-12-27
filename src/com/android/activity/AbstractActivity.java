package com.android.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.StrictMode;

import com.android.R;
import com.android.common.MyApp;
import com.android.component.AuditComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.ui.MenuComponent;
import com.android.component.ui.login.LoginComponent;
import com.android.dialog.design.DialogBuilder;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public abstract class AbstractActivity extends Activity {

	@Bean
	public MenuComponent menuComponent;

	@Pref
	public SharedPreferencesComponent_ sharedPrefs;

	@Bean
	AuditComponent auditComponent;

	@Bean
	LoginComponent loginComponent;

	@App
	MyApp myApp;

	@AfterInject
	public void initAbstract() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																																// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());

	}

	/**
	 * @TODO: 最后放到基类里面去
	 * 
	 */
	@Click(R.id.menu_btn)
	public void menu() {
		menuComponent.initPopupWindow();
	}

	@Click(R.id.layout_exit)
	void layoutExitOnClick() {
		CreatedDialog().create().show();
	}

	/**
	 * 退出系统操作
	 * 
	 * @return
	 */
	public DialogBuilder CreatedDialog() {
		DialogBuilder builder = new DialogBuilder(this);
		builder.setTitle(R.string.message_title);
		builder.setMessage(R.string.message_exit);
		builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				loginComponent.executeLogout(myApp.getUserId(), myApp.getShopId());

			}
		});
		builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return builder;
	}

	/**
	 * 屏蔽回退按钮
	 */
	public void onBackPressed() {
		return;
	}

}
