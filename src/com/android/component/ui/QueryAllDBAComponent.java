package com.android.component.ui;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.R;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.component.ToastComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

@EBean
public class QueryAllDBAComponent {
	// 注入 Context 变量
	@RootContext
	Context context;

	@ViewById(R.id.buttonFoodOrder)
	Button buttonFoodOrder;

	@ViewById(R.id.buttonExpensesOrder)
	Button buttonExpensesOrder;

	@ViewById(R.id.buttonCollectionOrder)
	Button buttonCollectionOrder;

	@ViewById(R.id.buttonBalanceOrder)
	Button buttonBalanceOrder;
	
	@ViewById(R.id.button_id_shang)
	Button button_id_shang;
	
	@ViewById(R.id.button_id_xia)
	Button button_id_xia;
	
	@Bean
	QueryAllDBLoadingComponent dbLoadingComponent;
	
	@Bean ToastComponent toastComponent;
	
	private int pangeno = 1;
	private String viewFlag="food";
	private String startTime="开始时间";
	private String endTime="结束时间";
	private String foodStatus="全部";
	
	private Calendar calendar=Calendar.getInstance();
	
	@ViewById(R.id.textStatusID)
	TextView textStatusID;
	
	@ViewById(R.id.startTimeID)
	TextView startTimeID;
	
	@ViewById(R.id.endTimeID)
	TextView endTimeID;
	
	@ViewById(R.id.buttonSearchID)
	Button buttonSearchID;
	
	
	@AfterViews
	void init() {
		dbLoadingComponent.queryFoodLoading(pangeno);
	}

	@Click(R.id.textStatusID)
	void textStatusOnClick(){
		showPopWindow(context);
	}

	
	private void showPopWindow(Context context) {
		final PopupWindow pw;

		View myView;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		myView = inflater.inflate(R.layout.status_pop_window, null);
		final TextView textStatusAllID=(TextView) myView.findViewById(R.id.textStatusAllID);
		final TextView textStatusSuccessID=(TextView) myView.findViewById(R.id.textStatusSuccessID);
		final TextView textStatusFailID=(TextView) myView.findViewById(R.id.textStatusFailID);
		pw = new PopupWindow(myView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true); 
		pw.setOutsideTouchable(true); 
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.showAtLocation( myView, textStatusID.getHeight(), 
				10, textStatusID.getHeight()+20);
		pw.showAsDropDown(textStatusID);
		textStatusAllID.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
				textStatusID.setText("全部");
			}
		});
		textStatusSuccessID.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
				textStatusID.setText("成功");
			}
		});
		textStatusFailID.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
				textStatusID.setText("失败");
			}
		});
	}
	
	@Click(R.id.buttonSearchID)
	void buttonStatusIDOnClick(){
		String StatusContext=textStatusID.getText().toString();
		String startTimeContext=startTimeID.getText().toString();
		String endTimeContext=endTimeID.getText().toString();
//		DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
//		yyyy-MM-dd hh:mm:ss
		judge(startTimeContext, endTimeContext, StatusContext);
	}
	public void judge(String startTimeContext,String endTimeContext,String StatusContext){
		if(!startTimeContext.equals("开始时间") && !endTimeContext.equals("开始时间")){
			startTime=startTimeContext;endTime=endTimeContext;
			if(StatusContext.contains("全部")){
				foodStatus ="全部";
				dbLoadingComponent.queryFoodLoading(pangeno,startTimeContext,endTimeContext);
			}else if(StatusContext.contains("成功")){
				foodStatus = "成功";
				dbLoadingComponent.queryFoodLoading(pangeno,Constants.STATUS_SUCCESS+"",startTimeContext,endTimeContext);
			}else if(StatusContext.contains("失败")){
				foodStatus = "失败";
				dbLoadingComponent.queryFoodLoading(pangeno,Constants.STATUS_FAILED+"",startTimeContext,endTimeContext);
			}
		}else{
			if(StatusContext.contains("全部")){
				foodStatus ="全部";
				dbLoadingComponent.queryFoodLoading(pangeno);
			}else if(StatusContext.contains("成功")){
				foodStatus = "成功";
				dbLoadingComponent.queryFoodLoading(pangeno,Constants.STATUS_SUCCESS+"");
			}else if(StatusContext.contains("失败")){
				foodStatus = "失败";
				dbLoadingComponent.queryFoodLoading(pangeno,Constants.STATUS_FAILED+"");
			}
		}
	}
	@Click(R.id.startTimeID)
	void startTimeIDOnClick(){
		DatePickerDialog dialog = new DatePickerDialog(context,
	                new DatePickerDialog.OnDateSetListener() {
	                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
	                    	startTimeID.setText(year + "-" + (month+1) + "-" + dayOfMonth + "");
	                    }
	                }, 
	                calendar.get(Calendar.YEAR), // 传入年份
	                calendar.get(Calendar.MONTH), // 传入月份
	                calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
	            );
		dialog.show();
	}
	
	@Click(R.id.endTimeID)
	void endTimeIDOnClick(){
		DatePickerDialog dialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                    	endTimeID.setText(year + "-" + (month+1) + "-" + dayOfMonth + "");
                    }
                }, 
                calendar.get(Calendar.YEAR), // 传入年份
                calendar.get(Calendar.MONTH), // 传入月份
                calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
            );
	dialog.show();
	}
	
	@Click(R.id.button_id_shang)
	void shangClick(){
		if(pangeno ==1){
			toastComponent.show("当前为第一页");
		}else{
			if(!startTime.equals("开始时间") || !endTime.equals("结束时间") || !foodStatus.equals("全部")){
				judge(startTime, endTime, startTime);
			}else{
					pangeno --;
					if(viewFlag.equals("food")){
						dbLoadingComponent.queryFoodLoading(pangeno);
					}else if(viewFlag.equals("expenses")){
						dbLoadingComponent.queryFoodLoading(pangeno);
					}else if(viewFlag.equals("collection")){
						dbLoadingComponent.queryFoodLoading(pangeno);
					}else if(viewFlag.equals("balance")){
						dbLoadingComponent.queryFoodLoading(pangeno);
					}
			}
		}
	}
	
	@Click(R.id.button_id_xia)
	void xiaClick(){
		if(!startTime.equals("开始时间") || !endTime.equals("结束时间") || !foodStatus.equals("全部")){
			judge(startTime, endTime, startTime);
		}else{
			if(viewFlag.equals("food")){
				if(pangeno*Constants.PARAM_PAGESIZE >= dbLoadingComponent.queryFoodGetCount()){
					toastComponent.show("当前为尾页");
				}else{
					pangeno ++;
					dbLoadingComponent.queryFoodLoading(pangeno);
				}
			}else if(viewFlag.equals("expenses")){
				dbLoadingComponent.queryFoodLoading(pangeno);
			}else if(viewFlag.equals("collection")){
				dbLoadingComponent.queryFoodLoading(pangeno);
			}else if(viewFlag.equals("balance")){
				dbLoadingComponent.queryFoodLoading(pangeno);
			}
		}
	}
	
	
	@Click(R.id.buttonFoodOrder)
	void QueryFoodOrderClick() {
		viewFlag="food";
		pangeno = 1;
		startTime="开始时间";
		endTime="结束时间";
		foodStatus = "全部";
		dbLoadingComponent.queryFoodLoading(pangeno);
	}

	@Click(R.id.buttonExpensesOrder)
	void QueryExpensesOrderClick() {
		viewFlag="expenses";
		pangeno = 1;
		startTime="开始时间";
		endTime="结束时间";
		foodStatus = "全部";
		dbLoadingComponent.queryExpensesLoading(pangeno);
	}

	@Click(R.id.buttonCollectionOrder)
	void QueryCollectionOrderClick() {
		viewFlag="collection";
		pangeno = 1;
		startTime="开始时间";
		endTime="结束时间";
		foodStatus = "全部";
		dbLoadingComponent.queryCollectionLoading(pangeno);
	}

	@Click(R.id.buttonBalanceOrder)
	void QueryBalanceOrderClick() {
		viewFlag="balance";
		pangeno = 1;
		startTime="开始时间";
		endTime="结束时间";
		foodStatus = "全部";
		dbLoadingComponent.queryBalanceLoading(pangeno);
	}
	
}
