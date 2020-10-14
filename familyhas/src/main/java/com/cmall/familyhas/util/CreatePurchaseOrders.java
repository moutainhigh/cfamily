package com.cmall.familyhas.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import com.cmall.groupcenter.homehas.HomehasSupport;
import com.cmall.membercenter.memberdo.MemberConst;
import com.srnpr.xmasorder.model.TeslaModelJdOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderAddress;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderExtras;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderInfoUpper;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.websupport.DataMapSupport;


public class CreatePurchaseOrders implements Runnable{


	   private List<String> purchase_order_id;
	   private String purchase_order_text;
	   private String createUser;

	   public CreatePurchaseOrders(List<String> purchase_order_id,String purchase_order_text,String createUser) {
              this.purchase_order_id=purchase_order_id;
              this.purchase_order_text=purchase_order_text;
              this.createUser = createUser;

		}
	    @Override
	    public void run() {

			String currentTime = FormatHelper.upDateTime();
			for (String pod : purchase_order_id) {
				DbUp.upTable("oc_purchase_order").dataUpdate(new MDataMap("status","449748490003","approve_remark",purchase_order_text,"purchase_order_id",pod), "approve_remark,status", "purchase_order_id");
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("uid", WebHelper.upUuid());
				mDataMap.put("purchase_order_id", pod);
				mDataMap.put("purchase_order_status", "449748490003");
				mDataMap.put("purchase_order_text",purchase_order_text );
				mDataMap.put("creater", createUser);
				mDataMap.put("create_time", FormatHelper.upDateTime());
				DbUp.upTable("oc_purchase_order_shenpi").dataInsert(mDataMap);	
				//生成订单
				//1.查询下单手机号是否为注册用户，否的话，需要新建该账户
				MDataMap map = DbUp.upTable("oc_purchase_order").one("purchase_order_id",pod);
				String phone = map.get("phone").toString();
				MDataMap userMap = DbUp.upTable("mc_login_info").one("login_name", phone, "manage_code", MemberConst.MANAGE_CODE_HOMEHAS);
				if(userMap==null) {
					HomehasSupport homehasSupport = new HomehasSupport();
					homehasSupport.register(phone,  RandomStringUtils.randomNumeric(8));
					userMap = DbUp.upTable("mc_login_info").one("login_name", phone, "manage_code", MemberConst.MANAGE_CODE_HOMEHAS);
				}
				String memberCode = userMap.get("member_code");
				String adress_id = map.get("adress_id").toString();
				MDataMap addressMap = DbUp.upTable("oc_purchase_order_address").one("adress_id",adress_id);
				List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_purchase_order_detail").dataSqlList("select * from oc_purchase_order_detail where purchase_order_id=:purchase_order_id and if_selected='1' and if_delete='1' ", new MDataMap("purchase_order_id",pod));
				if(dataSqlList!=null&&dataSqlList.size()>0) {
					TeslaXOrder teslaXOrder = new TeslaXOrder();
					TeslaModelOrderInfoUpper upper = teslaXOrder.getUorderInfo();
					String bigCode = WebHelper.upCode("OS");
					upper.setBigOrderCode(bigCode);
					upper.setUid(WebHelper.upUuid());
					upper.setBuyerCode(memberCode);
					upper.setBuyerMobile(phone);
					upper.setCreateTime(currentTime);
					upper.setUpdateTime(currentTime);
					upper.setSellerCode(MemberConst.MANAGE_CODE_HOMEHAS);
					BigDecimal bigDecimal = new BigDecimal("0");
					int num = 0;
					//订单来源：人工下单
					upper.setOrderSource("449715190037");
					//支付类型：线下支付
					upper.setPayType("449716200024");
					for (Map<String, Object> item : dataSqlList) {
						TeslaModelOrderInfo orderInfo = new TeslaModelOrderInfo();
						orderInfo.setBigOrderCode(bigCode);
						orderInfo.setBuyerCode(memberCode);
						orderInfo.setCreateTime(currentTime);
						BigDecimal subMoney = new BigDecimal(item.get("sell_money").toString());
						BigDecimal multiply = subMoney.multiply(new BigDecimal(item.get("sku_num").toString()));
						bigDecimal=bigDecimal.add(multiply);
						orderInfo.setDueMoney(multiply);
						//orderInfo.setIsKaolaOrder();
						orderInfo.setOrderCode(item.get("order_id").toString());
						orderInfo.setOrderChannel("449747430024");//惠家有采购订单
						orderInfo.setOrderMoney(multiply);
						orderInfo.setOrderStatus("4497153900010002");
						orderInfo.setOrderType("449715200005");
						orderInfo.setUid(WebHelper.upUuid());
						orderInfo.setProductMoney(multiply);
						orderInfo.setPayType("449716200024");
						orderInfo.setOrderSource("449715190037");
						orderInfo.setSellerCode(MemberConst.MANAGE_CODE_HOMEHAS);
						orderInfo.setProductName(item.get("product_name").toString());
						PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(item.get("product_code").toString()));
						orderInfo.setSmallSellerCode(productInfo.getSmallSellerCode());
						orderInfo.setOrderStatusExt("4497153900140002");
						MDataMap sellerInfoExt = DbUp.upTable("uc_seller_info_extend").one("small_seller_code", productInfo.getSmallSellerCode());
						orderInfo.setDeliveryStoreType(sellerInfoExt.get("delivery_store_type"));
						teslaXOrder.getSorderInfo().add(orderInfo);
						TeslaModelOrderDetail detail = new TeslaModelOrderDetail();
						detail.setCostPrice(new BigDecimal(item.get("cost_money").toString()));
						detail.setOrderCode(item.get("order_id").toString());
						detail.setUid(WebHelper.upUuid());
						detail.setSmallSellerCode(productInfo.getSmallSellerCode());
						detail.setSkuNum(Integer.parseInt(item.get("sku_num").toString()));
						detail.setSkuName((item.get("product_name").toString()));
						detail.setSkuCode(item.get("sku_code").toString());
						detail.setProductCode(item.get("product_code").toString());
						detail.setShowPrice(new BigDecimal(item.get("sell_money").toString()));
						detail.setSellPrice(new BigDecimal(item.get("sell_money").toString()));
						detail.setSkuPrice(new BigDecimal(item.get("sell_money").toString()));
						detail.setSell_skucode(item.get("sku_code").toString());
						detail.setSell_productcode(item.get("product_code").toString());
						detail.setGiftFlag("1");
						detail.setCostPrice(new BigDecimal(item.get("cost_money").toString()));
						detail.setTaxRate(productInfo.getTax_rate());
						teslaXOrder.getOrderDetails().add(detail);
						num++;
					}
					upper.setAllMoney(bigDecimal);
					upper.setDueMoney(bigDecimal);
					upper.setOrderMoney(bigDecimal);
					upper.setOrderNum(num);
					
					//地址信息封装
					String pcd = addressMap.get("province_city_district_code").toString();
					String[] split = pcd.split("_");
					String detailAddressStr = "";
					for (String st : split) {
						MDataMap one = DbUp.upTable("sc_tmp").one("code",st);
						if(one!=null) {
							detailAddressStr=detailAddressStr+one.get("name").toString();
						}
					}
					//MDataMap one = DbUp.upTable("mc_authenticationInfo").one("member_code",memberCode);
					String aCode = split[split.length-1];
					TeslaModelOrderAddress address = teslaXOrder.getAddress();
					address.setAddress(addressMap.get("detail_addtess").toString());
					address.setAreaCode(aCode);
					address.setAuthIdcardNumber(addressMap.get("identity_number")==null?"":addressMap.get("identity_number").toString());
					address.setAuthIdcardType("4497465200090001");
					address.setAuthPhoneNumber(addressMap.get("phone").toString());
					address.setAuthTrueName(addressMap.get("receiver").toString());
					address.setAuthAddress(detailAddressStr+addressMap.get("detail_addtess").toString());
					address.setMobilephone(addressMap.get("phone").toString());
					address.setPostcode(addressMap.get("postcode").toString());
					address.setReceivePerson(addressMap.get("receiver").toString());
					address.setAuthPhoneNumber(addressMap.get("phone").toString());
					address.setAuthTrueName(addressMap.get("receiver").toString());
					//持久化地址信息
					orderSaveToDB(teslaXOrder);
				}

				// 发送短信通知
		/*		MMessage messages = new MMessage();
				messages.setMessageContent("您在惠家有采购商品订单已生成，采购商品我们将在2-4个工作日邮寄，请注意查收，感谢支持~");
				messages.setMessageReceive(phone);
				messages.setSendSource("4497467200020006");
				MessageSupport.INSTANCE.sendMessage(messages);*/
				
			}
	    	
	    	
	    }

	    
		public boolean orderSaveToDB (TeslaXOrder teslaXOrder ) {
			
			DataMapSupport dataMapSupport = new DataMapSupport();
			
			try {
				//持久化  oc_orderinfo_upper
				TeslaModelOrderInfoUpper orderInfoUpper = teslaXOrder.getUorderInfo();
				dataMapSupport.saveToDb("oc_orderinfo_upper", orderInfoUpper,orderInfoUpper.getBigOrderCode());
				
				List<String> duohuozhuOrder = new ArrayList<String>();
				//持久化  oc_orderinfo  oc_orderadress
				for (TeslaModelOrderInfo orderInfo : teslaXOrder.getSorderInfo()) {				
					dataMapSupport.saveToDb("oc_orderinfo", orderInfo,orderInfo.getOrderCode());					
					//持久化地址信息
					TeslaModelOrderAddress orderAddress = teslaXOrder.getAddress();
					orderAddress.setUid(WebHelper.upUuid());
					orderAddress.setOrderCode(orderInfo.getOrderCode());
					dataMapSupport.saveToDb("oc_orderadress", orderAddress,orderAddress.getOrderCode());	
					//添加日志信息
					DbUp.upTable("lc_orderstatus").dataInsert(new MDataMap("code",orderInfo.getOrderCode(),"info","订单创建","create_time",orderInfo.getCreateTime(),"create_user",orderInfo.getBuyerCode(),"now_status",orderInfo.getOrderStatus()));
					//多货主订单
					if("4497471600430002".equals(orderInfo.getDeliveryStoreType())){
						//持久化oc_order_duohz
						DbUp.upTable("oc_order_duohz").insert("order_code",orderInfo.getOrderCode(),"small_seller_code",orderInfo.getSmallSellerCode(),"create_time",DateUtil.getSysDateTimeString());
						duohuozhuOrder.add(orderInfo.getOrderCode());
					}
				}
				
				//持久化  oc_orderdetail
				int seq = 0;
				for (TeslaModelOrderDetail orderDetail : teslaXOrder.getOrderDetails()) {
					dataMapSupport.saveToDb("oc_orderdetail", orderDetail,orderDetail.getOrderCode());
					//持久化多货主订单详情表
					if(duohuozhuOrder.contains(orderDetail.getOrderCode())) {
						for(int i = 0; i< orderDetail.getSkuNum(); i ++) {
							DbUp.upTable("oc_order_duohz_detail").insert("order_code",orderDetail.getOrderCode(),"sku_code",orderDetail.getSkuCode(),"seq",String.valueOf(++seq));
						}
					}
				}	
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}

}
