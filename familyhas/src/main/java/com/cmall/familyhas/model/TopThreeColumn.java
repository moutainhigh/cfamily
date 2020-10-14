package com.cmall.familyhas.model;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeColumn;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**   
 * 	首页前三个栏目
*    ligj
*/
public class TopThreeColumn  {
	@ZapcomApi(value="前三个栏目List")
	private List<HomeColumn> topThreeColumnList = new ArrayList<HomeColumn>();

	public List<HomeColumn> getTopThreeColumnList() {
		return topThreeColumnList;
	}

	public void setTopThreeColumnList(List<HomeColumn> topThreeColumnList) {
		this.topThreeColumnList = topThreeColumnList;
	}
	

}

