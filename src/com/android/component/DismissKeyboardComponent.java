package com.android.component;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
public class DismissKeyboardComponent {

	final Context context;

	public DismissKeyboardComponent(Context context) {
		this.context = context;
	}

	public void onFocusChange(View v, boolean hasFocus) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(login_password.getWindowToken(), 0);
//		imm.hideSoftInputFromWindow(login_name.getWindowToken(), 0); // 强制隐藏键盘
//		return super.onTouchEvent(event);
	}

}
