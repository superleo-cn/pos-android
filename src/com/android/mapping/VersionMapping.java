package com.android.mapping;

import com.android.common.RestHelper;

public class VersionMapping extends BasicMapping<VersionMapping.Version> {

	private static final VersionMapping mapping = new VersionMapping();

	public static class Version {

		public String id;

		public String name;

		public Integer versionNo;

		public String createBy;

		public String description;

		public String createDate;

	}

	public static VersionMapping getJSON(String url) {
		VersionMapping result = RestHelper.getJSON(url, VersionMapping.class);
		return result != null ? result : mapping;

	}
}
