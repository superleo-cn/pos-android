package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.R;
import com.android.domain.FoodOrder;

public class FoodQueryAllDBListviewAdapter extends BaseAdapter {
	private List<FoodOrder> data;
	private Context context;
	private LayoutInflater inflater;
	
	public FoodQueryAllDBListviewAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public List<FoodOrder> getData() {
		return data;
	}

	public void setData(List<FoodOrder> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data == null ? 0: data.size();
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
			viewHolder.list_item_title = (LinearLayout) convertView.findViewById(R.id.list_item_title);
			viewHolder.text_food_id = (TextView) convertView.findViewById(R.id.text_food_id);
			viewHolder.text_shop_id = (TextView) convertView.findViewById(R.id.text_shop_id);
			viewHolder.text_user_id = (TextView) convertView.findViewById(R.id.text_user_id);
			viewHolder.text_retail_price = (TextView) convertView.findViewById(R.id.text_retail_price);
			viewHolder.text_quantity = (TextView) convertView.findViewById(R.id.text_quantity);
			viewHolder.text_foc = (TextView) convertView.findViewById(R.id.text_foc);
			viewHolder.text_discount = (TextView) convertView.findViewById(R.id.text_discount);
			viewHolder.text_total_package = (TextView) convertView.findViewById(R.id.text_total_package);
			viewHolder.text_status = (TextView) convertView.findViewById(R.id.text_status);
			viewHolder.text_date = (TextView) convertView.findViewById(R.id.text_date);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(position == 0){
			viewHolder.list_item_title.setVisibility(View.VISIBLE);
		}else{
			viewHolder.list_item_title.setVisibility(View.GONE);
		}
		FoodOrder food_order=data.get(position);
		viewHolder.text_food_id.setText(food_order.foodId);
		viewHolder.text_shop_id.setText(food_order.shopId);
		viewHolder.text_user_id.setText(food_order.userId);
		viewHolder.text_retail_price.setText(food_order.retailPrice);
		viewHolder.text_quantity.setText(food_order.quantity);
		viewHolder.text_foc.setText(food_order.foc);
		viewHolder.text_discount.setText(food_order.discount);
		viewHolder.text_total_package.setText(food_order.totalPackage);
		viewHolder.text_status.setText(food_order.status);
		viewHolder.text_date.setText(food_order.date);
		return convertView;
	}
	private final class ViewHolder {
		LinearLayout list_item_title;
		TextView text_food_id;
		TextView text_shop_id;
		TextView text_user_id;
		TextView text_retail_price;
		TextView text_quantity;
		TextView text_foc;
		TextView text_discount;
		TextView text_total_package;
		TextView text_status;
		TextView text_date;
	}
}
