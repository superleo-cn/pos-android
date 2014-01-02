package com.android.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.android.activity.DailyPayActivity_;
import com.android.activity.LoginActivity_;
import com.android.activity.MainActivity_;
import com.android.activity.QueryAllDBActivity_;
import com.android.activity.SettingActivity_;
import com.android.dialog.MyProcessDialog;
import com.googlecode.androidannotations.annotations.Bean;
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

	@Bean
	StringResComponent stringResComponent;

	// 注入 Context 变量
	@RootContext
	Context context;

	// 注入 Activity 变量
	@RootContext
	Activity activity;

	MyProcessDialog dialog;

	public void startLogin() {
		startActivity(LoginActivity_.class);
	}

	public void startMain() {
		startActivityWithTransaction(MainActivity_.class);
	}

	public void startSetting() {
		startActivityWithTransaction(SettingActivity_.class);
	}

	public void startMainWithTransition() {
		startActivityWithTransaction(MainActivity_.class);
	}

	public void startDaily() {
		startActivityWithTransaction(DailyPayActivity_.class);
	}

	public void startQuery() {
		startActivityWithTransaction(QueryAllDBActivity_.class);
	}

	private <T> void startActivityWithTransaction(Class<T> to) {
		startActivity(to);
	}

	public <T> void startActivity(Class<T> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		activity.startActivity(intent);
		activity.finish();
	}

	class SwitchActivity extends AsyncTask<Class, Void, Integer> {

		@Override
		protected Integer doInBackground(Class... cls) {
			startActivity(cls[0]);
			return Integer.MIN_VALUE;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			dialog = new MyProcessDialog(context, stringResComponent.loginWait);
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

}
