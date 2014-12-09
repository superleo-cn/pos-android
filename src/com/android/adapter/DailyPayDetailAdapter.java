package com.android.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.R;
import com.android.bean.DailyPayDetailBean;

public class DailyPayDetailAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<DailyPayDetailBean> classList;
	private Handler handler;
	private int index = -1;
	public static final int CHAGE_NUM_DETAIL = 1020;
	public static Map<Integer, String> hashMap_detail = new HashMap<Integer, String>();
	private DailyPayDetailBean detailBean;

	public DailyPayDetailAdapter(Context context, List<DailyPayDetailBean> list, Handler handler) {
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
			viewHolder.text_id_price.setTag(position);
			viewHolder.text_id_price.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index = position;
					}

					return false;
				}
			});

			viewHolder.text_id_price.addTextChangedListener(new CustTextWatch(viewHolder) {

				@Override
				public void afterTextChanged(Editable s, ViewHolder holder) {
					if (is_maxPrice(s.toString())) {
						viewHolder.text_id_price.setText("9999.99");
						return;
					}
					int p = (Integer) viewHolder.text_id_price.getTag();
					cacheData(s.toString(), p);
					if (viewHolder.text_id_price.getText().toString().isEmpty()) {
						hashMap_detail.put(p, "");
						Message msg = new Message();
						msg.what = CHAGE_NUM_DETAIL;
						msg.obj = p + "+" + "0.00";
						handler.sendMessage(msg);
					} else {
						String price = s.toString();
						if (s.length() == 0) {
							price = "0.00";
						} else {
							if (price.equals("")) {
								price = "0.00";
							}
						}
						String now_price = price;
						hashMap_detail.put(p, now_price);
						Message msg = new Message();
						msg.what = CHAGE_NUM_DETAIL;
						msg.obj = p + "+" + now_price;
						handler.sendMessage(msg);
					}
				}

			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.text_id_price.setTag(position);
		}

		bean = classList.get(position);
		viewHolder.text_id_name.setText(bean.getName());
		viewHolder.text_id_price.setText(bean.getPrice());

		viewHolder.text_id_price.clearFocus();

		if (index != -1 && index == position) {
			viewHolder.text_id_price.requestFocus();
		}

		if (hashMap_detail.get(position) != null) {
			viewHolder.text_id_price.setText(hashMap_detail.get(position));
		}
		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// keyboardComponent.dismissKeyboard(v);
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
		return convertView;
	}

	public boolean is_maxPrice(String zhi) {
		try {
			Double now_price = Double.parseDouble(zhi);
			if (now_price > 9999.99) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;

	}

	private final class ViewHolder {
		TextView text_id_name;
		EditText text_id_price;
	}

	public void cacheData(String price, int p) {
		if (p > classList.size()){
			return;
		}
		detailBean = classList.get(p);
		detailBean.setPrice(price);
		classList.set(p, detailBean);
	}

	private abstract class CustTextWatch implements TextWatcher {
		private ViewHolder mViewHolder;

		public CustTextWatch(ViewHolder holder) {
			this.mViewHolder = holder;
		}

		@Override
		public void afterTextChanged(Editable s) {
			afterTextChanged(s, mViewHolder);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		public abstract void afterTextChanged(Editable s, ViewHolder holder);
	}
}
