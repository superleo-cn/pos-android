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
import com.android.domain.CollectionOrder;

public class CollectionQueryAllDBListviewAdapter extends BaseAdapter {
	private List<CollectionOrder> data;
	private Context context;
	private LayoutInflater inflater;

	public CollectionQueryAllDBListviewAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public CollectionQueryAllDBListviewAdapter(Context context, List<CollectionOrder> data) {
		this.data = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public List<CollectionOrder> getData() {
		return data;
	}

	public void setData(List<CollectionOrder> data) {
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
			convertView = inflater.inflate(R.layout.query_collection_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.list_item_title = (LinearLayout) convertView.findViewById(R.id.list_item_title);
			viewHolder.text_shop_id = (TextView) convertView.findViewById(R.id.text_shop_id);
			viewHolder.text_user_id = (TextView) convertView.findViewById(R.id.text_user_id);
			viewHolder.text_quantity = (TextView) convertView.findViewById(R.id.text_quantity);
			viewHolder.text_cash_id = (TextView) convertView.findViewById(R.id.text_cash_id);
			viewHolder.text_date = (TextView) convertView.findViewById(R.id.text_date);
			viewHolder.text_status = (TextView) convertView.findViewById(R.id.text_status);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			viewHolder.list_item_title.setVisibility(View.VISIBLE);
		} else {
			viewHolder.list_item_title.setVisibility(View.GONE);
		}
		CollectionOrder collectionOrder = data.get(position);
		viewHolder.text_shop_id.setText(collectionOrder.shopId);
		viewHolder.text_user_id.setText(collectionOrder.userId);
		viewHolder.text_quantity.setText(collectionOrder.quantity);
		viewHolder.text_cash_id.setText(collectionOrder.cashId);
		viewHolder.text_date.setText(collectionOrder.date);
		viewHolder.text_status.setText(collectionOrder.status);
		return convertView;
	}

	private final class ViewHolder {
		LinearLayout list_item_title;
		TextView text_shop_id;
		TextView text_user_id;
		TextView text_quantity;
		TextView text_cash_id;
		TextView text_date;
		TextView text_status;
	}
}
