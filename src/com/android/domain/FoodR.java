package com.android.domain;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.mapping.FoodAllMapping;

@Table(name = "tb_food")
public class FoodR extends Model {
	
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
	
	@Column(name = "flag")
	public String flag;

	@Column(name = "picture")
	public String picture;

	@Column(name = "retail_price")
	public String retailPrice;
	
	@Column(name = "categorysID")
	public String categorysID;
	
	public String attributesID;
	
	public String attributesContext;

	//非数据库字段
	public String title;
	
	@Override
	public String toString() {
		return "FoodR [foodId=" + foodId + ", sn=" + sn + ", name=" + name
				+ ", nameZh=" + nameZh + ", type=" + type + ", picture="
				+ picture + ", retailPrice=" + retailPrice + ", categorysID="
				+ categorysID + ", attributesID=" + attributesID
				+ ", attributesContext=" + attributesContext + ", title="
				+ title + "]";
	}

	/**
	 * 返回ID食物列表
	 * 
	 * @return
	 */
	public static List<FoodR> queryIDList(String foodId) {
		return new Select().from(FoodR.class).where("categorysID = ?",foodId).execute();
	}
	
	/**
	 * 返回食物列表
	 * 
	 * @return
	 */
	public static List<FoodR> queryList() {
		return new Select().from(FoodR.class).execute();
	}
	
	/**
	 * 返回食物显示列表
	 * 
	 * @return
	 */
	public static List<FoodR> queryListByDisplay() {
		return new Select().from(FoodR.class).where("flag = 'true'").execute();
	}

	/**
	 * 保存食物
	 * 
	 * @param foodRemote
	 */
	public static void save(FoodAllMapping.FoodRemote foodRemote) {
		// save Food
		FoodR food = new FoodR();
		food.foodId = foodRemote.id;
		food.barCode = foodRemote.barCode;
		food.name = foodRemote.name;
		food.nameZh = foodRemote.nameZh;
		food.picture = foodRemote.picture;
		food.retailPrice = foodRemote.retailPrice;
		food.sn = foodRemote.sn;
		food.type = foodRemote.type;
		food.categorysID = foodRemote.category.id;
		System.out.println("food.categorysID->"+food.categorysID);
		food.save();
	}

	/**
	 * 删除所有食物
	 */
	public static void deleteAll() {
		// save Food
		new Delete().from(FoodR.class).execute();
	}
	
	/**
	 * 根据食物ID查询食物信息
	 */
	public static FoodR queryFoodRByFoodId(String foodId) {
		return new Select().from(FoodR.class).where("foodId =?", foodId).executeSingle();
	}

}
