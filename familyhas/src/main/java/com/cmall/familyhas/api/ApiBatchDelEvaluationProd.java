package com.cmall.familyhas.api;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiBatchDelEvaluationProdInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 批量删除商品评论模板下的商品(js调用，后台首页板式栏目维护中维护内容页面下的批量删除按钮调用)
 * @author lgx
 * 
 *
 */
public class ApiBatchDelEvaluationProd extends RootApi<RootResultWeb, ApiBatchDelEvaluationProdInput>{
	public RootResultWeb Process(ApiBatchDelEvaluationProdInput input,MDataMap mDataMap){
		RootResultWeb result = new RootResultWeb();
		List<String> uids = input.getUids();
		
		if (null != uids) {
			String sql = "UPDATE fh_apphome_evaluation SET is_delete = '1' WHERE uid in ('"+StringUtils.join(uids,"','")+"')";
			DbUp.upTable("fh_apphome_evaluation").dataExec(sql, new MDataMap());
		}
		
		return result;
	}
}
