package com.android.component;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
public class LanguageComponent {

	final Context context;
	private SharedPreferences sharedPrefs;

	public LanguageComponent(Context context) {
		this.context = context;
		sharedPrefs = context.getSharedPreferences("language", Context.MODE_PRIVATE);
	}

	public void updateLange() {
		String type = sharedPrefs.getString("type", "zh");
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
