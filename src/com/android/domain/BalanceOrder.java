package com.android.domain;

import java.util.Date;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;

@Table(name = "tb_balance_order")
public class BalanceOrder extends Model {
	@Column(name = "shopId")
	public String shopId;

	@Column(name = "userId")
	public String userId;

	@Column(name = "aOpenBalance")
	public String aOpenBalance;

	@Column(name = "bExpenses")
	public String bExpenses;

	@Column(name = "cCashCollected")
	public String cCashCollected;

	@Column(name = "dDailyTurnover")
	public String dDailyTurnover;

	@Column(name = "eNextOpenBalance")
	public String eNextOpenBalance;

	@Column(name = "fBringBackCash")
	public String fBringBackCash;

	@Column(name = "gTotalBalance")
	public String gTotalBalance;

	@Column(name = "courier")
	public String courier;

	@Column(name = "others")
	public String others;

	@Column(name = "status")
	public String status;

	@Column(name = "date")
	public String date;

	@Override
	public String toString() {
		return "BalanceOrder [shopId=" + shopId + ", userId=" + userId + ", aOpenBalance=" + aOpenBalance + ", bExpenses=" + bExpenses
				+ ", cCashCollected=" + cCashCollected + ", dDailyTurnover=" + dDailyTurnover + ", eNextOpenBalance=" + eNextOpenBalance
				+ ", fBringBackCash=" + fBringBackCash + ", gTotalBalance=" + gTotalBalance + ", courier=" + courier + ", others=" + others
				+ ", status=" + status + ", date=" + date + "]";
	}

	/**
	 * 保存
	 * 
	 * @param bean
	 * @param myApp
	 */
	public static void save(BalanceOrder bean, MyApp myApp) {
		BalanceOrder b_order = new BalanceOrder();
		b_order.status = Constants.DB_FAILED;// 是否成功 1是 0否
		b_order.shopId = myApp.getShopId();
		b_order.userId = myApp.getUserId();
		b_order.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		b_order.aOpenBalance = bean.aOpenBalance;
		b_order.bExpenses = bean.bExpenses;
		b_order.cCashCollected = bean.cCashCollected;
		b_order.dDailyTurnover = bean.dDailyTurnover;
		b_order.eNextOpenBalance = bean.eNextOpenBalance;
		b_order.fBringBackCash = bean.fBringBackCash;
		b_order.gTotalBalance = bean.gTotalBalance;
		b_order.courier = bean.courier;
		b_order.others = bean.others;
		b_order.save();
	}

	/**
	 * 保存每日其他数据
	 * */
	public static void save(String aOpenBalance, String bExpenses, String cCashCollected, String dDailyTurnover, String eNextOpenBalance,
			String fBringBackCash, String gTotalBalance, String others, String courier, MyApp myApp) {
		BalanceOrder bean = new BalanceOrder();
		bean.status = Constants.DB_FAILED;// 是否成功 1是 0否
		bean.shopId = myApp.getShopId();// 店idmyApp.getShopid()
		bean.userId = myApp.getUserId();//
		bean.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		bean.aOpenBalance = aOpenBalance;
		bean.bExpenses = bExpenses;
		bean.cCashCollected = cCashCollected;
		bean.dDailyTurnover = dDailyTurnover;
		bean.eNextOpenBalance = eNextOpenBalance;
		bean.fBringBackCash = fBringBackCash;
		bean.gTotalBalance = gTotalBalance;
		bean.others = others;
		bean.courier = courier;
		bean.save();
	}

	/**
	 * 返回列表
	 * 
	 * @return
	 */
	public static List<BalanceOrder> queryList() {
		return new Select().from(BalanceOrder.class).execute();
	}

	/**
	 * 
	 * 店员今天是否有提交完成
	 * 
	 * 
	 * @param time
	 * @param status
	 * @return
	 */
	public static List<BalanceOrder> todayStatusList(String time, String status) {
		return new Select().from(BalanceOrder.class).where(" date >= ? and status=?", time, status).execute();
	}

	public static List<BalanceOrder> statusList(String status) {
		return new Select().from(BalanceOrder.class).where(" status=?", status).execute();
	}

	/**
	 * 店员今天是否有提交
	 * 
	 * @param userId
	 * @param shopId
	 * @param time
	 * @return
	 */
	public static List<BalanceOrder> todayList(String userId, String shopId, String time) {
		return new Select().from(BalanceOrder.class).where("userId =? and shopId = ? and date >= ?", userId, shopId, time).execute();
	}

	/**
	 * 该店是否今天完成
	 * 
	 * @param shopId
	 * @param time
	 * @param status
	 * @return
	 */
	public static List<BalanceOrder> todayCompleted(String shopId, String time, String status) {
		return new Select().from(BalanceOrder.class).where("shopId = ? and date >= ? and status = ?", shopId, time, status).execute();
	}

	/**
	 * 按照ID更新数据
	 * 
	 * @param androidId
	 */
	public static void updateByStatus(Long androidId) {
		BalanceOrder balance = BalanceOrder.load(BalanceOrder.class, androidId);
		if (balance != null) {
			balance.status = Constants.DB_SUCCESS;
			balance.save();
		}
	}
}
