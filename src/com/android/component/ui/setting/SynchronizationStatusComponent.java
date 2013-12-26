package com.android.component.ui.setting;

import android.content.Context;
import android.widget.TextView;

import com.android.R;
import com.android.component.StringResComponent;
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

	@AfterViews
	public void initSynchronizationStatus() {
		synchronize.setText(stringResComponent.syncErr);
	}

	// 同步菜单
	@Click(R.id.btu_setting_all_tong)
	void sync() {
	}

}
