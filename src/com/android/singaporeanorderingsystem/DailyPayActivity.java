/**
 * 
 */
package com.android.singaporeanorderingsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.component.ui.DailyPayComponent;
import com.android.component.ui.MenuComponent;
import com.android.dialog.DialogBuilder;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author jingang
 *
 */
//不需要标题
@NoTitle
//全屏显示
@Fullscreen
//绑定登录的layout
@EActivity(R.layout.daily_pay)
public class DailyPayActivity extends BasicActivity implements OnClickListener{
	@ViewById(R.id.text_id_all_price)
	TextView text_id_all_price;
	@ViewById(R.id.menu_btn)
	ImageView menu; //menu按钮
	@ViewById(R.id.login_name)
	TextView login_name; //用户名字
	@ViewById(R.id.layout_exit)
	RelativeLayout exit_layout; //退出
	@ViewById(R.id.wifi_iamge)
	ImageView wifi_image; //wifi 图标
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
	
	PopupWindow popupWindow;
	private List<DailyPayDetailBean> detail_classList;
	private List<TakeNumberBean> number_classList;
	private Double count=0.00;
	private final int OPEN_WIFI=1006;
	private final int CLOSE_WIFI=1007;
	private Double num_count=0.00;
	private List<Double> all_num_price;
	private List<Double> all_pay_price;
	public static boolean is_recer;
	private MyApp myApp;
	public static  HashMap<Integer, String> hashMap_detail = new HashMap<Integer, String>();  
	public static  HashMap<Integer, String> hashMap_num = new HashMap<Integer, String>();  
	public static  HashMap<Integer, String> hashMap_numprice = new HashMap<Integer, String>(); 
	private String search_date;
	private Double order_price=0.00;
//	private MyOrientationDetector1 m;
	private SharedPreferences sharedPrefs;
	
	@Bean
	MenuComponent menuComponent;
	
	@AfterViews
	public void init() {
		all_num_price=new ArrayList<Double>();
		all_pay_price=new ArrayList<Double>();
		myApp=(MyApp) DailyPayActivity.this.getApplication();
		DailyPayComponent.initView();
		
		btu_id_sbumit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreatedSubmitDialog().create().show();
			}
		});
		init_wifiReceiver();
	}
	 
	 // 提交数据窗口
	 public DialogBuilder CreatedSubmitDialog(){
			DialogBuilder builder=new DialogBuilder(this);
			builder.setTitle(R.string.message_title);
			builder.setMessage(R.string.message_zhifu);
			builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					if(doValidation()){
						btu_id_sbumit.setVisibility(View.GONE);
					}
				}});
			builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
				}});
			return builder;
		}
	@Override
	public void onClick(View V) {
		switch(V.getId()){
		case R.id.menu_btn:
			menuComponent.initPopupWindow();
			break;
		case R.id.layout_exit:

			CreatedDialog().create().show();
			break;
		}
	}

	    
	    public boolean doValidation(){
	    	// 带回总数 和 开店金额 不能为0
	    	String txtTotal = StringUtils.defaultIfEmpty(total_take_num.getText().toString(), "0");
			Double totalTakeNum = Double.parseDouble(txtTotal);
			String txtShopMoney = StringUtils.defaultIfEmpty(shop_money.getText().toString(), "0");
			Double shopMoney  = Double.parseDouble(txtShopMoney);
			
			if(totalTakeNum.doubleValue() <= 0 || shopMoney.doubleValue() <= 0){
				Toast.makeText(DailyPayActivity.this, getString(R.string.dialy_submit_error1), Toast.LENGTH_SHORT).show();
				return false;
			}
			
			//带回总数金额不一致
			Double sigle_price=0.00;
			for(int i=0;i<all_num_price.size();i++){	
				sigle_price += all_num_price.get(i).doubleValue();
			}
			
			if(sigle_price.doubleValue() != totalTakeNum.doubleValue()){
				Toast.makeText(DailyPayActivity.this, getString(R.string.dialy_submit_error2), Toast.LENGTH_SHORT).show();
				return false;
			}
			
			// 收银机不能为负数
	    	String txtCashRegister = StringUtils.defaultIfEmpty(cash_register.getText().toString(), "0");
			Double cashRegister = Double.parseDouble(txtCashRegister);
			if(cashRegister < 0){
				Toast.makeText(DailyPayActivity.this, getString(R.string.dialy_submit_error3), Toast.LENGTH_SHORT).show();
				return false;
			}
	    	return true;
	    }
	    
	   

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			DailyPayComponent.dismissKeyboard();
			
			return super.onTouchEvent(event);
		}

		@Override
		protected void onDestroy() {
			hashMap_detail.clear();
			super.onDestroy();
		}
		
	public void onload_payDetail(String id){
		RemoteDataHandler.asyncGet(Constants.URL_PAY_DETAIL+id,new Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				if(data.getCode() == 1){
					String json=data.getJson();
					Log.e("返回数据", json);
					//ArrayList<GetPayDetailBean> datas=GetPayDetailBean.newInstanceList(json);
				}else if(data.getCode() == 0){
					Toast.makeText(DailyPayActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
				}else if(data.getCode() == -1){
					Toast.makeText(DailyPayActivity.this, getString(R.string.login_service_err), Toast.LENGTH_SHORT).show();
				}
			}
		});
		}
	
	public void onload_takeNum(String id){
		HashMap<String, String> params =new HashMap<String, String>();
		params.put("id", id);
		RemoteDataHandler.asyncPost(Constants.URL_TAKE_DNUM, params, new Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				if(data.getCode() == 1){
				}else if(data.getCode() == 0){
					//Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
				}else if(data.getCode() == -1){
					//Toast.makeText(LoginActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
				}
			}
		});
		}

}
