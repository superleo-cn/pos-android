package com.android.component.ui.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract.Colors;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.R;
import com.android.adapter.AttrbutesGridViewAdapter;
import com.android.adapter.FoodListAdapter;
import com.android.component.SharedPreferencesComponent_;
import com.android.dialog.MyAttrbutesDialog;
import com.android.domain.AttributesR;
import com.android.domain.CategoriesR;
import com.android.domain.FoodR;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 点菜面板组件
 * 
 * @author superleo
 * 
 */
@EBean
public class FoodComponent {

	@RootContext
	Context context;

	@ViewById(R.id.food_list)
	GridView foodView; // 菜品列表
	
	@ViewById(R.id.layout_left)
	LinearLayout layout_left;

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	private List<FoodR> foodDataList;

	private OrderComponent orderComponent;

	/**
	 * 点菜面板组件初始化
	 * 
	 * @param foodView
	 * @param handler
	 * 
	 */
	@AfterViews
	public void initFood() {
		String type = sharedPrefs.language().get();
		List<CategoriesR> categoriesList =  CategoriesR.queryList();
		for(int i = 0 ; i< categoriesList.size(); i ++){
		CategoriesR bean =  categoriesList.get(i);
		if (StringUtils.equalsIgnoreCase(Locale.SIMPLIFIED_CHINESE.getLanguage(), type)) {
			bean.title = bean.nameZh;
		} else {
			bean.title = bean.name;
		}
		this.foodDataList = FoodR.queryIDList(bean.categoriesId);
		View leftView = LinearLayout.inflate(context, R.layout.main_left_view, null);
		Button buttonTitleID = (Button) leftView.findViewById(R.id.buttonTitleID);
		buttonTitleID.setText(Html.fromHtml(bean.title));
		final HorizontalScrollView horizontalScrollViewID = (HorizontalScrollView) leftView.findViewById(R.id.horizontalScrollViewID);
		
		buttonTitleID.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				horizontalScrollViewID.smoothScrollTo(0,0);
			}
		});
		
		LinearLayout linearLayout = new LinearLayout(context);
		for (final FoodR food : foodDataList) {
			if (StringUtils.equalsIgnoreCase(Locale.SIMPLIFIED_CHINESE.getLanguage(), type)) {
				food.title = food.nameZh;
			} else {
				food.title = food.name;
			}
			
			View leftitemView = RelativeLayout.inflate(context, R.layout.main_left_item, null);
			ImageView food_image = (ImageView) leftitemView.findViewById(R.id.food_image);
			TextView food_title = (TextView) leftitemView.findViewById(R.id.food_title);
			food_title.setText(food.title);
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Config.RGB_565;
			options.inDither = true;
			Bitmap bitmap = BitmapFactory.decodeFile(food.picture, options);
			food_image.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
			linearLayout.addView(leftitemView);
			leftitemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					food.attributesID = "";
					food.attributesContext= "";
					orderComponent.order(food);
				}
			});
			leftitemView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					List<AttributesR> list = AttributesR.queryIDList(food.foodId);
					if(list.size() != 0){
						showDialog(food,context,list);
						return false;
					}else{
						return true;
					}
				}
			});
		}
		
//		FoodListAdapter adapter = new FoodListAdapter(context, foodDataList, handler);
//		foodView.setAdapter(adapter);
		horizontalScrollViewID.addView(linearLayout);
		layout_left.addView(leftView);

		}
	}

	/**
	 * 显示属性的AlertDialog
	 * @param context
	 * */
	 private void showDialog(final FoodR food,Context context,List<AttributesR> list) { 
		final List<AttributesR> attributesList = new ArrayList<AttributesR>();
		String type = sharedPrefs.language().get();
		ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.dialog);  
//		LinearLayout EntryView = new LinearLayout(context);
//		EntryView.setPadding(10, 10, 10, 10);
//		EntryView.setGravity(Gravity.CENTER);
//		if(list.size()!= 0){
//		for(int i = 0; i< list.size() ;i ++){
//			final AttributesR bean = list.get(i);
//			CheckBox checkBox = new CheckBox(context);
//			checkBox.setTextSize(35);
//			if (StringUtils.equalsIgnoreCase(Locale.SIMPLIFIED_CHINESE.getLanguage(), type)) {
//				bean.title = bean.nameZh;
//			} else {
//				bean.title = bean.name;
//			}
//			checkBox.setText(bean.title);
//			EntryView.addView(checkBox);
//			
//			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					if(isChecked){
//						attributesList.add(bean);
//					}else{
//						attributesList.remove(bean);
//					}
//				}
//			});
//			
//		}
//		AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
//		builder.setCancelable(false);
//		if (StringUtils.equalsIgnoreCase(Locale.SIMPLIFIED_CHINESE.getLanguage(), type)) {
//			builder.setTitle("添加属性");
//			builder.setView(EntryView);
//			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int whichButton) {
//					String attributesID = "";
//					String attributesContext = "";
//					for(int i = 0 ; i <attributesList.size() ; i ++){
//						AttributesR bean = attributesList.get(i);
//						if(i >= attributesList.size()-1){
//							attributesID += bean.attributesRID;
//							attributesContext += bean.title;
//						}else{
//							attributesID += bean.attributesRID+",";
//							attributesContext += bean.title+",";
//						}
//					}
//					food.attributesID = attributesID;
//					food.attributesContext=attributesContext;
//					orderComponent.order(food);
//				}
//			});
//			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int whichButton) {
//				}
//			});
//		} else {
//			builder.setTitle("Add attributes");
//			builder.setView(EntryView);
//			builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int whichButton) {
//					String attributesID = "";
//					String attributesContext = "";
//					for(int i = 0 ; i <attributesList.size() ; i ++){
//						AttributesR bean = attributesList.get(i);
//						if(i >= attributesList.size()-1){
//							attributesID += bean.attributesRID;
//							attributesContext += bean.title;
//						}else{
//							attributesID += bean.attributesRID+",";
//							attributesContext += bean.title+",";
//						}
//					}
//					food.attributesID = attributesID;
//					food.attributesContext=attributesContext;
//					orderComponent.order(food);
//				}
//			});
//			builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int whichButton) {
//				}
//			});
//		}
//		AlertDialog dialog =builder.create();
//		dialog.show();
		if(list.size()!= 0){
		final MyAttrbutesDialog attrbutesDialog = new MyAttrbutesDialog(contextThemeWrapper);
		final AttrbutesGridViewAdapter adapter = new AttrbutesGridViewAdapter(context, type);
		adapter.setClassList(list);
		if(list.size()!= 0){
			attrbutesDialog.gridViewID.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			}
		attrbutesDialog.show();
		attrbutesDialog.dialog_yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				attrbutesDialog.dismiss();
				String attributesID = "";
				String attributesContext = "";
				for(int i = 0 ; i <adapter.attributesList.size() ; i ++){
					AttributesR bean = adapter.attributesList.get(i);
					if(i >= adapter.attributesList.size()-1){
						attributesID += bean.attributesRID;
						attributesContext += bean.title;
					}else{
						attributesID += bean.attributesRID+",";
						attributesContext += bean.title+",";
					}
				}
				food.attributesID = attributesID;
				food.attributesContext=attributesContext;
				orderComponent.order(food);
			}
		});
		attrbutesDialog.dialog_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attrbutesDialog.dismiss();
			}
		});
		}
	 }
	
	
	// 点菜操作
	@ItemClick(R.id.food_list)
	void foodPanel(int position) {
//		FoodR foodBean = foodDataList.get(position);
//		orderComponent.order(foodBean);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FoodListAdapter.LESS_DATALIST:
				int num = (Integer) msg.obj;
				orderComponent.remove(num);
				break;
			}

			super.handleMessage(msg);
		}

	};

	public List<FoodR> getFoodDataList() {
		return foodDataList;
	}

	public void setOrderComponent(OrderComponent orderComponent) {
		this.orderComponent = orderComponent;
	}

}
