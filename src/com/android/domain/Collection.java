package com.android.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.android.common.Constants;
import com.android.common.DateUtils;
import com.android.common.MyApp;

@Table(name ="tb_collection")
public class Collection extends Model{
	@Column(name = "cashID")
	public String cashID;
	
	@Column(name = "shopId")
	public String shopId;
	
	@Column(name = "userId")
	public String userId;
	
	@Column(name = "quantity")
	public String quantity;
	
	@Column(name = "status")
	public String status;
	
	@Column(name = "date")
	public String date;
	
	@Override
	public String toString() {
		return "Collection [cashID=" + cashID + ", shopId=" + shopId + ", userId=" + userId + ", quantity=" + quantity + "]";
	}
	/**
	 * 保存
	 * @param bean
	 * @param myApp
	 */
	public static void save(Collection bean,MyApp myApp) {
		Collection c_order = new Collection();
		c_order.status = Constants.DB_FAILED;// 是否成功 1是 0否
		c_order.shopId = myApp.getSettingShopId();// 店idmyApp.getShopid()
		c_order.userId = myApp.getUser_id();//
		c_order.date = DateUtils.dateToStr(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
		c_order.quantity = bean.quantity;//
		c_order.cashID= bean.cashID;
		c_order.save();
	}
	/**
	 * 返回列表
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
	 * @return
	 */
	public static List<Collection> TodayList(String time) {
		return new Select().from(Collection.class).where("date = ?", time).execute();
	}
	/**
	 * 返回今天数据列表
	 * @return
	 */
	public static List<Collection> TodayStatusList(String time,String status) {
		return new Select().from(Collection.class).where("date = ? and status=?", time,status).execute();
	}
	/**
	 * 更新所有提交成功的
	 */
	public static void updateAllByStatus() {
		List<Collection> Collections = queryListByStatus(Constants.DB_SUCCESS);
		if (CollectionUtils.isNotEmpty(Collections)) {
			for (Collection Collection : Collections) {
				Collection.status = Constants.DB_SUCCESS;
				Collection.save();
			}
		}
	}
	/**
	 * 按照ID更新数据
	 * @param androidId
	 */
	public static void updateByStatus(Long androidId) {
		Collection collection = Collection.load(Collection.class, androidId);
		if (collection != null) {
			collection.status = Constants.DB_SUCCESS;
			collection.save();
		}
	}
}
