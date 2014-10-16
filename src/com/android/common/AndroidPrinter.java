package com.android.common;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.util.Log;

import com.RT_Printer.WIFI.WifiPrintDriver;
import com.android.component.SharedPreferencesComponent_;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class AndroidPrinter {

	@RootContext
	Context context;

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	int connFlag = 0;

	@AfterInject
	public void initPrinter() {
		if (connFlag == 0) {
			try {
				connect();
			} catch (Exception e) {
				Log.e("[AndroidPrinter]", "打印机初始化错误", e);
			}
		}

	}

	// start to print
	public void print(String message, String type) {
		// connect to printer
		if (connFlag == 0) {
			connect();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Log.e("[AndroidPrinter]", "打印机中断", e);
			}
		}
		// if conenct to WIFI printer
		if (connFlag == 1) {
			startPrint(message, type);
		}
	}

	// connect to printer
	@Background
	public void connect() {
		try {
			if (connFlag == 0) {
				try {
					Log.d("[AndroidPrinter]", "连接打印机");
					if (!WifiPrintDriver.WIFISocket(sharedPrefs.printIp().get(), 9100)) {
						WifiPrintDriver.Close();
						connFlag = 0;
						return;
					}
					if (WifiPrintDriver.IsNoConnection()) {
						connFlag = 0;
						return;
					}
					connFlag = 1;
				} catch (Exception e) {
					Log.e("[AndroidPrinter]", "打印机中断", e);
				}
				connFlag = 0;
			}
		} catch (Exception ex) {
			Log.e("[AndroidPrinter]", "打印机连接失败", ex);
		}
	}

	// disconnect to printer
	public void disconnect() {
		try {
			WifiPrintDriver.Close();
		} catch (Exception ex) {
			Log.e("[AndroidPrinter]", "打印机关闭失败", ex);
		}
		connFlag = 0;
	}

	@Background
	public void reconnect() {
		disconnect();
		connect();
	}

	public void startPrint(String message, String type) {
		InputStream in = null;
		try {
			in = context.getResources().getAssets().open("Weebo.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		WifiPrintDriver.printImage();
		if (StringUtils.equals("CASH", type)) {
			printWithDrawer(message);
		} else {
			printOnly(message);
		}
	}

	public void printWithDrawer(String message) {
		if (message != null && message.length() > 0) {
			WifiPrintDriver.Begin();
			WifiPrintDriver.ImportData(message);
			WifiPrintDriver.ImportData("\r");
			WifiPrintDriver.LF();
			WifiPrintDriver.LF();
			WifiPrintDriver.excute();
			WifiPrintDriver.ClearData();
			WifiPrintDriver.OpenDrawer((byte) 0X00, (byte) 0X00, (byte) 0X10);
			WifiPrintDriver.excute();
			WifiPrintDriver.ClearData();
		}
	}

	public void printOnly(String message) {
		if (message != null && message.length() > 0) {
			WifiPrintDriver.Begin();
			WifiPrintDriver.ImportData(message);
			WifiPrintDriver.ImportData("\r");
			WifiPrintDriver.LF();
			WifiPrintDriver.LF();
			WifiPrintDriver.excute();
			WifiPrintDriver.ClearData();
		}
	}

}
