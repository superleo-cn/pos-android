package com.android.domain;

import java.util.Date;
import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.bean.TakeNumberBean;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;

@Table(name = "tb_collection_order")
public class CollectionOrder extends Model {

	@Column(name = "shop_id")
	public String shopId;

	@Column(name = "user_id")
	public String userId;

	@Column(name = "quantity")
	public String quantity;

	@Column(name = "cash_id")
	public String cashId;

	@Column(name = "date")
	public String date;

	@Column(name = "status")
	public String status;

	@Override
	public String toString() {
		return "Collection [id=" + getId() + ", cacheId=" + cashId + ", shopId=" + shopId + ", userId=" + userId + ", quantity=" + quantity
				+ ", date=" + date + "]";
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

	/**
	 * 保存
	 * 
	 * @param bean
	 * @param myApp
	 */
	public static void save(TakeNumberBean bean, MyApp myApp) {
		CollectionOrder c_order = new CollectionOrder();
		c_order.status = Constants.DB_FAILED;// 是否成功 1是 0否
		c_order.shopId = myApp.getSettingShopId();// 店idmyApp.getShopid()
		c_order.userId = myApp.getUser_id();//
		c_order.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		c_order.quantity = bean.getNum();//
		c_order.cashId = bean.getId();
		c_order.save();
	}

}
