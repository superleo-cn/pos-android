package com.android.singaporeanorderingsystem;
import java.util.HashMap;

import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;

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
			String url="http://ec2-54-254-145-129.ap-southeast-1.compute.amazonaws.com:8080/loginJson";
			HashMap<String, String> params =new HashMap<String, String>();
			params.put("user.username", str_login_name);
			params.put("user.password", str_login_password);
			params.put("user.userIp", "1111111");
			params.put("user.userMac", "22222222");
			RemoteDataHandler.asyncPost(url, params, new Callback() {
				@Override
				public void dataLoaded(ResponseData data) {
					if(data.getCode() == 1){
						String json=data.getJson();
						Toast.makeText(LoginActivity.this, "json-->"+json, Toast.LENGTH_SHORT).show();
					}else if(data.getCode() == 0){
						Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
					}else if(data.getCode() == -1){
						Toast.makeText(LoginActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
//			if(str_login_name.equals("admin") && str_login_password.equals("admin")){
//				Toast.makeText(LoginActivity.this,"恭喜你，登录成功", Toast.LENGTH_SHORT).show();
//				Intent  intent =new Intent();
//				intent.setClass(this, MainActivity.class);
//				startActivity(intent);
//				this.finish();
//			}else{
//				Toast.makeText(LoginActivity.this,"用户名或密码有误，稍后重试", Toast.LENGTH_SHORT).show();
//			}
			
			break;
		}
	}
	
}
