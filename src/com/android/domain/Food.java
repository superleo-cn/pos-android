package com.android.domain;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.mapping.FoodMapping;

@Table(name = "tb_food")
public class Food extends Model {

	@Column(name = "foodId")
	public String foodId;

	@Column(name = "sn")
	public String sn;

	@Column(name = "bar_code")
	public String barCode;

	@Column(name = "name")
	public String name;

	@Column(name = "nameZh")
	public String nameZh;

	@Column(name = "type")
	public String type;

	@Column(name = "picture")
	public String picture;

	@Column(name = "retail_price")
	public String retailPrice;

	@Column(name = "flag")
	public String flag;

	// 非数据库字段
	public String title;

	@Override
	public String toString() {
		return "Food [type=" + type + ", barCode =" + barCode + ", name=" + name + ", nameZh=" + nameZh + ",  sn=" + sn + ", retail_price="
				+ retailPrice + ", picture=" + picture + ", flag=" + flag + "]";
	}

	/**
	 * 返回食物列表
	 * 
	 * @return
	 */
	public static List<Food> queryList() {
		return new Select().from(Food.class).execute();
	}

	/**
	 * 返回食物显示列表
	 * 
	 * @return
	 */
	public static List<Food> queryListByDisplay() {
		return new Select().from(Food.class).where("flag = 'true'").execute();
	}

	/**
	 * 保存食物
	 * 
	 * @param foodRemote
	 */
	public static void save(FoodMapping.FoodRemote foodRemote) {
		// save Food
		Food food = new Food();
		food.foodId = foodRemote.id;
		food.barCode = foodRemote.barCode;
		food.name = foodRemote.name;
		food.nameZh = foodRemote.nameZh;
		food.picture = foodRemote.picture;
		food.retailPrice = foodRemote.retailPrice;
		food.sn = foodRemote.sn;
		food.type = foodRemote.type;
		food.flag = foodRemote.flag;
		food.save();
	}

	/**
	 * 删除所有食物
	 */
	public static void deleteAll() {
		// save Food
		new Delete().from(Food.class).execute();
	}

}
