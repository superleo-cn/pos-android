package com.android.component.ui.query;

import android.widget.BaseAdapter;

import com.android.adapter.query.BalanceQueryAllDBListviewAdapter;
import com.android.adapter.query.ExpensesQueryAllDBListviewAdapter;
import com.android.adapter.query.FoodOrderQueryListViewAdapter;
import com.android.bean.Pagination;
import com.android.bean.SearchCriteria;
import com.android.common.PaginationHelper;
import com.android.domain.BalanceOrder;
import com.android.domain.ExpensesOrder;
import com.android.domain.FoodOrder;
import com.googlecode.androidannotations.annotations.EBean;

@EBean
public class BalanceOrderQueryComponent extends AbstractQueryComponent {

	public BaseAdapter doQuery(Pagination pagination, SearchCriteria searchCriteria) {
		PaginationHelper.pagination(BalanceOrder.class, pagination, searchCriteria);
		return new BalanceQueryAllDBListviewAdapter(context, pagination.recordList);

	}

}
