package com.cmall.familyhas.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;

import com.cmall.dborm.txmodel.PcProductflow;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.common.SkuCommon;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 定时处理无库存的sku商品(仅限商户商品,不包含三方和LD品)
 * @remark 
 * @author lgx
 */
public class JobForDealNoSkuProd extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		// 查询上架商品，如果商品sku无库存，变更sku不可售状态，如果此商品下没有可售sku，商品下架
		bLogInfo(0,"***JobForDealNoSkuProd***开始****:"+DateUtil.getSysDateTimeString());
		// 查询上架且可售状态无库存的sku
		String sql = "SELECT sk.* FROM productcenter.pc_skuinfo sk " + 
				"LEFT JOIN (" + 
				"	SELECT p.* FROM productcenter.pc_productinfo p " + 
				"	WHERE p.seller_code = 'SI2003' " + 
				"	AND p.product_status = '4497153900060002' " + 
				"	AND p.small_seller_code NOT IN('SI2003','SF03WYKLPT','SF031JDSC') " + 
				") pp ON sk.product_code = pp.product_code " + 
				"LEFT JOIN (" + 
				"	SELECT s.sku_code, SUM(s.stock_num) stockNum FROM systemcenter.sc_store_skunum s " + 
				"	GROUP BY s.sku_code HAVING stockNum = 0 " + 
				") ss ON ss.sku_code = sk.sku_code " + 
				"WHERE  pp.seller_code = 'SI2003' " + 
				"	AND pp.product_status = '4497153900060002' " + 
				"	AND pp.small_seller_code NOT IN('SI2003','SF03WYKLPT','SF031JDSC') " + 
				"	AND ss.stockNum = 0 " + 
				"	AND sk.sale_yn = 'Y' AND sk.flag_enable = '1' ";
				//+ "LIMIT 3000";
		List<Map<String, Object>> skuList = DbUp.upTable("pc_skuinfo").dataSqlList(sql, new MDataMap());
		String sale_yn = "N";
		if(null != skuList && skuList.size() > 0) {
			for (Map<String, Object> skuInfo : skuList) {
				// 循环无库存sku,将sku置为不可售状态
				String uid = (String) skuInfo.get("uid");
				MDataMap skuDataMap = new MDataMap();
				skuDataMap.put("uid", uid);
				skuDataMap.put("sale_yn", sale_yn);
				DbUp.upTable("pc_skuinfo").dataUpdate(skuDataMap , "sale_yn", "uid");
				
				MDataMap skuObj = DbUp.upTable("pc_skuinfo").oneWhere("product_code,sku_code", "", "", "uid", uid);
				String productCode = skuObj.get("product_code");

				if (StringUtils.isNotEmpty(productCode)) {
					if ("N".equals(sale_yn)) {
						List<Map<String, Object>> countList = DbUp.upTable("pc_skuinfo").upTemplate().queryForList("SELECT count(1) num FROM pc_skuinfo WHERE product_code = '"+productCode+"' AND sale_yn = 'Y'", new HashMap<String, String>());
						int count = 0;
						if(null != countList && countList.size() > 0) {
							count = MapUtils.getIntValue(countList.get(0), "num");
						}
						if (count < 1) {
							// 下架商品
							MDataMap proMap = DbUp.upTable("pc_productinfo").one("product_code", productCode);
							FlowBussinessService fs = new FlowBussinessService();
							String flowBussinessUid = proMap.get("uid");
							String fromStatus = proMap.get("product_status");
							String toStatus = "4497153900060003";
							String flowType = "449715390006";

							String userCode = "system";
							String remark = "sku设置不可卖商品强制下架";
							fs.ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, userCode, remark, new MDataMap());
						}
					}
					JsonHelper<PcProductinfo> pHelper = new JsonHelper<PcProductinfo>();
					PcProductinfo pc = new ProductService().getProduct(productCode + "_1");

					if (null != pc && StringUtils.isNotBlank(pc.getProductCode())) {
						if (pc.getProductSkuInfoList() != null) {
							for (int i = 0; i < pc.getProductSkuInfoList().size(); i++) {
								if (skuObj.get("sku_code").equals(pc.getProductSkuInfoList().get(i).getSkuCode())) {
									pc.getProductSkuInfoList().get(i).setSaleYn(sale_yn);
								}
							}
						}

						com.cmall.dborm.txmapper.PcProductflowMapper ppfm = BeansHelper
								.upBean("bean_com_cmall_dborm_txmapper_PcProductflowMapper");
						PcProductflow ppf = new PcProductflow();
						ppf.setUid(UUID.randomUUID().toString().replace("-", ""));
						ppf.setProductCode(productCode);
						ppf.setFlowCode(WebHelper.upCode(ProductService.ProductFlowHead));
						ppf.setFlowStatus(SkuCommon.FlowStatusXG);
						ppf.setCreateTime(DateUtil.getSysDateTimeString());
						ppf.setCreator("JobForDealNoSkuProd");
						ppf.setUpdateTime(DateUtil.getSysDateTimeString());
						ppf.setUpdator("JobForDealNoSkuProd");
						ppf.setProductJson(pHelper.ObjToString(pc));
						ppfm.insertSelective(ppf);
					}
					PlusHelperNotice.onChangeProductInfo(productCode);
					// 触发消息队列
					ProductJmsSupport pjs = new ProductJmsSupport();
					pjs.onChangeForProductChangeAll(productCode);
				}
				
			}
		}
		bLogInfo(0,"****JobForDealNoSkuProd*****处理完成******:"+DateUtil.getSysDateTimeString());
	}
	
	
	
}
