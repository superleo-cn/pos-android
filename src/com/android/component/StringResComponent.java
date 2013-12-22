package com.android.component;

import com.android.R;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.api.Scope;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
// 定义成一个可以注入的组件
@EBean(scope = Scope.Singleton)
public class StringResComponent {

	// 登录界面的用到的
	@StringRes(R.string.login_wait)
	public String loginWait;

	@StringRes(R.string.login_quanxian)
	public String loginQuanxian;

	@StringRes(R.string.login_fail)
	public String loginFail;

	@StringRes(R.string.login_succ)
	public String loginSucc;

	@StringRes(R.string.login_service_err)
	public String loginServiceErr;

	@StringRes(R.string.login_wifi_err)
	public String loginWifiErr;

	// 主页用到的
	@StringRes(R.string.insufficientpermissions)
	public String insufficientpermissions;

	@StringRes(R.string.dialog_set)
	public String dialogSet;

	@StringRes(R.string.toast_setting_succ)
	public String toastSettingSucc;

	@StringRes(R.string.err_price)
	public String errPrice;

	@StringRes(R.string.open_print)
	public String openPrint;

	@StringRes(R.string.mainTitle_txt)
	public String mainTitle;

	@StringRes(R.string.selec_not_food)
	public String noFood;

	// 设置页面用到的
	@StringRes(R.string.toast_setting_language_succ)
	public String toastSettingLanguageSucc;

	@StringRes(R.string.message_title)
	public String messageTitle;

	@StringRes(R.string.setting_tanwei_id)
	public String settingTanweiId;

}
