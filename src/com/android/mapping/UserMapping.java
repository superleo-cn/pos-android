package com.android.mapping;

import java.util.List;
import java.util.Map;

import com.android.common.RestHelper;

public class UserMapping {

	public List<User> datas;

	public String message;

	public Integer code;

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
		return RestHelper.postJSON(url, UserMapping.class, params);
	}
}
