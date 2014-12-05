package com.android.activity;

import android.app.Activity;
import android.os.StrictMode;
import android.view.WindowManager;

import com.android.common.CrashHandler;
import com.android.common.MyApp;
import com.android.component.SharedPreferencesComponent_;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public abstract class AbstractActivity extends Activity {

	@Pref
	public SharedPreferencesComponent_ sharedPrefs;

	@App
	MyApp myApp;

	@Bean
	CrashHandler crashHandler;

	@AfterInject
	public void initAbstract() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																																// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		crashHandler.init(this); // 传入参数必须为Activity，否则AlertDialog将不显示。
	}

	@AfterViews
	public void initAbstractViews() {
		crashHandler.init(this); // 传入参数必须为Activity，否则AlertDialog将不显示。
	}

	/**
	 * 屏蔽回退按钮
	 */
	public void onBackPressed() {
		return;
	}

}
