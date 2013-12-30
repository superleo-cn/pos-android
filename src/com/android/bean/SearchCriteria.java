package com.android.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.common.Constants;

/**
 * @ Sep 9, 2009 9:50:41 AM @ [SearchCriteria]
 */
public class SearchCriteria {

	public Map<String, String> map = new HashMap<String, String>();

	public List params = new ArrayList();

	public String sql = Constants.DEFAUL_SQL_WHERE;

	public SearchCriteria() {
	}

}
