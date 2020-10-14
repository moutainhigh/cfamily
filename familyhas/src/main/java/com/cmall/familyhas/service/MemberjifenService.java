package com.cmall.familyhas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiIntegralDetailsInput;
import com.cmall.familyhas.api.model.CrdtInfo;
import com.cmall.familyhas.api.model.IntegralInfo;
import com.cmall.familyhas.api.model.PpcInfo;
import com.cmall.familyhas.api.result.ApiCrdtDetailsResult;
import com.cmall.familyhas.api.result.ApiIntegralDetailsResult;
import com.cmall.familyhas.api.result.ApiPpcDetailsResult;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmodel.MPageData;
import com.srnpr.zapweb.websupport.ApiCallSupport;


public class MemberjifenService extends BaseClass{
	private final String  jia = "+%s";
	private final String  jian = "-%s";
	public MPageData upChartData(String flag,String phonenumber,String pageIndex,String pageCount){
		MDataMap one = DbUp.upTable("mc_login_info").one("login_name",phonenumber,"manage_code","SI2003");
		if(one == null) {
			return new MPageData();
		}
		String member_code = one.get("member_code");
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("member_code", member_code);
		Map<String, Object> dataSqlOne = DbUp.upTable("za_oauth").dataSqlOne("select * from za_oauth where user_code =:member_code order by create_time desc limit 0,1", mWhereMap);
		String access_token = MapUtils.getString(dataSqlOne, "access_token");
		if("jifen".equals(flag)) {
			ApiCallSupport<ApiIntegralDetailsInput, ApiIntegralDetailsResult> apiCallSupport = new ApiCallSupport<ApiIntegralDetailsInput, ApiIntegralDetailsResult>();
			ApiIntegralDetailsInput input = new ApiIntegralDetailsInput();
			input.setPageCount(pageCount);
			input.setPageNum(pageIndex);
			ApiIntegralDetailsResult result = new ApiIntegralDetailsResult();
			try {
				result = apiCallSupport.doCallApiForToken(
						"http://localhost:8080/cfamily/jsonapi/", 
						"com_cmall_familyhas_api_ApiIntegralDetails", 
						"appfamilyhas", 
						/* bConfig("familyhas.apiPass") == null?"":bConfig("familyhas.apiPass"), */
						"amiauhsnehnujiauhz",
						access_token, 
						input,
						result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(result.getResultCode() == 1) {
				MPageData mPageData = new MPageData();
				if("Y".equalsIgnoreCase(result.getPageFlag())) {
					mPageData.setFlagNext(0);
				}else if("N".equalsIgnoreCase(result.getPageFlag())) {
					mPageData.setFlagNext(1);
				}
				int totalInt = 0;
				if(StringUtils.isNotBlank(result.getTotal())) {
					totalInt = Integer.parseInt(result.getTotal());
				}
				mPageData.setPageCount(totalInt);
				mPageData.setPageIndex(Integer.parseInt(pageIndex));
				int pageCountInt = Integer.parseInt(pageCount);
				int pageMax = totalInt/pageCountInt;
				if(totalInt%pageCountInt != 0) {
					pageMax += 1;
				}
				mPageData.setPageMax(pageMax);
				mPageData.setPageSize(pageCountInt);
				List<String> heads = new ArrayList<String>();
				heads.add("会员手机号");
				heads.add("类别");
				heads.add("时间");
				heads.add("类别说明");
				heads.add("积分收支");
				mPageData.setPageHead(heads);
				List<String> fileds = new ArrayList<String>();
				fileds.add("phonenumber");
				fileds.add("jifen");
				fileds.add("accmCnfmDate时间");
				fileds.add("accmDesc");
				fileds.add("accmtype");
				mPageData.setPageField(fileds);
				List<List<String>> pageData = new ArrayList<List<String>>();
				List<IntegralInfo> data = result.getList();
				for(IntegralInfo info : data) {
					List<String> list = new ArrayList<String>();
					list.add(phonenumber);
					list.add("积分");
					list.add(info.getAccmCnfmDate());
					list.add(info.getAccmDesc());
					
					if("0".equals(info.getAccmtype())) {
						list.add(String.format(jian, info.getAccmCount()));
					}else if("1".equals(info.getAccmtype())){
						list.add(String.format(jia, info.getAccmCount()));
					}
					pageData.add(list);
				}
				mPageData.setPageData(pageData);
				return mPageData;
			}
		}else if("zancunkuan".equals(flag)) {
			ApiCallSupport<ApiIntegralDetailsInput, ApiCrdtDetailsResult> apiCallSupport = new ApiCallSupport<ApiIntegralDetailsInput, ApiCrdtDetailsResult>();
			ApiIntegralDetailsInput input = new ApiIntegralDetailsInput();
			input.setPageCount(pageCount);
			input.setPageNum(pageIndex);
			ApiCrdtDetailsResult result = new ApiCrdtDetailsResult();
			try {
				result = apiCallSupport.doCallApiForToken(
						"http://localhost:8080/cfamily/jsonapi/", 
						"com_cmall_familyhas_api_ApiCrdtDetails", 
						"appfamilyhas", 
						/* bConfig("familyhas.apiPass") == null?"":bConfig("familyhas.apiPass"), */
						"amiauhsnehnujiauhz",
						access_token, 
						input,
						result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(result.getResultCode() == 1) {
				MPageData mPageData = new MPageData();
				if("Y".equalsIgnoreCase(result.getPageFlag())) {
					mPageData.setFlagNext(0);
				}else if("N".equalsIgnoreCase(result.getPageFlag())) {
					mPageData.setFlagNext(1);
				}
				int totalInt = 0;
				if(StringUtils.isNotBlank(result.getTotal())) {
					totalInt = Integer.parseInt(result.getTotal());
				}
				mPageData.setPageCount(totalInt);
				mPageData.setPageIndex(Integer.parseInt(pageIndex));
				int pageCountInt = Integer.parseInt(pageCount);
				int pageMax = totalInt/pageCountInt;
				if(totalInt%pageCountInt != 0) {
					pageMax += 1;
				}
				mPageData.setPageMax(pageMax);
				mPageData.setPageSize(pageCountInt);
				List<String> heads = new ArrayList<String>();
				heads.add("会员手机号");
				heads.add("类别");
				heads.add("时间");
				heads.add("类别说明");
				heads.add("暂存款收支");
				mPageData.setPageHead(heads);
				List<String> fileds = new ArrayList<String>();
				fileds.add("phonenumber");
				fileds.add("jifen");
				fileds.add("accmCnfmDate时间");
				fileds.add("accmDesc");
				fileds.add("accmtype");
				mPageData.setPageField(fileds);
				List<List<String>> pageData = new ArrayList<List<String>>();
				List<CrdtInfo> data = result.getList();
				for(CrdtInfo info : data) {
					List<String> list = new ArrayList<String>();
					list.add(phonenumber);
					list.add("暂存款");
					list.add(info.getCrdtCnfmDate());
					list.add(info.getCrdtDesc());
					
					if("0".equals(info.getCrdtType())) {
						list.add(String.format(jian, info.getCrdtCount()));
					}else if("1".equals(info.getCrdtType())){
						list.add(String.format(jia, info.getCrdtCount()));
					}
					pageData.add(list);
				}
				mPageData.setPageData(pageData);
				return mPageData;
			}
		}else if("chuzhijin".equals(flag)) {
			ApiCallSupport<ApiIntegralDetailsInput, ApiPpcDetailsResult> apiCallSupport = new ApiCallSupport<ApiIntegralDetailsInput, ApiPpcDetailsResult>();
			ApiIntegralDetailsInput input = new ApiIntegralDetailsInput();
			input.setPageCount(pageCount);
			input.setPageNum(pageIndex);
			ApiPpcDetailsResult result = new ApiPpcDetailsResult();
			try {
				result = apiCallSupport.doCallApiForToken(
						"http://localhost:8080/cfamily/jsonapi/", 
						"com_cmall_familyhas_api_ApiPpcDetails", 
						"appfamilyhas", 
						/* bConfig("familyhas.apiPass") == null?"":bConfig("familyhas.apiPass"), */
						"amiauhsnehnujiauhz",
						access_token, 
						input,
						result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(result.getResultCode() == 1) {
				MPageData mPageData = new MPageData();
				if("Y".equalsIgnoreCase(result.getPageFlag())) {
					mPageData.setFlagNext(0);
				}else if("N".equalsIgnoreCase(result.getPageFlag())) {
					mPageData.setFlagNext(1);
				}
				int totalInt = 0;
				if(StringUtils.isNotBlank(result.getTotal())) {
					totalInt = Integer.parseInt(result.getTotal());
				}
				mPageData.setPageCount(totalInt);
				mPageData.setPageIndex(Integer.parseInt(pageIndex));
				int pageCountInt = Integer.parseInt(pageCount);
				int pageMax = totalInt/pageCountInt;
				if(totalInt%pageCountInt != 0) {
					pageMax += 1;
				}
				mPageData.setPageMax(pageMax);
				mPageData.setPageSize(pageCountInt);
				List<String> heads = new ArrayList<String>();
				heads.add("会员手机号");
				heads.add("类别");
				heads.add("时间");
				heads.add("类别说明");
				heads.add("储值金收支");
				mPageData.setPageHead(heads);
				List<String> fileds = new ArrayList<String>();
				fileds.add("phonenumber");
				fileds.add("jifen");
				fileds.add("accmCnfmDate时间");
				fileds.add("accmDesc");
				fileds.add("accmtype");
				mPageData.setPageField(fileds);
				List<List<String>> pageData = new ArrayList<List<String>>();
				List<PpcInfo> data = result.getList();
				for(PpcInfo info : data) {
					List<String> list = new ArrayList<String>();
					list.add(phonenumber);
					list.add("储值金");
					list.add(info.getPpcCnfmDate());
					list.add(info.getPpcDesc());
					
					if("0".equals(info.getPpctype())) {
						list.add(String.format(jian, info.getPpcCount()));
					}else if("1".equals(info.getPpctype())){
						list.add(String.format(jia, info.getPpcCount()));
					}
					pageData.add(list);
				}
				mPageData.setPageData(pageData);
				return mPageData;
			}
		}
		return new MPageData();
	}
}