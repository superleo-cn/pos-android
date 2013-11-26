package com.android.singaporeanorderingsystem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.bean.FoodHttpBean;
import com.android.bean.GetPTakeNumBean;
import com.android.bean.GetPayDetailBean;
import com.android.common.Constants;
import com.android.common.HttpHelper;
import com.android.common.MyApp;
import com.android.dao.DailyMoneyDao;
import com.android.dao.FoodHttpBeanDao;
import com.android.dao.GetTakeNumDao;
import com.android.dao.NumListDao;
import com.android.dao.PayListDao;
import com.android.dao.getDetailPayListDao;
import com.android.dialog.DialogBuilder;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;

public class SettingActivity extends Activity {

	private EditText language_set;
	private EditText print_one_edit;
	private EditText shop_set;
	private EditText take_price_edit;
	private boolean is_chinese;
	private SharedPreferences sharedPrefs;
	private PopupWindow popupWindow;
	private View view;
	private ImageView menu;
	private Button synchronization_menu;
	private Button synchronization_shop;
	private Button btu_discount;
	private Button synchronization_pay;
	private Button price_set_brn;
	public static String type;
	private Button print_one_btu;
	private Button btu_setting_all_tong;
	private MyApp myApp;
	private TextView admin_set;
	private RelativeLayout r_set_admin_lay;
	private RelativeLayout layout_exit;
	private SharedPreferences spf;
	private FoodHttpBeanDao fhb_dao;
	private MyProcessDialog dialog;
	private String search_date;
	private MyOrientationDetector3 m;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
   	 .detectDiskReads()
   	 .detectDiskWrites()
   	 .detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
   	 .penaltyLog() //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
   	 .build());
   	 StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
   	 .detectLeakedSqlLiteObjects() //探测SQLite数据库操作
   	.penaltyLog() //打印logcat
   	 .penaltyDeath()
   	 .build());
   	m=new MyOrientationDetector3(SettingActivity.this);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);	
		myApp = (MyApp) SettingActivity.this.getApplication();
		dialog=new MyProcessDialog(this,getResources().getString(R.string.dialog_set));
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		type = bundle.getString("type");
		language_set = (EditText) findViewById(R.id.language_set);
		print_one_edit = (EditText) findViewById(R.id.print_one_edit);
		take_price_edit = (EditText) findViewById(R.id.take_price_edit);
		admin_set = (TextView) findViewById(R.id.admin_set);
		r_set_admin_lay =(RelativeLayout) findViewById(R.id.r_set_admin_lay);
		layout_exit =(RelativeLayout) findViewById(R.id.layout_exit);
		shop_set = (EditText) findViewById(R.id.shop_set);
		menu = (ImageView) findViewById(R.id.menu_btn);
		print_one_btu = (Button) findViewById(R.id.print_one_btu);
		btu_discount = (Button) findViewById(R.id.btu_discount);
		synchronization_menu = (Button) findViewById(R.id.synchronization_menu_brn);
		synchronization_shop = (Button) findViewById(R.id.synchronization_shop_brn);
		synchronization_pay=(Button) this.findViewById(R.id.synchronization_pay_brn);
		btu_setting_all_tong =(Button) this.findViewById(R.id.btu_setting_all_tong);
		price_set_brn=(Button) this.findViewById(R.id.price_set_brn);
		sharedPrefs = getSharedPreferences("language", Context.MODE_PRIVATE);
		String type = sharedPrefs.getString("type", "");
		if(myApp.getU_type().equals("SUPERADMIN")){
			admin_set.setVisibility(View.VISIBLE);
			r_set_admin_lay.setVisibility(View.VISIBLE);
		}
		btu_setting_all_tong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    	String date=df.format(new Date());
		    	SettingActivity.this.search_date=date;
		    	post_payList();
		    	post_numList(); 
		    	post_dailyMoney();
			}
		});
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initPopupWindow();
				popupWindow.setFocusable(true);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.showAsDropDown(menu, 0, -5);
			}
		});
		print_one_edit.setText(myApp.getIp_str());
		take_price_edit.setText(myApp.getDiscount());
		shop_set.setText(myApp.getSettingShopId());
		if (type == null) {
			type = "en";
		}
		if (type.equals("zh")) {
			is_chinese = true;
		} else {
			is_chinese = false;
		}
		if (!is_chinese) {
			language_set.setText("English");
		} else {
			language_set.setText("中文");
		}
		layout_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreatedDialog().create().show();
			}
		});
		print_one_btu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String ip = print_one_edit.getText().toString();
				myApp.setIp_str(ip);
				myApp.getPrinter().reconnect();
				Toast.makeText(SettingActivity.this, getString(R.string.toast_setting_succ), Toast.LENGTH_SHORT).show();
			}
		});
		btu_discount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text_discount=take_price_edit.getText().toString();
				myApp.setDiscount(text_discount);
				Toast.makeText(SettingActivity.this, getString(R.string.toast_setting_succ), Toast.LENGTH_SHORT).show();
			}
		});

		// 设置摊位ID,第一次超管必须设置好
		synchronization_shop.setOnClickListener(new OnClickListener() {;
			@Override
			public void onClick(View v) {
				String shop_id = shop_set.getText().toString();
				myApp.setSettingShopId(shop_id);
				Toast.makeText(SettingActivity.this, getString(R.string.toast_setting_succ), Toast.LENGTH_SHORT)
						.show();
			}
		});
		synchronization_menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("myApp.getSettingShopId()-->"+myApp.getSettingShopId());
				dialog.show();
				if(myApp.getSettingShopId() == null || myApp.getSettingShopId().equals("") ||
						myApp.getSettingShopId().equals("null")||
						myApp.getSettingShopId().equals("0")){
					Toast.makeText(SettingActivity.this, getString(R.string.setting_tanwei_id), Toast.LENGTH_SHORT).show();
				return ;
				}
				String url = Constants.URL_FOODSLIST_PATH+myApp.getSettingShopId();
				RemoteDataHandler.asyncGet(url, new Callback() {
					@Override
					public void dataLoaded(ResponseData data) {
						if(data.getCode() == 1){
							String json =data.getJson();
							ArrayList<FoodHttpBean> datas=FoodHttpBean.newInstanceList(json);
							fhb_dao =FoodHttpBeanDao.getInatance(SettingActivity.this);
							fhb_dao.delete();
							for(int i=0 ; i< datas.size() ; i ++){
								FoodHttpBean food_h_bean=datas.get(i);
								System.out.println("f-->"+food_h_bean.getPicture()+",i--->"+i);
								try {
									System.out.println("11111111111111");
									String image_file=Constants.CACHE_IMAGE+"/"+"food_image_"+i+".png";
									HttpHelper.download(food_h_bean.getPicture(),new File(image_file));
									food_h_bean.setPicture(image_file);
									fhb_dao.save(food_h_bean);
									
								} catch (IOException e) {
									e.printStackTrace();
								}
								
							}
							dialog.cancel();
						}else if(data.getCode() == -1){
							
						}
					}
				});
			}
		});

		// 语言设置
		language_set.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(SettingActivity.this, getString(R.string.toast_setting_language_succ), Toast.LENGTH_SHORT).show();
				DialogBuilder builder=new DialogBuilder(SettingActivity.this);
				builder.setTitle(R.string.message_title);
				builder.setMessage(R.string.message_2);
				builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						//Toast.makeText(DailyPayActivity.this, "你点击了确定", Toast.LENGTH_SHORT).show();
						if(!is_chinese){
						updateLange(Locale.SIMPLIFIED_CHINESE);
						language_set.setText("中文");
						Editor editor = sharedPrefs.edit();
						editor.putString("type", "zh");
						editor.commit();
						is_chinese=true;
						}else{
							updateLange(Locale.ENGLISH);
							language_set.setText("English");
							Editor editor = sharedPrefs.edit();
							editor.putString("type", "en");
							editor.commit();
							is_chinese=false;
						}
						getDetailPayListDao.getInatance(SettingActivity.this).delete();
						RemoteDataHandler.asyncGet(Constants.URL_PAY_DETAIL+myApp.getSettingShopId(),new Callback() {
							@Override
							public void dataLoaded(ResponseData data) {
								if(data.getCode() == 1){
									String json=data.getJson();
									Log.e("返回数据", json);
									Log.e("中英文",is_chinese+"" );
									ArrayList<GetPayDetailBean> datas=GetPayDetailBean.newInstanceList(json,is_chinese);
									Log.e("支付页详情数据", datas.size()+"");
									for(int i=0;i<datas.size();i++){
										GetPayDetailBean bean=datas.get(i);
										getDetailPayListDao.getInatance(SettingActivity.this).save(bean.getId(), bean.getName());
									}
								}else if(data.getCode() == 0){
									Toast.makeText(SettingActivity.this, "支付页失败", Toast.LENGTH_SHORT).show();
								}else if(data.getCode() == -1){
									Toast.makeText(SettingActivity.this, getString(R.string.login_service_err), Toast.LENGTH_SHORT).show();
								}
							}
						});
					}});
				builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						//Toast.makeText(DailyPayActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
					}});
				builder.create().show();
				
			}});
		
		//支付款同步
		synchronization_pay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.show();
				getDetailPayListDao.getInatance(SettingActivity.this).delete();
				RemoteDataHandler.asyncGet(Constants.URL_PAY_DETAIL+myApp.getSettingShopId(),new Callback() {
					@Override
					public void dataLoaded(ResponseData data) {
						if(data.getCode() == 1){
							String json=data.getJson();
							Log.e("返回数据", json);
							Log.e("中英文",is_chinese+"" );
							ArrayList<GetPayDetailBean> datas=GetPayDetailBean.newInstanceList(json,is_chinese);
							Log.e("支付页详情数据", datas.size()+"");
							for(int i=0;i<datas.size();i++){
								GetPayDetailBean bean=datas.get(i);
								getDetailPayListDao.getInatance(SettingActivity.this).save(bean.getId(), bean.getName());
							}
							dialog.cancel();
							Toast.makeText(SettingActivity.this, getString(R.string.toast_setting_succ), Toast.LENGTH_SHORT)
							.show();	
						}else if(data.getCode() == 0){
							Toast.makeText(SettingActivity.this, "支付页失败", Toast.LENGTH_SHORT).show();
						}else if(data.getCode() == -1){
							Toast.makeText(SettingActivity.this, getString(R.string.login_service_err), Toast.LENGTH_SHORT).show();
						}
					}
				});
			}});
		
		//现金配置
		price_set_brn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
				GetTakeNumDao.getInatance(SettingActivity.this).delete();
				RemoteDataHandler.asyncGet(Constants.URL_TAKE_DNUM+myApp.getSettingShopId(),new Callback() {
					@Override
					public void dataLoaded(ResponseData data) {
						if(data.getCode() == 1){
							String json=data.getJson();
							Log.e("金额配置返回数据", json);
							ArrayList<GetPTakeNumBean> datas=GetPTakeNumBean.newInstanceList(json,is_chinese);
							Log.e("金额配置详情数据", datas.size()+"");
							for(int i=0;i<datas.size();i++){
								GetPTakeNumBean bean=datas.get(i);
								GetTakeNumDao.getInatance(SettingActivity.this).save(bean.getId(), bean.getPrice());
							}
							dialog.cancel();
							Toast.makeText(SettingActivity.this, getString(R.string.toast_setting_succ), Toast.LENGTH_SHORT)
							.show();	
						}else if(data.getCode() == 0){
							Toast.makeText(SettingActivity.this, "金额配置失败", Toast.LENGTH_SHORT).show();
						}else if(data.getCode() == -1){
							Toast.makeText(SettingActivity.this, getString(R.string.login_service_err), Toast.LENGTH_SHORT).show();
						}
					}
				});
			}});
	}
	/***********************************************************************************/
	
	/*提交每日支付*/
	public void post_payList(){
		try{
		HashMap<String, String> params= new HashMap<String,String>();
		List<Map<String,String>> datas=PayListDao.getInatance(this).getList(search_date);
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
					Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
				String str=json.substring(1,json.length()-1);
				String []array=str.split(",");
				if(array.length!=0){
					for(int i=0;i<array.length;i++){
						Log.e("数据组",array[i]+"");
					int result=	PayListDao.getInatance(SettingActivity.this).update_type(array[i], "1");
					if(result==-1){
						//Toast.makeText(DailyPayActivity.this, "每日支付接口更新失败", Toast.LENGTH_SHORT).show();
					}else{
						//Toast.makeText(DailyPayActivity.this, "每日支付接口更新成功", Toast.LENGTH_SHORT).show();
					}
						
					}
				}
				}else if(data.getCode() == 0){
					Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_fail), Toast.LENGTH_SHORT).show();
				}else if(data.getCode() == -1){
					Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_err), Toast.LENGTH_SHORT).show();
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
			List<Map<String,String>> datas=NumListDao.getInatance(this).getList(search_date);
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
						Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
					String str=json.substring(1,json.length()-1);
					String []array=str.split(",");
					if(array.length!=0){
						for(int i=0;i<array.length;i++){
							Log.e("数据组",array[i]+"");
						int result=	NumListDao.getInatance(SettingActivity.this).update_type(array[i], "1");
						if(result==-1){
							//Toast.makeText(DailyPayActivity.this, "带回总数接口更新失败", Toast.LENGTH_SHORT).show();
						}else{
							//Toast.makeText(DailyPayActivity.this, "带回总数接口更新成功", Toast.LENGTH_SHORT).show();
						}
							
						}
					}
					}else if(data.getCode() == 0){
						Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_fail), Toast.LENGTH_SHORT).show();
					}else if(data.getCode() == -1){
						Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_err), Toast.LENGTH_SHORT).show();
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
			HashMap<String, String> params= DailyMoneyDao.getInatance(SettingActivity.this).getList(search_date);
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
						Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
						int result=DailyMoneyDao.getInatance(SettingActivity.this).update_type(search_date);
						if(result==-1){
							Toast.makeText(SettingActivity.this, "每日营业额更新失败", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(SettingActivity.this, "每日营业额更新成功", Toast.LENGTH_SHORT).show();
						}
					}else if(data.getCode() == 0){
						Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_fail), Toast.LENGTH_SHORT).show();
					}else if(data.getCode() == -1){
						Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_err), Toast.LENGTH_SHORT).show();
					}
				}
			});
			}catch(Exception e){
				e.getMessage();
			}
	}
	
	/*********************************************************************************/
	
	public void initPopupWindow() {
		if (popupWindow == null) {
			view = this.getLayoutInflater().inflate(R.layout.popupwindow, null);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			popupWindow.setOutsideTouchable(true);
			TextView popu_setting = (TextView) view
					.findViewById(R.id.popu_setting);
			TextView popu_exit = (TextView) view.findViewById(R.id.popu_exit);
			TextView popu_daily = (TextView) view.findViewById(R.id.popu_daily);
			TextView popu_diancai = (TextView) view
					.findViewById(R.id.popu_diancai);
			popu_diancai.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
					Intent intent = new Intent(SettingActivity.this,
							MainActivity.class);
					SettingActivity.this.startActivity(intent);
					// overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
					SettingActivity.this.finish();
				}
			});
			;
			popu_setting.setVisibility(View.GONE);

			popu_daily.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
					Intent intent = new Intent(SettingActivity.this,
							DailyPayActivity.class);
					overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
					SettingActivity.this.startActivity(intent);
					SettingActivity.this.finish();
				}
			});
			popu_exit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
					CreatedDialog().create().show();
				}
			});
		}
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	public void updateActivity() {
		Intent intent = new Intent();
		intent.setClass(this, SettingActivity.class);// 当前Activity重新打开
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle bundle = new Bundle();
		bundle.putString("type", type);
		intent.putExtras(bundle);
		startActivity(intent);
		this.finish();

	}

	public DialogBuilder CreatedDialog() {
		DialogBuilder builder = new DialogBuilder(this);
		builder.setTitle(R.string.message_title);
		builder.setMessage(R.string.message_exit);
		builder.setPositiveButton(R.string.message_ok,
				new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						System.exit(0);
					}
				});
		builder.setNegativeButton(R.string.message_cancle,
				new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				});
		return builder;
	}

	private void updateLange(Locale locale) {
		Resources res = getResources();
		Configuration config = res.getConfiguration();
		config.locale = locale;
		DisplayMetrics dm = res.getDisplayMetrics();
		res.updateConfiguration(config, dm);
		Toast.makeText(this, "Locale in " + locale + " !", Toast.LENGTH_LONG)
				.show();
		updateActivity();

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		 InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.hideSoftInputFromWindow(print_one_edit.getWindowToken(), 0); //强制隐藏键盘 
		 imm.hideSoftInputFromWindow(shop_set.getWindowToken(), 0); //强制隐藏键盘 
		 imm.hideSoftInputFromWindow(take_price_edit.getWindowToken(), 0); //强制隐藏键盘 
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (type.equals("1")) {
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
				this.finish();
			} else {
				Intent intent = new Intent();
				intent.setClass(this, DailyPayActivity.class);
				startActivity(intent);
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		super.onResume();
		m.enable();
	}
	@Override
	protected void onPause() {
		super.onPause();
		m.disable();
	}
}
class MyOrientationDetector3 extends OrientationEventListener{
	private Context context;
    public MyOrientationDetector3( Context context ) {
        super(context );
        this.context=context;
    }
    @Override
    public void onOrientationChanged(int orientation) {
    	if(orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
    	    return;  //手机平放时，检测不到有效的角度
    	}
    	//只检测是否有四个角度的改变
    	if( orientation > 350 || orientation< 10 ) { //0度
    	     orientation = 0;
    	}  
    	else if( orientation > 80 &&orientation < 100 ) { //90度
    	    orientation= 90;
    	    ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    	}
    	else if( orientation > 170 &&orientation < 190 ) { //180度
    	    orientation= 180;
    	}
    	else if( orientation > 260 &&orientation < 280  ) { //270度
    	    orientation= 270;
    	    ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	}
    	else {
    	    return;
    	}
    	Log.i("MyOrientationDetector ","onOrientationChanged:"+orientation);
    }
}