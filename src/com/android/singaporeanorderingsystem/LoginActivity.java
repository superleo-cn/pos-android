package com.android.singaporeanorderingsystem;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.R;
import com.android.common.SystemHelper;
import com.android.component.AppUpdateComponent;
import com.android.component.DismissKeyboardComponent;
import com.android.component.LanguageComponent;
import com.android.component.LoginComponent;
import com.android.domain.User;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.LongClick;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

@NoTitle
@Fullscreen
@EActivity(R.layout.login)
public class LoginActivity extends Activity {

	@ViewById(R.id.login_ok)
	Button login_ok;

	@ViewById(R.id.login_name)
	EditText login_name;

	@ViewById(R.id.login_password)
	EditText login_password;

	@ViewById(R.id.image_logo_ico)
	ImageView image_logo_ico;

	private LoginComponent loginComponent;

	void login(String loginType) {
		String str_login_name = login_name.getText().toString();
		String str_login_password = login_password.getText().toString();
		loginComponent.executeLogin(str_login_name, str_login_password, loginType);
		return;
	}

	@LongClick({ R.id.image_logo_ico })
	void adminLogin() {
		login("SUPERADMIN");
	}

	@Click({ R.id.login_ok })
	void cashierLogin() {
		login("CASHIER");
	}

	@AfterViews
	public void init() {
		// 启动相关组件
		new AppUpdateComponent(LoginActivity.this).updateApp();
		new LanguageComponent(LoginActivity.this).updateLange();
		loginComponent = new LoginComponent(LoginActivity.this, LoginActivity.this);

		// 初始化系统UI
		login_name.setOnFocusChangeListener(new DismissKeyboardComponent(LoginActivity.this));
		login_password.setOnFocusChangeListener(new DismissKeyboardComponent(LoginActivity.this));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(login_password.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(login_name.getWindowToken(), 0); // 强制隐藏键盘
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