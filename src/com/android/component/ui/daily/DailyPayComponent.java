package com.android.component.ui.daily;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
impimport android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
ort android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.R;
import com.android.adapter.DailyPayDetailAdapter;
import com.android.adaimport android.widget.ScrollView;
pter.TakeNumerAdapter;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;
import com.android.common.MyNumberUtils;
import com.android.common.MyTextUtils;
import com.android.component.ActivityComponent;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.dialog.design.DialogBuilder;
import com.android.domain.CollectionOrder;
import com.android.domain.ExpensesOrder;
import com.android.domain.FoodOrder;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.TextChange;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EBean
public class DailyPayComponent {
	@App
	MyApp myApp;

	@RootContext
	Context context;

	@RootContext
	Activity activity;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	ActivityComponent activityComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	@Pref
	SharedPreferencesComponent_ sharedPrefs;
	
	@ViewById(R.id.scrollviewID)
	ScrollView scrollviewID;
	@ViewById(R.id.write_name)
	TextView write_name;

	@ViewById(R.id.send_person)
	EditText send_person;

	@ViewById(R.id.daily_detail_list)
	ListView daily_list;	
	@ViewById(R.id.scrollviewID)
	ScrollView scrollviewID;

	@ViewById(R.id.daily_num_list)
	ListView num_list;

	@ViewById(R.id.text_id_all_price)
	TextView text_id_all_price;

	@ViewById(R.id.take_all_price)
	TextView take_all_price;

	@ViewById(R.id.tomorrow_money)
	EditText tomorrow_money;

	@ViewById(R.id.shop_money)
	EditText shop_money;

	@ViewById(R.id.cash_register)
	EditText cash_register;

	@ViewById(R.id.today_turnover)
	EditText today_turnover;

	@ViewById(R.id.total)
	EditText total;

	@ViewById(R.id.total_take_num)
	EditText total_take_num;

	@ViewById(R.id.noon_time)
	EditText noon_time;

	@ViewById(R.id.noon_turnover)
	EditText noon_turnover;

	@ViewById(R.id.time)
	EditText time;

	@ViewById(R.id.btu_id_sbumit)
	Button btu_id_sbumit;

	@ViewById(R.id.other)
	EditText other;

	@Bean
	DailypaySubmitComponent dailypaysubmitComponent;

	private List<Double> all_num_price = new ArrayList<Double>();
	private List<Double> all_pay_price = new ArrayList<Double>();
	private List<DailyPayDetailBean> detail_classList = new ArrayList<DailyPayDetailBean>();
	private List<TakeNumberBean> number_classList = new ArrayList<TakeNumberBean>();
	private DailyPayDetailAdapter detail_adapter;
	private TakeNumerAdapter number_adapter;
	private Double num_count = Constants.DEFAULT_PRICE_NUM_FLOAT;
	private Double count = Constants.DEFAULT_PRICE_NUM_FLOAT;
	// 今日收银机
	Double todayReceive = Constants.DEFAULT_PRICE_NUM_FLOAT;

	@AfterViews
	public void initDailayPay() {
		initData();
		initSubmitButton();
		scrollviewID.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				keyboardComponent.dismissKeyboard(v);
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
	}

	public void initData() {
		// 设置记录人和送款人
		write_name.setText(myApp.getUsername());
		send_person.setText(myApp.getUsername());

		// 收银机
		cash_register.setText(MyNumberUtils.numToStr(todayReceive));

		// 加载支付款项
		scrollviewID.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				keyboardComponent.dismissKeyboard(v);
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
		dailypaysubmitComponent.loadingExpenses(detail_classList, all_pay_price, detail_adapter, daily_list, handler);
		text_id_all_price.setText(Constants.DEFAULT_PRICE_FLOAT);

		// 加载支付款项
		dailypaysubmitComponent.loadingCollection(number_classList, number_adapter, num_list, all_num_price, num_count, take_all_price,
				handler);
		// 计算
		// compute();

	}

	public void initSubmitButton() {
		String date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
		Log.e("今天日期", date);
		boolean flag = dailypaysubmitComponent.isComplete(date);
		if (!flag) {
			btu_id_sbumit.setVisibility(View.VISIBLE);
		} else {
			btu_id_sbumit.setVisibility(View.GONE);
		}
	}

	// 提交数据窗口
	public DialogBuilder CreatedSubmitDialog() {
		DialogBuilder builder = new DialogBuilder(context);
		builder.setTitle(R.string.message_title);
		builder.setMessage(R.string.message_zhifu);
		builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (doValidation()) {
					storeAndSync();
					btu_id_sbumit.setVisibility(View.GONE);
				}
			}
		});
		builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return builder;
	}

	public boolean doValidation() {
		// 带回总数 和 开店金额 不能为0
		String txtTotal = StringUtils.defaultIfEmpty(total_take_num.getText().toString(), "0");
		Double totalTakeNum = Double.parseDouble(txtTotal);
		String txtShopMoney = StringUtils.defaultIfEmpty(shop_money.getText().toString(), "0");
		Double shopMoney = Double.parseDouble(txtShopMoney);

		if (totalTakeNum.doubleValue() <= 0 || shopMoney.doubleValue() <= 0) {
			toastComponent.show(stringResComponent.dialy_submit_error1);
			return false;
		}

		// 带回总数金额不一致
		Double sigle_price = Constants.DEFAULT_PRICE_NUM_FLOAT;
		;
		for (int i = 0; i < all_num_price.size(); i++) {
			sigle_price += all_num_price.get(i).doubleValue();
		}

		if (sigle_price.doubleValue() != totalTakeNum.doubleValue()) {
			toastComponent.show(stringResComponent.dialy_submit_error2);
			return false;
		}

		// 收银机不能为负数
		String txtCashRegister = StringUtils.defaultIfEmpty(cash_register.getText().toString(), "0");
		Double cashRegister = Double.parseDouble(txtCashRegister);
		if (cashRegister < 0) {
			toastComponent.show(stringResComponent.dialy_submit_error3);
			return false;
		}
		return true;
	}

	/**
	 * 存储本机并且提交
	 */
	public void storeAndSync() {
		/* 提交每日支付金额 */
		ExpensesOrder.saveExpenses(detail_classList, myApp);
		/* 提交每日支付金额结束 */

		/* 提交带回总数接口 */
		CollectionOrder.save(number_classList, myApp);
		/* 提交带回总数接口结束 */

		// 设置成只读操作
		setReadonly(num_list, R.id.num_id_price);
		setReadonly(daily_list, R.id.text_id_price);

		// 保存其他输入项目
		dailypaysubmitComponent.save(shop_money, text_id_all_price, cash_register, today_turnover, tomorrow_money, total_take_num, total,
				noon_time, noon_turnover, noon_time, other, send_person);

		MyTextUtils.clearTextView(cash_register, today_turnover, noon_time, noon_turnover, time, total, tomorrow_money, total_take_num,
				send_person, other, shop_money);

		String date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
		dailypaysubmitComponent.submitAll(date);
	}

	public void compute() {
		try {

			String shop_money_text = StringUtils.defaultIfEmpty(shop_money.getText().toString(), Constants.DEFAULT_PRICE_INT);
			String tomorrow_money_text = StringUtils.defaultIfEmpty(tomorrow_money.getText().toString(), Constants.DEFAULT_PRICE_INT);
			String all_price = text_id_all_price.getText().toString();
			// 收银机
			cash_register
					.setText(MyNumberUtils.numToStr(todayReceive + Double.parseDouble(shop_money_text) - Double.parseDouble(all_price)));

			// 今日营业额
			Double price_b = Double.parseDouble(all_price);
			Double price_c = Double.parseDouble(cash_register.getText().toString());
			Double price_d = todayReceive;
			today_turnover.setText(MyNumberUtils.numToStr(price_d));

			// 总数
			Double total_t = price_c - price_b;
			total.setText(MyNumberUtils.numToStr(total_t));

			// 带回总数
			Double price_e = Double.parseDouble(tomorrow_money_text);
			Double take_price = Double.parseDouble(cash_register.getText().toString()) - price_e;
			total_take_num.setText(MyNumberUtils.numToStr(take_price));
		} catch (Exception e) {
			Log.e("总计算", e.getMessage());
			// e.getMessage();
		}
	}

	@Click(R.id.btu_id_sbumit)
	void sbumitOnClick() {
		CreatedSubmitDialog().create().show();
	}

	@TextChange(R.id.shop_money)
	void shopmoneyTextChanged() {
		compute();
		Log.e("今日输出价格", "");
	}

	@TextChange(R.id.tomorrow_money)
	void tomorrowmoneyTextChanged() {
		compute();
		Log.e("明日输出价格", "");
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case DailyPayDetailAdapter.CHAGE_NUM_DETAIL:
				try {
					count = Constants.DEFAULT_PRICE_NUM_FLOAT;
					String str = (String) msg.obj;
					Log.e("改变支付款价格", str);
					int num = Integer.parseInt(str.substring(0, 1));
					String price = str.substring(2, str.length());
					Log.e("截取的价格", price);
					all_pay_price.set(num, Double.parseDouble(price));
					for (Double sigle_price : all_pay_price) {
						count = count + sigle_price;
					}
					text_id_all_price.setText(MyNumberUtils.numToStr(count));
					compute();
				} catch (Exception e) {
					Log.e("支付款计算报错信息", e.getMessage());
					toastComponent.show(stringResComponent.err_price);
				}
				break;
			case TakeNumerAdapter.SET_NUM:
				num_count = Constants.DEFAULT_PRICE_NUM_FLOAT;
				String str = (String) msg.obj;
				Log.e("改变带回总数价格", str);
				int num = Integer.parseInt(str.substring(0, 1));
				String price = str.substring(2, str.length());
				Log.e("截取带回的价格", price);
				all_num_price.set(num, Double.parseDouble(price));
				for (Double sigle_price : all_num_price) {
					num_count = num_count + sigle_price;
				}
				take_all_price.setText(MyNumberUtils.numToStr(num_count));
				compute();
				break;
			}
		}

	};

	public void dismissKeyboard() {
		keyboardComponent.dismissKeyboard(cash_register, today_turnover, noon_time, noon_turnover, time, total, tomorrow_money,
				total_take_num, send_person, shop_money);
		keyboardComponent.clearfocusKeyboard(cash_register, today_turnover, noon_time, noon_turnover, time, total, tomorrow_money,
				total_take_num, send_person, shop_money);
	}

	public void setReadonly(ListView view, int r) {
		int num_of_visible_view = view.getLastVisiblePosition() - view.getFirstVisiblePosition();
		for (int i = 0; i <= num_of_visible_view; i++) {
			EditText edit = (EditText) view.getChildAt(i).findViewById(r);
			keyboardComponent.dismissKeyboardReadonly(true, edit);
		}
	}
}
