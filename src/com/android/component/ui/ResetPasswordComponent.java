package com.android.component.ui;

import org.apache.commons.lang.StringUtils;

import android.widget.EditText;

import com.android.R;
import com.android.common.MyApp;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.domain.User;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 重置密码操作
 * 
 * @author superleo
 * 
 */
@EBean
public class ResetPasswordComponent {

	@ViewById(R.id.edit_setting_chongzhi_login_name)
	EditText editSettingChongzhiLoginName;

	@ViewById(R.id.edit_setting_chongzhi_login_password)
	EditText editSettingChongzhiLoginPassword;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	@App
	MyApp myApp;

	// 查询用户名
	@Click(R.id.btu_setting_login_name)
	void checkLoginUser() {
		String login_name = editSettingChongzhiLoginName.getText().toString();
		User user = User.checkLogin(login_name, sharedPrefs.shopId().get());
		if (user != null) {
			editSettingChongzhiLoginPassword.setText(user.password);
			toastComponent.show("该用户信息存在");

		} else {
			toastComponent.show("该用户不存在，请输入正确的用户");
		}
	}

	// 更新密码
	@Click(R.id.btu_setting_login_password)
	void resetPassword() {
		String login_name = editSettingChongzhiLoginName.getText().toString();
		String login_password = editSettingChongzhiLoginPassword.getText().toString();
		if (StringUtils.isNotEmpty(login_name) && StringUtils.isNotEmpty(login_password)) {
			User user = User.checkLogin(login_name, sharedPrefs.shopId().get());
			if (user != null) {
				try {
					user.password = login_password;
					user.save();
					toastComponent.show("修改密码成功");
				} catch (Exception ex) {
					ex.printStackTrace();
					toastComponent.show("修改密码失败，稍后重试");
				}
			}
		} else {
			toastComponent.show("用户名或者密码不能为空");
		}

	}

}
