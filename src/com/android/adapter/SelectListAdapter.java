package com.android.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.R;
import com.android.bean.SelectFoodBean;
import com.android.component.ui.main.OrderComponent;

public class SelectListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<SelectFoodBean> classList;
	private OrderComponent component;
	private float x, ux;
	private static long timer = 0;
	private static final long LONG_PRESS_TIME = 100;

	public SelectListAdapter(Context context, List<SelectFoodBean> list) {
		this.context = context;
		this.classList = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public List<SelectFoodBean> getClassList() {
		return classList;
	}

	public void setClassList(List<SelectFoodBean> classList) {
		this.classList = classList;
	}

	public OrderComponent getComponent() {
		return component;
	}

	public void setComponent(OrderComponent component) {
		this.component = component;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return classList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		SelectFoodBean bean;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.select_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.food_name = (TextView) convertView.findViewById(R.id.food_name);
			viewHolder.food_num = (TextView) convertView.findViewById(R.id.food_num);
			viewHolder.food_price = (TextView) convertView.findViewById(R.id.food_price);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bean = classList.get(position);
		if (position % 2 == 0) {
			convertView.setBackgroundColor(color.white);
		} else {
			convertView.setBackgroundColor(Color.LTGRAY);
		}
		viewHolder.food_name.setText(bean.getFood_name());
		viewHolder.food_num.setText("x" + bean.getFood_num());
		viewHolder.food_price.setText("S$" + new DecimalFormat("0.00").format(Double.parseDouble(bean.getFood_price())));

		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
		        	classList.clear();
					component.doCalculation();
					notifyDataSetChanged();
				return false;
			}
		});

		// 为每一个view项设置触控监听
		convertView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// 当按下时处理
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// 获取按下时的x轴坐标
					x = event.getX();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {// 松开处理
					// 获取松开时的x坐标
					ux = event.getX();
					// 判断当前项中按钮控件不为空时
					if (Math.abs(x - ux) > 20) {
						component.remove2(position);
						return true;
					}
				}
				return false;
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		TextView food_name;
		TextView food_num;
		TextView food_price;
	}
}
