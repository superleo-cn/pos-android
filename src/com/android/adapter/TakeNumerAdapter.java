package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.R;
import com.android.bean.TakeNumberBean;

public class TakeNumerAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<TakeNumberBean> classList;
	private Handler handler;
	public TakeNumerAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public TakeNumerAdapter(Context context, List<TakeNumberBean> list,
			Handler handler) {
		this.context = context;
		this.classList = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.handler = handler;
	}

	public int getCount() {
		return classList.size();
	}

	public List<TakeNumberBean> getClassList() {
		return classList;
	}

	public void setClassList(List<TakeNumberBean> classList) {
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
		TakeNumberBean bean;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.num_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.text_id_name = (TextView) convertView.findViewById(R.id.text_id_name);
			viewHolder.text_id_price = (EditText) convertView.findViewById(R.id.text_id_price);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bean = classList.get(position);
		viewHolder.text_id_name.setText(bean.getText1());
		viewHolder.text_id_price.setText(bean.getText2());
		viewHolder.text_id_price.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘 	
				}
			}
		});
		return convertView;
	}

	public final class ViewHolder {
		TextView text_id_name;
		EditText text_id_price;
	}

}
