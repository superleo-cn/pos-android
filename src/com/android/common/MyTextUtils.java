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
		String value = trimTextView(textview);
		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNumeric(value)) {
				return value;
			}
		}
		return "0";
	}
}
