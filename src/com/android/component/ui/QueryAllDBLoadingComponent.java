package com.android.component.ui;

import java.util.List;

import android.content.Context;
import android.widget.ListView;

import com.android.R;
import com.android.adapter.BalanceQueryAllDBListviewAdapter;
import com.android.adapter.CollectionQueryAllDBListviewAdapter;
import com.android.adapter.ExpensesQueryAllDBListviewAdapter;
import com.android.adapter.FoodQueryAllDBListviewAdapter;
import com.android.common.Constants;
import com.android.domain.BalanceOrder;
import com.android.domain.CollectionOrder;
import com.android.domain.Expenses;
import com.android.domain.ExpensesOrder;
import com.android.domain.FoodOrder;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

@EBean
public class QueryAllDBLoadingComponent {

	// 注入 Context 变量
	@RootContext
	Context context;

	@ViewById(R.id.queryAllDBListview)
	ListView queryAllDBListview;

	public void queryFoodLoading(int pangeno) {
		FoodQueryAllDBListviewAdapter adapter = new FoodQueryAllDBListviewAdapter(context);
		List<FoodOrder> data = FoodOrder.queryListByPange(pangeno, Constants.PARAM_PAGESIZE);
		adapter.setData(data);
		queryAllDBListview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void queryExpensesLoading(int pangeno) {
		ExpensesQueryAllDBListviewAdapter adapter = new ExpensesQueryAllDBListviewAdapter(context);
		List<ExpensesOrder> data = ExpensesOrder.queryList();
		adapter.setData(data);
		queryAllDBListview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void queryCollectionLoading(int pangeno) {
		CollectionQueryAllDBListviewAdapter adapter = new CollectionQueryAllDBListviewAdapter(context);
		List<CollectionOrder> data = CollectionOrder.queryList();
		adapter.setData(data);
		queryAllDBListview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void queryBalanceLoading(int pangeno) {
		BalanceQueryAllDBListviewAdapter adapter = new BalanceQueryAllDBListviewAdapter(context);
		List<BalanceOrder> data = BalanceOrder.queryList();
		adapter.setData(data);
		queryAllDBListview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	public int queryFoodGetCount(){
		return FoodOrder.queryListByCount();
	}
}
