package com.android.component.ui.setting;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.android.R;
import com.android.common.Constants;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.component.WifiComponent;
import com.android.dialog.MyProcessDialog;
import com.android.mapping.ExpensesMapping;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 支付款项同步操作
 * 
 * @author superleo
 * 
 */
@EBean
public class ExpenseSynchronizationComponent {

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@ViewById(R.id.synchronization_pay_brn)
	Button synchronizatioPayBrn;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	@Bean
	WifiComponent wifiComponent;

	// 注入 Context 变量
	@RootContext
	Context context;

	MyProcessDialog dialog;

	@AfterInject
	public void initLogin() {
		dialog = new MyProcessDialog(context, stringResComponent.dialogSet);
	}

	// 同步菜单
	@Click(R.id.synchronization_pay_brn)
	void expensesSync() {
		if (wifiComponent.isConnected()) {
			String shopId = myPrefs.shopId().get();
			if (StringUtils.isEmpty(shopId) || StringUtils.equals(shopId, "0")) {
				toastComponent.show(stringResComponent.settingTanweiId);
				return;
			}
			new ExpenseSynchronizationTask().execute(shopId);
		} else {
			toastComponent.show(stringResComponent.wifiErr);
		}

	}

	private class ExpenseSynchronizationTask extends AsyncTask<String, Void, ExpensesMapping> {

		@Override
		protected ExpensesMapping doInBackground(String... objs) {
			String url = Constants.URL_PAY_DETAIL + objs[0];
			return ExpensesMapping.getJSONAndSave(url);
		}

		@Override
		protected void onPostExecute(ExpensesMapping mapping) {
			dialog.dismiss();
			switch (mapping.code) {
			case Constants.STATUS_FAILED:
				toastComponent.show(stringResComponent.toastSettingErr);
				break;
			case Constants.STATUS_SUCCESS:
				toastComponent.show(stringResComponent.toastSettingSucc);
				break;
			case Constants.STATUS_SERVER_FAILED:
				toastComponent.show(stringResComponent.serviceErr);
				break;
			case Constants.STATUS_NETWORK_ERROR:
				toastComponent.show(stringResComponent.wifiErr);
				break;
			}
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

}
