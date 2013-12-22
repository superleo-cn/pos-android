/**
 * 
 */
package com.android.component.ui;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import android.content.Context;
import android.util.Log;

import com.android.common.Constants;
import com.android.domain.Balance;
import com.android.domain.Collection;
import com.android.domain.Expenses;
import com.android.mapping.StatusMapping;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

/**
 * @author jingang
 * 
 */
@EBean
public class DailypaySubmitComponent {
	@RootContext
	Context context;

	/* 提交每日支付 */
	public void post_payList(String search_date) {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			List<Expenses> datas=Expenses.TodayList(search_date);
			if (!datas.isEmpty()) {
				for (int i = 0; i < datas.size(); i++) {
					Expenses expenses = datas.get(i);
					params.put("consumeTransactions[" + i + "].androidId", expenses.getId()+"");
					Log.e("consumeTransactions[" + i + "].androidId", expenses.getId()+"");
					params.put("consumeTransactions[" + i + "].consumption.id",expenses.consumptionId);
					Log.e("consumeTransactions[" + i + "].consumption.id", expenses.consumptionId);
//					params.put("consumeTransactions[" + i + "].shop.id", expenses.shopId);
//					Log.e("consumeTransactions[" + i + "].shop.id",expenses.shopId);
//					params.put("consumeTransactions[" + i + "].user.id", expenses.userId);
//					Log.e("consumeTransactions[" + i + "].user.id", expenses.userId);
//					params.put("consumeTransactions[" + i + "].price", expenses.price);
//					Log.e("consumeTransactions[" + i + "].price", expenses.price);
				}
			}
			// 异步请求数据
			StatusMapping mapping = StatusMapping.postJSON(Constants.URL_POST_PAYLIST, params);
			if (mapping.code == 1) {
				Expenses.updateAllByStatus();
			} else if (mapping.code == 0) {
				List<Long> ids = mapping.datas;
				if (CollectionUtils.isNotEmpty(ids)) {
					for (Long id : ids) {
						Expenses.updateByStatus(id);
					}
				}
			} else if (mapping.code == -1) {
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/* 提交带回总数 */
	public void post_numList(String search_date) {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			List<Collection> datas =Collection.TodayList(search_date);
			if (!datas.isEmpty()) {
				for (int i = 0; i < datas.size(); i++) {
					Collection collection = datas.get(i);
						params.put("cashTransactions[" + i + "].androidId", collection.getId()+"");
						Log.e("cashTransactions[" + i + "].androidId", collection.getId()+"");
						params.put("cashTransactions[" + i + "].cash.id", collection.cashID);
						Log.e("cashTransactions[" + i + "].cash.id", collection.cashID);
						params.put("cashTransactions[" + i + "].shop.id", collection.shopId);
						Log.e("cashTransactions[" + i + "].shop.id", collection.shopId);
						params.put("cashTransactions[" + i + "].user.id", collection.userId);
						Log.e("cashTransactions[" + i + "].user.id", collection.userId);
						params.put("cashTransactions[" + i + "].quantity", collection.quantity);
						Log.e("cashTransactions[" + i + "].quantity", collection.quantity);
				}
			}
			// 异步请求数据
			StatusMapping mapping = StatusMapping.postJSON(Constants.URL_POST_TAKENUM, params);
			if (mapping.code == 1) {
				// 更新全部
				Collection.updateAllByStatus();
			} else if (mapping.code == 0) {
				List<Long> ids = mapping.datas;
				if (CollectionUtils.isNotEmpty(ids)) {
					for (Long id : ids) {
						Collection.updateByStatus(id);
					}
				}
			} else if (mapping.code == -1) {
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/* 提交每日营业额 */
	public void post_dailyMoney(final String search_date) {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			List<Balance> datas=Balance.TodayList(search_date);
			if (!datas.isEmpty()) {
				for (int i = 0; i < datas.size(); i++) {
					Balance balance = datas.get(i);
					params.put("cashTransactions[" + i + "].androidId", balance.getId()+"");
					params.put("cashTransactions[" + i + "].aOpenBalance", balance.aOpenBalance);
					params.put("cashTransactions[" + i + "].bExpenses", balance.bExpenses);
					params.put("cashTransactions[" + i + "].cCashCollected", balance.cCashCollected);
					params.put("cashTransactions[" + i + "].dDailyTurnover", balance.dDailyTurnover);
					params.put("cashTransactions[" + i + "].eNextOpenBalance", balance.eNextOpenBalance);
					params.put("cashTransactions[" + i + "].fBringBackCash", balance.fBringBackCash);
					params.put("cashTransactions[" + i + "].gTotalBalance", balance.gTotalBalance);
					params.put("cashTransactions[" + i + "].middleCalculateTime", balance.middleCalculateTime);
					params.put("cashTransactions[" + i + "].middleCalculateBalance", balance.middleCalculateBalance);
					params.put("cashTransactions[" + i + "].calculateTime", balance.calculateTime);
					params.put("cashTransactions[" + i + "].courier", balance.courier);
					params.put("cashTransactions[" + i + "].others", balance.others);
				}
			}
			// 异步请求数据
						StatusMapping mapping = StatusMapping.postJSON(Constants.URL_POST_DAILY_MONEY, params);
						if (mapping.code == 1) {
							// 更新全部
							Balance.updateAllByStatus();
						} else if (mapping.code == 0) {
							List<Long> ids = mapping.datas;
							if (CollectionUtils.isNotEmpty(ids)) {
								for (Long id : ids) {
									Balance.updateByStatus(id);
								}
							}
						} else if (mapping.code == -1) {
						}
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
