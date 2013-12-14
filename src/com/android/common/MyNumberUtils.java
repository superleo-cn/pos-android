package com.android.common;

import java.text.DecimalFormat;
import java.text.Format;

import org.apache.commons.lang.math.NumberUtils;

/**
 * Date Help Class
 * 
 * @author Hu Bo
 */
public class MyNumberUtils extends NumberUtils {

	public static final String NUMBER_FORMAT_0_00 = "0.00";

	/**
	 * 数字转换成格式化的字符串
	 * 
	 * @param t
	 * @param pattern
	 * @return
	 */
	public static <T extends Number> String numToStr(T t, String pattern) {
		Format df = new DecimalFormat(pattern);
		return df.format(t);
	}

	/**
	 * 数字转换成格式化的字符串
	 * 
	 * @param t
	 * @return
	 */
	public static <T extends Number> String numToStr(T t) {
		Format df = new DecimalFormat(NUMBER_FORMAT_0_00);
		return df.format(t);
	}

	public static Double strToNum(String num) {
		return NumberUtils.toDouble(num);
	}
}
