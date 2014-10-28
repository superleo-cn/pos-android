package com.android.domain;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.common.Constants;

@Table(name = "tb_audit")
public class Audit extends Model {

	@Column(name = "shop_id")
	public String shopId;

	@Column(name = "user_id")
	public String userId;

	@Column(name = "action")
	public String action;

	@Column(name = "actionDate")
	public String actionDate;

	@Column(name = "status")
	public String status;

	/**
	 * 查询状态
	 * 
	 * @param status
	 * @return
	 */
	public static List<Audit> queryByStatus(String status) {
		return new Select().from(Audit.class).where("status = ?", status).execute();
	}

	/**
	 * 更新状态位
	 * 
	 * @param status
	 */
	public static void updateByStatus(String status) {
		List<Audit> audits = queryByStatus(Constants.DB_FAILED);
		if (CollectionUtils.isNotEmpty(audits)) {
			for (Audit audit : audits) {
				audit.status = status;
				audit.save();
			}
		}
	}

	public static void updateById(Long id, String status) {
		Audit audit = Audit.load(Audit.class, id);
		audit.status = status;
		audit.save();
	}

	/**
	 * 删除历史数据
	 * 
	 * @param time
	 */
	public static void deleteByDate(String time) {
		new Delete().from(Audit.class).where("actionDate < ?", time).execute();
	}
}
