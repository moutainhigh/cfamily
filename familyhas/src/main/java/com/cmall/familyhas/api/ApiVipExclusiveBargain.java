package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiVipExclusiveBarginInput;
import com.cmall.familyhas.api.result.ApiVipExclusiveBargainResult;
import com.cmall.familyhas.model.VipBargain;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiVipExclusiveBargain extends RootApiForToken<ApiVipExclusiveBargainResult, ApiVipExclusiveBarginInput> {
	
	private static final String VIP_TYPE = "449748130003";
	
	@SuppressWarnings("deprecation")
	@Override
	public ApiVipExclusiveBargainResult Process(ApiVipExclusiveBarginInput inputParam, MDataMap mRequestMap) {
		int page = inputParam.getPage() - 1;//当前页数
		int pageCount = inputParam.getPageCount();//每页记录个数
		int start = page * pageCount;
		String orderSeq = inputParam.getOrderSeq();//排序方式
		String maxWidth = StringUtils.isBlank(inputParam.getMaxWidth()) ? "0" : inputParam.getMaxWidth(); // 最大宽度
		String picType = StringUtils.isBlank(inputParam.getPicType()) ? "" : inputParam.getPicType(); // 图片格式
		
		int totalCount = 0;
		List<VipBargain> bargainList = new ArrayList<VipBargain>();
		ApiVipExclusiveBargainResult apiResult = new ApiVipExclusiveBargainResult();
		String shareSql = "select c.channel_name, c.share_title, c.is_share, c.share_desc, c.share_pic from fh_apphome_channel c where c.channel_type = '" + VIP_TYPE + "' "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' limit 0, 1";
		Map<String, Object> shareMap = DbUp.upTable("fh_apphome_channel").dataSqlOne(shareSql, new MDataMap());
		if(shareMap != null) {
			//添加微信分享信息
			apiResult.setChannelName(shareMap.get("channel_name") == null ? "" : shareMap.get("channel_name").toString());
			apiResult.setShareTitle(shareMap.get("share_title") == null ? "" : shareMap.get("share_title").toString());
			apiResult.setIsShare(shareMap.get("is_share") == null ? "" : shareMap.get("is_share").toString());
			apiResult.setShareDesc(shareMap.get("share_desc") == null ? "" : shareMap.get("share_desc").toString());
			apiResult.setSharePic(shareMap.get("share_pic") == null ? "" : shareMap.get("share_pic").toString());
			
			//特价列表数据
			String orderBySql = "";
			String dataHeadSql = "select t.product_code, t.product_name, min(t.selling_price) selling_price, min(t.favorable_price) favorable_price, max(t.sales_num) stock_count, t.product_photo, t.is_down from ("
					+ "select p.product_code, (select info.product_name from productcenter.pc_productinfo info where info.product_code = p.product_code) product_name,"
					+ " p.selling_price, p.favorable_price, p.sales_num,"
					+ " (select info.mainpic_url from productcenter.pc_productinfo info where info.product_code = p.product_code) product_photo,"
					+ " (select count(1) from productcenter.pc_productinfo info where info.product_code = p.product_code and info.product_status = '4497153900060002') is_down";
			String countHeadSql = "select count(1) from (select t.product_code from (select p.product_code";
			String commonSql = " from sc_event_info i, sc_event_item_product p where i.event_type_code = '4497472600010020' and i.begin_time <= sysdate() and i.end_time >= sysdate() "
					+ "and i.event_code = p.event_code and i.event_status = '4497472700020002' and p.flag_enable = '1'";
			if("".equals(orderSeq) || "0".equals(orderSeq)) {//默认排序
				//默认排序已取消
			}else if("1".equals(orderSeq) || "2".equals(orderSeq)) {//销量
				if("1".equals(orderSeq)) {//升序
					orderBySql = " order by (select count(1) from ordercenter.oc_orderdetail de where de.product_code = t.product_code), d.uid asc";
				}else if("2".equals(orderSeq)) {//降序
					orderBySql = " order by (select count(1) from ordercenter.oc_orderdetail de where de.product_code = t.product_code), d.uid desc";
				}
			}else if("3".equals(orderSeq) || "4".equals(orderSeq)) {//价格
				if("3".equals(orderSeq)) {//升序
					orderBySql = " order by min(t.favorable_price), d.uid asc";
				}else if("4".equals(orderSeq)) {//降序
					orderBySql = " order by min(t.favorable_price), d.uid desc";
				}
			}
			
			String dataSql = dataHeadSql + commonSql + ")t " + " group by t.product_code" + orderBySql +  " limit " + start + "," + pageCount;
			List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlList(dataSql, new MDataMap());
			
			String countSql = countHeadSql + commonSql + ")t group by t.product_code)a";
			totalCount = DbUp.upTable("sc_event_info").upTemplate().queryForInt(countSql, new HashMap<String, Object>());
			
			List<String> urlList = new ArrayList<String>();
			ProductService pService = new ProductService();
			for(Map<String, Object> map : list) {
				urlList.clear();
				VipBargain bargain = new VipBargain();
				bargain.setProductCode(map.get("product_code") == null ? "" : map.get("product_code").toString());
				bargain.setProductName(map.get("product_name") == null ? "" : map.get("product_name").toString());
				
				String sellPrice = map.get("selling_price") == null ? "" : map.get("selling_price").toString();
				bargain.setSellPrice(new BigDecimal(sellPrice).stripTrailingZeros().toPlainString());
				bargain.setFavorablePrice(map.get("favorable_price") == null ? "" : map.get("favorable_price").toString());
				
				int stockCount = map.get("stock_count") == null ? 0 : Integer.parseInt(map.get("stock_count").toString());
				boolean isLoot = stockCount > 0 ? false : true;
				bargain.setStockCount(stockCount);
				bargain.setLoot(isLoot);
				
				int proCount = map.get("is_down") == null ? 0 : Integer.parseInt(map.get("is_down").toString());
				boolean isDown = proCount > 0 ? false : true;
				bargain.setDown(isDown);
				
				//压缩图片
				String productImg = "";
				String photoUrl = MapUtils.getString(map, "product_photo", "");
				if(!"".equals(photoUrl)) {
					urlList.add(photoUrl);
					List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(Integer.parseInt(maxWidth), urlList, picType);
					if(picInfoList.size() > 0) {
						PicInfo info = picInfoList.get(0);
						productImg = info.getPicNewUrl();
					}
				}
				bargain.setProductImg(productImg);
				bargainList.add(bargain);
			}
		}
		apiResult.setItemList(bargainList);
		apiResult.setTotalPage(totalCount);
		return apiResult;
	}
}
