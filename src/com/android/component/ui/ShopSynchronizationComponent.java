package com.android.component.ui;

import android.widget.EditText;

import com.android.R;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
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
		myPrefs.shopId().put(shopId);
		dissmissKeyboard();
		toastComponent.show(stringResComponent.toastSettingSucc);
	}

	public void dissmissKeyboard() {
		keyboardComponent.dismissKeyboard(shopSet);
	}
}
