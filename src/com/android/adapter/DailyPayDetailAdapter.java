package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.bean.DailyPayDetailBean;
import com.android.singaporeanorderingsystem.R;

public class DailyPayDetailAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<DailyPayDetailBean> classList;
	private Handler handler;
	public DailyPayDetailAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public DailyPayDetailAdapter(Context context, List<DailyPayDetailBean> list,
			Handler handler) {
		this.context = context;
		this.classList = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.handler = handler;
	}

	public int getCount() {
		return classList.size();
	}

	public List<DailyPayDetailBean> getClassList() {
		return classList;
	}

	public void setClassList(List<DailyPayDetailBean> classList) {
		this.classList = classList;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		DailyPayDetailBean bean;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.detail_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.text_id_name = (TextView) convertView.findViewById(R.id.text_id_name);
			viewHolder.text_id_price = (EditText) convertView.findViewById(R.id.text_id_price);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bean = classList.get(position);
		viewHolder.text_id_name.setText(bean.getName());
		viewHolder.text_id_price.setText(bean.getPrice());
		return convertView;
	}

	public final class ViewHolder {
		TextView text_id_name;
		EditText text_id_price;
	}

}
