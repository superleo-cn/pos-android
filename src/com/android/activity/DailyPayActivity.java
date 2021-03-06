package com.android.activity;

import android.view.MotionEvent;

import com.android.R;
import com.android.component.ui.MenuComponent;
import com.android.component.ui.daily.DailyPayComponent;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;

/**
 * @author jingang
 */
// 不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.daily_pay)
public class DailyPayActivity extends BasicActivity {

	@Bean
	DailyPayComponent dailyPayComponent;
	
	@Bean
	public MenuComponent menuComponent;

	/**
	 * @TODO: 最后放到基类里面去
	 * 
	 */
	@Click(R.id.menu_btn)
	public void menu() {
		menuComponent.initPopupWindow();
		menuComponent.textDianCai();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		dailyPayComponent.dismissKeyboard();
		return super.onTouchEvent(event);
	}
}
