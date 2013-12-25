/**
 * 
 */
package com.android.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

/**
 * @author Administrator
 * 
 */
public class MyService extends Service {
	WallpaperManager wManager;
	private String search_date;

	@Override
	public void onStart(Intent intent, int startId) {
		// new UpdateOperation().execute("");
		super.onStart(intent, startId);
	}

	private class UpdateOperation extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... objs) {
			/** 判断今天是否是最新的 */
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date = df.format(new Date());
			MyService.this.search_date = date;

			// if (!isLatestData()) {
			// post_diancai();
			// post_payList();
			// post_numList();
			// post_dailyMoney();
			// }
			System.out.println("闹钟执行中。。。");
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化WallpaperManager
		wManager = WallpaperManager.getInstance(MyService.this);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
