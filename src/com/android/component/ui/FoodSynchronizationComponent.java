package com.android.component.ui;

import org.apache.commons.lang.StringUtils;

import com.android.R;
import com.android.common.Constants;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.mapping.FoodMapping;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 店铺ID同步操作
 * 
 * @author superleo
 * 
 */
@EBean
public class FoodSynchronizationComponent {

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	// 同步菜单
	@Click(R.id.synchronization_menu_brn)
	void foodSync() {
		String shopId = myPrefs.shopId().get();
		if (StringUtils.isEmpty(shopId) || StringUtils.equals(shopId, "0")) {
			toastComponent.show(stringResComponent.settingTanweiId);
			return;
		}
		String url = Constants.URL_FOODSLIST_PATH + shopId;
		FoodMapping.getJSONAndSave(url);
		toastComponent.show(stringResComponent.toastSettingSucc);
	}

}
