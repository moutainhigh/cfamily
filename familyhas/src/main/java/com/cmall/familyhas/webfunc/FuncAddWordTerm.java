package com.cmall.familyhas.webfunc;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.PinyinUtil;
import com.cmall.systemcenter.support.TermChangeSupport;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加搜索词库
 */
public class FuncAddWordTerm extends RootFunc{
      
		@Override
		public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
			MWebResult result = new MWebResult();
			String term = StringUtils.trimToEmpty(mDataMap.get("zw_f_term"));
			if(StringUtils.isBlank(term) || term.length() == 1) {
				result.setResultCode(0);
				result.setResultMessage("关键词不能为空且长度必须大于1");
				return result;
			}
			
			if(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(term).length > 30) {
				result.setResultCode(0);
				result.setResultMessage("关键词最长不能超过10个汉字");
				return result;
			}
			
			Pattern pattern = Pattern.compile("\\s+");
			if(pattern.matcher(term).find()) {
				result.setResultCode(0);
				result.setResultMessage("关键词中不能包含空格");
				return result;
			}
			
			if(DbUp.upTable("sc_word_term").count("term", term) > 0) {
				result.setResultCode(0);
				result.setResultMessage("关键词已存在");
				return result;
			}
			
			String pinyin = PinyinUtil.getFullSpell(term);
			
			// 保存到词库表
			MDataMap insertMap = new MDataMap();
			insertMap.put("term", term);
			insertMap.put("pinyin", pinyin);
			insertMap.put("create_time", FormatHelper.upDateTime());
			insertMap.put("create_user", UserFactory.INSTANCE.create().getLoginName());
			DbUp.upTable("sc_word_term").dataInsert(insertMap);
			
			// 记录日志表
			MDataMap logInsertMap = new MDataMap();
			logInsertMap.put("term", term);
			logInsertMap.put("oper_type", TermChangeSupport.TYPE_ADD+"");
			logInsertMap.put("create_time", insertMap.get("create_time"));
			logInsertMap.put("create_user", insertMap.get("create_user"));
			DbUp.upTable("lc_word_term_log").dataInsert(logInsertMap);
			
			return result;
		}
		
	}
