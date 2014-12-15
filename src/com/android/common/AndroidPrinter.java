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

	@Pref
	SharedPreferencesComponent_ myPrefs;

	public String shopName;

	public String shopAddress;

	public String shopContact;

	public String shopWebsite;

	public String gstRegNo;

	public String weChat;

	public String gstRate;

	public String serviceRate;

	@AfterInject
	public void initPrinter() {
		shopName = myPrefs.shopName().get();
		shopAddress = myPrefs.shopAddress().get();
		shopContact = myPrefs.shopContact().get();
		shopWebsite = myPrefs.shopWebsite().get();
		gstRegNo = myPrefs.gstRegNo().get();
		weChat = myPrefs.weChat().get();
		gstRate = myPrefs.gstRate().get();
		serviceRate = myPrefs.serviceRate().get();
	}

	public void print(String message, String subTotal, String gstCharge, String serviceCharge, String cost, String paid, String remain, String type) {
		try {
			Log.d("[AndroidPrinter]", "连接打印机");
			if (connect()) {
				startPrint(message, subTotal, gstCharge, serviceCharge, cost, paid, remain, type);
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

	public void startPrint(String message, String subTotal, String gstCharge, String serviceCharge, String cost, String paid, String remain, String type) {
		WifiPrintDriver.Begin();
		String shopPrintable = myPrefs.shopPrintable().get();
		if (StringUtils.equals(shopPrintable, Constants.PRINTABLE_YES)) {
			if (StringUtils.equals(type, Constants.PLACE_ORDER)) {
				printOrderDate();
				printOrder(message);
				// printWithDrawer(message, false);
				feedAndCutPaper();
				printOrderDate();
				printOrder(message);
				// printWithDrawer(message, false);
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
				printFooter(subTotal, gstCharge, serviceCharge, cost, paid, remain);
				feedAndCutPaper();
			}
		} else {
			openDrawer();
		}

	}

	public void printWithDrawer(String message, boolean flag) {
		if (StringUtils.isNotEmpty(message)) {
			setNormal();
			printContent(message);
			if (flag) {
				openDrawer();
			}
		}
	}

	public void openDrawer() {
		WifiPrintDriver.OpenDrawer((byte) 0X00, (byte) 0X00, (byte) 0X10);
		WifiPrintDriver.excute();
		WifiPrintDriver.ClearData();
	}

	public void printOrder(String message) {
		if (StringUtils.isNotEmpty(message)) {
			WifiPrintDriver.SetAlignMode((byte) 0);// 左对齐
			WifiPrintDriver.SetLineSpacing((byte) 20);
			WifiPrintDriver.SetFontEnlarge((byte) 0x00);// 字体放大
			printContent(message);
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
		printContent(shopName);
		printSpace();
		printSpace();
		setNormal();
		if (!shopAddress.isEmpty()) {
			printContent("Address(地址）: " + shopAddress);
			printSpace();
		}
		// if(!shopContact.isEmpty()){
		// printContent("Contact（电话）: "+ shopContact);
		// printSpace();
		// }
		if (!weChat.isEmpty()) {
			printContent("WeChat（微信）: " + weChat);
			printSpace();
		}
		if (!shopWebsite.isEmpty()) {
			printContent("Website（网站）: " + shopWebsite);
			printSpace();
		}
		if (!gstRegNo.isEmpty()) {
			printContent("GST Reg No: " + gstRegNo);
			printSpace();
		}
		printSpace();
		printLine();
		printSpace();
	}

	private void printFooter(String subTotal, String gstCharge, String serviceCharge, String cost, String paid, String remain) {
		setNormal();
		boolean iSserviceRate = false;
		boolean isGstRate = false;
		if (StringUtils.isNotEmpty(serviceRate) && !StringUtils.equals(serviceRate, "0")) {
			iSserviceRate = true;
		}
		if (StringUtils.isNotEmpty(gstRate) && !StringUtils.equals(gstRate, "0")) {
			isGstRate = true;
		}
		if (iSserviceRate || isGstRate) {
			printContent("\t\t\t" + "Sub-Total:\t\t$" + subTotal);
			printSpace();
		}
		if (iSserviceRate) {
			printContent("\t\t\t" + serviceRate + "% Service(服务费）:\t$" + serviceCharge);
			printSpace();
		}
		if (isGstRate) {
			printContent("\t\t\t" + gstRate + "% GST（税金）:\t\t$" + gstCharge);
			printSpace();
		}

		printContent("\t\t\tTotal(总计):\t\t$" + cost);
		printSpace();
		printContent("\t\t\tPayment(付款):\t\t$" + paid);
		printSpace();
		printContent("\t\t\tChange(找零):\t\t$" + remain);
		printSpace();
	}

	private void printTransaction() {
		// String billNo = DateUtils.dateToStr(new Date(),
		// DateUtils.YYYYMMDD_HH_MM_SS );
		String time = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		// setNormal();
		// printContent("SN(单号): B" + billNo);
		// printSpace();
		setNormal();
		printContent("Time(时间): " + time);
		printSpace();
		printLine();
		printSpace();
	}

	private void printOrderDate() {
		String time = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		setNormal();
		printContent("Order Time(下单时间): " + time);
		printSpace();
		setNormal();
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
		WifiPrintDriver.SetFontEnlarge((byte) 0x01);
		// WifiPrintDriver.SetCharacterFont((byte)0X00);
	}

	private void setLarge() {
		WifiPrintDriver.SetAlignMode((byte) 1);// 居中对齐
		WifiPrintDriver.SetLineSpacing((byte) 50);
		WifiPrintDriver.SetFontEnlarge((byte) 0x11);// 字体放大
	}

}
