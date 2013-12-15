package com.android.domain;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "tb_food")
public class FoodOrder extends Model {

	@Column(name = "android_id")
	public String androidId;

	@Column(name = "user_id")
	public String userId;

	@Column(name = "shop_id")
	public String shopId;

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

	@Column(name = "quantity")
	public String quantity;

	@Column(name = "food_id")
	public String foodId;

	@Column(name = "discount")
	public String discount;

	@Column(name = "total_package")
	public String totalPackage;

	@Column(name = "foc")
	public String foc;

	@Column(name = "status")
	public String status;

	@Column(name = "date")
	public String date;

	@Override
	public String toString() {
		return "FoodOrder [android_id=" + androidId + ", user_id=" + userId + ", shop_id=" + shopId + ", retail_price=" + retailPrice
				+ ", quantity=" + quantity + ", foodid=" + foodId + ", discount=" + discount + ", total_package=" + totalPackage + ", foc="
				+ foc + ", status=" + status + ", date " + date + "]";
	}

	/**
	 * 返回食物列表
	 * 
	 * @return
	 */
	public static List<FoodOrder> queryList() {
		return new Select().from(FoodOrder.class).execute();
	}

}
