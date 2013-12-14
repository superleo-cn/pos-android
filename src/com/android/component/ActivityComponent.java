package com.android.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.R;
import com.android.singaporeanorderingsystem.MainActivity_;
import com.android.singaporeanorderingsystem.SettingActivity_;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
// 定义成一个可以注入的组件
@EBean
public class ActivityComponent {

	// 注入 Context 变量
	@RootContext
	Context context;

	// 注入 Activity 变量
	@RootContext
	Activity activity;

	public ActivityComponent() {

	}

	@AfterInject
	public void init() {

	}

	public void startMain() {
		startActivity(MainActivity_.class);
	}

	public void startSetting() {
		startActivityWithTransaction(SettingActivity_.class);
	}

	public <T> void startActivityWithTransaction(Class<T> to) {
		Intent intent = new Intent(context, to);
		activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		Bundle bundle = new Bundle();
		bundle.putString("type", "1");
		intent.putExtras(bundle);
		activity.startActivity(intent);
		activity.finish();
	}

	public <T> void startActivity(Class<T> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		activity.startActivity(intent);
		activity.finish();
	}
}
