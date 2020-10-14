package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForAfterSaleInfoInput;
import com.cmall.familyhas.api.result.ApiForAfterSaleInfoResult;
import com.cmall.familyhas.api.result.ApiForAfterSaleInfoResult.AfterSale;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 审核详情
 * 
 * 
 * @author jlin
 *
 */
public class ApiForAfterSaleInfo extends RootApiForToken<ApiForAfterSaleInfoResult, ApiForAfterSaleInfoInput> {
	
	@Override
	public ApiForAfterSaleInfoResult Process(ApiForAfterSaleInfoInput inputParam, MDataMap mRequestMap) {
		String afterCode = inputParam.getAfterCode();
		String buyer_code = getUserCode();
		ApiForAfterSaleInfoResult result = new ApiForAfterSaleInfoResult();
		if(afterCode.contains("LD")||afterCode.contains("P")||afterCode.contains("A")){//LD售後單
			result = this.ldAfterSaleInfo(afterCode,buyer_code);;
		}else{
			result = this.appAfterSaleInfo(afterCode, buyer_code);
		}
		return result;
	}

	/**
	 * LD审核详情查询
	 * @param afterCode
	 * @param buyer_code
	 * @return
	 */
	private ApiForAfterSaleInfoResult ldAfterSaleInfo(String afterCode, String buyer_code) {
		ApiForAfterSaleInfoResult result = new ApiForAfterSaleInfoResult();
		if(afterCode.contains("CGS")) {
			result.setResultMessage("LD品暂无换货审核明细");
			return result;
		}
		MDataMap asaleLd = DbUp.upTable("oc_after_sale_ld").one("after_sale_code_app",afterCode);
		if(asaleLd != null && !asaleLd.isEmpty()) {
			afterCode = asaleLd.get("after_sale_code_ld");
		}
//		String sql = "SELECT * FROM logcenter.lc_serial_after_sale WHERE asale_code = '"+afterCode+"'";
		List<MDataMap> list = DbUp.upTable("lc_serial_after_sale").queryAll("", "zid", "asale_code=:asale_code",new MDataMap("asale_code", afterCode));
		if(list != null && list.size() > 0) {
			for (MDataMap mDataMap : list) {
				String create_source = mDataMap.get("create_source");
				String serial_msg = mDataMap.get("serial_msg");
				String serial_title = mDataMap.get("serial_title");
				String create_time = mDataMap.get("create_time");
				String template_code = mDataMap.get("template_code");
				
				AfterSale afterSale = new AfterSale();
				afterSale.setBgColor("4497477800100004");
				afterSale.setIdentity(create_source);
				afterSale.setTime(create_time);
				afterSale.setTitle(serial_title);
				afterSale.setContent(serial_msg);
				
				
//				4497477800100001	浅灰
//				4497477800100002	黄色
//				4497477800100003	玫瑰红
//				4497477800100004	深灰
				if ("OST160312100018".equals(template_code)||("OST160312100002").equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				} else if ("OST160312100014".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				} else if ("OST160312100008".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				} else if ("OST160312100012".equals(template_code)) {
					afterSale.setBgColor("4497477800100003");
				} else if ("OST160312100016".equals(template_code)) {
					afterSale.setBgColor("4497477800100003");
				} else if ("OST160312100010".equals(template_code)) {
					afterSale.setBgColor("4497477800100003");
				} else if ("OST160312100015".equals(template_code)) {
					afterSale.setBgColor("4497477800100004");
				} else if ("OST160312100009".equals(template_code)) {
					afterSale.setBgColor("4497477800100004");
				} else if ("OST160312100021".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				} else if ("OST160312100022".equals(template_code)) {
					afterSale.setBgColor("4497477800100003");
				}else if ("OST160312100025".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				}else if ("OST160312100024".equals(template_code)) {
					afterSale.setBgColor("4497477800100004");
				}else if ("OST160312100026".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				}else if ("OST160312100027".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				}
				
				result.getAfterSaleList().add(afterSale);
			}
		}else {
			result.setResultCode(0);
			result.setResultMessage("暂无售后详情，请稍后查询或电话联系客服");
		}
		return result;
	}

	/**
	 * APP售後單查詢
	 * @param afterCode
	 * @param buyer_code
	 * @return
	 */
	private ApiForAfterSaleInfoResult appAfterSaleInfo(String afterCode,String buyer_code){


		ApiForAfterSaleInfoResult result = new ApiForAfterSaleInfoResult();

		MDataMap asMap = DbUp.upTable("oc_order_after_sale").one("asale_code", afterCode, "buyer_code", buyer_code);
		if (asMap == null || asMap.isEmpty()) {
			result.setResultCode(916422153);
			result.setResultMessage(bInfo(916422153));
			return result;
		}
		
		List<MDataMap> list = DbUp.upTable("lc_serial_after_sale").queryAll("", "zid", "asale_code=:asale_code",new MDataMap("asale_code", afterCode));
		
		if (list != null) {
			for (MDataMap mDataMap : list) {

				// String lac_code=mDataMap.get("lac_code");
				String create_source = mDataMap.get("create_source");
				String serial_msg = mDataMap.get("serial_msg");
				String serial_title = mDataMap.get("serial_title");
				String create_time = mDataMap.get("create_time");
				// String asale_code=mDataMap.get("asale_code");
				String template_code = mDataMap.get("template_code");
				
				AfterSale afterSale = new AfterSale();
				afterSale.setBgColor("4497477800100004");
				afterSale.setIdentity(create_source);
				afterSale.setTime(create_time);
				afterSale.setTitle(serial_title);
				afterSale.setContent(serial_msg);
				
				if ("OST160312100018".equals(template_code)) { // 待完善物流信息
					if("4497477800050010".equals(asMap.get("asale_status"))){
						afterSale.setStatusType("002");
					}
				}
				
				if (StringUtils.startsWithAny(template_code, "OST160312100007","OST160312100013")) { // 查询上传的图片
//					if ("OST160312100007".equals(template_code)) {
					MDataMap pics = DbUp.upTable("oc_order_remark").one("remark_type", "1", "asale_code", afterCode);
					afterSale.setPicTitle(bConfig("familyhas.after_saleInfo_pic_tip"));
					if (pics != null) {
						for (int i = 1; i <= 5; i++) {
							String pic = pics.get("remark_picurl" + i);
							if (StringUtils.isNotBlank(pic)) {
								afterSale.getPicUrl().add(pic);
							}
						}
					}
				}
				
				
//				4497477800100001	浅灰
//				4497477800100002	黄色
//				4497477800100003	玫瑰红
//				4497477800100004	深灰
				if ("OST160312100018".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				} else if ("OST160312100014".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				} else if ("OST160312100008".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				} else if ("OST160312100012".equals(template_code)) {
					afterSale.setBgColor("4497477800100003");
				} else if ("OST160312100016".equals(template_code)) {
					afterSale.setBgColor("4497477800100003");
				} else if ("OST160312100010".equals(template_code)) {
					afterSale.setBgColor("4497477800100003");
				} else if ("OST160312100015".equals(template_code)) {
					afterSale.setBgColor("4497477800100004");
				} else if ("OST160312100009".equals(template_code)) {
					afterSale.setBgColor("4497477800100004");
				} else if ("OST160312100021".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				} else if ("OST160312100022".equals(template_code)) {
					afterSale.setBgColor("4497477800100003");
				}else if ("OST160312100025".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				}else if ("OST160312100024".equals(template_code)) {
					afterSale.setBgColor("4497477800100004");
				}else if ("OST160312100026".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				}else if ("OST160312100027".equals(template_code)) {
					afterSale.setBgColor("4497477800100002");
				}
				
				result.getAfterSaleList().add(afterSale);
			}
		}

		return result;
	
	}
}
