package com.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.R;


/**
 * 等待对话框
 * 
 * @author HJGANG
 */
public class MyDialog extends Dialog {
	public Button dialog_yes;
	public Button dialog_no;
	public TextView dialog_message;
	public TextView dialog_title;
	public TextView textDialogAllMoenyID;
	public TextView textDialogSearchMoenyID;
	public TextView textDialogAllMoenyTitleID;
	public TextView textDialogSearchMoenyTitleID;
	public TextView textDialogGSTMoenyID;
	public TextView textDialogSCMoenyID;
	public TextView textDialogSumMoenyID;
	public LinearLayout linearlayoutID;
	public MyDialog(Context context) {
		super(context, R.style.MyProgressDialog);
		this.setContentView(R.layout.mydialog);
		linearlayoutID = (LinearLayout) findViewById(R.id.linearlayoutID);
		dialog_message = (TextView) findViewById(R.id.dialog_message);
		textDialogAllMoenyID = (TextView) findViewById(R.id.textDialogAllMoenyID);
		textDialogSearchMoenyID = (TextView) findViewById(R.id.textDialogSearchMoenyID);
		textDialogAllMoenyTitleID = (TextView) findViewById(R.id.textDialogAllMoenyTitleID);
		textDialogSearchMoenyTitleID = (TextView) findViewById(R.id.textDialogSearchMoenyTitleID);
		textDialogGSTMoenyID = (TextView) findViewById(R.id.textDialogGSTMoenyID);
		textDialogSCMoenyID = (TextView) findViewById(R.id.textDialogSCMoenyID);
		textDialogSumMoenyID = (TextView) findViewById(R.id.textDialogSumMoenyID);
		dialog_yes = (Button) findViewById(R.id.dialog_yes);
		dialog_no = (Button) findViewById(R.id.dialog_no);
		dialog_title  = (TextView) findViewById(R.id.dialog_title);
	}
}
