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

	public static String putTabValues(String str, int length) {
		str = StringUtils.trimToEmpty(str);
		StringBuilder builder = new StringBuilder(str);
		int size = getLength(str);
		while (size <= length) {
			builder.append("\t");
			size += 4;
		}
		return builder.toString();
	}

	public static String matchTabValues(String str, int maxLenght) {
		StringBuilder builder = new StringBuilder(str);
		while (builder.length() < maxLenght) {
			builder.append("\t");
		}
		return builder.toString();
	}

	public static String stringFormat(String str, int length) {
		String result = String.format("%-30s", str);
		return result;
	}

	// GENERAL_PUNCTUATION 判断中文的“号
	// CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
	// HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static final boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	public static final int chineseSize(String strName) {
		int size = 0;
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				size++;
			}
		}
		return size;
	}

	public static final int getLength(String strName) {
		float size = 0;
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				size += 1.8;
			} else {
				size += 1;
			}
		}
		return Math.round(size);
	}

}
