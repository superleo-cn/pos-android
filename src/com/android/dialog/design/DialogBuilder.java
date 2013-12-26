package com.android.dialog.design;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogBuilder extends AlertDialog.Builder {

	private IDialog md;
	private Context context;

	public DialogBuilder(Context context) {
		super(context);
		md = new IDialog(context);
		this.context = context;
	}

	public DialogBuilder setMessage(int messageId) {
		md.setMessage(context.getResources().getString(messageId));
		return this;
	}

	public DialogBuilder setMessage(CharSequence message) {
		md.setMessage(message);
		return this;
	}

	public DialogBuilder setTitle(int titleId) {
		md.setTitle(context.getResources().getString(titleId));
		return this;
	}

	public DialogBuilder setTitle(CharSequence title) {
		md.setTitle(title);
		return this;
	}

	// 认同按钮
	public DialogBuilder setPositiveButton(int textId,
			OnClickListener listener) {
		md.setButton(context.getResources().getString(textId), listener);
		return this;
	}

	// 认同按钮
	public DialogBuilder setPositiveButton(CharSequence text,
			OnClickListener listener) {
		md.setButton(text, listener);
		return this;
	}

	// 中立按钮
	public DialogBuilder setNeutralButton(int textId,
			OnClickListener listener) {
		md.setButton2(context.getResources().getString(textId), listener);
		return this;
	}

	// 中立按钮
	public DialogBuilder setNeutralButton(CharSequence text,
			OnClickListener listener) {
		md.setButton2(text, listener);
		return this;
	}

	// 否定按钮
	public DialogBuilder setNegativeButton(int textId,
			OnClickListener listener) {
		md.setButton3(context.getResources().getString(textId), listener);
		return this;
	}

	// 否定按钮
	public DialogBuilder setNegativeButton(CharSequence text,
			OnClickListener listener) {
		md.setButton3(text, listener);
		return this;
	}

	@Override
	public IDialog create() {
		return md;
	}
}