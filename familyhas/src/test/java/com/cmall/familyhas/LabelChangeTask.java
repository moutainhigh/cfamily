package com.cmall.familyhas;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class LabelChangeTask implements Callable<Integer> {
		private List<Map<String, Object>> subList;
		private String labelCode;
		public LabelChangeTask(List<Map<String, Object>> subList, String labelCode) {
			this.subList = subList;
			this.labelCode = labelCode;
		}
		
		@Override
		public Integer call() {
			Integer updated = new Integer(0);
			ProductJmsSupport pjs = new ProductJmsSupport();
			for (Map<String, Object> map : subList) {
				String productCode = map.get("product_code") != "" ? map.get("product_code").toString() : "";
				if(StringUtils.isNotBlank(productCode)) {
					System.out.print(productCode + ",");
					String sql = "update pc_productdescription set keyword=:labelCode where product_code=:productCode";
					int count = DbUp.upTable("pc_productdescription").dataExec(sql, new MDataMap("labelCode", labelCode, "productCode",productCode));
					if(count > 0) {
						PlusHelperNotice.onChangeProductInfo(productCode);
						//触发消息队列
						pjs.onChangeForProductChangeAll(productCode);
						updated++;
					}
				}
			}
			return updated;
		}
	}