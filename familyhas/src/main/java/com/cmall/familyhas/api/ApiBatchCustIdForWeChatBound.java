package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.ApiBatchCustIdForWeChatBound.ImportCustIdInput;
import com.cmall.familyhas.api.ApiBatchCustIdForWeChatBound.ImportCustIdResult;
import com.cmall.familyhas.model.OrderDetail;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.HttpUtil;
import com.cmall.groupcenter.homehas.HomehasSupport;
import com.cmall.groupcenter.homehas.RsyncCustInfo;
import com.cmall.groupcenter.homehas.model.RsyncResponseRsyncCustInfo.Address;
import com.cmall.groupcenter.homehas.model.RsyncResponseRsyncCustInfo.CustInfo;
import com.cmall.membercenter.memberdo.MemberConst;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;

/**
 * 
 *<p>Description: <／p> 
 * @author zb
 * @date 2020年8月4日
 * 批量导入客代号绑定微信
 */
public class ApiBatchCustIdForWeChatBound extends RootApi<ImportCustIdResult, ImportCustIdInput> {
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	static AtomicInteger idx = new AtomicInteger();
	
	@SuppressWarnings("unused")
	private static class JsonResult {
		private List<String> success = new LinkedList<String>();
		private String notFound = "";
		private String same = "";
		
		public List<String> getSuccess() {
			return success;
		}
		public void setSuccess(List<String> success) {
			this.success = success;
		}
		public String getNotFound() {
			return notFound;
		}
		public void setNotFound(String notFound) {
			this.notFound = notFound;
		}
		public String getSame() {
			return same;
		}
		public void setSame(String same) {
			this.same = same;
		}
	}
	
	public static class ImportCustIdInput extends RootInput {
		//上传excel文件名(先传到文件服务器)
		private String upload_show;

		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}

		
		
	}
	
	public static class ImportModel {
		
		
		private boolean flag = true;
		private String error_message="";
		private String cust_id="";
		private String member_code="";
		private String name="";
		private String nick_name="";
		private String phone_num="";
		private String wechat_code="";
		private String nick_name_wechat="";
		private String registe_time="";
		private String operator="";
		private String is_bound="";
		
		
		
		public String getIs_bound() {
			return is_bound;
		}
		public void setIs_bound(String is_bound) {
			this.is_bound = is_bound;
		}
		public String getCust_id() {
			return cust_id;
		}
		public void setCust_id(String cust_id) {
			this.cust_id = cust_id;
		}
		public String getMember_code() {
			return member_code;
		}
		public void setMember_code(String member_code) {
			this.member_code = member_code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getNick_name() {
			return nick_name;
		}
		public void setNick_name(String nick_name) {
			this.nick_name = nick_name;
		}
		public String getPhone_num() {
			return phone_num;
		}
		public void setPhone_num(String phone_num) {
			this.phone_num = phone_num;
		}
		public String getWechat_code() {
			return wechat_code;
		}
		public void setWechat_code(String wechat_code) {
			this.wechat_code = wechat_code;
		}
		public String getNick_name_wechat() {
			return nick_name_wechat;
		}
		public void setNick_name_wechat(String nick_name_wechat) {
			this.nick_name_wechat = nick_name_wechat;
		}
		public String getRegiste_time() {
			return registe_time;
		}
		public void setRegiste_time(String registe_time) {
			this.registe_time = registe_time;
		}
		public String getOperator() {
			return operator;
		}
		public void setOperator(String operator) {
			this.operator = operator;
		}
		public boolean isFlag() {
			return flag;
		}
		public void setFlag(boolean flag) {
			this.flag = flag;
		}
		public String getError_message() {
			return error_message;
		}
		public void setError_message(String error_message) {
			this.error_message = error_message;
		}

	}
	
	public static class ImportCustIdResult extends RootResult {

	}

	@Override
	public ImportCustIdResult Process(ImportCustIdInput input, MDataMap mRequestMap) {
	    String fileRemoteUrl = input.getUpload_show();
	    ImportCustIdResult result = new ImportCustIdResult();
	    
	    Map<String, String > errorMap = new HashMap<String, String >();
	    
	    if(!StringUtils.isEmpty(fileRemoteUrl)) {
			java.net.URL resourceUrl;
			InputStream instream = null;
			try {
				resourceUrl = new java.net.URL(fileRemoteUrl);
				instream = (InputStream) resourceUrl.getContent();
				if(null != instream){
					List<ImportModel> rtnList = readExcel(instream);
					if(rtnList.size() <= 0) {
						result.setResultCode(0);
						result.setResultMessage("没有需要导入的数据!");
						return result;
					}
					//参数集合
					List<Map<String,Object>> listMap = new ArrayList<>();
					boolean isExcute = true;
					for (ImportModel importModel : rtnList) {
						if(!importModel.isFlag()) {
						isExcute = false;
						result.setResultCode(0);
						result.setResultMessage(importModel.getError_message());
						break;		
					}
				}
					if(isExcute) {
						RsyncCustInfo rsyncCustInfo = new RsyncCustInfo();
						for (ImportModel simportModel : rtnList) {
							MDataMap mDataMap = new MDataMap();
							mDataMap.put("uid",WebHelper.upUuid() );
							mDataMap.put("cust_id", simportModel.getCust_id());
							mDataMap.put("member_code", simportModel.getMember_code());
							mDataMap.put("name", simportModel.getName());
							mDataMap.put("nick_name", simportModel.getNick_name());
							mDataMap.put("phone_num", simportModel.getPhone_num());
							mDataMap.put("wechat_code", simportModel.getWechat_code());
							mDataMap.put("nick_name_wechat", simportModel.getNick_name_wechat());
							mDataMap.put("is_bound", simportModel.getIs_bound());
							mDataMap.put("operator", simportModel.getOperator());
							DbUp.upTable("mc_member_wechat_bound").dataInsert(mDataMap);
							//同步地址
							
							rsyncCustInfo.upRsyncRequest().setCust_id(simportModel.getCust_id());
							rsyncCustInfo.doRsync();
							CustInfo custInfo = rsyncCustInfo.getResponseObject().getResult().get(0); 
							List<Address> addressList = custInfo.getAddressList();
							if(addressList!=null&&addressList.size()>0) {
								int count = DbUp.upTable("nc_address").count("app_code","SI2003", "address_code", simportModel.getMember_code(),"address_default", "1");
								for (Address address : addressList) {
									MDataMap mDataMap2 = new MDataMap();
									mDataMap2.put("uid", WebHelper.upUuid());
									mDataMap2.put("address_id", WebHelper.upCode("DZ"));
									if(count>0) {
										mDataMap2.put("address_default","0" );
									}else {
										if("Y".equals(address.getIs_default())) {
											mDataMap2.put("address_default","1" );
										}else {
											mDataMap2.put("address_default","0" );
										}
									}
									mDataMap2.put("address_name",address.getRcver_name() );
									mDataMap2.put("address_mobile",address.getRcver_mobile() );
									mDataMap2.put("address_postalcode",address.getZip_no() );
									mDataMap2.put("address_province", address.getAddr_1());
									//mDataMap2.put("address_city","" );
									//mDataMap2.put("address_county","" );
									mDataMap2.put("address_street",address.getAddr_2() );
									mDataMap2.put("address_code",simportModel.getMember_code() );
									//mDataMap2.put("sort_num", );
									mDataMap2.put("app_code","SI2003" );
									mDataMap2.put("area_code", address.getArea_cd());
									//mDataMap2.put("email","" );
									mDataMap2.put("create_time", DateUtil.getNowTime());
									mDataMap2.put("update_time",DateUtil.getNowTime() );
									DbUp.upTable("nc_address").dataInsert(mDataMap2);
									
								}
							}
							
							
						}
						
						result.setResultCode(1);
						result.setResultMessage("导入成功！");
					 }
			}
				
			} catch (Exception e) {
				result.setResultMessage("导入失败" /*+ e.getLocalizedMessage()*/);
				e.printStackTrace();
			} finally {
				if(null != instream) try { instream.close(); } catch (IOException e) {}
			}
	    }
		return result;
	}
	
	/**
	 * 读取Excel商品数据
	 * 
	 * @param file
	 */
	public List<ImportModel> readExcel(InputStream input) {
		
		@SuppressWarnings("unused")
		String result = "";
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		try {
			Workbook wb = null;
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);// 第一个工作表
			resultModel = importCustId(sheet);
		} catch (FileNotFoundException e) {
			result = "导入采购信息数据失败！未找到上传文件";
			e.printStackTrace();
		} catch (IOException e) {
			result = "导入采购信息数据失败！" /*+ e.getLocalizedMessage()*/;
			e.printStackTrace();
		}
		return resultModel;
	}
	
	private List<ImportModel> importCustId(Sheet sheet) {
		
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		RsyncCustInfo rsyncCustInfo = new RsyncCustInfo();
		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();
		
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
			
			ImportModel model = new ImportModel();
			
			String custId ="";
			boolean flag = true;
		    
			try {
				Row row = sheet.getRow(rIndex);
				
				if( null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//custId
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						custId = row.getCell(0).getStringCellValue();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(0).getNumericCellValue();
						custId = new DecimalFormat("#").format(d); 
					}
				}
				
				if (!StringUtils.isEmpty(custId)) {
					int count = DbUp.upTable("mc_member_wechat_bound").count("cust_id", custId);
                    if (count > 0) {
						model.setCust_id(custId);
						model.setError_message("第" + (rIndex + 1) + "行,该客代号已存在，请检查！");
						model.setFlag(false);
						flag = false;
					}else {
						//请求家有的 实时数据
						rsyncCustInfo.upRsyncRequest().setCust_id(custId);
						rsyncCustInfo.doRsync();
						if(!rsyncCustInfo.getResponseObject().isSuccess() ||rsyncCustInfo.getResponseObject().getResult().size()<1){
							model.setCust_id(custId);
							model.setError_message("第" + (rIndex + 1) + "行,该客代号不正确，请检查！");
							model.setFlag(false);
							flag = false;
						}else {
							CustInfo custInfo = rsyncCustInfo.getResponseObject().getResult().get(0); 
							String phoneNum = custInfo.getHp_teld()+custInfo.getHp_telh()+custInfo.getHp_teln();
							String name = custInfo.getCust_nm();
							List<Map<String, Object>> dataSqlList = DbUp.upTable("mc_login_info").dataSqlList("select * from mc_login_info where login_name=:login_name ", new MDataMap("login_name",phoneNum));
							if(dataSqlList!=null&&dataSqlList.size()>0) {
								Map<String, Object> map = dataSqlList.get(0);
								String unionId = map.get("unionId").toString();
								String is_bound = "0", wxNickName="";
								if(StringUtils.isNotBlank(unionId)) {
									//不为空，未绑定微信
									is_bound="1";
									model.setWechat_code(unionId);
									wxNickName= getWXNickName(unionId);
									
								}
								String member_code = map.get("member_code").toString();
								MDataMap one = DbUp.upTable("mc_member_sync").one("login_name",phoneNum);
								String nickname =  (one==null?"":one.get("nickname"));
								//赋值
								model.setIs_bound(is_bound);
								model.setMember_code(member_code);
								model.setName(name==null?"":name);
								model.setNick_name(nickname);
								model.setNick_name_wechat(wxNickName);
								model.setPhone_num(phoneNum);
								
								String userCode = UserFactory.INSTANCE.create().getUserCode();
								model.setOperator(userCode);
	
							}else {
								//不存在，调贾老师接口创建用户
								HomehasSupport homehasSupport = new HomehasSupport();
								String register = homehasSupport.register(phoneNum,  RandomStringUtils.randomNumeric(8));
								if(StringUtils.isNotBlank(register)) {
								MDataMap map = DbUp.upTable("mc_login_info").one("login_name", phoneNum, "manage_code", MemberConst.MANAGE_CODE_HOMEHAS);
								if(map!=null) {
									String unionId = map.get("unionId").toString();
									String is_bound = "0", wxNickName="";
									if(StringUtils.isNotBlank(unionId)) {
										is_bound="1";
										model.setWechat_code(unionId);
										wxNickName= getWXNickName(unionId);
									}
									String member_code = map.get("member_code").toString();
									MDataMap one = DbUp.upTable("mc_member_sync").one("login_name",phoneNum);
									String nickname =  (one==null?"":one.get("nickname"));
									//赋值
									model.setIs_bound(is_bound);
									model.setMember_code(member_code);
									model.setName(name);
									model.setNick_name(nickname);
									model.setNick_name_wechat(wxNickName);
									model.setPhone_num(phoneNum);
									String userCode = UserFactory.INSTANCE.create().getUserCode();
									model.setOperator(userCode);
								}
								}else {
									model.setCust_id(custId);
									model.setError_message("第" + (rIndex + 1) + "行,该客代号同步惠家有账号注册失败,请联系技术!");
									model.setFlag(false);
									flag = false;
								}
	
							}
							
						}
					}
				}
				
				if (flag) {
					model.setCust_id(custId);
					model.setFlag(true);
				}
				resultModel.add(model);

				}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return resultModel;
	}
	
	private boolean isEmpty(Object obj) {
		return null == obj || obj.toString().equals("");
	}
	
	
	 public String getWXNickName(String unionid) {
		 Date now = new Date();
		 List<String> paramList = new ArrayList<String>();
		 String hostUrl = bConfig("zapweb.wxgate_url");
	     String channeId = bConfig("zapweb.send_gzh_channelid");
	     String merchantId = bConfig("zapweb.wxgate_merchant_id");
	     String sKey = bConfig("zapweb.wxgate_merchant_key");
		 String orderno = dateFormat.format(now) + (1000 + idx.incrementAndGet());
		    String tradetime = dateFormat.format(now);
	        paramList.add("merchantid="+merchantId);
	        paramList.add("tradetype=SendWX");
	        paramList.add("TradeCode=Wx_Get_User_info");
	        paramList.add("orderno="+orderno);
	        paramList.add("tradetime="+tradetime);
	        paramList.add("sender=1");
	        paramList.add("v="+"1.1");
	        paramList.add("receivers="+unionid);
	        paramList.add("message=");
	        paramList.add("wxtype="+"USER");
	        paramList.add("tradekeyid=zh_CN");
	        paramList.add("channelid="+channeId);
	        String  paramStr=StringUtils.join(paramList,"&");
	        MDataMap mReqMap =  new MDataMap();
	        mReqMap.inAllValues(FormatHelper.upUrlStrings(paramStr));
	        String createSignature = createSignature(mReqMap,sKey);
	        paramStr = paramStr+"&mac="+createSignature;
	        String returnStr = HttpUtil.post(hostUrl, paramStr, "","application/x-www-form-urlencoded");
	        try {
				returnStr= URLDecoder.decode(returnStr, "gb2312");
				MDataMap mResponMap =  new MDataMap();
				mResponMap.inAllValues(FormatHelper.upUrlStrings(returnStr));
				String resultcode = mResponMap.get("resultcode");
				if("00".equals(resultcode)) {
					String syncReturn = mResponMap.get("SyncReturn");
					if(StringUtils.isNotBlank(syncReturn)) {
	        			Object parse = JSONObject.parse(syncReturn);
	        			net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(parse);
	        			String  nickname = fromObject.get("nickname").toString();
	                    return nickname;
			        		}
				}
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogFactory.getLog(ApiBatchCustIdForWeChatBound.class).error(returnStr);
			}
	        return "";
	        
	 }
	 
	 private String createSignature(Map<String, String> param,String merchantKey) {
			List<String> dataList = new ArrayList<String>();
			Set<Map.Entry<String, String>> entryList = param.entrySet();
			for (Map.Entry<String, String> entry : entryList) {
				if (entry.getValue() != null && !entry.getValue().toString().trim().isEmpty()) {
					dataList.add(entry.getValue().toString().trim());
				}
			}
			Collections.sort(dataList);

			dataList.add(merchantKey);
			String text = StringUtils.join(dataList, "");

			text = DigestUtils.md5Hex(text);
			return text;
		}
	
	
	/**
	 * 
	 * 方法: verifyFormData <br>
	 * 描述: 验证验证表单数据是否正确 <br>
	 * 
	 * @param map
	 * @return
	 */
	public static String verifyFormData(String start_date,String end_date,String sell_price ) {
		String error = "";
		String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
		String dateRegex = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|\n" + 
				           "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|\n" + 
				           "((0[48]|[2468][048]|[3579][26])00))-02-29)$";
		if (sell_price == null || "".equals(sell_price)) {
			error = "销售价格不能为空";
		} else if (!sell_price.matches(regex)) {
			error = "销售价格只能是数字";
		}

		else {
            
			if(start_date.matches(dateRegex)&&end_date.matches(dateRegex)) {
				Date startDate = DateUtil.toDate(start_date);
				Date endDate = DateUtil.toDate(end_date);
				Date nowTime = DateUtil.toDate(DateUtil.getSysDateString());
				if (startDate.compareTo(nowTime) < 0) {
					error = "开始日期必须大于等于当前日期";
				} else if (endDate.compareTo(nowTime) < 0) {
					error = "结束日期必须大于等于当前日期";
				} else if (endDate.compareTo(startDate) < 0) {
					error = "开始日期必须小于或等于结束日期";
				} 
			}
			else if(!start_date.matches(dateRegex)) {
				error = "开始日期格式不正确";
			}
			else if(!end_date.matches(dateRegex)) {
				error = "结束日期格式不正确";
			}
		
		}
		return error;
	}
}
