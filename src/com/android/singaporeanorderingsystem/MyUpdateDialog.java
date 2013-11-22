/**
 *  ClassName: MyProcessDialog.java
 *  created on 2012-3-17
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.android.singaporeanorderingsystem;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.R;


/**
 * 等待对话框
 * @author hjgang
 */
public class MyUpdateDialog extends Dialog {
	public Button dialog_yes;
	public Button dialog_no;
	public TextView dialog_message;
	public ProgressBar progress;
	public TextView shuzhi;
	public MyUpdateDialog(Context context) {
		super(context, R.style.MyProgressDialog);
		this.setContentView(R.layout.update_dialog);
		dialog_message =(TextView) findViewById(R.id.dialog_message);
		dialog_yes =(Button) findViewById(R.id.dialog_yes);
		dialog_no =(Button) findViewById(R.id.dialog_no);
		progress =(ProgressBar) findViewById(R.id.progress);
		shuzhi = (TextView) findViewById(R.id.shuzhi);
	}
}
