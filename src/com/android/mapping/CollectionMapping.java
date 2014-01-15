package com.android.mapping;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.android.common.Constants;
import com.android.common.RestHelper;
import com.android.domain.Collection;
import com.android.mapping.CollectionMapping.CollectionRemote;

public class CollectionMapping extends BasicMapping<CollectionRemote> {

	private static final CollectionMapping mapping = new CollectionMapping();

	public static class CollectionRemote {

		public String id;

		public String price;

		public String position;

	}

	public static CollectionMapping getJSONAndSave(String url) {
		try {
			CollectionMapping collectionMapping = RestHelper.getJSON(url, CollectionMapping.class);
			if (collectionMapping != null && collectionMapping.code == Constants.STATUS_SUCCESS) {
				// 删除历史数据
				Collection.deleteAll();
				List<CollectionRemote> list = collectionMapping.datas;
				if (CollectionUtils.isNotEmpty(list)) {
					for (int i = 0; i < list.size(); i++) {
						CollectionRemote CollectionRemote = list.get(i);
						Collection.save(CollectionRemote);
					}
				}
			}
			return collectionMapping;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mapping;

	}

}
