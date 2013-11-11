package com.android.singaporeanorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	
	private Button ok_btn;
	private EditText login_name;
	private EditText login_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);
		ok_btn=(Button) this.findViewById(R.id.login_ok);
		login_name = (EditText) LoginActivity.this.findViewById(R.id.login_name);
		login_password = (EditText) LoginActivity.this.findViewById(R.id.login_password);
		ok_btn.setOnClickListener(LoginActivity.this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login_ok :
			String str_login_name=login_name.getText().toString();
			String str_login_password=login_password.getText().toString();
			if(str_login_name.equals("admin") && str_login_password.equals("admin")){
				Toast.makeText(LoginActivity.this,"恭喜你，登录成功", Toast.LENGTH_SHORT).show();
				Intent  intent =new Intent();
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
				this.finish();
			}else{
				Toast.makeText(LoginActivity.this,"用户名或密码有误，稍后重试", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
	
}
