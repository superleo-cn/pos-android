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

import com.android.bean.LoginUserBean;
import com.android.common.Constants;
import com.android.common.DbHelper;

/**
 * @author hjgang
 * @category Dao
 * @日期 2012-10-25
 * @时间 上午9:54:13
 * @年份 2012
 */
public class UserDao {
	private DbHelper dbHelper;

	public UserDao(Context context) {
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

			db.execSQL(Constants.SQL_USER_INFO_DELETE_ALL);

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
	public void delete(String user_id) {
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();

			String sql = MessageFormat.format(Constants.SQL_USER_INFO_DELETE_BY,user_id);
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
	public void insert(LoginUserBean user) {
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();

			Object[] paramValues = { user.getUsername(),user.getPasswrod(), user.getRealname(),
					user.getUsertype(),user.getStatus(),user.getShop_id()};

			db.execSQL(Constants.SQL_USER_INFO_INSERT, paramValues);

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
	 * 查询所有数据
	 * @param null
	 * @return ArrayList<NotePad>
	 * */
	public ArrayList<LoginUserBean> findall() {
		ArrayList<LoginUserBean> favos = new ArrayList<LoginUserBean>();
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();
			Cursor c = db.rawQuery(Constants.SQL_USER_INFO_ALL, null);
			while (c.moveToNext()) {
				LoginUserBean user_bean = new LoginUserBean();
				user_bean.setUser_id(c.getString(c.getColumnIndex("u_id")));
				user_bean.setUsername(c.getString(c.getColumnIndex("username")));
				user_bean.setRealname(c.getString(c.getColumnIndex("realname")));
				user_bean.setUsertype(c.getString(c.getColumnIndex("usertype")));
				user_bean.setStatus(c.getString(c.getColumnIndex("status")));
				user_bean.setPasswrod(c.getString(c.getColumnIndex("passwrod")));
				favos.add(user_bean);
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
		return favos;
	}

	/**
	 * 查询一个数据
	 * @param String
	 * @return LoginUserBean
	 * */
	public LoginUserBean select(String user_name) {
		LoginUserBean user_bean = new LoginUserBean();
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getSQLiteDatabase();
			db.beginTransaction();
			String sql = MessageFormat.format(Constants.SQL_USER_INFO_FILETIME, user_name);
			Cursor c = db.rawQuery(sql, null);
			while (c.moveToNext()) {
				user_bean.setUser_id(c.getString(c.getColumnIndex("u_id")));
				user_bean.setUsername(c.getString(c.getColumnIndex("username")));
				user_bean.setRealname(c.getString(c.getColumnIndex("realname")));
				user_bean.setUsertype(c.getString(c.getColumnIndex("usertype")));
				user_bean.setStatus(c.getString(c.getColumnIndex("status")));
				user_bean.setPasswrod(c.getString(c.getColumnIndex("password")));
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
