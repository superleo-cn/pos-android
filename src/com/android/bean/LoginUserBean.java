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
public class LoginUserBean {
	public static class Attr{
		public static final String ID = "id";
		public static final String USERNAME = "username";
		public static final String REALNAME = "realname";
		public static final String USERTYPE = "usertype";
		public static final String STATUS = "status";
		public static final String SHOP_ID = "shop";
	}
	private String id;
	private String username;//用户名
	private String passwrod;//密码
	private String realname;//真实姓名
	private String usertype;//用户组
	private String status;//用户状态
	private String shop_id;//店铺ID
	public LoginUserBean() {}
	public LoginUserBean(String username, String realname, String usertype,
			String status) {
		super();
	}
	
	public LoginUserBean(String id, String username, String realname, String usertype, String status,
			String shop_id) {
		super();
		this.id = id;
		this.username = username;
		this.realname = realname;
		this.usertype = usertype;
		this.status = status;
		this.shop_id = shop_id;
	}
	public static ArrayList<LoginUserBean> newInstanceList(String jsonDatas){
		ArrayList<LoginUserBean> login_bean = new ArrayList<LoginUserBean>();
		
		try {
			JSONArray arr = new JSONArray(jsonDatas);
			int size = null == arr ? 0 : arr.length();
			for(int i = 0; i < size; i++){
				JSONObject obj = arr.getJSONObject(i);
				String id = obj.optString(Attr.ID);
				String username = obj.optString(Attr.USERNAME);
				String realname = obj.optString(Attr.REALNAME);
				String usertype = obj.optString(Attr.USERTYPE);
				String status = obj.optString(Attr.STATUS);
				String shop_id = obj.optString(Attr.SHOP_ID);
				String shop = null;
				if(shop_id!=null && !shop_id.equals("")){
					JSONObject json_obj=new JSONObject(shop_id);
					shop = json_obj.optString("id");
				}
				login_bean.add(new LoginUserBean(id, username, realname, usertype, status, shop)) ;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return login_bean;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getPasswrod() {
		return passwrod;
	}
	public void setPasswrod(String passwrod) {
		this.passwrod = passwrod;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "LoginUserBean [id=" + id + ", username=" + username
				+ ", passwrod=" + passwrod + ", realname=" + realname
				+ ", usertype=" + usertype + ", status=" + status
				+ ", shop_id=" + shop_id + "]";
	}
	
}
