/**
 * ClassName:MyApp.java
 * PackageName:com.shopnc_local_life.android.common
 * Create On 2013-8-6下午4:52:02
 * Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 * EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-2-18 hjgang All rights reserved.
 */
package com.android.common;

import com.android.dao.FoodOrderDao;
import com.android.dao.UserDao;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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
	private String shopid = "0";
	private String u_name = "";
	private String user_id= "0";
	private String discount="0.5";
	private String u_type = "CACHIER";

	@Override
	public void onCreate() {
		super.onCreate();
		sysInitSharedPreferences = getSharedPreferences(
				Constants.SYSTEM_INIT_FILE_NAME, MODE_PRIVATE);
		discount= sysInitSharedPreferences.getString("discount", "0.5");
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

	public void setDiscount(String discount) {
		this.discount = discount;
		sysInitSharedPreferences.edit().putString("discount", discount).commit();
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

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
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

	/**
	 * 获取系统初始化文件操作器
	 * @return
	 */
	public SharedPreferences getSysInitSharedPreferences() {
		return sysInitSharedPreferences;
	}
	
}
