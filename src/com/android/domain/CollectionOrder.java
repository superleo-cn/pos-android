package com.android.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
	 * @return
	 */
	public static List<CollectionOrder> queryList() {
		return new Select().from(CollectionOrder.class).execute();
	}

	/**
	 * 店员今天是否有提交完成
	 * 
	 * 
	 * @param date
	 * @param status
	 * @return
	 */
	public static List<CollectionOrder> todayStatusList(String date, String status) {
		return new Select().from(CollectionOrder.class).where("date >= ? and status = ?", date, status).execute();
	}

	public static List<CollectionOrder> statusList(String status) {
		return new Select().from(CollectionOrder.class).where("status = ?", status).execute();
	}

	/**
	 * 店员今天是否有提交
	 * 
	 * @param userId
	 * @param shopId
	 * @param date
	 * @return
	 */
	public static List<CollectionOrder> todayList(String userId, String shopId, String date) {
		return new Select().from(CollectionOrder.class).where("user_id =? and shop_id = ? and date >= ?", userId, shopId, date).execute();
	}

	/**
	 * 该店是否今天完成
	 * 
	 * @param shopId
	 * @param date
	 * @param status
	 * @return
	 */
	public static List<CollectionOrder> todayCompleted(String shopId, String time, String status) {
		return new Select().from(CollectionOrder.class).where("shop_id = ? and date >= ? and status = ?", shopId, time, status).execute();
	}

	/**
	 * 保存
	 * 
	 * @param bean
	 * @param myApp
	 */
	public static void save(TakeNumberBean bean, MyApp myApp) {
		CollectionOrder collectionOrder = new CollectionOrder();
		collectionOrder.status = Constants.DB_FAILED;// 是否成功 1是 0否
		collectionOrder.shopId = myApp.getShopId();// 店idmyApp.getShopid()
		collectionOrder.userId = myApp.getUserId();//
		collectionOrder.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		collectionOrder.quantity = bean.getNum();//
		collectionOrder.cashId = bean.getId();
		collectionOrder.save();
	}

	/**
	 * 保存每日带回总数
	 * */
	public static void save(List<TakeNumberBean> number_classList, MyApp myApp) {
		for (int j = 0; j < number_classList.size(); j++) {
			TakeNumberBean bean = number_classList.get(j);
			bean.setPrice(StringUtils.defaultIfEmpty(bean.getNum(), Constants.DEFAULT_PRICE_INT));
			CollectionOrder.save(bean, myApp);
		}
	}

	/**
	 * 按照ID更新数据
	 * 
	 * @param androidId
	 */
	public static void updateByStatus(Long androidId) {
		CollectionOrder collection = CollectionOrder.load(CollectionOrder.class, androidId);
		if (collection != null) {
			collection.status = Constants.DB_SUCCESS;
			collection.save();
		}
	}

	/**
	 * 删除历史数据
	 * 
	 * @param time
	 */
	public static void deleteByDate(String time) {
		new Delete().from(CollectionOrder.class).where("date < ?", time).execute();
	}
}
