package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiUserInfoQueryFromHomeInput;
import com.cmall.familyhas.api.model.MyPoints;
import com.cmall.familyhas.api.model.MyStoreGold;
import com.cmall.familyhas.api.model.MyTemporaryStore;
import com.cmall.familyhas.api.model.PageOption;
import com.cmall.familyhas.api.model.PageResults;
import com.cmall.familyhas.api.result.ApiUserInfoQueryFromHomeResult;
import com.cmall.familyhas.util.DateUtilA;
import com.cmall.groupcenter.homehas.RsyncCustInfo;
import com.cmall.groupcenter.homehas.model.RsyncResponseRsyncCustInfo.CustInfo;
import com.cmall.groupcenter.homehas.model.RsyncResponseRsyncCustInfo.Point;
import com.cmall.groupcenter.homehas.model.RsyncResponseRsyncCustInfo.StoredGold;
import com.cmall.groupcenter.homehas.model.RsyncResponseRsyncCustInfo.TempGold;
import com.cmall.membercenter.memberdo.MemberConst;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 我的积分、礼金卷、储值金、暂存款
 * @author dyc
 *
 */
public class ApiUserInfoQueryFromHomePc extends RootApiForToken<ApiUserInfoQueryFromHomeResult, ApiUserInfoQueryFromHomeInput> {

	
	public ApiUserInfoQueryFromHomeResult Process(ApiUserInfoQueryFromHomeInput inputParam, MDataMap mRequestMap) {
		
		ApiUserInfoQueryFromHomeResult userInfoQueryFromHomeResult = new ApiUserInfoQueryFromHomeResult();
		
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(getOauthInfo().getUserCode()));
		String homehas_code = levelInfo.getCustId();
		
		if(StringUtils.isBlank(homehas_code)){
			return userInfoQueryFromHomeResult;
		}
		
		//请求家有的 实时数据
		RsyncCustInfo rsyncCustInfo = new RsyncCustInfo();
		rsyncCustInfo.upRsyncRequest().setCust_id(homehas_code);
//		rsyncCustInfo.upRsyncRequest().setCust_id("8404624");
		rsyncCustInfo.doRsync();
		
		if(!rsyncCustInfo.getResponseObject().isSuccess() ||rsyncCustInfo.getResponseObject().getResult().size()<1){
			userInfoQueryFromHomeResult.setResultCode(922401009);
			userInfoQueryFromHomeResult.setResultMessage(bInfo(922401009));
			return userInfoQueryFromHomeResult;
		}
		
		//会员数据
		CustInfo custInfo = rsyncCustInfo.getResponseObject().getResult().get(0); 
		
		PageOption paging = inputParam.getPaging();
		String type = inputParam.getType();//0：用户积分、1：用户礼金卷、2：用户储值金、3：用户暂存款
		String time = inputParam.getTime();
		
		//进入假分页流程
		int totalNum = 0;
		int offset = paging.getOffset();//起始页
		int limit = paging.getLimit();//每页条数
		if(offset==0&&limit==0){
			limit = totalNum;
		}
		int startNum = limit*offset;//开始条数
		int endNum = startNum+limit;//结束条数
		int more = 1;//有更多数据

		if("0".equals(type)){ //0：用户积分
			
			// 积分失效：每年12月31日24时将清除当年1月至11月内所获得的所有积分，当年12月积分与次年2月最后一天24时清除。
			//记录当年1-11月的可用积分；记录12月份的积分
			
			
//			String increaseStr=bConfig("familyhas.memberInfo_myPoints_increase");
			String reduceStr=bConfig("familyhas.memberInfo_myPoints_reduce");
			String ar11=bConfig("familyhas.memberInfo_myPoints_ar");
			
			List<MyPoints> pointsList = userInfoQueryFromHomeResult.getPoints();
			List<Point>  list=custInfo.getPointList();
			BigDecimal arPoint11=new BigDecimal(0);//失效的积分
			
			String now_year=String.valueOf(DateUtilA.getYear(null));
			int now_month=DateUtilA.getMonth(null);//当前月份
			BigDecimal poss_accm_amt=new BigDecimal(custInfo.getPoss_accm_amt());//总的可用积分
			BigDecimal arPoint12=new BigDecimal(0);//失效的积分
			
			if(list!=null&&list.size()>0){
				
				for (Point point : list) {
					
					String etr_date=point.getEtr_date();//创建
					int etr_date_year=Integer.valueOf(etr_date.substring(0, 4));
					
					//判断，如果是当前年的数据，则进行加减
					if(etr_date.startsWith(now_year)){
						//判断是否为1,2月
						if("1".equals(etr_date.substring(5,7))||"2".equals(etr_date.substring(5,7))){
							if(!reduceStr.contains(point.getAccm_rsn_cd())){
								arPoint12=arPoint12.add(new BigDecimal(point.getAccm_amt()));//获得积分
							}
						}
					}

					//判断查询条件
					if(StringUtils.isNotEmpty(time)){ //过滤条件  年
						int time_year = Integer.valueOf(inputParam.getTime());
						if(time_year>=0){
							if(etr_date_year!=time_year){
								continue;
							}
						}else{//若果传入的年小于0 则属于XXXX年以上  不包含XXXX年
							if(etr_date_year>=(-time_year)){
								continue;
							}
						}
					}
					
					MyPoints myPoints = new MyPoints();
					myPoints.setEtr_date(point.getEtr_date());//创建时间
					
					
					if(reduceStr.contains(point.getAccm_rsn_cd())){
						myPoints.setConsume_point(point.getAccm_amt());//消耗积分
					}else{
						myPoints.setGet_point(point.getAccm_amt());//获得积分
					}
					myPoints.setRemark(bConfig("familyhas.memberInfo_myPoints_"+point.getAccm_rsn_cd()).substring(3));//描述
					pointsList.add(myPoints);
				}
			}
			
			totalNum=pointsList.size();
			if(endNum>=totalNum){
				endNum = totalNum;
				more = 0;
			}
			//如果起始条件大于总数则返回0条数据
			if(startNum>totalNum){
				startNum = 0;
				endNum = 0;
				more = 0;
			}
			
			userInfoQueryFromHomeResult.setPoints(pointsList.subList(startNum, endNum));
			
			userInfoQueryFromHomeResult.setAvailablePoint(String.valueOf(poss_accm_amt));
			
			
//			失效积分数计算方法：
//			1、如当前时间为3-12月，失效积分数为可用积分；
//			2、如当前时间为1-2月，失效积分数为可用积分减去1-2月获得积分，如差值为负按0结果
			
			if(now_month>=3&&now_month<=12){
				arPoint11=poss_accm_amt;
				userInfoQueryFromHomeResult.setPointTip(FormatHelper.formatString(ar11,arPoint11,now_year+"-12-31"));
			}else{
				arPoint11=poss_accm_amt.subtract(arPoint12);
				if(arPoint11.compareTo(new BigDecimal(0))<0){
					arPoint11=new BigDecimal(0);
				}
				
				userInfoQueryFromHomeResult.setPointTip(FormatHelper.formatString(ar11,arPoint11,DateUtilA.toString(DateUtilA.getlastDay(DateUtilA.getYear(null)+1, 2), DateUtilA.DATE_FORMAT_DATEONLY)));
			}
			
		}else if("1".equals(type)){ //1：用户礼金卷
			
			//TODO 这块数据暂时不显示，因为数据不全
			
//			List<MyGiftCard> giftCardList = userInfoQueryFromHomeResult.getGiftCard();
//			List<CashGift> cashGiftList = custInfo.getCashGiftList();
//			BigDecimal availableGiftCard = new BigDecimal(0);//
//			if(cashGiftList!=null&&cashGiftList.size()>0){
//				
//				for (CashGift cashGift : cashGiftList) {
//					
//					String status = inputParam.getStatus();//0：未用、1：已用、2：失效
//					String sy_vl=cashGift.getSy_vl();//是否使用（Y:已用 N：未用）
//					String vl_yn=cashGift.getVl_yn();//是否有效（Y：有效 N：无效）
//					
//					
//					//把未用有效的礼金券统计一下
//					if("N".equals(sy_vl)&&"Y".equals(vl_yn)){
//						availableGiftCard=availableGiftCard.add(new BigDecimal(cashGift.getLj_amt()));
//					}
//					
//					
//					//条件过滤
//					if("0".equals(status)){//未用 ：  未用 有效
//						if("Y".equals(sy_vl)||"N".equals(vl_yn)){
//							continue;
//						}
//					} else if("1".equals(status)){//已用：   未用 有效
//						if("N".equals(sy_vl)){
////						if("N".equals(sy_vl)||"N".equals(vl_yn)){
//							continue;
//						}
//					}else if("2".equals(status)) { //全部失效包含  未用 无效
//						if("Y".equals(sy_vl)||"Y".equals(vl_yn)){
//							continue;
//						}
//					}else{
//						break;
//					}
//					
//					MyGiftCard myGiftCard = new MyGiftCard();
//					myGiftCard.setGiftCard_end_date(cashGift.getEnd_date());//有效期结束时间/失效日期
////					myGiftCard.setGiftCard_fre_date(cashGift.getFr_date());//有效期开始时间
//					myGiftCard.setGiftCard_min_txtPrice("");//最低消费额//TODO 
//					myGiftCard.setGiftCard_name("");//礼金券名称//TODO
//					myGiftCard.setGiftCard_num("1");//数量 固定值为1
//					myGiftCard.setGiftCard_par(cashGift.getLj_amt());//面值
//					myGiftCard.setGiftCard_source(bConfig("familyhas.memberInfo_myGiftCard_source"));//来源
//					myGiftCard.setGiftCard_txtPrice_num("1");//消费数量 固定值为1
//					myGiftCard.setGiftCard_txtPrice_orderid(cashGift.getLj_rel_id());//消费订单
//					giftCardList.add(myGiftCard);
//					
//				}
//			}
//			
//			totalNum=giftCardList.size();
//			if(endNum>=totalNum){
//				endNum = totalNum;
//				more = 0;
//			}
//			//如果起始条件大于总数则返回0条数据
//			if(startNum>totalNum){
//				startNum = 0;
//				endNum = 0;
//				more = 0;
//			}
//			
//			userInfoQueryFromHomeResult.setGiftCard(giftCardList.subList(startNum, endNum));
//			
//			userInfoQueryFromHomeResult.setAvailableGiftCard(String.valueOf(availableGiftCard));
			
		} else if("2".equals(type)){ //2：用户储值金
			
			String increaseStr=bConfig("familyhas.memberInfo_myStoreGold_increase");
			String reduceStr=bConfig("familyhas.memberInfo_myStoreGold_reduce");
			
			List<MyStoreGold> storeGoldList = userInfoQueryFromHomeResult.getStoreGold();
			
			List<StoredGold> storedGoldList = custInfo.getStoredGoldList();
			
			if(storedGoldList!=null&&storedGoldList.size()>0){
				for (StoredGold storedGold : storedGoldList) {
					
					//判断查询条件
					String etr_date=storedGold.getEtr_date();
					int etr_date_year=Integer.valueOf(etr_date.substring(0, 4));
					
					//判断查询条件
					if(StringUtils.isNotEmpty(time)){ //过滤条件  年
						int time_year = Integer.valueOf(inputParam.getTime());
						if(time_year>=0){
							if(etr_date_year!=time_year){
								continue;
							}
						}else{//若果传入的年小于0 则属于XXXX年以上  不包含XXXX年
							if(etr_date_year>=(-time_year)){
								continue;
							}
						}
					}
					
					
					
					
					
					
					
					
					
					String remark=bConfig("familyhas.memberInfo_myPoints_"+storedGold.getPpc_rsn_cd());
					MyStoreGold myStoreGold = new MyStoreGold();
					myStoreGold.setRemark(remark.substring(3));
					myStoreGold.setStoreGold_etr_date(etr_date);
					if(increaseStr.contains(storedGold.getPpc_rsn_cd())){
						myStoreGold.setStoreGold_income(storedGold.getPpc_amt());//收入
					}else{
						myStoreGold.setStoreGold_expend(storedGold.getPpc_amt());//支出	
					}
					storeGoldList.add(myStoreGold);
				}
			}
			
			totalNum=storeGoldList.size();
			if(endNum>=totalNum){
				endNum = totalNum;
				more = 0;
			}
			//如果起始条件大于总数则返回0条数据
			if(startNum>totalNum){
				startNum = 0;
				endNum = 0;
				more = 0;
			}
			
			userInfoQueryFromHomeResult.setStoreGold(storeGoldList.subList(startNum, endNum));
			userInfoQueryFromHomeResult.setAvailableStoreGold(custInfo.getPoss_ppc_amt());
			
		} else if("3".equals(type)){ //3：用户暂存款
			
			String increaseStr=bConfig("familyhas.memberInfo_myTemporaryStore_increase");
			String reduceStr=bConfig("familyhas.memberInfo_myTemporaryStore_reduce");
			
			List<MyTemporaryStore> temporaryStoreList = userInfoQueryFromHomeResult.getTemporaryStore();
			List<TempGold>  tempGoldList = custInfo.getTempGoldList();
			if(tempGoldList!=null&&tempGoldList.size()>0){
				for (TempGold tempGold : tempGoldList) {
					//判断查询条件
					String etr_date=tempGold.getEtr_date();
					
					int etr_date_year=Integer.valueOf(etr_date.substring(0, 4));
					
					//判断查询条件
					if(StringUtils.isNotEmpty(time)){ //过滤条件  年
						int time_year = Integer.valueOf(inputParam.getTime());
						if(time_year>=0){
							if(etr_date_year!=time_year){
								continue;
							}
						}else{//若果传入的年小于0 则属于XXXX年以上  不包含XXXX年
							if(etr_date_year>=(-time_year)){
								continue;
							}
						}
					}
					
					String remark=bConfig("familyhas.memberInfo_myTemporaryStore_"+tempGold.getCrdt_cd());
					MyTemporaryStore myTemporaryStore = new MyTemporaryStore();
					myTemporaryStore.setRemark(remark.substring(4));
					myTemporaryStore.setStoreGold_etr_date(etr_date);
					
					
					if(increaseStr.contains(tempGold.getCrdt_cd())){
						myTemporaryStore.setStoreGold_income(tempGold.getCrdt_amt());//收入
					}else{
						myTemporaryStore.setStoreGold_expend(tempGold.getCrdt_amt());//支出
					}
					temporaryStoreList.add(myTemporaryStore);
				}
			}
			
			totalNum=temporaryStoreList.size();
			if(endNum>=totalNum){
				endNum = totalNum;
				more = 0;
			}
			//如果起始条件大于总数则返回0条数据
			if(startNum>totalNum){
				startNum = 0;
				endNum = 0;
				more = 0;
			}
			
			userInfoQueryFromHomeResult.setTemporaryStore(temporaryStoreList.subList(startNum, endNum));
			
			userInfoQueryFromHomeResult.setAvailableTemporaryStore(custInfo.getPoss_crdt_amt());
		}
		
		
		//分页信息
		PageResults pageResults = userInfoQueryFromHomeResult.getPaged();
		pageResults.setTotal(totalNum);
		pageResults.setCount(endNum-startNum);
		pageResults.setMore(more);
		
		return userInfoQueryFromHomeResult;
	}
	
}


