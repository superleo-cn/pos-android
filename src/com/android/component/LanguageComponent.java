package com.android.component;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
// 定义成一个可以注入的组件
@EBean(scope = Scope.Singleton)
public class LanguageComponent {

	// 注入 Context 变量
	@RootContext
	Context context;

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@Bean
	ActivityComponent activityComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	StringResComponent stringResComponent;

	public void readLanguage() {
		String type = myPrefs.language().get();
		if (StringUtils.equalsIgnoreCase(type, "zh")) {
			readLanguage(Locale.SIMPLIFIED_CHINESE);
		} else {
			readLanguage(Locale.ENGLISH);
		}
	}

	private void readLanguage(Locale locale) {
		Resources res = context.getResources();
		Configuration config = res.getConfiguration();
		config.locale = locale;
		DisplayMetrics dm = res.getDisplayMetrics();
		res.updateConfiguration(config, dm);

	}

	public <T> void updateLanguage(Class<T> t, String type, Locale locale) {
		Resources res = context.getResources();
		Configuration config = res.getConfiguration();
		config.locale = locale;
		DisplayMetrics dm = res.getDisplayMetrics();
		res.updateConfiguration(config, dm);
		toastComponent.showLong(stringResComponent.toastSettingSucc);
		activityComponent.updateActivity(t, type);

	}

}
