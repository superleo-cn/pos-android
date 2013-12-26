package com.android.singaporeanorderingsystem;

import android.view.MotionEvent;

import com.android.R;
import com.android.component.ui.DailyPayComponent;
import com.googlecode.androidannotations.annotations.Bean;
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
public class DailyPayActivity extends AbstractActivity {
	@Bean
	DailyPayComponent dailyPayComponent;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		dailyPayComponent.dismissKeyboard();
		return super.onTouchEvent(event);
	}
}
