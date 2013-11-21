package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.R;
import com.android.bean.FoodListBean;


public class FoodListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<FoodListBean> classList;
	private Handler handler;
	private MyListener myListener;
	public static final int LESS_DATALIST = 1004;

	public FoodListAdapter(Context context, List<FoodListBean> list,
			Handler handler) {
		this.context = context;
		this.classList = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.handler = handler;
	}

	public int getCount() {
		return classList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		FoodListBean bean;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.foot_list_item, null);
			viewHolder = new ViewHolder();
			myListener = new MyListener(position);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.food_title);
			viewHolder.food_image = (ImageView) convertView.findViewById(R.id.food_image);
			viewHolder.food_btn = (ImageView) convertView.findViewById(R.id.food_btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bean = classList.get(position);
		viewHolder.titleTextView.setText(bean.getTitle());
		Bitmap bitmap = BitmapFactory.decodeFile(bean.getImageID());
		viewHolder.food_image.setImageDrawable(new BitmapDrawable(bitmap));
		viewHolder.food_btn.setOnClickListener(myListener);
		return convertView;
	}

	private class MyListener implements OnClickListener {
		int mPosition;

		public MyListener(int inPosition) {
			mPosition = inPosition;
		}

		public void onClick(View v) {
			Log.e("点击按钮", "");
			new Thread() {
				public void run() {

					Message msg = new Message();
					msg.what = LESS_DATALIST;
					msg.obj = mPosition;
					handler.sendMessage(msg);
				}
			}.start();
		}

	}

	public final class ViewHolder {
		TextView titleTextView;
		ImageView food_image;
		ImageView food_btn;
	}

}
