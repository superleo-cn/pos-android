package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.R;
import com.android.domain.Food;

public class FoodListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Food> classList;
	private Handler handler;
	private MyListener myListener;
	public static final int LESS_DATALIST = 1004;

	public FoodListAdapter(Context context, List<Food> list, Handler handler) {
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

//		if (convertView == null) {
			convertView = inflater.inflate(R.layout.foot_list_item, null);
			viewHolder = new ViewHolder();
			Food bean = classList.get(position);
			myListener = new MyListener(position);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.food_title);
			viewHolder.titleTextView.setText(bean.title);
			viewHolder.food_image = (ImageView) convertView.findViewById(R.id.food_image);
			// reset the quality
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Config.RGB_565;
			options.inDither = true;
			Bitmap bitmap = BitmapFactory.decodeFile(bean.picture, options);
			viewHolder.food_image.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
			viewHolder.food_btn = (ImageView) convertView.findViewById(R.id.food_btn);
			viewHolder.food_btn.setOnClickListener(myListener);
			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}

		return convertView;
	}

	/**
	 * 点菜面板删除监听器
	 * 
	 * @author superleo
	 * 
	 */
	private class MyListener implements OnClickListener {
		int mPosition;

		public MyListener(int inPosition) {
			mPosition = inPosition;
		}

		public void onClick(View v) {
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
