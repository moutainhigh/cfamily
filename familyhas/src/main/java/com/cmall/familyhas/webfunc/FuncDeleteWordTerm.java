package com.cmall.familyhas.webfunc;


import org.apache.commons.lang.StringUtils;

import com.cmall.systemcenter.support.TermChangeSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 删除搜索词库
 */
public class FuncDeleteWordTerm extends RootFunc{
      
		@Override
		public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
			MWebResult result = new MWebResult();
			String uid = StringUtils.trimToEmpty(mDataMap.get("zw_f_uid"));
			
			// 忽略词内容未变更的情况
			MDataMap oldTermMap = DbUp.upTable("sc_word_term").one("uid", uid);
			
			DbUp.upTable("sc_word_term").delete("zid", oldTermMap.get("zid"));
			
			// 记录删除词日志表
			MDataMap logDelMap = new MDataMap();
			logDelMap.put("term", oldTermMap.get("term"));
			logDelMap.put("oper_type", TermChangeSupport.TYPE_DEL+"");
			logDelMap.put("create_time", oldTermMap.get("create_time"));
			logDelMap.put("create_user", oldTermMap.get("create_user"));
			DbUp.upTable("lc_word_term_log").dataInsert(logDelMap);
			
			return result;
		}
		
	}
