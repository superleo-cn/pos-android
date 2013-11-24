package com.android.singaporeanorderingsystem;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PriceSave extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "order.db";
	private static final int DATABASE_VERSION = 1000;

	private static final String TABLE_NAME = "pricedata";
	private static  PriceSave mOpenHelper;

	
	private static final String SQL=" CREATE TABLE "
		+ TABLE_NAME
		+ " ( " 
		+ "_ID"+ " INTEGER PRIMARY KEY, "
		+ "username"+ " TEXT, "
		+ "date"+ " TEXT, "
		+ "shopId"+ " TEXT, "
		+ "price" + " TEXT "
	    + " ) ";
	private PriceSave(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	 public static  PriceSave getInatance(Context context){
		 if(mOpenHelper==null){
			 mOpenHelper=new PriceSave(context);
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

	public long save(String username,String date,String price,String shopId){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("price", price);
		values.put("username", username);
		values.put("date", date);
		values.put("shopId", shopId);
		long result=db.insert(TABLE_NAME, null, values);
		db.close();
		return result;
		
	}
	
	public ArrayList<String> getList(String username,String date,String shopId){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor cursor=db.query(TABLE_NAME, null, "username=? and date=? and shopId=?",new String[]{username,date,shopId}, null, null, null, null);
		
		ArrayList<String>  list=new ArrayList<String> ();
		while(cursor.moveToNext()){
			list.add(cursor.getString(cursor.getColumnIndex("price")));
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
