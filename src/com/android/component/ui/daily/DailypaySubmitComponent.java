/**
 * 
 */
package com.android.component.ui.daily;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.adapter.DailyPayDetailAdapter;
import com.android.adapter.TakeNumerAdapter;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.common.MyTextUtils;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.domain.BalanceOrder;
import com.android.domain.Collection;
import com.android.domain.CollectionOrder;
import com.android.domain.Expenses;
import com.android.domain.ExpensesOrder;
import com.android.mapping.StatusMapping;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * @author jingang
 * 
 */
@EBean
public class DailypaySubmitComponent {
	@RootContext
	Context context;

	@App
	MyApp myApp;

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	@Bean
	KeyboardComponent keyboardComponent;

	public void submitAll(String date) {
		postPayList(date);
		postNumList(date);
		postDailyMoney(date);
	}

	/* 提交每日支付 */
	public void postPayList(String searchDate) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			List<ExpensesOrder> datas = ExpensesOrder.todayStatusList(searchDate, Constants.DB_FAILED);
			if (CollectionUtils.isNotEmpty(datas)) {
				for (int i = 0; i < datas.size(); i++) {
					ExpensesOrder expenses = datas.get(i);
					params.put("consumeTransactions[" + i + "].androidId", String.valueOf(expenses.getId()));
					params.put("consumeTransactions[" + i + "].consumption.id", expenses.consumptionId);
					params.put("consumeTransactions[" + i + "].shop.id", expenses.shopID);
					params.put("consumeTransactions[" + i + "].user.id", expenses.userID);
					params.put("consumeTransactions[" + i + "].price",
							StringUtils.defaultIfEmpty(expenses.price, Constants.DEFAULT_PRICE_INT));
				}
			}
			// 异步请求数据
			StatusMapping mapping = StatusMapping.postJSON(Constants.URL_POST_PAYLIST, params);
			if (mapping.code == Constants.STATUS_SUCCESS || mapping.code == Constants.STATUS_FAILED) {
				List<Long> ids = mapping.datas;
				if (CollectionUtils.isNotEmpty(ids)) {
					for (Long id : ids) {
						ExpensesOrder.updateByStatus(id);
					}
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/* 提交带回总数 */
	public void postNumList(String searchDate) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			List<CollectionOrder> datas = CollectionOrder.todayStatusList(searchDate, Constants.DB_FAILED);
			if (CollectionUtils.isNotEmpty(datas)) {
				for (int i = 0; i < datas.size(); i++) {
					CollectionOrder collection = datas.get(i);
					params.put("cashTransactions[" + i + "].androidId", String.valueOf(collection.getId()));
					Log.e("cashTransactions[" + i + "].androidId", String.valueOf(collection.getId()));
					params.put("cashTransactions[" + i + "].cash.id", collection.cashId);
					Log.e("cashTransactions[" + i + "].cash.id", collection.cashId);
					params.put("cashTransactions[" + i + "].shop.id", collection.shopId);
					Log.e("cashTransactions[" + i + "].shop.id", collection.shopId);
					params.put("cashTransactions[" + i + "].user.id", collection.userId);
					Log.e("cashTransactions[" + i + "].user.id", collection.userId);
					params.put("cashTransactions[" + i + "].quantity",
							StringUtils.defaultIfEmpty(collection.quantity, Constants.DEFAULT_PRICE_INT));
					Log.e("cashTransactions[" + i + "].quantity", collection.quantity);
				}
			}
			// 异步请求数据
			StatusMapping mapping = StatusMapping.postJSON(Constants.URL_POST_TAKENUM, params);
			if (mapping.code == Constants.STATUS_SUCCESS || mapping.code == Constants.STATUS_FAILED) {
				List<Long> ids = mapping.datas;
				if (CollectionUtils.isNotEmpty(ids)) {
					for (Long id : ids) {
						CollectionOrder.updateByStatus(id);
					}
				}
			}

		} catch (Exception e) {
			e.getMessage();
		}
	}

	/* 提交每日营业额 */
	public void postDailyMoney(final String searchDate) {
		try {
			List<BalanceOrder> datas = BalanceOrder.todayStatusList(searchDate, Constants.DB_FAILED);
			if (CollectionUtils.isNotEmpty(datas)) {
				for (BalanceOrder balance : datas) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("dailySummary.androidId", String.valueOf(balance.getId()));
					params.put("dailySummary.shop.id", balance.shopId);
					params.put("dailySummary.user.id", balance.userId);
					params.put("dailySummary.aOpenBalance", balance.aOpenBalance);
					params.put("dailySummary.bExpenses", balance.bExpenses);
					params.put("dailySummary.cCashCollected", balance.cCashCollected);
					params.put("dailySummary.dDailyTurnover", balance.dDailyTurnover);
					params.put("dailySummary.eNextOpenBalance", balance.eNextOpenBalance);
					params.put("dailySummary.fBringBackCash", balance.fBringBackCash);
					params.put("dailySummary.gTotalBalance", balance.gTotalBalance);
					params.put("dailySummary.middleCalculateTime", balance.middleCalculateTime);
					params.put("dailySummary.middleCalculateBalance", balance.middleCalculateBalance);
					params.put("dailySummary.calculateTime", balance.calculateTime);
					params.put("dailySummary.courier", balance.courier);
					params.put("dailySummary.others", balance.others);
					params.put("dailySummary.date", balance.date);

					// 异步请求数据
					StatusMapping mapping = StatusMapping.postJSON(Constants.URL_POST_DAILY_MONEY, params);
					if (mapping.code == Constants.STATUS_SUCCESS || mapping.code == Constants.STATUS_FAILED) {
						List<Long> ids = mapping.datas;
						if (CollectionUtils.isNotEmpty(ids)) {
							for (Long id : ids) {
								BalanceOrder.updateByStatus(id);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * 保存每日其他数据
	 * */
	public void save(TextView shop_money, TextView text_id_all_price, TextView cash_register, TextView today_turnover,
			TextView tomorrow_money, TextView total_take_num, TextView total, TextView noon_time, TextView noon_turnover, TextView time,
			TextView other, TextView send_person) {
		String aOpenBalance = MyTextUtils.checkIntTextView(shop_money);
		String bExpenses = MyTextUtils.checkIntTextView(text_id_all_price);
		String cCashCollected = MyTextUtils.checkIntTextView(cash_register);
		String dDailyTurnover = MyTextUtils.checkIntTextView(today_turnover);
		String eNextOpenBalance = MyTextUtils.checkIntTextView(tomorrow_money);
		String fBringBackCash = MyTextUtils.checkIntTextView(total_take_num);
		String gTotalBalance = MyTextUtils.checkIntTextView(total);
		String middleCalculateTime = MyTextUtils.checkIntTextView(noon_time);
		String middleCalculateBalance = MyTextUtils.checkIntTextView(noon_turnover);
		String calculateTime = MyTextUtils.checkIntTextView(time);
		String others = MyTextUtils.checkIntTextView(other);
		String courier = MyTextUtils.checkIntTextView(send_person);

		BalanceOrder.save(aOpenBalance, bExpenses, cCashCollected, dDailyTurnover, eNextOpenBalance, fBringBackCash, gTotalBalance,
				middleCalculateTime, middleCalculateBalance, calculateTime, others, courier, myApp);
	}

	/**
	 * 支付款项加载数据
	 * */
	public void loadingExpenses(List<DailyPayDetailBean> detail_classList, List<Double> all_pay_price,
			DailyPayDetailAdapter detail_adapter, ListView daily_list, Handler handler) {
		String type = sharedPrefs.language().get();
		List<Expenses> datas = Expenses.queryList();
		if (datas != null) {
			for (Expenses expenses : datas) {
				DailyPayDetailBean bean = new DailyPayDetailBean();
				bean.setName(expenses.nameZh);
				if (StringUtils.equalsIgnoreCase(Locale.SIMPLIFIED_CHINESE.getLanguage(), type)) {
					bean.setName(expenses.nameZh);
				} else {
					bean.setName(expenses.name);
				}
				bean.setId(expenses.expensesID);
				bean.setPrice(StringUtils.EMPTY);
				detail_classList.add(bean);
				all_pay_price.add(Constants.DEFAULT_PRICE_NUM_FLOAT);
			}

			detail_adapter = new DailyPayDetailAdapter(context, detail_classList, handler);
			daily_list.setAdapter(detail_adapter);

		}
	}

	/**
	 * 带回总数加载数据
	 * */
	public void loadingCollection(List<TakeNumberBean> number_classList, TakeNumerAdapter number_adapter, ListView num_list,
			List<Double> all_num_price, Double num_count, TextView take_all_price, Handler handler) {
		DecimalFormat df = new DecimalFormat(Constants.DEFAULT_PRICE_FLOAT);
		List<Collection> datas_num = Collection.queryList();
		Log.e("查询带回数据库", datas_num.size() + "");
		if (datas_num != null) {
			for (Collection collection : datas_num) {
				TakeNumberBean bean = new TakeNumberBean();
				bean.setPrice(collection.price);
				bean.setId(collection.collectionID);
				bean.setNum("");
				number_classList.add(bean);
			}
			Log.e("打包带走", number_classList.size() + "");
			number_adapter = new TakeNumerAdapter(context, number_classList, handler);
			num_list.setAdapter(number_adapter);
			try {
				for (TakeNumberBean bean : number_classList) {
					String price_tv = bean.getPrice();
					if (StringUtils.isEmpty(price_tv)) {
						price_tv = Constants.DEFAULT_PRICE_FLOAT;
					}
					Double sigle_price = Double.parseDouble(price_tv);
					String num_tv = bean.getNum();
					if (StringUtils.isEmpty(num_tv)) {
						num_tv = Constants.DEFAULT_PRICE_INT;
					}
					int num = Integer.parseInt(num_tv);
					Double total_price = Constants.DEFAULT_PRICE_NUM_FLOAT;
					total_price = num * sigle_price;
					all_num_price.add(total_price);
					num_count = num_count + total_price;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		take_all_price.setText(df.format(num_count));
	}

	/**
	 * 判断某个店员是否已经完成
	 * 
	 * @param date
	 * @return
	 */
	public boolean isCompleted(String date, MyApp myApp) {
		List<ExpensesOrder> expenseslist = ExpensesOrder.todayList(myApp.getUserId(), myApp.getShopId(), date);
		List<BalanceOrder> balanceOrderlist = BalanceOrder.todayList(myApp.getUserId(), myApp.getShopId(), date);
		List<CollectionOrder> collectionOrderlist = CollectionOrder.todayList(myApp.getUserId(), myApp.getShopId(), date);
		if (CollectionUtils.isNotEmpty(expenseslist) && CollectionUtils.isNotEmpty(balanceOrderlist)
				&& CollectionUtils.isNotEmpty(collectionOrderlist)) {
			return true;
		}
		return false;
	}

	/**
	 * 某个点完全是否完成
	 * 
	 * @param date
	 * @return
	 */
	public boolean isCompleted(String date) {
		List<ExpensesOrder> expenseslist = ExpensesOrder.todayCompleted(myApp.getShopId(), date, Constants.DB_FAILED);
		List<BalanceOrder> balanceOrderlist = BalanceOrder.todayCompleted(myApp.getShopId(), date, Constants.DB_FAILED);
		List<CollectionOrder> collectionOrderlist = CollectionOrder.todayCompleted(myApp.getShopId(), date, Constants.DB_FAILED);
		if (CollectionUtils.isNotEmpty(expenseslist) && CollectionUtils.isNotEmpty(balanceOrderlist)
				&& CollectionUtils.isNotEmpty(collectionOrderlist)) {
			return true;
		}
		return false;
	}
}
