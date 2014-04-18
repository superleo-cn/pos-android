package com.android.domain;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.mapping.FoodAllMapping;

@Table(name = "tb_categories")
public class CategoriesR extends Model {
	
	@Column(name = "categoriesId")
	public String categoriesId;

	@Column(name = "status")
	public String status;

	@Column(name = "name")
	public String name;

	@Column(name = "nameZh")
	public String nameZh;

	@Column(name = "position")
	public String position;

	@Column(name = "code")
	public String code;
	
	//非数据库字段
	public String title;
	

	@Override
	public String toString() {
		return "CategoriesR [categoriesId=" + categoriesId + ", status="
				+ status + ", name=" + name + ", nameZh=" + nameZh
				+ ", position=" + position + ", code=" + code + "]";
	}

	/**
	 * 返回列表
	 * 
	 * @return
	 */
	public static List<CategoriesR> queryList() {
		return new Select().from(CategoriesR.class).execute();
	}

	/**
	 * 保存
	 * 
	 * @param foodRemote
	 */
	public static void save(FoodAllMapping.CategoryRemote categoryRemote) {
		// save Food
		CategoriesR categories = new CategoriesR();
		categories.categoriesId= categoryRemote.id;
		categories.name = categoryRemote.name;
		categories.nameZh = categoryRemote.nameZh;
		categories.code = categoryRemote.code;
		categories.status = categoryRemote.status;
		categories.position = categoryRemote.position;
		categories.save();
	}

	/**
	 * 删除所有
	 */
	public static void deleteAll() {
		// save Food
		new Delete().from(CategoriesR.class).execute();
	}

}
