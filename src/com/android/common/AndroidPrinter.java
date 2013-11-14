package com.android.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.zj.wfsdk.WifiCommunication;

public class AndroidPrinter {
	String ip = "192.168.1.100";
	WifiCommunication wfComm = null;
	EditText txt_ip = null;
	int connFlag = 0;
	revMsgThread revThred = null;
	checkPrintThread cheThread = null;
	final Context context;
	String content = null;
	// checkPrintThread cheThread = null;
	private static final int WFPRINTER_REVMSG = 0x06;

	public AndroidPrinter(Context context) {
		this.context = context;
		onCreate();
	}

	public void print(String message) {
		// connect to printer
		if (connFlag == 0) {
			onCreate();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (connFlag == 1) {
			startPrint(message);
		}

	}

	private void onCreate() {
		if (connFlag == 0) {
			Log.d("wifi", "Connection to WIFI Printer.");
			wfComm.initSocket(ip, 9100);
		}
	}

	private void onDestroy() {
		wfComm.close();
	}

	private void startPrint(String message) {
		// print
		String msg = "第一次Android客户端必须连接服务器，\n提交[用户名],[密码]以及[MAC地址]提交到服务器进行验证。\n如果验证成功，则生成一个本地密码，为Android本地登录密码。\n\n";
		String msg1 = "  You have sucessfully created communications between your device and our WIFI printer.\n\n"
				+ "  Shenzhen Zijiang Electronics Co..Ltd is a high-tech enterprise which specializes"
				+ " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n"
				+ "  Please go to our website and see details about our company :\n"
				+ "     http://www.zjiang.com\n\n";
		if (msg.length() > 0) {
			byte[] tcmd = new byte[3];
			tcmd[0] = 0x10;
			tcmd[1] = 0x04;
			tcmd[2] = 0x00;
			wfComm.sndByte(tcmd);
			wfComm.sendMsg(msg, "gbk");

			byte[] bytecmd = new byte[5];
			bytecmd[0] = 0x1B;
			bytecmd[1] = 0x70;
			bytecmd[2] = 0x00;
			bytecmd[3] = 0x40;
			bytecmd[4] = 0x50;
			wfComm.sndByte(bytecmd);

			byte[] tail = new byte[3];
			tail[0] = 0x0A;
			tail[1] = 0x0D;
			wfComm.sndByte(tail);
			wfComm.close();
		}
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WifiCommunication.WFPRINTER_CONNECTED:
				connFlag = 1;
				Toast.makeText(context, "Connect the WIFI-printer successful",
						Toast.LENGTH_SHORT).show();
				revThred = new revMsgThread();
				revThred.start();
				cheThread = new checkPrintThread();
				cheThread.start();
				break;
			case WifiCommunication.WFPRINTER_DISCONNECTED:
				Toast.makeText(context,
						"Disconnect the WIFI-printer successful",
						Toast.LENGTH_SHORT).show();
				revThred.interrupt();
				cheThread.interrupt();
				break;
			case WifiCommunication.SEND_FAILED:
				Toast.makeText(context, "Send Data Failed,please reconnect",
						Toast.LENGTH_SHORT).show();
				break;
			case WifiCommunication.WFPRINTER_CONNECTEDERR:
				connFlag = 0;
				Toast.makeText(context, "Connect the WIFI-printer error",
						Toast.LENGTH_SHORT).show();
				break;
			case WifiCommunication.CONNECTION_LOST:
				connFlag = 0;
				Toast.makeText(context, "Connection lost,please reconnect",
						Toast.LENGTH_SHORT).show();
				revThred.interrupt();
				cheThread.interrupt();
				break;
			case WFPRINTER_REVMSG:
				byte revData = (byte) Integer.parseInt(msg.obj.toString());
				if (((revData >> 6) & 0x01) == 0x01)
					Toast.makeText(context, "The printer have no paper",
							Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	class checkPrintThread extends Thread {
		@Override
		public void run() {
			byte[] tcmd = new byte[3];
			tcmd[0] = 0x10;
			tcmd[1] = 0x04;
			tcmd[2] = 0x04;
			try {
				while (true) {
					wfComm.sndByte(tcmd);
					Thread.sleep(15);
					Log.d("wifi", "Check connection.");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Log.d("wifi", "Connection failed");
			}
		}
	}

	class revMsgThread extends Thread {
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
					Thread.sleep(2);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Log.d("wifi", "Connection failed");
			}
		}
	}

}
