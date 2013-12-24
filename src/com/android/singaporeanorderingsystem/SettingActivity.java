package com.android.singaporeanorderingsystem;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.R;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ui.CollectionSynchronizationComponent;
import com.android.component.ui.DiscountSetComponent;
import com.android.component.ui.ExpenseSynchronizationComponent;
import com.android.component.ui.FoodSynchronizationComponent;
import com.android.component.ui.LanguageSetComponent;
import com.android.component.ui.PrintSetComponent;
import com.android.component.ui.ResetPasswordComponent;
import com.android.component.ui.ShopSynchronizationComponent;
import com.android.component.ui.TimeSetComponent;
import com.android.dao.DailyMoneyDao;
import com.android.dao.NumListDao;
import com.android.domain.CollectionOrder;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

//不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.setting)
public class SettingActivity extends AbstractActivity {

	@ViewById(R.id.synchronization_pay_brn)
	Button synchronization_pay;

	@ViewById(R.id.admin_set)
	TextView admin_set;

	@ViewById(R.id.r_set_admin_lay)
	RelativeLayout r_set_admin_lay;

	@ViewById(R.id.btu_setting_time)
	Button btu_setting_time;

	@ViewById(R.id.setting_time)
	RelativeLayout setting_time;

	@ViewById(R.id.synchronizeText)
	TextView synchronize;

	@Bean
	StringResComponent stringResComponent;

	// 时间组件
	@Bean
	TimeSetComponent timeSetComponent;

	// 打印机组件
	@Bean
	PrintSetComponent printSetComponent;

	// 打折组件
	@Bean
	DiscountSetComponent discountSetComponent;

	// 摊位组件
	@Bean
	ShopSynchronizationComponent shopSynchronizationComponent;

	// 语言组件
	@Bean
	LanguageSetComponent languageSetComponent;

	// 食物组件
	@Bean
	FoodSynchronizationComponent foodSynchronizationComponent;

	// 支付款项组件
	@Bean
	ExpenseSynchronizationComponent expenseSynchronizationComponent;

	// 带回总数组件
	@Bean
	CollectionSynchronizationComponent collectionSynchronizationComponent;

	// 设置密码
	@Bean
	ResetPasswordComponent resetPasswordComponent;

	@App
	MyApp myApp;

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	private MyProcessDialog dialog;

	// public static String type;

	@AfterViews
	public void init() {

		dialog = new MyProcessDialog(this, stringResComponent.dialogSet);

		/** 判断今天是否是最新的 */
		if (!isLatestUpdate()) {
			synchronize.setText(stringResComponent.syncErr);
		} else {
			synchronize.setText(stringResComponent.syncSucc);
		}

		if (StringUtils.equalsIgnoreCase(myApp.getU_type(), Constants.ROLE_SUPERADMIN)) {
			admin_set.setVisibility(View.VISIBLE);
			r_set_admin_lay.setVisibility(View.VISIBLE);
			setting_time.setVisibility(View.VISIBLE);
		}

	}

	/***********************************************************************************/

	/* 判断今天是否已经是最新数据 */
	public boolean isLatestUpdate() {

		String search_date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);

		List<CollectionOrder> pays = CollectionOrder.queryListByDate(search_date);
		if (CollectionUtils.isEmpty(pays)) {
			return false;
		}

		List<Map<String, String>> nums = NumListDao.getInatance(this).getList(search_date);
		if (!nums.isEmpty()) {
			return false;
		}

		HashMap<String, String> params = DailyMoneyDao.getInatance(SettingActivity.this).getList(search_date);
		if (!params.isEmpty()) {
			return false;
		}
		return true;

	}

}
