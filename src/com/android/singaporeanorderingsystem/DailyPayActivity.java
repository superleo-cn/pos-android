/**
 * 
 */
package com.android.singaporeanorderingsystem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.adapter.DailyPayDetailAdapter;
import com.android.adapter.TakeNumerAdapter;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.common.SystemHelper;
import com.android.dao.DailyMoneyDao;
import com.android.dao.NumListDao;
import com.android.dao.PayListDao;
import com.android.dialog.DialogBuilder;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;

/**
 * @author jingang
 *
 */
public class DailyPayActivity extends Activity implements OnClickListener{
	private ListView daily_list;
	private ListView num_list;
	private List<DailyPayDetailBean> detail_classList;
	private List<TakeNumberBean> number_classList;
	private DailyPayDetailAdapter detail_adapter;
	private TakeNumerAdapter number_adapter;
	private Double count=0.00;
	private TextView text_id_all_price;
	private ImageView menu; //menu按钮
	private TextView login_name; //用户名字
	private RelativeLayout exit_layout; //退出
	private ImageView wifi_image; //wifi 图标 
	private Button btu_id_sbumit;
	private PopupWindow popupWindow;
	private View view;
	private final int OPEN_WIFI=1006;
	private final int CLOSE_WIFI=1007;
	private EditText cash_register;
	private EditText today_turnover;
	private EditText noon_time;
	private EditText noon_turnover;
	private EditText time;
	private EditText total;
	private EditText tomorrow_money;
	private EditText total_take_num;
	private EditText send_person;
	private EditText other;
	private EditText shop_money;
	private DecimalFormat  df;
	private TextView take_all_price;
	private Double num_count=0.00;
	private List<Double> all_num_price;
	public static boolean is_recer;
	private MyApp myApp;
	public static  HashMap<Integer, String> hashMap_detail = new HashMap<Integer, String>();  
	public static  HashMap<Integer, String> hashMap_num = new HashMap<Integer, String>();  
	public static  HashMap<Integer, String> hashMap_numprice = new HashMap<Integer, String>(); 
	private String search_date;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_pay);
		//init_wifiReceiver();
		all_num_price=new ArrayList<Double>();
		myApp=(MyApp) DailyPayActivity.this.getApplication();
	//onload_payDetail("1");
	}
	 
	 public void initView(){
		 take_all_price=(TextView) this.findViewById(R.id.take_all_price);
		 	menu=(ImageView) this.findViewById(R.id.menu_btn);
			menu.setOnClickListener(this);
			login_name=(TextView) this.findViewById(R.id.login_name);
			exit_layout=(RelativeLayout) this.findViewById(R.id.layout_exit);
			exit_layout.setOnClickListener(this);
			wifi_image=(ImageView) this.findViewById(R.id.wifi_iamge);
			daily_list = (ListView) findViewById(R.id.daily_detail_list);
			num_list = (ListView) findViewById(R.id.daily_num_list);
			text_id_all_price = (TextView) findViewById(R.id.text_id_all_price);
			btu_id_sbumit = (Button) findViewById(R.id.btu_id_sbumit);
			
			cash_register=(EditText) this.findViewById(R.id.cash_register);
			today_turnover=(EditText) this.findViewById(R.id.today_turnover);
			noon_time=(EditText) this.findViewById(R.id.noon_time);
			noon_turnover=(EditText) this.findViewById(R.id.noon_turnover);
			time=(EditText) this.findViewById(R.id.time);
			total=(EditText) this.findViewById(R.id.total);
			tomorrow_money=(EditText) this.findViewById(R.id.tomorrow_money);
			total_take_num=(EditText) this.findViewById(R.id.total_take_num);
			send_person=(EditText) this.findViewById(R.id.send_person);
			other=(EditText) this.findViewById(R.id.other);
			shop_money=(EditText) this.findViewById(R.id.shop_money);
			initData();
			if(myApp.getDaily_pay_submit_flag().equals("1")){
				btu_id_sbumit.setVisibility(View.VISIBLE);
			}else if(myApp.getDaily_pay_submit_flag().equals("0")){
				btu_id_sbumit.setVisibility(View.GONE);
			}
			shop_money.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
					compute();
					Log.e("今日输出价格", "");
				}});
			
			tomorrow_money.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
					compute();
					Log.e("明日输出价格", "");
				}});
	 }
	 
	 public void initData(){
		List<String> priceList= PriceSave.getInatance(DailyPayActivity.this).getList();
		Double price=0.00;
		Resources res =getResources();
		String[] pay_list=res.getStringArray(R.array.pay_list);
    	String[] take_number=res.getStringArray(R.array.take_number);
		for(int i=0;i<priceList.size();i++){
			price+=Double.parseDouble(priceList.get(i));
		}
		cash_register.setText(df.format(price));
			detail_classList = new ArrayList<DailyPayDetailBean>();
			number_classList = new ArrayList<TakeNumberBean>();
			
			for(int i=0 ; i < pay_list.length ; i++){
				DailyPayDetailBean bean=new DailyPayDetailBean();
				bean.setName(pay_list[i]);
				bean.setPrice("0");
				detail_classList.add(bean);
			}
			detail_adapter= new DailyPayDetailAdapter(this,detail_classList,handler);
			daily_list.setAdapter(detail_adapter);
			text_id_all_price.setText(df.format(count));
			 compute();
			 
			 for(int j=0 ; j < take_number.length ; j++){
				//num_count+=(2*i);
				 TakeNumberBean bean=new TakeNumberBean();
				 bean.setText1(take_number[j]);
				 bean.setText2("0");
					number_classList.add(bean);
				}
				number_adapter=new TakeNumerAdapter(this,number_classList,handler);;
				num_list.setAdapter(number_adapter);
				try{
					for(int i=0;i<number_classList.size();i++){
						Double sigle_price=Double.parseDouble(number_classList.get(i).getText1());
						int num=Integer.parseInt(number_classList.get(i).getText2());
						Double total_price=0.00;
						total_price=num*sigle_price;
						all_num_price.add(total_price);
						num_count=num_count+total_price;					
					}
					take_all_price.setText(df.format(num_count));
				}catch(Exception e){
					
				}
	 }
	 
	 public void initPopupWindow() {
			if (popupWindow == null) {
				view = this.getLayoutInflater().inflate(
						R.layout.popupwindow, null);
				popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				popupWindow.setOutsideTouchable(true);
				TextView popu_setting=(TextView) view.findViewById(R.id.popu_setting);
				TextView popu_exit=(TextView) view.findViewById(R.id.popu_exit);
				TextView popu_daily=(TextView) view.findViewById(R.id.popu_daily);
				TextView popu_diancai=(TextView) view.findViewById(R.id.popu_diancai);
				popu_diancai.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
						Intent intent =new Intent(DailyPayActivity.this , MainActivity.class);
						DailyPayActivity.this.startActivity(intent);
						overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
						//DailyPayActivity.this.finish();
					}
				});;
				popu_setting.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
						if(myApp.getU_type().equals("SUPERADMIN") || myApp.getU_type().equals("ADMIN") || 
								myApp.getU_type().equals("OPERATOR")){						
							Intent intent =new Intent(DailyPayActivity.this , SettingActivity.class);
							overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
							Bundle bundle=new Bundle();
							bundle.putString("type", "1");
							intent.putExtras(bundle);
							DailyPayActivity.this.startActivity(intent);
							DailyPayActivity.this.finish();
						}else{
							Toast.makeText(DailyPayActivity.this, getString(R.string.insufficientpermissions), Toast.LENGTH_SHORT).show();	
						}
					}
				});
				
				popu_daily.setVisibility(View.GONE);
				popu_exit.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
						CreatedDialog2().create().show();
					}
				});
			}
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
		}
	 public DialogBuilder CreatedDialog(){
			DialogBuilder builder=new DialogBuilder(this);
			builder.setTitle(R.string.message_title);
			builder.setMessage(R.string.message_zhifu);
			builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					//Toast.makeText(DailyPayActivity.this, "你点击了确定", Toast.LENGTH_SHORT).show();
					clear_data();
					myApp.setDaily_pay_submit_flag("0");
					btu_id_sbumit.setVisibility(View.GONE);
				}});
			builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					//Toast.makeText(DailyPayActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
				}});
			return builder;
		}
	@Override
	public void onClick(View V) {
		switch(V.getId()){
		case R.id.menu_btn:
			initPopupWindow();
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.showAsDropDown(menu, 0, -5);
			break;
		case R.id.layout_exit:
			DialogBuilder builder=new DialogBuilder(DailyPayActivity.this);
			builder.setTitle(R.string.message_title);
			builder.setMessage(R.string.message_exit);
			builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					System.exit(0);
				}});
			builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
				}});
			builder.create().show();
			
			break;
		}
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case OPEN_WIFI:
				wifi_image.setImageResource(R.drawable.wifi_open);
				break;
			case CLOSE_WIFI:
				wifi_image.setImageResource(R.drawable.wifi_close);
				break;
			case DailyPayDetailAdapter.CHAGE_NUM_DETAIL:
				try{
				count=0.00;
				String str=(String) msg.obj;
				int num=Integer.parseInt(str.substring(0,1));
				String price=str.substring(1,str.length());
				detail_classList.get(num).setPrice(price);
				
				for(int i=0;i<detail_classList.size();i++){
					count+=Double.parseDouble(detail_classList.get(i).getPrice());
				}
				text_id_all_price.setText(df.format(count));
				
				compute();
				}catch(Exception e){
					Toast.makeText(DailyPayActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
				}
				break;
			case TakeNumerAdapter.SET_NUM:
				num_count=0.00;
				String str=(String) msg.obj;
				int num=Integer.parseInt(str.substring(0,1));
				String price=str.substring(1,str.length());
				all_num_price.set(num, Double.parseDouble(price));
				Double sigle_price=0.00;
				for(int i=0;i<all_num_price.size();i++){	
					sigle_price=all_num_price.get(i).doubleValue();
					num_count=num_count+sigle_price;
				}
				
				take_all_price.setText(df.format(num_count));
				break;
			}
		}
		
	};


	 private BroadcastReceiver myReceiver=new BroadcastReceiver()
	    {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				 String action = intent.getAction();
		            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo  mobInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo  wifiInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(!mobInfo.isConnected()&&!wifiInfo.isConnected()){
					new Thread(new Runnable(){

						public void run() {
							// TODO Auto-generated method stub
							Message msg = new Message();
							msg.what=CLOSE_WIFI;
							handler.sendMessage(msg);
						}
						
					}).start();
				
				}else{
					new Thread(new Runnable(){

						public void run() {
							// TODO Auto-generated method stub
							Message msg = new Message();
							msg.what=OPEN_WIFI;
							handler.sendMessage(msg);
						}
						
					}).start();
				}
			}
			}
	    };
	    public DialogBuilder CreatedDialog2(){
			DialogBuilder builder=new DialogBuilder(this);
			builder.setTitle(R.string.message_title);
			builder.setMessage(R.string.message_exit);
			builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					System.exit(0);
				}});
			builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
				}});
			return builder;
		}
	    private void init_wifiReceiver()
	    {
	    	IntentFilter filter=new IntentFilter();
	    	 filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	    	registerReceiver(myReceiver,filter);
	    	is_recer=true;
	    }
	    
	    public void  clear_data(){
	    	 InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
	    	String detail_price;
	    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    	String date=df.format(new Date());
	    	this.search_date=date;
	    	/*提交每日支付金额*/	    	
	    	for(int i=0;i<detail_classList.size();i++){
	    		DailyPayDetailBean bean = detail_classList.get(i);
	    		if(bean != null && StringUtils.isNotEmpty(bean.getPrice())){
	    			detail_price=bean.getPrice();
	    		}else{
	    			detail_price="0.00";
	    		} 
	    /*参数*/		
	    //Sandroid_id,Sconsumption_id,shop_id,user_id,date,type,price
		    	PayListDao.getInatance(DailyPayActivity.this).save(String.valueOf(i+1),
		    			String.valueOf(i+1),
		    			myApp.getSettingShopId(),
		    			myApp.getUser_id(),
		    			date,
		    			"0",
		    			detail_price);
	    	}
	    /*提交每日支付金额结束*/	
	    	
	   /*提交带回总数接口*/
	    	String take_num;
	    	for(int j=0;j<number_classList.size();j++){
	    		TakeNumberBean bean = number_classList.get(j);
	    		if(bean != null && StringUtils.isNotEmpty(bean.getText1())){
	    			take_num=bean.getText1();
	    		}else{
	    			take_num="0.00";
	    		} 
	   /*参数*/
	   //android_id,cash_id,shop_id,user_id,date,type,quantity
	    		NumListDao.getInatance(DailyPayActivity.this).save(String.valueOf(j+1),
		    			String.valueOf(j+1),
		    			myApp.getSettingShopId(),
		    			myApp.getUser_id(),
		    			date,
		    			"0",
	    				take_num);
	    	}
	    
	    /*提交带回总数接口结束*/

	    	int num_of_visible_view=num_list.getLastVisiblePosition() - num_list.getFirstVisiblePosition();
	    	for(int i=0;i<=num_of_visible_view;i++){
	    		EditText edit=(EditText) num_list.getChildAt(i).findViewById(R.id.num_id_price);
	    		edit.setEnabled(false);
	    		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	    		
	    	}
	    	
	    	int num_of_view=daily_list.getLastVisiblePosition() - daily_list.getFirstVisiblePosition();
	    	for(int i=0;i<=num_of_view;i++){
	    		EditText edit=(EditText) daily_list.getChildAt(i).findViewById(R.id.text_id_price);
	    		edit.setEnabled(false);
	    		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	    		
	    	}
	    	
	    	
	    	String aOpenBalance=shop_money.getText().toString();
	    	if(aOpenBalance.isEmpty()){
	    		aOpenBalance="0";
	    	}
	    	String bExpenses=text_id_all_price.getText().toString();
	    	if(bExpenses.isEmpty()){
	    		bExpenses="0";
	    	}
	    	String cCashCollected=cash_register.getText().toString();
	    	if(cCashCollected.isEmpty()){
	    		cCashCollected="0";
	    	}
	    	String dDailyTurnover=today_turnover.getText().toString();
	    	if(dDailyTurnover.isEmpty()){
	    		dDailyTurnover="0";
	    	}
	    	String eNextOpenBalance=tomorrow_money.getText().toString();
	    	if(eNextOpenBalance.isEmpty()){
	    		eNextOpenBalance="0";
	    	}
	    	String fBringBackCash=total_take_num.getText().toString();
	    	if(fBringBackCash.isEmpty()){
	    		fBringBackCash="0";
	    	}
	    	String gTotalBalance=total.getText().toString();
	    	if(gTotalBalance.isEmpty()){
	    		gTotalBalance="0";
	    	}
	    	String middleCalculateTime=noon_time.getText().toString();
	    	if(middleCalculateTime.isEmpty()){
	    		middleCalculateTime="yyyy-MM-dd";
	    	}
	    	String middleCalculateBalance=noon_turnover.getText().toString();
	    	if(middleCalculateBalance.isEmpty()){
	    		middleCalculateBalance="0";
	    	}
	    	String calculateTime=time.getText().toString();
	    	if(calculateTime.isEmpty()){
	    		calculateTime="yyyy-MM-dd";
	    	}
	    	String others=other.getText().toString();
	    	if(others.isEmpty()){
	    		others="";
	    	}
	    	String courier=send_person.getText().toString();
	    	if(courier.isEmpty()){
	    		courier="";
	    	}
	    	String type="0";
			DailyMoneyDao.getInatance(DailyPayActivity.this).save("0", 
					myApp.getSettingShopId(),
					myApp.getUser_id(), 
					aOpenBalance,
					bExpenses,
					cCashCollected, 
					dDailyTurnover,
					eNextOpenBalance, 
					fBringBackCash,
					gTotalBalance,
					middleCalculateTime,
					middleCalculateBalance,
					calculateTime,
					others, 
					courier, 
					type,
					date);
	    	
	    	
	    	cash_register.setText("");
	    	today_turnover.setText("");
	    	noon_time.setText("");
	    	noon_turnover.setText("");
	    	time.setText("");
	    	total.setText("");
	    	tomorrow_money.setText("");
	    	total_take_num.setText("");
	    	send_person.setText("");
	    	other.setText("");
	    	shop_money.setText("");
	    	//text_id_all_price.setText("");
	    	post_payList();
	    	post_numList(); 
	    	post_dailyMoney();
	    }
	    
	    public void compute(){
	    	try{
	    		String shop_money_text;
	    		String tomorrow_money_text;
				if(shop_money.getText().toString().length()==0){
					shop_money_text="0";
				}else{
					shop_money_text=shop_money.getText().toString();
				}
				
				if(tomorrow_money.getText().toString().length()==0){
					tomorrow_money_text="0";
				}else{
					tomorrow_money_text=tomorrow_money.getText().toString();
				}
	    		
				String all_price=text_id_all_price.getText().toString();
				Double price_b=Double.parseDouble(all_price);
				Double price_c=Double.parseDouble(cash_register.getText().toString());
				Double price_today=price_b+price_c;
				Double price_d=price_b+price_c;
				today_turnover.setText(df.format(price_today));
				
				Double price_a=Double.parseDouble(shop_money_text);
				Double total_t=price_a+price_b+price_c;
				total.setText(df.format(total_t));
				
				Double price_e=Double.parseDouble(tomorrow_money_text);
				Double take_price=price_d-price_e;
				total_take_num.setText(df.format(take_price));
			}catch(Exception e){
				e.getMessage();
			}
	    }

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			df=new DecimalFormat("0.00");
			initView();
			
			btu_id_sbumit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//text_id_all_price.setText(count+R.string.rmb);
//					shop_money.setText("100");
//					tomorrow_money.setText("100");
					//compute();
					CreatedDialog().create().show();
				}
			});
			
			super.onResume();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			 InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
			 imm.hideSoftInputFromWindow(cash_register.getWindowToken(), 0); 
			 imm.hideSoftInputFromWindow(today_turnover.getWindowToken(), 0); //强制隐藏键盘 
			 imm.hideSoftInputFromWindow(noon_time.getWindowToken(), 0); //强制隐藏键盘 
			 imm.hideSoftInputFromWindow(noon_turnover.getWindowToken(), 0); //强制隐藏键盘 
			 imm.hideSoftInputFromWindow(time.getWindowToken(), 0); //强制隐藏键盘 
			 imm.hideSoftInputFromWindow(total.getWindowToken(), 0); //强制隐藏键盘 
			 imm.hideSoftInputFromWindow(tomorrow_money.getWindowToken(), 0); //强制隐藏键盘 
			 imm.hideSoftInputFromWindow(total_take_num.getWindowToken(), 0); //强制隐藏键盘 
			 imm.hideSoftInputFromWindow(send_person.getWindowToken(), 0); //强制隐藏键盘 
			 imm.hideSoftInputFromWindow(shop_money.getWindowToken(), 0); //强制隐藏键盘 
			 //Toast.makeText(this, "取消软键盘" , Toast.LENGTH_SHORT).show();
			
			return super.onTouchEvent(event);
		}

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			//unregisterReceiver(myReceiver);
			super.onDestroy();
		}
		
	public void onload_payDetail(String id){
//		HashMap<String, String> params =new HashMap<String, String>();
//		params.put("id", id);
//		RemoteDataHandler.asyncPost(Constants.URL_PAY_DETAIL, params, new Callback() {
//			@Override
//			public void dataLoaded(ResponseData data) {
//				if(data.getCode() == 1){
//					String json=data.getJson();
//					Log.e("返回数据", json);
//					//ArrayList<GetPayDetailBean> datas=GetPayDetailBean.newInstanceList(json);
//				}else if(data.getCode() == 0){
//					Toast.makeText(DailyPayActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//				}else if(data.getCode() == -1){
//					Toast.makeText(DailyPayActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
		System.out.println("url-->"+Constants.URL_PAY_DETAIL+id);
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
	/*提交每日支付*/
	public void post_payList(){
		try{
		HashMap<String, String> params= new HashMap<String,String>();
		List<Map<String,String>> datas=PayListDao.getInatance(this).getList();
		if(!datas.isEmpty()){
		for(int i=0;i<datas.size();i++){
			if(datas.get(i).get("type").equals("0")){
			params.put("consumeTransactions["+i+"].androidId", datas.get(i).get("android_id"));
			Log.e("consumeTransactions["+i+"].androidId", datas.get(i).get("android_id"));
			params.put("consumeTransactions["+i+"].consumption.id", datas.get(i).get("consumption_id"));
			Log.e("consumeTransactions["+i+"].consumption.id", datas.get(i).get("consumption_id"));
			params.put("consumeTransactions["+i+"].shop.id", datas.get(i).get("shop_id"));
			Log.e("consumeTransactions["+i+"].shop.id", datas.get(i).get("shop_id"));
			params.put("consumeTransactions["+i+"].user.id", datas.get(i).get("user_id"));
			Log.e("consumeTransactions["+i+"].user.id", datas.get(i).get("user_id"));
			params.put("consumeTransactions["+i+"].price", datas.get(i).get("price"));
			Log.e("consumeTransactions["+i+"].price", datas.get(i).get("price"));
			}
		}
		}
		RemoteDataHandler.asyncPost(Constants.URL_POST_PAYLIST, params, new Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				if(data.getCode() == 1){
					String json=data.getJson();
					Toast.makeText(DailyPayActivity.this, getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
				String str=json.substring(1,json.length()-1);
				String []array=str.split(",");
				if(array.length!=0){
					for(int i=0;i<array.length;i++){
						Log.e("数据组",array[i]+"");
					int result=	PayListDao.getInatance(DailyPayActivity.this).update_type(array[i], "1");
					if(result==-1){
						//Toast.makeText(DailyPayActivity.this, "每日支付接口更新失败", Toast.LENGTH_SHORT).show();
					}else{
						//Toast.makeText(DailyPayActivity.this, "每日支付接口更新成功", Toast.LENGTH_SHORT).show();
					}
						
					}
				}
				}else if(data.getCode() == 0){
					Toast.makeText(DailyPayActivity.this, getString(R.string.toast_submmit_fail), Toast.LENGTH_SHORT).show();
				}else if(data.getCode() == -1){
					Toast.makeText(DailyPayActivity.this, getString(R.string.toast_submmit_err), Toast.LENGTH_SHORT).show();
				}
			}
		});
		}catch(Exception e){
			e.getMessage();
		}
	}
	
	/*提交带回总数*/
	public void post_numList(){
		try{
			HashMap<String, String> params= new HashMap<String,String>();
			List<Map<String,String>> datas=NumListDao.getInatance(this).getList();
			if(!datas.isEmpty()){
			for(int i=0;i<datas.size();i++){
				if(datas.get(i).get("type").equals("0")){
				params.put("cashTransactions["+i+"].androidId", datas.get(i).get("android_id"));
				Log.e("cashTransactions["+i+"].androidId", datas.get(i).get("android_id"));
				params.put("cashTransactions["+i+"].cash.id", datas.get(i).get("cash_id"));
				Log.e("cashTransactions["+i+"].cash.id", datas.get(i).get("cash_id"));
				params.put("cashTransactions["+i+"].shop.id", datas.get(i).get("shop_id"));
				Log.e("cashTransactions["+i+"].shop.id", datas.get(i).get("shop_id"));
				params.put("cashTransactions["+i+"].user.id", datas.get(i).get("user_id"));
				Log.e("cashTransactions["+i+"].user.id", datas.get(i).get("user_id"));
				params.put("cashTransactions["+i+"].quantity", datas.get(i).get("quantity"));
				Log.e("cashTransactions["+i+"].quantity", datas.get(i).get("quantity"));
				}
			}
			}
			RemoteDataHandler.asyncPost(Constants.URL_POST_TAKENUM, params, new Callback() {
				@Override
				public void dataLoaded(ResponseData data) {
					if(data.getCode() == 1){
						String json=data.getJson();
						Toast.makeText(DailyPayActivity.this, getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
					String str=json.substring(1,json.length()-1);
					String []array=str.split(",");
					if(array.length!=0){
						for(int i=0;i<array.length;i++){
							Log.e("数据组",array[i]+"");
						int result=	NumListDao.getInatance(DailyPayActivity.this).update_type(array[i], "1");
						if(result==-1){
							//Toast.makeText(DailyPayActivity.this, "带回总数接口更新失败", Toast.LENGTH_SHORT).show();
						}else{
							//Toast.makeText(DailyPayActivity.this, "带回总数接口更新成功", Toast.LENGTH_SHORT).show();
						}
							
						}
					}
					}else if(data.getCode() == 0){
						Toast.makeText(DailyPayActivity.this, getString(R.string.toast_submmit_fail), Toast.LENGTH_SHORT).show();
					}else if(data.getCode() == -1){
						Toast.makeText(DailyPayActivity.this, getString(R.string.toast_submmit_err), Toast.LENGTH_SHORT).show();
					}
				}
			});
			}catch(Exception e){
				e.getMessage();
			}
	}
	
	/*提交每日营业额*/
	public void post_dailyMoney(){
		try{
			HashMap<String, String> params= DailyMoneyDao.getInatance(DailyPayActivity.this).getList(search_date);
//			HashMap<String, String> map=new HashMap<String,String>();
//			map.put("dailySummary.android.id", "1");
//			map.put("dailySummary.shop.id", "2");
//			map.put("dailySummary.user.id", "1");
//			map.put("dailySummary.aOpenBalance", "0");
//			map.put("dailySummary.bExpenses", "0");
//			map.put("dailySummary.cCashCollected", "0");
//			map.put("dailySummary.dDailyTurnover", "0");
//			map.put("dailySummary.eNextOpenBalance", "0");
//			map.put("dailySummary.fBringBackCash", "0");
//			map.put("dailySummary.gTotalBalance", "0");
//			map.put("dailySummary.middleCalculateTime", "0");
//			map.put("dailySummary.middleCalculateBalance", "0");
//			map.put("dailySummary.calculateTime", "0");
//			map.put("dailySummary.others", "0");
//			map.put("dailySummary.courier", "0");
			RemoteDataHandler.asyncPost(Constants.URL_POST_DAILY_MONEY, params, new Callback() {
				@Override
				public void dataLoaded(ResponseData data) {
					if(data.getCode() == 1){
						String json=data.getJson();
						Toast.makeText(DailyPayActivity.this, getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
						int result=DailyMoneyDao.getInatance(DailyPayActivity.this).update_type(search_date);
						if(result==-1){
							Toast.makeText(DailyPayActivity.this, "每日营业额更新失败", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(DailyPayActivity.this, "每日营业额更新成功", Toast.LENGTH_SHORT).show();
						}
					}else if(data.getCode() == 0){
						Toast.makeText(DailyPayActivity.this, getString(R.string.toast_submmit_fail), Toast.LENGTH_SHORT).show();
					}else if(data.getCode() == -1){
						Toast.makeText(DailyPayActivity.this, getString(R.string.toast_submmit_err), Toast.LENGTH_SHORT).show();
					}
				}
			});
			}catch(Exception e){
				e.getMessage();
			}
	}
		
}
