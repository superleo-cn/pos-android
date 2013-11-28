package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.R;
import com.android.bean.DailyPayDetailBean;
import com.android.singaporeanorderingsystem.DailyPayActivity;


public class DailyPayDetailAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private Context context;
	private LayoutInflater inflater;
	private List<DailyPayDetailBean> classList;
	private Handler handler;
	public static final int CHAGE_NUM_DETAIL=1020;
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
		return classList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final DailyPayDetailBean bean;
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
		final String now_str=viewHolder.text_id_price.getText().toString();
		
		
		viewHolder.text_id_price.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
				if(now_str.equals(s.toString())){
				}else{
					//bean.setPrice(viewHolder.text_id_price.getText().toString());			
					
					String price=s.toString();
					if(price==null){
						price="0.00";
					}else{
						if(price.equals("")){
							price="0.00";
						}
					}
					DailyPayActivity.hashMap_detail.put(position, s.toString()); 
							Message msg = new Message();
							msg.what = CHAGE_NUM_DETAIL;
							msg.obj=position+"+"+price;
							handler.sendMessage(msg);
							Log.e("执行输入的价格", "价格");	
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
			//	Log.e("输入改变完", "价格呢");
				
			}});
		
		 
		 if(DailyPayActivity.hashMap_detail.get(position) != null){  
			   viewHolder.text_id_price.setText(DailyPayActivity.hashMap_detail.get(position));
			   Log.e("改变值", "成功");
			             }  
		return convertView;
	}

	private final class ViewHolder {
		TextView text_id_name;
		EditText text_id_price;
	}
	
}
