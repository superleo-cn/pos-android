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

public class getDetailPayListDao extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "getdetailpay.db";
	private static final int DATABASE_VERSION = 1001;

	private static final String TABLE_NAME = "getdetail";
	private static  getDetailPayListDao mOpenHelper;

	
	private static final String SQL=" CREATE TABLE "
		+ TABLE_NAME
		+ " ( " 
		+ "_ID"+ " INTEGER PRIMARY KEY, "
		+ "number_id" + " varchar,"
		+ "name" + " varchar,"
		+ "nameZh" + " varchar "
	    + " ) ";
	private getDetailPayListDao(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	 public static  getDetailPayListDao getInatance(Context context){
		 if(mOpenHelper==null){
			 mOpenHelper=new getDetailPayListDao(context);
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

	public long save(String number_id,String name,String nameZh){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("number_id", number_id);
		values.put("name", name);
		values.put("nameZh", nameZh);
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
			map.put("number_id", cursor.getString(cursor.getColumnIndex("number_id")));
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("nameZh", cursor.getString(cursor.getColumnIndex("nameZh")));
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
	
//	public int update_type(String id,String type){
//		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//		ContentValues values=new ContentValues();
//		values.put("type", type);
//		int result=db.update(TABLE_NAME, values, "_ID=?", new String[]{id});
//		System.err.print("更新了数据库");
//		db.close();
//		return result;
//		
//	}
}
