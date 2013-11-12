/**
 * 
 */
package com.android.singaporeanorderingsystem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.adapter.DailyPayDetailAdapter;
import com.android.adapter.TakeNumerAdapter;
import com.android.bean.DailyPayDetailBean;
import com.android.bean.TakeNumberBean;
import com.android.dialog.DialogBuilder;

/**
 * @author jingang
 *
 */
public class DailyPayActivity extends Activity implements OnClickListener{
	private ListView daily_detail_list;
	private ListView daily_num_list;
	private List<DailyPayDetailBean> detail_classList;
	private List<TakeNumberBean> number_classList;
	private DailyPayDetailAdapter detail_adapter;
	private TakeNumerAdapter number_adapter;
	private int count=0;
	private TextView text_id_all_price;
	private ImageView daily_menu; //menu按钮
	private TextView daily_login_name; //用户名字
	private RelativeLayout daily_exit_layout; //退出
	private ImageView daily_wifi_image; //wifi 图标 
	private Button btu_id_sbumit;
	private PopupWindow popupWindow;
	private View view;
	private final int OPEN_WIFI=1006;
	private final int CLOSE_WIFI=1007;
	private EditText cash_register;
	private EditText today_turnover;
	private EditText noon_time;
	private EditText noon_turnover;
	private EditText time;
	private EditText total;
	private EditText tomorrow_money;
	private EditText total_take_num;
	private EditText send_person;
	private EditText other;
	private EditText shop_money;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_pay);
		initView();
		//init_wifiReceiver();
		btu_id_sbumit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreatedDialog().create().show();
			}
		});
	}
	 
	 public void initView(){
		 daily_menu=(ImageView) this.findViewById(R.id.daily_menu_btn);
			daily_menu.setOnClickListener(this);
			daily_login_name=(TextView) this.findViewById(R.id.daily_login_name);
			daily_exit_layout=(RelativeLayout) this.findViewById(R.id.daily_layout_exit);
			daily_exit_layout.setOnClickListener(this);
			daily_wifi_image=(ImageView) this.findViewById(R.id.daily_wifi_iamge);
			daily_detail_list = (ListView) findViewById(R.id.daily_detail_list);
			daily_num_list = (ListView) findViewById(R.id.daily_num_list);
			text_id_all_price = (TextView) findViewById(R.id.text_id_all_price);
			btu_id_sbumit = (Button) findViewById(R.id.btu_id_sbumit);
			
			cash_register=(EditText) this.findViewById(R.id.cash_register);
			today_turnover=(EditText) this.findViewById(R.id.today_turnover);
			noon_time=(EditText) this.findViewById(R.id.noon_time);
			noon_turnover=(EditText) this.findViewById(R.id.noon_turnover);
			time=(EditText) this.findViewById(R.id.time);
			total=(EditText) this.findViewById(R.id.total);
			tomorrow_money=(EditText) this.findViewById(R.id.tomorrow_money);
			total_take_num=(EditText) this.findViewById(R.id.total_take_num);
			send_person=(EditText) this.findViewById(R.id.send_person);
			other=(EditText) this.findViewById(R.id.other);
			shop_money=(EditText) this.findViewById(R.id.shop_money);
			initData();
	 }
	 
	 public void initData(){
		 detail_adapter = new DailyPayDetailAdapter(DailyPayActivity.this);
			number_adapter = new TakeNumerAdapter(DailyPayActivity.this);
			detail_classList = new ArrayList<DailyPayDetailBean>();
			number_classList = new ArrayList<TakeNumberBean>();
			
			for(int i=1 ; i < 10 ; i++){
				count+=(2*i);
				detail_classList.add(new DailyPayDetailBean("测试"+i, "$"+(2*i)));
			}
			detail_adapter.setClassList(detail_classList);
			daily_detail_list.setAdapter(detail_adapter);
			detail_adapter.notifyDataSetChanged();
			
			for(int i=1 ; i < 10 ; i++){
				number_classList.add(new TakeNumberBean("$"+(0.5*i),(2*i)+""));
			}
			number_adapter.setClassList(number_classList);
			daily_num_list.setAdapter(number_adapter);
			number_adapter.notifyDataSetChanged();
			
			//text_id_all_price.setText(count+"元");
	 }
	 
	 public void initPopupWindow() {
			if (popupWindow == null) {
				view = this.getLayoutInflater().inflate(
						R.layout.popupwindow, null);
				popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
			}
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
		}
	 public DialogBuilder CreatedDialog(){
			DialogBuilder builder=new DialogBuilder(this);
			builder.setTitle(R.string.message_title);
			builder.setMessage(R.string.message_zhifu);
			builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(DailyPayActivity.this, "你点击了确定", Toast.LENGTH_SHORT).show();
					 clear_data();
				}});
			builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(DailyPayActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
				}});
			return builder;
		}
	@Override
	public void onClick(View V) {
		switch(V.getId()){
		case R.id.daily_menu_btn:
			initPopupWindow();
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.showAsDropDown(daily_menu, 0, -5);
			break;
		case R.id.daily_layout_exit:
			DialogBuilder builder=new DialogBuilder(this);
			builder.setTitle(R.string.message_title);
			builder.setMessage(R.string.message_exit);
			builder.setPositiveButton(R.string.message_ok, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					System.exit(0);
				}});
			builder.setNegativeButton(R.string.message_cancle, new android.content.DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
				}});
			builder.create().show();
			
			break;
		}
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case OPEN_WIFI:
				daily_wifi_image.setImageResource(R.drawable.wifi_open);
				break;
			case CLOSE_WIFI:
				daily_wifi_image.setImageResource(R.drawable.wifi_close);
				break;
			}
		}
		
	};
	
	 private BroadcastReceiver myReceiver=new BroadcastReceiver()
	    {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				 String action = intent.getAction();
		            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo  mobInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo  wifiInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(!mobInfo.isConnected()&&!wifiInfo.isConnected()){
					new Thread(new Runnable(){

						public void run() {
							// TODO Auto-generated method stub
							Message msg = new Message();
							msg.what=CLOSE_WIFI;
							handler.sendMessage(msg);
						}
						
					}).start();
				
				}else{
					new Thread(new Runnable(){

						public void run() {
							// TODO Auto-generated method stub
							Message msg = new Message();
							msg.what=OPEN_WIFI;
							handler.sendMessage(msg);
						}
						
					}).start();
				}
			}
			}
	    };
	    private void init_wifiReceiver()
	    {
	    	IntentFilter filter=new IntentFilter();
	    	 filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	    	registerReceiver(myReceiver,filter);
	    }
	    public void  clear_data(){
	    	daily_detail_list.setAdapter(null);
	    	daily_num_list.setAdapter(null);
	    	cash_register.setText("");
	    	today_turnover.setText("");
	    	noon_time.setText("");
	    	noon_turnover.setText("");
	    	time.setText("");
	    	total.setText("");
	    	tomorrow_money.setText("");
	    	total_take_num.setText("");
	    	send_person.setText("");
	    	other.setText("");
	    	shop_money.setText("");
	    	text_id_all_price.setText("");
	    }
}
