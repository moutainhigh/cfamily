package com.cmall.familyhas.webfunc;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改闪购商品位置
 * 
 * @author jlin
 *
 */
public class FuncEditFlashLocation extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MDataMap mEditMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		if (mEditMaps != null && mEditMaps.size() > 0) {
			String activity_code = mEditMaps.get("activity_code");

			for (Map.Entry<String, String> map : mEditMaps.entrySet()) {
				String key = map.getKey();
				String location = map.getValue();
				if (key.startsWith("pro")) {
					String product_code = key.substring(3);
					if (StringUtils.isBlank(location)) {
						location = "0";
					}
					DbUp.upTable("oc_flashsales_skuInfo").dataUpdate(
							new MDataMap("activity_code", activity_code,
									"product_code", product_code, "location",
									location), "location",
							"activity_code,product_code");
				}
			}
		}

		return mResult;
	}
}
