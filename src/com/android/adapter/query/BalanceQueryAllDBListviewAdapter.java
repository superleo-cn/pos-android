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
import com.android.domain.BalanceOrder;

public class BalanceQueryAllDBListviewAdapter extends BaseAdapter {
	private List<BalanceOrder> data;
	private Context context;
	private LayoutInflater inflater;

	public BalanceQueryAllDBListviewAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public BalanceQueryAllDBListviewAdapter(Context context, List<BalanceOrder> data) {
		this.data = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public List<BalanceOrder> getData() {
		return data;
	}

	public void setData(List<BalanceOrder> data) {
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
			convertView = inflater.inflate(R.layout.query_balance_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.list_item_title = (LinearLayout) convertView.findViewById(R.id.list_item_title);
			viewHolder.text_shopId = (TextView) convertView.findViewById(R.id.text_shopId);
			viewHolder.text_userId = (TextView) convertView.findViewById(R.id.text_userId);
			viewHolder.text_aOpenBalance = (TextView) convertView.findViewById(R.id.text_aOpenBalance);
			viewHolder.text_bExpenses = (TextView) convertView.findViewById(R.id.text_bExpenses);
			viewHolder.text_cCashCollected = (TextView) convertView.findViewById(R.id.text_cCashCollected);
			viewHolder.text_dDailyTurnover = (TextView) convertView.findViewById(R.id.text_dDailyTurnover);
			viewHolder.text_eNextOpenBalance = (TextView) convertView.findViewById(R.id.text_eNextOpenBalance);
			viewHolder.text_fBringBackCash = (TextView) convertView.findViewById(R.id.text_fBringBackCash);
			viewHolder.text_gTotalBalance = (TextView) convertView.findViewById(R.id.text_gTotalBalance);
			viewHolder.text_courier = (TextView) convertView.findViewById(R.id.text_courier);
			viewHolder.text_others = (TextView) convertView.findViewById(R.id.text_others);
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
		BalanceOrder balanceOrder = data.get(position);
		viewHolder.text_shopId.setText(balanceOrder.shopId);
		viewHolder.text_userId.setText(balanceOrder.userId);
		viewHolder.text_aOpenBalance.setText(balanceOrder.aOpenBalance);
		viewHolder.text_bExpenses.setText(balanceOrder.bExpenses);
		viewHolder.text_cCashCollected.setText(balanceOrder.cCashCollected);
		viewHolder.text_dDailyTurnover.setText(balanceOrder.dDailyTurnover);
		viewHolder.text_eNextOpenBalance.setText(balanceOrder.eNextOpenBalance);
		viewHolder.text_fBringBackCash.setText(balanceOrder.fBringBackCash);
		viewHolder.text_gTotalBalance.setText(balanceOrder.gTotalBalance);
		viewHolder.text_courier.setText(balanceOrder.courier);
		viewHolder.text_others.setText(balanceOrder.others);
		viewHolder.text_status.setText(balanceOrder.status);
		viewHolder.text_date.setText(balanceOrder.date);
		return convertView;
	}

	private final class ViewHolder {
		LinearLayout list_item_title;
		TextView text_shopId;
		TextView text_userId;
		TextView text_aOpenBalance;
		TextView text_bExpenses;
		TextView text_cCashCollected;
		TextView text_dDailyTurnover;
		TextView text_eNextOpenBalance;
		TextView text_fBringBackCash;
		TextView text_gTotalBalance;
		TextView text_courier;
		TextView text_others;
		TextView text_status;
		TextView text_date;
	}
}
