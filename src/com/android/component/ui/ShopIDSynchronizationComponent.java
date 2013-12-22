package com.android.component.ui;

import android.content.Context;
import android.widget.EditText;

import com.android.R;
import com.android.common.MyApp;
import com.android.component.KeyboardComponent;
import com.android.component.SharedPreferencesComponent_;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@EBean
public class ShopIDSynchronizationComponent {

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

	public void synchronizeShopID() {
		keyboardComponent.dismissKeyboard(shopSet);
		String shopId = shopSet.getText().toString();
		myPrefs.shopId().put(shopId);
		toastComponent.show(stringResComponent.toastSettingSucc);
	}
}
