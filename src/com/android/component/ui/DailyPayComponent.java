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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.R;
import com.android.adapter.DailyPayDetailAdapter;
import com.android.adapter.TakeNumerAdapter;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.common.MyTextUtils;
import com.android.component.ActivityComponent;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.dialog.DialogBuilder;
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

	@Bean
	MenuComponent menuComponent;

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
		dailypaysubmitComponent.initView(btu_id_sbumit);
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
			toastComponent.show(stringResComponent.dialy_submit_error1);
			return false;
		}

		// 带回总数金额不一致
		Double sigle_price = 0.00;
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

	public void clear_data() {
		/* 提交每日支付金额 */
		dailypaysubmitComponent.saveExpenses(detail_classList);
		/* 提交每日支付金额结束 */

		/* 提交带回总数接口 */
		dailypaysubmitComponent.saveCollectionOrder(number_classList);

		/* 提交带回总数接口结束 */

		dailypaysubmitComponent.submitOver(num_list, R.id.num_id_price);
		dailypaysubmitComponent.submitOver(daily_list, R.id.text_id_price);

		dailypaysubmitComponent.saveOther(shop_money, text_id_all_price, cash_register, today_turnover, tomorrow_money, total_take_num,
				total, noon_time, noon_turnover, noon_time, other, send_person);

		MyTextUtils.clearTextView(cash_register, today_turnover, noon_time, noon_turnover, time, total, tomorrow_money,
				total_take_num, send_person, other, shop_money);

		SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = df_date.format(new Date());
		
		dailypaysubmitComponent.submitAll(date);
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
			cash_register.setText(df.format(order_price + Double.parseDouble(shop_money_text) - Double.parseDouble(all_price)));
			Double price_b = Double.parseDouble(all_price);
			Double price_c = Double.parseDouble(cash_register.getText().toString());
			Double price_d = order_price;
			today_turnover.setText(df.format(price_d));

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

	
	@Click(R.id.btu_id_sbumit)
	void sbumitOnClick() {
		CreatedSubmitDialog().create().show();
	}

	@Click(R.id.menu_btn)
	void menuBtnOnClick() {
		menuComponent.initPopupWindow();
	}

	@TextChange(R.id.shop_money)
	void shopmoneyTextChanged(){
		compute();
		Log.e("今日输出价格", "");
	}
	@TextChange(R.id.tomorrow_money)
	void tomorrowmoneyTextChanged(){
		compute();
		Log.e("明日输出价格", "");
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
					toastComponent.show(stringResComponent.err_price);
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
