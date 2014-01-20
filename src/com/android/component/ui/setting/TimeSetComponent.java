package com.android.component.ui.setting;

import org.apache.commons.lang.StringUtils;

import android.widget.EditText;

import com.android.R;
import com.android.component.KeyboardComponent;
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
 * 时间设置
 * 
 * @author Administrator
 * 
 */
@EBean
public class TimeSetComponent {

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@ViewById(R.id.edit_setting_time)
	EditText editSettingTime;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	@Bean
	ToastComponent toastComponent;

	// 初始化数据
	@AfterViews
	public void initTimeSet() {
		editSettingTime.setText(String.valueOf(myPrefs.time().get() / 1000));
	}

	@Click(R.id.btu_setting_time)
	public void setTime() {
		String setTime = editSettingTime.getText().toString();
		if (StringUtils.isNotEmpty(setTime)) {
			dissmissKeyboard();
			toastComponent.show(stringResComponent.toastSettingSucc);
			myPrefs.time().put(Long.parseLong(setTime) * 1000);
		}
	}

	public void dissmissKeyboard() {
		keyboardComponent.dismissKeyboard(editSettingTime);
	}

}
