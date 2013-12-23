package com.android.common;

import org.apache.commons.lang.StringUtils;

import android.widget.TextView;

/**
 * Date Help Class
 * 
 * @author Hu Bo
 */
public class MyTextUtils {
	public static String trimTextView(TextView textview) {
		return StringUtils.trim(textview.toString());
	}

	public static String checkIntTextView(TextView textview) {
		String str = textview.getText().toString();
		if (str.isEmpty()) {
			str = "0";
		}
		return str;
	}

	public static String checkTimeTextView(TextView textview) {
		String str = textview.getText().toString();
		if (str.isEmpty()) {
			str = "yyyy-MM-dd";
		}
		return str;
	}

	public static String checkStringTextView(TextView textview) {
		String str = textview.getText().toString();
		if (str.isEmpty()) {
			str = "";
		}
		return str;
	}

	/**
	 * 要强制失去焦点的组件
	 * 
	 * @param objs
	 *            不定参数,可以传入任意数量的参数
	 */
	public static void clearTextView(TextView... objs) {
		if (objs != null) {
			for (TextView view : objs) {
				view.setText("");
			}
		}
	}
}
