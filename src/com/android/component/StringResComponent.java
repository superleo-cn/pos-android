package com.android.component;

import com.android.R;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.res.StringRes;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
// 定义成一个可以注入的组件
@EBean
public class StringResComponent {

	// 登录界面的用到的
	@StringRes(R.string.login_wait)
	public String loginWait;

	@StringRes(R.string.logout_wait)
	public String logoutWait;

	@StringRes(R.string.login_quanxian)
	public String loginQuanxian;

	@StringRes(R.string.login_fail)
	public String loginFail;

	@StringRes(R.string.login_succ)
	public String loginSucc;

	@StringRes(R.string.wifi_err)
	public String wifiErr;

	@StringRes(R.string.service_err)
	public String serviceErr;

	// 主页用到的
	@StringRes(R.string.insufficientpermissions)
	public String insufficientpermissions;

	@StringRes(R.string.dialog_set)
	public String dialogSet;

	@StringRes(R.string.toast_setting_succ)
	public String toastSettingSucc;

	@StringRes(R.string.toast_setting_err)
	public String toastSettingErr;

	@StringRes(R.string.err_price)
	public String errPrice;

	@StringRes(R.string.open_print)
	public String openPrint;

	@StringRes(R.string.mainTitle_txt)
	public String mainTitle;

	@StringRes(R.string.selec_not_food)
	public String noFood;
	
	@StringRes(R.string.food_package)
	public String foodPackage;

	// 设置页面用到的
	@StringRes(R.string.toast_setting_language_succ)
	public String toastSettingLanguageSucc;

	@StringRes(R.string.message_title)
	public String messageTitle;

	@StringRes(R.string.message_exit)
	public String messageExit;

	@StringRes(R.string.message_zhifu)
	public String messageZhifu;

	@StringRes(R.string.setting_tanwei_id)
	public String settingTanweiId;

	@StringRes(R.string.dialy_submit_error1)
	public String dialy_submit_error1;

	@StringRes(R.string.dialy_submit_error2)
	public String dialy_submit_error2;

	@StringRes(R.string.dialy_submit_error3)
	public String dialy_submit_error3;

	@StringRes(R.string.err_price)
	public String err_price;

	@StringRes(R.string.no_need_sync)
	public String noNeedSync;

	@StringRes(R.string.sync_succ)
	public String syncSucc;

	@StringRes(R.string.sync_err)
	public String syncErr;

	@StringRes(R.string.all_sync_err)
	public String allSyncErr;

	@StringRes(R.string.all_sync_succ)
	public String allSyncSucc;
	
	@StringRes(R.string.diancai_name)
	public String diancaiName;
	
	@StringRes(R.string.daily_pay)
	public String dailyPay;
	
}
