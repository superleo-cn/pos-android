package com.android.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class GetPayDetailBean {
	
		public static class Attr{
			public static final String ID = "id";
			public static final String NAME = "name";
			public static final String NAME_ZH = "name_zh";
		}
		private String id;//编号
		private String name;//名称
		private String nameZh;//中文名称
		public GetPayDetailBean() {}
		public GetPayDetailBean(String id, String name, String nameZh) {
			super();
			this.id = id;
			this.name = name;
			this.nameZh = nameZh;
		}
		
		
		public static ArrayList<GetPayDetailBean> newInstanceList(String jsonDatas,boolean is_chinese){
			ArrayList<GetPayDetailBean> pay_detail = new ArrayList<GetPayDetailBean>();
			
			try {
				JSONArray arr = new JSONArray(jsonDatas);
				int size = null == arr ? 0 : arr.length();
				for(int i = 0; i < size; i++){
					JSONObject obj = arr.getJSONObject(i);
					String id = obj.optString(Attr.ID);
					String nameZh = obj.optString(Attr.NAME_ZH);
					String name = obj.optString(Attr.NAME);
					 Log.e("解析中文", nameZh);
					 Log.e("解析英文", name);
					pay_detail.add(new GetPayDetailBean(id, name, nameZh)) ;
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
		public String getNameZh() {
			return nameZh;
		}
		public void setNameZh(String nameZh) {
			this.nameZh = nameZh;
		}
	
}
