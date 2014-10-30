package com.android.mapping;

import java.util.Date;

import com.android.common.RestHelper;

public class ShopMapping extends BasicMapping<ShopMapping.Shop> {

	private static final ShopMapping mapping = new ShopMapping();

	public static class Shop {

		public String id;

		public String name;
		
		/** Add new attributes 27/10/2014 **/
		
		public String openTime;
		
		public String gstRegNo;
		
		public String gstRate;
		
		public String serviceRate;
		
		public String address;
		
		public String contact;
		
		public String website;
		
		public String email;
		
		public String weChat;
		
		public Boolean kichenPrinter;
		/** End **/

		public String code;

		public Boolean status;

		public Date expiryDate;

	}

	public static ShopMapping getJSON(String url) {
		ShopMapping result = RestHelper.getJSON(url, ShopMapping.class);
		return result != null ? result : mapping;
	}
}
