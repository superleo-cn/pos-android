package com.android.singaporeanorderingsystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
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
import com.android.bean.VersionBean;
import com.android.common.Constants;
import com.android.common.CrashHandler;
import com.android.common.MyApp;
import com.android.common.SystemHelper;
import com.android.dao.UserDao2;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;

public class LoginActivity extends Activity implements OnClickListener{
	// 返回的安装包url
	private String apkUrl = new String();
	/* 下载包安装路径 */
	// private static final String savePath = "/sdcard/VeryNCShop/";
	private static final String savePath = Constants.CACHE_DIR;

	private  String saveFileName = savePath + "/";

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;
	private Button ok_btn;
	private EditText login_name;
	private EditText login_password;
	private SharedPreferences sharedPrefs;
	private int save_year;
	private int save_month;
	private int save_day;
	private MyApp myApp;
	private ImageView image_logo_ico;
	private MyProcessDialog dialog;
	private MyUpdateDialog myUpdateDialog;
	private MyOrientationDetector m;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//	   	 .detectDiskReads()
//	   	 .detectDiskWrites()
//	   	 .detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
//	   	 .penaltyLog() //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
//	   	 .build());
//	   	 StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//	   	 .detectLeakedSqlLiteObjects() //探测SQLite数据库操作
//	   	.penaltyLog() //打印logcat
//	   	 .penaltyDeath()
//	   	 .build());
		super.onCreate(savedInstanceState);
		m=new MyOrientationDetector(LoginActivity.this);
		//CrashHandler crashHandler = CrashHandler.getInstance();   //错误监听 
//        crashHandler.init(LoginActivity.this);  //传入参数必须为Activity，否则AlertDialog将不显示。  
//		//横屏正方向
//
//		if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
//		{
//
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//		}
//		//横屏反方向
//		else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
//		{
//
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
		UpdateAPP();
		dialog =new MyProcessDialog(LoginActivity.this,getResources().getString(R.string.login_wait));
		myUpdateDialog =new MyUpdateDialog(LoginActivity.this);
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
		
//		if(year>=save_year&&month>=save_month&&day>=save_day){
//			PriceSave.getInatance(LoginActivity.this).delete();
//			//Toast.makeText(LoginActivity.this, "清空数据", Toast.LENGTH_SHORT).show();
//		}


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
				dialog.show();
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
						dialog.dismiss();
						if(data.getCode() == 1){
							String json=data.getJson();
							ArrayList<LoginUserBean> datas=LoginUserBean.newInstanceList(json);
							LoginUserBean user_bean=datas.get(0);
							if(!user_bean.getUsertype().equals("SUPERADMIN")){
								Toast.makeText(LoginActivity.this,getString(R.string.login_quanxian), Toast.LENGTH_SHORT).show();
								return;
							}
							myApp.setU_name(user_bean.getUsername());
							myApp.setUser_id(user_bean.getId());
							myApp.setU_type(user_bean.getUsertype());
							Toast.makeText(LoginActivity.this,getString(R.string.login_succ), Toast.LENGTH_SHORT).show();
							Intent  intent =new Intent();
							intent.setClass(LoginActivity.this, MainActivity.class);
							LoginActivity.this.startActivity(intent);
							LoginActivity.this.finish();
						}else if(data.getCode() == 0){
							Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
						}else if(data.getCode() == -1){
							Toast.makeText(LoginActivity.this, getString(R.string.login_service_err), Toast.LENGTH_SHORT).show();
						}
					}
				});
				return false;
			}
		});
	}
	/**
	 * 登录前检测版本更新
	 * */
	public void UpdateAPP(){
		String url = Constants.URL_UPDATE_APP;
		RemoteDataHandler.asyncGet(url, new Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				if(data.getCode() == 1){
					String json = data.getJson();
					ArrayList<VersionBean> datas=VersionBean.newInstanceList(json);
					if(datas !=null){
						final VersionBean vbean=  datas.get(0);
						if(!vbean.getVersionNo().equals(SystemHelper.getAppVersionCode(LoginActivity.this)+"")){
							myUpdateDialog.dialog_message.setText("有新版本（V"+vbean.getVersionNo()+"）升级");
							myUpdateDialog.show();
							myUpdateDialog.dialog_yes.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Toast.makeText(LoginActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
									if (Environment.MEDIA_MOUNTED.equals(Environment
											.getExternalStorageState())) {
										saveFileName += vbean.getName();
										apkUrl = Constants.URL_UPDATE_APP_DOWN + vbean.getName();
										downLoadThread = new Thread(mdownApkRunnable);
										downLoadThread.start();
									} else {
										myUpdateDialog.dismiss();
										Toast.makeText(LoginActivity.this, "亲，SD卡不在了，快去找找！！",
												Toast.LENGTH_SHORT).show();
									}
								}
							});
							myUpdateDialog.dialog_no.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									interceptFlag = true;
									myUpdateDialog.dismiss();
								}
							});
						}else{
							Toast.makeText(LoginActivity.this, "当前是最新版本", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
	}
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				myUpdateDialog.shuzhi.setText("下载:" + (progress + 1) + "%");
				myUpdateDialog.progress.setProgress(progress);
				break;
			case DOWN_OVER:
				Toast.makeText(LoginActivity.this, "下载完成现在安装！！", Toast.LENGTH_SHORT).show();
				myUpdateDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
		};
	};
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		LoginActivity.this.startActivity(i);

	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login_ok :
			dialog.show();
			String str_login_name=login_name.getText().toString();
			final String str_login_password=login_password.getText().toString();
//			final UserDao user_dao=myApp.getUserdao();
//			LoginUserBean user_bean = user_dao.select(str_login_name);
			final UserDao2 u_dao=UserDao2.getInatance(LoginActivity.this);
			ArrayList<LoginUserBean> u_datas=u_dao.getList(str_login_name);
			LoginUserBean user_bean = new LoginUserBean();
			if(u_datas != null && u_datas.size() !=0){
				user_bean = u_datas.get(0);
			}
			if(user_bean.getUsername() != null && str_login_password.equalsIgnoreCase(user_bean.getPasswrod())){
				dialog.dismiss();
				Toast.makeText(LoginActivity.this,getString(R.string.login_succ), Toast.LENGTH_SHORT).show();
				myApp.setU_name(user_bean.getUsername());
				myApp.setUser_id(user_bean.getId());
				myApp.setU_type(user_bean.getUsertype());
				Intent  intent =new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
				return ;
			}else if(user_bean.getUsername() != null && !str_login_password.equalsIgnoreCase(user_bean.getPasswrod())){
				dialog.dismiss();
				Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
				return ;
			}
			
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
			params.put("user.shop.id", myApp.getSettingShopId());
			params.put("user.userIp", str_ip);
			params.put("user.userMac", str_mac);
			RemoteDataHandler.asyncPost(Constants.URL_LOGIN_PATH, params, new Callback() {
				@Override
				public void dataLoaded(ResponseData data) {
					dialog.dismiss();
					if(data.getCode() == 1){
						String json=data.getJson();
						ArrayList<LoginUserBean> datas=LoginUserBean.newInstanceList(json);
						LoginUserBean user_bean=datas.get(0);
//						InfolabPasswordGen pass = new InfolabPasswordGen();
//						pass.generatePassword();
			            user_bean.setPasswrod(str_login_password);
//						user_dao.insert(user_bean);
						u_dao.save(user_bean);
						myApp.setUser_id(user_bean.getId());
						myApp.setU_name(user_bean.getUsername());
						myApp.setU_type(user_bean.getUsertype());
						Toast.makeText(LoginActivity.this,getString(R.string.login_succ), Toast.LENGTH_SHORT).show();
						Intent  intent =new Intent();
						intent.setClass(LoginActivity.this, MainActivity.class);
						LoginActivity.this.startActivity(intent);
						LoginActivity.this.finish();
					}else if(data.getCode() == 0){
						Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
					}else if(data.getCode() == -1){
						Toast.makeText(LoginActivity.this, getString(R.string.login_service_err), Toast.LENGTH_SHORT).show();
					}
				}
			});
			
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
//	    	Toast.makeText(this, "Locale in "+locale+" !", Toast.LENGTH_LONG).show();
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

class MyOrientationDetector extends OrientationEventListener{
	private Context context;
    public MyOrientationDetector( Context context ) {
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
