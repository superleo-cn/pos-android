/**
 * 
 */
package com.android.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.common.Constants;
import com.android.dao.DailyMoneyDao;
import com.android.dao.NumListDao;
import com.android.dao.PayListDao;
import com.android.handler.RemoteDataHandler;
import com.android.model.ResponseData;

/**
 * @author Administrator
 *
 */
public class MyService extends Service {  
    WallpaperManager wManager;  
	private String search_date;
    @Override  
    public void onStart(Intent intent,int startId)  
    {  
    	 /** 判断今天是否是最新的*/
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    	String date=df.format(new Date());
    	MyService.this.search_date=date;
    	
    	if(!isLatestData()){
    		post_payList();
	    	post_numList(); 
	    	post_dailyMoney();
    	}
    	System.out.println("闹钟执行中。。。");
        super.onStart(intent, startId);  
    }  
    @Override  
    public void onCreate()  
    {  
        super.onCreate();  
        //初始化WallpaperManager  
        wManager = WallpaperManager.getInstance(MyService.this);  
    }  
    @Override  
    public IBinder onBind(Intent arg0) {  
        return null;  
    }  
    /* 判断今天是否已经是最新数据 */
	public boolean isLatestData(){
		List<Map<String,String>> pays=PayListDao.getInatance(this).getList(search_date);
		if(!pays.isEmpty()){
			return false;
		}
		List<Map<String,String>> nums=NumListDao.getInatance(this).getList(search_date);
		if(!nums.isEmpty()){
			return false;
		}
		HashMap<String, String> params= DailyMoneyDao.getInatance(this).getList(search_date);
		if(!params.isEmpty()){
			return false;
		}
		return true;
	}
    /*提交每日支付*/
	public void post_payList(){
		System.out.println("每日支付提交++++");
		try{
		HashMap<String, String> params= new HashMap<String,String>();
		List<Map<String,String>> datas=PayListDao.getInatance(this).getList(search_date);
		if(!datas.isEmpty()){
			for(int i=0;i<datas.size();i++){
				if(datas.get(i).get("type").equals("0")){
				params.put("consumeTransactions["+i+"].androidId", datas.get(i).get("android_id"));
				Log.e("consumeTransactions["+i+"].androidId", datas.get(i).get("android_id"));
				params.put("consumeTransactions["+i+"].consumption.id", datas.get(i).get("consumption_id"));
				Log.e("consumeTransactions["+i+"].consumption.id", datas.get(i).get("consumption_id"));
				params.put("consumeTransactions["+i+"].shop.id", datas.get(i).get("shop_id"));
				Log.e("consumeTransactions["+i+"].shop.id", datas.get(i).get("shop_id"));
				params.put("consumeTransactions["+i+"].user.id", datas.get(i).get("user_id"));
				Log.e("consumeTransactions["+i+"].user.id", datas.get(i).get("user_id"));
				params.put("consumeTransactions["+i+"].price", datas.get(i).get("price"));
				Log.e("consumeTransactions["+i+"].price", datas.get(i).get("price"));
				}
			}
			
			ResponseData data = RemoteDataHandler.post(Constants.URL_POST_PAYLIST, params);
			if(data.getCode() == 1){
				String json=data.getJson();
			String str=json.substring(1,json.length()-1);
			String []array=str.split(",");
			if(array.length!=0){
				for(int i=0;i<array.length;i++){
					Log.e("数据组",array[i]+"");
				int result=	PayListDao.getInatance(MyService.this).update_type(array[i], "1");
				if(result==-1){
					//Toast.makeText(DailyPayActivity.this, "每日支付接口更新失败", Toast.LENGTH_SHORT).show();
				}else{
					//Toast.makeText(DailyPayActivity.this, "每日支付接口更新成功", Toast.LENGTH_SHORT).show();
				}
					
				}
			}
			}else if(data.getCode() == 0){
			}else if(data.getCode() == -1){
			}
		}
		
		}catch(Exception e){
			e.getMessage();
		}
	}
	
	/*提交带回总数*/
	public void post_numList(){
		System.out.println("每日带回总数++++");
		try{
			HashMap<String, String> params= new HashMap<String,String>();
			List<Map<String,String>> datas=NumListDao.getInatance(this).getList(search_date);
			if(!datas.isEmpty()){
				for(int i=0;i<datas.size();i++){
					if(datas.get(i).get("type").equals("0")){
					params.put("cashTransactions["+i+"].androidId", datas.get(i).get("android_id"));
					Log.e("cashTransactions["+i+"].androidId", datas.get(i).get("android_id"));
					params.put("cashTransactions["+i+"].cash.id", datas.get(i).get("cash_id"));
					Log.e("cashTransactions["+i+"].cash.id", datas.get(i).get("cash_id"));
					params.put("cashTransactions["+i+"].shop.id", datas.get(i).get("shop_id"));
					Log.e("cashTransactions["+i+"].shop.id", datas.get(i).get("shop_id"));
					params.put("cashTransactions["+i+"].user.id", datas.get(i).get("user_id"));
					Log.e("cashTransactions["+i+"].user.id", datas.get(i).get("user_id"));
					params.put("cashTransactions["+i+"].quantity", datas.get(i).get("quantity"));
					Log.e("cashTransactions["+i+"].quantity", datas.get(i).get("quantity"));
					}
				}
			}
			
			ResponseData data = RemoteDataHandler.post(Constants.URL_POST_TAKENUM, params);
			
			if(data.getCode() == 1){
				String json=data.getJson();
				//Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
				String str=json.substring(1,json.length()-1);
				String []array=str.split(",");
				if(array.length!=0){
					for(int i=0;i<array.length;i++){
						Log.e("数据组",array[i]+"");
						int result=	NumListDao.getInatance(MyService.this).update_type(array[i], "1");
						if(result==-1){
							//Toast.makeText(DailyPayActivity.this, "带回总数接口更新失败", Toast.LENGTH_SHORT).show();
						}else{
							//Toast.makeText(DailyPayActivity.this, "带回总数接口更新成功", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}else if(data.getCode() == 0){
			}else if(data.getCode() == -1){
			}
		
			
			}catch(Exception e){
			e.getMessage();
		}
	}
	
	/*提交每日营业额*/
	public void post_dailyMoney(){
		System.out.println("每日营业额++++");
		try{
			HashMap<String, String> params= DailyMoneyDao.getInatance(MyService.this).getList(search_date);
			ResponseData data = RemoteDataHandler.post(Constants.URL_POST_DAILY_MONEY, params);
			if(data.getCode() == 1){
				String json=data.getJson();
				//Toast.makeText(SettingActivity.this, getString(R.string.toast_submmit_succ)+json, Toast.LENGTH_SHORT).show();
				int result=DailyMoneyDao.getInatance(MyService.this).update_type(search_date);
				if(result==-1){
				}else{
				}
			}else if(data.getCode() == 0){
			}else if(data.getCode() == -1){
			}
		
		}catch(Exception e){
			e.getMessage();
		}
	}
}  
