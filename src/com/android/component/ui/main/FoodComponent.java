package com.android.component.ui.main;

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
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.R;
import com.android.adapter.FoodListAdapter;
import com.android.component.SharedPreferencesComponent_;
import com.android.domain.CategoriesR;
import com.android.domain.Food;
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
		HorizontalScrollView horizontalScrollViewID = (HorizontalScrollView) leftView.findViewById(R.id.horizontalScrollViewID);
		LinearLayout linearLayout = new LinearLayout(context);
		for (FoodR food : foodDataList) {
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
		}
		
//		FoodListAdapter adapter = new FoodListAdapter(context, foodDataList, handler);
//		foodView.setAdapter(adapter);
		horizontalScrollViewID.addView(linearLayout);
		layout_left.addView(leftView);

		}
	}

	// 点菜操作
	@ItemClick(R.id.food_list)
	void foodPanel(int position) {
		FoodR foodBean = foodDataList.get(position);
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
