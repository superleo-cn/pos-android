package com.android.common;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * Date Help Class
 * 
 * @author Hu Bo
 */
public class DateUtils {

	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";

	public static String dateToStr(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

}
