/**
 * 
 */
package com.android.component.ui;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.R;
import com.android.adapter.DailyPayDetailAdapter;
import com.android.adapter.TakeNumerAdapter;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.DateUtils;
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
import com.android.singaporeanorderingsystem.PriceSave;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;
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

	@ViewById(R.id.btu_id_sbumit)
	Button btu_id_sbumit;

	public void submitAll(String date) {
		postPayList(date);
		postNumList(date);
		postDailyMoney(date);
	}

	/* 提交每日支付 */
	public void postPayList(String searchDate) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			List<ExpensesOrder> datas = ExpensesOrder.TodayList(searchDate);
			if (!datas.isEmpty()) {
				for (int i = 0; i < datas.size(); i++) {
					ExpensesOrder expenses = datas.get(i);
					params.put("consumeTransactions[" + i + "].androidId", expenses.getId() + "");
					params.put("consumeTransactions[" + i + "].consumption.id", expenses.consumptionId);
					params.put("consumeTransactions[" + i + "].shop.id", expenses.shopID);
					params.put("consumeTransactions[" + i + "].user.id", expenses.userID);
					params.put("consumeTransactions[" + i + "].price", expenses.price);
				}
			}
			// 异步请求数据
			StatusMapping mapping = StatusMapping.postJSON(Constants.URL_POST_PAYLIST, params);
			if (mapping.code == Constants.STATUS_SUCCESS) {
				ExpensesOrder.updateAllByStatus();
			} else if (mapping.code == Constants.STATUS_FAILED) {
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
			List<CollectionOrder> datas = CollectionOrder.queryListByDate(searchDate);
			if (!datas.isEmpty()) {
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
					params.put("cashTransactions[" + i + "].quantity", collection.quantity);
					Log.e("cashTransactions[" + i + "].quantity", collection.quantity);
				}
			}
			// 异步请求数据
			StatusMapping mapping = StatusMapping.postJSON(Constants.URL_POST_TAKENUM, params);
			if (mapping.code == Constants.STATUS_SUCCESS) {
				// 更新全部
				CollectionOrder.updateAllByStatus();
			} else if (mapping.code == Constants.STATUS_FAILED) {
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
			Map<String, String> params = new HashMap<String, String>();
			List<BalanceOrder> datas = BalanceOrder.TodayList(searchDate);
			if (!datas.isEmpty()) {
				for (int i = 0; i < datas.size(); i++) {
					BalanceOrder balance = datas.get(i);
					params.put("cashTransactions[" + i + "].androidId", balance.getId() + "");
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
			if (mapping.code == Constants.STATUS_SUCCESS) {
				// 更新全部
				BalanceOrder.updateAllByStatus();
			} else if (mapping.code == Constants.STATUS_FAILED) {
				List<Long> ids = mapping.datas;
				if (CollectionUtils.isNotEmpty(ids)) {
					for (Long id : ids) {
						BalanceOrder.updateByStatus(id);
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
				all_pay_price.add(Constants.PRICE_NUM_FLOAT);
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
		DecimalFormat df = new DecimalFormat(Constants.PRICE_FLOAT);
		List<Collection> datas_num = Collection.queryList();
		Log.e("查询带回数据库", datas_num.size() + "");
		if (datas_num != null) {
			for (Collection collection : datas_num) {
				TakeNumberBean bean = new TakeNumberBean();
				bean.setPrice(collection.price);
				bean.setId(collection.collectionID);
				bean.setNum("");
				// hashMap_num.put(j, "");
				number_classList.add(bean);
			}
			Log.e("打包带走", number_classList.size() + "");
			number_adapter = new TakeNumerAdapter(context, number_classList, handler);
			num_list.setAdapter(number_adapter);
			try {
				for (TakeNumberBean bean : number_classList) {
					String price_tv = bean.getPrice();
					if (StringUtils.isEmpty(price_tv)) {
						price_tv = Constants.PRICE_FLOAT;
					}
					Double sigle_price = Double.parseDouble(price_tv);
					String num_tv = bean.getNum();
					if (StringUtils.isEmpty(num_tv)) {
						num_tv = Constants.PRICE_INT;
					}
					int num = Integer.parseInt(num_tv);
					Double total_price = Constants.PRICE_NUM_FLOAT;
					total_price = num * sigle_price;
					all_num_price.add(total_price);
					num_count = num_count + total_price;
				}

			} catch (Exception e) {

			}

		}
		take_all_price.setText(df.format(num_count));
	}

	@AfterViews
	public void initDailypaySubmit() {
		Double order_price = Constants.PRICE_NUM_FLOAT;
		String shopId = sharedPrefs.shopId().get();
		String date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
		Log.e("今天日期", date);
		boolean flag = false; // DailyMoneyDao.getInatance(context).isCompleted(shopId,
								// userId, date, "1");
		if (!flag) {
			btu_id_sbumit.setVisibility(View.VISIBLE);
			List<String> priceList = null;
			if (!flag) {
				priceList = PriceSave.getInatance(context).getList(myApp.getUserId(), date, shopId);
			} else {
				btu_id_sbumit.setVisibility(View.GONE);
			}

			// Double price=0.00;
			if (priceList == null) {
				order_price = 0.00;
			} else {
				if (priceList.size() != 0) {
					for (int i = 0; i < priceList.size(); i++) {
						order_price += Double.parseDouble(priceList.get(i));
					}
				} else {
					order_price = 0.00;
				}

			}
		} else if (false) {
			btu_id_sbumit.setVisibility(View.GONE);
		}
	}

}
