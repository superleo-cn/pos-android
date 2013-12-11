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
public class DismissKeyboardComponent implements OnFocusChangeListener {

	final Context context;

	public DismissKeyboardComponent(Context context) {
		this.context = context;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
		}
	}

}
