package com.android.component.ui;

import android.content.Context;
import android.widget.EditText;

import com.android.R;
import com.android.common.AndroidPrinter;
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

/**
 * 打印机IP设置
 * 
 * @author Administrator
 * 
 */
@EBean
public class PrintSetComponent {

	@Pref
	SharedPreferencesComponent_ myPrefs;

	@ViewById(R.id.print_one_edit)
	EditText printOneEdit;

	@Bean
	StringResComponent stringResComponent;

	@Bean
	ToastComponent toastComponent;

	@Bean
	AndroidPrinter androidPrinter;

	@Bean
	KeyboardComponent keyboardComponent;

	public void setPrintIP() {
		keyboardComponent.dismissKeyboard(printOneEdit);
		String ip = printOneEdit.getText().toString();
		myPrefs.printIp().put(ip);
		androidPrinter.reconnect();
		toastComponent.show(stringResComponent.toastSettingSucc);
	}

}
