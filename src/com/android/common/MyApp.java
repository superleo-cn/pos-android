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

import android.os.Environment;
import android.widget.Toast;

import com.activeandroid.app.Application;
import com.android.component.SharedPreferencesComponent_;
import com.googlecode.androidannotations.annotations.EApplication;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Author:hjgang Create On 2013-8-6下午4:52:02 Site:http://weibo.com/hjgang or
 * http://t.qq.com/hjgang_ EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-8-6 hjgang All rights reserved.
 */

@EApplication
public class MyApp extends Application {
	/** 系统初始化配置文件操作器 */
	private String username = "";
	private String userId = "0";
	private String userType = "";
	private String shopName = "";
	private String shopCode = "";

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@Override
	public void onCreate() {
		super.onCreate();
		createCacheDir();

	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopId() {
		return myPrefs.shopId().get();
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

}
