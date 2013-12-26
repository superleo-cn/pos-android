package com.android.domain;

import java.util.Date;
import java.util.List;

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

	@Column(name = "expensesID")
	public String expensesID;

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
		return "Expenses [expensesID=" + expensesID + ", name=" + name + ", nameZh=" + nameZh + ", status=" + status + ", date=" + date
				+ "]";
	}

	/**
	 * 保存
	 * 
	 * @param bean
	 * @param myApp
	 */
	public static void save(ExpensesRemote bean) {
		Expenses expenses = new Expenses();
		expenses.status = Constants.DB_FAILED;// 是否成功 1是 0否
		expenses.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		expenses.expensesID = bean.id;
		expenses.name = bean.name;
		expenses.nameZh = bean.nameZh;
		expenses.save();
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
	public static List<Expenses> todayStatusList(String time, String status) {
		return new Select().from(Expenses.class).where("date = ? and status = ?", time, status).execute();
	}

}
