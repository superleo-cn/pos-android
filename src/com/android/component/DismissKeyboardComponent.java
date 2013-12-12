package com.android.component;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
@EBean(scope = Scope.Singleton)
public class DismissKeyboardComponent {

	@RootContext
	Context context;

	public void dismiss(View... objs) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (objs != null) {
			for (View view : objs) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}
	}

}
