package com.android.dao;



import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.bean.LoginUserBean;

public class UserDao2 extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "user_dao.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_NAME = "u_dao";
	private static  UserDao2 mOpenHelper;

	
	private static final String SQL=" CREATE TABLE "
		+ TABLE_NAME
		+ " ( " 
		+"id integer primary key AUTOINCREMENT,"
		+ "u_id" + " varchar,"
		+ "username" + " varchar,"
		+ "password" + " varchar,"
		+ "realname" + " varchar,"
		+ "usertype" + " varchar,"
		+ "status" + " varchar,"
		+ "shop_id" + " varchar"
	    + " ) ";
	private UserDao2(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	 public static  UserDao2 getInatance(Context context){
		 if(mOpenHelper==null){
			 mOpenHelper=new UserDao2(context);
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

	public long save(LoginUserBean user_bean)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("u_id", user_bean.getId());
		values.put("username", user_bean.getUsername().toLowerCase());
		values.put("password", user_bean.getPasswrod());
		values.put("realname", user_bean.getRealname());
		values.put("usertype", user_bean.getUsertype());
		values.put("status", user_bean.getStatus());
		values.put("shop_id", user_bean.getShop_id());
		
		long result=db.insert(TABLE_NAME, null, values);
		db.close();
		return result;
		
	}

	public ArrayList<LoginUserBean> getList(String login_name){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor c=db.query(TABLE_NAME, null, "username=?",new String[]{login_name.toLowerCase()}, null, null, null, null);
//		Cursor c = db.rawQuery("select * from u_dao where username=?;", new String[]{login_name});
		ArrayList<LoginUserBean> map=new ArrayList<LoginUserBean>();
		while(c.moveToNext()){
			//android_id,user_id,shop_id,quantity,foodid,discount,totalretailprice,totalpackage,foc,food_flag
			LoginUserBean login_user_bean = new LoginUserBean();
			login_user_bean.setId(c.getString(c.getColumnIndex("u_id")));
			login_user_bean.setUsername(c.getString(c.getColumnIndex("username")));
			login_user_bean.setPasswrod(c.getString(c.getColumnIndex("password")));
			login_user_bean.setRealname(c.getString(c.getColumnIndex("realname")));
			login_user_bean.setUsertype(c.getString(c.getColumnIndex("usertype")));
			login_user_bean.setStatus(c.getString(c.getColumnIndex("status")));
			login_user_bean.setShop_id(c.getString(c.getColumnIndex("shop_id")));
			map.add(login_user_bean);
		}
		c.close();
		db.close();
		
		return map;
		
	}
	public ArrayList<LoginUserBean> getList(String login_name,String shop_id){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor c=db.query(TABLE_NAME, null, "username=? and shop_id=?",new String[]{login_name.toLowerCase(),shop_id}, null, null, null, null);
//		Cursor c = db.rawQuery("select * from u_dao where username=?;", new String[]{login_name});
		ArrayList<LoginUserBean> map=new ArrayList<LoginUserBean>();
		while(c.moveToNext()){
			//android_id,user_id,shop_id,quantity,foodid,discount,totalretailprice,totalpackage,foc,food_flag
			LoginUserBean login_user_bean = new LoginUserBean();
			login_user_bean.setId(c.getString(c.getColumnIndex("u_id")));
			login_user_bean.setUsername(c.getString(c.getColumnIndex("username")));
			login_user_bean.setPasswrod(c.getString(c.getColumnIndex("password")));
			login_user_bean.setRealname(c.getString(c.getColumnIndex("realname")));
			login_user_bean.setUsertype(c.getString(c.getColumnIndex("usertype")));
			login_user_bean.setStatus(c.getString(c.getColumnIndex("status")));
			login_user_bean.setShop_id(c.getString(c.getColumnIndex("shop_id")));
			map.add(login_user_bean);
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
	
//	public int update_all_type(String food_flag){
//		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//		ContentValues values=new ContentValues();
//		values.put("food_flag", "1");
//		int result=db.update(TABLE_NAME, values, "food_flag=?", new String[]{food_flag});
//		System.err.print("更新了数据库");
//		db.close();
//		return result;
//	}
//	
//	public int update_type(String food_id){
//		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//		ContentValues values=new ContentValues();
//		values.put("food_flag", "1");
//		int result=db.update(TABLE_NAME, values, "food_id=?", new String[]{food_id});
//		System.err.print("更新了数据库");
//		db.close();
//		return result;
//		
//	}
	
}
