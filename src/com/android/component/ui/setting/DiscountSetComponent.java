package com.android.component.ui.setting;

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
 * 打折设置
 * 
 * @author Administrator
 * 
 */
@EBean
public class DiscountSetComponent {

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@ViewById(R.id.take_price_edit)
	EditText takePriceEdit;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	KeyboardComponent keyboardComponent;

	// 初始化数据
	@AfterViews
	public void initDiscountSet() {
		takePriceEdit.setText(myPrefs.discount().get());
	}

	@Click(R.id.btu_discount)
	public void setDiscount() {
		String textDiscount = takePriceEdit.getText().toString();
		myPrefs.discount().put(textDiscount);
		dissmissKeyboard();
		toastComponent.show(stringResComponent.toastSettingSucc);
	}

	public void dissmissKeyboard() {
		keyboardComponent.dismissKeyboard(takePriceEdit);
	}

}
