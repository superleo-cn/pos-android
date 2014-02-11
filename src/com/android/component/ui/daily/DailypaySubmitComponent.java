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

	public void submitToday(String date) {
		postPayList(date);
		postNumList(date);
		postDailyMoney(date);
	}

	public void submitAll() {
		postAllPayList();
		postAllNumList();
		postAllDailyMoney();
	}

	/* 提交每日支付 */
	public void postPayList(String searchDate) {
		try {
			List<ExpensesOrder> datas = ExpensesOrder.todayStatusList(searchDate, Constants.DB_FAILED);
			postPayList(datas);
		} catch (Exception e) {
			Log.e("[AndroidPrinter]", "提交每日支付错误", e);
		}
	}

	public void postAllPayList() {
		try {
			List<ExpensesOrder> datas = ExpensesOrder.statusList(Constants.DB_FAILED);
			postPayList(datas);
		} catch (Exception e) {
			Log.e("[AndroidPrinter]", "提交每日支付错误", e);
		}
	}

	private void postPayList(List<ExpensesOrder> datas) {
		Map<String, String> params = new HashMap<String, String>();
		if (CollectionUtils.isNotEmpty(datas)) {
			for (int i = 0; i < datas.size(); i++) {
				ExpensesOrder expenses = datas.get(i);
				params.put("consumeTransactions[" + i + "].androidId", String.valueOf(expenses.getId()));
				params.put("consumeTransactions[" + i + "].consumption.id", expenses.consumptionId);
				params.put("consumeTransactions[" + i + "].shop.id", expenses.shopID);
				params.put("consumeTransactions[" + i + "].user.id", expenses.userID);
				params.put("consumeTransactions[" + i + "].price", StringUtils.defaultIfEmpty(expenses.price, Constants.DEFAULT_PRICE_INT));
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
	}

	/* 提交带回总数 */
	public void postNumList(String searchDate) {
		try {
			List<CollectionOrder> datas = CollectionOrder.todayStatusList(searchDate, Constants.DB_FAILED);
			postNumList(datas);
		} catch (Exception e) {
			Log.e("[AndroidPrinter]", "带回总数提交错误", e);
		}
	}

	public void postAllNumList() {
		try {
			List<CollectionOrder> datas = CollectionOrder.statusList(Constants.DB_FAILED);
			postNumList(datas);
		} catch (Exception e) {
			Log.e("[AndroidPrinter]", "带回总数提交错误", e);
		}
	}

	private void postNumList(List<CollectionOrder> datas) {
		Map<String, String> params = new HashMap<String, String>();
		if (CollectionUtils.isNotEmpty(datas)) {
			for (int i = 0; i < datas.size(); i++) {
				CollectionOrder collection = datas.get(i);
				params.put("cashTransactions[" + i + "].androidId", String.valueOf(collection.getId()));
				params.put("cashTransactions[" + i + "].cash.id", collection.cashId);
				params.put("cashTransactions[" + i + "].shop.id", collection.shopId);
				params.put("cashTransactions[" + i + "].user.id", collection.userId);
				params.put("cashTransactions[" + i + "].quantity",
						StringUtils.defaultIfEmpty(collection.quantity, Constants.DEFAULT_PRICE_INT));
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
	}

	/* 提交每日营业额 */
	public void postDailyMoney(final String searchDate) {
		try {
			List<BalanceOrder> datas = BalanceOrder.todayStatusList(searchDate, Constants.DB_FAILED);
			postDailyMoney(datas);
		} catch (Exception e) {
			Log.e("[AndroidPrinter]", "每日营业额提交错误", e);
		}
	}

	public void postAllDailyMoney() {
		try {
			List<BalanceOrder> datas = BalanceOrder.statusList(Constants.DB_FAILED);
			postDailyMoney(datas);
		} catch (Exception e) {
			Log.e("[AndroidPrinter]", "每日营业额提交错误", e);
		}
	}

	private void postDailyMoney(List<BalanceOrder> datas) {
		Map<String, String> params = new HashMap<String, String>();
		if (CollectionUtils.isNotEmpty(datas)) {
			for (int i = 0; i < datas.size(); i++) {
				BalanceOrder balance = datas.get(i);
				params.put("dailySummaries[" + i + "].androidId", String.valueOf(balance.getId()));
				params.put("dailySummaries[" + i + "].shop.id", balance.shopId);
				params.put("dailySummaries[" + i + "].user.id", balance.userId);
				params.put("dailySummaries[" + i + "].aOpenBalance", balance.aOpenBalance);
				params.put("dailySummaries[" + i + "].bExpenses", balance.bExpenses);
				params.put("dailySummaries[" + i + "].cCashCollected", balance.cCashCollected);
				params.put("dailySummaries[" + i + "].dDailyTurnover", balance.dDailyTurnover);
				params.put("dailySummaries[" + i + "].eNextOpenBalance", balance.eNextOpenBalance);
				params.put("dailySummaries[" + i + "].fBringBackCash", balance.fBringBackCash);
				params.put("dailySummaries[" + i + "].gTotalBalance", balance.gTotalBalance);
				params.put("dailySummaries[" + i + "].middleCalculateTime", StringUtils.EMPTY);
				params.put("dailySummaries[" + i + "].middleCalculateBalance", StringUtils.EMPTY);
				params.put("dailySummaries[" + i + "].calculateTime", StringUtils.EMPTY);
				params.put("dailySummaries[" + i + "].courier", balance.courier);
				params.put("dailySummaries[" + i + "].others", balance.others);
				params.put("dailySummaries[" + i + "].date", balance.date);
			}

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

	/**
	 * 保存每日其他数据
	 * */
	public void save(TextView shop_money, TextView text_id_all_price, TextView cash_register, TextView today_turnover,
			TextView tomorrow_money, TextView total_take_num, TextView total, TextView other, TextView send_person) {
		String aOpenBalance = MyTextUtils.checkIntTextView(shop_money);
		String bExpenses = MyTextUtils.checkIntTextView(text_id_all_price);
		String cCashCollected = MyTextUtils.checkIntTextView(cash_register);
		String dDailyTurnover = MyTextUtils.checkIntTextView(today_turnover);
		String eNextOpenBalance = MyTextUtils.checkIntTextView(tomorrow_money);
		String fBringBackCash = MyTextUtils.checkIntTextView(total_take_num);
		String gTotalBalance = MyTextUtils.checkIntTextView(total);
		String others = MyTextUtils.checkIntTextView(other);
		String courier = MyTextUtils.checkIntTextView(send_person);

		BalanceOrder.save(aOpenBalance, bExpenses, cCashCollected, dDailyTurnover, eNextOpenBalance, fBringBackCash, gTotalBalance, others,
				courier, myApp);
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
		if (datas_num != null) {
			for (Collection collection : datas_num) {
				TakeNumberBean bean = new TakeNumberBean();
				bean.setPrice(collection.price);
				bean.setId(collection.collectionID);
				bean.setNum("");
				number_classList.add(bean);
			}
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
				Log.e("[AndroidPrinter]", "支付页加载错误", e);
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
		if (CollectionUtils.isNotEmpty(expenseslist) || CollectionUtils.isNotEmpty(balanceOrderlist)
				|| CollectionUtils.isNotEmpty(collectionOrderlist)) {
			return false;
		}
		return true;
	}
}
