package com.android.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.mapping.ExpensesMapping.ExpensesRemote;

@Table(name = "tb_expenses")
public class Expenses extends Model {

	@Column(name = "consumptionId")
	public String consumptionId;

	@Column(name = "name")
	public String name;

	@Column(name = "nameZh")
	public String nameZh;

	@Column(name = "status")
	public String status;

	@Column(name = "date")
	public String date;

	@Override
	public String toString() {
		return "Expenses [consumptionId=" + consumptionId + ", status=" + status + ", date=" + date + "]";
	}

	/**
	 * 保存
	 * 
	 * @param bean
	 * @param myApp
	 */
	public static void save(Expenses bean) {
		Expenses expense = new Expenses();
		expense.status = Constants.DB_FAILED;// 是否成功 1是 0否
		expense.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		expense.consumptionId = bean.consumptionId;
		expense.name = bean.name;
		expense.nameZh = bean.nameZh;
		expense.save();
	}

	public static void save(ExpensesRemote bean) {
		Expenses expense = new Expenses();
		expense.status = Constants.DB_FAILED;// 是否成功 1是 0否
		expense.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		expense.consumptionId = bean.id;
		expense.name = bean.name;
		expense.nameZh = bean.nameZh;
		expense.save();
	}

	/**
	 * 删除所有支付款项
	 */
	public static void deleteAll() {
		new Delete().from(Expenses.class).execute();
	}

	/**
	 * 返回列表
	 * 
	 * @return
	 */
	public static List<Expenses> queryList() {
		return new Select().from(Expenses.class).execute();
	}

	/**
	 * 返回订单列表
	 * 
	 * @return
	 */
	public static List<Expenses> queryListByStatus(String status) {
		return new Select().from(Expenses.class).where("status = ?", status).execute();
	}

	/**
	 * 返回今天数据列表
	 * 
	 * @return
	 */
	public static List<Expenses> TodayList(String time) {
		return new Select().from(Expenses.class).where("date = ?", time).execute();
	}

	/**
	 * 返回今天数据列表
	 * 
	 * @return
	 */
	public static List<Expenses> TodayStatusList(String time, String status) {
		return new Select().from(Expenses.class).where("date = ? and status=?", time, status).execute();
	}

	/**
	 * 更新所有提交成功的
	 */
	public static void updateAllByStatus() {
		List<Expenses> expensess = queryListByStatus(Constants.DB_SUCCESS);
		if (CollectionUtils.isNotEmpty(expensess)) {
			for (Expenses expenses : expensess) {
				expenses.status = Constants.DB_SUCCESS;
				expenses.save();
			}
		}
	}

	/**
	 * 按照ID更新数据
	 * 
	 * @param androidId
	 */
	public static void updateByStatus(Long androidId) {
		Expenses expenses = Expenses.load(Expenses.class, androidId);
		if (expenses != null) {
			expenses.status = Constants.DB_SUCCESS;
			expenses.save();
		}
	}
}
