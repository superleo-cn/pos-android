package com.android.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.bean.DailyPayDetailBean;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;

@Table(name = "tb_expenses_order")
public class ExpensesOrder extends Model {

	@Column(name = "consumption_id")
	public String consumptionId;

	@Column(name = "shop_id")
	public String shopID;

	@Column(name = "user_id")
	public String userID;

	@Column(name = "price")
	public String price;

	@Column(name = "status")
	public String status;

	@Column(name = "date")
	public String date;

	@Override
	public String toString() {
		return "ExpensesOrder [consumptionId=" + consumptionId + ", shopID=" + shopID + ", userID=" + userID + ", price=" + price
				+ ", status=" + status + ", date=" + date + "]";
	}

	/**
	 * 保存
	 * 
	 * @param bean
	 * @param myApp
	 */
	public static void save(DailyPayDetailBean bean, MyApp myApp) {
		ExpensesOrder expense = new ExpensesOrder();
		expense.status = Constants.DB_FAILED;// 是否成功 1是 0否
		expense.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		expense.consumptionId = bean.getId();
		expense.shopID = myApp.getShopId();// 店idmyApp.getShopid()
		expense.userID = myApp.getUserId();//
		expense.price = StringUtils.defaultIfEmpty(bean.getPrice(), Constants.DEFAULT_PRICE_FLOAT);
		expense.save();
	}

	/**
	 * 保存每日支付金额
	 * */
	public static void saveExpenses(List<DailyPayDetailBean> detail_classList, MyApp myApp) {
		/* 提交每日支付金额 */
		for (DailyPayDetailBean bean : detail_classList) {
			save(bean, myApp);
		}
	}

	/**
	 * 删除所有支付款项
	 */
	public static void deleteAll() {
		new Delete().from(ExpensesOrder.class).execute();
	}

	/**
	 * 返回列表
	 * 
	 * @return
	 */
	public static List<ExpensesOrder> queryList() {
		return new Select().from(ExpensesOrder.class).execute();
	}

	/**
	 * 店员今天是否有提交完成
	 * 
	 * 
	 * @param time
	 * @param status
	 * @return
	 */
	public static List<ExpensesOrder> todayStatusList(String time, String status) {
		return new Select().from(ExpensesOrder.class).where("date >= ? and status = ?", time, status).execute();
	}

	public static List<ExpensesOrder> statusList(String status) {
		return new Select().from(ExpensesOrder.class).where("status = ?", status).execute();
	}

	/**
	 * 店员今天是否有提交
	 * 
	 * @param userId
	 * @param shopId
	 * @param time
	 * @return
	 */
	public static List<ExpensesOrder> todayList(String userId, String shopId, String time) {
		return new Select().from(ExpensesOrder.class).where("user_id =? and shop_id = ? and date >= ?", userId, shopId, time).execute();
	}

	/**
	 * 该店是否今天完成
	 * 
	 * @param shopId
	 * @param time
	 * @param status
	 * @return
	 */
	public static List<ExpensesOrder> todayCompleted(String shopId, String time, String status) {
		return new Select().from(ExpensesOrder.class).where("shop_id = ? and date >= ? and status = ?", shopId, time, status).execute();
	}

	/**
	 * 按照ID更新数据
	 * 
	 * @param androidId
	 */
	public static void updateByStatus(Long androidId) {
		ExpensesOrder expenses = ExpensesOrder.load(ExpensesOrder.class, androidId);
		if (expenses != null) {
			expenses.status = Constants.DB_SUCCESS;
			expenses.save();
		}
	}

	/**
	 * 删除历史数据
	 * 
	 * @param time
	 */
	public static void deleteByDate(String time) {
		new Delete().from(ExpensesOrder.class).where("date < ?", time).execute();
	}
}
