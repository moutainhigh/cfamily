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
 * 修改搜索词库
 */
public class FuncEditWordTerm extends RootFunc{
      
		@Override
		public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
			MWebResult result = new MWebResult();
			String uid = StringUtils.trimToEmpty(mDataMap.get("zw_f_uid"));
			String term = StringUtils.trimToEmpty(mDataMap.get("zw_f_term"));
			if(StringUtils.isBlank(term) || term.length() == 1) {
				result.setResultCode(0);
				result.setResultMessage("关键词不能为空且长度必须大于1");
				return result;
			}
			
			Pattern pattern = Pattern.compile("\\s+");
			if(pattern.matcher(term).find()) {
				result.setResultCode(0);
				result.setResultMessage("关键词中不能包含空格");
				return result;
			}
			
			if(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(term).length > 30) {
				result.setResultCode(0);
				result.setResultMessage("关键词最长不能超过10个汉字");
				return result;
			}
			
			if(DbUp.upTable("sc_word_term").count("term", term) > 0) {
				result.setResultCode(0);
				result.setResultMessage("关键词已存在");
				return result;
			}
			
			// 忽略词内容未变更的情况
			MDataMap oldTermMap = DbUp.upTable("sc_word_term").one("uid", uid);
			if(term.equals(oldTermMap.get("term"))) {
				return result;
			}
			
			String oldTerm = oldTermMap.get("term");
			String pinyin = PinyinUtil.getFullSpell(term);
			
			// 保存更新到词库表
			oldTermMap.put("term", term);
			oldTermMap.put("pinyin", pinyin);
			oldTermMap.put("create_time", FormatHelper.upDateTime());
			oldTermMap.put("create_user", UserFactory.INSTANCE.create().getLoginName());
			DbUp.upTable("sc_word_term").dataUpdate(oldTermMap, "term,pinyin,create_time,create_user", "zid");
			
			/** 修改词库等于是删除旧词添加新词 */
			// 记录删除词日志表
			MDataMap logDelMap = new MDataMap();
			logDelMap.put("term", oldTerm);
			logDelMap.put("oper_type", TermChangeSupport.TYPE_DEL+"");
			logDelMap.put("create_time", oldTermMap.get("create_time"));
			logDelMap.put("create_user", oldTermMap.get("create_user"));
			DbUp.upTable("lc_word_term_log").dataInsert(logDelMap);
			
			// 记录新增词日志表
			MDataMap logInsertMap = new MDataMap();
			logInsertMap.put("term", term);
			logInsertMap.put("oper_type", TermChangeSupport.TYPE_ADD+"");
			logInsertMap.put("create_time", oldTermMap.get("create_time"));
			logInsertMap.put("create_user", oldTermMap.get("create_user"));
			DbUp.upTable("lc_word_term_log").dataInsert(logInsertMap);
			
			return result;
		}
		
	}
