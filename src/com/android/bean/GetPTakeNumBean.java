package com.android.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetPTakeNumBean {
	
		public static class Attr{
			public static final String ID = "id";
			public static final String PRICE = "price";
			//public static final String PRICE_ZH = "price_zh";
		}
		private String id;//编号
		private String price;//名称
		public GetPTakeNumBean() {}
		public GetPTakeNumBean(String id, String price) {
			super();
			this.id = id;
			this.price = price;
		}
		
		
		public static ArrayList<GetPTakeNumBean> newInstanceList(String jsonDatas){
			ArrayList<GetPTakeNumBean> pay_detail = new ArrayList<GetPTakeNumBean>();
			
			try {
				JSONArray arr = new JSONArray(jsonDatas);
				int size = null == arr ? 0 : arr.length();
				for(int i = 0; i < size; i++){
					JSONObject obj = arr.getJSONObject(i);
					String id = obj.optString(Attr.ID);
					String price;
					price = obj.optString(Attr.PRICE);
					
					pay_detail.add(new GetPTakeNumBean(id, price)) ;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return pay_detail;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		
	
}
