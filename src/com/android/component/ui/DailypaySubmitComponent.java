/**
 * 
 */
package com.android.component.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.adapter.DailyPayDetailAdapter;
import com.android.adapter.TakeNumerAdapter;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.component.SharedPreferencesComponent_;
import com.android.dao.DailyMoneyDao;
import com.android.dao.GetTakeNumDao;
import com.android.dao.getDetailPayListDao;
import com.android.domain.Balance;
import com.android.domain.Collection;
import com.android.domain.CollectionOrder;
import com.android.domain.Expenses;
import com.android.mapping.StatusMapping;
import com.android.singaporeanorderingsystem.PriceSave;
import com.googlecode.androidannotations.annotations.App;
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

	public void submitAll(String date) {

		post_payList(date);
		post_numList(date);
		post_dailyMoney(date);
	}

	/* 提交每日支付 */
	public void post_payList(String search_date) {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			List<Expenses> datas = Expenses.TodayList(search_date);
			if (!datas.isEmpty()) {
				for (int i = 0; i < datas.size(); i++) {
					Expenses expenses = datas.get(i);
					params.put("consumeTransactions[" + i + "].androidId", expenses.getId() + "");
					Log.e("consumeTransactions[" + i + "].androidId", expenses.getId() + "");
					params.put("consumeTransactions[" + i + "].consumption.id", expenses.consumptionId);
					Log.e("consumeTransactions[" + i + "].consumption.id", expenses.consumptionId);
					// params.put("consumeTransactions[" + i + "].shop.id",
					// expenses.shopId);
					// Log.e("consumeTransactions[" + i +
					// "].shop.id",expenses.shopId);
					// params.put("consumeTransactions[" + i + "].user.id",
					// expenses.userId);
					// Log.e("consumeTransactions[" + i + "].user.id",
					// expenses.userId);
					// params.put("consumeTransactions[" + i + "].price",
					// expenses.price);
					// Log.e("consumeTransactions[" + i + "].price",
					// expenses.price);
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
			List<CollectionOrder> datas = CollectionOrder.queryListByDate(search_date);
			if (!datas.isEmpty()) {
				for (int i = 0; i < datas.size(); i++) {
					CollectionOrder collection = datas.get(i);
					params.put("cashTransactions[" + i + "].androidId", collection.getId() + "");
					Log.e("cashTransactions[" + i + "].androidId", collection.getId() + "");
					params.put("cashTransactions[" + i + "].cash.id", collection.cacheId);
					Log.e("cashTransactions[" + i + "].cash.id", collection.cacheId);
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
			List<Balance> datas = Balance.TodayList(search_date);
			if (!datas.isEmpty()) {
				for (int i = 0; i < datas.size(); i++) {
					Balance balance = datas.get(i);
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

	/**
	 * 保存每日支付金额
	 * */
	public void saveExpenses(List<DailyPayDetailBean> detail_classList) {
		String detail_price;
		/* 提交每日支付金额 */
		for (int i = 0; i < detail_classList.size(); i++) {
			DailyPayDetailBean bean = detail_classList.get(i);
			if (bean != null && StringUtils.isNotEmpty(bean.getPrice())) {
				detail_price = bean.getPrice();
			} else {
				detail_price = "0.00";
			}
			Expenses e_bean = new Expenses();
			e_bean.consumptionId = bean.getId();//
			// e_bean.price = detail_price;
			// Expenses.save(e_bean, myApp);
		}
	}

	/**
	 * 保存每日带回总数
	 * */
	public void saveCollection(List<TakeNumberBean> number_classList) {
		String take_num;
		for (int j = 0; j < number_classList.size(); j++) {
			TakeNumberBean bean = number_classList.get(j);
			if (bean != null && StringUtils.isNotEmpty(bean.getNum())) {
				take_num = bean.getNum();
			} else {
				take_num = "0";
			}
			Collection c_bean = new Collection();
			c_bean.cashID = bean.getId();
			c_bean.quantity = take_num;
			Collection.save(c_bean);
		}
	}

	/**
	 * 保存每日其他数据
	 * */
	public void saveOther(TextView shop_money, TextView text_id_all_price, TextView cash_register, TextView today_turnover,
			TextView tomorrow_money, TextView total_take_num, TextView total, TextView noon_time, TextView noon_turnover, TextView time,
			TextView other, TextView send_person) {
		String aOpenBalance = checkIntTextView(shop_money);
		String bExpenses = checkIntTextView(text_id_all_price);
		String cCashCollected = checkIntTextView(cash_register);
		String dDailyTurnover = checkIntTextView(today_turnover);
		String eNextOpenBalance = checkIntTextView(tomorrow_money);
		String fBringBackCash = checkIntTextView(total_take_num);
		String gTotalBalance = checkIntTextView(total);
		String middleCalculateTime = checkIntTextView(noon_time);
		String middleCalculateBalance = checkIntTextView(noon_turnover);
		String calculateTime = checkIntTextView(time);
		String others = checkIntTextView(other);
		String courier = checkIntTextView(send_person);

		Balance bean = new Balance();
		bean.aOpenBalance = aOpenBalance;
		bean.bExpenses = bExpenses;
		bean.cCashCollected = cCashCollected;
		bean.dDailyTurnover = dDailyTurnover;
		bean.eNextOpenBalance = eNextOpenBalance;
		bean.fBringBackCash = fBringBackCash;
		bean.gTotalBalance = gTotalBalance;
		bean.middleCalculateTime = middleCalculateTime;
		bean.middleCalculateBalance = middleCalculateBalance;
		bean.calculateTime = calculateTime;
		bean.others = others;
		bean.courier = courier;
		Balance.save(bean);
	}

	/**
	 * 支付款项加载数据
	 * */
	public void loadingExpenses(List<DailyPayDetailBean> detail_classList, List<Double> all_pay_price,
			DailyPayDetailAdapter detail_adapter, ListView daily_list, Handler handler) {
		String type = sharedPrefs.language().get();
		List<Map<String, String>> datas = getDetailPayListDao.getInatance(context).getList();
		if (datas == null) {
		} else {
			for (int i = 0; i < datas.size(); i++) {
				DailyPayDetailBean bean = new DailyPayDetailBean();
				bean.setName(datas.get(i).get("name"));
				if (StringUtils.equalsIgnoreCase("zh", type)) {
					bean.setName(datas.get(i).get("nameZh"));
				} else {
					bean.setName(datas.get(i).get("name"));
				}
				bean.setId(datas.get(i).get("number_id"));
				bean.setPrice("");
				detail_classList.add(bean);
				all_pay_price.add(0.00);
			}

			detail_adapter = new DailyPayDetailAdapter(context, detail_classList, handler);
			daily_list.setAdapter(detail_adapter);

		}
	}

	/**
	 * 
	 * */
	public void loadingCollection(List<TakeNumberBean> number_classList, TakeNumerAdapter number_adapter, ListView num_list,
			List<Double> all_num_price, Double num_count, TextView take_all_price, Handler handler) {
		DecimalFormat df = new DecimalFormat("0.00");
		List<Map<String, String>> datas_num = GetTakeNumDao.getInatance(context).getList();
		Log.e("查询带回数据库", datas_num.size() + "");
		if (datas_num == null) {
		} else {
			for (int j = 0; j < datas_num.size(); j++) {
				TakeNumberBean bean = new TakeNumberBean();
				bean.setPrice(datas_num.get(j).get("price"));
				;
				bean.setId(datas_num.get(j).get("number_id"));
				bean.setNum("");
				// hashMap_num.put(j, "");
				number_classList.add(bean);
			}
			Log.e("打包带走", number_classList.size() + "");
			number_adapter = new TakeNumerAdapter(context, number_classList, handler);
			num_list.setAdapter(number_adapter);
			try {
				for (int i = 0; i < number_classList.size(); i++) {
					String price_tv = number_classList.get(i).getPrice();
					if (price_tv.equals("")) {
						price_tv = "0.00";
					}
					Double sigle_price = Double.parseDouble(price_tv);
					String num_tv = number_classList.get(i).getNum();
					if (num_tv.equals("")) {
						num_tv = "0";
					}
					int num = Integer.parseInt(num_tv);
					Double total_price = 0.00;
					total_price = num * sigle_price;
					all_num_price.add(total_price);
					// hashMap_numprice.put(i, String.valueOf(total_price));
					num_count = num_count + total_price;
				}

			} catch (Exception e) {

			}
		}
		take_all_price.setText(df.format(num_count));
	}

	public void initView(Button btu_id_sbumit) {
		Double order_price = 0.00;
		String userId = myApp.getUser_id();
		String shopId = myApp.getSettingShopId();
		if (shopId == null) {
			shopId = "0";
		}

		SimpleDateFormat df_save = new SimpleDateFormat("yyyy-MM-dd");
		String date = df_save.format(new Date());
		Log.e("今天日期", date);
		boolean flag = DailyMoneyDao.getInatance(context).isCompleted(shopId, userId, date, "1");
		if (!flag) {
			btu_id_sbumit.setVisibility(View.VISIBLE);
			List<String> priceList = null;
			if (!flag) {
				priceList = PriceSave.getInatance(context).getList(myApp.getUser_id(), date, myApp.getSettingShopId());
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
		} else if (myApp.getDaily_pay_submit_flag().equals("0")) {
			btu_id_sbumit.setVisibility(View.GONE);
		}
	}

	public void submitOver(ListView view, int r) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		int num_of_visible_view = view.getLastVisiblePosition() - view.getFirstVisiblePosition();
		for (int i = 0; i <= num_of_visible_view; i++) {
			EditText edit = (EditText) view.getChildAt(i).findViewById(r);
			edit.setEnabled(false);
			imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);

		}
	}

	public String checkIntTextView(TextView textview) {
		String str = textview.getText().toString();
		if (str.isEmpty()) {
			str = "0";
		}
		return str;
	}

	public String checkTimeTextView(TextView textview) {
		String str = textview.getText().toString();
		if (str.isEmpty()) {
			str = "yyyy-MM-dd";
		}
		return str;
	}

	public String checkStringTextView(TextView textview) {
		String str = textview.getText().toString();
		if (str.isEmpty()) {
			str = "";
		}
		return str;
	}

	/**
	 * 要强制失去焦点的组件
	 * 
	 * @param objs
	 *            不定参数,可以传入任意数量的参数
	 */
	public void clearTextView(TextView... objs) {
		if (objs != null) {
			for (TextView view : objs) {
				view.setText("");
			}
		}
	}
}
