package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cmall.familyhas.model.BillInfo;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.familyhas.service.ShopCartServiceForBeauty;
import com.cmall.productcenter.common.DateUtil;
import com.cmall.productcenter.model.PcFreeTryOutGood;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 *  对于试用商品申请试用状态进行修改
 * @author houwen
 *
 */
public class TryOutApplyStatus extends RootFunc {

	
	private static String TABLE_TPL="nc_freetryout_apply"; //免费试用商品申请表
	
	
	/**
	 * 
	 *  (non-Javadoc)
	 * @see com.srnpr.zapweb.webface.IWebFunc#funcDo(java.lang.String, com.srnpr.zapcom.basemodel.MDataMap)
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		mResult.setResultCode(1);
		String tplUid = mDataMap.get("zw_f_uid");
		String end_time = mDataMap.get("zw_f_time");
		MDataMap dataMap = new MDataMap();
		MDataMap mDataMap2 = new MDataMap();
		MDataMap map = new MDataMap();
		ProductService productService = new ProductService();
		dataMap.put("uid", tplUid);
		mDataMap2.put("uid", tplUid);
		List<Map<String, Object>> maps = DbUp.upTable("nc_freetryout_apply").dataQuery("", "", "", mDataMap2, -1, -1);
		//申请状态 ：未申请：449746890001；已申请：449746890002；申请通过：449746890003；449746890004：已结束
		String isDisable = maps.get(0).get("status").toString();
		if("449746890002".equals(isDisable)){  //如果状态为已申请，状态改为申请通过
			
			map.put("sku_code", maps.get(0).get("sku_code").toString());
			map.put("status", "449746890003");
			map.put("end_time", end_time);
			List<Map<String, Object>> mapss = DbUp.upTable("nc_freetryout_apply").dataQuery("", "", "", map, -1, -1);
			int count = mapss.size();
			List<Map<String, Object>> list = productService.getMyTryOutGoodsForSkuCode(maps.get(0).get("sku_code").toString(), "449746930003", maps.get(0).get("member_code").toString(), maps.get(0).get("app_code").toString(),end_time);
			if(list.size()!=0){
			PcFreeTryOutGood pcFreeTryOutGood = (PcFreeTryOutGood) list.get(0).get("freeGood");
			int tryoutCount = pcFreeTryOutGood.getInitInventory();
		/*	if (nowTime.compareTo(end_time)>0) {
				mResult.setResultCode(934205104);
				mResult.setResultMessage("试用商品活动已结束！");
			}else {*/
			
			if(count<tryoutCount){
				
				ShopCartServiceForBeauty Service = new ShopCartServiceForBeauty();
	
				//校验购买条件是否符合
				List<GoodsInfoForAdd> goods = new ArrayList<GoodsInfoForAdd>();
				GoodsInfoForAdd goodss = new GoodsInfoForAdd();
				goodss.setSku_code(maps.get(0).get("sku_code").toString());
				goodss.setSku_num(1);
				goodss.setProduct_code(pcFreeTryOutGood.getpInfo().getProductCode());
				goods.add(goodss);
				RootResult rootResult = Service.checkGoodsStock(maps.get(0).get("member_code").toString(), goods);
				if(rootResult.getResultCode()==1){
					rootResult = Service.checkGoodsLimit(maps.get(0).get("app_code").toString(),maps.get(0).get("member_code").toString(), goods);
					if(rootResult.getResultCode()>1){
						mResult.setResultCode(rootResult.getResultCode());
						mResult.setResultMessage(rootResult.getResultMessage());
						return mResult;
					}
				}else {
					mResult.setResultCode(rootResult.getResultCode());
					mResult.setResultMessage(rootResult.getResultMessage());
					return mResult;
				}
				
				Map<String, Object> mapsss = new HashMap<String, Object>();
				BillInfo billInfo = new BillInfo();
				mapsss.put("order_type", "449715200003");//订单类型
				mapsss.put("order_souce", maps.get(0).get("ordersource"));//订单来源
				mapsss.put("buyer_code", maps.get(0).get("member_code"));//买家编号
				mapsss.put("buyer_name", maps.get(0).get("address_name"));//收货人姓名
				mapsss.put("buyer_address", maps.get(0).get("address_street"));//收件人地址
				mapsss.put("buyer_address_code", maps.get(0).get("address_county"));//收货人地址第三极编号
				mapsss.put("buyer_mobile", maps.get(0).get("address_mobile"));//收件人手机号
				mapsss.put("check_pay_money", 0);
				mapsss.put("pay_type", "");//支付类型
				mapsss.put("billInfo", billInfo);//发票信息 bill_Type发票类型  bill_title发票抬头 bill_detail发票内容
				mapsss.put("goods", goods);//商品列表
				mapsss.put("seller_code", maps.get(0).get("app_code"));
				mapsss.put("app_vision", "1.0");
				Map<String, Object> re= Service.createOrder(mapsss);
				
				if(re.containsKey("check_pay_money_error")){
					mResult.setResultCode(916401133);
					mResult.setResultMessage(re.get("check_pay_money_error").toString());
					return mResult;
				}
				
				if(re.containsKey("error")&&re.get("error")!=null&&!"".equals(re.get("error").toString())&&(re.containsKey("order_code")&&!re.get("order_code").toString().equals(re.get("error").toString()))){
					mResult.setResultCode(2);
					mResult.setResultMessage(re.get("error").toString());
				}else {
					
					if(!"".equals(re.get("order_code").toString())&&null!=re.get("order_code").toString()){

						MDataMap mpayDataMap = new MDataMap();
						
						mpayDataMap.put("order_code", re.get("order_code").toString());
						
						mpayDataMap.put("pay_type", "449746280003");
						
						mpayDataMap.put("merchant_id", maps.get(0).get("member_code").toString());
						
						mpayDataMap.put("payed_money", "0.00");
						
						mpayDataMap.put("pay_remark", "免费试用商品");
						
						DbUp.upTable("oc_order_pay").dataInsert(mpayDataMap);
						
					}
				}
				
				if(re.get("order_code")!=null && !re.get("order_code").equals("")){
					dataMap.put("status", "449746890003");
					DbUp.upTable(TABLE_TPL).dataUpdate(dataMap, "status", "uid");
					
					//推送表中插入一条消息
					
					MDataMap dbDataMap = DbUp.upTable("nc_sys_push_configure").one("configure_code","TS449746660001","status","449747090001","app_code",UserFactory.INSTANCE.create().getManageCode());
					
					if(dbDataMap!=null && !dbDataMap.isEmpty()){
						
						String start = dbDataMap.get("push_time_start").toString();
						
						String end = dbDataMap.get("push_time_end").toString();
						
						String now = DateUtil.getSysTimeString();
						
						int num1 = now.compareTo(start);
						
						int num2 = end.compareTo(now);
						
						Boolean flag = num1>=0 && num2>=0;
						
						if(start.equals("全天") || flag ){
							
							MDataMap dataMap2 = DbUp.upTable("pc_skuinfo").one("sku_code",map.get("sku_code").toString());
							String jump_position = map.get("sku_code").toString()+","+map.get("end_time").toString();
							String buyer_code = maps.get(0).get("member_code").toString();
							String sku_name = dataMap2.get("sku_name").toString();
							
							String content = dbDataMap.get("comment").replace("###", sku_name);
							
							MDataMap insertmap = new MDataMap();

							insertmap.inAllValues("accept_member",buyer_code,"comment", content , "push_time",DateUtil.getSysDateTimeString(), "jump_type", "5","jump_position",jump_position, "push_status","4497465000070001", "create_time",DateUtil.getSysDateTimeString(), "app_code",UserFactory.INSTANCE.create().getManageCode());
							
							DbUp.upTable("nc_comment_push_system").dataInsert(insertmap);
						}
						
					}
				}
			
		}else{
			mResult.setResultCode(934205104);
			mResult.setResultMessage("试用商品申请通过人数已满！");
		}
		}
	   }else{
			mResult.setResultCode(934205104);
			mResult.setResultMessage("数据已审核，状态不可再修改！");
		}
		
		return mResult;
 }
	
}
