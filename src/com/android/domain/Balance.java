package com.android.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;

@Table(name = "tb_balance")
public class Balance extends Model{
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
	
	@Column(name = "middleCalculateTime")
	public String middleCalculateTime;
	
	@Column(name = "middleCalculateBalance")
	public String middleCalculateBalance;
	
	@Column(name = "calculateTime")
	public String calculateTime;
	
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
		return "Balance [shopId=" + shopId + ", userId=" + userId + ", aOpenBalance=" + aOpenBalance + ", bExpenses=" + bExpenses
				+ ", cCashCollected=" + cCashCollected + ", dDailyTurnover=" + dDailyTurnover + ", eNextOpenBalance=" + eNextOpenBalance
				+ ", fBringBackCash=" + fBringBackCash + ", gTotalBalance=" + gTotalBalance + ", middleCalculateTime="
				+ middleCalculateTime + ", middleCalculateBalance=" + middleCalculateBalance + ", calculateTime=" + calculateTime
				+ ", courier=" + courier + ", others=" + others + ", status=" + status + ", date=" + date + "]";
	}
	
	/**
	 * 保存
	 * @param bean
	 * @param myApp
	 */
	public static void save(Balance bean,MyApp myApp) {
		Balance b_order = new Balance();
		b_order.status = Constants.DB_FAILED;// 是否成功 1是 0否
		b_order.shopId = myApp.getSettingShopId();// 店idmyApp.getShopid()
		b_order.userId = myApp.getUser_id();//
		b_order.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		b_order.aOpenBalance=bean.aOpenBalance;
		b_order.bExpenses=bean.bExpenses;
		b_order.cCashCollected=bean.cCashCollected;
		b_order.dDailyTurnover=bean.dDailyTurnover;
		b_order.eNextOpenBalance=bean.eNextOpenBalance;
		b_order.fBringBackCash=bean.fBringBackCash;
		b_order.gTotalBalance=bean.gTotalBalance;
		b_order.middleCalculateTime=bean.middleCalculateTime;
		b_order.middleCalculateBalance=bean.middleCalculateBalance;
		b_order.calculateTime=bean.calculateTime;
		b_order.courier=bean.courier;
		b_order.others=bean.others;
		b_order.save();
	}
	/**
	 * 返回列表
	 * @return
	 */
	public static List<Balance> queryList() {
		return new Select().from(Balance.class).execute();
	}
	/**
	 * 返回订单列表
	 * 
	 * @return
	 */
	public static List<Balance> queryListByStatus(String status) {
		return new Select().from(Balance.class).where("status = ?", status).execute();
	}
	/**
	 * 返回今天数据列表
	 * @return
	 */
	public static List<Balance> TodayList(String time) {
		return new Select().from(Balance.class).where("date = ?", time).execute();
	}
	/**
	 * 返回今天数据列表
	 * @return
	 */
	public static List<Balance> TodayStatusList(String time,String status) {
		return new Select().from(Balance.class).where("date = ? and status=?", time,status).execute();
	}
	/**
	 * 更新所有提交成功的
	 */
	public static void updateAllByStatus() {
		List<Balance> Balances = queryListByStatus(Constants.DB_SUCCESS);
		if (CollectionUtils.isNotEmpty(Balances)) {
			for (Balance Balance : Balances) {
				Balance.status = Constants.DB_SUCCESS;
				Balance.save();
			}
		}
	}
	/**
	 * 按照ID更新数据
	 * @param androidId
	 */
	public static void updateByStatus(Long androidId) {
		Balance balance = Balance.load(Balance.class, androidId);
		if (balance != null) {
			balance.status = Constants.DB_SUCCESS;
			balance.save();
		}
	}
}
