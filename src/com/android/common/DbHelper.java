/**
 *  ClassName: Constants.java
 *  created on 2012-10-19
 *  Copyrights 2012-10-19 hjgang All rights reserved.
 *  site: http://www.hjgang.tk
 *  email: hjgang@yahoo.cn
 */
package com.android.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Android系统中操作SQLite数据库的帮助类 主要用于数据库版本的管理，并提供SQLiteDatabase实例
 * 
 * @author hjgang
 */
public class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}
