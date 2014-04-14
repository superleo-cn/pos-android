package com.android.mapping;

import java.io.File;
import java.util.List;

import com.android.common.Constants;
import com.android.common.HttpHelper;
import com.android.common.RestHelper;
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
