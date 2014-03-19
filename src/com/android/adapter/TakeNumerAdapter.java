package com.android.adapter;

import java.text.DecimalFormat;
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
import com.android.bean.TakeNumberBean;

public class TakeNumerAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<TakeNumberBean> classList;
	public static final int SET_NUM = 2001;
	private int index = -1;
	private Handler handler;
	public static Map<Integer, String> hashMap_numprice = new HashMap<Integer, String>();
	public static Map<Integer, String> hashMap_num = new HashMap<Integer, String>();

	public TakeNumerAdapter(Context context, List<TakeNumberBean> list, Handler handler) {
		this.context = context;
		this.classList = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.handler = handler;
	}

	public int getCount() {
		return classList.size();
	}

	public Object getItem(int position) {
		return classList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final TakeNumberBean bean;
		//if (convertView == null) {
			convertView = inflater.inflate(R.layout.num_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.num_id_name = (TextView) convertView.findViewById(R.id.num_id_name);
			viewHolder.id_price = (EditText) convertView.findViewById(R.id.num_id_price);
			viewHolder.num_price = (TextView) convertView.findViewById(R.id.num_price);
			viewHolder.danwei = (TextView) convertView.findViewById(R.id.danwei);
			convertView.setTag(viewHolder);
		//} else {
			//viewHolder = (ViewHolder) convertView.getTag();
		//}

		bean = classList.get(position);
		viewHolder.num_id_name.setText(new DecimalFormat("0.00").format(Double.parseDouble(bean.getPrice() == "" ? "0" : bean.getPrice()))
				.toString());
		viewHolder.id_price.setText(bean.getNum());
		Double is_price = Double.parseDouble(bean.getPrice());
		if (is_price > 1) {
			viewHolder.danwei.setText(context.getString(R.string.money_piece));
		} else {
			viewHolder.danwei.setText(context.getString(R.string.money_coin));
		}
		try {
			Double price = Double.parseDouble(viewHolder.num_id_name.getText().toString());
			String num_tv = viewHolder.id_price.getText().toString();
			if (num_tv == null) {
				num_tv = "0";
			} else {
				if (num_tv.equals("")) {
					num_tv = "0";
				}
			}
			int num = Integer.parseInt(num_tv);
			Double total_price = price * num;
			DecimalFormat df = new DecimalFormat("0.00");
			viewHolder.num_price.setText(df.format(total_price));
		} catch (Exception e) {
			Log.e("[TakeNumerAdapter]", "转换报错", e);
		}

		viewHolder.id_price.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {

					index = position;

				}

				return false;
			}
		});

		viewHolder.id_price.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// viewHolder.id_price.setInputType(InputType.TYPE_CLASS_NUMBER);
				if (is_maxPrice(s.toString())) {
					viewHolder.id_price.setText("9999.99");
					return;
				}

				try {
					String result = s.toString();
					if (result == null) {
						result = "0";
					} else {
						if (result.equals("")) {
							result = "0";
						}
					}
					Double price = Double.parseDouble(viewHolder.num_id_name.getText().toString());
					String num_tv = result;
					bean.setNum(num_tv);
					int num = Integer.parseInt(num_tv);
					Double total_price = price * num;
					DecimalFormat df = new DecimalFormat("0.00");
					viewHolder.num_price.setText(df.format(total_price));
					hashMap_num.put(position, result);
					hashMap_numprice.put(position, String.valueOf(total_price));
					Message msg = new Message();
					msg.what = SET_NUM;
					msg.obj = position + "," + String.valueOf(total_price);
					handler.sendMessage(msg);
					Log.d("[TakeNumerAdapter]", "计算次数");
				} catch (Exception e) {
					Log.e("[TakeNumerAdapter]", "计算错误", e);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

		viewHolder.id_price.clearFocus();

		if (index != -1 && index == position) {

			// 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
			viewHolder.id_price.requestFocus();
			if (hashMap_num.get(position) != null) {
				viewHolder.id_price.setText(hashMap_num.get(position));
				Log.d("[TakeNumerAdapter]", "修改数量成功");
			}
			if (hashMap_numprice.get(position) != null) {
				viewHolder.num_price.setText(hashMap_numprice.get(position));
				Log.d("[TakeNumerAdapter]", "修改价格成功");
			}
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
			Log.e("[TakeNumerAdapter]", "价钱转换错误", e);
			return false;
		}
		return false;

	}

	public final class ViewHolder {
		TextView num_id_name;
		EditText id_price;
		TextView num_price;
		TextView danwei;
	}

}
