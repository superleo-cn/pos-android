package com.android.component;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
@EBean(scope = Scope.Singleton)
public class EmailComponent {

	@RootContext
	Activity activity;

	@Bean
	WifiComponent wifiComponent;

	protected void sendEmail() {
		Log.d("[EmailComponent]", "开始发送邮件");

		String[] TO = { "amrood.admin@gmail.com" };
		String[] CC = { "mcmohd@gmail.com" };
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");

		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

		try {
			activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			activity.finish();
		} catch (Exception ex) {
			Log.e("[EmailComponent]", "邮件发送失败", ex);
		}
	}
}
