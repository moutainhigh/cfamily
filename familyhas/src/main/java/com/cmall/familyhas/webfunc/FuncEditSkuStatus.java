package com.cmall.familyhas.webfunc;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.dborm.txmodel.PcProductflow;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.productcenter.common.SkuCommon;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.dcb.PushSkuStatusService;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改sku是否可卖
 * @author ligj
 *
 */
public class FuncEditSkuStatus extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		String sale_yn = mDataMap.get("zw_f_sale_yn");
		String uid = mDataMap.get("zw_f_uid");
		
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		MDataMap skuObj = DbUp.upTable("pc_skuinfo").oneWhere("product_code,sku_code", "", "", "uid",uid);
		String productCode = skuObj.get("product_code");
		
		MUserInfo userInfo = UserFactory.INSTANCE.create();
		if (null == userInfo) {
			userInfo = new MUserInfo();
		}
		if (StringUtils.isNotEmpty(productCode)) {
			if ("N".equals(sale_yn)) {
				if(DbUp.upTable("pc_skuinfo").count("product_code",productCode,"sale_yn","Y")<1){
					//下架商品
//					DbUp.upTable("pc_productinfo").dataUpdate(new MDataMap("product_code",skuObj.get("product_code"),"product_status","4497153900060004"), "product_status", "product_code");
					MDataMap proMap = DbUp.upTable("pc_productinfo").one("product_code",productCode);
					FlowBussinessService fs = new FlowBussinessService();
					String flowBussinessUid=proMap.get("uid");
					String fromStatus= proMap.get("product_status");
					String toStatus="4497153900060003";
					String flowType = "449715390006";
					
					String userCode=userInfo.getUserCode();
					String remark="sku设置不可卖商品强制下架";
					fs.ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, userCode, remark, new MDataMap());
				}
			}
			JsonHelper<PcProductinfo> pHelper=new JsonHelper<PcProductinfo>();
			PcProductinfo pc = new ProductService().getProduct(productCode+"_1");
			
			if (null != pc && StringUtils.isNotBlank(pc.getProductCode())) {
				if (pc.getProductSkuInfoList() != null) {
					for (int i = 0; i < pc.getProductSkuInfoList().size(); i++) {
						if (skuObj.get("sku_code").equals(pc.getProductSkuInfoList().get(i).getSkuCode())) {
							pc.getProductSkuInfoList().get(i).setSaleYn(sale_yn);
						}
					}
				}
				
				com.cmall.dborm.txmapper.PcProductflowMapper ppfm = 
						BeansHelper.upBean("bean_com_cmall_dborm_txmapper_PcProductflowMapper");
				PcProductflow ppf = new PcProductflow();
				ppf.setUid(UUID.randomUUID().toString().replace("-", ""));
				ppf.setProductCode(productCode);
				ppf.setFlowCode(WebHelper.upCode(ProductService.ProductFlowHead));
				ppf.setFlowStatus(SkuCommon.FlowStatusXG);
				ppf.setCreateTime(DateUtil.getSysDateTimeString());
				ppf.setCreator(userInfo.getLoginName());
				ppf.setUpdateTime(DateUtil.getSysDateTimeString());
				ppf.setUpdator(userInfo.getLoginName());
				ppf.setProductJson(pHelper.ObjToString(pc));
				ppfm.insertSelective(ppf);
			}
			PlusHelperNotice.onChangeProductInfo(productCode);
			//触发消息队列
			ProductJmsSupport pjs = new ProductJmsSupport();
			pjs.onChangeForProductChangeAll(productCode);
		}
		//自营商品处理SKU不可卖时，下架多彩宝商品
		if("N".equals(sale_yn)){
			this.pushSkuStatus(uid);
		}
		return mResult;
	}
	
	private void pushSkuStatus(String uid){
		MDataMap skuObj = DbUp.upTable("pc_skuinfo").oneWhere("product_code,sku_code", "", "", "uid",uid);
		if(skuObj != null){
			String sku_code = skuObj.get("sku_code").toString();
			String sql = "select * from productcenter.pc_bf_skuinfo where sku_code = "+"\""+sku_code+"\"";
			Map<String,Object> map = DbUp.upTable("pc_bf_skuinfo").dataSqlOne(sql,null);
			PushSkuStatusService service = new PushSkuStatusService();
			if(map != null){
				service.pushSkuStatus(sku_code, "N", 1, "自营商品处理SKU不可卖时，多彩宝对应商品下架");
			}
		}
	}
}
