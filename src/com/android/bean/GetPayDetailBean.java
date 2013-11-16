package com.android.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetPayDetailBean {
	
		public static class Attr{
			public static final String ID = "id";
			public static final String NAME = "name";
		}
		private String id;//编号
		private String name;//名称
		public GetPayDetailBean() {}
		public GetPayDetailBean(String id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
		
		
		public static ArrayList<GetPayDetailBean> newInstanceList(String jsonDatas){
			ArrayList<GetPayDetailBean> pay_detail = new ArrayList<GetPayDetailBean>();
			
			try {
				JSONArray arr = new JSONArray(jsonDatas);
				int size = null == arr ? 0 : arr.length();
				for(int i = 0; i < size; i++){
					JSONObject obj = arr.getJSONObject(i);
					String id = obj.optString(Attr.ID);
					String name = obj.optString(Attr.NAME);
					pay_detail.add(new GetPayDetailBean(id, name)) ;
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
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	
}
