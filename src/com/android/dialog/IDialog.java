package com.android.dialog;





import com.android.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * 主对话框[仿Iphone风格]
 *
 */
public class IDialog extends AlertDialog {

	private DialogView view;
	private LayoutInflater mInflater;
	private Context context;

	protected IDialog(Context context) {
		super(context);
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (DialogView) mInflater.inflate(R.layout.dialog, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
	}

	@Override
	public void setMessage(CharSequence message) {
		view.setMessage(message);
	}

	@Override
	public void setTitle(CharSequence title) {
		view.setTitle(title);
	}

	@Override
	public void setButton(CharSequence text, final OnClickListener listener) {
		final Button button = (Button) view.findViewById(R.id.dialog_yes);
		button.setText(text);
		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				if(listener!=null){
					listener.onClick(IDialog.this, 0);
					dismiss();
					}
			}
		});

		super.setButton(text, listener);
	}

	@Override
	public void setButton2(CharSequence text, final OnClickListener listener) {
		final Button button = (Button) view.findViewById(R.id.dialog_no);
		button.setText(text);
		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				if(listener!=null){
				listener.onClick(IDialog.this, 0);
				dismiss();
				}
			}
		});
		super.setButton2(text, listener);
	}

	@Override
	public void setButton3(CharSequence text, final OnClickListener listener) {
		final Button button = (Button) view.findViewById(R.id.dialog_cancel);
		button.setText(text);
		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				listener.onClick(IDialog.this, 0);
				dismiss();
			}
		});
		super.setButton3(text, listener);
	}
}
