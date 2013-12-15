package com.android.component;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
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
import com.android.singaporeanorderingsystem.DailyPayActivity_;
import com.android.singaporeanorderingsystem.MainActivity_;
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

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	ActivityComponent activityComponent;

	private PopupWindow popupWindow;

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
}
