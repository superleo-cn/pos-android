package com.android.domain;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "tb_user")
public class User extends Model {

	@Column(name = "uid")
	public String uid;

	@Column(name = "username")
	public String username;

	@Column(name = "password")
	public String password;

	@Column(name = "realname")
	public String realname;

	@Column(name = "usertype")
	public String usertype;

	@Column(name = "status")
	public String status;

	@Column(name = "shop_id")
	public String shopId;

	@Column(name = "shop_name")
	public String shopName;

	@Column(name = "shop_code")
	public String shopCode;

	/**
	 * 判断用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static User checkLogin(String username, String shopId) {
		return new Select().from(User.class).where("username = ? and shop_id = ?", username, shopId).executeSingle();
	}

	/**
	 * 根据ID得到User
	 * 
	 * @param id
	 * @return
	 */
	public static User getUserById(Long id) {
		return Model.load(User.class, id);
	}

}
