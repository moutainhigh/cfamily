package com.cmall.familyhas.webfunc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.PinyinUtil;
import com.cmall.systemcenter.support.TermChangeSupport;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.ExcelSupport;

/**
 * 批量导入搜索词库
 */
public class FuncBatchImportSearchTerm extends RootFunc {
	
	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String uploadFileUrl = mDataMap.get("uploadFile");
		if(StringUtils.isBlank(uploadFileUrl)){
			mResult.setResultCode(0);
			mResult.setResultMessage("未找到上传的文件");
			return mResult;
		}
		
		List<MDataMap> list = null;
		try {
			list = new ExcelSupport().upExcelFromUrl(uploadFileUrl);
		} catch (Exception e) {
			e.printStackTrace();
			mResult.setResultCode(0);
			mResult.setResultMessage("解析文件内容失败："+e);
			return mResult;
		}
		
		if(list == null || list.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("导入文件无内容");
			return mResult;
		}
		
		Pattern pattern = Pattern.compile("\\s+");
		Set<String> errList = new HashSet<String>();
		Set<String> termList = new HashSet<String>();
		String v;
		Set<String> errLenList = new HashSet<String>();
		for(MDataMap map : list) {
			v = StringUtils.trimToEmpty(map.get("关键词"));
			if(pattern.matcher(v).find()) {
				errList.add(v);
			} else {
				termList.add(v);
			}
			
			if(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(v).length > 30) {
				errLenList.add(v);
			}
		}
		
		if(!errList.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("关键词中不能包含空格："+StringUtils.join(errList,"、"));
			return mResult;
		}
		
		if(!errLenList.isEmpty()) {
			mResult.setResultCode(0);
			mResult.setResultMessage("关键词最长不能超过10个汉字："+StringUtils.join(errLenList,"、"));
			return mResult;
		}
		
		String createTime = FormatHelper.upDateTime();
		String createUser = UserFactory.INSTANCE.create().getLoginName();
		String pinyin;
		int total = 0, succ = 0;
		for(String term : termList) {
			total++;
			if(DbUp.upTable("sc_word_term").count("term", term) > 0) {
				continue;
			}
			
			pinyin = PinyinUtil.getFullSpell(term);
			
			// 保存到词库表
			MDataMap insertMap = new MDataMap();
			insertMap.put("term", term);
			insertMap.put("pinyin", pinyin);
			insertMap.put("create_time", createTime);
			insertMap.put("create_user", createUser);
			DbUp.upTable("sc_word_term").dataInsert(insertMap);
			
			// 记录日志表
			MDataMap logInsertMap = new MDataMap();
			logInsertMap.put("term", term);
			logInsertMap.put("oper_type", TermChangeSupport.TYPE_ADD+"");
			logInsertMap.put("create_time", createTime);
			logInsertMap.put("create_user", createUser);
			DbUp.upTable("lc_word_term_log").dataInsert(logInsertMap);
			
			succ++;
		}
		
		mResult.setResultMessage("执行结果， 总数："+total+", 成功数："+succ);
		
		return mResult;
	}
	
}
