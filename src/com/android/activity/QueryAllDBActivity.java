/**
 * 
 */
package com.android.activity;
import com.android.R;
import com.android.component.ui.QueryAllDBAComponent;

import android.app.Activity;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;

/**
 * @author jingang
 *
 */
//不需要标题
@NoTitle
//全屏显示
@Fullscreen
//绑定登录的layout
@EActivity(R.layout.query_all_db)
public class QueryAllDBActivity extends Activity{
	
	@Bean
	QueryAllDBAComponent queryAllDBAComponent;

}
