package com.android.component.ui.setting;

import org.apache.commons.lang.StringUtils;

import android.widget.EditText;

import com.android.R;
import com.android.common.Constants;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.android.mapping.ShopMapping;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 店铺ID同步操作
 * 
 * @author superleo
 * 
 */
@EBean
public class ShopSynchronizationComponent {

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@ViewById(R.id.shop_set)
	EditText shopSet;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	@ViewById(R.id.shop_set)
	EditText shopSset;

	@AfterViews
	public void initFoodSynchronization() {
		shopSset.setText(myPrefs.shopId().get());
	}

	@Click(R.id.synchronization_shop_brn)
	public void synchronizeSHopID() {
		String shopId = shopSet.getText().toString();
		ShopMapping mapping = ShopMapping.getJSON(Constants.URL_SHOP_PATH + shopId);
		if(mapping != null && mapping.code == Constants.STATUS_SUCCESS){
			myPrefs.shopId().put(shopId);
			toastComponent.show(stringResComponent.toastSettingSucc);
		}else{
			toastComponent.show(stringResComponent.toastSettingShopFail);
		}
		dissmissKeyboard();
	}

	public void dissmissKeyboard() {
		keyboardComponent.dismissKeyboard(shopSet);
	}
}
