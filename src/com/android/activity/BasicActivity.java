package com.android.activity;

import com.android.R;
import com.android.component.ActivityComponent;
import com.android.component.StringResComponent;
import com.android.component.ui.MenuComponent;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;

@EActivity
public abstract class BasicActivity extends AbstractActivity {

	@Bean
	public MenuComponent menuComponent;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ActivityComponent activityComponent;

	/**
	 * @TODO: 最后放到基类里面去
	 * 
	 */
	@Click(R.id.menu_btn)
	public void menu() {
		menuComponent.initPopupWindow();
	}

}
