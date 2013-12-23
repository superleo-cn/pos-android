package com.android.domain;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "tb_collection_order")
public class CollectionOrder extends Model {

	@Column(name = "shop_id")
	public String shopId;

	@Column(name = "user_id")
	public String userId;

	@Column(name = "quantity")
	public String quantity;

	@Column(name = "cache_id")
	public String cacheId;

	@Column(name = "date")
	public String date;

	@Override
	public String toString() {
		return "Collection [id=" + getId() + ", cacheId=" + cacheId + ", shopId=" + shopId + ", userId=" + userId + ", quantity="
				+ quantity + ", date=" + date + "]";
	}

	/**
	 * 删除所有带回总数
	 */
	public static void deleteAll() {
		new Delete().from(CollectionOrder.class).execute();
	}

	/**
	 * 返回今天数据列表
	 * 
	 * @return
	 */
	public static List<CollectionOrder> queryListByDate(String time) {
		return new Select().from(CollectionOrder.class).where("date = ?", time).execute();
	}

}
