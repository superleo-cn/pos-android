package com.android.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;
import com.android.mapping.CollectionMapping.CollectionRemote;

@Table(name = "tb_collection")
public class Collection extends Model {

	@Column(name = "collectionID")
	public String collectionID;

	@Column(name = "price")
	public String price;

	@Column(name = "status")
	public String status;

	@Column(name = "date")
	public String date;


	@Override
	public String toString() {
		return "Collection [collectionID=" + collectionID + ", price=" + price + ", status=" + status + ", date=" + date + "]";
	}

	public static void save(CollectionRemote bean) {
		Collection c_order = new Collection();
		c_order.status = Constants.DB_FAILED;// 是否成功 1是 0否
		c_order.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		c_order.price = bean.price;//
		c_order.collectionID = bean.id;
		c_order.save();
	}

	/**
	 * 返回列表
	 * 
	 * @return
	 */
	public static List<Collection> queryList() {
		return new Select().from(Collection.class).execute();
	}

	/**
	 * 返回订单列表
	 * 
	 * @return
	 */
	public static List<Collection> queryListByStatus(String status) {
		return new Select().from(Collection.class).where("status = ?", status).execute();
	}

	/**
	 * 返回今天数据列表
	 * 
	 * @return
	 */
	public static List<Collection> TodayStatusList(String time, String status) {
		return new Select().from(Collection.class).where("date = ? and status=?", time, status).execute();
	}
	/**
	 * 删除所有带回总数
	 */
	public static void deleteAll() {
		new Delete().from(Collection.class).execute();
	}

}
