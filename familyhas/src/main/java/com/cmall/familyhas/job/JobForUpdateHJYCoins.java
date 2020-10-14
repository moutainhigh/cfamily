package com.cmall.familyhas.job;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import com.srnpr.xmassystem.invoke.ref.CustRelAmtRef;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput.ChildOrder;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;
/**
 * 
 *<p>Description:定时变更惠币可提现金额 <／p> 
 * @author zb
 * @date 2020年7月8日
 *
 */
public class JobForUpdateHJYCoins extends  RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		// TODO Auto-generated method stub
		
		//查询有惠币回馈的订单   daguan_recommend_testdata 测试Y 灰度M 生产N，zid是取的此功能上线前的数据，防止扫描数据过多造成资源浪费
		int zid =0 ;
		if("Y".equals(TopConfig.Instance.bConfig("familyhas.daguan_recommend_testdata"))) {
			//测试环境
			zid = 298093;
		}else if("M".equals(TopConfig.Instance.bConfig("familyhas.daguan_recommend_testdata"))){
			//灰度
			zid = 3512133;
		}else {
			//生产
			zid = 4734594;
		}
		 List<Map<String, Object>> sqlList = DbUp.upTable("oc_orderdetail").dataSqlList("select IFNULL(sum(a.give_hjycoin),0) give_hjycoin ,a.order_code,b.buyer_code buyer_code from ordercenter.oc_orderdetail a,ordercenter.oc_orderinfo b where b.order_status='4497153900010005' and  b.zid>"+zid+" and b.small_seller_code!='SI2003' and a.order_code=b.order_code and b.update_time<DATE_SUB(now(),INTERVAL 15 DAY) and b.update_time > DATE_SUB(now(), INTERVAL 20 DAY) and NOT EXISTS(select c.code from familyhas.fh_hjycoin_oper_details c where c.code=b.order_code) group BY order_code ", null);

		 if(sqlList!=null&&sqlList.size()>0) {
			 PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
			 CustRelAmtRef custRelAmtRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
			 for(Iterator<Map<String, Object>> iterator = sqlList.iterator();iterator.hasNext();) {
				 Map<String, Object> next = iterator.next();
				 String order_code = next.get("order_code").toString();
				 BigDecimal give_hjycoin = new BigDecimal(next.get("give_hjycoin").toString());
				 String buyer_code = next.get("buyer_code").toString();
				 BigDecimal reMoney = new BigDecimal("0");
				 //此定时不再考虑退货单
				/* Map<String, Object> sqlOne = DbUp.upTable("oc_return_goods").dataSqlOne("select IFNULL(sum(expected_return_give_hjycoin_money),0) reMoney from oc_return_goods where order_code=:order_code and flag_return_goods='4497477800090001' and status!='4497153900050007' ", new MDataMap("order_code",order_code));
				 if(sqlOne!=null&&sqlOne.size()>0) {
					  reMoney = new BigDecimal(sqlOne.get("reMoney").toString());
				 }*/
				BigDecimal finalMoney = MoneyHelper.round(3,BigDecimal.ROUND_HALF_UP,give_hjycoin.subtract(reMoney));
				//如果全部退货，则不做转正处理
				 {
					 if(finalMoney.compareTo(BigDecimal.ZERO)>0) {
						 UpdateCustAmtInput paramInput = new UpdateCustAmtInput();
							paramInput.setCurdFlag(UpdateCustAmtInput.CurdFlag.ZZHB);
							String custId = plusServiceAccm.getCustId(buyer_code);
							paramInput.setCustId(custId);
							ChildOrder childOrder = new ChildOrder();
							childOrder.setAppChildOrdId(order_code);
							childOrder.setChildHcoinAmt(finalMoney);
							paramInput.getOrderList().add(childOrder);
							RootResult updateCustAmt = custRelAmtRef.updateCustAmt(paramInput);
							if(updateCustAmt.getResultCode()==1) {
								MDataMap insertMap = new MDataMap();
								insertMap.put("uid",WebHelper.upUuid() );
								insertMap.put("code",order_code );
								insertMap.put("hjycoin",finalMoney.toString());
								insertMap.put("oper_type", "449748650001");
								insertMap.put("member_code", buyer_code);
								insertMap.put("create_time", DateHelper.upNow());
								DbUp.upTable("fh_hjycoin_oper_details").dataInsert(insertMap);
							} 
					 }
					
					//再判断此订单是否有推广人收益
					List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_tgz_order_detail").dataSqlList("select * from fh_tgz_order_detail  where  order_code=:order_code ", new MDataMap("order_code",order_code));
					if(dataSqlList!=null&&dataSqlList.size()>0) {
						//按照推广人分组处理
						Map<String,List<Map<String, Object>>> groupMap = new HashMap();
						for (Map<String, Object> map : dataSqlList) {
							if(groupMap.containsKey(map.get("tgz_member_code"))) {
								groupMap.get(map.get("tgz_member_code")).add(map);
							}else {
								List<Map<String, Object>> subList = new ArrayList<>();
								subList.add(map);
								groupMap.put(map.get("tgz_member_code").toString(), subList);
							}
						}
						
						//存在退款单，排除之
						if(reMoney.compareTo(BigDecimal.ZERO)>0) {
							//存在退货订单，排除这一部分惠币回馈
							UpdateCustAmtInput subParamInput = new UpdateCustAmtInput();
							subParamInput.setCurdFlag(UpdateCustAmtInput.CurdFlag.ZZHB);
							List<Map<String,Object>> returnDetailsList = DbUp.upTable("oc_return_goods_detail").dataSqlList("select a.* from oc_return_goods_detail a,oc_return_goods b where a.return_code=b.return_code and b.order_code=:order_code ", new MDataMap("order_code",order_code));
							if(returnDetailsList!=null&&returnDetailsList.size()>0) {
								for(Iterator<String> subiterator = groupMap.keySet().iterator();subiterator.hasNext();) {
									String memberCode = subiterator.next();
									List<Map<String, Object>> sublist = groupMap.get(memberCode);
									String subcustId = plusServiceAccm.getCustId(memberCode);
									subParamInput.setCustId(subcustId);
									ChildOrder subChildOrder = new ChildOrder();
									subChildOrder.setAppChildOrdId(order_code);
									Map<String,Map<String, Object>> temMap = new HashMap();
									for (Map<String, Object> map : sublist) {
										subChildOrder.setChildHcoinAmt(subChildOrder.getChildHcoinAmt().add(new BigDecimal(map.get("tgz_money").toString())));
										temMap.put(map.get("sku_code").toString(), map);
									}
									for (Map<String, Object> map : returnDetailsList) {
										String sku_code = MapUtils.getString(map, "sku_code","");
										Integer skuNum = MapUtils.getInteger(map, "count",0);
										Map<String, Object> map2 = temMap.get(sku_code);
										BigDecimal returnMoney = new BigDecimal(map2.get("tgz_money").toString()).multiply(new BigDecimal(skuNum)).divide(new BigDecimal(map2.get("sku_num").toString()));
										subChildOrder.setChildHcoinAmt(subChildOrder.getChildHcoinAmt().subtract(returnMoney));
									}
									List<ChildOrder> orderList = new ArrayList<ChildOrder>();
									orderList.add(subChildOrder);
									subParamInput.setOrderList(orderList);
									RootResult supdateCustAmt = custRelAmtRef.updateCustAmt(subParamInput);
									if(supdateCustAmt.getResultCode()==1) {
										MDataMap insertMap = new MDataMap();
										insertMap.put("uid",WebHelper.upUuid() );
										insertMap.put("code",order_code );
										insertMap.put("hjycoin",subChildOrder.getChildHcoinAmt().toString());
										insertMap.put("oper_type", "449748650001");
										insertMap.put("member_code", dataSqlList.get(0).get("tgz_member_code").toString());
										insertMap.put("create_time", DateHelper.upNow());
										DbUp.upTable("fh_hjycoin_oper_details").dataInsert(insertMap);
									}
								}							
							}
						  }else {
								UpdateCustAmtInput subParamInput = new UpdateCustAmtInput();
								subParamInput.setCurdFlag(UpdateCustAmtInput.CurdFlag.ZZHB);
								for(Iterator<String> subiterator = groupMap.keySet().iterator();subiterator.hasNext();) {
									String memberCode = subiterator.next();
									List<Map<String, Object>> sublist = groupMap.get(memberCode);
									String subcustId = plusServiceAccm.getCustId(memberCode);
									subParamInput.setCustId(subcustId);
									ChildOrder subChildOrder = new ChildOrder();
									subChildOrder.setAppChildOrdId(order_code);
									for (Map<String, Object> map : sublist) {
										subChildOrder.setChildHcoinAmt(subChildOrder.getChildHcoinAmt().add(new BigDecimal(map.get("tgz_money").toString())));	
									}
									List<ChildOrder> orderList = new ArrayList<ChildOrder>();
									orderList.add(subChildOrder);
									subParamInput.setOrderList(orderList);
									RootResult supdateCustAmt = custRelAmtRef.updateCustAmt(subParamInput);
									if(supdateCustAmt.getResultCode()==1) {
										MDataMap insertMap = new MDataMap();
										insertMap.put("uid",WebHelper.upUuid() );
										insertMap.put("code",order_code );
										insertMap.put("hjycoin",subChildOrder.getChildHcoinAmt().toString());
										insertMap.put("oper_type", "449748650001");
										insertMap.put("member_code", dataSqlList.get(0).get("tgz_member_code").toString());
										insertMap.put("create_time", DateHelper.upNow());
										DbUp.upTable("fh_hjycoin_oper_details").dataInsert(insertMap);
									}
								}
								
							}
					}

				}
			 }
		 }

	}
	
}
