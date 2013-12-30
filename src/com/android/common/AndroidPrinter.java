package com.android.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;
import com.zj.wfsdk.WifiCommunication;

@EBean(scope = Scope.Singleton)
public class AndroidPrinter {

	@RootContext
	Context context;

	String ip = "192.168.0.100";
	WifiCommunication wfComm = null;
	int connFlag = 0;
	RevMsgThread revThred = null;
	CheckPrintThread cheThread = null;
	// checkPrintThread cheThread = null;
	private static final int WFPRINTER_REVMSG = 0x06;

	// print msg
	private static final String message1 = "测试数据。。。。\n" + "1/八宝 酿豆腐\t\t\t\tQuty:1 \n" + "2/特制酿豆腐\t\t\t\tQuty:5 \n"
			+ "3/珍珠米\t\t\t\tQuty:10 \n" + "4/虾棒墨鱼汤\t\t\t\tQuty:20 \n\n";
	private static final String message2 = "  You have sucessfully created communications between your device and our WIFI printer.\n\n"
			+ "  Shenzhen Zijiang Electronics Co..Ltd is a high-tech enterprise which specializes"
			+ " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n"
			+ "  Please go to our website and see details about our company :\n" + "     http://www.zjiang.com\n\n";

	@AfterInject
	public void initPrinter() {
		if (wfComm == null) {
			try {
				wfComm = new WifiCommunication(mHandler);
				Log.d("WIFI Printer", "try to re-connect printer and print message. ");
				connect();
			} catch (Exception e) {
				Log.d("WIFI Printer", "Cannot find WIFI Printer ", e);
				// Toast.makeText(context, "Cannot find WIFI Printer",
				// Toast.LENGTH_SHORT).show();
			}
		}

	}

	// start to print
	public void print(String message) {
		// connect to printer
		if (connFlag == 0) {
			connect();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.d("WIFI Printer", "try to re-connect printer and print message: " + message);
			}
		}
		// if conenct to WIFI printer
		if (connFlag == 1) {
			Log.d("WIFI Printer", "start to printer :" + message);
			startPrint(message);
		}
	}

	// connect to printer
	@Background
	public void connect() {
		try {
			if (connFlag == 0) {
				try {
					Thread.sleep(1000);
					Log.d("WIFI Printer", "Connection to WIFI Printer.");
					wfComm.initSocket(ip, 9100);
					connFlag = 1;
				} catch (InterruptedException e) {
					Log.d("WIFI Printer", "try to re-connect printer and print message: " + e);
				}

			}
		} catch (Exception ex) {
			Log.e("WIFI Printer", "WIFI connection failed", ex);
		}
	}

	// disconnect to printer
	public void disconnect() {
		try {
			connFlag = 0;
			wfComm.close();
		} catch (Exception ex) {
			Log.e("WIFI Printer", "WIFI disconnect failed", ex);
		}
	}

	@Background
	public void reconnect() {
		disconnect();
		connect();
	}

	public void startPrint(String message) {
		if (message.length() > 0) {
			// drawer 先弹出抽屉
			byte[] tail = new byte[3];
			tail[0] = 0x0A;
			tail[1] = 0x0D;
			wfComm.sndByte(tail);

			byte[] bytecmd = new byte[5];
			bytecmd[0] = 0x1B;
			bytecmd[1] = 0x70;
			bytecmd[2] = 0x00;
			bytecmd[3] = 0x40;
			bytecmd[4] = 0x50;
			wfComm.sndByte(bytecmd);

			// set double height and double width mode
			byte[] tcmd = new byte[3];
			tcmd[0] = 0x1b;
			tcmd[1] = 0x21;
			tcmd[2] = 0x10;
			wfComm.sndByte(tcmd);
			wfComm.sendMsg(message, "gbk");
			Log.d("WIFI Printer", "Print message is: " + message);

			// cut paper
			byte[] bits = new byte[4];
			bits[0] = 0x1D;
			bits[1] = 0x56;
			bits[2] = 0x42;
			bits[3] = 90;
			wfComm.sndByte(bits);
		}
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WifiCommunication.WFPRINTER_CONNECTED:
				connFlag = 1;
				// Toast.makeText(context,
				// "Connect the WIFI-printer successful",
				// Toast.LENGTH_SHORT).show();
				revThred = new RevMsgThread();
				revThred.start();
				cheThread = new CheckPrintThread();
				cheThread.start();
				break;
			case WifiCommunication.WFPRINTER_DISCONNECTED:
				connFlag = 0;
				// Toast.makeText(context,
				// "Disconnect the WIFI-printer successful",
				// Toast.LENGTH_SHORT).show();
				if (wfComm != null && revThred != null && cheThread != null) {
					revThred.interrupt();
					cheThread.interrupt();
				}
				reconnect();
				break;
			case WifiCommunication.SEND_FAILED:
				// Toast.makeText(context, "Send Data Failed,please reconnect",
				// Toast.LENGTH_SHORT).show();
				reconnect();
				break;
			case WifiCommunication.WFPRINTER_CONNECTEDERR:
				connFlag = 0;
				// Toast.makeText(context, "Connect the WIFI printer get error",
				// Toast.LENGTH_SHORT).show();
				if (wfComm != null && revThred != null) {
					revThred.interrupt();
				}
				reconnect();
				break;
			case WifiCommunication.CONNECTION_LOST:
				connFlag = 0;
				// Toast.makeText(context, "Connection lost,please reconnect",
				// Toast.LENGTH_SHORT).show();
				if (wfComm != null && revThred != null) {
					revThred.interrupt();
					// cheThread.interrupt();
				}
				reconnect();
				break;
			case WFPRINTER_REVMSG:
				byte revData = (byte) Integer.parseInt(msg.obj.toString());
				if (((revData >> 6) & 0x01) == 0x01)
					// Toast.makeText(getApplicationContext(),
					// "The printer have no paper", Toast.LENGTH_SHORT)
					// .show();
					break;
			default:
				break;
			}
		}
	};

	class CheckPrintThread extends Thread {
		@Override
		public void run() {
			byte[] tcmd = new byte[3];
			tcmd[0] = 0x10;
			tcmd[1] = 0x04;
			tcmd[2] = 0x04;
			try {
				while (true) {
					wfComm.sndByte(tcmd);
					Thread.sleep(10000);
					Log.d("WIFI Printer", "WIFI printer is still working.");
				}
			} catch (InterruptedException e) {
				Log.d("WIFI Printer", "Check Printer Error, trying to re-connect.", e);
				// reconnect();
			}
		}
	}

	class RevMsgThread extends Thread {
		@Override
		public void run() {
			try {
				Message msg = new Message();
				int revData;
				while (true) {
					revData = wfComm.revByte();
					if (revData != -1) {
						msg = mHandler.obtainMessage(WFPRINTER_REVMSG);
						msg.obj = revData;
						mHandler.sendMessage(msg);
					}
					Thread.sleep(5000);
				}
			} catch (InterruptedException e) {
				Log.d("WIFI Printer", "Cannot Receive Message, trying to re-connect.", e);
				// reconnect();
			}
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
