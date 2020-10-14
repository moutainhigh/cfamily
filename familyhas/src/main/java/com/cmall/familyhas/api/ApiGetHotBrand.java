package com.cmall.familyhas.api;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiGetHotBrandInput;
import com.cmall.familyhas.api.model.HotBrandInfo;
import com.cmall.familyhas.api.model.PageResults;
import com.cmall.familyhas.api.result.ApiGetHotBrandResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;


/**
 * 获取热销品牌列表
 * */
public class ApiGetHotBrand extends RootApiForManage<ApiGetHotBrandResult, ApiGetHotBrandInput> {

	public ApiGetHotBrandResult Process(ApiGetHotBrandInput inputParam,
			MDataMap mRequestMap) {
		ApiGetHotBrandResult re = new ApiGetHotBrandResult();
		int offset = inputParam.getPaging().getOffset();//起始页
		int limit = inputParam.getPaging().getLimit();//每页条数
		int startNum = limit*offset;//开始条数
		int endNum = startNum+limit;//结束条数
		String sql = "SELECT b.brand_code,b.brand_name,b.brand_pic,a.link FROM pc_hot_brand a,pc_brandinfo b where a.brand_code = b.brand_code ORDER BY a.sort DESC limit "+startNum+","+limit;
		List<Map<String, Object>> list = DbUp.upTable("pc_hot_brand").dataSqlList(sql, new MDataMap());
		Map<String, Object> map = DbUp.upTable("pc_hot_brand").dataSqlOne("SELECT count(1) count FROM pc_hot_brand a,pc_brandinfo b where a.brand_code = b.brand_code", new MDataMap());
		int totalNum = Integer.parseInt(map.get("count").toString());
		
		
		int more = 1;//有更多数据
		if(endNum>totalNum){
			more = 0;
		}
		
		//分页信息
		PageResults pageResults = new PageResults();
		pageResults.setTotal(totalNum);
		pageResults.setMore(more);
		int num = 0;
		if(list!=null){
			num = list.size();
			for(Map<String, Object> obj : list){
				HotBrandInfo info = new HotBrandInfo();
				info.setBrandCode(obj.get("brand_code").toString());
				info.setBrandName(obj.get("brand_name").toString());
				info.setPic(obj.get("brand_pic").toString());
				info.setLink(obj.get("link").toString());
				re.getBrands().add(info);
			}
		}
		pageResults.setCount(num);
		re.setPaged(pageResults);
		return re;
	}

}
