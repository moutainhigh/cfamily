package com.cmall.familyhas.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.systemcenter.service.FlowBussinessService;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.MailSupport;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 惠家有专用
 * 系统定时（周期：5分钟）检查商品金额，如果商品下任何SKU小于50元，则自动下架商品并发下架通知邮件
 * @author ligj
 *
 */
public class JobBanProductStatus extends RootJob{
	private final String  PRODUCT_STATUS_UNSHELVE= "4497153900060003";			//商品已下架
	private final String  PRODUCT_STATUS_SALE= "4497153900060002";			//商品上架状态
	public void doExecute(JobExecutionContext context) {
		String maxSellPrice = "50";
		String sSql = " select pc.product_code as product_code,pc.product_name as product_name,pc.uid as uid from pc_productinfo pc ,pc_skuinfo ps "+
				" where pc.product_code = ps.product_code and pc.product_status='"+PRODUCT_STATUS_SALE+"' "+
				" and ps.sell_price < '"+maxSellPrice+"' and pc.seller_code='SI2003' group by pc.product_code";
		List<Map<String,Object>> banProductCode = DbUp.upTable("pc_productinfo").dataSqlList(sSql, null);
		for (Map<String, Object> map : banProductCode) {
			String productCode = map.get("product_code").toString();
			String productName = map.get("product_name").toString();
			String uid = map.get("uid").toString();
			this.productStatusUnshelve(uid);
			String mail_receive = bConfig("familyhas.mail_receive");		//邮件收件人
			String mail_title = bConfig("familyhas.mail_title");			//邮件标题
			String mail_content = bConfig("familyhas.mail_content");		//邮件内容
			for (String receiveUser: mail_receive.split(",")) {
				MailSupport.INSTANCE.sendMail(receiveUser, 
						FormatHelper.formatString(mail_title,productCode,productName,DateUtil.getSysDateTimeString()), 
						FormatHelper.formatString(mail_content,productCode,productName,DateUtil.getSysDateTimeString()));
			}
		}
	}
	/**
	 * 商品下架
	 * @return
	 */
	private int productStatusUnshelve(String productUid){
		
		if (StringUtils.isNotEmpty(productUid)) {
			FlowBussinessService fs = new FlowBussinessService();
			String flowBussinessUid=productUid;
			String fromStatus= PRODUCT_STATUS_SALE;
			String toStatus= PRODUCT_STATUS_UNSHELVE;
			String flowType = "449715390006";			//商家后台商品状态
			
			String userCode = "system";
			String remark = "商品下存在SKU小于50元，商品自动下架";
			
			MDataMap mSubMap = new MDataMap();
			mSubMap.put("flow_bussinessid", flowBussinessUid);
			mSubMap.put("from_status", fromStatus);
			mSubMap.put("to_status", toStatus);
			mSubMap.put("flow_type", flowType);
			mSubMap.put("remark", remark);
			RootResult ret =
					fs.ChangeFlow(flowBussinessUid, flowType, fromStatus, toStatus, userCode, remark, mSubMap);
		}
		return 0;
	}
	
}
