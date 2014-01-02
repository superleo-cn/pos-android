package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.R;

public class GiditNumberAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<String> classList;

	public GiditNumberAdapter(Context context, List<String> list) {
		this.classList = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return classList == null ? 0 : classList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		String number = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.digit_btn_item, null);
			viewHolder = new ViewHolder();
			viewHolder.gidit_num = (TextView) convertView.findViewById(R.id.gidit_num);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		number = classList.get(position);

		viewHolder.gidit_num.setText(number);
		return convertView;
	}

	public final class ViewHolder {
		TextView gidit_num;

	}

}
