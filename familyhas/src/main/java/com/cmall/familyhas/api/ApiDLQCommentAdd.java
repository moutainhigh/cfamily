package com.cmall.familyhas.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.input.ApiDLQCommentAddInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basehelper.ALibabaJsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiDLQCommentAdd extends
		RootApiForManage<RootResult, ApiDLQCommentAddInput> {

	@Override
	public RootResult Process(ApiDLQCommentAddInput inputParam,
			MDataMap mRequestMap) {

		RootResult result = new RootResult();
		if(!this.mobileReg(inputParam.getMobile())) {
			result.setResultCode(0);
			result.setResultMessage("请填写正确的手机号");
			return result;
		}
		String flg = "";
		MDataMap one = DbUp.upTable("fh_dlq_page").one("page_number",inputParam.getRel_source().split("-")[1]);
		if(null != one) {
			String page_type = one.get("page_type");
			if(StringUtils.isNotBlank(page_type)) {
				if("1000".equals(page_type)) {
					flg = "1000";
				} else if("1001".equals(page_type)){
					flg = "1001";
				} else {
					result.setResultCode(0);
					result.setResultMessage("评论发表失败");
					return result;
				}
			} else {
				result.setResultCode(0);
				result.setResultMessage("评论发表失败");
				return result;
			}
		}
		
		DbUp.upTable("fh_dlq_comment").insert("mobile", inputParam.getMobile(),
				"u_ip", inputParam.getIp(), "content", this.StringFilter(inputParam.getContent()),
				"c_status", "1001",// 非屏蔽状态
				"source",inputParam.getRel_source(),
				"create_time", DateUtil.getNowTime(),
				"flg", flg);

		return result;
	}

	/**
	 *  字符串过滤
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	private String StringFilter(String str) throws PatternSyntaxException {
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("*").trim();
	}
	
	private boolean mobileReg(String mobile) {
		String regEx = "^1[0-9]{10}$";
		Pattern p = Pattern.compile(regEx);
		if(p.matcher(mobile).find()) {
			return true;
		} else {
			return false;
		}
		
	}
	public static void main(String[] args) {
		String sInput = "{\"content\":\"很棒\",\"ip\":\"124.126.190.199\",\"mobile\":\"13552143112\",\"rel_source\":\"DLQTV161014100001-DLQ161130100003\",\"version\":1}";
		ApiDLQCommentAddInput input = new ApiDLQCommentAddInput();
		
		input = ALibabaJsonHelper.fromJson(sInput , input);
		System.out.println(JSON.toJSONString(input));
	}
	
}
