package com.android.dao;



import java.util.ArrayList;
import java.util.HashMap;

import com.android.bean.FoodOrder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodOrderDao2 extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "food_order.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_NAME = "f_order";
	private static  FoodOrderDao2 mOpenHelper;

	
	private static final String SQL=" CREATE TABLE "
		+ TABLE_NAME
		+ " ( " 
		+ "android_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "user_id" + " varchar,"
		+ "shop_id" + " varchar,"
		+ "quantity" + " varchar,"
		+ "foodid" + " varchar,"
		+ "discount" + " varchar,"
		+ "totalretailprice" + " varchar,"
		+ "totalpackage" + " varchar,"
		+ "foc" + " varchar,"
		+ "food_flag" + " varchar,"
		+ "date" + " varchar"
	    + " ) ";
	private FoodOrderDao2(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	 public static  FoodOrderDao2 getInatance(Context context){
		 if(mOpenHelper==null){
			 mOpenHelper=new FoodOrderDao2(context);
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

	public long save(FoodOrder fo)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("user_id", fo.getUser_id());
		values.put("shop_id", fo.getShop_id());
		values.put("quantity", fo.getQuantity());
		values.put("foodid", fo.getFoodid());
		values.put("discount", fo.getDiscount());
		values.put("totalretailprice", fo.getRetailprice());
		values.put("totalpackage", fo.getTotalpackage());
		values.put("foc", fo.getFoc());
		values.put("food_flag", fo.getFood_flag());
		values.put("date", fo.getDate());
		long result=db.insert(TABLE_NAME, null, values);
		db.close();
		return result;
		
	}

	public ArrayList<FoodOrder> getList(String food_flag){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor c=db.query(TABLE_NAME, null, "food_flag=?",new String[]{food_flag}, null, null, null, null);
		
		ArrayList<FoodOrder> map=new ArrayList<FoodOrder>();
		while(c.moveToNext()){
			//android_id,user_id,shop_id,quantity,foodid,discount,totalretailprice,totalpackage,foc,food_flag
			FoodOrder user_bean = new FoodOrder();
			user_bean.setAndroid_id(c.getString(c.getColumnIndex("android_id")));
			user_bean.setUser_id(c.getString(c.getColumnIndex("user_id")));
			user_bean.setShop_id(c.getString(c.getColumnIndex("shop_id")));
			user_bean.setRetailprice(c.getString(c.getColumnIndex("totalretailprice")));
			user_bean.setQuantity(c.getString(c.getColumnIndex("quantity")));
			user_bean.setFoodid(c.getString(c.getColumnIndex("foodid")));
			user_bean.setDiscount(c.getString(c.getColumnIndex("discount")));
			user_bean.setTotalpackage(c.getString(c.getColumnIndex("totalpackage")));
			user_bean.setFoc(c.getString(c.getColumnIndex("foc")));
			user_bean.setFood_flag(c.getString(c.getColumnIndex("food_flag")));
			user_bean.setDate(c.getString(c.getColumnIndex("date")));
			map.add(user_bean);
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
		int result=db.update(TABLE_NAME, values, "foodid=?", new String[]{food_id});
		System.err.print("更新了数据库");
		db.close();
		return result;
		
	}
	
}
