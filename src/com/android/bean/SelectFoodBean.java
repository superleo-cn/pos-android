﻿package com.android.bean;

public class SelectFoodBean {
	private String food_name;
	private String food_num;
	private String food_price;
	private String food_dayin_code;
	private String food_id;
	private String food_type;
	
	private double dabao_price;
	private double dazhe_price;
	
	private double gst_charge;
	private double service_charge;
	
	private String attributesID;
	private String attributesContext;
	private String flag;
	private String packageable;
	
	public String getPackageable() {
		return packageable;
	}
	public void setPackageable(String packageable) {
		this.packageable = packageable;
	}
	public String getFood_id() {
		return food_id;
	}
	public void setFood_id(String food_id) {
		this.food_id = food_id;
	}
	public String getFood_name() {
		return food_name;
	}
	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}
	public String getFood_num() {
		return food_num;
	}
	public void setFood_num(String food_num) {
		this.food_num = food_num;
	}
	public String getFood_price() {
		return food_price;
	}
	public void setFood_price(String food_price) {
		this.food_price = food_price;
	}
	public String getFood_dayin_code() {
		return food_dayin_code;
	}
	public void setFood_dayin_code(String food_dayin_code) {
		this.food_dayin_code = food_dayin_code;
	}
	public double getDabao_price() {
		return dabao_price;
	}
	public void setDabao_price(double dabao_price) {
		this.dabao_price = dabao_price;
	}
	public double getDazhe_price() {
		return dazhe_price;
	}
	public void setDazhe_price(double dazhe_price) {
		this.dazhe_price = dazhe_price;
	}
	public double getGst_charge() {
		return gst_charge;
	}
	public void setGst_charge(double gst_charge) {
		this.gst_charge = gst_charge;
	}
	public double getService_charge() {
		return service_charge;
	}
	public void setService_charge(double service_charge) {
		this.service_charge = service_charge;
	}
	public String getFood_type() {
		return food_type;
	}
	public void setFood_type(String food_type) {
		this.food_type = food_type;
	}
	
	public String getAttributesID() {
		return attributesID;
	}
	public void setAttributesID(String attributesID) {
		this.attributesID = attributesID;
	}
	public String getAttributesContext() {
		return attributesContext;
	}
	public void setAttributesContext(String attributesContext) {
		this.attributesContext = attributesContext;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "SelectFoodBean [food_name=" + food_name + ", food_num="
				+ food_num + ", food_price=" + food_price
				+ ", food_dayin_code=" + food_dayin_code + ", food_id="
				+ food_id + ", food_type=" + food_type + ", dabao_price="
				+ dabao_price + ", dazhe_price=" + dazhe_price
				+ ", gst_charge=" + gst_charge + ", service_charge="
				+ service_charge + ", attributesID=" + attributesID
				+ ", attributesContext=" + attributesContext + ", flag=" + flag
				+ ", packageable=" + packageable + "]";
	}
	
}
