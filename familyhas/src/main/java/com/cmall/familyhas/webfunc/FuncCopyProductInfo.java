package com.cmall.familyhas.webfunc;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.cmall.productcenter.common.SkuCommon;
import com.cmall.productcenter.model.PcProductflow;
import com.cmall.productcenter.model.PcProductinfo;
import com.cmall.productcenter.model.ProductSkuInfo;
import com.cmall.productcenter.model.ScStoreSkunum;
import com.cmall.productcenter.service.ProductService;
import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 复制商品信息、SKU信息
 * 
 * @author ligj
 * 
 */
public class FuncCopyProductInfo extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		String copyProductCode = mDataMap.get("zw_f_product_code"); // 需要同步上商品编号
		String smallSellerInput = mDataMap.get("zw_f_small_seller");		//传入的商家编号
		// 商品编号为空，提示信息：商品编号错误，查不到商品信息！
		if (StringUtils.isBlank(copyProductCode)) {
			mResult.inErrorMessage(916423010);
			return mResult;
		}
		if (mResult.upFlagTrue()) {
			MDataMap productMap = DbUp.upTable("pc_productinfo").one("product_code",copyProductCode);
			// 查不到商品信息时返回错误，提示信息：商品编号错误，查不到商品信息！
			if (productMap == null || productMap.isEmpty()) {
				mResult.inErrorMessage(916423010);
				return mResult;
			}
			//如果为商户商品菜单，则传入的是SF031，所以用startsWith
			if (!productMap.get("small_seller_code").startsWith(smallSellerInput)) {
				mResult.inErrorMessage(916423011);
				return mResult;
			}
			
			MUserInfo userInfo = null;
			String userCode = "";
			if (UserFactory.INSTANCE != null) {
				try {
					userInfo = UserFactory.INSTANCE.create();
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (userInfo != null) {
					userCode = userInfo.getUserCode();
				}
			}
			// ----------
			// 开始进行复制---->
			// ----------
			ProductService ps = new ProductService();
			PcProductinfo product = ps.getProduct(copyProductCode);
			StringBuffer error = new StringBuffer();
			
			if (product.getProductCode().length() > 1) {
				//进行一些code的重新设置
//				String productCode = WebHelper.upCode(ProductService.ProductHead);
				// 根据商户编码查询商户类型 2016-11-14 zhy
				MDataMap seller = DbUp.upTable("uc_seller_info_extend").oneWhere("uc_seller_type", "", "", "small_seller_code",
						productMap.get("small_seller_code"));
				// 根据商户类型读取配置文件获取商品编码前缀 2016-11-14 zhy
				String code_start = bConfig("productcenter.product" + seller.get("uc_seller_type"));
				String productCode = WebHelper.upCode(code_start);
				
				product.setProductCode(productCode);
				product.setProductStatus("4497153900060003");
				product.setFlagSale(0);
				//商品虚类
				if(null != product.getUsprList() && product.getUsprList().size() > 0){
					for(int i=0;i<product.getUsprList().size();i++){
						product.getUsprList().get(i).setProductCode(productCode);
						product.getUsprList().get(i).setSellerCode(product.getSellerCode());	//这个字段应该与虚类所属的sell_code一致，故此不能用small_seller_code
					}
				}
				//商品实类
				if(null != product.getPcProductcategoryRel()){
						product.getPcProductcategoryRel().setProductCode(productCode);
						product.getPcProductcategoryRel().setFlagMain(1);
				}
				//商品扩展信息
				if(null != product.getPcProductinfoExt()){
					product.getPcProductinfoExt().setProductCode(productCode);
				}
				if (product.getProductSkuInfoList() != null) {
					for (int i = 0; i < product.getProductSkuInfoList().size(); i++) {
						ProductSkuInfo sku = product.getProductSkuInfoList().get(i);
						sku.setProductCode(productCode);
						sku.setSkuCode(WebHelper.upCode(ProductService.SKUHead));
						sku.setSellerCode(product.getSellerCode());
						sku.setFlagEnable("1");
						sku.setScStoreSkunumList(new ArrayList<ScStoreSkunum>());
					}
				}
				PcProductflow pcProdcutflow = new PcProductflow();
				pcProdcutflow.setProductCode(product.getProductCode());
				pcProdcutflow.setFlowCode(WebHelper.upCode(ProductService.ProductFlowHead));
				pcProdcutflow.setFlowStatus(SkuCommon.FlowStatusInit);
				pcProdcutflow.setUpdator(userCode);
				product.setPcProdcutflow(pcProdcutflow);
				
				ps.AddProductTx(product, error,userCode);
				
				PlusHelperNotice.onChangeProductInfo(product.getProductCode());
				// 触发消息队列
				ProductJmsSupport pjs = new ProductJmsSupport();
				pjs.onChangeForProductChangeAll(product.getProductCode());
			}
			// 添加或更新发生错误
			if (StringUtils.isNotBlank(error.toString())) {
				mResult.inErrorMessage(951001004, error.toString());
			}
		}
		return mResult;
	}

}
