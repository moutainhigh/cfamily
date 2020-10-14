package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cmall.familyhas.api.ApiCheckUnshelveProduct.CheckUnshelveProductInput;
import com.cmall.familyhas.api.ApiCheckUnshelveProduct.CheckUnshelveProductResult;
import com.cmall.systemcenter.common.DateUtil;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;

public class ApiCheckUnshelveProduct extends RootApi<CheckUnshelveProductResult,CheckUnshelveProductInput>{
	
	@Override
	public CheckUnshelveProductResult Process(CheckUnshelveProductInput inputParam, MDataMap mRequestMap) {
		CheckUnshelveProductResult result = new CheckUnshelveProductResult();
		//判断操作类型  1、审批选中：通过    2、审批选中：驳回    3、 审批全部  ：通过   4、审批全部  ：驳回
		String xiajiaFlag = inputParam.getXiajiaFlag();//0全部下架,1下架选中
		boolean checkIdea = inputParam.isCheckIdea();//审批意见 true为下架，false为驳回
		String xiajiaUids = inputParam.getXiajiaUids();
		if("0".equals(xiajiaFlag)) {//全部下架
			//从pc_product_xiajia_shenpi查询所有shenpi_status为0的商品
			String sql = "select zid,product_code from pc_product_xiajia_shenpi where shenpi_status =  '0'";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_product_xiajia_shenpi").dataSqlList(sql, null);
			if(checkIdea) {
				if(null!=dataSqlList&&dataSqlList.size()>0) {
					for(int i = 0;i<dataSqlList.size();i++) {
						Map<String, Object> map = dataSqlList.get(i);
						int zid =  (int)(map.get("zid"));
						String product_code = map.get("product_code").toString();
						//同意下架，将pc_product_xiajia_shenpi表所有shenpi_status为0的改为1已下架
						MDataMap updateMap = new MDataMap();
						updateMap.put("zid", zid+"");
						updateMap.put("shenpi_status", "1");
						DbUp.upTable("pc_product_xiajia_shenpi").dataUpdate(updateMap,
								"shenpi_status",
								"zid");
						//同意下架，将pc_productinfo表所有需下架商品的 product_status改为4497153900060003
						MDataMap updateMap2 = new MDataMap();
						updateMap2.put("product_code", product_code);
						updateMap2.put("product_status", "4497153900060003");
						int dataUpdate = DbUp.upTable("pc_productinfo").dataUpdate(updateMap2,
								"product_status",
								"product_code");
						
						if(dataUpdate>0) {
							//添加批量下架审核记录
							Map<String,Object> querMap =DbUp.upTable("pc_productinfo").dataSqlOne("select uid from pc_productinfo where product_code=:product_code", new MDataMap("product_code",product_code));
							UUID uuid2 = UUID.randomUUID();
							MDataMap insertDatamap = new MDataMap();
							insertDatamap.put("uid", uuid2.toString().replace("-", ""));
							insertDatamap.put("flow_code", querMap.get("uid").toString());
							insertDatamap.put("flow_type", "449717230011");
							insertDatamap.put("creator", UserFactory.INSTANCE.create().getUserCode());
							insertDatamap.put("create_time", DateUtil.getSysDateTimeString());
							insertDatamap.put("flow_remark", "商品批量审批下架");
							insertDatamap.put("current_status", "4497153900060003");
							DbUp.upTable("sc_flow_bussiness_history").dataInsert(insertDatamap);
							
							PlusHelperNotice.onChangeProductInfo(product_code);
							ProductJmsSupport productJmsSupport = new  ProductJmsSupport();
							productJmsSupport.updateSolrData(product_code);
							//更新分类商品数量表
							XmasKv.upFactory(EKvSchema.IsUpdateCategoryProductCount).set("isUpdateCateProd","update");
							//更新品牌商品数量表
							XmasKv.upFactory(EKvSchema.IsUpdateBrandProductCount).set("isUpdateBrandProductCount","update");
						}
					}
				}
			}else {
				if(null!=dataSqlList&&dataSqlList.size()>0) {
					for(int i = 0;i<dataSqlList.size();i++) {
						Map<String, Object> map = dataSqlList.get(i);
						int zid =  (int)(map.get("zid"));
						//驳回下架，将pc_product_xiajia_shenpi表所有shenpi_status为0的改为2驳回下架
						MDataMap updateMap = new MDataMap();
						updateMap.put("zid", zid+"");
						updateMap.put("shenpi_status", "2");
						DbUp.upTable("pc_product_xiajia_shenpi").dataUpdate(updateMap,
								"shenpi_status",
								"zid");
					}
				}
			}
		}else if("1".equals(xiajiaFlag)) {
			//从pc_product_xiajia_shenpi查询所有shenpi_status为0的商品
			String  uids = "";
			for(String str :  xiajiaUids.split(",")) {
				uids += "'"+str+"',";
			}
			uids = uids.substring(0, uids.length()-1);
			String sql = "select product_code from pc_product_xiajia_shenpi where uid in("+uids+")";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_product_xiajia_shenpi").dataSqlList(sql, null);
			if(checkIdea) {
				for(int i= 0;i<dataSqlList.size();i++) {
					//同意下架，循环xiajiaUids 将pc_product_xiajia_shenpi表 对应product_code并且shenpi_status为0的改为1已下架
					String uid = xiajiaUids.split(",")[i];
					MDataMap updateMap = new MDataMap();
					updateMap.put("uid", uid);
					updateMap.put("shenpi_status", "1");
					DbUp.upTable("pc_product_xiajia_shenpi").dataUpdate(updateMap,
							"shenpi_status",
							"uid");
					
					//同意下架，循环xiajiaUids 将pc_productinfo表 对应product_code商品的 product_status改为4497153900060003
					Map<String, Object> map = dataSqlList.get(i);
					String  product_code  = map.get("product_code").toString();
					MDataMap updateMap2 = new MDataMap();
					updateMap2.put("product_code", product_code);
					updateMap2.put("product_status", "4497153900060003");
					int dataUpdate = DbUp.upTable("pc_productinfo").dataUpdate(updateMap2,
							"product_status",
							"product_code");
					if(dataUpdate>0) {
						//添加批量下架审核记录
						Map<String,Object> querMap =DbUp.upTable("pc_productinfo").dataSqlOne("select uid from pc_productinfo where product_code=:product_code", new MDataMap("product_code",product_code));
						UUID uuid2 = UUID.randomUUID();
						MDataMap insertDatamap = new MDataMap();
						insertDatamap.put("uid", uuid2.toString().replace("-", ""));
						insertDatamap.put("flow_code", querMap.get("uid").toString());
						insertDatamap.put("flow_type", "449717230011");
						insertDatamap.put("creator", UserFactory.INSTANCE.create().getUserCode());
						insertDatamap.put("create_time", DateUtil.getSysDateTimeString());
						insertDatamap.put("flow_remark", "商品批量审批下架");
						insertDatamap.put("current_status", "4497153900060003");
						DbUp.upTable("sc_flow_bussiness_history").dataInsert(insertDatamap);
						
						PlusHelperNotice.onChangeProductInfo(product_code);
						ProductJmsSupport productJmsSupport = new  ProductJmsSupport();
						productJmsSupport.updateSolrData(product_code);
						//更新分类商品数量表
						XmasKv.upFactory(EKvSchema.IsUpdateCategoryProductCount).set("isUpdateCateProd","update");
						//更新品牌商品数量表
						XmasKv.upFactory(EKvSchema.IsUpdateBrandProductCount).set("isUpdateBrandProductCount","update");
					}
				}
				
			}else {
				//驳回下架，循环xiajiaUids 将pc_product_xiajia_shenpi表 对应product_code并且shenpi_status为0的改为2驳回下架
				String[] split = xiajiaUids.split(",");
				for(int  i = 0;i<split.length;i++) {
					String uid = split[i];
					MDataMap updateMap = new MDataMap();
					updateMap.put("uid", uid);
					updateMap.put("shenpi_status", "2");
					DbUp.upTable("pc_product_xiajia_shenpi").dataUpdate(updateMap,
							"shenpi_status",
							"uid");
				}
			}
		}
		
		return result;
	}
	
	public static class CheckUnshelveProductInput extends RootInput {
		private String xiajiaFlag;//0全部下架,1下架选中
		private String xiajiaUids;//当xiajiaFlag为1时有用，需下架商品编码用,隔开
		private boolean checkIdea;//审批意见 true为下架，false为驳回 
		public String getXiajiaFlag() {
			return xiajiaFlag;
		}
		public void setXiajiaFlag(String xiajiaFlag) {
			this.xiajiaFlag = xiajiaFlag;
		}
		public String getXiajiaUids() {
			return xiajiaUids;
		}
		public void setXiajiaUids(String xiajiaUids) {
			this.xiajiaUids = xiajiaUids;
		}
		public boolean isCheckIdea() {
			return checkIdea;
		}
		public void setCheckIdea(boolean checkIdea) {
			this.checkIdea = checkIdea;
		}
		
	}
	public static class CheckUnshelveProductResult extends RootResult {
		
	}
	

}

