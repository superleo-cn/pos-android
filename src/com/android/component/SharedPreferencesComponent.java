package com.android.component;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultLong;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

// 用接口代替直接使用变量, 方便安全
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface SharedPreferencesComponent {

	// 如果没有值的话，设置一个默认值 "zh"
	@DefaultString("zh")
	String language();

	@DefaultString("0.5")
	String discount();

	@DefaultString("0.2")
	String packageCost();

	@DefaultString("192.168.0.100")
	String printIp();

	@DefaultString("0")
	String shopId();

	@DefaultLong(600000)
	long time();

}