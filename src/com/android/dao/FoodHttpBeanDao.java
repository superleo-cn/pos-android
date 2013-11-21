package com.android.dao;



import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.bean.FoodHttpBean;

public class FoodHttpBeanDao extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "food_http_bean.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_NAME = "f_http_bean";
	private static  FoodHttpBeanDao mOpenHelper;

	
	private static final String SQL=" CREATE TABLE "
		+ TABLE_NAME
		+ " ( " 
		+ "id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "f_h_b_id" + " varchar,"
		+ "sn" + " varchar,"
		+ "name" + " varchar,"
		+ "nameZh" + " varchar,"
		+ "type" + " varchar,"
		+ "picture" + " varchar"
	    + " ) ";
	private FoodHttpBeanDao(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	 public static  FoodHttpBeanDao getInatance(Context context){
		 if(mOpenHelper==null){
			 mOpenHelper=new FoodHttpBeanDao(context);
		 }
		 return mOpenHelper;
	 }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);
	}

	public long save(FoodHttpBean fhb)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("f_h_b_id", fhb.getId());
		values.put("sn", fhb.getSn());
		values.put("name", fhb.getName());
		values.put("nameZh", fhb.getNameZh());
		values.put("type", fhb.getType());
		values.put("picture", fhb.getPicture());
		long result=db.insert(TABLE_NAME, null, values);
		db.close();
		return result;
		
	}

	public ArrayList<FoodHttpBean> getList(){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor c=db.query(TABLE_NAME, null, null,null, null, null, null, null);
		
		ArrayList<FoodHttpBean> map=new ArrayList<FoodHttpBean>();
		while(c.moveToNext()){
			//android_id,user_id,shop_id,quantity,foodid,discount,totalretailprice,totalpackage,foc,food_flag
			FoodHttpBean fhb = new FoodHttpBean();
			fhb.setId(c.getString(c.getColumnIndex("f_h_b_id")));
			fhb.setSn(c.getString(c.getColumnIndex("sn")));
			fhb.setName(c.getString(c.getColumnIndex("name")));
			fhb.setNameZh(c.getString(c.getColumnIndex("nameZh")));
			fhb.setType(c.getString(c.getColumnIndex("type")));
			fhb.setPicture(c.getString(c.getColumnIndex("picture")));
			map.add(fhb);
		}
		c.close();
		db.close();
		
		return map;
		
	}
	
	public void delete(){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}
	
	public int update_all_type(String food_flag){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("food_flag", "1");
		int result=db.update(TABLE_NAME, values, "food_flag=?", new String[]{food_flag});
		System.err.print("更新了数据库");
		db.close();
		return result;
	}
	
	public int update_type(String food_id){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("food_flag", "1");
		int result=db.update(TABLE_NAME, values, "food_id=?", new String[]{food_id});
		System.err.print("更新了数据库");
		db.close();
		return result;
		
	}
	
}
