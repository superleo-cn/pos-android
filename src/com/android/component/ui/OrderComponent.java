package com.android.component.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.R;
import com.android.adapter.GiditNumberAdapter;
import com.android.adapter.SelectListAdapter;
import com.android.bean.FoodListBean;
import com.android.bean.SelectFoodBean;
import com.android.common.AndroidPrinter;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;
import com.android.common.MyNumberUtils;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.dialog.DialogBuilder;
import com.android.domain.FoodOrder;
import com.android.mapping.StatusMapping;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * 订单组件
 * 
 * @author superleo
 * 
 */
@EBean
public class OrderComponent {

	@RootContext
	Context context;

	@App
	MyApp myApp; // 注入 MyApp

	@ViewById(R.id.total_price)
	TextView totalPrice; // 总价格

	@ViewById(R.id.gathering)
	TextView gathering; // 收款

	@ViewById(R.id.surplus)
	TextView surplus; // 找回

	@ViewById(R.id.take_package)
	ImageView take_package; // 打包选项

	@ViewById(R.id.foc)
	ImageView foc; // FOC

	@ViewById(R.id.discount)
	ImageView discount; // 打折选项

	@ViewById(R.id.select_list)
	ListView selectList; // 左上角面板

	@ViewById(R.id.digit_btn)
	GridView calucatorView; // 0-9按钮 用gridView做的按钮

	@Bean
	AndroidPrinter androidPrinter;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	FoodComponent foodComponent;

	CalculatorComponent calculatorComponent;

	private List<SelectFoodBean> selectDataList;

	private SelectListAdapter selectAdapter;

	// 输入的支付总钱
	private StringBuffer sbuff;
	// 显示要支付的总价钱
	private double showTotalPrice = 0.00;
	private boolean is_takePackage;

	private boolean is_discount;
	private boolean is_foc;
	private double save_discount_price;

	private double package_money;

	/**
	 * 初始化订单组件
	 * 
	 * @param calucatorView
	 * @param handler
	 */
	@AfterViews
	public void initOrder() {
		// 初始化订单面板
		this.selectDataList = new ArrayList<SelectFoodBean>();
		this.selectAdapter = new SelectListAdapter(context, selectDataList);
		this.selectList.setAdapter(selectAdapter);
		// 初始化订单价钱
		sbuff = new StringBuffer();
		save_discount_price = MyNumberUtils.strToNum(myApp.getDiscount());
		package_money = MyNumberUtils.strToNum(myApp.getPackageCost());

		// 计算器
		List<String> dataList = new ArrayList<String>();
		String delete = String.valueOf(R.string.delete);
		String[] str = new String[] { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ".", "C" };
		for (int i = 0; i < str.length; i++) {
			dataList.add(str[i]);
		}
		GiditNumberAdapter adapter = new GiditNumberAdapter(context, dataList);
		calucatorView.setAdapter(adapter);
	}

	/**
	 * 添加订单
	 * 
	 * @param foodBean
	 */
	public void order(FoodListBean foodBean) {
		SelectFoodBean bean = new SelectFoodBean();
		bean.setFood_name(foodBean.getTitle());
		bean.setFood_price(foodBean.getPrice());
		bean.setFood_dayin_code(foodBean.getDaping_id());
		bean.setFood_id(foodBean.getFood_id());
		bean.setFood_type(foodBean.getType());
		bean.setFood_num("1");
		bean.setFood_price(foodBean.getPrice());
		selectDataList.add(bean);
		selectAdapter.notifyDataSetChanged();
		showTotalPrice += Double.parseDouble(foodBean.getPrice());
		add();
		totalPrice.setText(MyNumberUtils.numToStr(showTotalPrice));
	}

	/**
	 * 移除订单
	 * 
	 * @param index
	 */
	public void remove(int index) {
		if (CollectionUtils.isNotEmpty(selectDataList)) {
			Log.e("item", index + "");
			List<FoodListBean> food_dataList = foodComponent.getFoodDataList();
			FoodListBean bean = food_dataList.get(index);
			for (int i = selectDataList.size() - 1; i >= 0; i--) {
				SelectFoodBean remove_bean = selectDataList.get(i);
				if (StringUtils.equalsIgnoreCase(remove_bean.getFood_dayin_code(), bean.getDaping_id())) {
					selectDataList.remove(i);
					showTotalPrice -= Double.parseDouble(bean.getPrice());
					selectAdapter.notifyDataSetChanged();
					add();
					break;
				}
			}
		}
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
				List<SelectFoodBean> select_dataList = selectDataList;
				if (CollectionUtils.isEmpty(select_dataList)) {
					toastComponent.show(stringResComponent.noFood);
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
			List<SelectFoodBean> select_dataList = selectDataList;
			if (CollectionUtils.isEmpty(select_dataList)) {
				toastComponent.show(stringResComponent.noFood);
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
				List<SelectFoodBean> select_dataList = selectDataList;
				if (CollectionUtils.isEmpty(select_dataList)) {
					toastComponent.show(stringResComponent.noFood);
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
	 * 计算器操作
	 * 
	 * @param position
	 */
	@ItemClick(R.id.digit_btn)
	void calulatorPanel(int position) {
		calculatorComponent.calculation(sbuff, position);
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
			Double show_gathering = Double.parseDouble(sbuff.toString().trim());
			if (calculatorComponent.is_maxPrice(sbuff)) {
				gathering.setText(Constants.MAX_PRICE);
			} else {
				try {
					gathering.setText(MyNumberUtils.numToStr(show_gathering));
				} catch (Exception e) {
					toastComponent.show(stringResComponent.errPrice);
				}
			}
			double result = showTotalPrice;
			Log.e("最后金额", result + "");
			if (show_gathering > Constants.MAX_NUM_PRICE) {
				show_gathering = Constants.MAX_NUM_PRICE;
			}
			Double show_surplus = show_gathering - result;
			surplus.setText(MyNumberUtils.numToStr(show_surplus));

		} catch (Exception e) {
			toastComponent.show(stringResComponent.errPrice);
		}
		if (CollectionUtils.isNotEmpty(selectDataList)) {
			printDialog().create().show();
		}

	}

	/**
	 * 计算总金额
	 */
	public void add() {
		showTotalPrice = 0;
		if (CollectionUtils.isEmpty(selectDataList)) {
			totalPrice.setText(MyNumberUtils.numToStr(showTotalPrice));
		} else {
			for (int i = 0; i < selectDataList.size(); i++) {
				SelectFoodBean bean = selectDataList.get(i);
				int num = Integer.parseInt(bean.getFood_num());
				Double price = Double.parseDouble(bean.getFood_price());
				bean.setDabao_price(0);
				bean.setDazhe_price(0);
				showTotalPrice += price;
				if (is_foc) {
					showTotalPrice = 0;
					bean.setDabao_price(0);
					bean.setDazhe_price(0);
				} else {
					double dabao = num * package_money;
					double dazhe = num * save_discount_price;
					String type = bean.getFood_type();
					if (StringUtils.equalsIgnoreCase(type, Constants.FOOD_DISH)) {
						if (!is_discount && is_takePackage) {
							showTotalPrice = showTotalPrice + dabao;
							dazhe = 0;
						} else if (is_discount && !is_takePackage) {
							showTotalPrice = showTotalPrice - dazhe;
							dabao = 0;
						} else if (!is_discount && !is_takePackage) {
							dabao = 0;
							dazhe = 0;
						} else if (is_discount && is_takePackage) {
							showTotalPrice = showTotalPrice + dabao - dazhe;
						}
						bean.setDabao_price(dabao);
						bean.setDazhe_price(dazhe);
					}
				}
			}
			totalPrice.setText(MyNumberUtils.numToStr(showTotalPrice));
			if (Double.parseDouble(gathering.getText().toString()) > 0) {
				calculatorComponent.compute_surplus();
			}
		}
	}

	/**
	 * 清空数据
	 */
	public void clean() {
		if (CollectionUtils.isNotEmpty(selectDataList)) {
			selectDataList.clear();
			selectAdapter.notifyDataSetChanged();
		}
		totalPrice.setText("0.00");
		gathering.setText("0.00");
		surplus.setText("0.00");
		showTotalPrice = 0.00;
		int sb_length = sbuff.length();
		sbuff.delete(0, sb_length);
		is_takePackage = false;
		take_package.setImageResource(R.drawable.package_not_select);
		is_discount = false;
		discount.setImageResource(R.drawable.package_not_select);
		is_foc = false;
		foc.setImageResource(R.drawable.package_not_select);

	}

	// 同步到服务器上去
	@Background
	void syncToServer() {
		String date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
		long result_price = 0;
		// long result_price =
		// PriceSave.getInatance(MainActivity.this).save(myApp.getUser_id(),
		// date,
		// total_price.getText().toString(), myApp.getSettingShopId());
		if (result_price == -1) {
			Log.e("保存价格失败", "");
		} else {
			Log.e("保存价格成功", "");
		}
		for (int i = 0; i < selectDataList.size(); i++) {
			SelectFoodBean bean = selectDataList.get(i);
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
		StatusMapping mapping = StatusMapping.postJSON(Constants.URL_FOOD_ORDER, params);
		if (mapping.code == 1) {
			FoodOrder.updateAllByStatus();
		} else if (mapping.code == 0) {
			List<Long> ids = mapping.datas;
			if (CollectionUtils.isNotEmpty(ids)) {
				for (Long id : ids) {
					FoodOrder.updateByStatus(id);
				}
			}
		} else if (mapping.code == -1) {

		}

	}

	public DialogBuilder printDialog() {
		DialogBuilder builder = new DialogBuilder(context);
		// builder.setTitle(R.string.message_title);
		builder.setMessage(stringResComponent.openPrint);
		builder.setPositiveButton(R.string.message_ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 先打印数据，不耽误正常使用----------------------------
				StringBuffer sb = new StringBuffer();
				String time = DateUtils.dateToStr(new Date(), DateUtils.DD_MM_YYYY_HH_MM);
				sb.append(time + "\n\n");
				for (int i = 0; i < selectDataList.size(); i++) {
					SelectFoodBean bean = selectDataList.get(i);
					String foodName = bean.getFood_dayin_code() + " / " + bean.getFood_name();
					String qty = "X" + bean.getFood_num() + "\n\n";
					if (is_takePackage) {
						foodName += "(包)";
					}
					sb.append(foodName + "     " + qty);
				}
				androidPrinter.setIp(myApp.getIp_str());
				androidPrinter.print(sb.toString());
				// 同步开始------------------------------
				syncToServer();
				// 清空数据------------------------------
				clean();
			}
		});
		builder.setNegativeButton(R.string.message_cancle, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 不做任何操作
			}
		});
		return builder;
	}

	public List<SelectFoodBean> getSelectDataList() {
		return selectDataList;
	}

	public double getShowTotalPrice() {
		return showTotalPrice;
	}

	public TextView getGathering() {
		return gathering;
	}

	public TextView getSurplus() {
		return surplus;
	}

	public boolean isIs_takePackage() {
		return is_takePackage;
	}

	public boolean isIs_foc() {
		return is_foc;
	}

	public StringBuffer getSbuff() {
		return sbuff;
	}

	public TextView getTotalPrice() {
		return totalPrice;
	}

	public void setFoodComponent(FoodComponent foodComponent) {
		this.foodComponent = foodComponent;
	}

	public void setCalculatorComponent(CalculatorComponent calculatorComponent) {
		this.calculatorComponent = calculatorComponent;
	}
}
