package com.android.singaporeanorderingsystem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.adapter.FoodListAdapter;
import com.android.adapter.GiditNumberAdapter;
import com.android.adapter.SelectListAdapter;
import com.android.bean.FoodListBean;
import com.android.bean.GiditNumberBean;
import com.android.bean.SelectFoodBean;
import com.android.common.MyApp;
import com.android.dialog.DialogBuilder;

public class MainActivity extends Activity implements OnClickListener{
	
	private ImageView menu; //menu按钮
	private TextView login_name; //用户名字
	private RelativeLayout exit_layout; //退出
	private ImageView wifi_image; //wifi 图标
	private GridView foodView;  //菜品列表
	private ListView select_list; //选择的菜品
	private TextView total_price; //总价格
	private TextView gathering; //收款
	private TextView surplus; //剩余
	private ImageView take_package; //打包选项
	private ImageView foc;     //FOC
	private ImageView discount; //打折选项
	private GridView giditNum_view; //0-9按钮   用gridView做的按钮
	private Button ok_btn; //ok 按钮
	private boolean frist=true;//首次选择
	private final int GETLIST=1001;
	private final int OPEN_WIFI=1002;
	private final int CLOSE_WIFI=1003;
	private List<SelectFoodBean> select_dataList;
	private List<FoodListBean> food_dataList;
	private SelectListAdapter select_adapter;
	private PopupWindow popupWindow;
	private View view;
	private double show_totalPrice;
	private StringBuffer sbuff;
	private double show_gathering;
	private double show_surplus;
	private boolean is_takePackage;
	private boolean is_discount;
	private boolean is_foc;
	private boolean is_revice;
	private boolean is_moreClick;
	private DecimalFormat df;
	private double save_foc_price;
	private double save_discount_price;
	private int save_selectNum;
	public static boolean main_isRever;
	private SharedPreferences sharedPrefs;
	private MyApp myApp;
	/*主菜单activity*/
	private Integer[] food_image={
			R.drawable.food_image01,
			R.drawable.food_image02,
			R.drawable.food_image03,
			R.drawable.food_image04,
			R.drawable.food_image05,
			R.drawable.food_image06,
			R.drawable.food_image07,
			R.drawable.food_image08,
			R.drawable.food_image09,
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    /*初始化控件*/
    public void initView(){
    	myApp= (MyApp) MainActivity.this.getApplication();
    	menu=(ImageView) this.findViewById(R.id.menu_btn);
    	menu.setOnClickListener(this);
    	login_name=(TextView) this.findViewById(R.id.login_name);
    	exit_layout=(RelativeLayout) this.findViewById(R.id.layout_exit);
    	exit_layout.setOnClickListener(this);
    	wifi_image=(ImageView) this.findViewById(R.id.wifi_iamge);
    	foodView=(GridView) this.findViewById(R.id.food_list);
    	select_list=(ListView) this.findViewById(R.id.select_list);
    	total_price=(TextView) this.findViewById(R.id.total_price);
    	gathering=(TextView) this.findViewById(R.id.gathering);
    	surplus=(TextView) this.findViewById(R.id.surplus);
    	take_package=(ImageView) this.findViewById(R.id.take_package);
    	take_package.setOnClickListener(this);
    	foc=(ImageView) this.findViewById(R.id.foc);
    	foc.setOnClickListener(this);
    	discount=(ImageView) this.findViewById(R.id.discount);
    	discount.setOnClickListener(this);
    	giditNum_view=(GridView) this.findViewById(R.id.digit_btn);
    	ok_btn=(Button) this.findViewById(R.id.ok_btn);
    	ok_btn.setOnClickListener(this);
    	initData();
    	login_name.setText(myApp.getU_name()+getString(R.string.mainTitle_txt));
    }
    
    /*初始化数据*/
    public void initData(){
//        sbuff.append(0);
    	init_foodView();
    	init_giditNum_view();
    	onclick_foodView();
    	onclick_giditNum_view();
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
			popu_diancai.setVisibility(View.GONE);
			popu_setting.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
					Intent intent =new Intent(MainActivity.this , SettingActivity.class);
					MainActivity.this.startActivity(intent);
					overridePendingTransition(
							R.anim.in_from_right,
							R.anim.out_to_left);
					//MainActivity.this.finish();
				}
			});
			
			popu_daily.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
					Intent intent =new Intent(MainActivity.this , DailyPayActivity.class);
					MainActivity.this.startActivity(intent);
					overridePendingTransition(
							R.anim.in_from_right,
							R.anim.out_to_left);
					//MainActivity.this.finish();
				}});
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
    public void init_foodView(){
    	food_dataList=new ArrayList<FoodListBean>();   
    	Resources res =getResources();
    	String[] food_name=res.getStringArray(R.array.food_name);
    	String[] food_dayin_code=res.getStringArray(R.array.food_dayin_code);
    	String[] food_type=res.getStringArray(R.array.food_type);
    	String[] food_price=res.getStringArray(R.array.food_price);
    	for(int i=0;i<food_name.length;i++){
    		FoodListBean bean=new FoodListBean();
    		bean.setTitle(food_name[i]+"");
    		bean.setDaping_id(food_dayin_code[i]);
    		bean.setImageID(food_image[i]);  
    		bean.setType(food_type[i]);
    		bean.setFood_id(i+1+"");
    		bean.setPrice(food_price[i]);
    		food_dataList.add(bean);
//    		if(i>=5){
//    			bean.setType("0"); //主食
//    			String main_food=String.valueOf(R.string.main_food);
//    			bean.setTitle("food"+i);
//    		}else{
//    			bean.setType("1"); //菜品
//    			String vegetable=String.valueOf(R.string.vegetable);
//    			bean.setTitle("caipin"+i);
//    		}
//    		String price=i+".00";
//    		bean.setPrice(String.valueOf(price));
//    		food_dataList.add(bean);
    	}
    	FoodListAdapter adapter=new FoodListAdapter(this,food_dataList,handler);
    	foodView.setAdapter(adapter);
    }
    
    
    public void init_giditNum_view(){
    	List<GiditNumberBean> dataList=new ArrayList<GiditNumberBean>();  	
    	String delete=String.valueOf(R.string.delete);
    	String []str=new String[]{"1","2","3","4","5","6","7","8","9","0",".","删除"};
    	for(int i=0;i<str.length;i++){
    		GiditNumberBean bean=new GiditNumberBean();
    		bean.setNumber(str[i]);
    		dataList.add(bean);
    	}
    	GiditNumberAdapter adapter=new GiditNumberAdapter(this,dataList);
    	giditNum_view.setAdapter(adapter);
    }
    public void onclick_foodView(){
    	foodView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(food_dataList.get(arg2).getType().equals("0")){
					
				}else{
				save_selectNum++;
				}
				if(frist){
					SelectFoodBean bean=new SelectFoodBean();
					bean.setFood_name(food_dataList.get(arg2).getTitle());
					bean.setFood_price(food_dataList.get(arg2).getPrice());
					show_totalPrice+=Double.parseDouble(food_dataList.get(arg2).getPrice());
					if(is_foc){
						save_foc_price=show_totalPrice;
						if(is_discount){
							if(food_dataList.get(arg2).getType().equals("0")){
								
							}else{
							save_foc_price=show_totalPrice-0.5;
							}
							
						}
						if(is_takePackage){
							if(food_dataList.get(arg2).getType().equals("0")){
								
							}else{
							save_foc_price=show_totalPrice+0.2;
							}
						}
						
						show_totalPrice=0;
					}else{
						if(is_discount){
							if(food_dataList.get(arg2).getType().equals("0")){
								
							}else{
							show_totalPrice=show_totalPrice-0.5;
							}
						}
						if(is_takePackage){
							if(food_dataList.get(arg2).getType().equals("0")){
								
							}else{
							show_totalPrice=show_totalPrice+0.2;
							}
						}
						save_foc_price=show_totalPrice;
					}
					bean.setFood_num("1");
					select_dataList.add(bean);
					new Thread() 
					{
						public void run() 
						{
							Message msg = new Message();
							msg.what = GETLIST;
							msg.obj = select_dataList;
							handler.sendMessage(msg);
						}
					}.start();
				}else{
					SelectFoodBean bean=new SelectFoodBean();
					bean.setFood_name(food_dataList.get(arg2).getTitle());
					for(int i=select_dataList.size()-1;i>=0;i--){
						SelectFoodBean add_bean=select_dataList.get(i);
						if(add_bean.getFood_name().equals(bean.getFood_name())){
							is_moreClick=true;
							int num=Integer.parseInt(add_bean.getFood_num());
							num=num+1;
							double price=Double.parseDouble(select_dataList.get(i).getFood_price());
							price=price+Double.parseDouble(food_dataList.get(arg2).getPrice());
							select_dataList.get(i).setFood_num(String.valueOf(num));
							select_dataList.get(i).setFood_price(df.format(price));
							show_totalPrice+=Double.parseDouble(food_dataList.get(arg2).getPrice());
							if(is_foc){
								save_foc_price=show_totalPrice;
								if(is_discount){
									if(food_dataList.get(arg2).getType().equals("0")){
										
									}else{
									save_foc_price=show_totalPrice-0.5;
									}
								}
								if(is_takePackage){
									if(food_dataList.get(arg2).getType().equals("0")){
										
									}else{
									save_foc_price=show_totalPrice+0.2;
									}
								}
								
								show_totalPrice=0;
							}else{
								if(is_discount){
									if(food_dataList.get(arg2).getType().equals("0")){
										
									}else{
									show_totalPrice=show_totalPrice-0.5;
									}
								}
								if(is_takePackage){
									if(food_dataList.get(arg2).getType().equals("0")){
										
									}else{
									show_totalPrice=show_totalPrice+0.2;
									}
								}
								save_foc_price=show_totalPrice;
							}
							select_adapter.notifyDataSetChanged();
							total_price.setText(df.format(show_totalPrice));
							break;
						}else{
							is_moreClick=false;
						}
					}
					if(!is_moreClick){
					bean.setFood_price(food_dataList.get(arg2).getPrice());
					show_totalPrice+=Double.parseDouble(food_dataList.get(arg2).getPrice());
					if(is_foc){
						save_foc_price=show_totalPrice;
						if(is_discount){
							if(food_dataList.get(arg2).getType().equals("0")){
								
							}else{
							save_foc_price=show_totalPrice-0.5;
							}
						}
						if(is_takePackage){if(food_dataList.get(arg2).getType().equals("0")){
							
						}else{
							
							save_foc_price=show_totalPrice+0.2;
						}
						}
						show_totalPrice=0;
					}else{
						if(is_discount){
							if(food_dataList.get(arg2).getType().equals("0")){
								
							}else{
							show_totalPrice=show_totalPrice-0.5;
							}
						}
						if(is_takePackage){
							if(food_dataList.get(arg2).getType().equals("0")){
								
							}else{
							show_totalPrice=show_totalPrice+0.2;
							}
						}
						save_foc_price=show_totalPrice;
					}
					bean.setFood_num("1");
					select_dataList.add(bean);
					select_adapter.notifyDataSetChanged();
					total_price.setText(df.format(show_totalPrice));
					}
				}
			}});
    }
    
    public void onclick_giditNum_view(){
    	giditNum_view.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(arg2){
				case 0:
					sbuff.append(1);
					gathering.setText(sbuff.toString().trim());
					break;
				case 1:
					sbuff.append(2);
					gathering.setText(sbuff.toString().trim());
					break;
				case 2:
					sbuff.append(3);
					gathering.setText(sbuff.toString().trim());
					break;
				case 3:
					sbuff.append(4);
					gathering.setText(sbuff.toString().trim());
					break;
				case 4:
					sbuff.append(5);
					gathering.setText(sbuff.toString().trim());
					break;
				case 5:
					sbuff.append(6);
					gathering.setText(sbuff.toString().trim());
					break;
				case 6:
					sbuff.append(7);
					gathering.setText(sbuff.toString().trim());
					break;
				case 7:
					sbuff.append(8);
					gathering.setText(sbuff.toString().trim());
					break;
				case 8:
					sbuff.append(9);
					gathering.setText(sbuff.toString().trim());
					break;
				case 9:
					sbuff.append(0);
					gathering.setText(sbuff.toString().trim());
					break;
				case 10:
					sbuff.append(".");
					gathering.setText(sbuff.toString().trim());
					break;
				case 11:
					int sb_length=sbuff.length();
					sbuff.delete(0, sb_length);
					gathering.setText("0.00");
					surplus.setText("0.00");
					break;
				}
			}});
    }
    Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case GETLIST:
				if(msg.obj==null){
					
				}else{
					select_dataList=(List<SelectFoodBean>) msg.obj;
					select_adapter=new SelectListAdapter(MainActivity.this,select_dataList);
					select_list.setAdapter(select_adapter);
					total_price.setText(df.format(show_totalPrice));
					frist=false;
				}
				
				break;
			case OPEN_WIFI:
				wifi_image.setBackgroundResource(R.drawable.wifi_open);
				break;
			case CLOSE_WIFI:
				wifi_image.setBackgroundResource(R.drawable.wifi_close);
				break;
			case FoodListAdapter.LESS_DATALIST:
				int num=(Integer) msg.obj;
				if(select_dataList.size()!=0){
					Log.e("item", num+"");
					SelectFoodBean bean=new SelectFoodBean();
					bean.setFood_name(food_dataList.get(num).getTitle());
					bean.setFood_price(food_dataList.get(num).getPrice());
					bean.setFood_num("1");
					for(int i=select_dataList.size()-1;i>=0;i--){
						SelectFoodBean remove_bean=select_dataList.get(i);
						if(remove_bean.getFood_name().equals(bean.getFood_name())){
							int number=Integer.parseInt(remove_bean.getFood_num());
							number=number-1;
							if(food_dataList.get(num).getType().equals("0")){
								
							}else{
							save_selectNum--;
							}
							if(number==0){
								select_dataList.remove(i);
								show_totalPrice-=Double.parseDouble(food_dataList.get(num).getPrice());
								if(is_foc){
									save_foc_price=show_totalPrice;
									if(is_discount){
										if(food_dataList.get(num).getType().equals("0")){
											
										}else{
										save_foc_price=show_totalPrice-0.5;
										}
									}
									if(is_takePackage){
										if(food_dataList.get(num).getType().equals("0")){
											
										}else{
										save_foc_price=show_totalPrice+0.2;
										}
									}
									show_totalPrice=0;
								}else{
									if(is_discount){
										if(food_dataList.get(num).getType().equals("0")){
											
										}else{
										show_totalPrice=show_totalPrice+0.5;
										}
									}
									if(is_takePackage){
										if(food_dataList.get(num).getType().equals("0")){
											
										}else{
										show_totalPrice=show_totalPrice-0.2;
										}
									}
									save_foc_price=show_totalPrice;
								}
								select_adapter.notifyDataSetChanged();
								total_price.setText(df.format(show_totalPrice));
								if(select_dataList.size()==0){
									frist=true;
								}
							}else{
								select_dataList.get(i).setFood_num(String.valueOf(number));
								double price=Double.parseDouble(select_dataList.get(i).getFood_price());
								price=price-Double.parseDouble(food_dataList.get(num).getPrice());
								select_dataList.get(i).setFood_price(df.format(price));
								show_totalPrice-=Double.parseDouble(food_dataList.get(num).getPrice());
								if(is_foc){
									save_foc_price=show_totalPrice;
									if(is_discount){
										if(food_dataList.get(num).getType().equals("0")){
											
										}else{
										save_foc_price=show_totalPrice-0.5;
										}
									}
									if(is_takePackage){
										if(food_dataList.get(num).getType().equals("0")){
											
										}else{
										save_foc_price=show_totalPrice+0.2;
										}
									}
									show_totalPrice=0;
								}else{
									if(is_discount){
										if(food_dataList.get(num).getType().equals("0")){
											
										}else{
										show_totalPrice=show_totalPrice+0.5;
										}
									}
									if(is_takePackage){
										if(food_dataList.get(num).getType().equals("0")){
											
										}else{
										show_totalPrice=show_totalPrice-0.2;
										}
									}
									save_foc_price=show_totalPrice;
								}
								select_adapter.notifyDataSetChanged();
								total_price.setText(df.format(show_totalPrice));
							}
							
							break;
						}
					}
					
				}
				break;
			}
			
			super.handleMessage(msg);
		}
    	
    };
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.menu_btn:
			initPopupWindow();
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.showAsDropDown(menu, 0, -5);
			break;
		case R.id.layout_exit:
			CreatedDialog().create().show();
			
			break;
		case R.id.take_package:
			if(select_dataList.size()==0){
				Toast.makeText(this, R.string.selec_not_food, Toast.LENGTH_SHORT).show();
			}else{
			if(!is_takePackage){
				take_package.setImageResource(R.drawable.package_seclect);
				//Toast.makeText(this, "全部打包", Toast.LENGTH_SHORT).show();				
				is_takePackage=true;
				//判断是否免单
				if(is_foc){
					save_foc_price=save_foc_price+save_selectNum*0.2;
				//	Toast.makeText(this, "打包后价格："+save_foc_price, Toast.LENGTH_SHORT).show();
				}else{
					show_totalPrice=show_totalPrice+save_selectNum*0.2;
					save_foc_price=show_totalPrice;
					total_price.setText(df.format(show_totalPrice));
				}
			}else{
				take_package.setImageResource(R.drawable.package_not_select);
				//Toast.makeText(this, "取消了打包", Toast.LENGTH_SHORT).show();
				is_takePackage=false;
				//判断是否免单
				if(is_foc){
					save_foc_price=save_foc_price-save_selectNum*0.2;
				//	Toast.makeText(this, "取消打包后价格："+save_foc_price, Toast.LENGTH_SHORT).show();
				}else{
					show_totalPrice=show_totalPrice-save_selectNum*0.2;
					save_foc_price=show_totalPrice;
					total_price.setText(df.format(show_totalPrice));
				}
			}
			}
			break;
		case R.id.foc:
			if(select_dataList.size()==0){
				Toast.makeText(this, R.string.selec_not_food, Toast.LENGTH_SHORT).show();
			}else{
			if(!is_foc){
				foc.setImageResource(R.drawable.package_seclect);
				//Toast.makeText(this, "免单", Toast.LENGTH_SHORT).show();
				is_foc=true;
				if(!is_discount&&!is_takePackage){
				save_foc_price=show_totalPrice;
				}
				total_price.setText("0.00");
			}else{
				foc.setImageResource(R.drawable.package_not_select);
				//Toast.makeText(this, "不免单", Toast.LENGTH_SHORT).show();
				is_foc=false;
				show_totalPrice=save_foc_price;
				total_price.setText(df.format(save_foc_price));
			}
			}
			break;
		case R.id.discount:
			if(select_dataList.size()==0){
				Toast.makeText(this, R.string.selec_not_food, Toast.LENGTH_SHORT).show();
			}else{
			if(!is_discount){
				discount.setImageResource(R.drawable.package_seclect);
				//Toast.makeText(this, "打折", Toast.LENGTH_SHORT).show();
				is_discount=true;
				//save_discount_price=Double.parseDouble(total_price.getText().toString());
				if(is_foc){
					save_foc_price=save_foc_price-save_selectNum*0.5;
					//Toast.makeText(this, "打折后价格："+save_foc_price, Toast.LENGTH_SHORT).show();
				}else{
					show_totalPrice=show_totalPrice-save_selectNum*0.5;
					save_foc_price=show_totalPrice;
					total_price.setText(df.format(show_totalPrice));
				}
			}else{
				discount.setImageResource(R.drawable.package_not_select);
				//Toast.makeText(this, "不打折", Toast.LENGTH_SHORT).show();
				is_discount=false;
				if(is_foc){
					save_foc_price=save_foc_price+save_selectNum*0.5;
				//	Toast.makeText(this, "取消打折后价格："+save_foc_price, Toast.LENGTH_SHORT).show();
				}else{
					show_totalPrice=show_totalPrice+save_selectNum*0.5;
					save_foc_price=show_totalPrice;
					total_price.setText(df.format(show_totalPrice));
				}
			}
			}
			break;
		case R.id.ok_btn:
			try{
				Log.e("输入的金额", sbuff.toString().trim());
				show_gathering=Double.parseDouble(sbuff.toString().trim());
				gathering.setText(df.format(show_gathering));
				double result=show_totalPrice;
				if(result == 0.00){
					Toast.makeText(this,R.string.selec_not_food, Toast.LENGTH_SHORT).show();
				}else{
					if(sbuff.toString().trim().equals("0") || sbuff.toString().trim().equals("0.0")){
						surplus.setText(df.format(show_surplus));
					}else{
					Log.e("最后金额", show_totalPrice+"");
					show_surplus=show_gathering-result;
					surplus.setText(df.format(show_surplus));
//					int sb_length=sbuff.length();
//					sbuff.delete(0, sb_length);
					}
					Show_print().create().show();
				}
			}catch(Exception e){
				Toast.makeText(this, R.string.err_price, Toast.LENGTH_SHORT).show();
			}
		
			break;
		}
	}
	
	public DialogBuilder CreatedDialog(){
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
	
	public DialogBuilder Show_print(){
		DialogBuilder builder=new DialogBuilder(this);
		builder.setTitle(R.string.message_title);
		builder.setMessage(R.string.open_print);
		builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				long result=PriceSave.getInatance(MainActivity.this).save(total_price.getText().toString());
				if(result==-1){
					Log.e("保存价格失败", "");
				}else{
					Log.e("保存价格成功", "");
				}
				if(false){
					myApp.getPrinter().print("测试数据。。。");
				}
				clear_data();
			}});
		builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int which) {
				int sb_length=sbuff.length();
				sbuff.delete(0, sb_length);
				gathering.setText("0.00");
				surplus.setText("0.00");
			}});
		return builder;
	}
	
	public void clear_data(){
		if(select_dataList.size()!=0){
		select_dataList.clear();
		select_adapter.notifyDataSetChanged();
		}
		frist=true;
		total_price.setText("0.00");
		gathering.setText("0.00");
		surplus.setText("0.00");
		show_totalPrice=0.00;;
		show_gathering=0.00;
		show_surplus=0.00;
		int sb_length=sbuff.length();
		sbuff.delete(0, sb_length);
		is_takePackage=false;
		take_package.setImageResource(R.drawable.package_not_select);
		is_discount=false;
		discount.setImageResource(R.drawable.package_not_select);
		is_foc=false;
		foc.setImageResource(R.drawable.package_not_select);
		save_foc_price=0.00;
		save_selectNum=0;
	}
	 private BroadcastReceiver wifi_myReceiver=new BroadcastReceiver()
	    {

			@Override
			public void onReceive(Context context, Intent intent) {
				 String action = intent.getAction();
		            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo  mobInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo  wifiInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(!mobInfo.isConnected()&&!wifiInfo.isConnected()){
					new Thread(new Runnable(){

						public void run() {
							Message msg = new Message();
							msg.what=CLOSE_WIFI;
							handler.sendMessage(msg);
						}
						
					}).start();
				
				}else{
					new Thread(new Runnable(){

						public void run() {
							Message msg = new Message();
							msg.what=OPEN_WIFI;
							handler.sendMessage(msg);
						}
						
					}).start();
				}
			}
			}
	    };
	    
	    private void init_wifiReceiver()
	    {
	    	if(!main_isRever){
	    	IntentFilter filter=new IntentFilter();
	    	 filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	    	registerReceiver(wifi_myReceiver,filter);
	    	main_isRever=true;
	    	}
	    }
	    

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		 sharedPrefs= getSharedPreferences("language", Context.MODE_PRIVATE);
			String type=sharedPrefs.getString("type", "");
			if(type.isEmpty()){
				type="zh";
			}
			if(type.equals("zh")){
				updateLange(Locale.SIMPLIFIED_CHINESE);
			}else{
				updateLange(Locale.ENGLISH);
			}
		 select_dataList=new ArrayList<SelectFoodBean>();
	        sbuff=new StringBuffer();
	       init_wifiReceiver();
	        initView();
	        df=new DecimalFormat("0.00");
	       
		super.onResume();
	}
	 
	public void updateActivity() {
		  Intent intent = new Intent();
		  intent.setClass(this,LoginActivity.class);//当前Activity重新打开
		  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  startActivity(intent);
		 
		 }

		private void updateLange(Locale locale){    	
			 Resources res = getResources();
			 Configuration config = res.getConfiguration();
			 config.locale = locale;
			 DisplayMetrics dm = res.getDisplayMetrics();
			 res.updateConfiguration(config, dm);        
	    	Toast.makeText(this, "Locale in "+locale+" !", Toast.LENGTH_LONG).show();
	    	//updateActivity();  

	    }
	
}
