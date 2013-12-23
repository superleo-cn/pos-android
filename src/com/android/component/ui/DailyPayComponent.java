package com.android.component.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.adapter.DailyPayDetailAdapter;
import com.android.adapter.TakeNumerAdapter;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.component.ActivityComponent;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.dao.DailyMoneyDao;
import com.android.dialog.DialogBuilder;
import com.android.singaporeanorderingsystem.PriceSave;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
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

	@ViewById(R.id.write_name)
	TextView write_name;

	@ViewById(R.id.shop_name1234)
	TextView shop_name1234;

	@ViewById(R.id.send_person)
	EditText send_person;

	@ViewById(R.id.daily_detail_list)
	ListView daily_list;

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

	@ViewById(R.id.menu_btn)
	ImageView menu; // menu按钮

	@ViewById(R.id.other)
	EditText other;

	@ViewById(R.id.wifi_iamge)
	ImageView wifi_image; // wifi 图标

	@ViewById(R.id.login_name)
	TextView login_name; // 用户名字

	@Bean
	DailypaySubmitComponent dailypaysubmitComponent;

	private List<Double> all_num_price = new ArrayList<Double>();
	private List<Double> all_pay_price = new ArrayList<Double>();
	private List<DailyPayDetailBean> detail_classList = new ArrayList<DailyPayDetailBean>();
	private List<TakeNumberBean> number_classList = new ArrayList<TakeNumberBean>();
	private DailyPayDetailAdapter detail_adapter;
	private TakeNumerAdapter number_adapter;
	private DecimalFormat df = new DecimalFormat("0.00");
	private Double num_count = 0.00;
	private Double count = 0.00;
	private Double order_price = 0.00;
	private String search_date;

	@AfterViews
	public void initDailayPay() {
		initData();
	}

	public void initData() {
		login_name.setText(context.getString(R.string.mainTitle_txt) + " " + myApp.getU_name() + ",");
		shop_name1234.setText(myApp.getShop_name() + "-" + myApp.getShop_code());
		write_name.setText(myApp.getU_name());
		send_person.setText(myApp.getU_name());

		dailypaysubmitComponent.loadingExpenses(detail_classList, all_pay_price, detail_adapter, daily_list, handler);
		text_id_all_price.setText(df.format(count));

		dailypaysubmitComponent.loadingCollection(number_classList, number_adapter, num_list, all_num_price, num_count, take_all_price,
				handler);
		compute();
	}

	// 提交数据窗口
	public DialogBuilder CreatedSubmitDialog() {
		DialogBuilder builder = new DialogBuilder(context);
		builder.setTitle(R.string.message_title);
		builder.setMessage(R.string.message_zhifu);
		builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (doValidation()) {
					clear_data();
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
			Toast.makeText(context, context.getString(R.string.dialy_submit_error1), Toast.LENGTH_SHORT).show();
			return false;
		}

		// 带回总数金额不一致
		Double sigle_price = 0.00;
		for (int i = 0; i < all_num_price.size(); i++) {
			sigle_price += all_num_price.get(i).doubleValue();
		}

		if (sigle_price.doubleValue() != totalTakeNum.doubleValue()) {
			Toast.makeText(context, context.getString(R.string.dialy_submit_error2), Toast.LENGTH_SHORT).show();
			return false;
		}

		// 收银机不能为负数
		String txtCashRegister = StringUtils.defaultIfEmpty(cash_register.getText().toString(), "0");
		Double cashRegister = Double.parseDouble(txtCashRegister);
		if (cashRegister < 0) {
			Toast.makeText(context, context.getString(R.string.dialy_submit_error3), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public void clear_data() {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = df_date.format(new Date());
		this.search_date = date;
		/* 提交每日支付金额 */
		dailypaysubmitComponent.saveExpenses(detail_classList);
		/* 提交每日支付金额结束 */

		/* 提交带回总数接口 */
		dailypaysubmitComponent.saveCollection(number_classList);

		/* 提交带回总数接口结束 */

		int num_of_visible_view = num_list.getLastVisiblePosition() - num_list.getFirstVisiblePosition();
		for (int i = 0; i <= num_of_visible_view; i++) {
			EditText edit = (EditText) num_list.getChildAt(i).findViewById(R.id.num_id_price);
			edit.setEnabled(false);
			imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);

		}

		int num_of_view = daily_list.getLastVisiblePosition() - daily_list.getFirstVisiblePosition();
		for (int i = 0; i <= num_of_view; i++) {
			EditText edit = (EditText) daily_list.getChildAt(i).findViewById(R.id.text_id_price);
			edit.setEnabled(false);
			imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);

		}
		dailypaysubmitComponent.saveOther(shop_money, text_id_all_price, cash_register, today_turnover, tomorrow_money, total_take_num,
				total, noon_time, noon_turnover, noon_time, other, send_person);

		dailypaysubmitComponent.clearTextView(cash_register, today_turnover, noon_time, noon_turnover, time, total, tomorrow_money,
				total_take_num, send_person, other, shop_money);

		dailypaysubmitComponent.post_payList(search_date);
		dailypaysubmitComponent.post_numList(search_date);
		dailypaysubmitComponent.post_dailyMoney(search_date);

	}

	public void compute() {
		try {
			String shop_money_text;
			String tomorrow_money_text;
			if (shop_money.getText().toString().length() == 0) {
				shop_money_text = "0";
			} else {
				shop_money_text = shop_money.getText().toString();
			}

			if (tomorrow_money.getText().toString().length() == 0) {
				tomorrow_money_text = "0";
			} else {
				tomorrow_money_text = tomorrow_money.getText().toString();
			}

			String all_price = text_id_all_price.getText().toString();
			Double price_f = Double.parseDouble(take_all_price.getText().toString());
			cash_register.setText(df.format(order_price + Double.parseDouble(shop_money_text) - Double.parseDouble(all_price)));
			Double price_b = Double.parseDouble(all_price);
			Double price_c = Double.parseDouble(cash_register.getText().toString());
			Double price_today = price_b + price_c;
			Double price_d = order_price;
			today_turnover.setText(df.format(price_d));

			Double price_a = Double.parseDouble(shop_money_text);
			Double total_t = price_c - price_b;
			total.setText(df.format(total_t));

			Double price_e = Double.parseDouble(tomorrow_money_text);
			Double take_price = Double.parseDouble(cash_register.getText().toString()) - price_e;
			total_take_num.setText(df.format(take_price));
		} catch (Exception e) {
			Log.e("总计算", e.getMessage());
			// e.getMessage();
		}
	}

	public void initView() {
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
		// initData();
		shop_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				compute();
				Log.e("今日输出价格", "");
			}
		});

		tomorrow_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				compute();
				Log.e("明日输出价格", "");
			}
		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constants.OPEN_WIFI:
				wifi_image.setImageResource(R.drawable.wifi_open);
				break;
			case Constants.CLOSE_WIFI:
				wifi_image.setImageResource(R.drawable.wifi_close);
				break;
			case DailyPayDetailAdapter.CHAGE_NUM_DETAIL:
				try {
					count = 0.00;
					String str = (String) msg.obj;
					Log.e("改变支付款价格", str);
					int num = Integer.parseInt(str.substring(0, 1));
					String price = str.substring(2, str.length());
					Log.e("截取的价格", price);
					all_pay_price.set(num, Double.parseDouble(price));
					Double sigle_price = 0.00;
					for (int i = 0; i < all_pay_price.size(); i++) {
						sigle_price = all_pay_price.get(i).doubleValue();
						count = count + sigle_price;
					}
					text_id_all_price.setText(df.format(count));

					compute();
				} catch (Exception e) {
					Log.e("支付款计算报错信息", e.getMessage());
					Toast.makeText(context, R.string.err_price, Toast.LENGTH_SHORT).show();
				}
				break;
			case TakeNumerAdapter.SET_NUM:
				num_count = 0.00;
				String str = (String) msg.obj;
				Log.e("改变带回总数价格", str);
				int num = Integer.parseInt(str.substring(0, 1));
				String price = str.substring(2, str.length());
				Log.e("截取带回的价格", price);
				all_num_price.set(num, Double.parseDouble(price));
				Double sigle_price = 0.00;
				for (int i = 0; i < all_num_price.size(); i++) {
					sigle_price = all_num_price.get(i).doubleValue();
					num_count = num_count + sigle_price;
				}

				take_all_price.setText(df.format(num_count));
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
}
