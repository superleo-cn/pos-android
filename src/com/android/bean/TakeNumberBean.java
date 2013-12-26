package com.android.bean;

public class TakeNumberBean {
	private String id;
	private String price;
	private String num;

	public TakeNumberBean() {
	}

	public TakeNumberBean(String id, String price, String num) {
		super();
		this.id = id;
		this.price = price;
		this.num = num;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

}
