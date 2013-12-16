package com.android.component.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.Handler;
import android.widget.GridView;

import com.android.adapter.FoodListAdapter;
import com.android.bean.FoodListBean;
import com.android.component.SharedPreferencesComponent_;
import com.android.domain.Food;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;

/**
 * 键盘操作方法
 * 
 * @author superleo
 * 
 */
@EBean(scope = Scope.Singleton)
public class FoodComponent {

	@RootContext
	Context context;

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	private List<FoodListBean> foodDataList;

	public List<FoodListBean> getFoodDataList() {
		return foodDataList;
	}

	/**
	 * 要强制屏蔽键盘的组件
	 * 
	 * @param objs
	 *            不定参数,可以传入任意数量的参数
	 */
	public void init(GridView foodView, Handler handler) {
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

		FoodListAdapter adapter = new FoodListAdapter(context, foodDataList, handler);
		foodView.setAdapter(adapter);
	}

}
