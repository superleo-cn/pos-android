/**
 *  ClassName: NotePadInFoDao.java
 *  created on 2012-10-25
 *  Copyrights 2012-10-25 hjgang All rights reserved.
 *  site: http://www.hjgang.tk
 *  email: hjgang@yahoo.cn
 */
package com.android.dao;

import java.text.MessageFormat;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.bean.FoodOrder;
import com.android.common.Constants;
import com.android.common.DbHelper;

/**
 * @author hjgang
 * @category Dao
 * @日期 2012-10-25
 * @时间 上午9:54:13
 * @年份 2012
 */
public class FoodOrderDao {
	private DbHelper dbHelper;

	public FoodOrderDao(Context context) {
		dbHelper = new DbHelper(context);
	}

	/**
	 * 删除表中的所有数据
	 * @param null
	 * @return null
	 * */
	public void deleteAll() {
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();

			db.execSQL(Constants.SQL_ORDER_INFO_DELETE_ALL);

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	/**
	 * 删除表中的一个数据
	 * @param user_id
	 * @return null
	 * */
	public void delete(String food_flag) {
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();

			String sql = MessageFormat.format(Constants.SQL_ORDER_INFO_DELETE_BY,food_flag);
			db.execSQL(sql);

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * 新增一条数据
	 * @param NotePad
	 * @return null
	 * */
	public void insert(FoodOrder fo) {
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();

			Object[] paramValues = {fo.getUser_id(),fo.getShop_id(),fo.getQuantity()
					,fo.getFoodid(),fo.getDiscount(),fo.getRetailprice(),fo.getTotalpackage(),fo.getFoc(),fo.getFood_flag()};
			db.execSQL(Constants.SQL_ORDER_INFO_INSERT, paramValues);

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	/**
	 * 更新所有数据
	 * @param NotePad
	 * @return null
	 * */
	public void updateall(String food_flag) {
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();

			Object[] paramValues = {food_flag};
			db.execSQL(Constants.SQL_ORDER_INFO_UPDATEALL, paramValues);
			
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}}
	/**
	 * 更新一个数据
	 * @param NotePad
	 * @return null
	 * */
	public void update(String food_flag,String android_id) {
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();

			Object[] paramValues = {food_flag,android_id};
			db.execSQL(Constants.SQL_ORDER_INFO_UPDATE, paramValues);
			

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}}
	/**
	 * 查询所有数据
	 * @param null
	 * @return ArrayList<NotePad>
	 * */
	public ArrayList<FoodOrder> findall() {
		ArrayList<FoodOrder> favos = new ArrayList<FoodOrder>();
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();
			c = db.rawQuery(Constants.SQL_ORDER_INFO_ALL, null);
			while (c.moveToNext()) {
				FoodOrder user_bean = new FoodOrder();
				user_bean.setAndroid_id(c.getString(c.getColumnIndex("o_id")));
				user_bean.setUser_id(c.getString(c.getColumnIndex("user_id")));
				user_bean.setShop_id(c.getString(c.getColumnIndex("shop_id")));
				user_bean.setRetailprice(c.getString(c.getColumnIndex("totalretailprice")));
				user_bean.setQuantity(c.getString(c.getColumnIndex("quantity")));
				user_bean.setFoodid(c.getString(c.getColumnIndex("foodid")));
				user_bean.setDiscount(c.getString(c.getColumnIndex("discount")));
				user_bean.setTotalpackage(c.getString(c.getColumnIndex("totalpackage")));
				user_bean.setFoc(c.getString(c.getColumnIndex("foc")));
				user_bean.setFood_flag(c.getString(c.getColumnIndex("food_flag")));
				favos.add(user_bean);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(c != null){
				c.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return favos;
	}

	/**
	 * 查询一个数据
	 * @param String
	 * @return LoginUserBean
	 * */
	public FoodOrder select(String food_flag) {
		FoodOrder user_bean = new FoodOrder();
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();
			String sql = MessageFormat.format(Constants.SQL_ORDER_INFO_FILETIME, food_flag);
			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				user_bean.setAndroid_id(c.getString(c.getColumnIndex("o_id")));
				user_bean.setUser_id(c.getString(c.getColumnIndex("user_id")));
				user_bean.setShop_id(c.getString(c.getColumnIndex("shop_id")));
				user_bean.setRetailprice(c.getString(c.getColumnIndex("totalretailprice")));
				user_bean.setQuantity(c.getString(c.getColumnIndex("quantity")));
				user_bean.setFoodid(c.getString(c.getColumnIndex("foodid")));
				user_bean.setDiscount(c.getString(c.getColumnIndex("discount")));
				user_bean.setTotalpackage(c.getString(c.getColumnIndex("totalpackage")));
				user_bean.setFoc(c.getString(c.getColumnIndex("foc")));
				user_bean.setFood_flag(c.getString(c.getColumnIndex("food_flag")));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
		return user_bean;
	}
}
