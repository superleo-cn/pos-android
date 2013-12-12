package com.android.component;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
// 定义成一个可以注入的组件
@EBean
public class LanguageComponent {

	// 注入 Context 变量
	@RootContext
	Context context;

	// 注入 Activity 变量
	@RootContext
	Activity activity;

	@Pref
	SharedPreferencesComponent_ myPrefs;

	public LanguageComponent() {

	}

	@AfterInject
	public void init() {
	}

	public void updateLange() {
		String type = myPrefs.language().get();
		if (StringUtils.equalsIgnoreCase(type, "zh")) {
			updateLange(Locale.SIMPLIFIED_CHINESE);
		} else {
			updateLange(Locale.ENGLISH);
		}
	}

	private void updateLange(Locale locale) {
		Resources res = context.getResources();
		Configuration config = res.getConfiguration();
		config.locale = locale;
		DisplayMetrics dm = res.getDisplayMetrics();
		res.updateConfiguration(config, dm);

	}

}
