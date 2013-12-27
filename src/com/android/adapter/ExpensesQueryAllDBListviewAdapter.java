package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.R;
import com.android.domain.ExpensesOrder;
import com.android.domain.FoodOrder;

public class ExpensesQueryAllDBListviewAdapter extends BaseAdapter {
	private List<ExpensesOrder> data;
	private Context context;
	private LayoutInflater inflater;

	public ExpensesQueryAllDBListviewAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public List<ExpensesOrder> getData() {
		return data;
	}

	public void setData(List<ExpensesOrder> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.query_all_list_item, null);
			viewHolder = new ViewHolder();
			// viewHolder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// ExpensesOrder expenses_order=data.get(position);
		// viewHolder.text.setText(expenses_order.toString());
		return convertView;
	}

	private final class ViewHolder {
		TextView text;
	}
}
