package com.android.component.ui.main;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;

import com.android.R;
import com.android.adapter.FoodListAdapter;
import com.android.component.SharedPreferencesComponent_;
import com.android.domain.Food;
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

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	private List<Food> foodDataList;

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
		this.foodDataList = Food.queryList();
		String type = sharedPrefs.language().get();
		for (Food food : foodDataList) {
			if (StringUtils.equalsIgnoreCase(Locale.SIMPLIFIED_CHINESE.getLanguage(), type)) {
				food.title = food.nameZh;
			} else {
				food.title = food.name;
			}
		}
		FoodListAdapter adapter = new FoodListAdapter(context, foodDataList, handler);
		foodView.setAdapter(adapter);
	}

	// 点菜操作
	@ItemClick(R.id.food_list)
	void foodPanel(int position) {
		Food foodBean = foodDataList.get(position);
		orderComponent.order(foodBean);
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

	public List<Food> getFoodDataList() {
		return foodDataList;
	}

	public void setOrderComponent(OrderComponent orderComponent) {
		this.orderComponent = orderComponent;
	}

}
