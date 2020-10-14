package com.cmall.familyhas.api;

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForAssetInput;
import com.cmall.familyhas.api.result.ApiForAssetResult;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 我的资产：积分、礼金卷、储值金、暂存款
 * @author dyc
 *
 */
public class ApiForAsset extends RootApiForToken<ApiForAssetResult, ApiForAssetInput> {

	public ApiForAssetResult Process(ApiForAssetInput inputParam, MDataMap mRequestMap) {
		
		ApiForAssetResult assetResult = new ApiForAssetResult();
		
		String member_code = getUserCode();
		
		String homehas_code=null;
		
		
//		//首先查询惠家有的用户
//		// 优先取内购的用户编号
//		Map<String, Object> map = DbUp.upTable("mc_login_info").dataSqlOne("SELECT e.homehas_code  from mc_extend_info_homehas e LEFT JOIN  mc_login_info l on e.member_code=l.member_code WHERE l.login_name=:login_name and l.manage_code=:manage_code order by e.vip_type", new MDataMap("login_name", getOauthInfo().getLoginName(),"manage_code",MemberConst.MANAGE_CODE_HOMEHAS));
//		if(map!=null&&map.size()>0){
//			homehas_code=(String)map.get("homehas_code");
//		}
//		
//		if(StringUtils.isBlank(homehas_code)){
//			//查询家有惠中用户
//			// 优先取内购的用户编号
//			Map<String, Object> map1 = DbUp.upTable("mc_login_info").dataSqlOne("SELECT e.old_code from mc_extend_info_homepool e LEFT JOIN  mc_login_info l on e.member_code=l.member_code WHERE l.login_name=:login_name and l.manage_code=:manage_code order by e.vip_type", new MDataMap("login_name", getOauthInfo().getLoginName(),"manage_code",MemberConst.MANAGE_CODE_HPOOL));
//			if(map1!=null&&map1.size()>0){
//				homehas_code=(String)map1.get("old_code");
//			}
//		}
//		
//		
//		if(StringUtils.isBlank(homehas_code)){
//			return assetResult;
//		}
//		
//		//请求家有的 实时数据
//		RsyncCustInfo rsyncCustInfo = new RsyncCustInfo();
//		rsyncCustInfo.upRsyncRequest().setCust_id(homehas_code);
////		rsyncCustInfo.upRsyncRequest().setCust_id("8404624");
//		rsyncCustInfo.doRsync();
//		
//		if(!rsyncCustInfo.getResponseObject().isSuccess() ||rsyncCustInfo.getResponseObject().getResult().size()<1){
//			assetResult.setResultCode(922401009);
//			assetResult.setResultMessage(bInfo(922401009));
//			return assetResult;
//		}
//		
//		//会员数据
//		CustInfo custInfo = rsyncCustInfo.getResponseObject().getResult().get(0); 
//		
//		
//		assetResult.setAvailablePoint(custInfo.getPoss_accm_amt());
//		assetResult.setAvailableTemporaryStore(custInfo.getPoss_crdt_amt());
//		assetResult.setAvailableStoreGold(custInfo.getPoss_ppc_amt());
//			
//		List<CashGift> cashGiftList = custInfo.getCashGiftList();
//		BigDecimal availableGiftCard = new BigDecimal(0);//
//		if(cashGiftList!=null&&cashGiftList.size()>0){
//			
//			for (CashGift cashGift : cashGiftList) {
//				
//				String sy_vl=cashGift.getSy_vl();//是否使用（Y:已用 N：未用）
//				String vl_yn=cashGift.getVl_yn();//是否有效（Y：有效 N：无效）
//				
//				
//				//把未用有效的礼金券统计一下
//				if("N".equals(sy_vl)&&"Y".equals(vl_yn)){
//					availableGiftCard=availableGiftCard.add(new BigDecimal(cashGift.getLj_amt()));
//				}
//				
//				
//			}
//		}
		
//		assetResult.setAvailableGiftCard(String.valueOf(availableGiftCard));//用户礼金券 //TODO 礼金券暂时不写
		
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		
		homehas_code = plusServiceAccm.getCustId(member_code);
		if(StringUtils.isBlank(homehas_code)){
			return assetResult;
		}
		
		GetCustAmtResult amtResult = plusServiceAccm.getPlusModelCustAmt(homehas_code);
		if(amtResult != null){
//			DecimalFormat df = new DecimalFormat("#.#");
//			assetResult.setAvailablePoint(df.format(amtResult.getPossAccmAmt()));
//			assetResult.setAvailableTemporaryStore(df.format(amtResult.getPossCrdtAmt()));
//			assetResult.setAvailableStoreGold(df.format(amtResult.getPossPpcAmt()));
			
			assetResult.setAvailablePoint(amtResult.getPossAccmAmt().intValue()+"");
			assetResult.setAvailableTemporaryStore(amtResult.getPossCrdtAmt().intValue()+"");
			assetResult.setAvailableStoreGold(amtResult.getPossPpcAmt().intValue()+"");
		}
		
		return assetResult;
	}
	
}


