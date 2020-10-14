package com.cmall.familyhas.api;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.cmall.familyhas.api.input.ApiForNewProductCodeInput;
import com.cmall.familyhas.api.input.ApiForQRCodeInput;
import com.cmall.familyhas.api.result.ApiForNewProductCodeResult;
import com.cmall.familyhas.api.result.ApiForQRCodeResult;
import com.cmall.groupcenter.accountmarketing.util.DateFormatUtil;
import com.cmall.systemcenter.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webdo.WebTemp;

/***
 * 扫一扫二维码接口
 * 
 * @author xiegj
 *
 */
public class ApiForQRCode extends RootApiForVersion<ApiForQRCodeResult, ApiForQRCodeInput> {

	public ApiForQRCodeResult Process(ApiForQRCodeInput inputParam, MDataMap mRequestMap) {
		ApiForQRCodeResult result = new ApiForQRCodeResult();

		String sReturn = inputParam.getQrStr();

		boolean bFlagUrl = StringUtils.startsWithIgnoreCase(inputParam.getQrStr(), "http");
		String[] str = bConfig("familyhas.hjyerWeiMaUrl").split(",");
		boolean flag = false;
		String smgCode = "";
		for (int i = 0; i < str.length; i++) {
			String ss = str[i];
			if (inputParam.getQrStr().contains(ss)) {
				flag = true;
				smgCode = StringUtils.substringAfter(inputParam.getQrStr(), "=");

				// 增加校验 优先APPSMG

//				if (StringUtils.isBlank(smgCode)) {
//
//					if (StringUtils.contains(inputParam.getQrStr(), "/s/")) {
//						smgCode = "IC_APPSMG_" + StringUtils.substringAfterLast(inputParam.getQrStr(), "/");
//
//					}
//				}

				if (StringUtils.isBlank(smgCode)) {

					String sSkuCode = StringUtils.substringAfterLast(inputParam.getQrStr(), "/");

					List<MDataMap> mapdata = WebTemp.upTempDataList("sc_event_qrcode", "", "", "");
					for (MDataMap map : mapdata) {
						if (StringUtils.contains(inputParam.getQrStr(), map.get("qr_url"))) {
							smgCode = "IC_" + map.get("qr_code") + "_" + sSkuCode;
						}
					}

					if (StringUtils.isBlank(smgCode)) {

						if (StringUtils.contains(inputParam.getQrStr(), "/a/")) {
							smgCode = "IC_DM_" + sSkuCode;

						} else if (StringUtils.contains(inputParam.getQrStr(), "/b/")) {

							smgCode = "IC_BJTV_" + sSkuCode;

						} else {
							smgCode = "IC_APPSMG_" + sSkuCode;
						}
					}

				}

				break;
			}
		}
		if(bFlagUrl && inputParam.getQrStr().contains("collageCode")) {
			result.setGotoType("5");
		}else if (bFlagUrl
				&& (inputParam.getQrStr().contains(".huijiayou.cn") 
						|| inputParam.getQrStr().contains("m.jyh.com")
						|| inputParam.getQrStr().contains("r.jyh.com")
						|| inputParam.getQrStr().contains("s-test.huijiayou.cn")) 
						|| StringUtils.endsWithIgnoreCase(sReturn, bConfig("xmassystem.pnm_suffix"))
						|| (ArrayUtils.contains(bConfig("familyhas.smg_wap_url").split(","), sReturn) && AppVersionUtils.compareTo("5.1.0", getApiClient().get("app_vision")) <= 0) // 新扫码购WAP页面
						|| StringUtils.contains(sReturn, bConfig("familyhas.integral_team_url"))
				) {
			// 把扫码专题生成二维码时的后缀去掉
			sReturn = StringUtils.substringBeforeLast(sReturn, bConfig("xmassystem.pnm_suffix"));
			result.setGotoType("1");
		} else if (flag) {
			result.setGotoType("2");
			sReturn = smgCode;
			String url = getErweiUrl(inputParam.getQrStr());
			if (StringUtils.isNotBlank(url)) {
				result.setGotoType("1");
				sReturn = url;
			}else{
				// 老的扫码购需要跳转到新的商品编号上
				try {
					//if(new Date().compareTo(DateUtils.parseDate("2018-04-26", new String[]{"yyyy-MM-dd"})) > 0){  //for test
					if(new Date().compareTo(DateUtils.parseDate("2018-05-01", new String[]{"yyyy-MM-dd"})) > 0){
						ApiForNewProductCodeInput newInput = new ApiForNewProductCodeInput();
						newInput.setProductCode(sReturn);
						ApiForNewProductCodeResult newResult = new ApiForNewProductCode().Process(newInput, mRequestMap);
						if(StringUtils.isNotBlank(newResult.getProductCodeCopy())){
							sReturn = newResult.getProductCodeCopy();
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			/** 修改这句话是为了解决https://协议开头的bug **/
		} else if (bFlagUrl && inputParam.getQrStr().contains(":")
				&& StringUtils.isNumeric(StringUtils.substringAfterLast(sReturn, ":"))) {
			result.setGotoType("3");
			sReturn = StringUtils.substringAfterLast(sReturn, ":");
		} else if (bFlagUrl || ArrayUtils.contains(bConfig("familyhas.smg_wap_url").split(","), sReturn)) {
			result.setGotoType("4");
		} else {
			result.setGotoType("99");
		}
		result.setGotoContent(sReturn);
		return result;
	}

	private String getErweiUrl(String str) {
		String result = "";
		int minute = Integer.valueOf(bConfig("familyhas.TV_OVER"));
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.add(Calendar.MINUTE, minute);
		end.add(Calendar.MINUTE, 0 - minute);
		String ss = new DateFormatUtil().dateToString(start.getTime());
		String ee = new DateFormatUtil().dateToString(end.getTime());
		MDataMap map = DbUp.upTable("sc_erwei_code").one("img_href", str);
		if (map != null && !map.isEmpty()) {
			String productCode = map.get("product_code");
			String sql = "select * from pc_tv where good_id=:productCode and form_fr_date<:start_time and form_end_date>:end_time and so_id='1000001'";
			List<Map<String, Object>> li = DbUp.upTable("pc_tv").dataSqlList(sql,
					new MDataMap("productCode", productCode, "start_time", ss, "end_time", ee));
			if (li != null && !li.isEmpty()) {
				for (int i = 0; i < li.size(); i++) {
					if (StringUtils.isBlank(result)) {
						result = li.get(i).get("topic_link").toString();
					}
				}
			}
		}

		return result;
	}
}
