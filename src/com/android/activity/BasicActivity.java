package com.android.activity;

import android.content.DialogInterface;

import com.android.R;
import com.android.component.ui.MenuComponent;
import com.android.dialog.design.DialogBuilder;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;

@EActivity
public abstract class BasicActivity extends AbstractActivity {

	@Bean
	public MenuComponent menuComponent;

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
		CreatedDialog().create().show();
	}

	/**
	 * 退出系统操作
	 * 
	 * @return
	 */
	public DialogBuilder CreatedDialog() {
		DialogBuilder builder = new DialogBuilder(this);
		builder.setTitle(R.string.message_title);
		builder.setMessage(R.string.message_exit);
		builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				loginComponent.executeLogout(myApp.getUserId(), myApp.getShopId());

			}
		});
		builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return builder;
	}

}
