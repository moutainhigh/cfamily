package com.cmall.familyhas.webfunc;

import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 评论造假
 * @author ligj
 * Date 2015-12-23
 */
public class FuncFraudCommentCf extends RootFunc {

	private static String highPraise = "好评";
	
	private static String commonPraise = "中评";
	
	private static String lowPraise = "差评";
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		// 定义插入数据库
		MDataMap mInsertMap = new MDataMap();
	
		recheckMapField(mResult, mPage, mAddMaps);
		if (mResult.upFlagTrue()) {
			// 循环所有结构 初始化插入map
			for (MWebField mField : mPage.getPageFields()) {
				
				if (mAddMaps.containsKey(mField.getFieldName())
						&& StringUtils.isNotEmpty(mField.getColumnName())) {
					
					String sValue = mAddMaps.get(mField.getFieldName());
					if("order_assessment".equals(mField.getColumnName())){//对评论内容处理下子
						sValue = getNoHTMLString(sValue,sValue.length());//去掉html代码的评论
					}
					mInsertMap.put(mField.getColumnName(), sValue);
				}
			}
			mInsertMap.put("manage_code",UserFactory.INSTANCE.create().getManageCode()); // app
			//好。中评
			switch (Integer.parseInt(mAddMaps.get("grade"))) {
			case 1:
				mInsertMap.put("grade_type", lowPraise);
				break;
			case 2:
			case 3:
				mInsertMap.put("grade_type", commonPraise);
				break;
			case 4:
			case 5:
				mInsertMap.put("grade_type", highPraise);
				break;
			default:
				break;
			}
			
			String oder_photos = mInsertMap.get("oder_photos");
			String is_share = mInsertMap.get("is_share");
			int grade = Integer.parseInt(mInsertMap.get("grade"));
			String order_assessment = mInsertMap.get("order_assessment");
			
			//随机生成会员手机号
			String[] numStrings = {"13","15","18"};
			Random  random = new  Random();
			String numStr = random.nextInt(1000000000)+"";
			while (numStr.length()<9) {
				numStr = "0"+ numStr;
			}
			String number = numStrings[random.nextInt(3)]+numStr;
			mInsertMap.put("user_mobile", number);
			
			//order_smallphotos
			mInsertMap.put("order_smallphotos", mInsertMap.get("oder_photos"));
			
			//会员编号
			mInsertMap.put("order_name", "MI150824100141");// 13810137968 
			
			//评论状态默认为审核通过
			mInsertMap.put("check_flag", "4497172100030002");
			
			MDataMap productCodeMap =  DbUp.upTable("pc_skuinfo").oneWhere("product_code,sku_name", " -zid ", " sku_code = :sku_code", "sku_code",mAddMaps.get("order_skuid"));
			
			mInsertMap.put("sku_name", productCodeMap.get("sku_name"));//sku名称
			mInsertMap.put("product_code", productCodeMap.get("product_code"));//商品编号
			
			String uid = DbUp.upTable(mPage.getPageTable()).dataInsert(mInsertMap);
			mResult.setResultMessage(bInfo(969909001));
			
			// 是否分享到买家秀,校验是否满足分享买家秀条件:上传图片或视频,且有评价文字的好评(虽然是造假,但是还要判断是否满足分享到买家秀的条件)
			if(is_share.equals("1") && StringUtils.isNotBlank(oder_photos) && grade >= 4 && StringUtils.isNotBlank(order_assessment)) {
				MDataMap buyer_show = DbUp.upTable("nc_buyer_show_info").one("evaluation_uid",uid);
				if(buyer_show == null) {
					// 买家秀表新增
					MDataMap insertMap = new MDataMap();
					insertMap.put("evaluation_uid", uid);
					insertMap.put("create_time", DateUtil.getSysDateTimeString());
					insertMap.put("check_status", "449748580001");
					insertMap.put("is_delete", "0");
					insertMap.put("member_code", "MI150824100141");
					DbUp.upTable("nc_buyer_show_info").dataInsert(insertMap);
				}
			}
			
		}
		return mResult;
	}
	

	/*** 
    *  
    * @param content 内容String 
    * @param p >0 .位数 
    * @purpose：得到相应位数已过滤html、script、style 标签的内容 
    */  
    private String getNoHTMLString(String content,int p){  
        if(null==content) return "";  
        if(0==p) return "";  
        java.util.regex.Pattern p_script;   
             java.util.regex.Matcher m_script;   
             java.util.regex.Pattern p_style;   
             java.util.regex.Matcher m_style;   
             java.util.regex.Pattern p_html;   
             java.util.regex.Matcher m_html;   
         try {   
             String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";  
             //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }   
             String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";   
                   //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }   
                   String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式   
                 
                   p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);   
                   m_script = p_script.matcher(content);   
                   content = m_script.replaceAll(""); //过滤script标签  
                   p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);   
                   m_style = p_style.matcher(content);   
                   content = m_style.replaceAll(""); //过滤style标签   
                   p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);   
                   m_html = p_html.matcher(content);   
                     
                   content = m_html.replaceAll(""); //过滤html标签   
               }catch(Exception e) {   
                       return "";  
               }   
               /*if(content.length()>p){  
                content = content.substring(0, p)+"...";  
               }else{  
                content = content + "...";  
               }  */
       return content;  
    }  
}
