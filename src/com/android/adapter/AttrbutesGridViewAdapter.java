package com.android.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.android.R;
import com.android.domain.AttributesR;
import com.android.domain.FoodR;

public class AttrbutesGridViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<AttributesR> classList;
	public List<AttributesR>  attributesList;
	private String type;

	public AttrbutesGridViewAdapter(Context context, String type) {
		this.context = context;
		attributesList = new ArrayList<AttributesR>();
		this.type = type;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return classList == null ? 0 : classList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}
	

	public List<AttributesR> getClassList() {
		return classList;
	}

	public void setClassList(List<AttributesR> classList) {
		this.classList = classList;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.attbutes_g_item, null);
			viewHolder = new ViewHolder();
			viewHolder.checkboxID = (CheckBox) convertView.findViewById(R.id.checkboxID);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final AttributesR bean = classList.get(position);
		if (StringUtils.equalsIgnoreCase(Locale.SIMPLIFIED_CHINESE.getLanguage(), type)) {
			bean.title = bean.nameZh;
		} else {
			bean.title = bean.name;
		}
		viewHolder.checkboxID.setText(bean.title);
		viewHolder.checkboxID.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					attributesList.add(bean);
				}else{
					attributesList.remove(bean);
				}
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		CheckBox checkboxID;
	}

}
