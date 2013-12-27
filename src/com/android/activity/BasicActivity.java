package com.android.activity;

import android.app.Dialog;
import android.content.Context;

import com.android.R;
import com.android.component.StringResComponent;
import com.android.component.ui.MenuComponent;
import com.android.dialog.ConfirmDialog;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.RootContext;

@EActivity
public abstract class BasicActivity extends AbstractActivity {

	@RootContext
	Context context;

	@Bean
	public MenuComponent menuComponent;

	@Bean
	StringResComponent stringResComponent;

	/**
	 * @TODO: 最后放到基类里面去
	 * 
	 */
	@Click(R.id.menu_btn)
	public void menu() {
		menuComponent.initPopupWindow();
	}

	@Click(R.id.layout_exit)
	void layoutExitOnClick() {
		buildExitDialog().show();
	}

	/**
	 * 退出系统操作
	 * 
	 * @return
	 */
	public Dialog buildExitDialog() {
		return new ConfirmDialog(context, stringResComponent.messageTitle, stringResComponent.messageExit) {

			@Override
			public void doClick() {
				loginComponent.executeLogout(myApp.getUserId(), myApp.getShopId());
			}

		}.build();
	}

}
