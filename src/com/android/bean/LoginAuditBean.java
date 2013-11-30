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
public class LoginAuditBean {
	private String android_id;
	private String shop_id;
	private String user_id;
	private String actionDate;
	private String action;
	private String food_flag;
	public LoginAuditBean() {}
	public LoginAuditBean(String username, String realname, String usertype,
			String status) {
		super();
	}
	public LoginAuditBean(String android_id, String shop_id, String user_id,
			String actionDate, String action, String food_flag) {
		super();
		this.android_id = android_id;
		this.shop_id = shop_id;
		this.user_id = user_id;
		this.actionDate = actionDate;
		this.action = action;
		this.food_flag = food_flag;
	}
	public String getAndroid_id() {
		return android_id;
	}
	public void setAndroid_id(String android_id) {
		this.android_id = android_id;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getActionDate() {
		return actionDate;
	}
	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getFood_flag() {
		return food_flag;
	}
	public void setFood_flag(String food_flag) {
		this.food_flag = food_flag;
	}
	@Override
	public String toString() {
		return "LoginAuditBean [android_id=" + android_id + ", shop_id="
				+ shop_id + ", user_id=" + user_id + ", actionDate="
				+ actionDate + ", action=" + action + ", food_flag="
				+ food_flag + "]";
	}
	
}
