package com.android.singaporeanorderingsystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.bean.GetPTakeNumBean;
import com.android.bean.GetPayDetailBean;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;
import com.android.component.ActivityComponent;
import com.android.component.LanguageComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ui.DiscountSetComponent;
import com.android.component.ui.ExpenseSynchronizationComponent;
import com.android.component.ui.FoodSynchronizationComponent;
import com.android.component.ui.LanguageSetComponent;
import com.android.component.ui.PrintSetComponent;
import com.android.component.ui.ResetPasswordComponent;
import com.android.component.ui.ShopSynchronizationComponent;
import com.android.component.ui.TimeSetComponent;
import com.android.dao.DailyMoneyDao;
import com.android.dao.GetTakeNumDao;
import com.android.dao.NumListDao;
import com.android.dao.PayListDao;
import com.android.dao.getDetailPayListDao;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;
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

	@ViewById(R.id.menu_btn)
	ImageView menu;

	@Bean
	ActivityComponent activityComponent;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	LanguageComponent languageComponent;

	@Bean
	TimeSetComponent timeSetComponent;

	@Bean
	PrintSetComponent printSetComponent;

	@Bean
	DiscountSetComponent discountSetComponent;

	@Bean
	ShopSynchronizationComponent shopSynchronizationComponent;

	@Bean
	LanguageSetComponent languageSetComponent;

	@Bean
	FoodSynchronizationComponent foodSynchronizationComponent;

	@Bean
	ExpenseSynchronizationComponent expenseSynchronizationComponent;

	@Bean
	ResetPasswordComponent resetPasswordComponent;

	@App
	MyApp myApp;

	private MyProcessDialog dialog;
	private String search_date;

	@Pref
	SharedPreferencesComponent_ sharedPrefs;

	// public static String type;

	@AfterViews
	public void init() {

		dialog = new MyProcessDialog(this, stringResComponent.dialogSet);

		/** 判断今天是否是最新的 */
		search_date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);
		if (!isLatestData()) {
			synchronize.setText(getString(R.string.sync_err));
		} else {
			synchronize.setText(getString(R.string.sync_succ));
		}
		/***/

		if (myApp.getU_type().equals("SUPERADMIN")) {
			admin_set.setVisibility(View.VISIBLE);
			r_set_admin_lay.setVisibility(View.VISIBLE);
			setting_time.setVisibility(View.VISIBLE);
		}

		// 支付款同步
		synchronization_pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.show();
				getDetailPayListDao.getInatance(SettingActivity.this).delete();
				RemoteDataHandler.asyncGet(Constants.URL_PAY_DETAIL + myApp.getSettingShopId(), new Callback() {
					@Override
					public void dataLoaded(ResponseData data) {
						if (data.getCode() == 1) {
							String json = data.getJson();
							Log.e("返回数据", json);
							ArrayList<GetPayDetailBean> datas = GetPayDetailBean.newInstanceList(json);
							Log.e("支付页详情数据", datas.size() + "");
							for (int i = 0; i < datas.size(); i++) {
								GetPayDetailBean bean = datas.get(i);
								getDetailPayListDao.getInatance(SettingActivity.this).save(bean.getId(), bean.getName(), bean.getNameZh());
							}
							dialog.cancel();
							Toast.makeText(SettingActivity.this, getString(R.string.toast_setting_succ), Toast.LENGTH_SHORT).show();
						} else if (data.getCode() == 0) {
							Toast.makeText(SettingActivity.this, "支付页失败", Toast.LENGTH_SHORT).show();
						} else if (data.getCode() == -1) {
							Toast.makeText(SettingActivity.this, getString(R.string.login_service_err), Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});

	}

	/***********************************************************************************/

	/* 判断今天是否已经是最新数据 */
	public boolean isLatestData() {
		List<Map<String, String>> pays = PayListDao.getInatance(this).getList(search_date);
		if (!pays.isEmpty()) {
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
