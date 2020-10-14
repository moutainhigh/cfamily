package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.model.PlusInfo;
import com.cmall.familyhas.api.model.PlusMemberInfo;
import com.cmall.familyhas.api.model.PlusPrivilageInfo;
import com.cmall.familyhas.api.result.ApiForPlusCenterResult;
import com.cmall.groupcenter.homehas.RsyncGetPlusSaveMoney;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/** 
*  plus会员中心接口
* @author Angel Joy
* @Time 2020年6月28日 下午2:06:25 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForPlusCenter extends RootApiForToken<ApiForPlusCenterResult, RootInput> {

	@Override
	public ApiForPlusCenterResult Process(RootInput inputParam, MDataMap mRequestMap) {
		ApiForPlusCenterResult result = new ApiForPlusCenterResult();
		String memberCode = getUserCode();
		String channelId = getChannelId();
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode));
		PlusMemberInfo memberInfo = new PlusMemberInfo();
		String mobile = getOauthInfo().getLoginName();
		MDataMap map = this.getAvatarAndNickName(mobile);
		memberInfo.setAvatar(map.get("avatar"));
		memberInfo.setExpireDate(levelInfo.getPlusEndDate());
		memberInfo.setNickName(map.get("nickName"));
		if(!StringUtils.isEmpty(memberInfo.getExpireDate())) {
			memberInfo.setPrivilegeflag(1);
		}else {
			memberInfo.setPrivilegeflag(0);
		}
		BigDecimal saveMoney = BigDecimal.ZERO;
		if(memberInfo.getPrivilegeflag() == 1) {//是橙意卡会员，才会查询节约金额
			saveMoney = this.getSaveMoney(memberCode,levelInfo.getCustId());
		}
		memberInfo.setSaveMoney(saveMoney);//TODO
		result.setMemberInfo(memberInfo);
		PlusInfo plusInfo = new PlusInfo();
		List<PlusPrivilageInfo> plusPrivilageInfos = new ArrayList<PlusPrivilageInfo>();
		String sql = "SELECT * FROM systemcenter.sc_plus_privilege_info where flag_enable = 1 ";
		List<Map<String,Object>> plusPrivilageMaps = DbUp.upTable("sc_plus_privilege_info").dataSqlList(sql, new MDataMap());
		for(Map<String,Object> plusPrivilage : plusPrivilageMaps) {
			PlusPrivilageInfo plusPrivilageInfo = new PlusPrivilageInfo();
			plusPrivilageInfo.setPrivilegeDesc(MapUtils.getString(plusPrivilage, "privilege_desc",""));
			plusPrivilageInfo.setPrivilegeName(MapUtils.getString(plusPrivilage, "privilege_name",""));
			plusPrivilageInfo.setPrivilegeUrl(MapUtils.getString(plusPrivilage, "privilege_url",""));
			plusPrivilageInfo.setPrivilegeType(MapUtils.getString(plusPrivilage, "privilege_type",""));
			plusPrivilageInfo.setPrivilegeValue(MapUtils.getString(plusPrivilage, "privilege_value",""));
			plusPrivilageInfos.add(plusPrivilageInfo);
		}
		plusInfo.setPlusPrivilageInfos(plusPrivilageInfos);
		plusInfo.setPrivilegeDesc(bConfig("xmassystem.plus_doc_url"));
		String skuCode = bConfig("xmassystem.plus_sku_code");
		String productCode = bConfig("xmassystem.plus_product_code");
		ProductPriceService pps = new ProductPriceService();
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		skuQuery.setFxFlag("0");
		skuQuery.setChannelId(channelId);
		skuQuery.setCode(productCode);
		skuQuery.setMemberCode(memberCode);
		Map<String,PlusModelSkuInfo> skuInfoMap = pps.getProductMinPriceSkuInfo(skuQuery, false);
		if(skuInfoMap != null&&skuInfoMap.get(productCode)!=null) {
			PlusModelSkuInfo skuInfo = skuInfoMap.get(productCode);
			plusInfo.setShowPrice(skuInfo.getSourcePrice());
			plusInfo.setSellPrice(skuInfo.getSellPrice());
		}
		plusInfo.setPlusProductCode(productCode);
		plusInfo.setPlusSkuCode(skuCode);
		result.setPlusInfo(plusInfo);
		return result;
	}
	
	/**
	 * 根据手机号获取头像，昵称
	 * @param mobile
	 * @return
	 * 2020年6月29日
	 * Angel Joy
	 * MDataMap
	 */
	private MDataMap getAvatarAndNickName(String mobile) {
		String nickName = "";
		String avatar = "";
		if(mobile != null) {
			MDataMap commentUser = DbUp.upTable("mc_member_sync").one("login_name", StringUtils.trimToEmpty(mobile));
			if(commentUser != null) {
				avatar = StringUtils.trimToEmpty(commentUser.get("avatar"));
				nickName = StringUtils.trimToEmpty(commentUser.get("nickname"));
			} else {
				if(mobile.length() > 7){
					nickName = mobile.substring(0, 3)+"****"+mobile.substring(7, mobile.length());
				}
			}
			if(StringUtils.isEmpty(nickName)&& mobile.length() > 7) {//兼容一下空昵称的数据
				nickName = mobile.substring(0, 3)+"****"+mobile.substring(7, mobile.length());
			}
		}
		MDataMap map = new MDataMap("nickName",nickName,"avatar",avatar);
		return map;
	}
	
	/**
	 * 橙意卡节约金额
	 * @param custId 
	 * @return
	 * 2020年6月29日
	 * Angel Joy
	 * BigDecimal
	 */
	private BigDecimal getSaveMoney(String memberCode, String custId) {
		//请求家有的 实时数据
		RsyncGetPlusSaveMoney rsync = new RsyncGetPlusSaveMoney();
		List<Map<String,Object>> eventList = new ArrayList<Map<String,Object>>();
		String sql = "SELECT * FROM systemcenter.sc_event_info WHERE event_type_code = '4497472600010026' ";
		eventList = DbUp.upTable("sc_event_info").dataSqlList(sql, new MDataMap());
		List<String> eventIds = new ArrayList<String>();
		if(eventList != null && eventList.size() > 0) {
			for(Map<String,Object> map : eventList) {
				String id = MapUtils.getString(map, "out_active_code", "");
				if(StringUtils.isNotEmpty(id)) {
					eventIds.add(id);
				}
			}
		}
		String eventId =  StringUtils.join(eventIds,",");
		BigDecimal saveValueLd = BigDecimal.ZERO;
		if(StringUtils.isNotEmpty(eventId)) {
			rsync.upRsyncRequest().setEvent_id(eventId);
			rsync.upRsyncRequest().setCust_id(custId);
			rsync.doRsync();
			String result = rsync.upResponseObject().getResult();
			saveValueLd = new BigDecimal(result);
		}
		BigDecimal saveValueHjy = BigDecimal.ZERO;
		String sqlSum = "SELECT IFNULL(SUM(preferential_money),0) preferential_money FROM ordercenter.oc_order_activity WHERE activity_type = '4497472600010026' AND order_code in (SELECT order_code FROM ordercenter.oc_orderinfo WHERE buyer_code = :buyer_code)";
		Map<String,Object> map = DbUp.upTable("oc_order_activity").dataSqlOne(sqlSum, new MDataMap("buyer_code",memberCode));
		String preferentialMoney = MapUtils.getString(map, "preferential_money", "0");
		saveValueHjy = new BigDecimal(preferentialMoney);
		BigDecimal total = saveValueLd.add(saveValueHjy);
		return total;
	}

}
