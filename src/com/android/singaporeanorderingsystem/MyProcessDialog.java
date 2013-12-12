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
import android.widget.TextView;

import com.android.R;


/**
 * 等待对话框
 * 
 * @author hjgang
 */
public class MyProcessDialog extends Dialog {
	public MyProcessDialog(Context context, String str) {
		super(context, R.style.MyProgressDialog);
		this.setContentView(R.layout.progress_dialog);
		TextView text = (TextView) this.findViewById(R.id.txt_wait);
		text.setText(str);
	}
}
