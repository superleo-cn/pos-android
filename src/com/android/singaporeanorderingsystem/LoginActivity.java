package com.android.singaporeanorderingsystem;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.R;
import com.android.bean.LoginUserBean;
import com.android.common.Constants;
import com.android.common.InfolabPasswordGen;
import com.android.common.MyApp;
import com.android.common.SystemHelper;
import com.android.dao.UserDao;
import com.android.handler.RemoteDataHandler;
import com.android.handler.RemoteDataHandler.Callback;
import com.android.model.ResponseData;

public class LoginActivity extends Activity implements OnClickListener{
	private Button ok_btn;
	private EditText login_name;
	private EditText login_password;
	private MyApp myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);
		myApp = (MyApp) LoginActivity.this.getApplication();
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
			final UserDao user_dao=myApp.getUserdao();
			LoginUserBean user_bean = user_dao.select(str_login_name);
			if(user_bean.getUsername() != null && str_login_password.equalsIgnoreCase(user_bean.getPasswrod())){
				Toast.makeText(LoginActivity.this,"恭喜你，shujuku登录成功", Toast.LENGTH_SHORT).show();
				Intent  intent =new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
				return ;
			}else if(user_bean.getUsername() != null && !str_login_password.equalsIgnoreCase(user_bean.getPasswrod())){
				Toast.makeText(LoginActivity.this, "登录失败,用户名或者密码错误", Toast.LENGTH_SHORT).show();
				return ;
			}else{
//				Toast.makeText(LoginActivity.this, "登录失败,用户名或者密码错误", Toast.LENGTH_SHORT).show();
//				return ;
			}
			String str_ip = "0";
			String str_mac = "0";
			try {
				str_ip=SystemHelper.getLocalIPAddress() == null ? "0":SystemHelper.getLocalIPAddress();
				str_mac=SystemHelper.getLocalMacAddress(LoginActivity.this)== null ? "0":SystemHelper.getLocalMacAddress(LoginActivity.this);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			HashMap<String, String> params =new HashMap<String, String>();
			params.put("user.username", str_login_name);
			params.put("user.password", str_login_password);
			params.put("user.userIp", str_ip);
			params.put("user.userMac", str_mac);
			RemoteDataHandler.asyncPost(Constants.URL_LOGIN_PATH, params, new Callback() {
				@Override
				public void dataLoaded(ResponseData data) {
					if(data.getCode() == 1){
						String json=data.getJson();
						ArrayList<LoginUserBean> datas=LoginUserBean.newInstanceList(json);
						LoginUserBean user_bean=datas.get(0);
						InfolabPasswordGen pass = new InfolabPasswordGen();
						pass.generatePassword();
			            user_bean.setPasswrod(pass.getPassword().toString());
						user_dao.insert(user_bean);
						Toast.makeText(LoginActivity.this,"恭喜你，登录成功,您的新密码是+"+pass.getPassword().toString(), Toast.LENGTH_SHORT).show();
						Intent  intent =new Intent();
						intent.setClass(LoginActivity.this, MainActivity.class);
						LoginActivity.this.startActivity(intent);
						LoginActivity.this.finish();
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
