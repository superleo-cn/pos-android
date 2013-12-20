package com.android.component.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.adapter.DailyPayDetailAdapter;
import com.android.adapter.TakeNumerAdapter;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.component.ActivityComponent;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.dao.DailyMoneyDao;
import com.android.dao.GetTakeNumDao;
import com.android.dao.NumListDao;
import com.android.dao.PayListDao;
import com.android.dao.getDetailPayListDao;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;
import com.android.singaporeanorderingsystem.PriceSave;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

@EBean
public class DailyPayComponent {
	@App
	MyApp myApp;

	@RootContext
	Context context;

	@RootContext
	Activity activity;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	ActivityComponent activityComponent;
	
	@ViewById(R.id.write_name)
	TextView write_name;
	
	@ViewById(R.id.shop_name1234)
	TextView shop_name1234;
	
	@ViewById(R.id.send_person)
	EditText send_person;
	
	@ViewById(R.id.daily_detail_list)
	ListView daily_list;
	
	@ViewById(R.id.daily_num_list)
	ListView num_list;
	
	@ViewById(R.id.text_id_all_price)
	TextView text_id_all_price;
	
	@ViewById(R.id.take_all_price)
	TextView take_all_price;
	
	@ViewById(R.id.tomorrow_money)
	EditText tomorrow_money;
	
	@ViewById(R.id.shop_money)
	EditText shop_money;
	
	@ViewById(R.id.cash_register)
	EditText cash_register;
	
	@ViewById(R.id.today_turnover)
	EditText today_turnover;
	
	@ViewById(R.id.total)
	EditText total;
	
	@ViewById(R.id.total_take_num)
	EditText total_take_num;
	
	@ViewById(R.id.noon_time)
	EditText noon_time;
	
	@ViewById(R.id.noon_turnover)
	EditText noon_turnover;
	
	@ViewById(R.id.time)
	EditText time;
	
	@ViewById(R.id.btu_id_sbumit)
	Button btu_id_sbumit;
	
	@ViewById(R.id.menu_btn)
	ImageView menu; //menu按钮
	
	@ViewById(R.id.other)
	EditText other;
	
	@ViewById(R.id.wifi_iamge)
	ImageView wifi_image; //wifi 图标
	
	private List<Double> all_num_price = new ArrayList<Double>();
	private List<Double> all_pay_price = new ArrayList<Double>();
	private List<DailyPayDetailBean> detail_classList = new ArrayList<DailyPayDetailBean>();
	private List<TakeNumberBean> number_classList = new ArrayList<TakeNumberBean>();
	private DailyPayDetailAdapter detail_adapter;
	private TakeNumerAdapter number_adapter;
	private DecimalFormat df = new DecimalFormat("0.00");
	private Double num_count = 0.00;
	private Double count = 0.00;
	private Double order_price = 0.00;
	private String search_date;

	@AfterViews
	public void initDailayPay() {
		initData();
	}

	public void initData() {
		// sharedPrefs = getSharedPreferences("language", Context.MODE_PRIVATE);
		// String type = sharedPrefs.context.getString("type", "");
		// login_name.setText(context.getString(R.string.mainTitle_txt)+" "+myApp.getU_name()+",");
		shop_name1234.setText(myApp.getShop_name() + "-" + myApp.getShop_code());
		write_name.setText(myApp.getU_name());
		send_person.setText(myApp.getU_name());

		List<Map<String, String>> datas = getDetailPayListDao.getInatance(context).getList();
		if (datas == null) {
		} else {
			for (int i = 0; i < datas.size(); i++) {
				DailyPayDetailBean bean = new DailyPayDetailBean();
				bean.setName(datas.get(i).get("name"));
				// if(StringUtils.equalsIgnoreCase("zh", type)){
				// bean.setName(datas.get(i).get("nameZh"));
				// }else{
				// bean.setName(datas.get(i).get("name"));
				// }
				bean.setId(datas.get(i).get("number_id"));
				bean.setPrice("");
				detail_classList.add(bean);
				all_pay_price.add(0.00);
			}

			detail_adapter = new DailyPayDetailAdapter(context, detail_classList, handler);
			daily_list.setAdapter(detail_adapter);

		}
		text_id_all_price.setText(df.format(count));

		List<Map<String, String>> datas_num = GetTakeNumDao.getInatance(context).getList();
		Log.e("查询带回数据库", datas_num.size() + "");
		if (datas_num == null) {
		} else {
			for (int j = 0; j < datas_num.size(); j++) {
				TakeNumberBean bean = new TakeNumberBean();
				bean.setPrice(datas_num.get(j).get("price"));
				;
				bean.setId(datas_num.get(j).get("number_id"));
				bean.setNum("");
				// hashMap_num.put(j, "");
				number_classList.add(bean);
			}
			Log.e("打包带走", number_classList.size() + "");
			number_adapter = new TakeNumerAdapter(context, number_classList, handler);
			num_list.setAdapter(number_adapter);
			try {
				for (int i = 0; i < number_classList.size(); i++) {
					String price_tv = number_classList.get(i).getPrice();
					if (price_tv.equals("")) {
						price_tv = "0.00";
					}
					Double sigle_price = Double.parseDouble(price_tv);
					String num_tv = number_classList.get(i).getNum();
					if (num_tv.equals("")) {
						num_tv = "0";
					}
					int num = Integer.parseInt(num_tv);
					Double total_price = 0.00;
					total_price = num * sigle_price;
					all_num_price.add(total_price);
					// hashMap_numprice.put(i, String.valueOf(total_price));
					num_count = num_count + total_price;
				}

			} catch (Exception e) {

			}
		}
		take_all_price.setText(df.format(num_count));
		compute();
	}
	
	 public void  clear_data(){
    	 InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
    	String detail_price;
    	SimpleDateFormat df_date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	String date=df_date.format(new Date());
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
	    	PayListDao.getInatance(context).save(String.valueOf(i+1),
	    			bean.getId(),
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
    		if(bean != null && StringUtils.isNotEmpty(bean.getNum())){
    			take_num=bean.getNum();
    		}else{
    			take_num="0";
    		} 
   /*参数*/
   //android_id,cash_id,shop_id,user_id,date,type,quantity
    		NumListDao.getInatance(context).save(String.valueOf(j+1),
    				bean.getId(),
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
		DailyMoneyDao.getInatance(context).save("0", 
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
    	
    	//记录退出日志，退出系统
//    	logUserAction();
    	
    }

	public void compute() {
		try {
			String shop_money_text;
			String tomorrow_money_text;
			if (shop_money.getText().toString().length() == 0) {
				shop_money_text = "0";
			} else {
				shop_money_text = shop_money.getText().toString();
			}

			if (tomorrow_money.getText().toString().length() == 0) {
				tomorrow_money_text = "0";
			} else {
				tomorrow_money_text = tomorrow_money.getText().toString();
			}

			String all_price = text_id_all_price.getText().toString();
			Double price_f = Double.parseDouble(take_all_price.getText().toString());
			cash_register.setText(df.format(order_price + Double.parseDouble(shop_money_text) - Double.parseDouble(all_price)));
			Double price_b = Double.parseDouble(all_price);
			Double price_c = Double.parseDouble(cash_register.getText().toString());
			Double price_today = price_b + price_c;
			Double price_d = order_price;
			today_turnover.setText(df.format(price_d));

			Double price_a = Double.parseDouble(shop_money_text);
			Double total_t = price_c - price_b;
			total.setText(df.format(total_t));

			Double price_e = Double.parseDouble(tomorrow_money_text);
			Double take_price = Double.parseDouble(cash_register.getText().toString()) - price_e;
			total_take_num.setText(df.format(take_price));
		} catch (Exception e) {
			Log.e("总计算", e.getMessage());
			// e.getMessage();
		}
	}
	public void initView(){
		 String userId=myApp.getUser_id();
		 String shopId=myApp.getSettingShopId();
		 if(shopId==null){
			 shopId="0";
		 }

		 SimpleDateFormat df_save=new SimpleDateFormat("yyyy-MM-dd");
	    	String date=df_save.format(new Date());
	    	Log.e("今天日期", date);
		 boolean flag = DailyMoneyDao.getInatance(context).isCompleted(shopId, userId, date, "1");
		 if(!flag){
			btu_id_sbumit.setVisibility(View.VISIBLE);
			List<String> priceList= null;
			if(!flag){
				priceList = PriceSave.getInatance(context).getList(myApp.getUser_id(),date,myApp.getSettingShopId());
			}else{
				btu_id_sbumit.setVisibility(View.GONE);
			}

			//Double price=0.00;
			if(priceList==null){
				order_price=0.00;
			}else{		
				if(priceList.size()!=0){
					for(int i=0;i<priceList.size();i++){
				order_price+=Double.parseDouble(priceList.get(i));
					}
				}else{
					order_price=0.00;
				}

			}
		}else if(myApp.getDaily_pay_submit_flag().equals("0")){
			btu_id_sbumit.setVisibility(View.GONE);
		}
//		initData();
		shop_money.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
				compute();
				Log.e("今日输出价格", "");
			}});
		
		tomorrow_money.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
				compute();
				Log.e("明日输出价格", "");
			}});
 }
	/*提交每日支付*/
	public void post_payList(){
		try{
		HashMap<String, String> params= new HashMap<String,String>();
		List<Map<String,String>> datas=PayListDao.getInatance(context).getList(search_date);
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
					//Toast.makeText(context, context.getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
				String str=json.substring(1,json.length()-1);
				String []array=str.split(",");
				if(array.length!=0){
					for(int i=0;i<array.length;i++){
						Log.e("数据组",array[i]+"");
					int result=	PayListDao.getInatance(context).update_type(array[i], "1");
					if(result==-1){
						//Toast.makeText(context, "每日支付接口更新失败", Toast.LENGTH_SHORT).show();
					}else{
						//Toast.makeText(context, "每日支付接口更新成功", Toast.LENGTH_SHORT).show();
					}
					}
				}
				}else if(data.getCode() == 0){
					Toast.makeText(context, context.getString(R.string.toast_submmit_fail), Toast.LENGTH_SHORT).show();
				}else if(data.getCode() == -1){
					Toast.makeText(context, context.getString(R.string.toast_submmit_err), Toast.LENGTH_SHORT).show();
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
			List<Map<String,String>> datas=NumListDao.getInatance(context).getList(search_date);
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
						//Toast.makeText(context, context.getString(R.string.toast_submmit_succ)/, Toast.LENGTH_SHORT).show();
					String str=json.substring(1,json.length()-1);
					String []array=str.split(",");
					if(array.length!=0){
						for(int i=0;i<array.length;i++){
							Log.e("数据组",array[i]+"");
						int result=	NumListDao.getInatance(context).update_type(array[i], "1");
						if(result==-1){
							//Toast.makeText(context, "带回总数接口更新失败", Toast.LENGTH_SHORT).show();
						}else{
							//Toast.makeText(context, "带回总数接口更新成功", Toast.LENGTH_SHORT).show();
						}
						}
					}
					}else if(data.getCode() == 0){
						Toast.makeText(context, context.getString(R.string.toast_submmit_fail), Toast.LENGTH_SHORT).show();
					}else if(data.getCode() == -1){
						Toast.makeText(context, context.getString(R.string.toast_submmit_err), Toast.LENGTH_SHORT).show();
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
			HashMap<String, String> params= DailyMoneyDao.getInatance(context).getList(search_date);
			RemoteDataHandler.asyncPost(Constants.URL_POST_DAILY_MONEY, params, new Callback() {
				@Override
				public void dataLoaded(ResponseData data) {
					if(data.getCode() == 1){
						String json=data.getJson();
						//Toast.makeText(context, context.getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
						int result=DailyMoneyDao.getInatance(context).update_type(search_date);
						if(result==-1){
							Toast.makeText(context, "每日营业额更新失败", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(context, "每日营业额更新成功", Toast.LENGTH_SHORT).show();
						}
					}else if(data.getCode() == 0){
						Toast.makeText(context, context.getString(R.string.toast_submmit_fail), Toast.LENGTH_SHORT).show();
					}else if(data.getCode() == -1){
						Toast.makeText(context, context.getString(R.string.toast_submmit_err), Toast.LENGTH_SHORT).show();
					}
				}
			});
			}catch(Exception e){
				e.getMessage();
			}
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			 switch(msg.what){
			 case Constants.OPEN_WIFI:
			 wifi_image.setImageResource(R.drawable.wifi_open);
			 break;
			 case Constants.CLOSE_WIFI:
			 wifi_image.setImageResource(R.drawable.wifi_close);
			 break;
			 case DailyPayDetailAdapter.CHAGE_NUM_DETAIL:
			 try{
			 count=0.00;
			 String str=(String) msg.obj;
			 Log.e("改变支付款价格", str);
			 int num=Integer.parseInt(str.substring(0,1));
			 String price=str.substring(2,str.length());
			 Log.e("截取的价格",price );
			 all_pay_price.set(num, Double.parseDouble(price));
			 Double sigle_price=0.00;
			 for(int i=0;i<all_pay_price.size();i++){
			 sigle_price=all_pay_price.get(i).doubleValue();
			 count=count+sigle_price;
			 }
			 text_id_all_price.setText(df.format(count));
			
			 compute();
			 }catch(Exception e){
			 Log.e("支付款计算报错信息", e.getMessage());
			 Toast.makeText(context, R.string.err_price,
			 Toast.LENGTH_SHORT).show();
			 }
			 break;
			 case TakeNumerAdapter.SET_NUM:
			 num_count=0.00;
			 String str=(String) msg.obj;
			 Log.e("改变带回总数价格", str);
			 int num=Integer.parseInt(str.substring(0,1));
			 String price=str.substring(2,str.length());
			 Log.e("截取带回的价格",price );
			 all_num_price.set(num, Double.parseDouble(price));
			 Double sigle_price=0.00;
			 for(int i=0;i<all_num_price.size();i++){
			 sigle_price=all_num_price.get(i).doubleValue();
			 num_count=num_count+sigle_price;
			 }
			
			 take_all_price.setText(df.format(num_count));
			 compute();
			 break;
			 }
		}

	};

	public void dismissKeyboard() {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(cash_register.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(today_turnover.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(noon_time.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(noon_turnover.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(time.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(total.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(tomorrow_money.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(total_take_num.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(send_person.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(shop_money.getWindowToken(), 0); // 强制隐藏键盘
		// Toast.makeText(this, "取消软键盘" , Toast.LENGTH_SHORT).show();

		cash_register.clearFocus();
		today_turnover.clearFocus();
		noon_time.clearFocus();
		noon_turnover.clearFocus();
		time.clearFocus();
		total.clearFocus();
		tomorrow_money.clearFocus();
		total_take_num.clearFocus();
		send_person.clearFocus();
		shop_money.clearFocus();
	}
}
