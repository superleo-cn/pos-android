package com.android.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.android.bean.SelectFoodBean;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;
import com.android.common.MyNumberUtils;

@Table(name = "tb_food_order")
public class FoodOrder extends Model {

	@Column(name = "user_id")
	public String userId;

	@Column(name = "shop_id")
	public String shopId;

	@Column(name = "retail_price")
	public Double retailPrice;

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
		return "FoodOrder [android_id=" + getId() + ", user_id=" + userId + ", shop_id=" + shopId + ", retail_price=" + retailPrice
				+ ", quantity=" + quantity + ", foodid=" + foodId + ", discount=" + discount + ", total_package=" + totalPackage + ", foc="
				+ foc + ", date " + date + "]";
	}

	/**
	 * 返回食物订单列表
	 * 
	 * @return
	 */
	public static List<FoodOrder> queryListByStatus(String status) {
		return new Select().from(FoodOrder.class).where("status = ?", status).execute();
	}

	/**
	 * 分页查询
	 * @return
	 */
	public static List<FoodOrder> queryListByPange(int pangeno, int pangesize) {
		return new Select().from(FoodOrder.class).limit(pangeno + "," + pangesize).execute();
	}
	
	/**
	 * @param startTime,endTime,pangeno,pangesize
	 * @return
	 */
	public static List<FoodOrder> queryListByPange(String startTime,String endTime,int pangeno, int pangesize) {
		return new Select().from(FoodOrder.class).where("date between ? and ?",startTime,endTime).limit(pangeno + "," + pangesize).execute();
	}
	/**
	 * @param status,startTime,endTime,pangeno,pangesize
	 * @return
	 */
	public static List<FoodOrder> queryListByPange(String status,String startTime,String endTime,int pangeno, int pangesize) {
		return new Select().from(FoodOrder.class).where("status = ? and date between ? and ?",status,startTime,endTime).limit(pangeno + "," + pangesize).execute();
	}
	/**
	 * @param status,pangeno,pangesize
	 * @return
	 */
	public static List<FoodOrder> queryListByPange(String status,int pangeno, int pangesize) {
		return new Select().from(FoodOrder.class).where("status = ? ",status).limit(pangeno + "," + pangesize).execute();
	}

	/**
	 * 返回数据个数
	 * 
	 * @return count
	 */

	public static int queryListByCount() {
		return new Select().from(FoodOrder.class).execute().size();
	}

	/**
	 * 返回全部食物订单列表
	 * 
	 * @return
	 */
	public static List<FoodOrder> queryAllList() {
		return new Select().from(FoodOrder.class).execute();
	}

	/**
	 * 返回每天收银的总钱
	 * 
	 * @return
	 */
	public static Double totalRetailCollection(String username, String shopId) {
		FoodOrder food = new Select("sum(retail_price) as retail_price").from(FoodOrder.class)
				.where("user_id = ? and shop_id = ? ", username, shopId).groupBy("user_id, shop_id").executeSingle();
		if (food != null) {
			return food.retailPrice;
		}
		return Constants.DEFAULT_PRICE_NUM_FLOAT;
	}

	/**
	 * 保存食物订单
	 * 
	 * @param bean
	 * @param myApp
	 * @param is_foc
	 */
	public static void save(SelectFoodBean bean, MyApp myApp, boolean is_foc) {
		FoodOrder food_order = new FoodOrder();
		food_order.status = Constants.DB_FAILED;// 是否成功 1是 0否
		food_order.shopId = myApp.getShopId();// 店idmyApp.getShopid()
		food_order.totalPackage = String.valueOf(bean.getDabao_price());// 打包钱数
		food_order.discount = String.valueOf(bean.getDazhe_price()); // 打折钱数
		food_order.userId = myApp.getUserId();// 用户id
		double totalRetailPrice = Double.parseDouble(bean.getFood_price()) - bean.getDazhe_price() + bean.getDabao_price();
		if (is_foc) {
			food_order.foc = Constants.FOC_YES;// 是否免费 1是 0否
			totalRetailPrice = Constants.DEFAULT_PRICE_NUM_INT;
		} else {
			food_order.foc = Constants.FOC_NO;// 是否免费 1是 0否
		}
		food_order.retailPrice = totalRetailPrice;// 收钱数
		food_order.foodId = bean.getFood_id();// 食物id
		food_order.quantity = bean.getFood_num();// 数量
		food_order.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		food_order.save();
	}

	/**
	 * 更新所以提交成功的
	 */
	public static void updateAllByStatus() {
		List<FoodOrder> foodOrders = queryListByStatus(Constants.DB_FAILED);
		if (CollectionUtils.isNotEmpty(foodOrders)) {
			for (FoodOrder foodOrder : foodOrders) {
				foodOrder.status = Constants.DB_SUCCESS;
				foodOrder.save();
			}
		}
	}

	/**
	 * 安装Id更新数据
	 * 
	 * @param androidId
	 */
	public static void updateByStatus(Long androidId) {
		FoodOrder food_order = FoodOrder.load(FoodOrder.class, androidId);
		if (food_order != null) {
			food_order.status = Constants.DB_SUCCESS;
			food_order.save();
		}
	}
}
