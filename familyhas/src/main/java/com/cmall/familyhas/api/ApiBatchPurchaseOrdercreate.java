package com.cmall.familyhas.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.ApiBatchPurchaseOrdercreate.ImportProductInput;
import com.cmall.familyhas.api.ApiBatchPurchaseOrdercreate.ImportProductResult;
import com.cmall.familyhas.model.OrderDetail;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.webfunc.FuncChangeSkuPriceService;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;

/**
 * 
 * @author zb
 * 采购商品批量导入下单
 *
 */
public class ApiBatchPurchaseOrdercreate extends RootApi<ImportProductResult, ImportProductInput> {
	
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
	
	public static class ImportProductInput extends RootInput {
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
		private String skuCodes="";
		private boolean flag = true;
		private String error_message="";
		private String receiver="";
		private String pca="";
		private String detail="";
		private String phone="";
		public String getSkuCodes() {
			return skuCodes;
		}
		public void setSkuCodes(String skuCodes) {
			this.skuCodes = skuCodes;
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
		public String getReceiver() {
			return receiver;
		}
		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
		public String getPca() {
			return pca;
		}
		public void setPca(String pca) {
			this.pca = pca;
		}
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		
		
		
		

	}
	
	public static class ImportProductResult extends RootResult {

	}

	@Override
	public ImportProductResult Process(ImportProductInput input, MDataMap mRequestMap) {
	    String fileRemoteUrl = input.getUpload_show();
	    ImportProductResult result = new ImportProductResult();
	    
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
						
						for (ImportModel simportModel : rtnList) {
							String skuCodes = simportModel.getSkuCodes();
							//解决前端商品选择框的一个bug,有时会传过来空sku的信息
							skuCodes = skuCodes.replace(",,", ",");
							if(StringUtils.startsWith(skuCodes, ",")) {
								skuCodes = StringUtils.substringAfter(skuCodes, ",");
							}
							if(StringUtils.endsWith(skuCodes, ",")) {
								skuCodes = StringUtils.substringBeforeLast(skuCodes, ",");
							}
							String sWhere = "sku_code in ('"+skuCodes.replace(",", "','")+"')";
							List<MDataMap> mapList=DbUp.upTable("pc_skuinfo").queryAll("", "", sWhere,null);
							List<String> basic_order_skus=new ArrayList<String>();
							String PCode = WebHelper.upCode("PCI");
							BigDecimal sumMoney = new BigDecimal("0");
							for (MDataMap mDataMap : mapList) {
								//商品入库
								MDataMap mDataMap2 = new MDataMap();
								mDataMap2.put("uid",WebHelper.upUuid() );
								mDataMap2.put("order_id", WebHelper.upCode("DD"));
								mDataMap2.put("sku_code",mDataMap.get("sku_code").toString() );
								mDataMap2.put("product_code",mDataMap.get("product_code").toString() );
								mDataMap2.put("product_name", mDataMap.get("sku_name").toString());
								mDataMap2.put("product_img",mDataMap.get("sku_picurl").toString() );
								mDataMap2.put("product_property",mDataMap.get("sku_keyvalue").toString() );
								mDataMap2.put("cost_money",mDataMap.get("cost_price").toString());
								mDataMap2.put("sell_money",mDataMap.get("sell_price").toString());
								mDataMap2.put("sku_num", "1");
								mDataMap2.put("order_creater",UserFactory.INSTANCE.create().getRealName() );
								mDataMap2.put("purchase_order_id",PCode );
								mDataMap2.put("if_selected", "1");
								String skuStr = mDataMap.get("sku_code").toString()+"_"+mDataMap.get("cost_price").toString()+"_"+mDataMap.get("sell_price").toString()+"_1_1";
								basic_order_skus.add(skuStr);
								sumMoney= sumMoney.add(new BigDecimal(mDataMap.get("sell_price").toString()));
								DbUp.upTable("oc_purchase_order_detail").dataInsert(mDataMap2);
							}
							//地址入库
							String detail = simportModel.getDetail();
							String pca = simportModel.getPca();
							String phone = simportModel.getPhone();
							String receiver = simportModel.getReceiver();
							MDataMap mDataMap3 = new MDataMap();
							String addressCode = WebHelper.upCode("ADR");
							mDataMap3.put("uid", WebHelper.upUuid());
							mDataMap3.put("purchase_order_id", PCode);
							mDataMap3.put("adress_id",addressCode);
							mDataMap3.put("select_flag","1" );
							mDataMap3.put("receiver",receiver );
							mDataMap3.put("province_city_district_code",pca );
							mDataMap3.put("detail_addtess",detail );
							mDataMap3.put("phone",phone );
							DbUp.upTable("oc_purchase_order_address").dataInsert(mDataMap3);
							
							//主表入库
							String currentTime = FormatHelper.upDateTime();
							MDataMap mDataMap = new MDataMap();
							String bosStr = StringUtils.join(basic_order_skus, ",");
							mDataMap.put("uid",WebHelper.upUuid() );
							mDataMap.put("purchase_order_id", PCode);
							mDataMap.put("purchase_num", mapList.size()+"");
							mDataMap.put("purchase_money",sumMoney.toString() );
							mDataMap.put("creater",UserFactory.INSTANCE.create().getRealName() );
							mDataMap.put("purchase_time", currentTime);
							mDataMap.put("phone",phone );
							mDataMap.put("status","449748490001" );
							mDataMap.put("basic_order_skus", bosStr);
							mDataMap.put("adress_id", addressCode);
							DbUp.upTable("oc_purchase_order").dataInsert(mDataMap);
							
						    //添加审批流程
							MDataMap mDataMap2 = new MDataMap();
							mDataMap2.put("uid", WebHelper.upUuid());
							mDataMap2.put("purchase_order_id", PCode);
							mDataMap2.put("purchase_order_status","449748490001" );
							mDataMap2.put("creater", UserFactory.INSTANCE.create().getRealName());
							mDataMap2.put("create_time",currentTime );
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
			resultModel = importProduct(sheet);
		} catch (FileNotFoundException e) {
			result = "导入采购信息数据失败！未找到上传文件";
			e.printStackTrace();
		} catch (IOException e) {
			result = "导入采购信息数据失败！" /*+ e.getLocalizedMessage()*/;
			e.printStackTrace();
		}
		return resultModel;
	}
	
	private List<ImportModel> importProduct(Sheet sheet) {
		
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		
		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();
		
		for (int rIndex = firstRowIndex + 2; rIndex <= lastRowIndex; rIndex++) {
			
			ImportModel model = new ImportModel();
			
			String skuCodes ="";
			String pcd ="";
			String phone = "";
			String detail="";
			String receiver = "";
			boolean flag = true;
		    
			try {
				Row row = sheet.getRow(rIndex);
				
				if( null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//sku编号
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						skuCodes = row.getCell(0).getStringCellValue();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(0).getNumericCellValue();
						skuCodes = new DecimalFormat("#").format(d); 
					}
				}
				
				if(!StringUtils.isEmpty(skuCodes)){
					int size = skuCodes.split(",").length;
					String newSkus = "'"+skuCodes.replace(",", "','")+"'";
					Map<String, Object> one = DbUp.upTable("pc_skuinfo").dataSqlOne("select count(sku_code) skuNum from pc_skuinfo where sku_code in ("+newSkus+")", null);
					if(size!=Integer.parseInt(one.get("skuNum").toString())) {
						model.setSkuCodes(skuCodes);
						model.setError_message("第"+(rIndex+1)+"行,存在无效的或者重复的sku编码，请检查！");
						model.setFlag(false);
						flag=false;
					}

				}
				if( null == row.getCell(1) || row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) {
					model.setSkuCodes(skuCodes);
					model.setError_message("第"+(rIndex+1)+"行,收件人姓名不能为空！");
					model.setFlag(false);
					flag=false;
				}else {

					if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
						receiver = row.getCell(1).getStringCellValue();
					} else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(1).getNumericCellValue();
						receiver = new DecimalFormat("#").format(d); 
					}
				}
				if( null == row.getCell(2) || row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK) {
					model.setSkuCodes(skuCodes);
					model.setError_message("第"+(rIndex+1)+"行,省市区信息不能为空！");
					model.setFlag(false);
					flag=false;
				}else {

					String pcaCode = "";
					if(row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING) {
						pcd = row.getCell(2).getStringCellValue();
					} else if(row.getCell(2).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(2).getNumericCellValue();
						pcd = new DecimalFormat("#").format(d); 
					}
					
					String[] split = pcd.split("-");
					if(split==null||split.length==0) {
						model.setSkuCodes(skuCodes);
						model.setError_message("第"+(rIndex+1)+"行,省市区信息不能为空！");
						model.setFlag(false);
						flag=false;
					}
					else if(split.length==3&&(split[0].startsWith("北京市")||split[0].startsWith("天津市")||split[0].startsWith("上海市")||split[0].startsWith("重庆市"))){
						List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where name like '%"+split[0]+"%' and code_lvl=1 ", null);
						if(dataSqlList!=null&&dataSqlList.size()>0) {
							Map<String, Object> map = dataSqlList.get(0);
							List<Map<String, Object>> dataSqlList1 = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where  code_lvl=2 and p_code=:p_code ", new MDataMap("p_code",map.get("code").toString()));
                            if(dataSqlList1!=null&&dataSqlList1.size()>0) {
                            	//默认为直辖区
                            	Map<String, Object> map1 = dataSqlList1.get(0);
    							List<Map<String, Object>> dataSqlList2 = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where name like '%"+split[1]+"%' and code_lvl=3 and p_code=:p_code ", new MDataMap("p_code",map1.get("code").toString()));
                                if(dataSqlList2!=null&&dataSqlList2.size()>0) {
                                	Map<String, Object> map2 = dataSqlList2.get(0);
        							List<Map<String, Object>> dataSqlList3 = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where name like '%"+split[2]+"%' and code_lvl=4 and p_code=:p_code ", new MDataMap("p_code",map2.get("code").toString()));
                                    if(dataSqlList3!=null&&dataSqlList3.size()>0) {
                                    	Map<String, Object> map3 = dataSqlList3.get(0);
                                    	pcaCode = map.get("code")+"_"+map1.get("code")+"_"+map2.get("code")+"_"+map3.get("code");
                                    	pcd = pcaCode;
                                    }
                                }
                            }

						}
						
						if(StringUtils.isBlank(pcaCode)) {
							model.setSkuCodes(skuCodes);
							model.setError_message("第"+(rIndex+1)+"行,未匹配到省市区！");
							model.setFlag(false);
							flag=false;
						}
					}else if(split.length==4){
						List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where name like '%"+split[0]+"%' and code_lvl=1 ", null);
						if(dataSqlList!=null&&dataSqlList.size()>0) {
							Map<String, Object> map = dataSqlList.get(0);
							List<Map<String, Object>> dataSqlList1 = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where  code_lvl=2 and p_code=:p_code and name like '%"+split[1]+"%'", new MDataMap("p_code",map.get("code").toString()));
                            if(dataSqlList1!=null&&dataSqlList1.size()>0) {
                            	//默认为直辖区
                            	Map<String, Object> map1 = dataSqlList1.get(0);
    							List<Map<String, Object>> dataSqlList2 = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where name like '%"+split[2]+"%' and code_lvl=3 and p_code=:p_code ", new MDataMap("p_code",map1.get("code").toString()));
                                if(dataSqlList2!=null&&dataSqlList2.size()>0) {
                                	Map<String, Object> map2 = dataSqlList2.get(0);
        							List<Map<String, Object>> dataSqlList3 = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where name like '%"+split[3]+"%' and code_lvl=4 and p_code=:p_code ", new MDataMap("p_code",map2.get("code").toString()));
                                    if(dataSqlList3!=null&&dataSqlList3.size()>0) {
                                    	Map<String, Object> map3 = dataSqlList3.get(0);
                                    	pcaCode = map.get("code")+"_"+map1.get("code")+"_"+map2.get("code")+"_"+map3.get("code");
                                    	pcd= pcaCode;
                                    }
                                }
                            }

						}
						
						if(StringUtils.isBlank(pcaCode)) {
							model.setSkuCodes(skuCodes);
							model.setError_message("第"+(rIndex+1)+"行,未匹配到省市区！");
							model.setFlag(false);
							flag=false;
						}
					}else {
						model.setSkuCodes(skuCodes);
						model.setError_message("第"+(rIndex+1)+"行,请填写完整省市区！");
						model.setFlag(false);
						flag=false;		
					} 
					
				}
				if( null == row.getCell(3) || row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK) {
					model.setSkuCodes(skuCodes);
					model.setError_message("第"+(rIndex+1)+"行,详细地址不能为空！");
					model.setFlag(false);
					flag=false;
				}else  {
					if(row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING) {
						detail = row.getCell(3).getStringCellValue();
					} else if(row.getCell(3).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(3).getNumericCellValue();
						detail = new DecimalFormat("#").format(d); 
					}
					
				if( null == row.getCell(4) || row.getCell(4).getCellType() == Cell.CELL_TYPE_BLANK) {
					model.setSkuCodes(skuCodes);
					model.setError_message("第"+(rIndex+1)+"行,联系电话不能为空！");
					model.setFlag(false);
					flag=false;
				}else {
					if(row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
						phone = row.getCell(4).getStringCellValue();
					} else if(row.getCell(4).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double d = row.getCell(4).getNumericCellValue();
						phone = new DecimalFormat("#").format(d); 
					}
				}
		        if(flag) {
		        	model.setSkuCodes(skuCodes);
		        	model.setFlag(true);
                    model.setPca(pcd);
                    model.setPhone(phone);
                    model.setDetail(detail);
                    model.setReceiver(receiver);
		        }
				
				resultModel.add(model);
				
			}
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
