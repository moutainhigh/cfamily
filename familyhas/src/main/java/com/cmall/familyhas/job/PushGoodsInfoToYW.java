package com.cmall.familyhas.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.result.ApiConfirmReceiveForFamilyResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 商品信息推送银湾
 * @author fq
 *
 */
public class PushGoodsInfoToYW  extends RootJob {

	public void doExecute(JobExecutionContext context) {
		
		//提取更新过的信息
		saveUpdateGoodsInfo();
		
		String url = upRequestUrl();
		//查找需要发送的商品信息
		List<MDataMap> list = DbUp.upTable("pc_push_productinfo").query("zid,uid,product_code", "", "flag_push_yw=:flag_push_yw", new MDataMap("flag_push_yw","0"), 0, 1);
		MDataMap sendParam = new MDataMap();
		MDataMap valid = new MDataMap();
//		valid.put("user.username", "15901357003");
//		valid.put("user.password", "123456");
		valid.put("accessToken", "d08ede44cc8720893140c5d197862deb");
		valid.put("product.shop.id", "994");
		ApiConfirmReceiveForFamilyResult result = new ApiConfirmReceiveForFamilyResult();
		if(list.size() > 0) {
			MDataMap mDataMap = list.get(0);
			String product_code = mDataMap.get("product_code");
			MDataMap pcData = DbUp.upTable("pc_productinfo").one("product_code",product_code);
			String min_sell_price = pcData.get("min_sell_price");
			if(null != min_sell_price && !"".equals(min_sell_price) && !"0".equals(min_sell_price) && !"0.0".equals(min_sell_price) && !"0.00".equals(min_sell_price)) {
				sendParam.put("pproduct.vipPrice", min_sell_price);
				sendParam.put("product.price", pcData.get("market_price"));
				String goodsName = pcData.get("product_name");
				try {
					String product_name = URLEncoder.encode(goodsName,"utf-8");
					sendParam.put("product.name", product_name);
				} catch (UnsupportedEncodingException e1) {
					result.setResultCode(916401136);
					result.setResultMessage(bInfo(916401136, "["+product_code+"]","[商品名称]"));
				}
				sendParam.put("product.number", product_code);
				sendParam.put("product.unit", " ");
				sendParam.put("product.img", pcData.get("mainpic_url"));
				MDataMap detail = DbUp.upTable("pc_productdescription").one("product_code",product_code);
				String describe = detail.get("description_info");
				try {
					String content = URLEncoder.encode(describe,"utf-8");
					sendParam.put("product.content", content);
				} catch (UnsupportedEncodingException e) {
					result.setResultCode(916401136);
					result.setResultMessage(bInfo(916401136, "["+product_code+"]","[商品简介]"));
				}
				/*
				 * 获取商品类别名称
				 */
				MDataMap pro_type_relation = DbUp.upTable("uc_sellercategory_product_relation").one("product_code",product_code);
				MDataMap pro_type = DbUp.upTable("uc_sellercategory").one("category_code",pro_type_relation.get("category_code"));
				sendParam.put("productCategoryName", pro_type.get("category_name"));
				/*
				 * 获取商品轮播图   queryAll
				 */
				StringBuffer imgString = new StringBuffer(); 
				List<MDataMap> pro_pic = DbUp.upTable("pc_productpic").query("pic_url", "", "product_code=:product_code", new MDataMap("product_code",product_code), -1, -1);
				for (MDataMap mData_picMap : pro_pic) {
					String pic_url = mData_picMap.get("pic_url");
					imgString.append(pic_url);
					imgString.append(",");
				}
				String img = "";
				if(imgString.toString().length() > 0) {
					img = imgString.substring(0, imgString.length()-1);
				}
				sendParam.put("productUrls", img);
				sendParam.put("product.type", "product");
				sendParam.put("product.state", pcData.get("flag_sale"));
				try {
					valid.putAll(sendParam);
					String doGet = WebClientSupport.create().upPost(url, valid);
					JSONObject rtnJsonObject = JSON.parseObject(doGet);
					if("1".equals(rtnJsonObject.get("status"))) {
					//	System.out.println("发送成功!!");
						mDataMap.put("flag_push_yw", "1");//价格为0
						mDataMap.put("push_yw_time", DateUtil.getSysDateTimeString());
						DbUp.upTable("pc_push_productinfo").update(mDataMap);
					} else {
						if(rtnJsonObject.get("msg").equals("用户信息过期，请重新登录")) {
							String sendGet = WebClientSupport.create().doGet("http://api.yinwan.bangqu.com/user/login.json?user.username=15901357003&user.password=123456");
							JSONObject ywRtn = JSON.parseObject(sendGet);
							String status = ywRtn.getString("status");
							if(status.equals("1")) {
								JSONObject accessToken = ywRtn.getJSONObject("accessToken");
								String accessToken1 = accessToken.getString("accessToken");
								MDataMap valid1 = new MDataMap();
								valid1.put("accessToken", accessToken1);
								valid1.put("product.shop.id", "994");
								valid1.putAll(sendParam);
								String result1 = WebClientSupport.create().upPost(url, valid1);
								JSONObject resultJson1 = JSON.parseObject(result1);
								if("1".equals(resultJson1.get("status"))) {
									mDataMap.put("flag_push_yw", "1");//价格为0
									mDataMap.put("push_yw_time", DateUtil.getSysDateTimeString());
									DbUp.upTable("pc_push_productinfo").update(mDataMap);					
								}
								result.setResultCode(916401137);
								result.setResultMessage(bInfo(916401137, "["+rtnJsonObject.get("msg")+"]"));
							} else {
								result.setResultCode(916401137);
								result.setResultMessage(bInfo(916401137, "["+rtnJsonObject.get("msg")+"]"));
							}
						} else {
							result.setResultCode(916401138);
							result.setResultMessage(bInfo(916401138, "["+rtnJsonObject.get("msg")+"]"));
						}
					}
					
				} catch (Exception e) {
					result.setResultCode(916401138);
					result.setResultMessage(bInfo(916401138, "["+e.getMessage()+"]"));
				}
			} else {
				mDataMap.put("flag_push_yw", "2");//价格为0
				DbUp.upTable("pc_push_productinfo").update(mDataMap);
			}
			
		} 
	
	}
	
	/**
	 * 获取请求的url
	 * 
	 * @return
	 */
	private String upRequestUrl() {
		return bConfig("groupcenter.yw_url")
				+ "save.json";
	}
	
	/**
	 * 添加更新过的商品信息添加到推送列表
	 */
	private void saveUpdateGoodsInfo() {
		
		Map<String, Object> map= DbUp.upTable("pc_push_productinfo").dataSqlOne("SELECT max(update_time) max_time from pc_push_productinfo where flag_push_jy = 1  ", new MDataMap());
		Object max_time = map.get("max_time");
		if(null == max_time || "".equals(max_time)) {
			max_time = "2014-10-20 00:00:00";
		}
		List<MDataMap> saveAll = DbUp.upTable("pc_productinfo").query("product_code,update_time,product_status", "", "date_format(update_time,'%Y-%m-%d %H:%i:%s')>date_format(:update_time,'%Y-%m-%d %H:%i:%s') and seller_code = 'SI2003' ", new MDataMap("update_time",String.valueOf(max_time)), -1, -1);
		if(saveAll.size() > 0) {
			for (MDataMap mDataMap : saveAll) {
				String product_status = mDataMap.get("product_status");
				//判断编辑过的商品是否是下架状态
				if("4497153900060002".equals(product_status)) {//如果是上架状态，则不标识推送给微信
					DbUp.upTable("pc_push_productinfo").insert("product_code",mDataMap.get("product_code"),"update_time",mDataMap.get("update_time"));
				} else {
					DbUp.upTable("pc_push_productinfo").insert("product_code",mDataMap.get("product_code"),"update_time",mDataMap.get("update_time"),"flag_push_wx","0");
				}
			}
			
		}
		
	}
	
}
