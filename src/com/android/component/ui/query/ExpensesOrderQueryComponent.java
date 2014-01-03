package com.android.component.ui.query;

import android.widget.BaseAdapter;

import com.android.adapter.query.ExpensesQueryAllDBListviewAdapter;
import com.android.adapter.query.FoodOrderQueryListViewAdapter;
import com.android.bean.Pagination;
import com.android.bean.SearchCriteria;
import com.android.common.PaginationHelper;
import com.android.domain.ExpensesOrder;
import com.android.domain.FoodOrder;
import com.googlecode.androidannotations.annotations.EBean;

@EBean
public class ExpensesOrderQueryComponent extends AbstractQueryComponent {

	public BaseAdapter doQuery(Pagination pagination, SearchCriteria searchCriteria) {
		PaginationHelper.pagination(ExpensesOrder.class, pagination, searchCriteria);
		return new ExpensesQueryAllDBListviewAdapter(context, pagination.recordList);

	}

}
