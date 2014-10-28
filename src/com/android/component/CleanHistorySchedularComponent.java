package com.android.component;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import android.content.Context;

import com.android.domain.Audit;
import com.android.domain.BalanceOrder;
import com.android.domain.CollectionOrder;
import com.android.domain.ExpensesOrder;
import com.android.domain.FoodOrder;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 自动同步组件
 * 
 * @author superleo
 * 
 */
// 定义成一个可以注入的组件
@EBean
public class CleanHistorySchedularComponent {

	// 注入 Context 变量
	@RootContext
	Context context;

	@Pref
	SharedPreferencesComponent_ myPrefs;

	public void cleanHistory() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Date dateBefore7Days = cal.getTime();
		String time = DateFormatUtils.format(dateBefore7Days, "yyyy-MM-dd");

		BalanceOrder.deleteByDate(time);
		Audit.deleteByDate(time);
		CollectionOrder.deleteByDate(time);
		ExpensesOrder.deleteByDate(time);
		FoodOrder.deleteByDate(time);
	}
}
