package com.android.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.singaporeanorderingsystem.MainActivity_;
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

	public <T> void startActivity(Class<T> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		context.startActivity(intent);
		activity.finish();
	}
}
