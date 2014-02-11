/**
 * 
 */
package com.android.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.android.common.DateUtils;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.component.WifiComponent;
import com.android.component.ui.daily.DailypaySubmitComponent;
import com.android.component.ui.main.OrderComponent;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * @author Administrator
 * 
 */

@EService
public class MyService extends Service {

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@Bean
	DailypaySubmitComponent dailypaySubmitComponent;

	@Bean
	OrderComponent orderComponent;

	@Bean
	WifiComponent wifiComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	StringResComponent stringResComponent;

	// run on another Thread to avoid crash
	private Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// cancel if already existed
		if (mTimer != null) {
			mTimer.cancel();
		} else {
			// recreate new
			mTimer = new Timer();
		}
		// schedule task
		final long time = myPrefs.time().get() * 60 * 1000;
		mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 60 * 1000, time);
	}

	class TimeDisplayTimerTask extends TimerTask {

		@Override
		public void run() {
			// run on another thread
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// display toast
					if (wifiComponent.isConnected()) {
						try {
							dailypaySubmitComponent.submitAll();
							orderComponent.submitAll();
							// toastComponent.show(stringResComponent.allSyncSucc);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						// toastComponent.show(stringResComponent.allSyncErr);
					}
				}

			});
		}

	}
}