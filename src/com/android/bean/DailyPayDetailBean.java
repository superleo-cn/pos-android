package com.android.bean;

public class DailyPayDetailBean {
	private String name;
	private String price;

	public DailyPayDetailBean() {
	}

	public DailyPayDetailBean(String name, String price) {
		super();
		this.name = name;
		this.price = price;
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
