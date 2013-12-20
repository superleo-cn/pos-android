package com.android.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.R;
import com.android.singaporeanorderingsystem.DailyPayActivity_;
import com.android.singaporeanorderingsystem.LoginActivity_;
import com.android.singaporeanorderingsystem.MainActivity_;
import com.android.singaporeanorderingsystem.SettingActivity_;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

/**
 * 更新组件，因为要调用activey类，所以没法做成Singleton
 * 
 * @author superleo
 * 
 */
@EBean
public class ActivityComponent {

	// 注入 Context 变量
	@RootContext
	Context context;

	// 注入 Activity 变量
	@RootContext
	Activity activity;

	public void startLogin() {
		startActivity(LoginActivity_.class);
	}

	public void startMain() {
		startActivity(MainActivity_.class);
	}

	public void startSettingWithTransition() {
		startActivityWithTransaction(SettingActivity_.class);
	}

	public void startMainWithTransition() {
		startActivityWithTransaction(MainActivity_.class);
	}

	public void startDailyWithTransition() {
		startActivityWithTransaction(DailyPayActivity_.class);
	}

	public <T> void startActivityWithTransaction(Class<T> to) {
		Intent intent = new Intent(context, to);
		activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		// Bundle bundle = new Bundle();
		// bundle.putString("type", "1");
		// intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.finish();
	}

	public <T> void startActivity(Class<T> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		activity.startActivity(intent);
		activity.finish();
	}

	public <T> void updateActivity(Class<T> t) {
		Intent intent = new Intent();
		intent.setClass(activity, t);// 当前Activity重新打开
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// Bundle bundle = new Bundle();
		// bundle.putString("type", type);
		// intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.finish();

	}
}
