package com.android.component.ui.setting;

import org.apache.commons.lang.StringUtils;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.R;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * 权限设置
 * 
 * @author Administrator
 * 
 */
@EBean
public class AccessRightComponent {

	@ViewById(R.id.r_set_admin_lay)
	RelativeLayout r_set_admin_lay;

	@ViewById(R.id.admin_set)
	TextView admin_set;

	@ViewById(R.id.btu_setting_time)
	Button btu_setting_time;

	@ViewById(R.id.setting_time)
	RelativeLayout setting_time;

	@App
	MyApp myApp;

	@AfterViews
	public void initAccessRight() {

		if (StringUtils.equalsIgnoreCase(myApp.getUserType(), Constants.ROLE_SUPERADMIN)) {
			admin_set.setVisibility(View.VISIBLE);
			r_set_admin_lay.setVisibility(View.VISIBLE);
			setting_time.setVisibility(View.VISIBLE);
		}

	}
}
