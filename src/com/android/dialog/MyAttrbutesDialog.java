package com.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.R;


/**
 * 等待对话框
 * 
 * @author HJGANG
 */
public class MyAttrbutesDialog extends Dialog {
	public Button dialog_yes;
	public Button dialog_no;
	public GridView gridViewID;
	public MyAttrbutesDialog(Context context) {
		super(context, R.style.MyProgressDialog);
		this.setContentView(R.layout.attributes_dialog);
		gridViewID = (GridView) findViewById(R.id.gridViewID);
		dialog_yes = (Button) findViewById(R.id.dialog_yes);
		dialog_no = (Button) findViewById(R.id.dialog_no);
	}
}
