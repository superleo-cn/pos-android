package com.android.bean;

public class TakeNumberBean {
	private String text1;
	private String text2;

	public TakeNumberBean() {
	}

	public TakeNumberBean(String text1, String text2) {
		super();
		this.text1 = text1;
		this.text2 = text2;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	@Override
	public String toString() {
		return "RiChangListView01 [name=" + text1 + ", price=" + text2 + "]";
	}

}
