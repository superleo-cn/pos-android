package com.android.activity;

import android.view.View;
import android.widget.ImageView;

import com.android.R;
import com.android.component.ui.query.QueryAllDBAComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author jingang
 * 
 */
// 不需要标题
@NoTitle
// 全屏显示
@Fullscreen
// 绑定登录的layout
@EActivity(R.layout.query_all_db)
public class QueryAllDBActivity extends BasicActivity {
	@Bean
	QueryAllDBAComponent queryAllDBAComponent;
	
	@ViewById(R.id.orderId_circle)
	ImageView orderId_circle;
	
	@AfterViews
	public void init(){
		orderId_circle.setVisibility(View.GONE);
	}
}
