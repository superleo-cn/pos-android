package com.android.bean;

public class SelectFoodBean {
	private String food_name;
	private String food_num;
	private String food_price;
	private String food_dayin_code;
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
	@Override
	public String toString() {
		return "SelectFoodBean [food_name=" + food_name + ", food_num="
				+ food_num + ", food_price=" + food_price
				+ ", food_dayin_code=" + food_dayin_code + "]";
	}
	
}
