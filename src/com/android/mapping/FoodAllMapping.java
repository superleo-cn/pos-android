package com.android.mapping;

import java.io.File;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.android.common.Constants;
import com.android.common.HttpHelper;
import com.android.common.RestHelper;
import com.android.domain.AttributesR;
import com.android.domain.CategoriesR;
import com.android.domain.FoodR;
import com.android.mapping.FoodAllMapping.Remotes;

public class FoodAllMapping extends BasicExMapping<Remotes> {

	private static final FoodAllMapping mapping = new FoodAllMapping();

	public static class Remotes {
		public List<RemoteAttribute> attributes;
		public List<FoodRemote> foods;
		public List<CategoryRemote> categories;
	}

	public static class RemoteAttribute {
		public String id;

		public String code;

		public String name;

		public String nameZh;

		public String status;

		public String position;

		public FoodRemote food;
	}

	public static class FoodRemote {
		public String id;

		public String sn;

		public String name;

		public String nameZh;

		public String type;

		public String position;

		public String picture;

		public String retailPrice;
		
		public CategoryRemote category;
	}

	public static class CategoryRemote {
		public String id;

		public String code;

		public String name;

		public String nameZh;

		public String status;

		public String position;
		
	}

	public static FoodAllMapping getJSONAndSave(String url) {
		try {
			FoodAllMapping foodMapping = RestHelper.getJSON(url, FoodAllMapping.class);
			if (foodMapping != null) {
				if (foodMapping.code == Constants.STATUS_SUCCESS) {
					Remotes remotes = foodMapping.datas;
					// 这里放你的代码
					// 保存
					System.out.println(remotes.attributes);
					System.out.println(remotes.foods);
					System.out.println(remotes.categories);
					
					// 删除历史数据
					FoodR.deleteAll();
					// 删除历史数据
					CategoriesR.deleteAll();
					AttributesR.deleteAll();
					// 下载食物数据
					List<FoodRemote> foodlist = remotes.foods;
					if (CollectionUtils.isNotEmpty(foodlist)) {
						for (int i = 0; i < foodlist.size(); i++) {
							FoodRemote foodRemote = foodlist.get(i);
							String image_file = downloadImage(foodRemote, i);
							foodRemote.picture = image_file;
							// save Food
							FoodR.save(foodRemote);
						}
					}
					
					// 下载食物数据
					List<CategoryRemote> list = remotes.categories;
					if (CollectionUtils.isNotEmpty(list)) {
						for (int i = 0; i < list.size(); i++) {
							CategoryRemote categoryRemote = list.get(i);
							CategoriesR.save(categoryRemote);
						}
					}
					
					// 下载食物数据
					List<RemoteAttribute> attributelist = remotes.attributes;
					if (CollectionUtils.isNotEmpty(attributelist)) {
						for (int i = 0; i < attributelist.size(); i++) {
							RemoteAttribute attributeRemote = attributelist.get(i);
							AttributesR.save(attributeRemote);
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
