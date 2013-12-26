package com.android.component.ui;

import java.util.List;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;

import com.android.R;
import com.android.adapter.BalanceQueryAllDBListviewAdapter;
import com.android.adapter.CollectionQueryAllDBListviewAdapter;
import com.android.adapter.ExpensesQueryAllDBListviewAdapter;
import com.android.adapter.FoodQueryAllDBListviewAdapter;
import com.android.common.Constants;
import com.android.component.ToastComponent;
import com.android.domain.BalanceOrder;
import com.android.domain.CollectionOrder;
import com.android.domain.ExpensesOrder;
import com.android.domain.FoodOrder;
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

	@AfterViews
	void init() {
		dbLoadingComponent.queryFoodLoading(pangeno);
	}

	@Click(R.id.button_id_shang)
	void shangClick(){
		if(pangeno ==1){
			toastComponent.show("当前为第一页");
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
	
	@Click(R.id.button_id_xia)
	void xiaClick(){
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
	
	
	@Click(R.id.buttonFoodOrder)
	void QueryFoodOrderClick() {
		viewFlag="food";
		pangeno = 1;
		dbLoadingComponent.queryFoodLoading(pangeno);
	}

	@Click(R.id.buttonExpensesOrder)
	void QueryExpensesOrderClick() {
		viewFlag="expenses";
		pangeno = 1;
		dbLoadingComponent.queryExpensesLoading(pangeno);
	}

	@Click(R.id.buttonCollectionOrder)
	void QueryCollectionOrderClick() {
		viewFlag="collection";
		pangeno = 1;
		dbLoadingComponent.queryCollectionLoading(pangeno);
	}

	@Click(R.id.buttonBalanceOrder)
	void QueryBalanceOrderClick() {
		viewFlag="balance";
		pangeno = 1;
		dbLoadingComponent.queryBalanceLoading(pangeno);
	}
}
