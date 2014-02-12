package com.android.adapter.query;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.R;
import com.android.domain.ExpensesOrder;

public class ExpensesQueryAllDBListviewAdapter extends BaseAdapter {
	private List<ExpensesOrder> data;
	private LayoutInflater inflater;

	public ExpensesQueryAllDBListviewAdapter(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ExpensesQueryAllDBListviewAdapter(Context context, List<ExpensesOrder> data) {
		this.data = data;
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
			convertView = inflater.inflate(R.layout.query_expenses_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.list_item_title = (LinearLayout) convertView.findViewById(R.id.list_item_title);
			viewHolder.text_consumption_id = (TextView) convertView.findViewById(R.id.text_consumption_id);
			//viewHolder.text_shop_id = (TextView) convertView.findViewById(R.id.text_shop_id);
			viewHolder.text_user_id = (TextView) convertView.findViewById(R.id.text_user_id);
			viewHolder.text_price = (TextView) convertView.findViewById(R.id.text_price);
			viewHolder.text_status = (TextView) convertView.findViewById(R.id.text_status);
			viewHolder.text_date = (TextView) convertView.findViewById(R.id.text_date);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			viewHolder.list_item_title.setVisibility(View.VISIBLE);
		} else {
			viewHolder.list_item_title.setVisibility(View.GONE);
		}
		ExpensesOrder expensesOrder = data.get(position);
		viewHolder.text_consumption_id.setText(expensesOrder.consumptionId);
		//viewHolder.text_shop_id.setText(expensesOrder.shopID);
		viewHolder.text_user_id.setText(expensesOrder.userID);
		viewHolder.text_price.setText(expensesOrder.price);
		viewHolder.text_status.setText(expensesOrder.status);
		viewHolder.text_date.setText(expensesOrder.date);
		return convertView;
	}

	private final class ViewHolder {
		LinearLayout list_item_title;
		TextView text_consumption_id;
		//TextView text_shop_id;
		TextView text_user_id;
		TextView text_price;
		TextView text_status;
		TextView text_date;
	}
}
