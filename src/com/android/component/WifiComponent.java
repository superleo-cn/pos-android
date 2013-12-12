package com.android.component;

import java.net.SocketException;

import android.content.Context;

import com.android.common.SystemHelper;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
@EBean(scope = Scope.Singleton)
public class WifiComponent {

	public String getIPAddress() {
		try {
			return SystemHelper.getLocalIPAddress() == null ? "0" : SystemHelper.getLocalIPAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "0";
	}

	public String getMacAddress(Context context) {
		return SystemHelper.getLocalMacAddress(context) == null ? "0" : SystemHelper.getLocalMacAddress(context);
	}

}
