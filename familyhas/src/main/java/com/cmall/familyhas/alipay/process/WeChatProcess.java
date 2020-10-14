package com.cmall.familyhas.alipay.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.cmall.groupcenter.service.AlipayOrderShoppingService;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.membercenter.support.MemberLoginSupport;
import com.cmall.ordercenter.alipay.util.JsonUtil;
import com.cmall.ordercenter.model.PayResult;
import com.cmall.ordercenter.service.ApiWechatProcessService;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.ordercenter.util.HttpRequestUrlUtil;
import com.cmall.systemcenter.common.AppConst;
import com.cmall.systemcenter.common.CouponConst;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.cmall.systemcenter.util.AnalysisXmlUtil;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebSessionHelper;
/**
 * 微信支付最新版本回调
 * @author wz
 *
 */
public class WeChatProcess extends BaseClass {
	
	public void resultForm(String mark) {
		PayResult payResult = new PayResult();
		AlipayOrderShoppingService orderShoppingService = new AlipayOrderShoppingService();
		HttpServletRequest request = WebSessionHelper.create().upHttpRequest();
		
		
		try {
			InputStreamReader isr = new InputStreamReader(request.getInputStream());   
			BufferedReader br = new BufferedReader(isr); 
			StringBuffer sb = new StringBuffer();
			String s = "" ; 
			while((s=br.readLine())!=null){ 
				sb.append(s) ; 
			} 
			//微信返回来的信息
			String str =sb.toString(); 
			//解析微信返回来的信息
			Map wechatMoveParamsMap = AnalysisXmlUtil.readStringXmlOut(str);
			
			ApiWechatProcessService apiWechatProcessService = new ApiWechatProcessService();
			payResult =  apiWechatProcessService.responseWechatMoveVersionNewService(wechatMoveParamsMap,mark);
			
			if (payResult.upFlagTrue()) {
				
//				Map<String,Object> mapOrder = DbUp.upTable("oc_orderinfo").dataSqlOne("select * from oc_orderinfo where order_code=:order_code or big_order_code=:order_code", 
//						new MDataMap("order_code",payResult.getOrderCode()));
//				
//				
//				if(mapOrder!=null && !"".equals(mapOrder)){
//					/**
//					 * 只有ld的商品会同步到ld，其余商品不同步(只有small_seller_code为SI2003的时候说明是ld的商品)
//					 */
//					if("SI2003".equals(String.valueOf(mapOrder.get("small_seller_code")))){
//						orderShoppingService.deletefamilySkuToShopCart(payResult
//								.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品 并且 同步支付数据
//					}else{
//						orderShoppingService.deletefamilySkuToShopCartNotSynchronization(payResult
//								.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品     不同步支付数据
//					}
//					
//				}
				
				//update by jlin 2015-11-07 12:59:02
				List<MDataMap> orderList=DbUp.upTable("oc_orderinfo").queryAll("", "", "order_code=:order_code or big_order_code=:order_code", new MDataMap("order_code", payResult.getOrderCode()));
				boolean ld_flag=false;
				boolean th_flag=false;
				for (MDataMap mDataMap : orderList) {
					if(ld_flag&&th_flag){
						break;
					}
					
					String small_seller_code=mDataMap.get("small_seller_code");
					/**
					 * 只有ld的商品会同步到ld，其余商品不同步(只有small_seller_code为SI2003的时候说明是ld的商品)
					 */
					if (MemberConst.MANAGE_CODE_HOMEHAS.equals(small_seller_code)) {
						if(ld_flag){
							continue;
						}
						orderShoppingService.deletefamilySkuToShopCart(payResult.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品
						ld_flag=true;
					} else {
						if(th_flag){
							continue;
						}
						orderShoppingService.deletefamilySkuToShopCartNotSynchronization(payResult.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品
						th_flag=true;
					}
				}
				//end
				
				

				/**
				 * 同步kJT支付完成后的订单
				 */
				OrderService os = new OrderService();
				List<Map<String, Object>> listMap = os.ifKJTOrder(payResult.getOrderCode(), "SF03KJT");
				for(Map<String, Object> map : listMap){
//					JobExecHelper.createExecInfo("449746990003", String.valueOf(map.get("order_code")), null);
					JobExecHelper.createExecInfoForWebcore(Constants.ZA_EXEC_TYPE_SYNC_KJT, String.valueOf(map.get("order_code")), "" , "WeChatProcess line 122");
				}
				
				
				/**
				 * 
				 */
				if (VersionHelper.checkServerVersion("3.5.62.55")) {
					MDataMap bigOrderMap = DbUp.upTable("oc_orderinfo_upper").one(
							"big_order_code", wechatMoveParamsMap.get("out_trade_no")+"");

					if (bigOrderMap != null && !"".equals(bigOrderMap)
							&& bigOrderMap.size() > 0) {

						if (AppConst.MANAGE_CODE_HOMEHAS.equals(bigOrderMap.get("seller_code")) 
								|| AppConst.MANAGE_CODE_CDOG.equals(bigOrderMap.get("seller_code"))) {
							// 通过buyer_code获取手机号
							MemberLoginSupport mls = new MemberLoginSupport();
							String loginName = mls.getMoblie(bigOrderMap.get("buyer_code"));

							if (loginName != null && !"".equals(loginName)) {
								// 货到付款下单送优惠券
								JmsNoticeSupport.INSTANCE.sendQueue(
										JmsNameEnumer.OnDistributeCoupon,
										CouponConst.pay_coupon, new MDataMap(
												"mobile", loginName, "manage_code", bigOrderMap.get("seller_code"), 
												"big_order_code", bigOrderMap.get("big_order_code"),
												"member_code", bigOrderMap.get("buyer_code")));
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 微信WAP(此方法废弃  无需关注)
	 */
	@Deprecated
	public void wechatWAPRedirectURI(){
		HttpServletRequest request = WebSessionHelper.create().upHttpRequest();
		String code = request.getParameter("code");
		
		
		if(code!=null && !"".equals(code)){
			//System.out.println("**************************"+code);
			Map map =new HashMap();
			map.put("appid", bConfig("ordercenter.APP_ID_HUJIAYOU_WAP"));
			map.put("secret", bConfig("ordercenter.APP_SECRET_HUJIAYOU_WAP"));
			map.put("code", code);
			map.put("grant_type", "authorization_code");
			
			//通过code换取网页授权access_token、openid
			HttpRequestUrlUtil httpRequestUrl = new HttpRequestUrlUtil();
			String reponseParams = httpRequestUrl.requestURL("https://api.weixin.qq.com/sns/oauth2/access_token", map);
			
			//System.out.println("11111111111111111111111111111111111");
			JSONObject jsonObject = JsonUtil.getJsonValues(reponseParams);
			if(jsonObject!=null && !"".equals(jsonObject)){
				//System.out.println("2222222222222222222222222222222222");
				String openid = jsonObject.getString("openid");
				//System.out.println("======================================="+openid);
			}
		}else{
			//System.out.println("======================================else");
		}
		
	}
	
}
