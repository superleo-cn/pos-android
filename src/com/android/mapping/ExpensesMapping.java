package com.android.mapping;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.android.common.Constants;
import com.android.common.RestHelper;
import com.android.domain.Expenses;
import com.android.mapping.ExpensesMapping.ExpensesRemote;

public class ExpensesMapping extends BasicMapping<ExpensesRemote> {

	private static final ExpensesMapping mapping = new ExpensesMapping();

	public static class ExpensesRemote {

		public String id;

		public String name;

		public String nameZh;

		public String position;

	}

	public static ExpensesMapping getJSONAndSave(String url) {
		try {
			ExpensesMapping expensesMapping = RestHelper.getJSON(url, ExpensesMapping.class);
			if (expensesMapping != null && expensesMapping.code == Constants.STATUS_SUCCESS) {
				// 删除历史数据
				Expenses.deleteAll();
				List<ExpensesRemote> list = expensesMapping.datas;
				if (CollectionUtils.isNotEmpty(list)) {
					for (int i = 0; i < list.size(); i++) {
						ExpensesRemote expensesRemote = list.get(i);
						Expenses.save(expensesRemote);
					}
				}
			}
			return expensesMapping;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mapping;

	}

}
