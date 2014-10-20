package com.android.common;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.util.Log;

import com.RT_Printer.WIFI.WifiPrintDriver;
import com.android.component.SharedPreferencesComponent_;
import com.googlecode.androidannotations.annotations.AfterInject;
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

	@AfterInject
	public void initPrinter() {

	}

	public void print(String message, String cost, String paid, String remain, String type) {
		try {
			Log.d("[AndroidPrinter]", "连接打印机");
			if (connect()) {
				startPrint(message, cost, paid, remain, type);
				disconnect();
			} else {
				disconnect();
			}
		} catch (Exception e) {
			Log.e("[AndroidPrinter]", "打印机中断", e);
		}
	}

	// connect to printer
	public boolean connect() {
		try {
			Log.d("[AndroidPrinter]", "连接打印机");
			if (!WifiPrintDriver.WIFISocket(sharedPrefs.printIp().get(), 9100)) {
				WifiPrintDriver.Close();
				return false;
			}
			if (WifiPrintDriver.IsNoConnection()) {
				return false;
			}
			return true;
		} catch (Exception e) {
			Log.e("[AndroidPrinter]", "打印机中断", e);
		}
		return false;
	}

	// disconnect to printer
	public void disconnect() {
		try {
			WifiPrintDriver.Close();
		} catch (Exception ex) {
			Log.e("[AndroidPrinter]", "打印机关闭失败", ex);
		}
	}

	public void startPrint(String message, String cost, String paid, String remain, String type) {
		WifiPrintDriver.Begin();

		if (StringUtils.equals(type, Constants.PLACE_ORDER)) {
			printWithDrawer(message, false);
			feedAndCutPaper();
			printWithDrawer(message, false);
			feedAndCutPaper();
		} else {
			printHeader();
			printTransaction();
			if (StringUtils.equals(type, Constants.PAYTYPE_CASH)) {
				printWithDrawer(message, true);
			} else {
				printWithDrawer(message, false);
			}
			printLine();
			printSpace();
			printFooter(cost, paid, remain);
			feedAndCutPaper();
		}

	}

	public void printWithDrawer(String message, boolean flag) {
		if (StringUtils.isNotEmpty(message)) {
			setNormal();
			printContent(message);
			if (flag) {
				WifiPrintDriver.OpenDrawer((byte) 0X00, (byte) 0X00, (byte) 0X10);
				WifiPrintDriver.excute();
				WifiPrintDriver.ClearData();
			}
		}
	}

	private void printHeader() {
		// InputStream in = null;
		// try {
		// in = context.getResources().getAssets().open("Weebo.jpg");
		// BufferedInputStream bis = new BufferedInputStream(in);
		// Bitmap bitmap = BitmapFactory.decodeStream(bis);
		// //
		// byte[] start = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		// 0x1B, 0x40, 0x1B, 0x33, 0x00 };
		// WifiPrintDriver.ImportData(start, start.length);
		// WifiPrintDriver.excute();
		// WifiPrintDriver.ClearData();
		// //
		// byte[] byteImage = Utils.getReadBitMapBytes(bitmap);
		// WifiPrintDriver.ImportData(byteImage, byteImage.length);
		// WifiPrintDriver.excute();
		// WifiPrintDriver.ClearData();
		//
		// } catch (IOException ex) {
		// Log.e("[AndroidPrinter]", "图片打印失败", ex);
		// }

		setLarge();
		printContent("账单(Bill)");
		printSpace();
		printSpace();
	}

	private void printFooter(String cost, String paid, String remain) {
		setNormal();
		printContent("\t\t\t总计(Total):\t$" + cost);
		printSpace();
		// printContent("付款(payment): " + paid);
		// printSpace();
		// printContent("找零(remain): " + remain);
		// printSpace();
	}

	private void printTransaction() {
		String billNo = DateUtils.dateToStr(new Date(), DateUtils.YYYYMMDD_HH_MM_SS);
		String time = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		setNormal();
		printContent("单号(SN): B" + billNo);
		printSpace();
		setNormal();
		printContent("时间(Time): " + time);
		printSpace();
		printLine();
		printSpace();
	}

	private void printLine() {
		printContent("----------------------------------------------");
	}

	private void printContent(String msg) {
		WifiPrintDriver.ImportData(msg);
		WifiPrintDriver.excute();
		WifiPrintDriver.ClearData();
	}

	private void printSpace() {
		WifiPrintDriver.LF();
		WifiPrintDriver.excute();
		WifiPrintDriver.ClearData();
	}

	private void feedAndCutPaper() {
		WifiPrintDriver.FeedAndCutPaper((byte) 0x20);
		WifiPrintDriver.excute();
		WifiPrintDriver.ClearData();
	}

	private void setNormal() {
		WifiPrintDriver.SetAlignMode((byte) 0);
		WifiPrintDriver.SetFontEnlarge((byte) 0x00);
	}

	private void setLarge() {
		WifiPrintDriver.SetAlignMode((byte) 1);// 居中对齐
		WifiPrintDriver.SetLineSpacing((byte) 50);
		WifiPrintDriver.SetFontEnlarge((byte) 0x11);// 字体放大
	}

}
