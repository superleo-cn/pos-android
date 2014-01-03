package com.android.component.ui.query;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.R;
import com.android.bean.Pagination;
import com.android.bean.SearchCriteria;
import com.android.common.Constants;
import com.android.component.ToastComponent;
import com.android.component.ui.MenuComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

@EBean
public class QueryAllDBAComponent {
	// 注入 Context 变量
	@RootContext
	Context context;

	@Bean
	ToastComponent toastComponent;

	@ViewById(R.id.textStatusID)
	TextView textStatusID;

	@ViewById(R.id.startTimeID)
	TextView startTimeID;

	@ViewById(R.id.endTimeID)
	TextView endTimeID;

	@Bean
	public MenuComponent menuComponent;

	@Bean
	FoodOrderQueryComponent foodOrderQueryComponent;

	@Bean
	ExpensesOrderQueryComponent expensesOrderQueryComponent;

	@Bean
	BalanceOrderQueryComponent balanceOrderQueryComponent;

	@Bean
	CollectionOrderQueryComponent collectionOrderQueryComponent;

	Calendar calendar = Calendar.getInstance();

	AbstractQueryComponent queryComponent;

	Pagination pagination;

	SearchCriteria searchCriteria;

	// 第一次进来默认查询食物订单
	@AfterViews
	void init() {
		pagination = new Pagination();
		searchCriteria = new SearchCriteria();
		queryComponent = foodOrderQueryComponent;
		queryComponent.queryResult(pagination, searchCriteria);
	}

	private void showPopWindow(Context context) {
		final PopupWindow pw;

		View myView;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		myView = inflater.inflate(R.layout.status_pop_window, null);
		final TextView textStatusAllID = (TextView) myView.findViewById(R.id.textStatusAllID);
		final TextView textStatusSuccessID = (TextView) myView.findViewById(R.id.textStatusSuccessID);
		final TextView textStatusFailID = (TextView) myView.findViewById(R.id.textStatusFailID);
		pw = new PopupWindow(myView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.showAtLocation(myView, textStatusID.getHeight(), 10, textStatusID.getHeight() + 20);
		pw.showAsDropDown(textStatusID);
		textStatusAllID.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
				textStatusID.setText("全部");
			}
		});
		textStatusSuccessID.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
				textStatusID.setText("成功");
			}
		});
		textStatusFailID.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
				textStatusID.setText("失败");
			}
		});
	}

	@Click(R.id.buttonSearchID)
	void buttonStatusIDOnClick() {
		String statusContext = textStatusID.getText().toString();
		String startTimeContext = startTimeID.getText().toString();
		String endTimeContext = endTimeID.getText().toString();
		String status = StringUtils.EMPTY;
		String dateFrom = startTimeContext;
		String dateTo = endTimeContext;
		// 设置状态位
		if (StringUtils.equals("成功", statusContext)) {
			status = Constants.DB_SUCCESS;
		} else if (StringUtils.equals("失败", statusContext)) {
			status = Constants.DB_FAILED;
		}
		// 设置查询时间
		if (StringUtils.equals("开始时间", startTimeContext)) {
			dateFrom = StringUtils.EMPTY;
		}
		if (StringUtils.equals("结束时间", endTimeContext)) {
			dateTo = StringUtils.EMPTY;
		}
		// 开始查询
		pagination = new Pagination();
		searchCriteria = new SearchCriteria();
		searchCriteria.map.put("dateFrom", dateFrom);
		searchCriteria.map.put("dateTo", dateTo);
		searchCriteria.map.put("status", status);
		queryComponent.queryResult(pagination, searchCriteria);
	}

	// 查询条件
	@Click(R.id.textStatusID)
	void textStatusOnClick() {
		showPopWindow(context);
	}

	@Click(R.id.startTimeID)
	void startTimeIDOnClick() {
		showDatePicker(startTimeID);
	}

	@Click(R.id.endTimeID)
	void endTimeIDOnClick() {
		showDatePicker(endTimeID);
	}

	void showDatePicker(final TextView textView) {
		DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
				textView.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), // 传入年份
				calendar.get(Calendar.MONTH), // 传入月份
				calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
		);
		dialog.show();
	}

	// 上一页和下一页
	@Click(R.id.button_id_prev)
	void prevClick() {
		if (pagination.recordCount == 0 || pagination.currentPage == 1) {
			toastComponent.show("当前为第一页");
		} else {
			pagination.currentPage--;
			queryComponent.queryResult(pagination, searchCriteria);
		}
	}

	@Click(R.id.button_id_next)
	void nextClick() {
		if (pagination.recordCount == 0 || pagination.currentPage == pagination.pageCount) {
			toastComponent.show("当前为最后一页");
		} else {
			pagination.currentPage++;
			queryComponent.queryResult(pagination, searchCriteria);
		}
	}

	// 每个页卡的加载显示
	@Click(R.id.buttonFoodOrder)
	void QueryFoodOrderClick() {
		queryComponent = foodOrderQueryComponent;
		pagination = new Pagination();
		searchCriteria = new SearchCriteria();
		queryComponent.queryResult(pagination, searchCriteria);
	}

	@Click(R.id.buttonExpensesOrder)
	void QueryExpensesOrderClick() {
		queryComponent = expensesOrderQueryComponent;
		pagination = new Pagination();
		searchCriteria = new SearchCriteria();
		queryComponent.queryResult(pagination, searchCriteria);
	}

	@Click(R.id.buttonCollectionOrder)
	void QueryCollectionOrderClick() {
		queryComponent = collectionOrderQueryComponent;
		pagination = new Pagination();
		searchCriteria = new SearchCriteria();
		queryComponent.queryResult(pagination, searchCriteria);
	}

	@Click(R.id.buttonBalanceOrder)
	void QueryBalanceOrderClick() {
		queryComponent = balanceOrderQueryComponent;
		pagination = new Pagination();
		searchCriteria = new SearchCriteria();
		queryComponent.queryResult(pagination, searchCriteria);
	}

}
