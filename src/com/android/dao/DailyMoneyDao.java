package com.android.dao;



import java.util.HashMap;

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
		+ "shop_id" + " TEXT,"
		+ "user_id" + " TEXT,"
		+ "aOpenBalance" + " TEXT,"
		+ "bExpenses" + " TEXT,"
		+ "cCashCollected" + " TEXT,"
		+ "dDailyTurnover" + " TEXT,"
		+ "eNextOpenBalance" + " TEXT,"
		+ "fBringBackCash" + " TEXT,"
		+ "gTotalBalance" + " TEXT,"
		+ "middleCalculateTime" + " TEXT,"
		+ "middleCalculateBalance" + " TEXT,"
		+ "calculateTime" + " TEXT,"
		+ "others" + " TEXT,"
		+ "type" + " TEXT,"
		+ "date" + " TEXT,"
		+ "courier" + " TEXT "
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
		db.execSQL(SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);
	}

	public long save(
			String android_id,
			String shop_id,
			String user_id,
			String aOpenBalance,
			String bExpenses,
			String cCashCollected,
			String dDailyTurnover,
			String eNextOpenBalance,
			String fBringBackCash,
			String gTotalBalance,
			String middleCalculateTime,
			String middleCalculateBalance,
			String calculateTime,
			String others,
			String courier,
			String type,
			String date
			)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("android_id", android_id);
		values.put("shop_id", shop_id);
		values.put("user_id", user_id);
		values.put("aOpenBalance", aOpenBalance);
		values.put("bExpenses", bExpenses);
		values.put("cCashCollected", cCashCollected);
		values.put("dDailyTurnover", dDailyTurnover);
		values.put("eNextOpenBalance", eNextOpenBalance);
		values.put("fBringBackCash", fBringBackCash);
		values.put("gTotalBalance", gTotalBalance);
		values.put("middleCalculateTime", middleCalculateTime);
		values.put("middleCalculateBalance", middleCalculateBalance);
		values.put("calculateTime", calculateTime);
		values.put("others", others);
		values.put("courier", courier);
		values.put("type", type);
		values.put("date", date);
		long result=db.insert(TABLE_NAME, null, values);
		db.close();
		return result;
		
	}

	public HashMap<String,String> getList(String date){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor cursor=db.query(TABLE_NAME, null, "date=?",new String[]{date}, null, null, null, null);
		
		HashMap<String,String> map=new HashMap<String,String> ();
		while(cursor.moveToNext()){
			map.put("dailySummary.android.id", cursor.getString(cursor.getColumnIndex("android_id")));
			map.put("dailySummary.shop.id", cursor.getString(cursor.getColumnIndex("shop_id")));
			map.put("dailySummary.user.id", cursor.getString(cursor.getColumnIndex("user_id")));
			map.put("dailySummary.aOpenBalance", cursor.getString(cursor.getColumnIndex("aOpenBalance")));
			map.put("dailySummary.bExpenses", cursor.getString(cursor.getColumnIndex("bExpenses")));
			map.put("dailySummary.cCashCollected", cursor.getString(cursor.getColumnIndex("cCashCollected")));
			map.put("dailySummary.dDailyTurnover", cursor.getString(cursor.getColumnIndex("dDailyTurnover")));
			map.put("dailySummary.eNextOpenBalance", cursor.getString(cursor.getColumnIndex("eNextOpenBalance")));
			map.put("dailySummary.fBringBackCash", cursor.getString(cursor.getColumnIndex("fBringBackCash")));
			map.put("dailySummary.gTotalBalance", cursor.getString(cursor.getColumnIndex("gTotalBalance")));
			map.put("dailySummary.middleCalculateTime", cursor.getString(cursor.getColumnIndex("middleCalculateTime")));
			map.put("dailySummary.middleCalculateBalance", cursor.getString(cursor.getColumnIndex("middleCalculateBalance")));
			map.put("dailySummary.calculateTime", cursor.getString(cursor.getColumnIndex("calculateTime")));
			map.put("dailySummary.others", cursor.getString(cursor.getColumnIndex("others")));
			map.put("dailySummary.courier", cursor.getString(cursor.getColumnIndex("courier")));
			map.put("dailySummary.type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("dailySummary.date", cursor.getString(cursor.getColumnIndex("date")));
		}
		cursor.close();
		db.close();
		
		return map;
		
	}
	
	public void delete(){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}
	
	public int update_type(String date){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("type", "1");
		int result=db.update(TABLE_NAME, values, "date=?", new String[]{date});
		System.err.print("更新了数据库");
		db.close();
		return result;
		
	}
	
}
