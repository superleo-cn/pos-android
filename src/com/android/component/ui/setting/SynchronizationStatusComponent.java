package com.android.component.ui.setting;

import java.util.Date;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.android.R;
import com.android.common.DateUtils;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.component.WifiComponent;
import com.android.component.ui.daily.DailypaySubmitComponent;
import com.android.dialog.MyProcessDialog;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * 查询是否全部同步成功
 * 
 * @author superleo
 * 
 */
@EBean
public class SynchronizationStatusComponent {

	@RootContext
	Context context;

	@ViewById(R.id.synchronizeText)
	TextView synchronize;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	DailypaySubmitComponent dailypaySubmitComponent;

	@Bean
	WifiComponent wifiComponent;

	// 是否已经是最新数据
	boolean flag = false;

	// 今天的日期
	String today = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD);

	MyProcessDialog dialog;

	@AfterViews
	public void initSynchronizationStatus() {
		dialog = new MyProcessDialog(context, stringResComponent.loginWait);
		/* 判断今天是否已经是最新数据 */
		setStatus();
	}

	/**
	 * 设置同步状态
	 */
	public void setStatus() {
		if (dailypaySubmitComponent.isCompleted(today)) {
			synchronize.setText(stringResComponent.syncSucc);
			flag = true;
		} else {
			synchronize.setText(stringResComponent.syncErr);
			flag = false;
		}
	}

	// 同步菜单
	@Click(R.id.btu_setting_all_tong)
	void sync() {
		if (wifiComponent.isConnected()) {
			if (flag) {
				toastComponent.show(stringResComponent.noNeedSync);
			} else {
				new SynchronizationTask().execute(today);
			}
		} else {
			toastComponent.show(stringResComponent.wifiErr);
		}
	}

	private class SynchronizationTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... objs) {
			try {
				dailypaySubmitComponent.submitAll(objs[0]);
				return true;
			} catch (Exception ex) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			dialog.dismiss();
			if (flag) {
				toastComponent.show(stringResComponent.syncSucc);
			} else {
				toastComponent.show(stringResComponent.syncErr);
			}
			setStatus();
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
