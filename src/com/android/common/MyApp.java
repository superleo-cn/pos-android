/**
 * ClassName:MyApp.java
 * PackageName:com.shopnc_local_life.android.common
 * Create On 2013-8-6下午4:52:02
 * Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 * EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-2-18 hjgang All rights reserved.
 */
package com.android.common;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Author:hjgang
 * Create On 2013-8-6下午4:52:02
 * Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 * EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-8-6 hjgang All rights reserved.
 */
public class MyApp extends Application{
	/** 系统初始化配置文件操作器 */
	private SharedPreferences sysInitSharedPreferences;
	@Override
	public void onCreate() {
		super.onCreate();
		sysInitSharedPreferences = getSharedPreferences(
				Constants.SYSTEM_INIT_FILE_NAME, MODE_PRIVATE);
	}

	/**
	 * 获取系统初始化文件操作器
	 * @return
	 */
	public SharedPreferences getSysInitSharedPreferences() {
		return sysInitSharedPreferences;
	}
	
}
