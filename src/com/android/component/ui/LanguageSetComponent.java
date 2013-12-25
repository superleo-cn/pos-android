package com.android.component.ui;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.widget.EditText;

import com.android.R;
import com.android.component.ActivityComponent;
import com.android.component.KeyboardComponent;
import com.android.component.LanguageComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
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

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@ViewById(R.id.language_set)
	EditText languageSet;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	LanguageComponent languageComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	@Bean
	ActivityComponent activityComponent;

	// 语言设置
	@Click(R.id.language_set)
	public void setLanguage() {
		String type = myPrefs.language().get();
		if (StringUtils.equalsIgnoreCase(type, Locale.SIMPLIFIED_CHINESE.getLanguage())) {
			languageComponent.updateLanguage(Locale.ENGLISH);
			languageSet.setText("English");
		} else {
			languageComponent.updateLanguage(Locale.SIMPLIFIED_CHINESE);
			languageSet.setText("中文");
		}
		activityComponent.startSettingWithTransition();
	}

	// 初始化数据
	@AfterViews
	public void initLanguage() {
		if (StringUtils.equals(myPrefs.language().get(), Locale.SIMPLIFIED_CHINESE.getLanguage())) {
			languageSet.setText("中文");
		} else {
			languageSet.setText("English");
		}
	}

	public void dissmissKeyboard() {
		keyboardComponent.dismissKeyboard(languageSet);
	}

}
