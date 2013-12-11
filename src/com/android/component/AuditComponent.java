package com.android.component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.android.bean.LoginAuditBean;
import com.android.bean.LoginUserBean;
import com.android.common.Constants;
import com.android.dao.LoginAuditDao;
import com.android.handler.RemoteDataHandler;
import com.android.model.ResponseData;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
public class AuditComponent {

	private Context context;

	public AuditComponent(Context context) {
		this.context = context;
	}

	public void login_audit(LoginUserBean login_user, String action) {
		final LoginAuditDao dao = LoginAuditDao.getInatance(context);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		LoginAuditBean login_audit = new LoginAuditBean();
		login_audit.setUser_id(login_user.getId());
		login_audit.setShop_id(login_user.getShop_id());
		login_audit.setActionDate(df.format(new Date()));
		login_audit.setAction(action);
		login_audit.setFood_flag("0");
		dao.save(login_audit);
		ArrayList<LoginAuditBean> u_datas = dao.getList("0");
		if (u_datas != null && u_datas.size() != 0) {
			HashMap<String, String> params = new HashMap<String, String>();
			for (int i = 0; i < u_datas.size(); i++) {
				LoginAuditBean login_a_bean = u_datas.get(i);
				params.put("audits[" + i + "].androidId", login_a_bean.getAndroid_id());
				params.put("audits[" + i + "].shop.id", login_a_bean.getShop_id());
				params.put("audits[" + i + "].user.id", login_a_bean.getUser_id());
				params.put("audits[" + i + "].actionDate", login_a_bean.getActionDate());
				params.put("audits[" + i + "].action", login_a_bean.getAction());
			}
			try {
				ResponseData data = RemoteDataHandler.post(Constants.URL_LOGIN_AUDIT, params);
				if (data.getCode() == 1) {
					dao.update_all_type("0");
				} else if (data.getCode() == 0) {
					String json = data.getJson();
					json = json.replaceAll("\\[", "");
					json = json.replaceAll("\\]", "");
					String[] str = json.split(",");
					for (int i = 0; i < str.length; i++) {
						dao.update_type(str[i]);
					}
				} else if (data.getCode() == -1) {

				}
			} catch (Exception e) {
				Log.e("error", "LogMessage", e);
			}
		}
	}

}
