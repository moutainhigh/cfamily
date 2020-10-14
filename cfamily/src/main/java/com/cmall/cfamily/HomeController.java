package com.cmall.cfamily;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.alipay.process.AlipayProcess;
import com.cmall.familyhas.orderpay.OrderPayProcess;
import com.cmall.familyhas.service.KJTOrderOutInfoService;
import com.cmall.familyhas.service.OutBindService;
import com.cmall.familyhas.service.ShareImageHandler;
import com.cmall.familyhas.service.WXMusicAlbumService;
import com.cmall.familyhas.upload.Uploader;
import com.cmall.familyhas.upload.VideoInfo;
import com.cmall.familyhas.util.VideoUtils;
import com.cmall.groupcenter.duohuozhu.support.DuohzAfterSaleSupport;
import com.cmall.groupcenter.homehas.RsyncKaoLaSupport;
import com.cmall.groupcenter.hserver.HAcceptServer;
import com.cmall.groupcenter.hserver.model.HServerResponse;
import com.cmall.groupcenter.job.JobSpecificUserCoupon;
import com.cmall.groupcenter.service.RsyncKLtoHJYSkuInfoService;
import com.cmall.ordercenter.alipay.util.JsonUtil;
import com.cmall.ordercenter.service.ApiWechatProcessService;
import com.cmall.ordercenter.util.HttpRequestUrlUtil;
import com.ordercenter.express.service.ReceiveCallback;
import com.srnpr.xmaspay.config.XmasPayConfig;
import com.srnpr.xmaspay.process.prepare.PayGatePreparePayProcess;
import com.srnpr.xmassystem.duohuozhu.model.ApiCode;
import com.srnpr.xmassystem.duohuozhu.utils.MD5Util;
import com.srnpr.xmassystem.duohuozhu.utils.XmlUtil;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmethod.RootControl;
import com.srnpr.zapweb.webmethod.WebUpload;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.webpage.RootProcess;


/**
 * Handles requests for the application home page.
 * 
 */ 
@Controller
public class HomeController extends RootControl  { 
	/**
	 * 通过code换取网页授权access_token、openId地址
	 */
	private static final String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
	/**
	 * 微信开发平台应用id(惠家有微信WAP支付)
	 */
	private static final String APPID = "wx7c73f526ee2324e8";
	/**
	 * 应用对应的凭证(惠家有微信WAP支付)
	 */
	private static final String SECRET = "bfe578412da6850d98c2defb555cc2a6";
	
	private static final String GRANT_TYPE = "authorization_code";

	
	/**
	 * 惠家有微信WAP支付
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechatWAP")
	public String wechatWAP(Model model,HttpServletRequest request) {
		
		String returnHTML = "/orderpay";
		
		RootResult rootResult = new RootResult();
		String code = request.getParameter("code");
		String orderCode = request.getParameter("orderCode");
		
		if(code!=null && !"".equals(code)){
			
			//通过code换取网页授权access_token、openid
			HttpRequestUrlUtil httpRequestUrl = new HttpRequestUrlUtil();
			
			//通过code换取网页授权access_token、openid
			String URLNEW = URL+"appid="+APPID+"&secret="+SECRET+"&grant_type="+GRANT_TYPE+"&code="+code;
			String reponseParams = httpRequestUrl.readContentFromGet(URLNEW);

			JSONObject jsonObject = JsonUtil.getJsonValues(reponseParams);
			
			if(!"".equals(String.valueOf(jsonObject.get("openid"))) && jsonObject.get("openid")!=null){
				
				String openid = String.valueOf(jsonObject.get("openid"));
				
				ApiWechatProcessService apiWechatProcessService = new ApiWechatProcessService();
				
				if(openid!=null && !"".equals(openid) && orderCode!=null && !"".equals(orderCode)){
					
					//获取微信支付相关信息
					Map wechatMap = apiWechatProcessService.wechatMovePaymentWapVersionNew(orderCode, "8.8.8.8", rootResult, openid);
					
					
					if(wechatMap!=null && !"".equals(wechatMap) && wechatMap.size()>0){
						
						
						model.addAttribute("appid", String.valueOf(wechatMap.get("appid")));
						model.addAttribute("mch_id", String.valueOf(wechatMap.get("mch_id")));
						model.addAttribute("nonce_str", String.valueOf(wechatMap.get("nonce_str")));
						model.addAttribute("prepay_id", String.valueOf(wechatMap.get("prepay_id")));
						model.addAttribute("sign", String.valueOf(wechatMap.get("sign")));
						model.addAttribute("trade_type", String.valueOf(wechatMap.get("trade_type")));
						model.addAttribute("result_code", String.valueOf(wechatMap.get("result_code")));
						model.addAttribute("return_code", String.valueOf(wechatMap.get("return_code")));
						model.addAttribute("return_msg", String.valueOf(wechatMap.get("return_msg")));
						model.addAttribute("timestamp", String.valueOf(wechatMap.get("timestamp")));
						model.addAttribute("orderCode", orderCode);
						
//						System.out.println("appid="+String.valueOf(wechatMap.get("appid"))+";mch_id="+String.valueOf(wechatMap.get("mch_id"))
//								+";nonce_str="+String.valueOf(wechatMap.get("nonce_str"))+";prepay_id="+String.valueOf(wechatMap.get("prepay_id")));
//						System.out.println("sign="+String.valueOf(wechatMap.get("sign")));
//						System.out.println("trade_type="+String.valueOf(wechatMap.get("trade_type")));
//						System.out.println("timestamp="+String.valueOf(wechatMap.get("timestamp")));
					}
				}
			}
			
		}
		return returnHTML;
	}
	
	/**
	 * 调用指定用户发优惠券--TMP(zht)
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/specUserCoupon")
	public String specUserCoupon(Model model,HttpServletRequest request) {
		String level = request.getParameter("level");
		JobSpecificUserCoupon jc = new JobSpecificUserCoupon();
		try {
			jc.httpDo(level);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * .net 提供的微信支付
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechatWAPNet")
	public String wechatWAPNet(Model model,HttpServletRequest request){
		
		String orderCode = request.getParameter("orderCode");
		String openid = request.getParameter("OpenID");
		
		System.out.println("==========================="+openid);
		System.out.println("==========================="+orderCode);
		
		
//		MDataMap map = new MDataMap();
//		map.put("merchantid", "13");
//		map.put("tradetype", "oauth");
//		map.put("orderno", "b");
//		map.put("tradetime", "");
//		map.put("TradeCode", "WeiXin");
//		map.put("channelid", "5");
//		map.put("CallBackURL", "http://api-family.syapi.ichsy.com/cfamily/wechatWAPNet?orderCode="+orderCode+"");
//		
//		PayUtil.getWechatOpenID(map);
		
		
		
		String returnHTML = "/orderpay";
		RootResult rootResult = new RootResult();
				
		ApiWechatProcessService apiWechatProcessService = new ApiWechatProcessService();
				
		if(openid!=null && !"".equals(openid) && orderCode!=null && !"".equals(orderCode)){
					
			//获取微信支付相关信息
			Map wechatMap = apiWechatProcessService.wechatMovePaymentWapVersionNew(orderCode, "8.8.8.8", rootResult, openid);
			
			if(wechatMap!=null && !"".equals(wechatMap) && wechatMap.size()>0){
				model.addAttribute("appid", String.valueOf(wechatMap.get("appid")));
				model.addAttribute("mch_id", String.valueOf(wechatMap.get("mch_id")));
				model.addAttribute("nonce_str", String.valueOf(wechatMap.get("nonce_str")));
				model.addAttribute("prepay_id", String.valueOf(wechatMap.get("prepay_id")));
				model.addAttribute("sign", String.valueOf(wechatMap.get("sign")));
				model.addAttribute("trade_type", String.valueOf(wechatMap.get("trade_type")));
				model.addAttribute("result_code", String.valueOf(wechatMap.get("result_code")));
				model.addAttribute("return_code", String.valueOf(wechatMap.get("return_code")));
				model.addAttribute("return_msg", String.valueOf(wechatMap.get("return_msg")));
				model.addAttribute("timestamp", String.valueOf(wechatMap.get("timestamp")));
				model.addAttribute("orderCode", orderCode);
				
				System.out.println("appid="+String.valueOf(wechatMap.get("appid"))+";mch_id="+String.valueOf(wechatMap.get("mch_id"))
						+";nonce_str="+String.valueOf(wechatMap.get("nonce_str"))+";prepay_id="+String.valueOf(wechatMap.get("prepay_id")));
				System.out.println("sign="+String.valueOf(wechatMap.get("sign")));
				System.out.println("trade_type="+String.valueOf(wechatMap.get("trade_type")));
				System.out.println("timestamp="+String.valueOf(wechatMap.get("timestamp")));
			}
		}

		return returnHTML;
	
	}
	
	
	
	@RequestMapping("/cfamily/coupon")
	public String lifehome(Model model,HttpServletRequest request){
		String resultView = "/cfamily/coupon";
		model.addAttribute("p", request.getParameter("p"));
		return resultView;
	}
	@RequestMapping("/cfamily/love520")
	public String love520(Model model,HttpServletRequest request){
		String resultView = "/cfamily/love520";
		model.addAttribute("cdkey", request.getParameter("cdkey"));
		return resultView;
	}
	
	
	/**
	 * 提供给跨境通调用
	 * @param sUrl
	 * 		请求的路径
	 * @param request
	 * 		请求信息
	 * @return 响应信息
	 */
	@RequestMapping(value = "/kjtapi" , produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String kjtapi(HttpServletRequest request) {

		return new KJTOrderOutInfoService().processRequest(request);

	} 
	
	

	/**
	 * 调用ld系统
	 * @param url
	 * 		路径
	 * @param data
	 * 		请求数据
	 * @return
	 * 		json格式数据
	 */
	@RequestMapping("/outbind")
	public @ResponseBody Map<String,Object> outbind(HttpServletRequest httpRequest){
		
		MDataMap mDataMap = new RootProcess().convertRequest(httpRequest);
		
		String sUrl = mDataMap.get("api_target");
				
		String sRequestString = mDataMap.get("api_input");
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		OutBindService service = new OutBindService();
		
		result.put("url", sUrl);
		
		result.put("data", service.callInterface(sUrl, sRequestString));
		
		return result;
		
	}
	
	
	/**
	 * 61
	 * 
	 * @param sDir
	 * @param sUrl
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cfamily/61/{url}")
	public String sixOne(@PathVariable("url") String sUrl, Model model,
			HttpServletRequest request) {
		
		model.addAttribute("mobile", request.getParameter("mobile"));
		return "/cfamily/61/" + sUrl;
	}
	
	/**
	 * 分享购物商品详情页跳转
	 * 
	 * @param sDir
	 * @param sUrl
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/shareshopping/{url}")
	public String web(@PathVariable("url") String sUrl, Model model,
			HttpServletRequest request) {
		
		if(null != request.getParameter("pc") && request.getParameter("pc").indexOf("IC") != -1){
			sUrl = "activityproductdetail";
		}
		model.addAttribute("b_method", root_method);
		return "web/shareshopping" +"/" + sUrl;
	}
	
	/**
	 * 支付宝WAP支付
	 * @param orderCode
	 * @param payType
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/payOrder/{orderCode}/{payType}")
	public String alipay(@PathVariable("orderCode") String orderCode, @PathVariable("payType") String payType,HttpServletRequest request, HttpServletResponse response) {
		AlipayProcess pool = new AlipayProcess();

		String hostString = request.getRequestURL().toString().split("//")[1];
		String contextPath = request.getContextPath();
		String submitStr = pool.upSubmitWebForm(orderCode,"http://"+hostString.substring(0,hostString.indexOf("/")) +contextPath);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(submitStr);
			if(out!=null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 微信商城支付接口 <br>
	 * @param orderCode
	 * @param payType
	 * @param request
	 * @param response    这个测试一下   怎么这样啊
	 * 
	 * @return
	 */
	@RequestMapping("/webPay/{bigOrderCode}/{payType}")
	public String webPay(@PathVariable("bigOrderCode") String bigOrderCode, @PathVariable("payType") String payType,HttpServletRequest request, HttpServletResponse response) {
		String openID = StringUtils.trimToEmpty(request.getParameter("openID"));
		String returnUrl = request.getParameter("returnUrl");
		String payGate = StringUtils.trimToEmpty(request.getParameter("payGate"));
		String sf = StringUtils.trimToEmpty(request.getParameter("sf"));
		
		if(StringUtils.isBlank(returnUrl)){
			returnUrl = XmasPayConfig.getPayGateWapDefaultReURL();
		}

		String errUrl = XmasPayConfig.getPayGateWapDefaultErrURL();
		
		// 短信支付记录标识
		// 微信付款提醒标识
		if("sms".equalsIgnoreCase(sf) || "wxpush".equalsIgnoreCase(sf)) {
			XmasKv.upFactory(EKvSchema.SmsPayFlag).setex(bigOrderCode, 3600*48, "1");
			returnUrl = XmasPayConfig.getPayGateSmsReURL();
		}
		
		// LD支付成功页面后续支付记录标识
		if("ldsms".equalsIgnoreCase(sf)) {
			returnUrl = XmasPayConfig.getPayGateLdSmsReURL();
		}
		
		String errorMsg = null;
		
		PayGatePreparePayProcess.PaymentResult result = null;
		if("449746280003".equals(payType)){
			// 支付宝支付
			result = new OrderPayProcess().aliPayH5PrepareNew(bigOrderCode, returnUrl, false, errUrl);
		}else if("449746280005".equals(payType)){
			if("sms".equalsIgnoreCase(sf)
					|| "ldsms".equalsIgnoreCase(sf)) {
				result = new OrderPayProcess().wechatSmsPrepare(bigOrderCode, returnUrl);
			} else {
				// 微信支付
				if(StringUtils.isNotBlank(openID)){
					result = new OrderPayProcess().wechatJSAPIPrepare(bigOrderCode, openID, returnUrl, false);
				}else{
					errorMsg = "openID is Empty!";
				}
			}
		}else if("449746280014".equals(payType)){
			// 银联支付
			result = new OrderPayProcess().unionPayWapPrepare(bigOrderCode, returnUrl, false);
		}else if("449746280016".equals(payType)){
			// 微匠支付
			result = new OrderPayProcess().vjmobiPayWapPrepare(bigOrderCode, returnUrl, payGate, openID);
		}else if("449746280020".equals(payType)){
			// 银联支付-分期付款
			result = new OrderPayProcess().unionPayFenqiWapPrepare(bigOrderCode, returnUrl);
		}else{
			errorMsg = "PayType Not Implemented!";
		}
		
		if(result != null && !result.upFlagTrue()){
			errorMsg = StringUtils.trimToEmpty(result.getResultMessage());
		}
		
		if(errorMsg == null){
			return "redirect:"+result.payUrl;
		}
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(errorMsg);
			if(out!=null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 微信商城嵌入APP时支付接口 <br>
	 * @param orderCode
	 * @param payType
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping("/wapPay/{bigOrderCode}/{payType}")
	public String webPayApp(@PathVariable("bigOrderCode") String bigOrderCode, @PathVariable("payType") String payType,HttpServletRequest request, HttpServletResponse response) {
		String returnUrl = request.getParameter("returnUrl");
		String payGate = StringUtils.trimToEmpty(request.getParameter("payGate"));
		String appVision = StringUtils.trimToEmpty(request.getParameter("app_vision"));
		
		if(StringUtils.isBlank(returnUrl)){
			returnUrl = XmasPayConfig.getPayGateWapDefaultReURL();
		}
		
		String errUrl = XmasPayConfig.getPayGateWapDefaultErrURL();
		
		String errorMsg = null;
		
		PayGatePreparePayProcess.PaymentResult result = null;
		if("449746280003".equals(payType)){
			// 支付宝支付
			if(StringUtils.isBlank(appVision) || AppVersionUtils.compareTo(appVision, "5.6.60") >= 0) {
				result = new OrderPayProcess().aliPayH5PrepareNew(bigOrderCode, returnUrl, true, errUrl);
			} else {
				result = new OrderPayProcess().aliPayH5Prepare(bigOrderCode, returnUrl, true);
			}
		}else if("449746280005".equals(payType)){
			// 微信支付
			result = new OrderPayProcess().wechatJSAPIPrepare(bigOrderCode, "", returnUrl, true);
		}else if("449746280014".equals(payType)){
			// 银联支付
			result = new OrderPayProcess().unionPayWapPrepare(bigOrderCode, returnUrl, true);
		}else if("449746280016".equals(payType)){
			// 微匠支付
			result = new OrderPayProcess().vjmobiPayWapPrepare(bigOrderCode, returnUrl, payGate, "");
		}else if("449746280020".equals(payType)){
			// 银联支付-分期付款
			result = new OrderPayProcess().unionPayFenqiWapPrepare(bigOrderCode, returnUrl);
		}else{
			errorMsg = "PayType Not Implemented!";
		}
		
		if(result != null && !result.upFlagTrue()){
			errorMsg = StringUtils.trimToEmpty(result.getResultMessage());
		}
		
		if(errorMsg == null){
			return "redirect:"+result.payUrl;
		}
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(errorMsg);
			if(out!=null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/face/{type}", method = RequestMethod.POST, produces = { "text/plain;charset=UTF-8" })
	@ResponseBody
	public String express(@PathVariable("type") String sType,
			HttpServletResponse response, HttpServletRequest request) {

		String sReturn = "";

		if (sType.equals("exp")) {
			String sign = request.getParameter("sign");
			String param = request.getParameter("param");
			//System.out.println("ddddddddddddd|"+sign);
			//System.out.println("ssssssssssssss|"+param);
			
			//ReceiverService xx = new ReceiverService();
			//sReturn = xx.doReceiver(param, sign);
			ReceiveCallback xx = new ReceiveCallback();
			sReturn = xx.doReceiver(param);
		}

		//System.out.println("return data to quaidi100  :" + sReturn);
		return sReturn;
	}
	
	/**
	 * 接受LD push过来的数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/acceptServer", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public HServerResponse acceptServer(@RequestBody String request) {
		return new HAcceptServer().doAccept(request);
	}
	
	/**
	 * 获取客户端ip地址
	 * @return
	 */
	@RequestMapping(value = "/getclientIp", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getclientIp (HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		response.setContentType("text/html;charset=UTF-8");
		try {
			String ipAddress = this.getIpAddress(request);
			resultMap.put("clientIp", ipAddress);
			PrintWriter writer = response.getWriter();
			writer.print(JSON.toJSONString(resultMap));
			if(writer!=null) {
				writer.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * 百分点获取屏蔽商品接口
	 * @return
	 */
	@RequestMapping(value = "/bfd/product", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object bfdProduct(HttpServletRequest req) {
		String day = StringUtils.trimToEmpty(req.getParameter("day"));
		if(StringUtils.isBlank(day)) day = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		
		List<MDataMap> mapList = DbUp.upTable("pc_product_bfd").queryAll("product_code", "", "", new MDataMap("day", day));
		List<String> list = new ArrayList<String>((int)(mapList.size() / 0.75) + 1);
		for(MDataMap map : mapList){
			list.add(map.get("product_code"));
		}
		
		Map<String,Object> resMap = new HashMap<String, Object>();
		resMap.put("day", day);
		resMap.put("dataList", list);
		
		return resMap;
	}
	
	/**
	 * 临时刷新缓存功能
	 * @return
	 */
	@RequestMapping(value = "/refresh", method = RequestMethod.GET, produces = { "text/html;charset=UTF-8" })
	@ResponseBody
	public Object refresh(HttpServletRequest req) {
		String eventCode = StringUtils.trimToEmpty(req.getParameter("code"));
		if(eventCode == null || !eventCode.startsWith("CX")){
			return "不是活动编号";
		}
		
		PlusHelperNotice.onChangeEvent(eventCode);
		
		return "SUCCESS";
	}
	
	/** 
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址; 
     *  
     * @param request 
     * @return 
     * @throws IOException 
     */  
    public static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
    }  
    
    /**
	 * 网易考拉商品信息变更回调
	 * @return
	 */
	@RequestMapping(value="/updateKLGoods")
	@ResponseBody
	public Map<String, Object> updateKLGoods(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		MDataMap logParams = new MDataMap();
		String requestTime = FormatHelper.upDateTime();
		
		String infoStr = request.getParameter("info") == null ? "" : request.getParameter("info").toString();
		try {
			if(!"".equals(infoStr)) {
				JSONObject infoJson = JsonUtil.getJsonValues(infoStr);
				String changeTypeChars = infoJson.get("changeTypes") == null ? "" : infoJson.get("changeTypes").toString();
				String changeTypeArray[] = changeTypeChars.replace("[", "").replace("]", "").split(",");
				List<String> changeTypeList = Arrays.asList(changeTypeArray);
				
				String channel = infoJson.get("channel") == null ? "" : infoJson.get("channel").toString();
				if(channel.equals(RsyncKaoLaSupport.getChannelId())) {
					if(changeTypeList.contains("4") || changeTypeList.contains("8") || changeTypeList.contains("9")) {
						//更新业务数据
						String skuId = infoJson.getString("skuId") == null ? "" : infoJson.getString("skuId").toString();
						String goodsId = infoJson.get("goodsId") == null ? "" : infoJson.get("goodsId").toString();
						if(!"".equals(skuId)) {
							RsyncKLtoHJYSkuInfoService.rsyncKLtoHJYSkuInfo(goodsId,skuId);
							
							result.put("recCode", 200);
							result.put("recMeg", "操作成功!");
						}else {
							result.put("recCode", 201);
							result.put("recMeg", "skuId有误!");
						}
					}else {
						result.put("recCode", 200);
						result.put("recMeg", "操作成功!");
					}
				}else {
					result.put("recCode", 201);
					result.put("recMeg", "渠道id有误!");
				}
				
				logParams.put("response_time", FormatHelper.upDateTime());
				logParams.put("response_data", result.toString());
			}else {
				result.put("recCode", 201);
				result.put("recMeg", "参数有误!");
			}
		}catch(Exception e) {
			logParams.put("exception_data", String.valueOf(e));
			e.printStackTrace();
		}finally {
			logParams.put("rsync_target", "updateKLGoods");
			logParams.put("request_url", "updateKLGoods");
			logParams.put("request_data", infoStr);
			logParams.put("request_time", requestTime);
			logParams.put("create_time", FormatHelper.upDateTime());
			
			if("on".equals(RsyncKaoLaSupport.getSwitch())) {
				DbUp.upTable("lc_rsync_kaola_log").dataInsert(logParams);
			}
		}
		return result;
	}
	
	//微信小程序-音乐相册 图片上传接口
	@RequestMapping(value = "/wxMusic/uploadImage", method = RequestMethod.POST ,produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> uploadImage(HttpServletResponse response, HttpServletRequest request) throws IOException {
		 Map<String, Object> result = new HashMap<String, Object>();
		 WebUpload webUpload = new WebUpload();
		 WXMusicAlbumService wxMusicAlbumService = new WXMusicAlbumService();

		//上传的服务器图片路径返回值
		MWebResult resultObj = webUpload.doRemoteUploadForMusicAlbum(request,"upload");
		Object retObjStr = resultObj.getResultObject();
		String[] split = retObjStr.toString().split("&");
		String uploadUrl ="";
		JSONObject jsonObject = new JSONObject();
		for (String string : split) {
			String[] split2 = string.split("=");
			if("file".equals(split2[0])) {
				uploadUrl=split2[1];
			}
			else {
				jsonObject.put(split2[0], split2[1]);
			}
		}
		
		result=wxMusicAlbumService.doProcess(jsonObject,uploadUrl);
		return result;
	}
	
	//微信小程序 图片上传接口
	@RequestMapping(value = "/img/upload", method = RequestMethod.POST ,produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public MWebResult uploadImg(HttpServletResponse response, HttpServletRequest request) throws IOException {
		MWebResult result = new MWebResult();
		WebUpload webUpload = new WebUpload();
		List<FileItem> files = VideoUtils.upFileFromRequest(request);
		WXMusicAlbumService wxMusicAlbumService = new WXMusicAlbumService();
		//上传的服务器图片路径返回值
		for(FileItem item : files) {
			String flag = new VideoUtils().checkPics(item.get(),wxMusicAlbumService.getToken());
			if("1".equals(flag)) {//通过鉴黄才允许上传服务器
				result = webUpload.remoteUpload("upload",item.getName(),item.get());
			}else {
				result.setResultCode(0);
				result.setResultMessage("图片违规，请重新选择图片上传");
			}
		}
		return result;
	}
	
	//视频上传接口
	@RequestMapping(value = "/video/upload", method = RequestMethod.POST ,produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Map<String, Object> uploadVideo(HttpServletResponse response, HttpServletRequest request) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		List<FileItem> items = VideoUtils.upFileFromRequest(request);
		String ccvid = "";
		String path = "";
		for(FileItem fileItem : items) {
			 if (!fileItem.isFormField()&&fileItem.getName()!=null&&!"".equals(fileItem.getName())){
                 String filName=fileItem.getName();
                 //利用UUID生成伪随机字符串，作为文件名避免重复
                 String uuid= UUID.randomUUID().toString().replaceAll("-", "");
                 //获取文件后缀名
                 String suffix=filName.substring(filName.lastIndexOf("."));

                 //获取文件上传目录路径，在项目部署路径下的upload目录里。若想让浏览器不能直接访问到图片，可以放在WEB-INF下

                 String uploadPath = request.getRealPath("/upload")+"/";
                 File file=new File(uploadPath);
                 file.mkdirs();
                 //写入文件到磁盘，该行执行完毕后，若有该临时文件，将会自动删除
                 try {
					fileItem.write(new File(uploadPath,uuid+suffix));
					String newPath = VideoUtils.cut(uploadPath+uuid+suffix, uploadPath, 15000l, suffix);
					//上传CC视频
					VideoInfo videoInfo = new VideoInfo(new File(newPath));
					Uploader upload = new Uploader(videoInfo);
					ccvid += (upload.upload()+"|");
					VideoUtils.deleteDir(uploadPath+uuid+suffix);//删除原文件
					VideoUtils.deleteDir(newPath);//删除切掉后的视频
					path += (uploadPath+uuid+suffix)+"|";
				} catch (Exception e) {
					e.printStackTrace();
				}
                 
             }
		}
		if(StringUtils.isEmpty(ccvid)) {
			result.put("success", false);
			result.put("resultCode", 999);
			result.put("resultMessage", "上传失败");
		}else {
			ccvid = ccvid.substring(0, ccvid.length()-1);
			path = path.substring(0, path.length()-1);
			result.put("success", true);
			result.put("ccvid", ccvid);
			result.put("resultCode", 1);
			result.put("resultMessage", "上传成功");
			MDataMap insert = new MDataMap();
			insert.put("uid", UUID.randomUUID().toString().replaceAll("-", "").trim());
			insert.put("path", path);
			insert.put("create_time", DateUtil.getSysDateTimeString());
			insert.put("ccvid", ccvid);
			DbUp.upTable("pc_product_evaluation_video").dataInsert(insert);
		}
		return result;
	}
	
	
	/**
	 * 多货主回传配送状态
	 * @param xml
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/deliveryStatus")
	@ResponseBody
	public String deliveryStatus(HttpServletRequest request) throws UnsupportedEncodingException {
		MDataMap logParams = new MDataMap();
		
		Map<String, Object> root = new LinkedHashMap<String, Object>();
		Map<String, Object> header = new LinkedHashMap<String, Object>();
		header.put("trade_id", UUID.randomUUID().toString().replaceAll("-", ""));
		header.put("function_id", ApiCode.CP000008.toString());
		header.put("cp_id", TopUp.upConfig("xmassystem.dhz_cp_id"));
		header.put("request_time", DateUtil.getSysDateTimeString1());
		
		List<LinkedHashMap<String, Object>> respstatusList = new ArrayList<LinkedHashMap<String, Object>>();
		
		BufferedReader reader = null;
		StringBuilder xmlSb = new StringBuilder();
		try {
			String todo = "";
			reader = new BufferedReader((new InputStreamReader(request.getInputStream())));
			while ((todo = reader.readLine()) != null) {
				xmlSb.append(todo);
            }
			
			if(StringUtils.isNotBlank(xmlSb.toString())) {
				System.out.println("/deliveryStatus："+xmlSb.toString());
				Map<String, Object> reqParams = XmlUtil.createMapByXml(xmlSb.toString());
				Map<String, Object> reqHeader = (Map<String, Object>) reqParams.get("header");
				Map<String, Object> body= (Map<String, Object>) reqParams.get("body");
				List<Map<String, Object>> statusList = (List<Map<String, Object>>) body.get("state");
				if(null!= statusList && !statusList.isEmpty()) {
					
					DuohzAfterSaleSupport duohzAfterSaleSupport = new DuohzAfterSaleSupport();
					
					for (Map<String, Object> statusNode : statusList) {
						String bizz_type = statusNode.get("bizz_type")+"";//业务类型 P R
						String cp_ord_id = statusNode.get("cp_ord_id")+"";//第三方订单编号
						String cp_rtn_id = statusNode.get("cp_rtn_id")+"";//第三方退货编号
						String cp_rtn_seq = statusNode.get("cp_rtn_seq")+"";//第三方退货序号
						String dlver_nm = statusNode.get("dlver_nm")+"";//配送公司
						String invc_id = statusNode.get("invc_id")+"";//运单号
						String status = statusNode.get("status")+"";//状态
						String status_time = statusNode.get("status_time")+"";//状态时间
						String desc = statusNode.get("desc")+"";//备注
						
						LinkedHashMap<String, Object> respStatusNode = new LinkedHashMap<String, Object>();
						respStatusNode.put("bizz_type", bizz_type);
						respStatusNode.put("cp_ord_id", cp_ord_id);
						respStatusNode.put("cp_rtn_id", cp_rtn_id);
						respStatusNode.put("cp_rtn_seq", cp_rtn_seq);
						respStatusNode.put("status", status);
						
						RootResult rootResult = new RootResult();
						if("P".equals(bizz_type)) {//正向
							if(StringUtils.isNotEmpty(cp_ord_id) && StringUtils.isNotEmpty(status)) {
								rootResult = duohzAfterSaleSupport.changeOrderStatus(cp_ord_id,status);
								if(1==rootResult.getResultCode() && StringUtils.isNotEmpty(dlver_nm) && StringUtils.isNotEmpty(invc_id)) {
									rootResult = duohzAfterSaleSupport.createShipment(cp_ord_id,dlver_nm,invc_id);
								}
							}
						}
						if("R".equals(bizz_type)) {//逆向
							if(StringUtils.isNotEmpty(cp_rtn_id) && StringUtils.isNotEmpty(status)) {
								rootResult = duohzAfterSaleSupport.changeAfterSaleStatus(cp_rtn_id,status);
							}
							if(1==rootResult.getResultCode() && StringUtils.isNotEmpty(dlver_nm) && StringUtils.isNotEmpty(invc_id)) {
								// 忽略换货单发货信息，直接在后台售后详情中查看
								if(!cp_rtn_id.startsWith("CGS190823100001")) {
									rootResult = duohzAfterSaleSupport.createShipment(cp_rtn_id,dlver_nm,invc_id);
								}
							}
						}
						
						respStatusNode.put("err_code", 1==rootResult.getResultCode()?"00":rootResult.getResultCode()+"");
						respStatusNode.put("err_msg", rootResult.getResultMessage());
						respstatusList.add(respStatusNode);
					}
				}
			}
		} catch (Exception e) {
			header.put("resp_code", "99");
			header.put("resp_msg", "接口处理异常");
			logParams.put("exception_data", String.valueOf(e));
		}finally {
			try {
				if(null != reader) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LinkedHashMap<String, Object> state = new LinkedHashMap<String, Object>();
		state.put("state", respstatusList);
		root.put("header", header);
		root.put("body", state);
		String todoResponse = XmlUtil.createXmlByMap(root, "root", false);
		String signed = MD5Util.MD5Encode(todoResponse+TopUp.upConfig("xmassystem.dhz_md5key"), "utf-8");
		header.put("signed", signed);
		root.remove("header");
		root.put("header", header);
		root.remove("body");
		root.put("body", state);
		String responsexml = XmlUtil.createXmlByMap(root, "root", true);
		
		if("on".equals(TopUp.upConfig("xmassystem.dhz_log_switch"))) {
			logParams.put("response_time", FormatHelper.upDateTime());
			logParams.put("response_data", responsexml);
			logParams.put("rsync_target", "CP000008");
			logParams.put("request_url", "/deliveryStatus");
			logParams.put("request_data", xmlSb.toString());
			logParams.put("request_time", DateUtil.getSysDateTimeString());
			logParams.put("create_time", DateUtil.getSysDateTimeString());
			DbUp.upTable("lc_rsync_duohz_log").dataInsert(logParams);
		}
		
		return URLEncoder.encode(responsexml, "utf-8");
	}
	
	
	//绘制商品分享图片
	@RequestMapping(value = "/imgHandler/getProShareImg", method = RequestMethod.GET)
	@ResponseBody
	public void getDecorationImg(HttpServletResponse response, HttpServletRequest request) throws IOException {
		
		 new ShareImageHandler().getShareImage(request,response);
		 
	}
	
	//验证是否能发言
	@RequestMapping(value = "/tencent/speakback") 
	@ResponseBody
	public String speakback(@RequestBody String body) throws IOException {
		MDataMap logParams = new MDataMap();
		logParams.put("rsync_target", "/tencent/speakback");
		logParams.put("uid", UUID.randomUUID().toString().replaceAll("-", "").trim());
		logParams.put("request_data", body);
		logParams.put("create_time", FormatHelper.upDateTime());
		String jsonString = "";
		com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
		object.put("ActionStatus", "OK");
		object.put("ErrorInfo", "");
		try {
			MDataMap mWhereMap = new MDataMap();
			com.alibaba.fastjson.JSONObject parseObject = com.alibaba.fastjson.JSONObject.parseObject(body);
			String group_id = parseObject.getString("GroupId");
			mWhereMap.put("group_id", group_id);
			List<Map<String,Object>> dataSqlList = DbUp.upTable("lv_live_room").dataSqlList("select * from lv_live_room where group_id =:group_id", mWhereMap);
			if(dataSqlList.size() > 0) {
				Map<String, Object> map = dataSqlList.get(0);
				String if_stop_comment = MapUtils.getString(map, "if_stop_comment");
				if("1".equals(if_stop_comment)) {
					object.put("ErrorCode", 1);
				}else if("0".equals(if_stop_comment)) {
					object.put("ErrorCode", 0);
				}
			}else {
				object.put("ErrorCode", 0);
			}
		}catch (Exception e) {
			logParams.put("exception_data", String.valueOf(e));
			object.put("ErrorCode", 1);
			e.printStackTrace();
		}finally {
			jsonString = object.toJSONString();
			logParams.put("response_data", jsonString);
			DbUp.upTable("lc_tencent_log").dataInsert(logParams);
			return jsonString;
		}
		
	}
	
	//腾讯云直播回放回调地址
	@RequestMapping(value = "/tencent/lookback")
	@ResponseBody
	public String lookback(@RequestBody String body) throws IOException {
		MDataMap logParams = new MDataMap();
		logParams.put("rsync_target", "/tencent/lookback");
		logParams.put("request_data", body);
		logParams.put("create_time", FormatHelper.upDateTime());
		logParams.put("uid", UUID.randomUUID().toString().replaceAll("-", "").trim());
		try {
			com.alibaba.fastjson.JSONObject parseObject = com.alibaba.fastjson.JSONObject.parseObject(body);
			String live_room_id = parseObject.getString("stream_id");
			String start_time = parseObject.getString("start_time");
			start_time = new SimpleDateFormat(DateUtil.DATE_FORMAT_DATETIME).format(new Date(Long.parseLong(start_time)*1000));
			String end_time = parseObject.getString("end_time");
			end_time = new SimpleDateFormat(DateUtil.DATE_FORMAT_DATETIME).format(new Date(Long.parseLong(end_time)*1000));
			String video_url = parseObject.getString("video_url");
			String file_id = parseObject.getString("file_id");
			int count = DbUp.upTable("lv_live_room_replay_infos").count("live_room_id",live_room_id,"file_id",file_id);
			if(count < 1) {
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("uid", UUID.randomUUID().toString().replaceAll("-", "").trim());
				mDataMap.put("live_room_id", live_room_id);
				mDataMap.put("start_time", start_time);
				mDataMap.put("end_time", end_time);
				mDataMap.put("video_url", video_url);
				mDataMap.put("file_id", file_id);
				DbUp.upTable("lv_live_room_replay_infos").dataInsert(mDataMap);
			}
		}catch (Exception e) {
			logParams.put("exception_data", String.valueOf(e));
			e.printStackTrace();
		}finally {
			DbUp.upTable("lc_tencent_log").dataInsert(logParams);
		}
		return "";
	}
	
	//短连接转原生链接
	@RequestMapping("/lts/{shortUrl}")     
	public ModelAndView jumpLongLink(HttpServletRequest request, ModelAndView mav, @PathVariable("shortUrl")String shortUrl) {

	    String longUrl = "";
	    String sql = "SELECT * FROM systemcenter.sc_shortlink where short_link_key = :short_link_key AND expire_time > sysdate()";
	    Map<String,Object> shortMap = DbUp.upTable("sc_shortlink").dataSqlOne(sql, new MDataMap("short_link_key",shortUrl));

	    if (shortMap!=null) {

	        longUrl = MapUtils.getString(shortMap, "long_link", "").trim();
	    }
	    mav.setViewName("redirect:" + longUrl);
	    return mav;
	}
	
}
