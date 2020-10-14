package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.model.ChangeGoods;
import com.cmall.familyhas.model.ChangeGoods.ChangeGoodsDetail;
import com.cmall.familyhas.service.ChangeGoodsService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 采购提交
 * @author zhangbo
 *
 */
public class FuncCommitPurchase extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);		
		String purchase_order_id=mAddMaps.get("purchase_order_id").trim();
		String purchase_text=mAddMaps.get("purchase_text")==null?"":mAddMaps.get("purchase_text");
		String basic_order_skus=mAddMaps.get("basic_order_skus");
		String adress_id=mAddMaps.get("adress_id");
		String[] split = basic_order_skus.split(",");
		int purchase_num = 0;
		BigDecimal purchase_money = BigDecimal.ZERO;
		String upUuid = WebHelper.upUuid();
		String userName =UserFactory.INSTANCE.create().getRealName();
		if(StringUtils.startsWith(purchase_order_id, "TEM")) {
			String newPurChaseId = WebHelper.upCode("PCI");
			String phone = "";
			
			//新增数据
			for (String subSplit : split) {
				String[] split2 = subSplit.split("_");
				String skuCode = split2[0];
				String costPrice =split2[1];
				String sellPrice = split2[2];
				String num = split2[3];
				String ifSelect = split2[4];
				if("1".equals(ifSelect)) {
					purchase_num = purchase_num+Integer.parseInt(num);
					BigDecimal sellP = new BigDecimal(sellPrice);
					purchase_money=purchase_money.add(sellP.multiply(new BigDecimal(num)));
				}
				//插入订单详情表
				MDataMap paramMap1 = new MDataMap();
				Map<String, Object> skuMap = DbUp.upTable("pc_skuinfo").dataSqlOne("select * from pc_skuinfo where sku_code=:sku_code ", new MDataMap("sku_code",skuCode));
				paramMap1.put("uid",upUuid );
				paramMap1.put("sku_code", skuCode);
				paramMap1.put("product_code",skuMap.get("product_code").toString());
				paramMap1.put("product_name",skuMap.get("sku_name").toString() );
				paramMap1.put("product_img",skuMap.get("sku_picurl").toString() );
				paramMap1.put("product_property",skuMap.get("sku_keyvalue").toString() );
				paramMap1.put("cost_money", costPrice);
				paramMap1.put("sell_money",sellPrice );
				paramMap1.put("order_creater", userName);
				paramMap1.put("purchase_order_id",newPurChaseId );
				paramMap1.put("if_selected",ifSelect );
				paramMap1.put("sku_num",num );
				paramMap1.put("order_id", WebHelper.upCode("DD"));
				DbUp.upTable("oc_purchase_order_detail").dataInsert(paramMap1);

			}
			//更新地址表
			List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_purchase_order_address").dataSqlList("select * from oc_purchase_order_address where if_delete='1' ",null);
			for (Map<String, Object> map : dataSqlList) {
				if(adress_id.equals(map.get("adress_id").toString())) {
					//DbUp.upTable("oc_purchase_order_address").dataUpdate(new MDataMap("purchase_order_id",newPurChaseId,"adress_id",map.get("adress_id").toString(),"select_flag","1"), "purchase_order_id,select_flag", "adress_id");
					phone = map.get("phone").toString();
				}else {
					//DbUp.upTable("oc_purchase_order_address").dataUpdate(new MDataMap("purchase_order_id",newPurChaseId,"adress_id",map.get("adress_id").toString()), "purchase_order_id", "adress_id");
				}
			}
			//插入采购主表数据
			MDataMap mDataMap2 = new MDataMap();
			mDataMap2.put("uid", upUuid);
			mDataMap2.put("purchase_order_id",newPurChaseId );
			mDataMap2.put("purchase_num", purchase_num+"");
			mDataMap2.put("purchase_money", purchase_money.toString());
			mDataMap2.put("creater", userName);
			mDataMap2.put("purchase_time",DateUtil.getSysDateTimeString() );
			mDataMap2.put("phone", phone);
			mDataMap2.put("purchase_text",purchase_text );
			mDataMap2.put("basic_order_skus", basic_order_skus);
			mDataMap2.put("adress_id", adress_id);
			DbUp.upTable("oc_purchase_order").dataInsert(mDataMap2);
						
			//插入审核表
			MDataMap mDataMap3 = new MDataMap();
			mDataMap3.put("uid", upUuid);
			mDataMap3.put("purchase_order_id",newPurChaseId );
			mDataMap3.put("purchase_order_status", "449748490001");
			mDataMap3.put("purchase_order_text", "");
			mDataMap3.put("creater", userName);
			mDataMap3.put("create_time",DateUtil.getSysDateTimeString() );
			DbUp.upTable("oc_purchase_order_shenpi").dataInsert(mDataMap3);
			
		}else {
			 //修改数据
             //查询采购订单详情表
			 List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_purchase_order_detail").dataSqlList("select * from oc_purchase_order_detail where purchase_order_id=:purchase_order_id and if_selected='1' ", new MDataMap("purchase_order_id",purchase_order_id));
             List<String> skuList = new ArrayList<String>();
             List<String> paramSkuList = new ArrayList<String>();
			 for (Map<String, Object> map : dataSqlList) {
				 skuList.add(map.get("sku_code").toString());
			}
			 String phone = "";		
			//新增数据
			for (String subSplit : split) {
				String[] split2 = subSplit.split("_");
				String skuCode = split2[0];
				String costPrice =split2[1];
				String sellPrice = split2[2];
				String num = split2[3];
				String ifSelect = split2[4];
				paramSkuList.add(skuCode);
				if("1".equals(ifSelect)) {
					purchase_num = purchase_num+Integer.parseInt(num);
					BigDecimal sellP = new BigDecimal(sellPrice);
					purchase_money =purchase_money.add(sellP.multiply(new BigDecimal(num)));
				}
				//不存在
				//插入订单详情表
				if(!skuList.contains(skuCode)) {
					MDataMap paramMap1 = new MDataMap();
					Map<String, Object> skuMap = DbUp.upTable("pc_skuinfo").dataSqlOne("select * from pc_skuinfo where sku_code=:sku_code ", new MDataMap("sku_code",skuCode));
					paramMap1.put("uid",upUuid );
					paramMap1.put("sku_code", skuCode);
					paramMap1.put("product_code",skuMap.get("product_code").toString());
					paramMap1.put("product_name",skuMap.get("sku_name").toString() );
					paramMap1.put("product_img",skuMap.get("sku_picurl").toString() );
					paramMap1.put("product_property",skuMap.get("sku_keyvalue").toString() );
					paramMap1.put("cost_money", costPrice);
					paramMap1.put("sell_money",sellPrice );
					paramMap1.put("order_creater", userName);
					paramMap1.put("purchase_order_id",purchase_order_id );
					paramMap1.put("if_selected",ifSelect );
					paramMap1.put("sku_num",num );
					paramMap1.put("order_id", WebHelper.upCode("DD"));
					DbUp.upTable("oc_purchase_order_detail").dataInsert(paramMap1);	
				}
				else {
					//存在，更新
					MDataMap paramMap1 = new MDataMap();
					paramMap1.put("sku_code", skuCode);
					paramMap1.put("cost_money", costPrice);
					paramMap1.put("sell_money",sellPrice );
					paramMap1.put("order_creater", userName);
					paramMap1.put("purchase_order_id",purchase_order_id );
					paramMap1.put("if_selected",ifSelect);
					paramMap1.put("sku_num",num );
					DbUp.upTable("oc_purchase_order_detail").dataUpdate(paramMap1,"cost_money,sell_money,sku_num,if_selected","sku_code,purchase_order_id");		
					
				}					
			}
			skuList.removeAll(paramSkuList);
			if(skuList.size()>0) {
				for (String skuCode : skuList) {
					DbUp.upTable("oc_purchase_order_detail").dataUpdate(new MDataMap("sku_code",skuCode,"purchase_order_id",purchase_order_id,"if_delete","0"),"if_delete","sku_code,purchase_order_id");		
				}
			}
			
			//更新地址表
			//Map<String, Object> dataSqlOne = DbUp.upTable("oc_purchase_order_address").dataSqlOne("select * from oc_purchase_order_address where purchase_order_id=:purchase_order_id and select_flag='1'  ", new MDataMap("purchase_order_id",purchase_order_id));
			//phone = dataSqlOne.get("phone").toString();
			/*if(!adress_id.equals(dataSqlOne.get("adress_id").toString())) {
				DbUp.upTable("oc_purchase_order_address").dataUpdate(new MDataMap("adress_id",dataSqlOne.get("adress_id").toString(),"select_flag","0"), "select_flag", "adress_id");
				DbUp.upTable("oc_purchase_order_address").dataUpdate(new MDataMap("adress_id",adress_id,"select_flag","1"), "select_flag", "adress_id");
				MDataMap one = DbUp.upTable("oc_purchase_order_address").one("adress_id",adress_id);
				phone = one.get("phone").toString();
			}*/
			MDataMap one = DbUp.upTable("oc_purchase_order_address").one("adress_id",adress_id);
			phone = one.get("phone").toString();
			//更新采购主表数据
			MDataMap mDataMap2 = new MDataMap();
			mDataMap2.put("purchase_order_id",purchase_order_id );
			mDataMap2.put("purchase_num", purchase_num+"");
			mDataMap2.put("purchase_money", purchase_money.toString());
			mDataMap2.put("phone", phone);
			mDataMap2.put("purchase_text",purchase_text);
			mDataMap2.put("basic_order_skus", basic_order_skus);
			mDataMap2.put("adress_id", adress_id);
			mDataMap2.put("status", "449748490001");
			DbUp.upTable("oc_purchase_order").dataUpdate(mDataMap2, "purchase_num,purchase_money,phone,purchase_text,basic_order_skus,adress_id,status", "purchase_order_id");
						
			//插入审核表
			MDataMap mDataMap3 = new MDataMap();
			mDataMap3.put("uid", upUuid);
			mDataMap3.put("purchase_order_id",purchase_order_id );
			mDataMap3.put("purchase_order_status", "449748490001");
			mDataMap3.put("purchase_order_text", "");
			mDataMap3.put("creater", userName);
			mDataMap3.put("create_time",DateUtil.getSysDateTimeString() );
			DbUp.upTable("oc_purchase_order_shenpi").dataInsert(mDataMap3);

		}
	    
		
	   return mResult;
	
	}
	
	

}
