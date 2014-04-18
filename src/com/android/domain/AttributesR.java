package com.android.domain;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.mapping.FoodAllMapping;

@Table(name = "tb_attributes")
public class AttributesR extends Model {
	
	@Column(name = "attributesRID")
	public String attributesRID;

	@Column(name = "name")
	public String name;

	@Column(name = "nameZh")
	public String nameZh;

	@Column(name = "code")
	public String code;

	@Column(name = "status")
	public String status;

	@Column(name = "position")
	public String position;

	@Column(name = "foodID")
	public String foodID;
	
	//非数据库字段
	public String title;
	

	@Override
	public String toString() {
		return "AttributesR [attributesRID=" + attributesRID + ", name=" + name
				+ ", nameZh=" + nameZh + ", code=" + code + ", status="
				+ status + ", position=" + position + ", foodID=" + foodID
				+ ", title=" + title + "]";
	}

	/**
	 * 返回列表
	 * 
	 * @return
	 */
	public static List<AttributesR> queryList() {
		return new Select().from(AttributesR.class).execute();
	}
	
	/**
	 * 返回列表
	 * 
	 * @return
	 */
	public static List<AttributesR> queryIDList(String foodID) {
		return new Select().from(AttributesR.class).where("foodID = ?",foodID).execute();
	}

	/**
	 * 保存
	 * 
	 * @param foodRemote
	 */
	public static void save(FoodAllMapping.RemoteAttribute attributesRemote) {
		// save Food
		AttributesR attributes = new AttributesR();
		attributes.attributesRID = attributesRemote.id;
		attributes.name = attributesRemote.name;
		attributes.nameZh = attributesRemote.nameZh;
		attributes.code = attributesRemote.code;
		attributes.status = attributesRemote.status;
		attributes.position = attributesRemote.position;
		attributes.foodID = attributesRemote.food.id;
		attributes.save();
	}

	/**
	 * 删除所有
	 */
	public static void deleteAll() {
		// save Food
		new Delete().from(AttributesR.class).execute();
	}

}
