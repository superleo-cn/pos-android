/**
 * ClassName:MyApp.java
 * PackageName:com.shopnc_local_life.android.common
 * Create On 2013-8-6下午4:52:02
 * Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 * EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-2-18 hjgang All rights reserved.
 */
package com.android.common;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

import com.android.dao.FoodOrderDao;
import com.android.dao.UserDao;

/**
 * Author:hjgang
 * Create On 2013-8-6下午4:52:02
 * Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 * EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-8-6 hjgang All rights reserved.
 */
public class MyApp extends Application{
	private Context context;
	/** 系统初始化配置文件操作器 */
	private SharedPreferences sysInitSharedPreferences;
	private UserDao userdao;
	private FoodOrderDao food_order_dao;
	private AndroidPrinter printer;
	private String u_name = "";
	private String user_id= "0";
	private String discount="0.5";
	private String u_type = "CACHIER";
	private String daily_pay_submit_flag="1";
	private String shop_name = "";;
	private String shop_code = "";;
	private long setting_time=0;

	@Override
	public void onCreate() {
		super.onCreate();
		createCacheDir();
		sysInitSharedPreferences = getSharedPreferences(
				Constants.SYSTEM_INIT_FILE_NAME, MODE_PRIVATE);
		discount= sysInitSharedPreferences.getString("discount", "0.5");
		setting_time = sysInitSharedPreferences.getLong("setting_time", 30*60*1000 );
		userdao = new UserDao(this);
		food_order_dao = new FoodOrderDao(this);
		printer = new AndroidPrinter(this);
	}

	public String getDiscount() {
		String discount = sysInitSharedPreferences.getString("discount", "0.5");
		return discount;
	}

	public String getU_type() {
		return u_type;
	}

	public void setU_type(String u_type) {
		this.u_type = u_type;
	}

	public String getDaily_pay_submit_flag() {
		//daily_pay_submit_flag = sysInitSharedPreferences.getString("daily_pay_submit_flag", "1");
		return daily_pay_submit_flag;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
		sysInitSharedPreferences.edit().putString("discount", discount).commit();
	}

	
	
	public long getSetting_time() {
		return setting_time;
	}

	public void setSetting_time(long setting_time) {
		this.setting_time = setting_time;
		sysInitSharedPreferences.edit().putLong("setting_time", setting_time).commit();
	}

	public String getIp_str() {
		String ip_str = sysInitSharedPreferences.getString("ip_str", "192.168.0.1");
		return ip_str;
	}

	public void setIp_str(String ip_str) {
		sysInitSharedPreferences.edit().putString("ip_str", ip_str).commit();
	}
	
	public String getSettingShopId() {
		return sysInitSharedPreferences.getString("shop_id", "0");
	}

	public void setSettingShopId(String shop_id) {
		sysInitSharedPreferences.edit().putString("shop_id", shop_id).commit();
	}
	
	public UserDao getUserdao() {
		return userdao;
	}

	public void setUserdao(UserDao userdao) {
		this.userdao = userdao;
	}
	
	public AndroidPrinter getPrinter() {
		return printer;
	}

	public void setPrinter(AndroidPrinter printer) {
		this.printer = printer;
	}

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}


	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public  String getuName() {
		return u_name;
	}

	public FoodOrderDao getFood_order_dao() {
		return food_order_dao;
	}

	public void setFood_order_dao(FoodOrderDao food_order_dao) {
		this.food_order_dao = food_order_dao;
	}
	
	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_code() {
		return shop_code;
	}

	public void setShop_code(String shop_code) {
		this.shop_code = shop_code;
	}

		// 创建SD卡缓存目录
		private void createCacheDir() {

			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				File f = new File(Constants.CACHE_DIR);

				if (f.exists()) {
					System.out.println("SD卡缓存目录:已存在!");
				} else {
					if (f.mkdirs()) {
						System.out.println("SD卡缓存目录:" + f.getAbsolutePath()+ "已创建!");
					} else {
						System.out.println("SD卡缓存目录:创建失败!");
					}
				}

				File ff = new File(Constants.CACHE_IMAGE);
				if (ff.exists()) {
					System.out.println("SD卡照片缓存目录:已存在!");
				} else {
					if (ff.mkdirs()) {
						System.out.println("SD卡照片缓存目录:" + ff.getAbsolutePath()+ "已创建!");
					} else {
						System.out.println("SD卡照片缓存目录:创建失败!");
					}
				}

				File ffff = new File(Constants.CACHE_DIR_UPLOADING_IMG);
				if (ffff.exists()) {
					System.out.println("SD卡上传缓存目录:已存在!");
				} else {
					if (ffff.mkdirs()) {
						System.out.println("SD卡上传缓存目录:" + ffff.getAbsolutePath()+ "已创建!");
					} else {
						System.out.println("SD卡上传缓存目录:创建失败!");
					}
				}
			} else {
				Toast.makeText(MyApp.this, "亲，您的SD不在了，可能有的功能不能用奥，赶快看看吧。",Toast.LENGTH_SHORT).show();
			}
		}
	/**
	 * 获取系统初始化文件操作器
	 * @return
	 */
	public SharedPreferences getSysInitSharedPreferences() {
		return sysInitSharedPreferences;
	}
	
}
