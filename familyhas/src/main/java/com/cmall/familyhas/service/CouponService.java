package com.cmall.familyhas.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.cmall.groupcenter.homehas.RsyncControlGiftVoucher;
import com.cmall.groupcenter.homehas.model.RsyncModelGiftVoucher;
import com.cmall.ordercenter.model.api.GiftVoucherInfo;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class CouponService extends BaseClass {

	/**
	 * 取消订单/取消发货回写礼金券给LD
	 * @param reWriteLD
	 */
	public void reWriteGiftVoucherToLD(List<GiftVoucherInfo> reWriteLD, String doType) {
		/**此处已停掉 礼金券所有操作都走异步推送项目 -rhb 20180927*/
//		if(reWriteLD != null && reWriteLD.size() > 0) {
//			reWriteLD = CouponService.reOrgCoupons(reWriteLD); //合并订单号
//			RsyncControlGiftVoucher rsync = new RsyncControlGiftVoucher();
//			List<RsyncModelGiftVoucher> list = new ArrayList<RsyncModelGiftVoucher>();
//			StringBuffer requestB = new StringBuffer();
//			for(GiftVoucherInfo coupon : reWriteLD) {
//				requestB.append(",");
//				RsyncModelGiftVoucher model = new RsyncModelGiftVoucher();
//				model.setHjy_ord_id(coupon.getHjy_ord_id());
//				Map<String, Object> map = DbUp.upTable("oc_coupon_info").dataSqlOne(
//								"SELECT out_coupon_code from oc_coupon_info where coupon_code=:coupon_code ",
//								new MDataMap("coupon_code", coupon.getLj_code().toString()));
//				if(map != null && map.get("out_coupon_code") != null) {
//					model.setLj_code(map.get("out_coupon_code").toString());
//					requestB.append("{\"hjy_ord_id\":\""+coupon.getHjy_ord_id()+"\"");
//					requestB.append(",\"lj_code\":\""+map.get("out_coupon_code").toString()+"\"}");
//					list.add(model);
//				}				
//			}
//			String request = "{\"doType\":\""+doType+"\",\"reWriteLD\":[" + requestB.substring(1) + "]}";
//			System.out.println("com.cmall.familyhas.service.CouponService reWriteGiftVoucherToLD request :" + request);
//			rsync.upRsyncRequest().setDo_type(doType);
//			rsync.upRsyncRequest().setLjqList(list);
//			rsync.doRsync();
//		}
	}
	
	/**
	 * 将订单号合并
	 * @param reWriteLD
	 * @return
	 */
	public static List<GiftVoucherInfo> reOrgCoupons(List<GiftVoucherInfo> reWriteLD) {
		//按订单号排序
		Collections.sort(reWriteLD, new Comparator<Object>() {
		      public int compare(Object info1, Object info2) {
		    	  String one = ((GiftVoucherInfo)info1).getHjy_ord_id().toString();
		    	  String two = ((GiftVoucherInfo)info2).getHjy_ord_id().toString();
		        return two.compareTo(one);
		      }
		    });
		for(int i=0;i<reWriteLD.size()-1;i++) {
			for(int j=reWriteLD.size()-1;j>i;j--) {
				if(reWriteLD.get(j).getLj_code().toString().equals(reWriteLD.get(i).getLj_code().toString())) {
					String orders = reWriteLD.get(i).getHjy_ord_id().toString() + "," + reWriteLD.get(j).getHjy_ord_id().toString();
					reWriteLD.get(i).setHjy_ord_id(orders);
					reWriteLD.remove(j);
				}
			}
		}		
		return reWriteLD;
	}
}
