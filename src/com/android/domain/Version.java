package com.android.domain;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "tb_version")
public class Version extends Model {

	@Column(name = "name")
	public String name;

	@Column(name = "version_no")
	public String versionNo;

	@Column(name = "description")
	public String description;

	@Column(name = "create_by")
	public String createBy;

	@Column(name = "create_date")
	public String createDate;

	/**
	 * 查询状态
	 * 
	 * @param status
	 * @return
	 */
	public static List<Version> queryByStatus(String versionNo) {
		return new Select().from(Version.class).where("versionNo = ?", versionNo).execute();
	}

}
