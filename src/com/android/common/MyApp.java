/**
 * ClassName:MyApp.java
 * PackageName:com.shopnc_local_life.android.common
 * Create On 2013-8-6下午4:52:02
 * Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 * EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-2-18 hjgang All rights reserved.
 */
package com.android.common;

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
	private AndroidPrinter printer;
	private String uid = "0";
	private String u_name = "";
	@Override
	public void onCreate() {
		super.onCreate();
		sysInitSharedPreferences = getSharedPreferences(
				Constants.SYSTEM_INIT_FILE_NAME, MODE_PRIVATE);
		userdao = new UserDao(this);
		printer = new AndroidPrinter(this);
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

	public void setUid(String uid) {
		this.uid = uid;
	}

	public  String getUid() {
		return uid;
	}

	public  String getuName() {
		return u_name;
	}

	/**
	 * 获取系统初始化文件操作器
	 * @return
	 */
	public SharedPreferences getSysInitSharedPreferences() {
		return sysInitSharedPreferences;
	}
	
}
