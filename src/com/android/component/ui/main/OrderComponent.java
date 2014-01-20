package com.android.component.ui.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.R;
import com.android.adapter.GiditNumberAdapter;
import com.android.adapter.SelectListAdapter;
import com.android.bean.SelectFoodBean;
import com.android.common.AndroidPrinter;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;
import com.android.common.MyNumberUtils;
import com.android.common.MyTextUtils;
import com.android.component.LockComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.component.WifiComponent;
import com.android.dialog.ConfirmDialog;
import com.android.dialog.MyDialog;
import com.android.domain.Food;
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
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

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

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	@Bean
	WifiComponent wifiComponent;

	FoodComponent foodComponent;

	CalculatorComponent calculatorComponent;

	private List<SelectFoodBean> selectDataList;

	private SelectListAdapter selectAdapter;

	private StringBuffer sb;
	// 输入的支付总钱
	private StringBuffer sbuff;
	// 显示要支付的总价钱
	private boolean is_takePackage;

	private boolean is_discount;
	private boolean is_foc;
	private double save_discount_price;
	private double package_money;

	// 打印框
	Dialog dialg;

	private MyDialog mydialog;

	/**
	 * 初始化订单组件
	 * 
	 * @param calucatorView
	 * @param handler
	 */
	@AfterViews
	public void initOrder() {
		// 初始化打印对话框
		dialg = buildPrintDialog();
		mydialog = new MyDialog(context);

		// 初始化订单面板
		this.selectDataList = new ArrayList<SelectFoodBean>();
		this.selectAdapter = new SelectListAdapter(context, selectDataList);
		selectAdapter.setComponent(OrderComponent.this);
		this.selectList.setAdapter(selectAdapter);
		this.selectList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int count = event.getPointerCount();
				if (count >= 2) {
					selectDataList.clear();
					selectAdapter.notifyDataSetChanged();
					doCalculation();
					return true;
				}
				return false;
			}

		});
		// 初始化订单价钱
		sbuff = new StringBuffer();
		save_discount_price = MyNumberUtils.strToNum(sharedPrefs.discount().get());
		package_money = MyNumberUtils.strToNum(sharedPrefs.packageCost().get());

		// 计算器
		List<String> dataList = new ArrayList<String>();
		String[] str = new String[] { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ".", "C" };
		for (int i = 0; i < str.length; i++) {
			dataList.add(str[i]);
		}
		GiditNumberAdapter adapter = new GiditNumberAdapter(context, dataList);
		calucatorView.setAdapter(adapter);

	}

	//
	// @Touch(R.id.select_list)
	// public void removeByTouch(MotionEvent event) {
	// int count = event.getPointerCount();
	// if (count >= 2) {
	// selectDataList.clear();
	// selectAdapter.notifyDataSetChanged();
	// doCalculation();
	// }
	// }

	/**
	 * 添加订单
	 * 
	 * @param foodBean
	 */
	public void order(Food foodBean) {
		SelectFoodBean bean = new SelectFoodBean();
		bean.setFood_name(foodBean.title);
		bean.setFood_price(foodBean.retailPrice);
		bean.setFood_dayin_code(foodBean.sn);
		bean.setFood_id(foodBean.foodId);
		bean.setFood_type(foodBean.type);
		bean.setFood_num("1");
		selectDataList.add(bean);
		selectAdapter.notifyDataSetChanged();
		doCalculation();
	}

	/**
	 * 移除订单
	 * 
	 * @param index
	 */
	public void remove(int index) {
		if (CollectionUtils.isNotEmpty(selectDataList)) {
			Food bean = foodComponent.getFoodDataList().get(index);
			for (int i = selectDataList.size() - 1; i >= 0; i--) {
				SelectFoodBean remove_bean = selectDataList.get(i);
				if (StringUtils.equalsIgnoreCase(remove_bean.getFood_dayin_code(), bean.sn)) {
					selectDataList.remove(i);
					selectAdapter.notifyDataSetChanged();
					doCalculation();
					break;
				}
			}
		}
	}

	public void remove2(int index) {
		if (CollectionUtils.isNotEmpty(selectDataList)) {
			if (selectDataList.size() != index + 1) {
				if (index != 0) {
					if (selectDataList.get(index - 1).getFood_name().equalsIgnoreCase(Constants.SPLIT_LINE)
							&& selectDataList.get(index + 1).getFood_name().equalsIgnoreCase(Constants.SPLIT_LINE)) {
						selectDataList.remove(index);
						selectDataList.remove(index);
					} else {
						selectDataList.remove(index);
					}
				} else {
					if (selectDataList.get(index + 1).getFood_name().equalsIgnoreCase(Constants.SPLIT_LINE)) {
						selectDataList.remove(index);
						selectDataList.remove(index);
					} else {
						selectDataList.remove(index);
					}
				}

			} else {
				selectDataList.remove(index);
			}

			selectAdapter.notifyDataSetChanged();
			doCalculation();
		}
	}

	/**
	 * 打包操作
	 */
	@Click(R.id.r_lay_id_take_package)
	public void takePackage() {
		if (CollectionUtils.isNotEmpty(selectDataList)) {
			setImageStatus(take_package, foc);
			doCalculation();
		}
	}

	/**
	 * 打折等操作
	 */
	@Click(R.id.r_lay_id_discount)
	public void discount() {
		if (CollectionUtils.isNotEmpty(selectDataList)) {
			setImageStatus(discount, foc);
			doCalculation();
		}
	}

	/**
	 * 免费等操作
	 */
	@Click(R.id.r_lay_id_foc)
	public void foc() {
		if (CollectionUtils.isNotEmpty(selectDataList)) {
			setImageStatus(foc, discount, take_package);
			doCalculation();
		}
	}

	/**
	 * 
	 * @param currentView
	 *            当前选中的菜单
	 * @param imageViews
	 *            要屏蔽掉的菜单
	 */
	private void setImageStatus(ImageView currentView, ImageView... imageViews) {
		for (ImageView imageView : imageViews) {
			imageView.setImageResource(R.drawable.package_not_select);
		}

		if (currentView == discount) {
			if (is_discount) {
				currentView.setImageResource(R.drawable.package_not_select);
				is_discount = false;
			} else {
				currentView.setImageResource(R.drawable.package_seclect);
				is_discount = true;
				is_foc = false;
			}

		} else if (currentView == take_package) {
			if (is_takePackage) {
				currentView.setImageResource(R.drawable.package_not_select);
				is_takePackage = false;
			} else {
				currentView.setImageResource(R.drawable.package_seclect);
				is_takePackage = true;
				is_foc = false;
			}

		} else {

			if (is_foc) {
				currentView.setImageResource(R.drawable.package_not_select);
				is_foc = false;
			} else {
				currentView.setImageResource(R.drawable.package_seclect);
				is_discount = false;
				is_takePackage = false;
				is_foc = true;
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
		if (CollectionUtils.isNotEmpty(selectDataList)) {
			// 先打印数据，不耽误正常使用----------------------------
			sb = new StringBuffer();
			String time = DateUtils.dateToStr(new Date(), DateUtils.DD_MM_YYYY_HH_MM);
			sb.append(time + "\n\n");
			for (int i = 0; i < selectDataList.size(); i++) {
				SelectFoodBean bean = selectDataList.get(i);
				if (StringUtils.isEmpty(bean.getFood_id())) {
					sb.append(bean.getFood_name() + "\n\n");
				} else {
					String foodName = bean.getFood_dayin_code() + " / " + bean.getFood_name();
					foodName = MyTextUtils.stringFormat(foodName);
					String qty = "X" + bean.getFood_num() + "\n\n";
					if (is_takePackage) {
						foodName += stringResComponent.foodPackage;
					}
					sb.append(foodName + qty);
				}
			}
			Log.i("[OrderComponent] -> [Result]", sb.toString());
			// connect to printer
			// androidPrinter.connect();
			// show dialog
			mydialog.show();
			mydialog.dialog_message.setText(stringResComponent.openPrint);
			mydialog.linearlayoutID.setVisibility(View.VISIBLE);
			mydialog.dialog_message.setVisibility(View.GONE);
			mydialog.textDialogAllMoenyID.setTextSize(60);
			mydialog.textDialogSearchMoenyID.setTextSize(60);
			mydialog.textDialogAllMoenyTitleID.setTextSize(60);
			mydialog.textDialogSearchMoenyTitleID.setTextSize(60);
			mydialog.textDialogAllMoenyTitleID.setText(stringResComponent.totalPrie);
			mydialog.textDialogSearchMoenyTitleID.setText(stringResComponent.Surplus);
			mydialog.textDialogAllMoenyID.setText("S$" + totalPrice.getText().toString());
			mydialog.textDialogSearchMoenyID.setText("S$" + surplus.getText().toString());
			mydialog.dialog_yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mydialog.dismiss();
					androidPrinter.print(sb.toString());
					// 保存数据------------------------------
					storeOrders();
					// 同步开始------------------------------
					syncToServer();
					// 清空数据------------------------------
					clean();
				}
			});
			mydialog.dialog_no.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mydialog.dismiss();
				}
			});
		}

	}

	/**
	 * 计算总金额
	 */
	public void doCalculation() {
		double showTotalPrice = 0;
		if (is_foc) {
			// 免费的话，全部清空
			showTotalPrice = 0;
			totalPrice.setText(MyNumberUtils.numToStr(showTotalPrice));
			for (SelectFoodBean bean : selectDataList) {
				bean.setDabao_price(0);
				bean.setDazhe_price(0);
			}
			totalPrice.setText(MyNumberUtils.numToStr(showTotalPrice));
			calculatorComponent.compute_surplus();
		} else {
			for (SelectFoodBean bean : selectDataList) {
				// 计算总价
				showTotalPrice += MyNumberUtils.strToNum(bean.getFood_price());

				// 计算打包打折
				int num = Integer.parseInt(bean.getFood_num());
				double dabao = num * package_money;
				double dazhe = num * save_discount_price;
				String type = bean.getFood_type();
				if (StringUtils.equalsIgnoreCase(type, Constants.FOOD_DISH)) {
					if (!is_discount && is_takePackage) {
						showTotalPrice += dabao;
						dazhe = 0;
					} else if (is_discount && !is_takePackage) {
						showTotalPrice -= dazhe;
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
		totalPrice.setText(Constants.DEFAULT_PRICE_FLOAT);
		gathering.setText(Constants.DEFAULT_PRICE_FLOAT);
		surplus.setText(Constants.DEFAULT_PRICE_FLOAT);
		sbuff.delete(0, sbuff.length());
		is_takePackage = false;
		take_package.setImageResource(R.drawable.package_not_select);
		is_discount = false;
		discount.setImageResource(R.drawable.package_not_select);
		is_foc = false;
		foc.setImageResource(R.drawable.package_not_select);
	}

	// 保存数据
	void storeOrders() {
		for (int i = 0; i < selectDataList.size(); i++) {
			SelectFoodBean bean = selectDataList.get(i);
			FoodOrder.save(bean, myApp, is_foc);
		}
	}

	// 同步到服务器上去
	@Background
	void syncToServer() {
		if (wifiComponent.isConnected()) {
			LockComponent.LOCKER.lock();
			try {
				submitAll();
			} finally {
				LockComponent.LOCKER.unlock();
			}
		}
	}

	// 准备发送数据
	public void submitAll() {
		Map<String, String> params = new HashMap<String, String>();
		List<FoodOrder> datas = FoodOrder.queryListByStatus(Constants.DB_FAILED);
		for (int i = 0; i < datas.size(); i++) {
			FoodOrder foodOrder = datas.get(i);
			params.put("transactions[" + i + "].androidId", String.valueOf(foodOrder.getId()));
			params.put("transactions[" + i + "].user.id", foodOrder.userId);
			params.put("transactions[" + i + "].shop.id", foodOrder.shopId);
			params.put("transactions[" + i + "].quantity", foodOrder.quantity);
			params.put("transactions[" + i + "].food.id", foodOrder.foodId);
			params.put("transactions[" + i + "].totalDiscount", foodOrder.discount);
			params.put("transactions[" + i + "].totalRetailPrice", String.valueOf(foodOrder.retailPrice));
			params.put("transactions[" + i + "].totalPackage", foodOrder.totalPackage);
			params.put("transactions[" + i + "].freeOfCharge", foodOrder.foc);
			params.put("transactions[" + i + "].orderDate", foodOrder.date);
		}

		// 异步请求数据
		StatusMapping mapping = StatusMapping.postJSON(Constants.URL_FOOD_ORDER, params);
		if (mapping.code == Constants.STATUS_SUCCESS) {
			FoodOrder.updateAllByStatus();
		} else if (mapping.code == Constants.STATUS_FAILED) {
			List<Long> ids = mapping.datas;
			if (CollectionUtils.isNotEmpty(ids)) {
				for (Long id : ids) {
					FoodOrder.updateByStatus(id);
				}
			}
		}
	}

	public Dialog buildPrintDialog() {
		return new ConfirmDialog(context, StringUtils.EMPTY, stringResComponent.openPrint) {

			@Override
			public void doClick() {
				androidPrinter.print(sb.toString());
				// 保存数据------------------------------
				storeOrders();
				// 同步开始------------------------------
				syncToServer();
				// 清空数据------------------------------
				clean();
			}

		}.build();
	}

	public List<SelectFoodBean> getSelectDataList() {
		return selectDataList;
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
