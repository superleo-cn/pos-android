/**
 * 
 */
package com.android.singaporeanorderingsystem;

import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Sampler.Value;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
import com.android.bean.GetPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.common.SystemHelper;
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
	private ImageView daily_menu; //menu按钮
	private TextView daily_login_name; //用户名字
	private RelativeLayout daily_exit_layout; //退出
	private ImageView daily_wifi_image; //wifi 图标 
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
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_pay);
		//init_wifiReceiver();
		all_num_price=new ArrayList<Double>();
	onload_payDetail("1");
	}
	 
	 public void initView(){
		 take_all_price=(TextView) this.findViewById(R.id.take_all_price);
		 daily_menu=(ImageView) this.findViewById(R.id.daily_menu_btn);
			daily_menu.setOnClickListener(this);
			daily_login_name=(TextView) this.findViewById(R.id.daily_login_name);
			daily_exit_layout=(RelativeLayout) this.findViewById(R.id.daily_layout_exit);
			daily_exit_layout.setOnClickListener(this);
			daily_wifi_image=(ImageView) this.findViewById(R.id.daily_wifi_iamge);
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
						Intent intent =new Intent(DailyPayActivity.this , SettingActivity.class);						
						overridePendingTransition(
								R.anim.in_from_right,
								R.anim.out_to_left);
						Bundle bundle=new Bundle();
						bundle.putString("type", "2");
						intent.putExtras(bundle);
						DailyPayActivity.this.startActivity(intent);
						DailyPayActivity.this.finish();
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
		case R.id.daily_menu_btn:
			initPopupWindow();
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.showAsDropDown(daily_menu, 0, -5);
			break;
		case R.id.daily_layout_exit:
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
				daily_wifi_image.setImageResource(R.drawable.wifi_open);
				break;
			case CLOSE_WIFI:
				daily_wifi_image.setImageResource(R.drawable.wifi_close);
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
	    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	    	String date=df.format(new Date());
	    	for(int i=0;i<detail_classList.size();i++){
	    		if(hashMap_detail.get(i)==null){
	    			detail_price="0.00";
	    		}else{
	    			detail_price=hashMap_detail.get(i);
	    		}
		    	PayListDao.getInatance(DailyPayActivity.this).save(String.valueOf(i),
		    			String.valueOf(i),
		    			String.valueOf(i),
		    			String.valueOf(1),
		    			date,
		    			"1",
		    			detail_price);
	    	}
	    	String take_num;
	    	for(int j=0;j<number_classList.size();j++){
	    		if(hashMap_detail.get(j)==null){
    				take_num="0.00";
    			}else{
    				take_num=hashMap_num.get(j);
    			}
	    		NumListDao.getInatance(DailyPayActivity.this).save(String.valueOf(j),
		    			String.valueOf(j),
		    			String.valueOf(1),
		    			String.valueOf(1),
		    			date,
		    			"1",
	    				take_num);
	    	}
	    	//daily_list.setAdapter(null);
	    	//daily_num_list.setAdapter(null);
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
					Toast.makeText(DailyPayActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
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
		
	}
	
	/*提交带回总数*/
	public void post_numList(){
		
	}
	
	/*提交每日营业额*/
	public void post_dailyMoney(){
		
	}
		
}
