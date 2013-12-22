package com.android.component.ui;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.widget.EditText;

import com.android.R;
import com.android.common.AndroidPrinter;
import com.android.component.KeyboardComponent;
import com.android.component.LanguageComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 打印机IP设置
 * 
 * @author Administrator
 * 
 */
@EBean
public class LanguageSetComponent {

	@RootContext
	Context context;

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@ViewById(R.id.language_set)
	EditText language_set;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	AndroidPrinter androidPrinter;

	@Bean
	KeyboardComponent keyboardComponent;

	@Bean
	LanguageComponent languageComponent;

	// 语言设置
	@Click(R.id.language_set)
	public void setLanguage() {
		String type = myPrefs.language().get();
		if (StringUtils.equalsIgnoreCase(type, Locale.SIMPLIFIED_CHINESE.getLanguage())) {
			languageComponent.updateLanguage(Locale.SIMPLIFIED_CHINESE);
			language_set.setText("中文");
		} else {
			languageComponent.updateLanguage(Locale.ENGLISH);
			language_set.setText("English");
		}
		toastComponent.show(stringResComponent.toastSettingLanguageSucc);
	}

	// 初始化数据
	@AfterViews
	public void initLanguage() {
		if (StringUtils.equals(myPrefs.language().get(), Locale.SIMPLIFIED_CHINESE.getLanguage())) {
			language_set.setText("中文");
		} else {
			language_set.setText("English");
		}
	}

}
