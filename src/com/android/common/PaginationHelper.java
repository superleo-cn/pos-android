package com.android.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.android.bean.Pagination;
import com.android.bean.SearchCriteria;

public abstract class PaginationHelper {

	/*******************************************
	 * 
	 * 分页查询
	 * 
	 * @return
	 */
	public static <T extends Model> Pagination pagination(Class<T> t, Pagination pagination, SearchCriteria searchCriteria) {
		buildSql(searchCriteria);
		pagination.recordCount = paginationCount(t, searchCriteria);
		pagination.recordList = paginatioList(t, pagination, searchCriteria);
		pagination.pageCount = (pagination.recordCount / pagination.pageSize) + 1;
		return pagination;
	}

	/**
	 * 返回分页所需数据
	 * 
	 * @param pagination
	 * @param searchCriteria
	 * @return
	 */
	private static <T extends Model> List<T> paginatioList(Class<T> t, Pagination pagination, SearchCriteria searchCriteria) {
		return new Select().from(t).where(searchCriteria.sql, searchCriteria.params.toArray())
				.limit((pagination.currentPage - 1) * pagination.pageSize + "," + pagination.pageSize).execute();
	}

	/**
	 * 返回数据个数
	 * 
	 * @return count
	 */
	private static <T extends Model> long paginationCount(Class<T> t, SearchCriteria searchCriteria) {
		return new Select().from(t).where(searchCriteria.sql, searchCriteria.params.toArray()).execute().size();
	}

	public static String buildSql(SearchCriteria searchCriteria) {
		if (searchCriteria != null && searchCriteria.map.size() > 0) {
			// 清空当前参数
			searchCriteria.params.clear();
			StringBuilder sql = new StringBuilder(Constants.DEFAUL_SQL_WHERE);
			for (Map.Entry<String, String> entry : searchCriteria.map.entrySet()) {
				if (StringUtils.isNotEmpty(entry.getValue())) {
					if (StringUtils.equals("status", entry.getKey())) {
						sql.append(" and status = ? ");
					} else if (StringUtils.equals("dateFrom", entry.getKey())) {
						sql.append(" and date >= ? ");
					} else if (StringUtils.equals("dateTo", entry.getKey())) {
						sql.append(" and date < ? ");
					}
					searchCriteria.params.add(entry.getValue());
				}
			}
			searchCriteria.sql = sql.toString();
		}
		return searchCriteria.sql;
	}
}
