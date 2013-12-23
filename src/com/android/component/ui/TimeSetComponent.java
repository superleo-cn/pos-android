package com.android.component.ui;

import android.text.TextUtils;
import android.widget.EditText;

import com.android.R;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
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

	@Click(R.id.edit_setting_time)
	public void setTime() {
		String setTime = editSettingTime.getText().toString();
		if (!TextUtils.isEmpty(setTime) && !setTime.equals("null")) {
			keyboardComponent.dismissKeyboard(editSettingTime);
			toastComponent.show(stringResComponent.toastSettingSucc);
			myPrefs.time().put(Long.parseLong(setTime) * 60 * 1000);
		}
	}

}
