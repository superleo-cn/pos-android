package com.android.component.ui;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.android.bean.LoginAuditBean;
import com.android.bean.LoginUserBean;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.component.ActivityComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.dao.DailyMoneyDao;
import com.android.dao.GetTakeNumDao;
import com.android.dao.LoginAuditDao;
import com.android.dao.NumListDao;
import com.android.dao.PayListDao;
import com.android.dao.UserDao2;
import com.android.dao.getDetailPayListDao;
import com.android.dialog.DialogBuilder;
import com.android.domain.Balance;
import com.android.domain.Collection;
import com.android.domain.Expenses;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;
import com.android.singaporeanorderingsystem.BasicActivity;
import com.android.singaporeanorderingsystem.LoginActivity_;
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
		String type = sharedPrefs.language().get();
		login_name.setText(context.getString(R.string.mainTitle_txt)+" "+myApp.getU_name()+",");
		shop_name1234.setText(myApp.getShop_name() + "-" + myApp.getShop_code());
		write_name.setText(myApp.getU_name());
		send_person.setText(myApp.getU_name());

		List<Map<String, String>> datas = getDetailPayListDao.getInatance(context).getList();
		if (datas == null) {
		} else {
			for (int i = 0; i < datas.size(); i++) {
				DailyPayDetailBean bean = new DailyPayDetailBean();
				bean.setName(datas.get(i).get("name"));
				 if(StringUtils.equalsIgnoreCase("zh", type)){
				 bean.setName(datas.get(i).get("nameZh"));
				 }else{
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
		text_id_all_price.setText(df.format(count));

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
		String detail_price;
		SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = df_date.format(new Date());
		this.search_date = date;
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
			e_bean.price = detail_price;//
			Expenses.save(e_bean, myApp);
		}
		/* 提交每日支付金额结束 */

		/* 提交带回总数接口 */
		String take_num;
		for (int j = 0; j < number_classList.size(); j++) {
			TakeNumberBean bean = number_classList.get(j);
			if (bean != null && StringUtils.isNotEmpty(bean.getNum())) {
				take_num = bean.getNum();
			} else {
				take_num = "0";
			}
			Collection c_bean =new Collection();
			c_bean.cashID=bean.getId();
			c_bean.quantity=take_num;
			Collection.save(c_bean, myApp);
		}

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

		String aOpenBalance = shop_money.getText().toString();
		if (aOpenBalance.isEmpty()) {
			aOpenBalance = "0";
		}
		String bExpenses = text_id_all_price.getText().toString();
		if (bExpenses.isEmpty()) {
			bExpenses = "0";
		}
		String cCashCollected = cash_register.getText().toString();
		if (cCashCollected.isEmpty()) {
			cCashCollected = "0";
		}
		String dDailyTurnover = today_turnover.getText().toString();
		if (dDailyTurnover.isEmpty()) {
			dDailyTurnover = "0";
		}
		String eNextOpenBalance = tomorrow_money.getText().toString();
		if (eNextOpenBalance.isEmpty()) {
			eNextOpenBalance = "0";
		}
		String fBringBackCash = total_take_num.getText().toString();
		if (fBringBackCash.isEmpty()) {
			fBringBackCash = "0";
		}
		String gTotalBalance = total.getText().toString();
		if (gTotalBalance.isEmpty()) {
			gTotalBalance = "0";
		}
		String middleCalculateTime = noon_time.getText().toString();
		if (middleCalculateTime.isEmpty()) {
			middleCalculateTime = "yyyy-MM-dd";
		}
		String middleCalculateBalance = noon_turnover.getText().toString();
		if (middleCalculateBalance.isEmpty()) {
			middleCalculateBalance = "0";
		}
		String calculateTime = time.getText().toString();
		if (calculateTime.isEmpty()) {
			calculateTime = "yyyy-MM-dd";
		}
		String others = other.getText().toString();
		if (others.isEmpty()) {
			others = "";
		}
		String courier = send_person.getText().toString();
		if (courier.isEmpty()) {
			courier = "";
		}
		Balance bean=new Balance();
		bean.aOpenBalance=aOpenBalance;
		bean.bExpenses=bExpenses;
		bean.cCashCollected=cCashCollected;
		bean.dDailyTurnover=dDailyTurnover;
		bean.eNextOpenBalance=eNextOpenBalance;
		bean.fBringBackCash=fBringBackCash;
		bean.gTotalBalance=gTotalBalance;
		bean.middleCalculateTime=middleCalculateTime;
		bean.middleCalculateBalance=middleCalculateBalance;
		bean.calculateTime=calculateTime;
		bean.others=others;
		bean.courier=courier;
		Balance.save(bean, myApp);

		cash_register.setText("");
		today_turnover.setText("");
		noon_time.setText("");
		noon_turnover.setText("");
		time.setText("");
		total.setText("");
		tomorrow_money.setText("");
		total_take_num.setText("");
		send_person.setText("");
		other.setText("");
		shop_money.setText("");
		// text_id_all_price.setText("");

		dailypaysubmitComponent.post_payList(search_date);
		dailypaysubmitComponent.post_numList(search_date);
		dailypaysubmitComponent.post_dailyMoney(search_date);
		

		// 记录退出日志，退出系统
		logUserAction();

	}
	public void logUserAction(){
		final UserDao2 u_dao = UserDao2.getInatance(context);
		LoginUserBean user_bean = new LoginUserBean();
		ArrayList<LoginUserBean> u_datas = null;
		if (StringUtils.equalsIgnoreCase("SUPERADMIN", myApp.getU_type())) {
			user_bean.setId(myApp.getUser_id());
		} else {
			u_datas = u_dao.getList(myApp.getU_name(), myApp.getSettingShopId());
			if (u_datas != null && u_datas.size() != 0) {
				user_bean = u_datas.get(0);
			}
		}
		login_audit(user_bean, "Logout");
		activityComponent.startLogin();
		//System.exit(0);
	}
	
	public void login_audit(LoginUserBean login_user, String action) {
		final LoginAuditDao dao = LoginAuditDao.getInatance(context);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		LoginAuditBean login_audit = new LoginAuditBean();
		login_audit.setUser_id(login_user.getId());
		login_audit.setShop_id(login_user.getShop_id());
		login_audit.setActionDate(df.format(new Date()));
		login_audit.setAction(action);
		login_audit.setFood_flag("0");
		dao.save(login_audit);
		ArrayList<LoginAuditBean> u_datas = dao.getList("0");
		if (u_datas != null && u_datas.size() != 0) {
			HashMap<String, String> params = new HashMap<String, String>();
			for (int i = 0; i < u_datas.size(); i++) {
				LoginAuditBean login_a_bean = u_datas.get(i);
				params.put("audits[" + i + "].androidId", login_a_bean.getAndroid_id());
				params.put("audits[" + i + "].shop.id", login_a_bean.getShop_id());
				params.put("audits[" + i + "].user.id", login_a_bean.getUser_id());
				params.put("audits[" + i + "].actionDate", login_a_bean.getActionDate());
				params.put("audits[" + i + "].action", login_a_bean.getAction());
			}
			try {
				ResponseData data = RemoteDataHandler.post(Constants.URL_LOGIN_AUDIT, params);
				if (data.getCode() == 1) {
					dao.update_all_type("0");
				} else if (data.getCode() == 0) {
					String json = data.getJson();
					json = json.replaceAll("\\[", "");
					json = json.replaceAll("\\]", "");
					String[] str = json.split(",");
					for (int i = 0; i < str.length; i++) {
						dao.update_type(str[i]);
					}
				} else if (data.getCode() == -1) {

				}
			} catch (Exception e) {
				Log.e("error", "LogMessage", e);
			}
		}
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
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(cash_register.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(today_turnover.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(noon_time.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(noon_turnover.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(time.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(total.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(tomorrow_money.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(total_take_num.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(send_person.getWindowToken(), 0); // 强制隐藏键盘
		imm.hideSoftInputFromWindow(shop_money.getWindowToken(), 0); // 强制隐藏键盘
		// Toast.makeText(this, "取消软键盘" , Toast.LENGTH_SHORT).show();

		cash_register.clearFocus();
		today_turnover.clearFocus();
		noon_time.clearFocus();
		noon_turnover.clearFocus();
		time.clearFocus();
		total.clearFocus();
		tomorrow_money.clearFocus();
		total_take_num.clearFocus();
		send_person.clearFocus();
		shop_money.clearFocus();
	}
}
