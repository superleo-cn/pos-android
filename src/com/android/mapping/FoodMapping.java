package com.android.mapping;

import java.io.File;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.android.common.Constants;
import com.android.common.HttpHelper;
import com.android.common.RestHelper;
import com.android.domain.Food;
import com.android.mapping.FoodMapping.FoodRemote;

public class FoodMapping extends BasicMapping<FoodRemote> {

	private static final FoodMapping mapping = new FoodMapping();

	public static class FoodRemote {

		public String id;

		public String sn;
		
		public String barCode;

		public String name;

		public String nameZh;

		public String type;

		public String position;

		public String picture;

		public String retailPrice;
		
		public String flag;
	}

	public static FoodMapping getJSONAndSave(String url) {
		try {
			FoodMapping foodMapping = RestHelper.getJSON(url, FoodMapping.class);
			if (foodMapping != null) {
				if (foodMapping.code == Constants.STATUS_SUCCESS) {
					// 删除历史数据
					Food.deleteAll();
					// 下载食物数据
					List<FoodRemote> list = foodMapping.datas;
					if (CollectionUtils.isNotEmpty(list)) {
						for (int i = 0; i < list.size(); i++) {
							FoodRemote foodRemote = list.get(i);
							String image_file = downloadImage(foodRemote, i);
							foodRemote.picture = image_file;
							// save Food
							Food.save(foodRemote);
						}
					}
				}
				return foodMapping;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mapping;

	}

	/**
	 * 下载图片
	 * 
	 * @param foodRemote
	 *            下载的对象
	 * @param i
	 *            下标
	 * @return 下载的图片名称
	 */
	public static String downloadImage(FoodRemote foodRemote, int i) {
		try {
			String image_file = Constants.CACHE_IMAGE + "/" + "food_image_" + i + ".png";
			HttpHelper.download(foodRemote.picture, new File(image_file));
			return image_file;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

}
