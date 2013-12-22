package com.android.singaporeanorderingsystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.R;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.ui.MenuComponent;
import com.android.dialog.DialogBuilder;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public abstract class AbsractActivity extends Activity {

	@Bean
	public MenuComponent menuComponent;

	// menu按钮
	@ViewById(R.id.menu_btn)
	ImageView menu;

	// 退出
	@ViewById(R.id.layout_exit)
	RelativeLayout exit_layout;

	@Pref
	public SharedPreferencesComponent_ sharedPrefs;

	/**
	 * @TODO: 最后放到基类里面去
	 * 
	 */
	@Click(R.id.menu_btn)
	public void menu() {
		menuComponent.initPopupWindow();
	}

	@Click(R.id.layout_exit)
	void layoutExitOnClick() {
		CreatedDialog().create().show();
	}

	public DialogBuilder CreatedDialog() {
		DialogBuilder builder = new DialogBuilder(this);
		builder.setTitle(R.string.message_title);
		builder.setMessage(R.string.message_exit);
		builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				new LogoutOperation().execute("");
			}
		});
		builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return builder;
	}

	private class LogoutOperation extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... objs) {
			logUserAction();
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

	public void logUserAction() {

	}

	/**
	 * 屏蔽回退按钮
	 */
	public void onBackPressed() {
		return;
	}

}
