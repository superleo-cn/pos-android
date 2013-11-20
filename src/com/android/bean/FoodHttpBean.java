/**
 * ClassName:LoginUserBean.java
 * PackageName:pos-android
 * Create On 2013-11-12 下午3:04:58
 * Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 * EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 * Copyrights 2013-11-12 hjgang All rights reserved.
 */
package com.android.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *  Author:hjgang
 *  Create On 2013-11-12 下午3:04:58
 *  Site:http://weibo.com/hjgang or http://t.qq.com/hjgang_
 *  EMAIL:hjgang@bizpower.com or hjgang@yahoo.cn
 *  Copyrights 2013-11-12 hjgang All rights reserved.
 */
public class FoodHttpBean {
	public static class Attr{
		public static final String ID = "id";
		public static final String SN = "sn";
		public static final String NAME = "name";
		public static final String NAMEZH = "nameZh";
		public static final String TYPE = "type";
		public static final String PICTURE = "picture";
	}
	private String id;
	private String sn;//用户名
	private String name;//密码
	private String nameZh;//真实姓名
	private String type;//用户组
	private String picture;//用户状态
	public FoodHttpBean() {}
	public FoodHttpBean(String username, String realname, String usertype,
			String status) {
		super();
	}
	
	public FoodHttpBean(String id, String sn, String name, String nameZh,
			String type, String picture) {
		super();
		this.id = id;
		this.sn = sn;
		this.name = name;
		this.nameZh = nameZh;
		this.type = type;
		this.picture = picture;
	}
	public static ArrayList<FoodHttpBean> newInstanceList(String jsonDatas){
		ArrayList<FoodHttpBean> login_bean = new ArrayList<FoodHttpBean>();
		
		try {
			JSONArray arr = new JSONArray(jsonDatas);
			int size = null == arr ? 0 : arr.length();
			for(int i = 0; i < size; i++){
				JSONObject obj = arr.getJSONObject(i);
				String id = obj.optString(Attr.ID);
				String sn = obj.optString(Attr.SN);
				String name = obj.optString(Attr.NAME);
				String namezh = obj.optString(Attr.NAMEZH);
				String type = obj.optString(Attr.TYPE);
				String picture = obj.optString(Attr.PICTURE);
				login_bean.add(new FoodHttpBean(id, sn, name, namezh, type, picture)) ;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return login_bean;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	@Override
	public String toString() {
		return "FoodHttpBean [id=" + id + ", sn=" + sn + ", name=" + name
				+ ", nameZh=" + nameZh + ", type=" + type + ", picture="
				+ picture + "]";
	}
}
