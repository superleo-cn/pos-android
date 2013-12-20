package com.android.singaporeanorderingsystem;

import android.widget.RelativeLayout;

import com.android.R;
import com.android.component.ui.FoodComponent;
import com.android.component.ui.OrderComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

//不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.activity_main)
public class MainActivity extends AbsractActivity {

	@ViewById(R.id.layout_exit)
	RelativeLayout exit_layout; // 退出

	@ViewById(R.id.r_lay_id_take_package)
	RelativeLayout r_lay_id_take_package;

	@ViewById(R.id.r_lay_id_foc)
	RelativeLayout r_lay_id_foc;

	@ViewById(R.id.r_lay_id_discount)
	RelativeLayout r_lay_id_discount;

	@Bean
	FoodComponent foodComponent;

	@Bean
	OrderComponent orderComponent;

	// @FragmentById(R.id.main_left_fragment)
	// MainLeftFragment mainLeftFragment;
	//
	// @FragmentById(R.id.main_right_fragment)
	// MainRightFragment mainRightFragment;

	@AfterViews
	public void init() {
		// mainLeftFragment.setOrderComponent(orderComponent);
		foodComponent.setOrderComponent(orderComponent);
		orderComponent.setFoodComponent(foodComponent);
	}

	// TODO: 暂时不用
	// CreatedDialog().create().show();

}
