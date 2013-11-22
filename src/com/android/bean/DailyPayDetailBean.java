package com.android.bean;

public class DailyPayDetailBean {
	private String name;
	private String price;
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DailyPayDetailBean() {
	}

	public DailyPayDetailBean(String name, String price,String id) {
		super();
		this.name = name;
		this.price = price;
		this.id=id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "RiChangListView01 [name=" + name + ", price=" + price + "]";
	}

}
