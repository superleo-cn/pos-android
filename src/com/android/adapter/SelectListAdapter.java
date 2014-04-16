package com.android.adapter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.R;
import com.android.bean.SelectFoodBean;
import com.android.common.Constants;
import com.android.common.DisplayUtil;
import com.android.component.ui.main.OrderComponent;

public class SelectListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<SelectFoodBean> classList;
	private OrderComponent component;
	private float x, ux;

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
			viewHolder.food_layout = (LinearLayout) convertView.findViewById(R.id.food_layout);
			viewHolder.food_name = (TextView) convertView.findViewById(R.id.food_name);
			viewHolder.food_num = (TextView) convertView.findViewById(R.id.food_num);
			viewHolder.food_price = (TextView) convertView.findViewById(R.id.food_price);
			viewHolder.textShuXing = (TextView) convertView.findViewById(R.id.textShuXing);
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

		if (bean.getFood_name().equalsIgnoreCase(Constants.SPLIT_LINE)) {
			viewHolder.food_name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			viewHolder.food_name.setTextSize(DisplayUtil.sp2px(context, 18));
			viewHolder.food_num.setTextSize(DisplayUtil.sp2px(context, 18));
			viewHolder.food_price.setTextSize(DisplayUtil.sp2px(context, 18));
			viewHolder.food_name.setText(bean.getFood_name());
		} else {
			viewHolder.food_name.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
			viewHolder.food_name.setTextSize(DisplayUtil.sp2px(context, 30));
			viewHolder.food_num.setTextSize(DisplayUtil.sp2px(context, 30));
			viewHolder.food_price.setTextSize(DisplayUtil.sp2px(context, 30));
			viewHolder.food_name.setText(bean.getFood_name());
		}
		if (bean.getFood_num().equals("0")) {
			viewHolder.food_num.setVisibility(View.GONE);
		} else {
			viewHolder.food_num.setVisibility(View.VISIBLE);
			viewHolder.food_num.setText("x" + bean.getFood_num());
		}
		if (bean.getFood_price().equals("0.00")) {
			viewHolder.food_price.setVisibility(View.GONE);
		} else {
			viewHolder.food_price.setVisibility(View.VISIBLE);
			viewHolder.food_price.setText("S$" + new DecimalFormat("0.00").format(Double.parseDouble(bean.getFood_price())));
		}

		if(bean.getAttributesContext()!=null && !bean.getAttributesContext().equals("")
				&& !bean.getAttributesContext().equals("null")){
			viewHolder.textShuXing.setVisibility(View.VISIBLE);
			viewHolder.textShuXing.setText("("+bean.getAttributesContext()+")");
		}
		// convertView.setOnLongClickListener(new OnLongClickListener() {
		// @Override
		// public boolean onLongClick(View v) {
		// classList.clear();
		// component.doCalculation();
		// notifyDataSetChanged();
		// return false;
		// }
		// });

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
					// if (Math.abs(x - ux) > 20) {
					// component.remove2(position);
					// return true;
					// }
					if (x - ux > 0 && Math.abs(x - ux) >= 20) {
						// Toast.makeText(context, "往左", 1).show();
						if (classList.size() == position + 1) {
							if (!classList.get(position).getFood_name().equalsIgnoreCase(Constants.SPLIT_LINE)) {
								Map<String, SelectFoodBean> map = new HashMap<String, SelectFoodBean>();
								int j = 0;
								for (int i = 0; i < classList.size(); i++) {
									map.put(i + j + "", classList.get(i));
									if (i == position) {
										SelectFoodBean foodBean = new SelectFoodBean();
										foodBean.setFood_name(Constants.SPLIT_LINE);
										foodBean.setFood_num("0");
										foodBean.setFood_price("0.00");
										j = 1;
										map.put(i + j + "", foodBean);
									}
								}
								j = 0;
								classList.clear();
								for (int z = 0; z < map.size(); z++) {
									classList.add(map.get(z + ""));
								}
								notifyDataSetChanged();
							}
						} else {
							if (!classList.get(position).getFood_name().equalsIgnoreCase(Constants.SPLIT_LINE)
									&& !classList.get(position + 1).getFood_name().equalsIgnoreCase(Constants.SPLIT_LINE)) {
								Map<String, SelectFoodBean> map = new HashMap<String, SelectFoodBean>();
								int j = 0;
								for (int i = 0; i < classList.size(); i++) {
									map.put(i + j + "", classList.get(i));
									if (i == position) {
										SelectFoodBean foodBean = new SelectFoodBean();
										foodBean.setFood_name(Constants.SPLIT_LINE);
										foodBean.setFood_num("0");
										foodBean.setFood_price("0.00");
										j = 1;
										map.put(i + j + "", foodBean);
									}
								}
								j = 0;
								classList.clear();
								for (int z = 0; z < map.size(); z++) {
									classList.add(map.get(z + ""));
								}
								notifyDataSetChanged();
							}
						}
						return true;
					} else if (x - ux < 0 && Math.abs(x - ux) >= 20) {
						// Toast.makeText(context, "往右", 1).show();
						component.remove2(position);
						return true;
					}
				}
				return true;
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		LinearLayout food_layout;
		TextView food_name;
		TextView food_num;
		TextView food_price;
		TextView textShuXing;
	}
}
