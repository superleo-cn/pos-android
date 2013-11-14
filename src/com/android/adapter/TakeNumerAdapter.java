package com.android.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.android.bean.TakeNumberBean;
import com.android.singaporeanorderingsystem.R;

public class TakeNumerAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private Context context;
	private LayoutInflater inflater;
	private List<TakeNumberBean> classList;
	@SuppressWarnings("unused")
	private Handler handler;
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
		final ViewHolder viewHolder;
		final TakeNumberBean bean;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.num_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.num_id_name = (TextView) convertView.findViewById(R.id.num_id_name);
			viewHolder.id_price = (EditText) convertView.findViewById(R.id.num_id_price);
			viewHolder.num_price=(TextView) convertView.findViewById(R.id.num_price);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bean = classList.get(position);
		viewHolder.num_id_name.setText(bean.getText1());
		viewHolder.id_price.setText(bean.getText2());
		//final String now_str=viewHolder.id_price.getText().toString();
		
		try{
			Double price=Double.parseDouble(viewHolder.num_id_name.getText().toString());
			int num=Integer.parseInt(viewHolder.id_price.getText().toString());
			Double total_price=price*num;
			DecimalFormat df=new DecimalFormat("0.00");
			viewHolder.num_price.setText(df.format(total_price));
		}catch(Exception e){
			
		}
		
		viewHolder.id_price.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(bean.getText2().equals(viewHolder.id_price.getText().toString())){
					
				}else{
				
					try{
						Double price=Double.parseDouble(viewHolder.num_id_name.getText().toString());
						int num=Integer.parseInt(viewHolder.id_price.getText().toString());
						Double total_price=price*num;
						DecimalFormat df=new DecimalFormat("0.00");
						viewHolder.num_price.setText(df.format(total_price));
					}catch(Exception e){
						
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}});
		return convertView;
	}

	public final class ViewHolder {
		TextView num_id_name;
		EditText id_price;
		TextView num_price;
	}
	

}
