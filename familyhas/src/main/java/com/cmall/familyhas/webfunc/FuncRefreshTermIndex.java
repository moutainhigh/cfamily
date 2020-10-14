package com.cmall.familyhas.webfunc;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.cmall.systemcenter.support.TermChangeSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 刷新关键词搜索索引
 */
public class FuncRefreshTermIndex extends RootFunc{
      
		@Override
		public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
			MWebResult result = new MWebResult();
			
			String term = (String)DbUp.upTable("sc_word_term").dataGet("term", "uid=:zw_f_uid", mDataMap);
			if(StringUtils.isNotBlank(term)) {
				new TermChangeSupport().updateTerm(term, TermChangeSupport.TYPE_ADD);
				new TermChangeSupport().refreshIndexByTerms(Arrays.asList(term));;
			}
			
			return result;
		}
		
	}
