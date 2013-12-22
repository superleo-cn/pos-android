/**
 * 
 */
package com.android.singaporeanorderingsystem;

import java.util.HashMap;
import java.util.Map;

import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.R;
import com.android.component.ui.DailyPayComponent;
import com.android.component.ui.MenuComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author jingang
 * 
 */
// 不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.daily_pay)
public class DailyPayActivity extends AbsractActivity {

	@ViewById(R.id.text_id_all_price)
	TextView text_id_all_price;

	@ViewById(R.id.btu_id_sbumit)
	Button btu_id_sbumit;

	@ViewById(R.id.cash_register)
	EditText cash_register;

	@ViewById(R.id.today_turnover)
	EditText today_turnover;

	@ViewById(R.id.noon_time)
	EditText noon_time;

	@ViewById(R.id.noon_turnover)
	EditText noon_turnover;

	@ViewById(R.id.time)
	EditText time;

	@ViewById(R.id.total)
	EditText total;

	@ViewById(R.id.tomorrow_money)
	EditText tomorrow_money;

	@ViewById(R.id.total_take_num)
	EditText total_take_num;

	@ViewById(R.id.other)
	EditText other;

	@ViewById(R.id.shop_money)
	EditText shop_money;

	@ViewById(R.id.take_all_price)
	TextView take_all_price;

	@Bean
	DailyPayComponent dailyPayComponent;

	@Bean
	MenuComponent menuComponent;

	public static Map<Integer, String> hashMap_detail = new HashMap<Integer, String>();
	public static Map<Integer, String> hashMap_num = new HashMap<Integer, String>();
	public static Map<Integer, String> hashMap_numprice = new HashMap<Integer, String>();

	@AfterViews
	public void init() {
		dailyPayComponent.initView();
	}

	@Click(R.id.btu_id_sbumit)
	void sbumitOnClick() {
		dailyPayComponent.CreatedSubmitDialog().create().show();
	}

	@Click(R.id.menu_btn)
	void menuBtnOnClick() {
		menuComponent.initPopupWindow();
	}

	@Click(R.id.layout_exit)
	void layoutExitOnClick() {
		CreatedDialog().create().show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		dailyPayComponent.dismissKeyboard();
		return super.onTouchEvent(event);
	}
}
