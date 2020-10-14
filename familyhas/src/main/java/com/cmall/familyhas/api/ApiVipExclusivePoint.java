package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiVipExclusivePointInut;
import com.cmall.familyhas.api.result.ApiVipExclusivePointResult;
import com.cmall.familyhas.model.VipAreaPoint;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiVipExclusivePoint extends RootApiForToken<ApiVipExclusivePointResult, ApiVipExclusivePointInut> {
	
	private static final String POINT_TYPE = "449748130002";//积分
	private static final String PRICE_TYPE = "449748130001";//加价
	
	@SuppressWarnings("deprecation")
	@Override
	public ApiVipExclusivePointResult Process(ApiVipExclusivePointInut inputParam, MDataMap mRequestMap) {
		String custPoint = inputParam.getCustPoint();//客户积分
		int page = inputParam.getPage() - 1;//当前页数
		int pageCount = inputParam.getPageCount();//每页记录个数
		int start = page * pageCount;
		String channelType = inputParam.getChannelType();//类型
		String orderSeq = inputParam.getOrderSeq();//排序
		String maxWidth = StringUtils.isBlank(inputParam.getMaxWidth()) ? "0" : inputParam.getMaxWidth(); // 最大宽度
		String picType = StringUtils.isBlank(inputParam.getPicType()) ? "" : inputParam.getPicType(); // 图片格式
		
		ApiVipExclusivePointResult apiResult = new ApiVipExclusivePointResult();
		if(!"".equals(channelType)) {
			String VIP_TYPE = "";
			if(POINT_TYPE.equals(channelType)) {
				VIP_TYPE = POINT_TYPE;
			}else if(PRICE_TYPE.equals(channelType)) {
				VIP_TYPE = PRICE_TYPE;
			}
			String dataHeadSql = "select d.uid, d.title, 200 * d.jf_cost jf_cost, d.extra_charges, d.allow_count, d.product_code, "
					+ "(select info.mainpic_url from productcenter.pc_productinfo info where info.product_code = d.product_code) product_photo, "
					+ "(select count(1) from productcenter.pc_productinfo i where i.product_code = d.product_code and i.product_status = '4497153900060002') is_down";
			String countHeadSql = "select count(1)";
			String commonSql = " from fh_apphome_channel c, fh_apphome_channel_details d where c.channel_type = '" + VIP_TYPE + "' and c.uid = d.channel_uid "
					+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' and d.start_time <= sysdate() and d.end_time >= sysdate()";
			if("0".equals(orderSeq)) {//默认排序
				commonSql += " order by abs((d.jf_cost * 200) - '" + custPoint + "'), d.uid";
			}else if("1".equals(orderSeq) || "2".equals(orderSeq)) {//销量
				if("1".equals(orderSeq)) {//销量升序
					commonSql += " order by (select count(1) from ordercenter.oc_orderdetail de where de.product_code = d.product_code), d.uid asc";
				}else if("2".equals(orderSeq)) {//销量降序
					commonSql += " order by (select count(1) from ordercenter.oc_orderdetail de where de.product_code = d.product_code) desc, d.uid desc";
				}
			}else if("3".equals(orderSeq) || "4".equals(orderSeq)) {//价格
				if("3".equals(orderSeq)) {//价格升序
					if(POINT_TYPE.equals(channelType)) {//积分兑换
						commonSql += " order by d.jf_cost, d.uid asc";
					}else if(PRICE_TYPE.equals(channelType)) {//加价兑换
						commonSql += " order by d.extra_charges, d.uid asc";
					}
				}else if("4".equals(orderSeq)) {//价格降序
					if(POINT_TYPE.equals(channelType)) {//积分兑换
						commonSql += " order by d.jf_cost desc, d.uid desc";
					}else if(PRICE_TYPE.equals(channelType)) {//加价兑换
						commonSql += " order by d.extra_charges desc, d.uid desc";
					}
				}
			}
			
			String dataSql = dataHeadSql + commonSql + " limit " + start + "," + pageCount;
			List<Map<String, Object>> list = DbUp.upTable("fh_apphome_channel").dataSqlList(dataSql, new MDataMap());
			
			String countSql = countHeadSql + commonSql;
			int totalCount = DbUp.upTable("fh_apphome_channel").upTemplate().queryForInt(countSql, new HashMap<String, Object>());
			
			List<String> urlList = new ArrayList<String>();
			ProductService pService = new ProductService();
			List<VipAreaPoint> pointList = new ArrayList<VipAreaPoint>();
			for(Map<String, Object> map : list) {
				urlList.clear();
				VipAreaPoint point = new VipAreaPoint();
				point.setInfoId(map.get("uid") == null ? "" : map.get("uid").toString());
				point.setProductCode(map.get("product_code") == null ? "" : map.get("product_code").toString());
				point.setTitle(map.get("title") == null ? "" : map.get("title").toString());
				point.setJfCost(map.get("jf_cost") == null ? "" : map.get("jf_cost").toString());
				
				String charge = map.get("extra_charges") == null ? "" : map.get("extra_charges").toString();
				if(!"".equals(charge)) {
					point.setCharge(new BigDecimal(charge).stripTrailingZeros().toPlainString());
				}
				
				int allowCount = map.get("allow_count") == null ? 0 : Integer.parseInt(map.get("allow_count").toString());
				boolean isLoot = allowCount > 0 ? false : true;
				point.setAllowCount(allowCount);
				point.setLoot(isLoot);
				
				int proCount = map.get("is_down") == null ? 0 : Integer.parseInt(map.get("is_down").toString());
				boolean isDown = proCount > 0 ? false : true;
				point.setDown(isDown);
				
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
				point.setProductImg(productImg);
				pointList.add(point);
			}
			apiResult.setItemList(pointList);
			apiResult.setTotalPage(totalCount);
			
			//添加微信分享信息
			String shareSql = "select c.channel_name, c.share_title, c.is_share, c.share_desc, c.share_pic from fh_apphome_channel c where c.channel_type = '" + VIP_TYPE + "' "
					+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' limit 0, 1";
			Map<String, Object> shareMap = DbUp.upTable("fh_apphome_channel").dataSqlOne(shareSql, new MDataMap());
			if(shareMap != null) {
				apiResult.setChannelName(shareMap.get("channel_name") == null ? "" : shareMap.get("channel_name").toString());
				apiResult.setShareTitle(shareMap.get("share_title") == null ? "" : shareMap.get("share_title").toString());
				apiResult.setIsShare(shareMap.get("is_share") == null ? "" : shareMap.get("is_share").toString());
				apiResult.setShareDesc(shareMap.get("share_desc") == null ? "" : shareMap.get("share_desc").toString());
				apiResult.setSharePic(shareMap.get("share_pic") == null ? "" : shareMap.get("share_pic").toString());
			}
		}
		return apiResult;
	}
}
