package com.cmall.familyhas.api;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.cmall.familyhas.api.ApiImportFxProduct.ImportProductInput;
import com.cmall.familyhas.service.ImportService;
import com.srnpr.xmassystem.load.LoadActivityAgent;
import com.srnpr.xmassystem.load.LoadCouponListForProduct;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportFenxiao;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;

public class ApiImportFxProduct extends RootApi<RootResult, ImportProductInput>{

	public  final String DEFAULT_TITLE_COLOR="#333333";
	public  final String DEFAULT_DESCRIPTION_COLOR ="#999999";
	public  final String DEFAULT_SHOWMORE_LINKTYPE="4497471600020004";
	public  final String DEFAUL="449746250002";

	static PlusSupportFenxiao plusSupportFenxiao = new PlusSupportFenxiao();
	
	@Override
	public RootResult Process(ImportProductInput inputParam, MDataMap mRequestMap) {
		
		
		RootResult result = new RootResult();
		String activity_code = inputParam.getEvent_code();
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		String fileRemoteUrl = inputParam.getUpload_show();
		if(!StringUtils.endsWith(fileRemoteUrl, ".xls")){
			result.setResultMessage("文件格式不对，请核实！");
			return result;
		}
		/*获取当前登录人*/
		String create_user = UserFactory.INSTANCE.create().getLoginName();

		try {
			List<Row> importExcel = new ImportService().importExcel(inputParam.getUpload_show(),0);
			List<ImportModel> importProduct = importProduct(importExcel);
		    if(importProduct.size()==0||importProduct.get(importProduct.size()-1).getError_message()!=null&&"导入数据为空".equals(importProduct.get(importProduct.size()-1).getError_message())) {
		    	result.setResultCode(0);
				result.setResultMessage("导入数据为空");
				return result;
		    }
		    int temNum = 1;
		    StringBuffer sb= new StringBuffer();
			for (ImportModel importModel : importProduct) {
				temNum++;
				if(importModel.isFlag()) {
					if(StringUtils.isBlank(importModel.getProduct_code()))
						continue;
					String product_code = importModel.getProduct_code();
					String sort = importModel.getSort();
					String do_type = inputParam.getDo_type();
					if(do_type.equals("2")){
						MDataMap mInsertMap = new MDataMap();
						mInsertMap.put("flag_enable", "0");
						mInsertMap.put("activity_code", activity_code);
						mInsertMap.put("produt_code", product_code);
						DbUp.upTable("oc_activity_agent_product").dataUpdate(mInsertMap, "flag_enable", "activity_code,produt_code");
					}else{
						int count = DbUp.upTable("oc_activity_agent_product").count("activity_code",activity_code,"produt_code",product_code,"flag_enable","1");
						if(count>0){
							continue;
						}
						MDataMap couponInfo = plusSupportFenxiao.getFenxiaoCouponInfo(product_code);
						//添加优惠券类型
						MDataMap maps=new MDataMap();
						String coupon_type_code = WebHelper.upCode("CT");
						maps.put("coupon_type_code", coupon_type_code);
						maps.put("coupon_type_name", "分销券");
						maps.put("activity_code", activity_code);
						maps.put("money", couponInfo.get("coupon_money"));
						maps.put("status", "4497469400030002");
						maps.put("limit_condition", "4497471600070002");
						maps.put("limit_scope", "指定商品可用");
						maps.put("valid_type", "4497471600080001");
						maps.put("valid_day", "999");
						
						maps.put("create_time", createTime);
						maps.put("creater", create_user);
						maps.put("update_time", createTime);
						maps.put("updater", create_user);
						maps.put("manage_code", UserFactory.INSTANCE.create().getManageCode());
						DbUp.upTable("oc_coupon_type").dataInsert(maps);
						//添加优惠券商品限制
						MDataMap maps1=new MDataMap();
						maps1.put("coupon_type_code", coupon_type_code);
						maps1.put("activity_code", activity_code);
						maps1.put("brand_limit", "4497471600070001");
						maps1.put("product_limit", "4497471600070002");
						maps1.put("category_limit", "4497471600070001");
						maps1.put("channel_limit", "4497471600070002");
						maps1.put("activity_limit", "449747110001");
						maps1.put("product_codes", product_code);
						maps1.put("channel_codes", "449747430023");
						DbUp.upTable("oc_coupon_type_limit").dataInsert(maps1);
						//添加分销商品
						MDataMap maps2=new MDataMap();
						maps2.put("update_user", create_user);
						maps2.put("create_time", createTime);
						maps2.put("update_time", createTime);
						maps2.put("activity_code", activity_code);
						maps2.put("produt_code", product_code);
						maps2.put("position", sort);
						maps2.put("sell_price", couponInfo.get("sell_price"));
						maps2.put("cost_price", couponInfo.get("cost_price"));
						maps2.put("coupon_money", couponInfo.get("coupon_money"));
						maps2.put("coupon_type_code", coupon_type_code);
						DbUp.upTable("oc_activity_agent_product").dataInsert(maps2);
					}					
					new LoadCouponListForProduct().deleteInfoByCode(product_code);
				} else {
					
					sb.append("第"+temNum+"行:"+importModel.getError_message()+"\r\n");
					if(sb.toString().length()>0)
						result.setResultMessage(sb.toString());
					return result;
				}
			}	
			new LoadActivityAgent().refresh(new PlusModelQuery("SI2003"));
			
		} catch (IOException e) {
			result.setResultCode(0);
			result.setResultMessage("导入数据错误，请检查导入数据信息");
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	public static class ImportProductInput extends RootInput {
		//上传excel文件名(先传到文件服务器)
		private String upload_show;
		
		private String event_code;
		
		private String do_type;
		
		public String getDo_type() {
			return do_type;
		}

		public void setDo_type(String do_type) {
			this.do_type = do_type;
		}

		public String getEvent_code() {
			return event_code;
		}

		public void setEvent_code(String event_code) {
			this.event_code = event_code;
		}

		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}

	}
	
	//获取商品数据
	private List<ImportModel> importProduct(List<Row> rowlist) {
		
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		int num = 0;
		for (Row row : rowlist) {
			
			ImportModel model = new ImportModel();
			//商品编号
			String product_code = "";
			String sort = "";
			try {
				if(  null != row&&null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//取商品编号，没有认为结束
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						product_code = row.getCell(0).getStringCellValue().trim();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double a = row.getCell(0).getNumericCellValue();
						product_code = new DecimalFormat("#").format(a).trim(); 
					}
					if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
						sort = row.getCell(1).getStringCellValue().trim();
					} else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double a = row.getCell(1).getNumericCellValue();
						sort = new DecimalFormat("#").format(a).trim(); 
					}
					  //产品编号字段校验
					if(!StringUtils.isEmpty(product_code)) {
						Map<String,Object> map = DbUp.upTable("pc_productinfo").dataSqlOne("select product_code, product_name from "
								+ "pc_productinfo where seller_code ='SI2003' and product_code='" + product_code + "'", null);
						Pattern pattern = Pattern.compile("[0-9]*");
						Matcher matcher = pattern.matcher(product_code);
					
						if(!matcher.matches()) {
							//商品编号格式错误
							model.setProduct_code(product_code);
							String temMessage = model.getError_message();
							if(!"".equals(temMessage)) {
								temMessage=temMessage+",";
							}
							model.setError_message(temMessage+"商品编号异常");
							model.setFlag(false);
						
				
						}else  if(map==null||map.size()<=0){
							//查询不到对应的记录
							model.setProduct_code(product_code);
							String temMessage = model.getError_message();
							if(!"".equals(temMessage)) {
								temMessage=temMessage+",";
							}
							model.setError_message(temMessage+"无对应商品信息");
							model.setFlag(false);
							
						}else {
							  model.setProduct_code(product_code);
							  model.setSort(sort);
						}		
					} 
					resultModel.add(model); 					 
			     }else {
					num++;
					if(num==rowlist.size())
					{
						model.setError_message("导入数据为空");
						model.setFlag(false);
					}
				}
				    	  
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return resultModel;
	}	
	
public static class ImportModel {

		private String product_code = "";
		
		private String sort = "";
			
		public String getSort() {
			return sort;
		}

		public void setSort(String sort) {
			this.sort = sort;
		}

		public String getProduct_code() {
			return product_code;
		}

		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}



		private boolean flag = true;
		
		private String error_message = "";

	

		/**
		 * 默认为true
		 * @return
		 */
		public boolean isFlag() {
			return flag;
		}

		/**
		 * 默认为true
		 * @param flag
		 */
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
	
}
