/**
 *  ClassName: MyProcessDialog.java
 *  created on 2012-3-17
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.R;
import com.android.dialog.design.DialogBuilder;

/**
 * 确定对话框
 * 
 * @author hjgang
 */
public abstract class ConfirmDialog {

	private Context context;
	private String title;
	private String message;

	public ConfirmDialog(Context context, String title, String message) {
		this.context = context;
		this.title = title;
		this.message = message;
	}

	public abstract void doClick();

	public Dialog build() {
		DialogBuilder builder = new DialogBuilder(context);
		builder.setTitle(message);
		builder.setMessage(title);
		builder.setPositiveButton(R.string.message_ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				doClick();
			}
		});
		builder.setNegativeButton(R.string.message_cancle, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 不做任何操作
			}
		});
		return builder.create();
	}

}
