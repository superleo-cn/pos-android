package com.android.component.ui.query;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.R;
import com.android.bean.Pagination;
import com.android.bean.SearchCriteria;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

@EBean
public abstract class AbstractQueryComponent {

	@RootContext
	Context context;

	@ViewById(R.id.queryAllDBListview)
	ListView queryAllDBListview;

	public abstract BaseAdapter doQuery(Pagination pagination, SearchCriteria searchCriteria);

	public void queryResult(Pagination pagination, SearchCriteria searchCriteria) {
		BaseAdapter adapter = doQuery(pagination, searchCriteria);
		queryAllDBListview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
