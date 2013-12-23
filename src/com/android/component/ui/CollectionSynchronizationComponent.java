package com.android.component.ui;

import org.apache.commons.lang.StringUtils;

import android.widget.Button;

import com.android.R;
import com.android.common.Constants;
import com.android.common.MyApp;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.mapping.CollectionMapping;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 支付款项同步操作
 * 
 * @author superleo
 * 
 */
@EBean
public class CollectionSynchronizationComponent {

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@ViewById(R.id.synchronization_pay_brn)
	Button synchronizatioPayBrn;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	@App
	MyApp myApp;

	// 同步菜单
	@Click(R.id.price_set_brn)
	void collectionSync() {
		System.out.println("myApp.getSettingShopId()-->" + myApp.getSettingShopId());
		if (StringUtils.isNotEmpty(myApp.getSettingShopId()) || StringUtils.equals(myApp.getSettingShopId(), "0")) {
			toastComponent.show(stringResComponent.settingTanweiId);
			return;
		}
		String url = Constants.URL_TAKE_DNUM + myApp.getSettingShopId();
		CollectionMapping.getJSONAndSave(url);
		toastComponent.show(stringResComponent.toastSettingSucc);
	}

}
