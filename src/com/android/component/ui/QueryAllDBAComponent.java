package com.android.component.ui;

import java.util.List;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;

import com.android.R;
import com.android.adapter.QueryAllDBListviewAdapter;
import com.android.domain.FoodOrder;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

@EBean
public class QueryAllDBAComponent {
	// 注入 Context 变量
	@RootContext
	Context context;

	@ViewById(R.id.queryAllDBListview)
	ListView queryAllDBListview;

	@ViewById(R.id.buttonFoodOrder)
	Button buttonFoodOrder;

	@ViewById(R.id.buttonExpensesOrder)
	Button buttonExpensesOrder;

	@ViewById(R.id.buttonCollectionOrder)
	Button buttonCollectionOrder;

	@ViewById(R.id.buttonBalanceOrder)
	Button buttonBalanceOrder;

	private QueryAllDBListviewAdapter adapter;

	@AfterViews
	void init() {
		adapter = new QueryAllDBListviewAdapter(context);
		List<FoodOrder> data = FoodOrder.queryAllList();
		adapter.setData(data);
		queryAllDBListview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
	}

	@Click(R.id.buttonFoodOrder)
	void QueryFoodOrderClick() {

	}

	@Click(R.id.buttonExpensesOrder)
	void QueryExpensesOrderClick() {

	}

	@Click(R.id.buttonCollectionOrder)
	void QueryCollectionOrderClick() {

	}

	@Click(R.id.buttonBalanceOrder)
	void QueryBalanceOrderClick() {

	}
}
