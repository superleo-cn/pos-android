package com.android.component.ui.setting;

import android.os.AsyncTask;

import com.android.R;
import com.android.common.MyApp;
import com.android.component.KeyboardComponent;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;

/**
 * 重置密码操作
 * 
 * @author superleo
 * 
 */
@EBean
public class TransactionSynchronizationComponent {

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	@App
	MyApp myApp;

	// 查询用户名
	@Click(R.id.btu_setting_all_tong)
	void syncAllTransactions() {
		new SyncALlOperation().execute("");
	}

	private class SyncALlOperation extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... objs) {
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 0:
				toastComponent.show(stringResComponent.noNeedSync);
				break;
			case 1:
				toastComponent.show(stringResComponent.syncSucc);
				break;
			case -1:
				toastComponent.show(stringResComponent.syncErr);
				break;
			}
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

}
