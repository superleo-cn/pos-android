package com.android.activity;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.R;
import com.android.common.Constants;
import com.android.component.ActivityComponent;
import com.android.component.AppUpdateComponent;
import com.android.component.KeyboardComponent;
import com.android.component.LanguageComponent;
import com.android.component.ui.login.LoginComponent;
import com.android.service.MyService_;
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
public class LoginActivity extends AbstractActivity {

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
	AppUpdateComponent appUpdateComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	@Bean
	LanguageComponent languageComponent;

	@Bean
	ActivityComponent activityComponent;

	@Bean
	LoginComponent loginComponent;

	@AfterViews
	public void init() {
		// 启动相关组件
		startService(new Intent(this, MyService_.class));
		languageComponent.readLanguage();
		appUpdateComponent.updateApp();

	}

	void login(String userType) {
		String str_login_name = StringUtils.trim(login_name.getText().toString());
		String str_login_password = StringUtils.trim(login_password.getText().toString());
		loginComponent.executeLogin(str_login_name, str_login_password, userType);
		return;
	}

	@LongClick(R.id.image_logo_ico)
	void adminLogin() {
		login(Constants.ROLE_SUPERADMIN);
	}

	@Click(R.id.login_ok)
	void cashierLogin() {
		login(Constants.ROLE_CASHIER);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 要隐藏键盘的组件
		keyboardComponent.dismissKeyboard(login_name, login_password);
		return super.onTouchEvent(event);
	}

}