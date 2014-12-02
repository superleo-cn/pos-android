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
			CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android_pos";
		} else {
			CACHE_DIR = Environment.getRootDirectory().getAbsolutePath() + "/Android_pos";
		}

		CACHE_IMAGE = CACHE_DIR + "/image";
		CACHE_DIR_IMAGE = CACHE_DIR + "/pic";
		CACHE_DIR_UPLOADING_IMG = CACHE_DIR + "/uploading_img";
	}

	private Constants() {
	}

	/** 加载情况分页参数 */
	public static final int PARAM_PAGENO = 1;
	public static final int PARAM_PAGESIZE = 10;
	/** 与服务器端连接的协议名 */
	public static final String PROTOCOL = "http://";
	/** 服务器IP */
	//public static final String HOST = "pos.weebo.com.sg";
	public static final String HOST = "pos.emd.com.sg";

	/** 服务器端口号 */
	public static final String PORT = ":80";
	/** 应用上下文名 */
	public static final String APP = "";//
	/** 应用上下文完整路径 */
	public static final String URL_CONTEXTPATH = PROTOCOL + HOST + PORT;
	/** 登录页完整URL路径 */
	public static final String URL_LOGIN_PATH = URL_CONTEXTPATH + "/loginJson";
	/** 登录页完整URL路径 */
	public static final String URL_LOGIN_ADMIN_PATH = URL_CONTEXTPATH + "/loginAdminJson";
	/** 店铺完整URL路径 */
	public static final String URL_SHOP_PATH = URL_CONTEXTPATH + "/shops/listJson/";
	/** 点菜单完整URL路径 */
	public static final String URL_FOODSLIST_PATH = URL_CONTEXTPATH + "/foods/listExtJson/";
	/** 支付款项 */
	public static final String URL_PAY_DETAIL = URL_CONTEXTPATH + "/consumptions/listJson/";
	/** 带回总数 */
	public static final String URL_TAKE_DNUM = URL_CONTEXTPATH + "/cashs/listJson/";
	/** 点菜单提交 */
	public static final String URL_FOOD_ORDER = URL_CONTEXTPATH + "/transactions/submit";
	/** 点菜单和属性提交 */
	public static final String URL_FOOD_ORDER_ATTR = URL_CONTEXTPATH + "/transactions/storeWithAttrs";

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

	/** 登录状态状态 */
	public static final String LOGIN = "login";
	public static final String LOGOUT = "logout";

	/** 数据库更新操作状态 */
	public static final String DB_SUCCESS = "1";
	public static final String DB_FAILED = "0";

	/** 是否免费状态 */
	public static final String FOC_YES = "1";
	public static final String FOC_NO = "0";

	/** 食物种类 */
	public static final String MEMBER = "MEMBER";

	public static final String PAYTYPE_CASH = "CASH";
	public static final String PAYTYPE_CARD = "CARD";
	public static final String PLACE_ORDER = "ORDER";
	
	/** 食物种类 */
	public static final String FOODORDER_PAUSE = "-1";// 挂单
	public static final String FOODORDER_SUBMIT_FAILURE = "0";// 结账，但数据还未上传或者上传失败
	public static final String FOODORDER_SUBMIT_SUCCESS = "1";// 结账，上传成功

	/** 最大金额 */
	public static final String MAX_PRICE = "9999.99";
	public static final Double MAX_NUM_PRICE = 9999.99;

	/** 默认金额 */
	public static final String DEFAULT_PRICE_FLOAT = "0.00";
	public static final String DEFAULT_PRICE_INT = "0";
	public static final double DEFAULT_PRICE_NUM_FLOAT = 0.00;
	public static final int DEFAULT_PRICE_NUM_INT = 0;

	public static final int OPEN_WIFI = 1006;
	public static final int CLOSE_WIFI = 1007;

	/** 默认 */
	public static final String DEFAUL_SQL_WHERE = " 1=1 ";
	/** 分割线 */
	public static final String SPLIT_LINE = "---------------------------------";

	/** 默认最多一次提交50条数据 */
	public static final int SYCN_SIZE = 25;
	
	/** 金额单位 */
	public static final String DOLLAR = "S$";

	/** email setting */
	public static final String ENV = "[UAT]";
	public static final String ERR_TITLE = Constants.ENV + "Android Stall Issue at [%s]";
	public static final String ERR_INFO = "Stall [%s] User [%s] got some issue at [%s], the detail information is [%s]";

	public static final String DAILY_SUM_TITLE = Constants.ENV + "Outlet [%s] User [%s] closing at [%s]";
	public static final String DAILY_SUM_INFO = "Total Expenses: $%s\nCash in Drawer: $%s\nCard: $%s\nCash collected: $%s\nTurnover: $%s\nTotal: $%s";

	public static final String mailFrom = "support@weebo.com.sg";
	public static final String mailFromName = "Support";
	public static final String username = "support@weebo.com.sg";
	public static final String password = "sgweeboteam";

	public static final String RECEIVE_EMAIL = "possupport@weebo.com.sg";
	public static final String RECEIVE_DAILY_SUM_EMAIL = "lihui@weebo.com.sg";
}
