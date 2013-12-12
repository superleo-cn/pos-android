package com.android.component;

import java.net.SocketException;

import android.content.Context;

import com.android.common.SystemHelper;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
public class WifiComponent {

	public static String getIPAddress() {
		try {
			return SystemHelper.getLocalIPAddress() == null ? "0" : SystemHelper.getLocalIPAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "0";
	}

	public static String getMacAddress(Context context) {
		return SystemHelper.getLocalMacAddress(context) == null ? "0" : SystemHelper.getLocalMacAddress(context);
	}

}
