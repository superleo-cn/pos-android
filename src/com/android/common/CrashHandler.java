/**
 * ClassName:CrashHandler.java
 * PackageName:pos-android
 * Create On 2013-11-23 下午4:59:03
 * Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 * EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-11-23 hjgang All rights reserved.
 */
package com.android.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;

import com.android.component.ActivityComponent;
import com.android.component.DirectEmailComponent;
import com.android.component.WifiComponent;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;

/**
 * Author:hjgang Create On 2013-11-23 下午4:59:03 Site:http://weibo.com/hjgang or
 * http://t.qq.com/hjgang_ EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-11-23 hjgang All rights reserved.
 */
@EBean
public class CrashHandler implements UncaughtExceptionHandler {

	@Bean
	DirectEmailComponent directEmailComponent;

	@Bean
	WifiComponent wifiComponent;

	@App
	MyApp myApp;

	@Bean
	ActivityComponent activityComponent;

	public static final String TAG = "CrashHandler";
	private static CrashHandler INSTANCE = new CrashHandler();
	private Context mContext;
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	public static final String ERR_TITLE = "Android POS Issue at [%s]";
	public static final String ERR_INFO = "Outlet [%s] User [%s] got some issue at [%s], the detail information is [%s]";

	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			promptMessageDialog();
		}
		// 非法退出，强行结束
		// android.os.Process.killProcess(android.os.Process.myPid());
		// System.exit(10);
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		// 发送信息
		if (wifiComponent.isConnected()) {
			String shopInfo = StringUtils.EMPTY;
			String userInfo = StringUtils.EMPTY;
			String dateTime = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
			String errorMsg = getStackTrace(ex);
			String title = String.format(ERR_TITLE, dateTime);
			if (myApp != null) {
				shopInfo = myApp.getShopId() + "-" + myApp.getShopName() + "(" + myApp.getShopCode() + ")";
				userInfo = myApp.getUserId() + "-" + myApp.getUsername() + "(" + myApp.getUserType() + ")";
			}
			String sendMsg = String.format(ERR_INFO, shopInfo, userInfo, dateTime, errorMsg);
			directEmailComponent.sendMail(title, sendMsg);
		}
		return true;
	}

	/**
	 * 点击退出系统
	 */
	private void promptMessageDialog() {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				new AlertDialog.Builder(mContext).setTitle("Error").setCancelable(false)
						.setMessage("The system was crashed. Please restart it later.").setNeutralButton("OK", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								System.exit(-1);
							}
						}).create().show();
				Looper.loop();
			}
		}.start();
	}

	/**
	 * 获取所有详细的信息
	 * 
	 * @param throwable
	 * @return
	 */
	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}