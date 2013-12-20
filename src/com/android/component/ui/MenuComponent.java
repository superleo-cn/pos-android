package com.android.component.ui;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.R;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.component.ActivityComponent;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.dialog.DialogBuilder;
import com.android.singaporeanorderingsystem.DailyPayActivity_;
import com.android.singaporeanorderingsystem.MainActivity_;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
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

	@ViewById(R.id.shop_name1234)
	TextView shop_name1234; // 顶部菜单栏显示的店的名称

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	ActivityComponent activityComponent;

	private PopupWindow popupWindow;

	/**
	 * 显示用户和点的名称
	 */
	@AfterViews
	public void initMenu() {
		login_name.setText(stringResComponent.mainTitle + " " + myApp.getU_name() + ",");
		shop_name1234.setText(myApp.getShop_name() + "-" + myApp.getShop_code());
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

			(getHiddenView(view)).setVisibility(View.GONE);

			popu_diancai.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dismiss();
					activityComponent.startMainWithTransition();
				}
			});

			popu_setting.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
					if (!StringUtils.equalsIgnoreCase(Constants.ROLE_CASHIER, myApp.getU_type())) {
						activityComponent.startSettingWithTransition();
					} else {
						toastComponent.show(stringResComponent.insufficientpermissions);
					}
				}
			});

			popu_daily.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dismiss();
					activityComponent.startDailyWithTransition();
				}
			});
			popu_exit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
					createdDialog().create().show();
				}
			});
		}
		dismiss();
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources()));
		popupWindow.showAsDropDown(menu, 0, -5);

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
		if (activity instanceof MainActivity_) {
			return popu_diancai;
		} else if (activity instanceof DailyPayActivity_) {
			return popu_daily;
		} else {
			return popu_setting;
		}
	}

	/**
	 * 创建退出对话框
	 * 
	 * @return
	 */
	public DialogBuilder createdDialog() {
		DialogBuilder builder = new DialogBuilder(context);
		builder.setTitle(R.string.message_title);
		builder.setMessage(R.string.message_exit);
		builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				activityComponent.startLogin();
			}
		});
		builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return builder;
	}
}
