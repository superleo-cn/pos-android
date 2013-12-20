package com.android.singaporeanorderingsystem;

import android.app.Activity;
import android.widget.ImageView;

import com.android.R;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.ui.MenuComponent;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public abstract class AbsractActivity extends Activity {

	@Bean
	public MenuComponent menuComponent;

	@ViewById(R.id.menu_btn)
	ImageView menu; // menu按钮

	@Pref
	public SharedPreferencesComponent_ sharedPrefs;

	/**
	 * @TODO: 最后放到基类里面去
	 * 
	 */
	@Click(R.id.menu_btn)
	public void menu() {
		menuComponent.initPopupWindow();
	}

	/**
	 * 屏蔽回退按钮
	 */
	public void onBackPressed() {
		return;
	}

}
