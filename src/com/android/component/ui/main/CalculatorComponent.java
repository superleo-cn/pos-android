package com.android.component.ui.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import android.content.Context;
import android.widget.GridView;
import android.widget.TextView;

import com.android.R;
import com.android.adapter.GiditNumberAdapter;
import com.android.common.Constants;
import com.android.common.MyNumberUtils;
import com.android.component.StringResComponent;
import com.android.component.ToastComponent;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * 计算器组件
 * 
 * @author superleo
 * 
 */
@EBean
public class CalculatorComponent {

	@RootContext
	Context context;

	@Bean
	ToastComponent toastComponent;

	@Bean
	StringResComponent stringResComponent;

	@ViewById(R.id.digit_btn)
	GridView calucatorView; // 0-9按钮 用gridView做的按钮

	OrderComponent orderComponent;

	/**
	 * 初始化计算器
	 * 
	 * @param calucatorView
	 * @param handler
	 */
	@AfterViews
	public void initCalculator() {
		List<String> dataList = new ArrayList<String>();
		String[] str = new String[] { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ".", "C" };
		for (int i = 0; i < str.length; i++) {
			dataList.add(str[i]);
		}
		GiditNumberAdapter adapter = new GiditNumberAdapter(context, dataList);
		calucatorView.setAdapter(adapter);
	}

	/**
	 * 计算输入金额
	 * 
	 * @param sbuff
	 * @param position
	 */
	public void calculation(StringBuffer sbuff, int position) {
		switch (position) {
		case 0:
			calulation(sbuff, "7", orderComponent.getGathering());
			break;
		case 1:
			calulation(sbuff, "8", orderComponent.getGathering());
			break;
		case 2:
			calulation(sbuff, "9", orderComponent.getGathering());
			break;
		case 3:
			calulation(sbuff, "4", orderComponent.getGathering());
			break;
		case 4:
			calulation(sbuff, "5", orderComponent.getGathering());
			break;
		case 5:
			calulation(sbuff, "6", orderComponent.getGathering());
			break;
		case 6:
			calulation(sbuff, "1", orderComponent.getGathering());
			break;
		case 7:
			calulation(sbuff, "2", orderComponent.getGathering());
			break;
		case 8:
			calulation(sbuff, "3", orderComponent.getGathering());
			break;
		case 9:
			calulation(sbuff, "0", orderComponent.getGathering());
			break;
		case 10:
			calulation(sbuff, ".", orderComponent.getGathering());
			break;
		case 11:
			int sb_length = sbuff.length();
			sbuff.delete(0, sb_length);
			orderComponent.getGathering().setText("0.00");
			orderComponent.getSurplus().setText("0.00");
			break;
		}

	}

	/**
	 * 开始计算
	 * 
	 * @param sbuff
	 * @param appendStr
	 * @param gathering
	 */
	private void calulation(StringBuffer sbuff, String appendStr, TextView gathering) {
		sbuff.append(appendStr);
		String number = sbuff.toString();
		if (NumberUtils.isNumber(number)) {
			if (StringUtils.indexOf(number, ".") > -1) {
				sbuff = new StringBuffer(StringUtils.substring(number, 0, number.indexOf(".") + 3));
			}
			if (is_maxPrice(sbuff)) {
				gathering.setText(Constants.MAX_PRICE);
			} else {
				try {
					gathering.setText(MyNumberUtils.numToStr(Double.parseDouble(sbuff.toString().trim())));
				} catch (Exception e) {
					toastComponent.show(stringResComponent.errPrice);
				}
			}
			compute_surplus();
		} else {
			toastComponent.show(stringResComponent.errPrice);
		}
	}

	/**
	 * 计算应该找回的款项
	 */
	public void compute_surplus() {
		try {
			Double get_gathering = Double.parseDouble(orderComponent.getGathering().getText().toString());
			Double get_total_price = Double.parseDouble(orderComponent.getTotalPrice().getText().toString());
			orderComponent.getSurplus().setText(MyNumberUtils.numToStr(get_gathering - get_total_price));
		} catch (Exception e) {
			toastComponent.show(stringResComponent.errPrice);
		}

	}

	/**
	 * 判断输入值是否超过最大值
	 * 
	 * @param sbuff
	 * @return
	 */
	public boolean is_maxPrice(StringBuffer sbuff) {
		try {
			Double now_price = Double.parseDouble(sbuff.toString().trim());
			if (now_price > Constants.MAX_NUM_PRICE) {
				return true;
			}
		} catch (Exception e) {
			toastComponent.show(stringResComponent.errPrice);
			return false;
		}
		return false;

	}

	public void setOrderComponent(OrderComponent orderComponent) {
		this.orderComponent = orderComponent;
	}

}
