package com.android.activity;

import com.android.R;
import com.android.component.ui.main.CalculatorComponent;
import com.android.component.ui.main.FoodComponent;
import com.android.component.ui.main.OrderComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;

//不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.activity_main)
public class MainActivity extends BasicActivity {

	@Bean
	FoodComponent foodComponent;

	@Bean
	OrderComponent orderComponent;

	@Bean
	CalculatorComponent calculatorComponent;
	
	@AfterViews
	public void init() {
		// 手动依赖注入，解决循环问题
		foodComponent.setOrderComponent(orderComponent);
		calculatorComponent.setOrderComponent(orderComponent);
		orderComponent.setFoodComponent(foodComponent);
		orderComponent.setCalculatorComponent(calculatorComponent);
	}

}
