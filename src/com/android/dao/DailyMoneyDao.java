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

public class DailyMoneyDao extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "dailymony.db";
	private static final int DATABASE_VERSION = 1005;

	private static final String TABLE_NAME = "dailymoneydata";
	private static  DailyMoneyDao mOpenHelper;

	
	private static final String SQL=" CREATE TABLE "
		+ TABLE_NAME
		+ " ( " 
		+ "_ID"+ " INTEGER PRIMARY KEY, "
		+ "android_id" + " TEXT,"
		+ "cash_id" + " TEXT,"
		+ "shop_id" + " TEXT,"
		+ "user_id" + " TEXT,"
		+ "date" + " TEXT,"
		+ "type" + " TEXT,"
		+ "quantity" + " TEXT "
	    + " ) ";
	private DailyMoneyDao(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	 public static  DailyMoneyDao getInatance(Context context){
		 if(mOpenHelper==null){
			 mOpenHelper=new DailyMoneyDao(context);
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

	public long save(String android_id,String cash_id,String shop_id,String user_id,String date,String type,String quantity){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("android_id", android_id);
		values.put("ccash_id", cash_id);
		values.put("shop_id", shop_id);
		values.put("user_id", user_id);
		values.put("date", date);
		values.put("type", type);
		values.put("quantity", quantity);
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
			map.put("cash_id", cursor.getString(cursor.getColumnIndex("cash_id")));
			map.put("shop_id", cursor.getString(cursor.getColumnIndex("shop_id")));
			map.put("user_id", cursor.getString(cursor.getColumnIndex("user_id")));
			map.put("date", cursor.getString(cursor.getColumnIndex("date")));
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("quantity", cursor.getString(cursor.getColumnIndex("quantity")));
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
