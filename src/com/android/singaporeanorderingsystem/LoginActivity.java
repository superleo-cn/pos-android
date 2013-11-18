package com.android.singaporeanorderingsystem;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.R;
import com.android.bean.LoginUserBean;
import com.android.common.Constants;
import com.android.common.InfolabPasswordGen;
import com.android.common.MyApp;
import com.android.common.SystemHelper;
import com.android.dao.UserDao;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;

public class LoginActivity extends Activity implements OnClickListener{
	
	private Button ok_btn;
	private EditText login_name;
	private EditText login_password;
	private SharedPreferences sharedPrefs;
	private int save_year;
	private int save_month;
	private int save_day;
	private MyApp myApp;
	private ImageView image_logo_ico;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		Calendar calendar=Calendar.getInstance();
		int year=calendar.WEEK_OF_YEAR;
		int month=calendar.WEEK_OF_MONTH;
		int day=calendar.DAY_OF_WEEK;
		
		save_year=sharedPrefs.getInt("year", 0);
		save_month=sharedPrefs.getInt("month", 0);
		save_day=sharedPrefs.getInt("day", 0);
		
		if(year>=save_year&&month>=save_month&&day>=save_day){
			PriceSave.getInatance(LoginActivity.this).delete();
			//Toast.makeText(LoginActivity.this, "清空数据", Toast.LENGTH_SHORT).show();
		}


		Editor editor = sharedPrefs.edit();
		editor.putInt("year", year);
		editor.putInt("month", month);
		editor.putInt("day", day);
		editor.commit();
		this.setContentView(R.layout.login);
		myApp = (MyApp) LoginActivity.this.getApplication();
		ok_btn=(Button) this.findViewById(R.id.login_ok);
		login_name = (EditText) LoginActivity.this.findViewById(R.id.login_name);
		login_password = (EditText) LoginActivity.this.findViewById(R.id.login_password);
		image_logo_ico = (ImageView) findViewById(R.id.image_logo_ico);
		ok_btn.setOnClickListener(LoginActivity.this);
		login_name.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘 	
				}
			}
		});
		login_password.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘 	
				}
			}
		});

		image_logo_ico.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				String str_login_name=login_name.getText().toString();
				String str_login_password=login_password.getText().toString();
				String str_ip = "0";
				String str_mac = "0";
				try {
					str_ip=SystemHelper.getLocalIPAddress() == null ? "0":SystemHelper.getLocalIPAddress();
					str_mac=SystemHelper.getLocalMacAddress(LoginActivity.this)== null ? "0":SystemHelper.getLocalMacAddress(LoginActivity.this);
				} catch (SocketException e) {
					e.printStackTrace();
				}
				HashMap<String, String> params =new HashMap<String, String>();
				params.put("user.username", str_login_name);
				params.put("user.password", str_login_password);
				params.put("user.userIp", str_ip);
				params.put("user.userMac", str_mac);
				RemoteDataHandler.asyncPost(Constants.URL_LOGIN_ADMIN_PATH, params, new Callback() {
					@Override
					public void dataLoaded(ResponseData data) {
						if(data.getCode() == 1){
							String json=data.getJson();
							ArrayList<LoginUserBean> datas=LoginUserBean.newInstanceList(json);
							LoginUserBean user_bean=datas.get(0);
							if(!user_bean.getUsertype().equals("SUPERADMIN")){
								Toast.makeText(LoginActivity.this,"你不是管理员无权登录", Toast.LENGTH_SHORT).show();
								return;
							}
							myApp.setU_name(user_bean.getUsername());
							myApp.setUser_id(user_bean.getId());
							myApp.setU_type(user_bean.getUsertype());
							Toast.makeText(LoginActivity.this,"恭喜你，登录成功", Toast.LENGTH_SHORT).show();
							Intent  intent =new Intent();
							intent.setClass(LoginActivity.this, MainActivity.class);
							LoginActivity.this.startActivity(intent);
							LoginActivity.this.finish();
						}else if(data.getCode() == 0){
							Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
						}else if(data.getCode() == -1){
							Toast.makeText(LoginActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
						}
					}
				});
				return false;
			}
		});

	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login_ok :

//			String str_login_name=login_name.getText().toString();
//			final String str_login_password=login_password.getText().toString();
//			final UserDao user_dao=myApp.getUserdao();
//			LoginUserBean user_bean = user_dao.select(str_login_name);
//			if(user_bean.getUsername() != null && str_login_password.equalsIgnoreCase(user_bean.getPasswrod())){
//				Toast.makeText(LoginActivity.this,"恭喜你，登录成功", Toast.LENGTH_SHORT).show();
//				myApp.setU_name(user_bean.getUsername());
//				myApp.setUser_id(user_bean.getId());
//				myApp.setU_type(user_bean.getUsertype());
//				Intent  intent =new Intent();
//				intent.setClass(LoginActivity.this, MainActivity.class);
//				LoginActivity.this.startActivity(intent);
//				LoginActivity.this.finish();
//				return ;
//			}else if(user_bean.getUsername() != null && !str_login_password.equalsIgnoreCase(user_bean.getPasswrod())){
//				Toast.makeText(LoginActivity.this, "登录失败,用户名或者密码错误", Toast.LENGTH_SHORT).show();
//				return ;
//			}
//			
//			String str_ip = "0";
//			String str_mac = "0";
//			try {
//				str_ip=SystemHelper.getLocalIPAddress() == null ? "0":SystemHelper.getLocalIPAddress();
//				str_mac=SystemHelper.getLocalMacAddress(LoginActivity.this)== null ? "0":SystemHelper.getLocalMacAddress(LoginActivity.this);
//			} catch (SocketException e) {
//				e.printStackTrace();
//			}
//			HashMap<String, String> params =new HashMap<String, String>();
//			params.put("user.username", str_login_name);
//			params.put("user.password", str_login_password);
//			params.put("user.shop.id", myApp.getSettingShopId());
//			params.put("user.userIp", str_ip);
//			params.put("user.userMac", str_mac);
//			RemoteDataHandler.asyncPost(Constants.URL_LOGIN_PATH, params, new Callback() {
//				@Override
//				public void dataLoaded(ResponseData data) {
//					if(data.getCode() == 1){
//						String json=data.getJson();
//						ArrayList<LoginUserBean> datas=LoginUserBean.newInstanceList(json);
//						LoginUserBean user_bean=datas.get(0);
////						InfolabPasswordGen pass = new InfolabPasswordGen();
////						pass.generatePassword();
//			            user_bean.setPasswrod(str_login_password);
//						user_dao.insert(user_bean);
//						myApp.setUser_id(user_bean.getId());
//						myApp.setU_name(user_bean.getUsername());
//						myApp.setU_type(user_bean.getUsertype());
//						Toast.makeText(LoginActivity.this,"恭喜你，登录成功,您的新密码是+"+str_login_password, Toast.LENGTH_SHORT).show();
						Intent  intent =new Intent();
						intent.setClass(LoginActivity.this, MainActivity.class);
						LoginActivity.this.startActivity(intent);
						LoginActivity.this.finish();
//					}else if(data.getCode() == 0){
//						Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//					}else if(data.getCode() == -1){
//						Toast.makeText(LoginActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
//					}
//				}
//			});
			
			break;
		}
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
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			 InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
			 imm.hideSoftInputFromWindow(login_password.getWindowToken(), 0); 
			 imm.hideSoftInputFromWindow(login_name.getWindowToken(), 0); //强制隐藏键盘 
			return super.onTouchEvent(event);
		}
		
		
}
