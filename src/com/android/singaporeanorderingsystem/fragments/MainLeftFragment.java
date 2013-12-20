package com.android.singaporeanorderingsystem.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;

import com.android.R;
import com.android.adapter.FoodListAdapter;
import com.android.bean.FoodListBean;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.ui.OrderComponent;
import com.android.domain.Food;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment(R.layout.main_left_fragment)
public class MainLeftFragment extends Fragment {

	Context context;

	@ViewById(R.id.food_list)
	GridView foodView; // 菜品列表

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	private List<FoodListBean> foodDataList;

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
		context = getActivity().getApplicationContext();
		this.foodDataList = new ArrayList<FoodListBean>();
		String type = sharedPrefs.language().get();
		List<Food> datas = Food.queryList();
		for (Food food : datas) {
			FoodListBean bean = new FoodListBean();
			if (StringUtils.equalsIgnoreCase("zh", type)) {
				bean.setTitle(food.nameZh);
			} else {
				bean.setTitle(food.name);
			}
			bean.setDaping_id(food.sn);
			bean.setImageID(food.picture);
			bean.setType(food.type);
			bean.setFood_id(food.foodId);
			bean.setPrice(food.retailPrice);
			foodDataList.add(bean);
		}

		FoodListAdapter adapter = new FoodListAdapter(this.context, foodDataList, handler);
		foodView.setAdapter(adapter);
	}

	// 点菜操作
	@ItemClick(R.id.food_list)
	void foodPanel(int position) {
		FoodListBean foodBean = foodDataList.get(position);
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

	public List<FoodListBean> getFoodDataList() {
		return foodDataList;
	}

	public void setOrderComponent(OrderComponent orderComponent) {
		this.orderComponent = orderComponent;
	}

}
