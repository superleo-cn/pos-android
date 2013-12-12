package com.android.mapping;

import java.util.Map;

import com.android.common.RestHelper;
import com.android.mapping.UserMapping.User;

public class UserMapping extends BasicMapping<User> {

	private static final UserMapping mapping = new UserMapping();

	public static class User {

		public String id;

		public String username;

		public String password;

		public String realname;

		public String usertype;

		public String status;

		public Shop shop;
	}

	public static class Shop {
		public String id;

		public String name;

		public String code;
	}

	public static UserMapping postJSON(String url, Map<String, String> params) {
		try {
			return RestHelper.postJSON(url, UserMapping.class, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mapping;

	}
}
