package com.android.dialog;

import com.android.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 订单号输入对话框
 * 
 * @author rw
 */
public class MyOrderDialog extends Dialog{
	
	public Button dialog_yes, dialog_cancel;
	public EditText order_edt;
	public TextView order_msg_text;

	public MyOrderDialog(Context context) {
		super(context, R.style.MyProgressDialog);
		this.setContentView(R.layout.myorderdialog);
		dialog_yes = (Button) findViewById(R.id.dialog_yes);
		dialog_cancel = (Button) findViewById(R.id.dialog_cancel);
		order_edt = (EditText) findViewById(R.id.order_edt);
		order_msg_text = (TextView) findViewById(R.id.order_msg_text);
	}

}
