package com.android.singaporeanorderingsystem;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.R;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;
import com.android.component.StringResComponent;
import com.android.component.ui.setting.CollectionSynchronizationComponent;
import com.android.component.ui.setting.DiscountSetComponent;
import com.android.component.ui.setting.ExpenseSynchronizationComponent;
import com.android.component.ui.setting.FoodSynchronizationComponent;
import com.android.component.ui.setting.LanguageSetComponent;
import com.android.component.ui.setting.PrintSetComponent;
import com.android.component.ui.setting.ResetPasswordComponent;
import com.android.component.ui.setting.ShopSynchronizationComponent;
import com.android.component.ui.setting.SynchronizationStatusComponent;
import com.android.component.ui.setting.TimeSetComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

//不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.setting)
public class SettingActivity extends AbstractActivity {

	@ViewById(R.id.r_set_admin_lay)
	RelativeLayout r_set_admin_lay;

	@ViewById(R.id.admin_set)
	TextView admin_set;

	@ViewById(R.id.btu_setting_time)
	Button btu_setting_time;

	@ViewById(R.id.setting_time)
	RelativeLayout setting_time;

	@Bean
	StringResComponent stringResComponent;

	// 状态同步组件
	@Bean
	SynchronizationStatusComponent synchronizationStatusComponent;

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

	@AfterViews
	public void initSetting() {

		if (StringUtils.equalsIgnoreCase(myApp.getUserType(), Constants.ROLE_SUPERADMIN)) {
			admin_set.setVisibility(View.VISIBLE);
			r_set_admin_lay.setVisibility(View.VISIBLE);
			setting_time.setVisibility(View.VISIBLE);
		}

	}

	/***********************************************************************************/

	/* 判断今天是否已经是最新数据 */
	public boolean isLatestUpdate() {

		String search_date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);

		// List<CollectionOrder> pays =
		// CollectionOrder.queryListByDate(search_date);
		// if (CollectionUtils.isEmpty(pays)) {
		// return false;
		// }
		//
		// List<Map<String, String>> nums =
		// NumListDao.getInatance(this).getList(search_date);
		// if (!nums.isEmpty()) {
		// return false;
		// }
		//
		// HashMap<String, String> params =
		// DailyMoneyDao.getInatance(SettingActivity.this).getList(search_date);
		// if (!params.isEmpty()) {
		// return false;
		// }
		return true;

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		timeSetComponent.dissmissKeyboard();
		printSetComponent.dissmissKeyboard();
		discountSetComponent.dissmissKeyboard();
		shopSynchronizationComponent.dissmissKeyboard();
		languageSetComponent.dissmissKeyboard();
		resetPasswordComponent.dissmissKeyboard();
		return super.dispatchTouchEvent(event);
	}

}
