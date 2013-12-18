package com.android.component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import android.content.Context;
import android.util.Log;

import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.domain.Audit;
import com.android.domain.User;
import com.android.mapping.StatusMapping;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
@EBean(scope = Scope.Singleton)
public class AuditComponent {

	@RootContext
	Context context;

	public void logAudit(User user, String action) {
		Audit audit = new Audit();
		audit.userId = user.uid;
		audit.shopId = user.shopId;
		audit.status = Constants.DB_FAILED;
		audit.action = action;
		audit.actionDate = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		audit.save();
		List<Audit> dates = Audit.queryByStatus(Constants.DB_FAILED);
		if (CollectionUtils.isNotEmpty(dates)) {
			HashMap<String, String> params = new HashMap<String, String>();
			for (int i = 0; i < dates.size(); i++) {
				Audit login_a_bean = dates.get(i);
				params.put("audits[" + i + "].androidId", String.valueOf(login_a_bean.getId()));
				params.put("audits[" + i + "].shop.id", login_a_bean.shopId);
				params.put("audits[" + i + "].user.id", login_a_bean.userId);
				params.put("audits[" + i + "].actionDate", login_a_bean.actionDate);
				params.put("audits[" + i + "].action", login_a_bean.action);
			}
			try {
				StatusMapping data = StatusMapping.postJSON(Constants.URL_LOGIN_AUDIT, params);
				if (data.code == Constants.STATUS_SUCCESS) {
					Audit.updateByStatus(Constants.DB_SUCCESS);
				} else if (data.code == Constants.STATUS_FAILED) {
					List<Long> ids = data.datas;
					if (CollectionUtils.isNotEmpty(ids)) {
						for (Long id : ids) {
							Audit.updateById(id, Constants.DB_SUCCESS);
						}
					}
				}
			} catch (Exception e) {
				Log.e("error", "LogMessage", e);
			}
		}
	}
}
