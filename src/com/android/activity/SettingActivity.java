package com.android.activity;

import android.view.MotionEvent;

import com.android.R;
import com.android.common.MyApp;
import com.android.component.StringResComponent;
import com.android.component.ui.setting.AccessRightComponent;
import com.android.component.ui.setting.CollectionSynchronizationComponent;
import com.android.component.ui.setting.DiscountSetComponent;
import com.android.component.ui.setting.ExpenseSynchronizationComponent;
import com.android.component.ui.setting.FoodSynchronizationComponent;
import com.android.component.ui.setting.LanguageSetComponent;
import com.android.component.ui.setting.PrintSetComponent;
import com.android.component.ui.setting.ResetPasswordComponent;
import com.android.component.ui.setting.ShopSynchronizationComponent;
import com.android.component.ui.setting.SynchronizationStatusComponent;
import com.android.component.ui.setting.TimeSetComponent;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;

//不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.setting)
public class SettingActivity extends BasicActivity {

	@Bean
	StringResComponent stringResComponent;

	// 状态同步组件
	@Bean
	SynchronizationStatusComponent synchronizationStatusComponent;

	// 时间组件
	@Bean
	TimeSetComponent timeSetComponent;

	// 打印机组件
	@Bean
	PrintSetComponent printSetComponent;

	// 打折组件
	@Bean
	DiscountSetComponent discountSetComponent;

	// 摊位组件
	@Bean
	ShopSynchronizationComponent shopSynchronizationComponent;

	// 语言组件
	@Bean
	LanguageSetComponent languageSetComponent;

	// 食物组件
	@Bean
	FoodSynchronizationComponent foodSynchronizationComponent;

	// 支付款项组件
	@Bean
	ExpenseSynchronizationComponent expenseSynchronizationComponent;

	// 带回总数组件
	@Bean
	CollectionSynchronizationComponent collectionSynchronizationComponent;

	// 设置密码
	@Bean
	ResetPasswordComponent resetPasswordComponent;

	// 权限设置组件
	@Bean
	AccessRightComponent accessRightComponent;

	@App
	MyApp myApp;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		timeSetComponent.dissmissKeyboard();
		printSetComponent.dissmissKeyboard();
		discountSetComponent.dissmissKeyboard();
		shopSynchronizationComponent.dissmissKeyboard();
		languageSetComponent.dissmissKeyboard();
		resetPasswordComponent.dissmissKeyboard();
		return super.dispatchTouchEvent(event);
	}

}
