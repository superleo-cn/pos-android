package com.android.dao;



import java.util.ArrayList;
import java.util.HashMap;

import com.android.bean.FoodOrder;
import com.android.bean.LoginAuditBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginAuditDao extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "login_audit.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_NAME = "l_audit";
	private static  LoginAuditDao mOpenHelper;

	
	private static final String SQL=" CREATE TABLE "
		+ TABLE_NAME
		+ " ( " 
		+ "android_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "user_id" + " varchar,"
		+ "shop_id" + " varchar,"
		+ "actionDate" + " varchar,"
		+ "action" + " varchar,"
		+ "food_flag" + " varchar"
	    + " ) ";
	private LoginAuditDao(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	 public static  LoginAuditDao getInatance(Context context){
		 if(mOpenHelper==null){
			 mOpenHelper=new LoginAuditDao(context);
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

	public long save(LoginAuditBean login_audit_bean)
	{	
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("user_id", login_audit_bean.getUser_id());
		values.put("shop_id", login_audit_bean.getShop_id());
		values.put("actionDate", login_audit_bean.getActionDate());
		values.put("action", login_audit_bean.getAction());
		values.put("food_flag", login_audit_bean.getFood_flag());
		long result=db.insert(TABLE_NAME, null, values);
		db.close();
		return result;
		
	}

	public ArrayList<LoginAuditBean> getList(String food_flag){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor c=db.query(TABLE_NAME, null, "food_flag=?",new String[]{food_flag}, null, null, null, null);
		
		ArrayList<LoginAuditBean> map=new ArrayList<LoginAuditBean>();
		while(c.moveToNext()){
			//android_id,user_id,shop_id,quantity,foodid,discount,totalretailprice,totalpackage,foc,food_flag
			LoginAuditBean login_bean = new LoginAuditBean();
			login_bean.setAndroid_id(c.getString(c.getColumnIndex("android_id")));
			login_bean.setUser_id(c.getString(c.getColumnIndex("user_id")));
			login_bean.setShop_id(c.getString(c.getColumnIndex("shop_id")));
			login_bean.setActionDate(c.getString(c.getColumnIndex("actionDate")));
			login_bean.setAction(c.getString(c.getColumnIndex("action")));
			login_bean.setFood_flag(c.getString(c.getColumnIndex("food_flag")));
			map.add(login_bean);
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
		int result=db.update(TABLE_NAME, values, "food_flag=?", new String[]{food_id});
		System.err.print("更新了数据库");
		db.close();
		return result;
		
	}
	
}
