/**
 *  ClassName: Constants.java
 *  created on 2013-1-24
 *  Copyrights 2013-1-24 hjgang All rights reserved.
 *  site: http://t.qq.com/hjgang2012
 *  email: hjgang@yahoo.cn
 */
package com.android.common;

import android.os.Environment;

/**
 * @author hjgang
 * @category 全局变量
 * @日期 2013年11月11日
 * @时间 下午 22:46:37
 * @年份 2013
 */
public final class Constants {
	/** 系统初始化配置文件名 */
	public static final String SYSTEM_INIT_FILE_NAME = "android_pos_ini";
	public static final String FLAG = "com.android";
	/** 用于标识请求照相功能的activity结果码 */
	public static final int RESULT_CODE_CAMERA = 1;
	/** 图片类型 */
	public static final String IMAGE_UNSPECIFIED = "image/*";
	/** 本地缓存目录 */
	public static final String CACHE_DIR;
	/** 图片缓存目录 */
	public static final String CACHE_DIR_IMAGE;
	/** 待上传图片缓存目录 */
	public static final String CACHE_DIR_UPLOADING_IMG;
	/** 图片目录 */
	public static final String CACHE_IMAGE;
	static {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android_pos/";
		} else {
			CACHE_DIR = Environment.getRootDirectory().getAbsolutePath() + "/Android_pos/";
		}

		CACHE_IMAGE = CACHE_DIR + "/image";
		CACHE_DIR_IMAGE = CACHE_DIR + "/pic";
		CACHE_DIR_UPLOADING_IMG = CACHE_DIR + "/uploading_img";
	}

	private Constants() {
	}

	/** 数据库版本号 */
	public static final int DB_VERSION = 2;
	/** 数据库名 */
	public static final String DB_NAME = "android_pos.db";

	/** 用户数据库 */
	public static final String SQL_USER_INFO_CREATE = "CREATE TABLE user(u_id integer primary key AUTOINCREMENT,username varchar(200),password varchar(200),realname varchar(200),usertype varchar(200),status varchar(200),shop_id varchar(200));";
	public static final String SQL_USER_INFO_ALL = "select * from user;";
	public static final String SQL_USER_INFO_FILETIME = "select * from user where username=''{0}'';";
	public static final String SQL_USER_INFO_INSERT = "INSERT INTO user(username, password,realname, usertype,status,shop_id) VALUES(?,?,?,?,?,?);";
	public static final String SQL_USER_INFO_DELETE_BY = "DELETE FROM user WHERE u_id=''{0}'';";
	public static final String SQL_USER_INFO_DELETE_ALL = "DELETE FROM user;";
	public static final String SQL_USER_INFO_DROP = "DROP TABLE user;";

	/** 菜单数据库 */
	public static final String SQL_ORDER_INFO_CREATE = "CREATE TABLE food_order(o_id integer primary key AUTOINCREMENT,user_id varchar(200),shop_id varchar(200),quantity varchar(200),foodid varchar(200),discount varchar(200),totalretailprice varchar(200),totalpackage varchar(200),foc varchar(200),food_flag varchar(200));";
	public static final String SQL_ORDER_INFO_ALL = "select * from food_order;";
	public static final String SQL_ORDER_INFO_FILETIME = "select * from food_order where food_flag=''{0}'';";
	public static final String SQL_ORDER_INFO_INSERT = "INSERT INTO food_order(user_id, shop_id, quantity,foodid,discount,totalretailprice,totalpackage,foc,food_flag) VALUES(?,?,?,?,?,?,?,?,?);";
	public static final String SQL_ORDER_INFO_UPDATEALL = "UPDATE food_order SET food_flag = ?;";
	public static final String SQL_ORDER_INFO_UPDATE = "UPDATE food_order SET food_flag = ? where o_id=?;";
	public static final String SQL_ORDER_INFO_DELETE_BY = "DELETE FROM food_order WHERE food_flag=''{0}'';";
	public static final String SQL_ORDER_INFO_DELETE_ALL = "DELETE FROM food_order;";
	public static final String SQL_ORDER_INFO_DROP = "DROP TABLE food_order;";

	/** 加载情况分页参数 */
	public static final int PARAM_PAGENO = 1;
	public static final int PARAM_PAGESIZE = 10;
	/** 与服务器端连接的协议名 */
	public static final String PROTOCOL = "http://";
	/** 服务器IP */
	public static final String HOST = "ec2-54-254-145-129.ap-southeast-1.compute.amazonaws.com";
	/** 服务器端口号 */
	public static final String PORT = ":8080";
	/** 应用上下文名 */
	public static final String APP = "";//
	/** 应用上下文完整路径 */
	public static final String URL_CONTEXTPATH = PROTOCOL + HOST + PORT;
	/** 登录页完整URL路径 */
	public static final String URL_LOGIN_PATH = URL_CONTEXTPATH + "/loginJson";
	/** 登录页完整URL路径 */
	public static final String URL_LOGIN_ADMIN_PATH = URL_CONTEXTPATH + "/loginAdminJson";
	/** 点菜单完整URL路径 */
	public static final String URL_FOODSLIST_PATH = URL_CONTEXTPATH + "/foods/listJson/";
	/** 支付款项 */
	public static final String URL_PAY_DETAIL = URL_CONTEXTPATH + "/consumptions/listJson/";
	/** 带回总数 */
	public static final String URL_TAKE_DNUM = URL_CONTEXTPATH + "/cashs/listJson/";
	/** 点菜单提交 */
	public static final String URL_FOOD_ORDER = URL_CONTEXTPATH + "/transactions/submit";

	/** 软件更新 */
	public static final String URL_UPDATE_APP = URL_CONTEXTPATH + "/checkUpdate";
	/** 软件下载 */
	public static final String URL_UPDATE_APP_DOWN = URL_CONTEXTPATH + "/versions/";
	/** 服务器登录记录 */
	public static final String URL_LOGIN_AUDIT = URL_CONTEXTPATH + "/audits/submit";

	/** 提交每日支付款 */
	public static final String URL_POST_PAYLIST = URL_CONTEXTPATH + "/consumeTransactions/submit";
	/** 带回总数接口 */
	public static final String URL_POST_TAKENUM = URL_CONTEXTPATH + "/cashTransactions/submit";
	/** 带回总数接口 */
	public static final String URL_POST_DAILY_MONEY = URL_CONTEXTPATH + "/dailySummarys/submit";

	/** 登录角色配置 */
	public static final String ROLE_SUPERADMIN = "SUPERADMIN";
	public static final String ROLE_CASHIER = "CASHIER";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_OPERATOR = "OPERATOR";

	/** 登录角色配置 */
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILED = 0;
	public static final int STATUS_SERVER_FAILED = -1;
	public static final int STATUS_NETWORK_ERROR = -2;

	/** 数据库更新操作状态 */
	public static final String DB_SUCCESS = "1";
	public static final String DB_FAILED = "0";

	/** 打包价格 */
	public static final Double PACKAGE_COST = 0.2;

	/** 食物种类 */
	public static final String FOOD_STAPLE = "STAPLE";
	public static final String FOOD_DISH = "DISH";
	
	/** 最大金额 */
	public static final String MAX_PRICE = "9999.99";
	public static final Double MAX_NUM_PRICE = 9999.99;

}
