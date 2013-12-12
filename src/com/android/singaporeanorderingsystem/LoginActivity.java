package com.android.singaporeanorderingsystem;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.R;
import com.android.common.Constants;
import com.android.component.AppUpdateComponent;
import com.android.component.AuditComponent;
import com.android.component.DismissKeyboardComponent;
import com.android.component.LanguageComponent;
import com.android.component.LoginComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.LongClick;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

//不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.login)
public class LoginActivity extends Activity {

	// 通过id绑定登录按钮
	@ViewById(R.id.login_ok)
	Button login_ok;

	// 通过id绑定用户名
	@ViewById(R.id.login_name)
	EditText login_name;

	// 通过id绑定密码
	@ViewById(R.id.login_password)
	EditText login_password;

	// 通过id绑定管理员登录图片
	@ViewById(R.id.image_logo_ico)
	ImageView image_logo_ico;

	@Bean
	LoginComponent loginComponent;

	@Bean
	AppUpdateComponent appUpdateComponent;

	@Bean
	LanguageComponent languageComponent;

	@Bean
	DismissKeyboardComponent dismissKeyboardComponent;


	@AfterViews
	public void init() {
		// 启动相关组件
		appUpdateComponent.updateApp();
		languageComponent.updateLange();
	}

	void login(String loginType) {
		String str_login_name = StringUtils.trim(login_name.getText().toString());
		String str_login_password = StringUtils.trim(login_password.getText().toString());
		loginComponent.executeLogin(str_login_name, str_login_password, loginType);
		return;
	}

	@LongClick({ R.id.image_logo_ico })
	void adminLogin() {
		login(Constants.ROLE_SUPERADMIN);
	}

	@Click({ R.id.login_ok })
	void cashierLogin() {
		login(Constants.ROLE_CASHIER);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 要隐藏键盘的组件
		dismissKeyboardComponent.dismiss(login_name, login_password);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		return;
	}

}