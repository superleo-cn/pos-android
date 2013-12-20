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

import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

import com.activeandroid.app.Application;
import com.googlecode.androidannotations.annotations.EApplication;

/**
 * Author:hjgang Create On 2013-8-6下午4:52:02 Site:http://weibo.com/hjgang or
 * http://t.qq.com/hjgang_ EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-8-6 hjgang All rights reserved.
 */

@EApplication
public class MyApp extends Application {
	/** 系统初始化配置文件操作器 */
	private SharedPreferences sysInitSharedPreferences;
	private String u_name = "";
	private String user_id = "0";
	private String u_type = "";
	private String daily_pay_submit_flag = "1";
	private String shop_name = "";;
	private String shop_code = "";;

	@Override
	public void onCreate() {
		super.onCreate();
		createCacheDir();
		sysInitSharedPreferences = getSharedPreferences(Constants.SYSTEM_INIT_FILE_NAME, MODE_PRIVATE);

	}

	public String getU_type() {
		return u_type;
	}

	public void setU_type(String u_type) {
		this.u_type = u_type;
	}

	public String getDaily_pay_submit_flag() {
		return daily_pay_submit_flag;
	}

	public String getDiscount() {
		return sysInitSharedPreferences.getString("discount", "0.5");
	}

	public void setDiscount(String discount) {
		sysInitSharedPreferences.edit().putString("discount", discount).commit();
	}

	public String getPackageCost() {
		return sysInitSharedPreferences.getString("packageCost", "0.2");
	}

	public void setPackageCost(String packageCost) {
		sysInitSharedPreferences.edit().putString("packageCost", packageCost).commit();
	}

	public long getSetting_time() {
		return sysInitSharedPreferences.getLong("setting_time", 30 * 60 * 1000);
	}

	public void setSetting_time(long setting_time) {
		sysInitSharedPreferences.edit().putLong("setting_time", setting_time).commit();
	}

	public String getIp_str() {
		return sysInitSharedPreferences.getString("ip_str", "192.168.0.1");
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

	public String getuName() {
		return u_name;
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
					System.out.println("SD卡缓存目录:" + f.getAbsolutePath() + "已创建!");
				} else {
					System.out.println("SD卡缓存目录:创建失败!");
				}
			}

			File ff = new File(Constants.CACHE_IMAGE);
			if (ff.exists()) {
				System.out.println("SD卡照片缓存目录:已存在!");
			} else {
				if (ff.mkdirs()) {
					System.out.println("SD卡照片缓存目录:" + ff.getAbsolutePath() + "已创建!");
				} else {
					System.out.println("SD卡照片缓存目录:创建失败!");
				}
			}

			File ffff = new File(Constants.CACHE_DIR_UPLOADING_IMG);
			if (ffff.exists()) {
				System.out.println("SD卡上传缓存目录:已存在!");
			} else {
				if (ffff.mkdirs()) {
					System.out.println("SD卡上传缓存目录:" + ffff.getAbsolutePath() + "已创建!");
				} else {
					System.out.println("SD卡上传缓存目录:创建失败!");
				}
			}
		} else {
			Toast.makeText(MyApp.this, "亲，您的SD不在了，可能有的功能不能用奥，赶快看看吧。", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 获取系统初始化文件操作器
	 * 
	 * @return
	 */
	public SharedPreferences getSysInitSharedPreferences() {
		return sysInitSharedPreferences;
	}

}
