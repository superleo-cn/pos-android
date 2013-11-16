package com.android.bean;

public class FoodOrder {
	private String android_id;
	private String user_id;
	private String shop_id;
	private String retailprice;
	private String quantity;
	private String foodid;
	private String discount;
	private String totalpackage;
	private String foc;
	private String food_flag;
	public String getAndroid_id() {
		return android_id;
	}
	public void setAndroid_id(String android_id) {
		this.android_id = android_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getRetailprice() {
		return retailprice;
	}
	public void setRetailprice(String retailprice) {
		this.retailprice = retailprice;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getFoodid() {
		return foodid;
	}
	public void setFoodid(String foodid) {
		this.foodid = foodid;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(String totalpackage) {
		this.totalpackage = totalpackage;
	}
	public String getFoc() {
		return foc;
	}
	public void setFoc(String foc) {
		this.foc = foc;
	}
	public String getFood_flag() {
		return food_flag;
	}
	public void setFood_flag(String food_flag) {
		this.food_flag = food_flag;
	}
	@Override
	public String toString() {
		return "FoodOrder [android_id=" + android_id + ", user_id=" + user_id
				+ ", shop_id=" + shop_id + ", retailprice=" + retailprice
				+ ", quantity=" + quantity + ", foodid=" + foodid
				+ ", discount=" + discount + ", totalpackage=" + totalpackage
				+ ", foc=" + foc + ", food_flag=" + food_flag + "]";
	}
	
}
