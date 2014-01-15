package com.android.mapping;

import java.util.Map;

import com.android.common.RestHelper;

public class StatusMapping extends BasicMapping<Long> {

	private static final StatusMapping mapping = new StatusMapping();

	public static StatusMapping postJSON(String url, Map<String, String> params) {
		StatusMapping result = RestHelper.postJSON(url, StatusMapping.class, params);
		return result != null ? result : mapping;
	}
}
