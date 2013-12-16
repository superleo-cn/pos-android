package com.android.singaporeanorderingsystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.adapter.FoodListAdapter;
import com.android.adapter.GiditNumberAdapter;
import com.android.adapter.SelectListAdapter;
import com.android.bean.FoodListBean;
import com.android.bean.GiditNumberBean;
import com.android.bean.SelectFoodBean;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;
import com.android.common.MyNumberUtils;
import com.android.component.MenuComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.component.main.FoodComponent;
import com.android.dialog.DialogBuilder;
import com.android.domain.FoodOrder;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

//不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

	@ViewById(R.id.menu_btn)
	ImageView menu; // menu按钮

	@ViewById(R.id.login_name)
	TextView login_name; // 用户名字

	@ViewById(R.id.layout_exit)
	RelativeLayout exit_layout; // 退出

	@ViewById(R.id.wifi_iamge)
	ImageView wifi_image; // wifi 图标

	@ViewById(R.id.food_list)
	GridView foodView; // 菜品列表

	@ViewById(R.id.select_list)
	ListView select_list; // 选择的菜品

	@ViewById(R.id.total_price)
	TextView total_price; // 总价格

	@ViewById(R.id.gathering)
	TextView gathering; // 收款

	@ViewById(R.id.surplus)
	TextView surplus; // 剩余

	@ViewById(R.id.take_package)
	ImageView take_package; // 打包选项

	@ViewById(R.id.foc)
	ImageView foc; // FOC

	@ViewById(R.id.discount)
	ImageView discount; // 打折选项

	@ViewById(R.id.r_lay_id_take_package)
	RelativeLayout r_lay_id_take_package;

	@ViewById(R.id.r_lay_id_foc)
	RelativeLayout r_lay_id_foc;

	@ViewById(R.id.r_lay_id_discount)
	RelativeLayout r_lay_id_discount;

	@ViewById(R.id.digit_btn)
	GridView giditNum_view; // 0-9按钮 用gridView做的按钮

	@ViewById(R.id.ok_btn)
	Button ok_btn; // ok 按钮

	@ViewById(R.id.shop_name1234)
	TextView shop_name1234;

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	@App
	MyApp myApp;

	@Bean
	MenuComponent menuComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	FoodComponent foodComponent;

	private List<SelectFoodBean> select_dataList;
	private SelectListAdapter select_adapter;
	// 计算要支付总钱
	private double show_totalPrice = 0.00;
	// 输入的支付总钱
	private StringBuffer sbuff;
	private double show_gathering = 0.00;
	private double show_surplus = 0.00;
	private boolean is_takePackage;
	private boolean is_discount;
	private boolean is_foc;
	private double save_discount_price;
	public static boolean main_isRever;

	private double package_money;

	/* 主菜单activity */
	@AfterViews
	public void init() {
		// init_wifiReceiver();
		// m=new MyOrientationDetector2(MainActivity.this);
		select_dataList = new ArrayList<SelectFoodBean>();
		select_adapter = new SelectListAdapter(MainActivity.this, select_dataList);
		select_list.setAdapter(select_adapter);
		sbuff = new StringBuffer();
		save_discount_price = MyNumberUtils.strToNum(myApp.getDiscount());
		package_money = Constants.PACKAGE_COST;
		initView();
		initData();
		// init_wifiReceiver();
	}

	/* 初始化控件 */
	public void initView() {
		login_name.setText(getString(R.string.mainTitle_txt) + " " + myApp.getU_name() + ",");
		shop_name1234.setText(myApp.getShop_name() + "-" + myApp.getShop_code());
	}

	/* 初始化数据 */
	public void initData() {
		foodComponent.init(foodView, handler);
		foodComponent.getFoodDataList();
		init_giditNum_view();
		// onclick_foodView();
		// onclick_giditNum_view();
	}

	/**
	 * 初始化计算器
	 */
	public void init_giditNum_view() {
		List<GiditNumberBean> dataList = new ArrayList<GiditNumberBean>();
		String delete = String.valueOf(R.string.delete);
		String[] str = new String[] { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ".", "C" };
		for (int i = 0; i < str.length; i++) {
			GiditNumberBean bean = new GiditNumberBean();
			bean.setNumber(str[i]);
			dataList.add(bean);
		}
		GiditNumberAdapter adapter = new GiditNumberAdapter(this, dataList);
		giditNum_view.setAdapter(adapter);
	}

	/**
	 * 点击食物点餐面板
	 * 
	 * @param position
	 */
	@ItemClick(R.id.food_list)
	void foodPanel(int position) {
		List<FoodListBean> food_dataList = foodComponent.getFoodDataList();
		SelectFoodBean bean = new SelectFoodBean();
		bean.setFood_name(food_dataList.get(position).getTitle());
		bean.setFood_price(food_dataList.get(position).getPrice());
		bean.setFood_dayin_code(food_dataList.get(position).getDaping_id());
		bean.setFood_id(food_dataList.get(position).getFood_id());
		bean.setFood_type(food_dataList.get(position).getType());
		bean.setFood_num("1");

		bean.setFood_price(food_dataList.get(position).getPrice());
		show_totalPrice += Double.parseDouble(food_dataList.get(position).getPrice());
		select_dataList.add(bean);
		select_adapter.notifyDataSetChanged();
		total_price.setText(MyNumberUtils.numToStr(show_totalPrice));
		// 计算总金额
		add();

	}

	@ItemClick(R.id.digit_btn)
	void calulatorPanel(int position) {
		switch (position) {
		case 0:
			calulation(sbuff, "7", gathering);
			break;
		case 1:
			calulation(sbuff, "8", gathering);
			break;
		case 2:
			calulation(sbuff, "9", gathering);
			break;
		case 3:
			calulation(sbuff, "4", gathering);
			break;
		case 4:
			calulation(sbuff, "5", gathering);
			break;
		case 5:
			calulation(sbuff, "6", gathering);
			break;
		case 6:
			calulation(sbuff, "1", gathering);
			break;
		case 7:
			calulation(sbuff, "2", gathering);
			break;
		case 8:
			calulation(sbuff, "3", gathering);
			break;
		case 9:
			calulation(sbuff, "0", gathering);
			break;
		case 10:
			calulation(sbuff, ".", gathering);
			break;
		case 11:
			int sb_length = sbuff.length();
			sbuff.delete(0, sb_length);
			gathering.setText("0.00");
			surplus.setText("0.00");
			compute_surplus();
			break;
		}

	}

	private void calulation(StringBuffer sbuff, String appendStr, TextView gathering) {
		sbuff.append(appendStr);
		String number = sbuff.toString();
		if (NumberUtils.isNumber(number)) {
			if(StringUtils.indexOf(number, ".") > -1){
				sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
			}
			if (is_maxPrice(sbuff)) {
				gathering.setText(Constants.MAX_PRICE);
			} else {
				try {
					gathering.setText(Double.parseDouble(sbuff.toString().trim()) + "");
				} catch (Exception e) {
					toastComponent.show(stringResComponent.errPrice);
				}
			}
			compute_surplus();
		} else {
			toastComponent.show(stringResComponent.errPrice);
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FoodListAdapter.LESS_DATALIST:
				int num = (Integer) msg.obj;
				if (select_dataList.size() != 0) {
					Log.e("item", num + "");
					List<FoodListBean> food_dataList = foodComponent.getFoodDataList();
					FoodListBean bean = food_dataList.get(num);
					for (int i = select_dataList.size() - 1; i >= 0; i--) {
						SelectFoodBean remove_bean = select_dataList.get(i);
						if (StringUtils.equalsIgnoreCase(remove_bean.getFood_dayin_code(), bean.getDaping_id())) {
							select_dataList.remove(i);
							show_totalPrice -= Double.parseDouble(bean.getPrice());
							select_adapter.notifyDataSetChanged();
							add();
							break;
						}
					}
				}
				break;
			}

			super.handleMessage(msg);
		}

	};

	@Click(R.id.menu_btn)
	public void menu() {
		menuComponent.initPopupWindow();
	}

	/**
	 * 打包操作
	 */
	@Click(R.id.r_lay_id_take_package)
	public void takePackage() {
		if (is_foc) {
			take_package.setImageResource(R.drawable.package_not_select);
			// Toast.makeText(this, "取消了打包", Toast.LENGTH_SHORT).show();
			is_takePackage = false;
		} else {

			if (!is_takePackage) {
				if (select_dataList.size() == 0) {
					Toast.makeText(this, R.string.selec_not_food, Toast.LENGTH_SHORT).show();
				} else {
					take_package.setImageResource(R.drawable.package_seclect);
					// Toast.makeText(this, "全部打包",
					// Toast.LENGTH_SHORT).show();
					is_takePackage = true;
					add();
				}
			} else {

				take_package.setImageResource(R.drawable.package_not_select);
				// Toast.makeText(this, "取消了打包", Toast.LENGTH_SHORT).show();
				is_takePackage = false;
				add();

			}
		}
	}

	/**
	 * 免费等操作
	 */
	@Click(R.id.r_lay_id_foc)
	public void foc() {
		if (!is_foc) {
			if (select_dataList.size() == 0) {
				Toast.makeText(this, R.string.selec_not_food, Toast.LENGTH_SHORT).show();
			} else {
				foc.setImageResource(R.drawable.package_seclect);
				// Toast.makeText(this, "免单", Toast.LENGTH_SHORT).show();
				is_foc = true;
				is_discount = false;
				discount.setImageResource(R.drawable.package_not_select);
				take_package.setImageResource(R.drawable.package_not_select);
				is_takePackage = false;
				add();
			}
		} else {
			foc.setImageResource(R.drawable.package_not_select);
			// Toast.makeText(this, "不免单", Toast.LENGTH_SHORT).show();
			is_foc = false;
			add();

		}
	}

	/**
	 * 打折等操作
	 */
	@Click(R.id.r_lay_id_discount)
	public void discount() {
		if (is_foc) {
			take_package.setImageResource(R.drawable.package_not_select);
			// Toast.makeText(this, "取消了打包", Toast.LENGTH_SHORT).show();
			is_takePackage = false;
		} else {

			if (!is_discount) {
				if (select_dataList.size() == 0) {
					Toast.makeText(this, R.string.selec_not_food, Toast.LENGTH_SHORT).show();
				} else {
					discount.setImageResource(R.drawable.package_seclect);
					// Toast.makeText(this, "打折",
					// Toast.LENGTH_SHORT).show();
					is_discount = true;
					add();
				}
			} else {
				discount.setImageResource(R.drawable.package_not_select);
				// Toast.makeText(this, "不打折", Toast.LENGTH_SHORT).show();
				is_discount = false;
				add();

			}
		}
	}

	/**
	 * 确定打印
	 */
	@Click(R.id.ok_btn)
	public void ok() {
		try {
			Log.e("输入的金额", sbuff.toString().trim());
			if (sbuff == null || sbuff.toString().trim().equals("")) {
				sbuff.append("0");
			}
			show_gathering = Double.parseDouble(sbuff.toString().trim());
			if (is_maxPrice(sbuff)) {
				gathering.setText("9999.99");
			} else {
				try {
					gathering.setText(MyNumberUtils.numToStr(show_gathering));
				} catch (Exception e) {
					Toast.makeText(MainActivity.this, R.string.err_price, Toast.LENGTH_SHORT).show();
				}
			}
			double result = show_totalPrice;
			Log.e("最后金额", show_totalPrice + "");
			if (show_gathering > Constants.MAX_NUM_PRICE) {
				show_gathering = Constants.MAX_NUM_PRICE;
			}
			show_surplus = show_gathering - result;
			surplus.setText(MyNumberUtils.numToStr(show_surplus));

		} catch (Exception e) {
			Toast.makeText(this, R.string.err_price, Toast.LENGTH_SHORT).show();
		}
		if (select_dataList != null && select_dataList.size() != 0) {
			Show_print().create().show();
		}

	}

	// TODO: 暂时不用
	// CreatedDialog().create().show();
	//

	public DialogBuilder Show_print() {
		DialogBuilder builder = new DialogBuilder(this);
		// builder.setTitle(R.string.message_title);
		builder.setMessage(stringResComponent.openPrint);
		builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 先打印数据，不耽误正常使用----------------------------
				StringBuffer sb = new StringBuffer();
				String time = DateUtils.dateToStr(new Date(), DateUtils.DD_MM_YYYY_HH_MM);
				sb.append(time + "\n\n");
				for (int i = 0; i < select_dataList.size(); i++) {
					SelectFoodBean bean = select_dataList.get(i);
					String foodName = bean.getFood_dayin_code() + " / " + bean.getFood_name();
					String qty = "X" + bean.getFood_num() + "\n\n";
					if (is_takePackage) {
						foodName += "(包)";
					}
					sb.append(foodName + "     " + qty);
				}
				myApp.getPrinter().setIp(myApp.getIp_str());
				myApp.getPrinter().print(sb.toString());
				// /--------------------------
				String date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
				long result_price = PriceSave.getInatance(MainActivity.this).save(myApp.getUser_id(), date,
						total_price.getText().toString(), myApp.getSettingShopId());
				if (result_price == -1) {
					Log.e("保存价格失败", "");
				} else {
					Log.e("保存价格成功", "");
				}
				for (int i = 0; i < select_dataList.size(); i++) {
					SelectFoodBean bean = select_dataList.get(i);
					FoodOrder.save(bean, myApp, is_foc);
				}
				// 准备发送数据
				Map<String, String> params = new HashMap<String, String>();
				List<FoodOrder> datas = FoodOrder.queryListByStatus(Constants.DB_FAILED);
				System.out.println("-->" + datas.size());
				for (int i = 0; i < datas.size(); i++) {
					FoodOrder foodOrder = datas.get(i);
					System.out.println("f_order.getFood_flag()-->" + foodOrder.status);
					params.put("transactions[" + i + "].androidId", String.valueOf(foodOrder.getId()));
					System.out.println("transactions[" + i + "].androidId-->" + foodOrder.getId());
					params.put("transactions[" + i + "].user.id", foodOrder.userId);
					System.out.println("transactions[" + i + "].user.id-->" + foodOrder.userId);
					params.put("transactions[" + i + "].shop.id", foodOrder.shopId);
					System.out.println("transactions[" + i + "].shop.id-->" + foodOrder.shopId);
					params.put("transactions[" + i + "].quantity", foodOrder.quantity);
					System.out.println("transactions[" + i + "].quantity-->" + foodOrder.quantity);
					params.put("transactions[" + i + "].food.id", foodOrder.foodId);
					System.out.println("transactions[" + i + "].food.id-->" + foodOrder.foodId);
					params.put("transactions[" + i + "].totalDiscount", foodOrder.discount);
					System.out.println("transactions[" + i + "].totalDiscount-->" + foodOrder.discount);
					params.put("transactions[" + i + "].totalRetailPrice", foodOrder.retailPrice);
					System.out.println("transactions[" + i + "].totalRetailPrice-->" + foodOrder.retailPrice);
					params.put("transactions[" + i + "].totalPackage", foodOrder.totalPackage);
					System.out.println("transactions[" + i + "].totalPackage-->" + foodOrder.totalPackage);
					params.put("transactions[" + i + "].freeOfCharge", foodOrder.foc);
					System.out.println("transactions[" + i + "].freeOfCharge-->" + foodOrder.foc);
					params.put("transactions[" + i + "].orderDate", foodOrder.date);
					System.out.println("transactions[" + i + "].orderDate-->" + foodOrder.date);
				}

				// 异步请求数据
				RemoteDataHandler.asyncPost(Constants.URL_FOOD_ORDER, params, new Callback() {
					@Override
					public void dataLoaded(ResponseData data) {
						if (data.getCode() == 1) {
							FoodOrder.updateAllByStatus();
						} else if (data.getCode() == 0) {
							String json = data.getJson();
							System.out.println("-----111>>>>>>" + json);
							json = json.replaceAll("\\[", "");
							json = json.replaceAll("\\]", "");
							System.out.println("-----222>>>>>>" + json);
							String[] str = json.split(",");
							for (int i = 0; i < str.length; i++) {
								System.out.println("-----333>>>>>>" + str[i]);
								FoodOrder.updateByStatus(Long.parseLong(str[i]));
							}
						} else if (data.getCode() == -1) {

						}
					}
				});

				// 清空数据
				clear_data();
			}
		});
		builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 不做任何操作
			}
		});
		return builder;
	}

	public void clear_data() {
		if (select_dataList.size() != 0) {
			select_dataList.clear();
			select_adapter.notifyDataSetChanged();
		}
		total_price.setText("0.00");
		gathering.setText("0.00");
		surplus.setText("0.00");
		show_totalPrice = 0.00;
		show_gathering = 0.00;
		show_surplus = 0.00;
		int sb_length = sbuff.length();
		sbuff.delete(0, sb_length);
		is_takePackage = false;
		take_package.setImageResource(R.drawable.package_not_select);
		is_discount = false;
		discount.setImageResource(R.drawable.package_not_select);
		is_foc = false;
		foc.setImageResource(R.drawable.package_not_select);
	}

	/**
	 * 计算总金额
	 */
	public void add() {
		show_totalPrice = 0;
		if (select_dataList.size() == 0) {
			total_price.setText(MyNumberUtils.numToStr(show_totalPrice));
		} else {
			for (int i = 0; i < select_dataList.size(); i++) {
				int num = Integer.parseInt(select_dataList.get(i).getFood_num());
				Double price = Double.parseDouble(select_dataList.get(i).getFood_price());
				select_dataList.get(i).setDabao_price(0);
				select_dataList.get(i).setDazhe_price(0);
				show_totalPrice += price;
				if (is_foc) {
					show_totalPrice = 0;
					select_dataList.get(i).setDabao_price(0);
					select_dataList.get(i).setDazhe_price(0);
				} else {
					double dabao = num * package_money;
					double dazhe = num * save_discount_price;
					String type = select_dataList.get(i).getFood_type();
					if (StringUtils.equalsIgnoreCase(type, Constants.FOOD_DISH)) {
						if (!is_discount && is_takePackage) {
							show_totalPrice = show_totalPrice + dabao;
							dazhe = 0;
						} else if (is_discount && !is_takePackage) {
							show_totalPrice = show_totalPrice - dazhe;
							dabao = 0;
						} else if (!is_discount && !is_takePackage) {
							dabao = 0;
							dazhe = 0;
						} else if (is_discount && is_takePackage) {
							show_totalPrice = show_totalPrice + dabao - dazhe;
						}
						select_dataList.get(i).setDabao_price(dabao);
						select_dataList.get(i).setDazhe_price(dazhe);
					}
				}
			}
			total_price.setText(MyNumberUtils.numToStr(show_totalPrice));
			if (Double.parseDouble(gathering.getText().toString()) > 0) {
				compute_surplus();
			}
		}
	}

	/**
	 * 计算应该找回的款项
	 */
	public void compute_surplus() {
		try {
			Double get_gathering = Double.parseDouble(gathering.getText().toString());
			Double get_total_price = Double.parseDouble(total_price.getText().toString());
			surplus.setText(MyNumberUtils.numToStr(get_gathering - get_total_price));
		} catch (Exception e) {
			toastComponent.show(stringResComponent.errPrice);
		}

	}

	/**
	 * 判断输入值是否超过最大值
	 * 
	 * @param sbuff
	 * @return
	 */
	public boolean is_maxPrice(StringBuffer sbuff) {
		try {
			Double now_price = Double.parseDouble(sbuff.toString().trim());
			if (now_price > Constants.MAX_NUM_PRICE) {
				return true;
			}
		} catch (Exception e) {
			toastComponent.show(stringResComponent.errPrice);
			return false;
		}
		return false;

	}

}
