/**
 * 
 */
package com.android.singaporeanorderingsystem;

import java.util.HashMap;

import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class DailyPayActivity extends BasicActivity {
	@ViewById(R.id.text_id_all_price)
	TextView text_id_all_price;
	
	@ViewById(R.id.menu_btn)
	ImageView menu; // menu按钮
	
	@ViewById(R.id.login_name)
	TextView login_name; // 用户名字
	
	@ViewById(R.id.layout_exit)
	RelativeLayout exit_layout; // 退出
	
	@ViewById(R.id.wifi_iamge)
	ImageView wifi_image; // wifi 图标
	
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
	DailyPayComponent DailyPayComponent;

	public static  HashMap<Integer, String> hashMap_detail = new HashMap<Integer, String>();  
	public static  HashMap<Integer, String> hashMap_num = new HashMap<Integer, String>();  
	public static  HashMap<Integer, String> hashMap_numprice = new HashMap<Integer, String>(); 
	
	@Bean
	MenuComponent menuComponent;
	
	@AfterViews
	public void init() {
		DailyPayComponent.initView();
		init_wifiReceiver();
	}

	@Click(R.id.btu_id_sbumit)
	void SbumitOnClick(){
		DailyPayComponent.CreatedSubmitDialog().create().show();
	}
	
	@Click(R.id.menu_btn)
	void MenuBtnOnClick(){
		menuComponent.initPopupWindow();
	}
	
	@Click(R.id.layout_exit)
	void LayoutExitOnClick(){
		CreatedDialog().create().show();
	}
	
	@Override
	protected void onDestroy() {
		hashMap_detail.clear();
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		DailyPayComponent.dismissKeyboard();
		return super.onTouchEvent(event);
	}
}
