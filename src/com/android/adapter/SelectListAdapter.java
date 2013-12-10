package com.android.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.android.R;
import com.android.adapter.GiditNumberAdapter.ViewHolder;
import com.android.bean.SelectFoodBean;




import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<SelectFoodBean> classList;
	
	public SelectListAdapter(Context context,List<SelectFoodBean> list)
	{
		this.context = context;
		this.classList = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

	
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		SelectFoodBean bean;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.select_list_item, null);
			viewHolder = new ViewHolder();		
			viewHolder.food_name=(TextView)convertView.findViewById(R.id.food_name);	
			viewHolder.food_num=(TextView)convertView.findViewById(R.id.food_num);	
			viewHolder.food_price=(TextView)convertView.findViewById(R.id.food_price);	
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		bean = classList.get(position);
		if(position % 2 ==0){
			convertView.setBackgroundColor(color.white);
		}else{
			convertView.setBackgroundColor(Color.LTGRAY);
		}
		viewHolder.food_name.setText(bean.getFood_name());
		viewHolder.food_num.setText("x"+bean.getFood_num());
		viewHolder.food_price.setText("S$"+new DecimalFormat("0.00").format(Double.parseDouble(bean.getFood_price())));
		return convertView;
	}
	
	public final class ViewHolder
	{	
		TextView food_name;
		TextView food_num;
		TextView food_price;
	}
}
