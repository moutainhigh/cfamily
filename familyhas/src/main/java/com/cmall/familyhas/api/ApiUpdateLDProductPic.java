package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.UpdateLDProductPicInput;
import com.cmall.familyhas.api.model.ProductPicModel;
import com.cmall.familyhas.api.result.UpdateLDProductPicResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiUpdateLDProductPic extends RootApi<UpdateLDProductPicResult, UpdateLDProductPicInput> {

	
	@Override
	public UpdateLDProductPicResult Process(UpdateLDProductPicInput input, MDataMap mRequestMap) {
		UpdateLDProductPicResult result = new UpdateLDProductPicResult();
		String productCode = input.getProductCode();
		String timestamp1 = input.getTimestamp1();
		String timestamp2 = input.getTimestamp2();
		List<ProductPicModel> ppmodels = new ArrayList<>();
		if(!"".equals(productCode)) {
			MDataMap proMap = DbUp.upTable("pc_productinfo").one("product_code",productCode);
			if(null==proMap) {
				result.setResultCode(0);
				result.setResultMessage("输入的商品编码有误");
			}else {
				String product_code = proMap.get("product_code");
				String mainpic_url = proMap.get("mainpic_url");
				if("".equals(mainpic_url)) {
					result.setResultCode(0);
					result.setResultMessage("此商品还未上传图片信息，请稍后再试");
				}else {
					ProductPicModel ppm = new ProductPicModel();
					ppm.setProductCode(product_code);
					ppm.setProductMainPic(mainpic_url);
					String sql = "SELECT a.*,b.description_pic FROM (" + 
							"	SELECT product_code,pic_url FROM pc_productpic pp WHERE pp.product_code = '"+product_code+"'" + 
							") a LEFT JOIN (" + 
							"	SELECT product_code,description_pic FROM pc_productdescription pd WHERE pd.product_code = '"+product_code+"'" + 
							") b ON a.product_code  = b.product_code ";
					List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_productpic").dataSqlList(sql, null);
					if(null!=dataSqlList&&dataSqlList.size()>0) {
						StringBuffer sb = new StringBuffer();
						for(Map<String, Object> map :dataSqlList) {
							String pic_url = map.get("pic_url").toString();
							sb.append(pic_url);sb.append("|");
							if("".equals(ppm.getProductDetailPics())) {
								ppm.setProductDetailPics(map.get("description_pic").toString());
							}
						}
						ppm.setProductCarouselPics(sb.length()>0?sb.deleteCharAt(sb.length() - 1).toString():"");
						ppmodels.add(ppm);
					}
				}
			}
		}else if(!"".equals(timestamp1)&&!"".equals(timestamp2)) {
			String startTime = timestamp1.compareTo(timestamp2)  > 0 ?timestamp2 :timestamp1;
			String endTime = timestamp1.compareTo(timestamp2)  > 0 ?timestamp1 :timestamp2;
			String sql = "SELECT a.*,pp.mainpic_url,pd.description_pic,ppc.pic_url FROM (" + 
					"SELECT DISTINCT product_code FROM logcenter.lc_productpic_update WHERE updatepic_time BETWEEN '"+startTime+"' AND '"+endTime+"' ) a" + 
					" JOIN productcenter.pc_productinfo pp ON a.product_code = pp.product_code" + 
					" JOIN productcenter.pc_productdescription pd ON a.product_code = pd.product_code" + 
					" JOIN productcenter.pc_productpic  ppc ON a.product_code  = ppc.product_code";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_productpic").dataSqlList(sql, null);
			Map<String,ProductPicModel> m = new HashMap<>();
			if(null!=dataSqlList&&dataSqlList.size()>0) {
				for(Map<String, Object> map : dataSqlList) {
					String product_code = map.get("product_code").toString();
					if(m.containsKey(product_code)) {
						ProductPicModel model = m.get(product_code);
						model.setProductCarouselPics(model.getProductCarouselPics()+"|"+map.get("pic_url").toString());
					}else {
						ProductPicModel model = new ProductPicModel();
						model.setProductCode(product_code);
						model.setProductMainPic(map.get("mainpic_url").toString());
						model.setProductDetailPics(map.get("description_pic").toString());
						model.setProductCarouselPics(map.get("pic_url").toString());
						m.put(product_code, model);
					}
				}
				//遍历map 存入结果集合
				if(m.size()>0) {
					for(ProductPicModel model : m.values()) {
						ppmodels.add(model);
					}
				}
			}
		}else {
			result.setResultCode(0);
			result.setResultMessage("入参为商品编号或两个时间戳，入参错误");
		}
		result.setPpmodels(ppmodels);
		return result;
	}
	
}
