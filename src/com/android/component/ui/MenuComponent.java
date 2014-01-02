package com.android.component.ui;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.R;
import com.android.activity.DailyPayActivity_;
import com.android.activity.MainActivity_;
import com.android.activity.QueryAllDBActivity_;
import com.android.activity.SettingActivity_;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.component.ActivityComponent;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.component.ui.login.LoginComponent;
import com.android.dialog.ConfirmDialog;
import com.android.dialog.MyProcessDialog;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
@EBean
public class MenuComponent {

	@RootContext
	Context context;

	@RootContext
	Activity activity;

	@App
	MyApp myApp;

	@ViewById(R.id.menu_btn)
	ImageView menu; // menu按钮

	@ViewById(R.id.login_name)
	TextView login_name; // 顶部菜单栏显示的用户名字

	@ViewById(R.id.shop_name)
	TextView shop_name; // 顶部菜单栏显示的店的名称

	@ViewById(R.id.textDailyPay)
	public TextView textDailyPay;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	ActivityComponent activityComponent;

	@Bean
	LoginComponent loginComponent;

	private PopupWindow popupWindow;

	MyProcessDialog dialog;

	/**
	 * 显示用户和点的名称
	 */
	@AfterViews
	public void initMenu() {
		login_name.setText(stringResComponent.mainTitle + " " + myApp.getUsername() + ",");
		shop_name.setText(myApp.getShopName() + "-" + myApp.getShopCode());
		dialog = new MyProcessDialog(context, stringResComponent.loading);
	}

	/**
	 * 弹出菜单
	 */
	public void initPopupWindow() {
		if (popupWindow == null) {
			View view = activity.getLayoutInflater().inflate(R.layout.popupwindow, null);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			popupWindow.setOutsideTouchable(true);
			TextView popu_setting = (TextView) view.findViewById(R.id.popu_setting);
			TextView popu_exit = (TextView) view.findViewById(R.id.popu_exit);
			TextView popu_daily = (TextView) view.findViewById(R.id.popu_daily);
			TextView popu_diancai = (TextView) view.findViewById(R.id.popu_diancai);
			TextView popu_QueryAllDB = (TextView) view.findViewById(R.id.popu_QueryAllDB);
			if (myApp.getUsername().equalsIgnoreCase(Constants.ROLE_SUPERADMIN)) {
				popu_QueryAllDB.setVisibility(View.VISIBLE);
			} else {
				popu_QueryAllDB.setVisibility(View.GONE);
			}
			if (myApp.getUserType().equalsIgnoreCase(Constants.ROLE_CASHIER)) {
				popu_setting.setVisibility(View.GONE);
			} else {
				popu_setting.setVisibility(View.VISIBLE);
			}

			(getHiddenView(view)).setVisibility(View.GONE);

			popu_diancai.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dismiss();
					new SwitchActivity(MainActivity_.class).execute();
				}
			});

			popu_setting.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
					if (!StringUtils.equalsIgnoreCase(Constants.ROLE_CASHIER, myApp.getUserType())) {
						dialog.show();
						new SwitchActivity(SettingActivity_.class).execute();
					} else {
						toastComponent.show(stringResComponent.insufficientpermissions);
					}
				}
			});

			popu_daily.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dismiss();
					new SwitchActivity(DailyPayActivity_.class).execute();
				}
			});
			popu_exit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
					buildExitDialog().show();
				}
			});
			popu_QueryAllDB.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					new SwitchActivity(QueryAllDBActivity_.class).execute();
				}
			});
		}
		dismiss();
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources()));
		popupWindow.showAsDropDown(menu, 0, -5);

	}

	public void textDaily() {
		textDailyPay.setText(stringResComponent.diancaiName);
	}

	@Click(R.id.textDailyPay)
	void textDailyPayOnClick() {
		if (textDailyPay.getText().toString().equals(stringResComponent.diancaiName)) {
			new SwitchActivity(MainActivity_.class).execute();
		} else {
			new SwitchActivity(DailyPayActivity_.class).execute();
		}
	}

	private void dismiss() {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	/**
	 * 切换时候，得到要隐藏掉的视图
	 * 
	 * @param view
	 * @return
	 */
	private View getHiddenView(View view) {
		TextView popu_daily = (TextView) view.findViewById(R.id.popu_daily);
		TextView popu_diancai = (TextView) view.findViewById(R.id.popu_diancai);
		TextView popu_setting = (TextView) view.findViewById(R.id.popu_setting);
		TextView popu_QueryAllDB = (TextView) view.findViewById(R.id.popu_QueryAllDB);
		if (activity instanceof MainActivity_) {
			return popu_diancai;
		} else if (activity instanceof DailyPayActivity_) {
			return popu_daily;
		} else if (activity instanceof SettingActivity_) {
			return popu_setting;
		} else {
			return popu_QueryAllDB;
		}
	}

	/**
	 * 退出系统操作
	 * 
	 * @return
	 */
	public Dialog buildExitDialog() {
		return new ConfirmDialog(context, stringResComponent.messageTitle, stringResComponent.messageExit) {

			@Override
			public void doClick() {
				loginComponent.executeLogout(myApp.getUserId(), myApp.getShopId());
				activityComponent.startLogin();
			}

		}.build();
	}

	class SwitchActivity extends AsyncTask<Class, Void, Integer> {

		private Class cls = null;

		public SwitchActivity(Class cls) {
			this.cls = cls;

		};

		@Override
		protected Integer doInBackground(Class... objs) {
			activityComponent.startActivity(cls);
			return Integer.MIN_VALUE;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
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
