package com.android.singaporeanorderingsystem;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
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
import com.android.bean.FoodHttpBean;
import com.android.bean.FoodListBean;
import com.android.bean.FoodOrder;
import com.android.bean.GiditNumberBean;
import com.android.bean.SelectFoodBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.dao.FoodHttpBeanDao;
import com.android.dao.FoodOrderDao2;
import com.android.dialog.DialogBuilder;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;

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
	private RelativeLayout r_lay_id_take_package;
	private RelativeLayout r_lay_id_foc;
	private RelativeLayout r_lay_id_discount;
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
	private double show_totalPrice=0.00;
	private StringBuffer sbuff;
	private double show_gathering=0.00;
	private double show_surplus=0.00;
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
	private MyApp myApp;
	private double package_money;
	private double dabao_price=0;
	private double dazhe_price=0;
	private FoodOrderDao2 f_dao;
	public static String save_date="2013-11-24";
//	private MyOrientationDetector2 m;
	private SharedPreferences sharedPrefs;
	/*主菜单activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init_wifiReceiver();
//        m=new MyOrientationDetector2(MainActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
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
    	foc=(ImageView) this.findViewById(R.id.foc);
    	discount=(ImageView) this.findViewById(R.id.discount);
    	r_lay_id_take_package=(RelativeLayout) this.findViewById(R.id.r_lay_id_take_package);
    	r_lay_id_foc=(RelativeLayout) this.findViewById(R.id.r_lay_id_foc);
    	r_lay_id_discount=(RelativeLayout) this.findViewById(R.id.r_lay_id_discount);
    	r_lay_id_take_package.setOnClickListener(this);
    	r_lay_id_foc.setOnClickListener(this);
    	r_lay_id_discount.setOnClickListener(this);
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
					if(myApp.getU_type().equals("SUPERADMIN") || myApp.getU_type().equals("ADMIN") || 
							myApp.getU_type().equals("OPERATOR")){						
						Intent intent =new Intent(MainActivity.this , SettingActivity.class);
						overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
						Bundle bundle=new Bundle();
						bundle.putString("type", "1");
						intent.putExtras(bundle);
						MainActivity.this.startActivity(intent);
						MainActivity.this.finish();
					}else{
						Toast.makeText(MainActivity.this, getString(R.string.insufficientpermissions), Toast.LENGTH_SHORT).show();	
					}
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
					MainActivity.this.finish();
					overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
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
//    	Resources res =getResources();
//    	String[] food_name=res.getStringArray(R.array.food_name);
//    	String[] food_dayin_code=res.getStringArray(R.array.food_dayin_code);
//    	String[] food_type=res.getStringArray(R.array.food_type);
//    	String[] food_price=res.getStringArray(R.array.food_price);
//    	String[] food_id =res.getStringArray(R.array.food_id);
//    	for(int i=0;i<food_name.length;i++){
//    		FoodListBean bean=new FoodListBean();
//    		bean.setTitle(food_name[i]+"");
//    		bean.setDaping_id(food_dayin_code[i]+"");
//    		bean.setImageID(food_image[i]);  
//    		bean.setType(food_type[i]+"");
//    		bean.setFood_id(food_id[i]);
//    		bean.setPrice(food_price[i]+"");
//    		food_dataList.add(bean);
//    	}
    	sharedPrefs = getSharedPreferences("language", Context.MODE_PRIVATE);
    	String type = sharedPrefs.getString("type", "");
    	FoodHttpBeanDao fhb_dao =FoodHttpBeanDao.getInatance(MainActivity.this);
    	ArrayList<FoodHttpBean> datas=fhb_dao.getList();
    	for(int i = 0 ; i<datas.size() ;i++){
    		FoodHttpBean fhb=datas.get(i);
    		FoodListBean bean=new FoodListBean();
    		if(StringUtils.equalsIgnoreCase("zh", type)){
    			bean.setTitle(fhb.getNameZh());
    		}else{
    			bean.setTitle(fhb.getName());
    		}
    		bean.setDaping_id(fhb.getSn());
    		bean.setImageID(fhb.getPicture());  
    		bean.setType(fhb.getType());
    		bean.setFood_id(fhb.getId());
    		bean.setPrice(fhb.getRetailPrice()+"");
    		food_dataList.add(bean);
    	}
    	
    	FoodListAdapter adapter=new FoodListAdapter(this,food_dataList,handler);
    	foodView.setAdapter(adapter);
    }
    
    
    public void init_giditNum_view(){
    	List<GiditNumberBean> dataList=new ArrayList<GiditNumberBean>();  	
    	String delete=String.valueOf(R.string.delete);
    	String []str=new String[]{"7","8","9","4","5","6","1","2","3","0",".","C"};
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
				if(food_dataList.get(arg2).getType().equals("STAPLE")){
					
				}else{
				save_selectNum++;
				}
				SelectFoodBean bean=new SelectFoodBean();
				bean.setFood_name(food_dataList.get(arg2).getTitle());
				bean.setFood_price(food_dataList.get(arg2).getPrice());
				bean.setFood_dayin_code(food_dataList.get(arg2).getDaping_id());
				bean.setFood_id(food_dataList.get(arg2).getFood_id());
				bean.setFood_type(food_dataList.get(arg2).getType());
				if(frist){
					//show_totalPrice=save_foc_price;
					show_totalPrice+=Double.parseDouble(food_dataList.get(arg2).getPrice());
					
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
					bean.setFood_num("1");
					select_dataList.add(bean);
					select_adapter.notifyDataSetChanged();
					total_price.setText(df.format(show_totalPrice));
					}
					//计算总金额
					add();
				}
			}});
    }
    
    public void onclick_giditNum_view(){
    	giditNum_view.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String number = "";
				switch(arg2){
				case 0:
					sbuff.append(7);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
							gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
								}catch(Exception e){
									Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
								}
					}
					compute_surplus();
					break;
				case 1:
					sbuff.append(8);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
							gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
								}catch(Exception e){
									Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
								}
					}
					compute_surplus();
					break;
				case 2:
					sbuff.append(9);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
							gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
								}catch(Exception e){
									Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
								}
					}
					compute_surplus();
					break;
				case 3:
					sbuff.append(4);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
							gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
								}catch(Exception e){
									Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
								}
					}
					compute_surplus();
					break;
				case 4:
					sbuff.append(5);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
							gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
								}catch(Exception e){
									Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
								}
					}
					compute_surplus();
					break;
				case 5:
					sbuff.append(6);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
							gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
								}catch(Exception e){
									Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
								}
					}
					compute_surplus();
					break;
				case 6:
					sbuff.append(1);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
							gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
								}catch(Exception e){
									Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
								}
					}
					compute_surplus();
					break;
				case 7:
					sbuff.append(2);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
						gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
					}catch(Exception e){
						Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
					}
					}
					compute_surplus();
					break;
				case 8:
					sbuff.append(3);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
							gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
								}catch(Exception e){
									Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
								}
					}
					compute_surplus();
					break;
				case 9:
					sbuff.append(0);
					number = sbuff.toString();
					if(number.indexOf(".") > -1){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
					}
					if(is_maxPrice()){
						gathering.setText("9999.99");
					}else{
						try{
							gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
								}catch(Exception e){
									Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
								}
					}
					compute_surplus();
					break;
				case 10:
					sbuff.append(".");
					number = sbuff.toString();
					if(NumberUtils.isNumber(number)){
						sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
						if(is_maxPrice()){
							gathering.setText("9999.99");
						}else{
							try{
						gathering.setText(Double.parseDouble(sbuff.toString().trim())+"");
							}catch(Exception e){
								Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
							}
						}
						compute_surplus();
					}else{
						Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
					}
					break;
				case 11:
					int sb_length=sbuff.length();
					sbuff.delete(0, sb_length);
					gathering.setText("0.00");
					surplus.setText("0.00");
					compute_surplus();
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
					add();
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
					bean.setFood_id(food_dataList.get(num).getFood_id());
					bean.setFood_num("1");
					bean.setFood_dayin_code(food_dataList.get(num).getDaping_id());
					for(int i=select_dataList.size()-1;i>=0;i--){
						SelectFoodBean remove_bean=select_dataList.get(i);
						if(remove_bean.getFood_name().equals(bean.getFood_name())){
							int number=Integer.parseInt(remove_bean.getFood_num());
							number=number-1;
							if(food_dataList.get(num).getType().equals("STAPLE")){
								
							}else{
							save_selectNum--;
							}
							if(number==0){
								select_dataList.remove(i);
								show_totalPrice-=Double.parseDouble(food_dataList.get(num).getPrice());
								select_adapter.notifyDataSetChanged();
								//total_price.setText("0.00");
								if(select_dataList.size()==0){
									frist=true;
								}
							}else{
								select_dataList.get(i).setFood_num(String.valueOf(number));
								double price=Double.parseDouble(select_dataList.get(i).getFood_price());
								price=price-Double.parseDouble(food_dataList.get(num).getPrice());
								select_dataList.get(i).setFood_price(df.format(price));
								show_totalPrice-=Double.parseDouble(food_dataList.get(num).getPrice());
								select_adapter.notifyDataSetChanged();
								//total_price.setText(df.format(show_totalPrice));
							}
							
							break;
						}
					}
					
				}
				add();
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
		case R.id.r_lay_id_take_package:
			if(is_foc){
				take_package.setImageResource(R.drawable.package_not_select);
				//Toast.makeText(this, "取消了打包", Toast.LENGTH_SHORT).show();
				is_takePackage=false;
			}else{
			
			if(!is_takePackage){
				if(select_dataList.size()==0){
					Toast.makeText(this, R.string.selec_not_food, Toast.LENGTH_SHORT).show();
				}else{
								take_package.setImageResource(R.drawable.package_seclect);
				//Toast.makeText(this, "全部打包", Toast.LENGTH_SHORT).show();				
				is_takePackage=true;
				add();
				}
			}else{
				
				take_package.setImageResource(R.drawable.package_not_select);
				//Toast.makeText(this, "取消了打包", Toast.LENGTH_SHORT).show();
				is_takePackage=false;
				add();
			
			}
			}
			break;
		case R.id.r_lay_id_foc:

			
			if(!is_foc){
				if(select_dataList.size()==0){
					Toast.makeText(this, R.string.selec_not_food, Toast.LENGTH_SHORT).show();
				}else{
				foc.setImageResource(R.drawable.package_seclect);
				//Toast.makeText(this, "免单", Toast.LENGTH_SHORT).show();
				is_foc=true;
				is_discount=false;
				discount.setImageResource(R.drawable.package_not_select);
				take_package.setImageResource(R.drawable.package_not_select);
				is_takePackage=false;
				add();
				}
			}else{
				foc.setImageResource(R.drawable.package_not_select);
				//Toast.makeText(this, "不免单", Toast.LENGTH_SHORT).show();
				is_foc=false;
				add();
			
			}
			break;
		case R.id.r_lay_id_discount:
			if(is_foc){
				take_package.setImageResource(R.drawable.package_not_select);
				//Toast.makeText(this, "取消了打包", Toast.LENGTH_SHORT).show();
				is_takePackage=false;
			}else{
			
			if(!is_discount){
				if(select_dataList.size()==0){
					Toast.makeText(this, R.string.selec_not_food, Toast.LENGTH_SHORT).show();
				}else{
				discount.setImageResource(R.drawable.package_seclect);
				//Toast.makeText(this, "打折", Toast.LENGTH_SHORT).show();
				is_discount=true;
				add();
				}
			}else{
				discount.setImageResource(R.drawable.package_not_select);
				//Toast.makeText(this, "不打折", Toast.LENGTH_SHORT).show();
				is_discount=false;
				add();
			
			}
			}
			break;
		case R.id.ok_btn:
//			FoodOrder food_order=new FoodOrder();
//			food_order.setDiscount(dazhe_price);//打折钱数
//			if(is_foc){
//				food_order.setFoc("1");//是否免费 1是 0否
//			}else{
//				food_order.setFoc("0");//是否免费 1是 0否
//			}
//			food_order.setFood_flag("0");//是否成功 1是 0否
//			food_order.setShop_id("1");//店idmyApp.getShopid()
//			food_order.setTotalpackage(dabao_price);//打包钱数
//			food_order.setUser_id(myApp.getUser_id());//用户id
//			food_order.setRetailprice(df.format(show_totalPrice));//收钱数
//			String Foodid = "0";
//			String Quantity = "0";
//			for(int i = 0 ; i < select_dataList.size() ; i ++){
//				SelectFoodBean  bean=select_dataList.get(i);
//				if(i == select_dataList.size()-1){
//					Foodid+=bean.getFood_id();
//					Quantity+=bean.getFood_num();
//				}else{
//					Foodid+=bean.getFood_id()+",";
//					Quantity+=bean.getFood_num()+",";
//				}
//			}
//			food_order.setFoodid(Foodid);//食物id
//			food_order.setQuantity(Quantity);//数量
//			System.out.println("-->"+food_order.toString());
//			food_dao.insert(food_order);
//			HashMap<String, String> params= new HashMap<String,String>();
//			ArrayList<FoodOrder> datas=food_dao.findall();
//			System.out.println("-->"+datas.size());
//			for(int i = 0 ; i<datas.size() ; i++){
//				FoodOrder f_order =  datas.get(i);
//				if(f_order.getFood_flag().equals("0")){
//					String [] foodid=f_order.getFoodid().split(",");
//					String [] Food_num=f_order.getQuantity().split(",");
//					for(int j = 0; j<foodid.length ;j++){
//						params.put("transactions["+j+"].androidId", f_order.getAndroid_id());
//						System.out.println("transactions["+j+"].androidId-->"+f_order.getAndroid_id());
//						params.put("transactions["+j+"].user.id", f_order.getUser_id());
//						System.out.println("transactions["+j+"].user.id-->"+f_order.getUser_id());
//						params.put("transactions["+j+"].shop.id", f_order.getShop_id());
//						System.out.println("transactions["+j+"].shop.id-->"+ f_order.getShop_id());
//						params.put("transactions["+j+"].quantity", Food_num[i]);
//						System.out.println("transactions["+j+"].quantity-->"+ Food_num[i]);
//						params.put("transactions["+j+"].food.id", foodid[i]);
//						System.out.println("transactions["+j+"].food.id-->"+ foodid[i]);
//						params.put("transactions["+j+"].totalDiscount", f_order.getDiscount());
//						System.out.println("transactions["+j+"].totalDiscount-->"+ f_order.getDiscount());
//						params.put("transactions["+j+"].totalRetailPrice", f_order.getRetailprice());
//						System.out.println("transactions["+j+"].totalRetailPrice-->"+f_order.getRetailprice());
//						params.put("transactions["+j+"].totalPackage", f_order.getTotalpackage());
//						System.out.println("transactions["+j+"].totalPackage-->"+ f_order.getTotalpackage());
//						params.put("transactions["+j+"].freeOfCharge", f_order.getFoc());
//						System.out.println("transactions["+j+"].freeOfCharge-->"+ f_order.getFoc());
//					}
//				}
//			}
			 SimpleDateFormat df_save=new SimpleDateFormat("yyyy-MM-dd");
		    	String date=df_save.format(new Date());
		    	save_date=date;
			long result_price=PriceSave.getInatance(MainActivity.this).save(myApp.getUser_id(),date,total_price.getText().toString(),myApp.getSettingShopId());
			if(result_price==-1){
				Log.e("保存价格失败", "");
			}else{
				Log.e("保存价格成功", "");
			}
			f_dao = FoodOrderDao2.getInatance(MainActivity.this);
			for(int i = 0 ; i < select_dataList.size() ; i ++){
				SelectFoodBean  bean=select_dataList.get(i);
				FoodOrder food_order=new FoodOrder();
				//food_order.setDiscount(dazhe_price+"");//打折钱数
				food_order.setFood_flag("0");//是否成功 1是 0否
				food_order.setShop_id(myApp.getSettingShopId());//店idmyApp.getShopid()

				food_order.setTotalpackage(bean.getDabao_price() + "");//打包钱数
				food_order.setDiscount(bean.getDazhe_price() + ""); //打折钱数
				food_order.setUser_id(myApp.getUser_id());//用户id
//				food_order.setRetailprice(Double.parseDouble( bean.getFood_price())*Double.parseDouble(bean.getFood_num())+"");//收钱数
				double totalRetailPrice = Double.parseDouble( bean.getFood_price()) - bean.getDazhe_price() + bean.getDabao_price();
				if(is_foc){
					food_order.setFoc("1");//是否免费 1是 0否
					totalRetailPrice = 0;
				}else{
					food_order.setFoc("0");//是否免费 1是 0否
				}
				food_order.setRetailprice(totalRetailPrice + "");//收钱数
				food_order.setFoodid(bean.getFood_id());//食物id
				food_order.setQuantity(bean.getFood_num());//数量
				DateFormat timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				food_order.setDate(timestamp.format(new Date()));
				f_dao.save(food_order);
			}
			
			HashMap<String, String> params= new HashMap<String,String>();
			ArrayList<FoodOrder> datas=f_dao.getList("0");
			System.out.println("-->"+datas.size());
			for(int i = 0 ; i<datas.size() ; i++){
				FoodOrder f_order =  datas.get(i);
				if(f_order.getFood_flag().equals("0")){
					System.out.println("f_order.getFood_flag()-->"+f_order.getFood_flag());
						params.put("transactions["+i+"].androidId", f_order.getAndroid_id());
						System.out.println("transactions["+i+"].androidId-->"+f_order.getAndroid_id());
						params.put("transactions["+i+"].user.id", f_order.getUser_id());
						System.out.println("transactions["+i+"].user.id-->"+f_order.getUser_id());
						params.put("transactions["+i+"].shop.id", f_order.getShop_id());
						System.out.println("transactions["+i+"].shop.id-->"+ f_order.getShop_id());
						params.put("transactions["+i+"].quantity", f_order.getQuantity());
						System.out.println("transactions["+i+"].quantity-->"+ f_order.getQuantity());
						params.put("transactions["+i+"].food.id", f_order.getFoodid());
						System.out.println("transactions["+i+"].food.id-->"+ f_order.getFoodid());
						params.put("transactions["+i+"].totalDiscount", f_order.getDiscount());
						System.out.println("transactions["+i+"].totalDiscount-->"+ f_order.getDiscount());
						params.put("transactions["+i+"].totalRetailPrice", f_order.getRetailprice());
						System.out.println("transactions["+i+"].totalRetailPrice-->"+f_order.getRetailprice());
						params.put("transactions["+i+"].totalPackage", f_order.getTotalpackage());
						System.out.println("transactions["+i+"].totalPackage-->"+ f_order.getTotalpackage());
						params.put("transactions["+i+"].freeOfCharge", f_order.getFoc());
						System.out.println("transactions["+i+"].freeOfCharge-->"+ f_order.getFoc());
						params.put("transactions["+i+"].orderDate", f_order.getDate());
						System.out.println("transactions["+i+"].orderDate-->"+ f_order.getDate());
				}
			}
			RemoteDataHandler.asyncPost(Constants.URL_FOOD_ORDER, params, new Callback() {
				@Override
				public void dataLoaded(ResponseData data) {
					if(data.getCode() == 1){
						f_dao.update_all_type("0");
					}else if(data.getCode() == 0){
						String json = data.getJson();
						System.out.println("-----111>>>>>>"+json);
						json=json.replaceAll("\\[", "");
						json=json.replaceAll("\\]", "");
						System.out.println("-----222>>>>>>"+json);
						String [] str=json.split(",");
						for(int i = 0; i<str.length;i++){
							System.out.println("-----333>>>>>>"+str[i]);
							f_dao.update_type(str[i]);
						}
					}else if(data.getCode() == -1){
						
					}
				}
			});
			try{
				Log.e("输入的金额", sbuff.toString().trim());
				if(sbuff==null||sbuff.toString().trim().equals("") ){
					sbuff.append("0");
				}
				show_gathering=Double.parseDouble(sbuff.toString().trim());
				gathering.setText(df.format(show_gathering));
				double result=show_totalPrice;
//				if(result == 0.00){
//					Toast.makeText(this,R.string.selec_not_food, Toast.LENGTH_SHORT).show();
//				}else{
				
					if(sbuff.toString().trim().equals("0") || sbuff.toString().trim().equals("0.0")){
						surplus.setText(df.format(show_surplus));
					}else{
					Log.e("最后金额", show_totalPrice+"");
					show_surplus=show_gathering-result;
					surplus.setText(df.format(show_surplus));
//					int sb_length=sbuff.length();
//					sbuff.delete(0, sb_length);
					}
//					Show_print().create().show();
//				}
			}catch(Exception e){
				Toast.makeText(this, R.string.err_price, Toast.LENGTH_SHORT).show();
			}
//			if(select_dataList!=null && select_dataList.size() !=0){
//				Show_print().create().show();
//			}
		
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
				
				StringBuffer sb=new StringBuffer();
				SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String time = sdf.format(new Date());
				sb.append(time+"\n\n");
				FoodHttpBeanDao fhb_dao =FoodHttpBeanDao.getInatance(MainActivity.this);
		    	int fixSize = fhb_dao.getList().size();
				int size = select_dataList.size();
				for(int i = 0 ; i < fixSize ; i++){
					if(i < size){
						SelectFoodBean bean=select_dataList.get(i);
						String foodName = bean.getFood_dayin_code()+" / "+bean.getFood_name();
						String qty = "X"+bean.getFood_num()+"\n\n";
						if(is_takePackage){
							foodName+="(包)";
						}
						sb.append(foodName + "     " + qty);
					}else{
						sb.append(" \n\n");
					}
				}
				myApp.getPrinter().setIp(myApp.getIp_str());
				myApp.getPrinter().print(sb.toString());
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
	    	IntentFilter filter1=new IntentFilter();
	    	 filter1.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	    	registerReceiver(wifi_myReceiver,filter1);
	    	main_isRever=true;
	    }
	    

	@Override
	protected void onResume() {
//		m.enable();
		 select_dataList=new ArrayList<SelectFoodBean>();
	        sbuff=new StringBuffer();
	        initView();
	        df=new DecimalFormat("0.00");
	        save_discount_price=Double.parseDouble(myApp.getDiscount());
	        package_money=0.2;
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

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
		//	unregisterReceiver(wifi_myReceiver);
			super.onDestroy();
		}
	public void add(){
		show_totalPrice=0;
		dabao_price=0;
		dazhe_price=0;
		if(select_dataList.size()==0){
			total_price.setText(df.format(show_totalPrice));
		}else{
		for(int i=0;i<select_dataList.size();i++){
			int num=Integer.parseInt(select_dataList.get(i).getFood_num());
			Double price=Double.parseDouble(select_dataList.get(i).getFood_price());
			select_dataList.get(i).setDabao_price(0);
			select_dataList.get(i).setDazhe_price(0);
			show_totalPrice+=price;
			if(is_foc){
				show_totalPrice=0;
				dabao_price=0;
				dazhe_price=0;
				select_dataList.get(i).setDabao_price(0);
				select_dataList.get(i).setDazhe_price(0);
			}else{
				double dabao = num * package_money;
				double dazhe = num * save_discount_price;
				String type = select_dataList.get(i).getFood_type();
				if(StringUtils.equalsIgnoreCase(type, "DISH")){
					if(!is_discount&&is_takePackage){
						show_totalPrice=show_totalPrice+dabao;
						dabao_price+=dabao;
						dazhe_price=0;
						dazhe=0;
					}else if(is_discount&&!is_takePackage){
						show_totalPrice=show_totalPrice-dazhe;
						dabao_price=0;
						dazhe_price+=dazhe;
						dabao=0;
					}else if(!is_discount&&!is_takePackage){
						dabao_price=0;
						dazhe_price=0;
						dabao = 0;
						dazhe = 0;
					}else if(is_discount&&is_takePackage){
						show_totalPrice=show_totalPrice+dabao-dazhe;
						dabao_price+=dabao;
						dazhe_price+=dazhe;
					}
					select_dataList.get(i).setDabao_price(dabao);
					select_dataList.get(i).setDazhe_price(dazhe);
				}
			}
		}
		total_price.setText(df.format(show_totalPrice));
		if(Double.parseDouble(gathering.getText().toString())>0){
		compute_surplus();
		}
		}
	}
	
	public void compute_surplus(){
		try{
		Double get_gathering=Double.parseDouble(gathering.getText().toString());
		Double get_total_price=Double.parseDouble(total_price.getText().toString());
		surplus.setText(df.format(get_gathering-get_total_price));
		}catch(Exception e){
			Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
		}

	}
	 public boolean is_maxPrice(){
		 try{
	    	Double now_price=Double.parseDouble(sbuff.toString().trim());
	    	if(now_price>9999.99){
	    		return true;
	    	}
		 }catch (Exception e){
			 Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
			 return false;
		 }
	    	return false;
		
	    }
//	@Override
//	protected void onPause() {
//		super.onPause();
//		m.disable();
//	}
}
class MyOrientationDetector2 extends OrientationEventListener{
	private Context context;
    public MyOrientationDetector2( Context context ) {
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
