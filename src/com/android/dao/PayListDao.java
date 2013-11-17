package com.android.dao;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PayListDao extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "paylist.db";
	private static final int DATABASE_VERSION = 1001;

	private static final String TABLE_NAME = "pricelistdata";
	private static  PayListDao mOpenHelper;

	
	private static final String SQL=" CREATE TABLE "
		+ TABLE_NAME
		+ " ( " 
		+ "_ID"+ " INTEGER PRIMARY KEY, "
		+ "android_id" + " TEXT,"
		+ "consumption_id" + " TEXT,"
		+ "shop_id" + " TEXT,"
		+ "user_id" + " TEXT,"
		+ "date" + " TEXT,"
		+ "type" + " TEXT,"
		+ "price" + " TEXT "
	    + " ) ";
	private PayListDao(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	 public static  PayListDao getInatance(Context context){
		 if(mOpenHelper==null){
			 mOpenHelper=new PayListDao(context);
		 }
		 return mOpenHelper;
	 }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);
	}

	public long save(String android_id,String consumption_id,String shop_id,String user_id,String date,String type,String price){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("android_id", android_id);
		values.put("consumption_id", consumption_id);
		values.put("shop_id", shop_id);
		values.put("user_id", user_id);
		values.put("date", date);
		values.put("type", type);
		values.put("price", price);
		long result=db.insert(TABLE_NAME, null, values);
		db.close();
		return result;
		
	}
	
	public List<Map<String,String>> getList(){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor cursor=db.query(TABLE_NAME, null, null,null, null, null, null, null);
		
		List<Map<String,String>>  list=new ArrayList<Map<String,String>> ();
		while(cursor.moveToNext()){
			Map<String,String> map=new HashMap<String,String>();
			map.put("android_id", cursor.getString(cursor.getColumnIndex("android_id")));
			map.put("consumption_id", cursor.getString(cursor.getColumnIndex("consumption_id")));
			map.put("shop_id", cursor.getString(cursor.getColumnIndex("shop_id")));
			map.put("user_id", cursor.getString(cursor.getColumnIndex("user_id")));
			map.put("date", cursor.getString(cursor.getColumnIndex("date")));
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("price", cursor.getString(cursor.getColumnIndex("price")));
			list.add(map);
		}
		cursor.close();
		db.close();
		
		return list;
		
	}
	
	public void delete(){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

}
