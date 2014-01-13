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
			str = Constants.DEFAULT_PRICE_INT;
		}
		return str;
	}

	public static String checkTimeTextView(TextView textview) {
		String str = textview.getText().toString();
		if (str.isEmpty()) {
			str = DateUtils.YYYY_MM_DD;
		}
		return str;
	}

	public static String checkStringTextView(TextView textview) {
		String str = textview.getText().toString();
		if (str.isEmpty()) {
			str = StringUtils.EMPTY;
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
				view.setText(StringUtils.EMPTY);
			}
		}
	}

	public static String putDefaultValues(String str, int length) {
		StringBuilder builder = new StringBuilder(StringUtils.trimToEmpty(str));
		if (builder.length() < length) {
			int len = length - builder.length();
			for (int i = 0; i < len; i++) {
				builder.append(" ");
			}
		}
		return builder.toString();
	}

}
