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
public class VersionBean {
	public static class Attr{
		public static final String NAME = "name";
		public static final String VERSIONNO = "versionNo";
		public static final String CREATEBY = "createBy";
		public static final String DESCRIPTION = "description";
		public static final String CREATEDATE = "createDate";
	}
	private String name;
	private String versionNo;
	private String createBy;
	private String description;
	private String createDate;
	public VersionBean() {}
	
	public VersionBean(String name, String versionNo, String createBy,
			String description, String createDate) {
		super();
		this.name = name;
		this.versionNo = versionNo;
		this.createBy = createBy;
		this.description = description;
		this.createDate = createDate;
	}

	public static ArrayList<VersionBean> newInstanceList(String jsonDatas){
		ArrayList<VersionBean> login_bean = new ArrayList<VersionBean>();
		
		try {
			JSONArray arr = new JSONArray(jsonDatas);
			int size = null == arr ? 0 : arr.length();
			for(int i = 0; i < size; i++){
				JSONObject obj = arr.getJSONObject(i);
				String name = obj.optString(Attr.NAME);
				String versionNo = obj.optString(Attr.VERSIONNO);
				String createBy = obj.optString(Attr.CREATEBY);
				String description = obj.optString(Attr.DESCRIPTION);
				String createDate = obj.optString(Attr.CREATEDATE);
				login_bean.add(new VersionBean(name, versionNo, createBy, description, createDate)) ;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return login_bean;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "VersionBean [name=" + name + ", versionNo=" + versionNo
				+ ", createBy=" + createBy + ", description=" + description
				+ ", createDate=" + createDate + "]";
	}
	
}
