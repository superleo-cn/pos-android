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
 * @时间   下午 22:46:37
 * @年份 2013
 */
public final class Constants {
	/** 系统初始化配置文件名 */
	public static final String SYSTEM_INIT_FILE_NAME = "android_pos_ini";
	public static final String FLAG = "com.android";
	/** 用于标识请求照相功能的activity结果码*/    
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
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android_pos/";
		} else {
			CACHE_DIR = Environment.getRootDirectory().getAbsolutePath() + "/Android_pos/";
		}
		
		CACHE_IMAGE= CACHE_DIR + "/image";
		CACHE_DIR_IMAGE = CACHE_DIR + "/pic";
		CACHE_DIR_UPLOADING_IMG = CACHE_DIR + "/uploading_img";
	}
	private Constants(){}
	/** 数据库版本号 */
	public static final int DB_VERSION = 2;
	/** 数据库名 */
	public static final String DB_NAME = "android_pos.db";
	
	/** 用户数据库*/
	public static final String SQL_USER_INFO_CREATE ="CREATE TABLE user(u_id integer primary key AUTOINCREMENT,username varchar(200),password varchar(200),realname varchar(200),usertype varchar(200),status varchar(200));";
	public static final String SQL_USER_INFO_ALL="select * from user;"; 
	public static final String SQL_USER_INFO_FILETIME="select * from user where username=''{0}'';"; 
	public static final String SQL_USER_INFO_INSERT="INSERT INTO user(username, password,realname, usertype,status) VALUES(?,?,?,?,?);";
	public static final String SQL_USER_INFO_DELETE_BY = "DELETE FROM user WHERE u_id=''{0}'';";
	public static final String SQL_USER_INFO_DELETE_ALL = "DELETE FROM user;";
	public static final String SQL_USER_INFO_DROP = "DROP TABLE user;";
	

	/** 加载情况分页参数 */
	public static final int PARAM_PAGENO = 1;
	public static final int PARAM_PAGESIZE = 10;
	/** 与服务器端连接的协议名 */
	public static final String PROTOCOL = "http://";
	/** 服务器IP */
	public static final String HOST = "ec2-54-254-145-129.ap-southeast-1.compute.amazonaws.com:8080/";
	/** 服务器端口号 */
	public static final String PORT = "80";
	/** 应用上下文名 */
	public static final String APP = "";//
	/** 应用上下文完整路径 */
	public static final String URL_CONTEXTPATH = PROTOCOL +HOST;
	/** 登录页完整URL路径 */
	public static final String URL_LOGIN_PATH = URL_CONTEXTPATH+"loginJson";
}
