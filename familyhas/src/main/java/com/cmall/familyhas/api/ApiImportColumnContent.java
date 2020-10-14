package com.cmall.familyhas.api;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.ApiImportColumnContent.ImportProductInput;
import com.cmall.familyhas.service.ImportService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;

public class ApiImportColumnContent extends RootApi<RootResult, ImportProductInput>{

	public  final String DEFAULT_TITLE_COLOR="#333333";
	public  final String DEFAULT_DESCRIPTION_COLOR ="#999999";
	public  final String DEFAULT_SHOWMORE_LINKTYPE="4497471600020004";
	public  final String DEFAUL="449746250002";

	
	@Override
	public RootResult Process(ImportProductInput inputParam, MDataMap mRequestMap) {
		
		
		RootResult result = new RootResult();
		String column_code = inputParam.getColumn_code().trim();
		String column_type = inputParam.getColumn_type().trim();
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
			List<ImportModel> importProduct = importProduct(importExcel,createTime,column_code);
			StringBuffer sql = new StringBuffer();
			String execSql  = " INSERT INTO familyhas.fh_apphome_column_content(uid,column_code,start_time,end_time,position,title_color,description_color,showmore_linktype,showmore_linkvalue,is_share,"
					+ "is_delete,create_time,create_user,update_time,update_user,skip_place,video_ad,video_link,product_code,product_name,product_price)  VALUES ";
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
						break;
					String sUid = UUID.randomUUID().toString().replace("-", "");
					sql.append("('"+sUid+"',");
					sql.append("'"+column_code+"',");
					sql.append("'"+importModel.getStart_time()+"',");
					sql.append("'"+importModel.getEnd_time()+"',");
					sql.append("'"+importModel.getPosition()+"',");
					sql.append("'"+DEFAULT_TITLE_COLOR+"',");
					sql.append("'"+DEFAULT_DESCRIPTION_COLOR+"',");
					sql.append("'"+DEFAULT_SHOWMORE_LINKTYPE+"',");
					sql.append("'"+importModel.getProduct_code()+"',");
					sql.append("'"+DEFAUL+"',");
					sql.append("'"+DEFAUL+"',");
					sql.append("'"+createTime+"',");
					sql.append("'"+create_user+"',");
					sql.append("'"+createTime+"',");
					sql.append("'"+create_user+"',");
					sql.append("'"+DEFAUL+"',");
					sql.append("'"+importModel.getVideo_ad()+"',");
					sql.append("'"+importModel.getVideo_link()+"',");
					sql.append("'"+importModel.getProduct_code()+"',");
					sql.append("'"+importModel.getProduct_name()+"',");
					sql.append(""+importModel.getProduct_price()+"),");
					
				} else {
					
					sb.append("第"+temNum+"行:"+importModel.getError_message()+"\r\n");
					if(sb.toString().length()>0)
						result.setResultMessage(sb.toString());
					return result;
				}
			}
			if(sql.length() > 0) {
				String temSql = sql.toString().substring(0,sql.toString().length()-1);
				execSql += temSql;
				DbUp.upTable("fh_apphome_column_content").dataExec(execSql, new MDataMap());
			}
	
			
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
		
		private String column_code;
		
		public String getColumn_code() {
			return column_code;
		}

		public void setColumn_code(String column_code) {
			this.column_code = column_code;
		}

		public String getColumn_type() {
			return column_type;
		}

		public void setColumn_type(String column_type) {
			this.column_type = column_type;
		}

		private String column_type;
		
		public String getUpload_show() {
			return upload_show;
		}

		public void setUpload_show(String upload_show) {
			this.upload_show = upload_show;
		}

	}
	
	//获取商品数据
	private List<ImportModel> importProduct(List<Row> rowlist,String currentTime,String columnCode) {
		
		List<ImportModel> resultModel = new ArrayList<ImportModel>();
		
		List<Map<String,Object>> listMap = new ArrayList<>();  //同一个时间段内的位置不能重复
		int num = 0;
		for (Row row : rowlist) {
			
			ImportModel model = new ImportModel();
			//商品编号
			String product_code = "";
			//开始时间
			String start_time = "";
			//结束时间
			String end_time = "";
			//位置
			String position = "";
			//广告语
			String video_ad = "";
			//视频连接
			String video_link = "";
			
			
			//参数map,存储导入的表格位置和时间，进行校验判断
            Map<String,Object> temMap = new HashMap<String,Object>();
			try {
				boolean timeFlag1 = true;
				boolean timeFlag2 = true;
				if(  null != row&&null != row.getCell(0) && row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					//取商品编号，没有认为结束
					if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
						product_code = row.getCell(0).getStringCellValue().trim();
					} else if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
						Double a = row.getCell(0).getNumericCellValue();
						product_code = new DecimalFormat("#").format(a).trim(); 
					}

					if(!StringUtils.isEmpty(product_code)) {
						Map<String,Object> map = DbUp.upTable("pc_productinfo").dataSqlOne("select product_code, product_name,min_sell_price from "
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
						
				
						}
						else  if(map==null||map.size()<=0){
							//查询不到对应的记录
							model.setProduct_code(product_code);
							String temMessage = model.getError_message();
							if(!"".equals(temMessage)) {
								temMessage=temMessage+",";
							}
							model.setError_message(temMessage+"无对应商品信息");
							model.setFlag(false);
							
						}
						
						else {
							  model.setProduct_code(product_code);
							  model.setProduct_name(map.get("product_name").toString());
							  model.setProduct_price(map.get("min_sell_price").toString());
						}
						  
							
					}
					
					
					//获取开始日期
					if(null == row.getCell(1) || row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) {
						model.setProduct_code(product_code);
						String temMessage = model.getError_message();
						if(!"".equals(temMessage)) {
							temMessage=temMessage+",";
						}
						model.setError_message(temMessage+"存在开始日期为空错误");
						model.setFlag(false);
						timeFlag1=false;
						
					}
					else {
						if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
							start_time = row.getCell(1).getStringCellValue().trim();
						} else if(row.getCell(1).getCellType()==Cell.CELL_TYPE_NUMERIC) {
							Double a = row.getCell(1).getNumericCellValue();
							start_time = new DecimalFormat("#").format(a).trim(); 
						}	
						
					}
	
					//获取结束日期
					if(null == row.getCell(2) || row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK) {
						model.setProduct_code(product_code);
						String temMessage = model.getError_message();
						if(!"".equals(temMessage)) {
							temMessage=temMessage+",";
						}
						model.setError_message(temMessage+"存在结束日期为空错误");
						timeFlag2=false;
						model.setFlag(false);
						
					}
					else {
						if(row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING) {
							end_time = row.getCell(2).getStringCellValue().trim();
						} else if(row.getCell(2).getCellType()==Cell.CELL_TYPE_NUMERIC) {
							Double a = row.getCell(3).getNumericCellValue();
							end_time = new DecimalFormat("#").format(a).trim(); 
						}
						
					}
			
				if(true) {
					//进行日期字段校验
			
					if(start_time.contains("-")&&start_time.contains("/")||!DateUtil.isValidDateFor19(start_time)) {
						String temMessage = model.getError_message();
						if(!"".equals(temMessage)) {
							temMessage=temMessage+",";
						}
						model.setError_message(temMessage+"存在开始日期格式错误");
						model.setProduct_code(product_code);
						model.setFlag(false);
						timeFlag1=false;
					
					}
					if(end_time.contains("-")&&end_time.contains("/")||!DateUtil.isValidDateFor19(end_time)) {
						String temMessage = model.getError_message();
						if(!"".equals(temMessage)) {
							temMessage=temMessage+",";
						}
						model.setError_message(temMessage+"存在结束日期格式错误");
						model.setProduct_code(product_code);
						model.setFlag(false);
						timeFlag2=false;
					
					}
					
					if(end_time.compareTo(start_time) <= 0&&timeFlag1&&timeFlag2) {
						model.setProduct_code(product_code);
						String temMessage = model.getError_message();
						if(!"".equals(temMessage)) {
							temMessage=temMessage+",";
						}
						model.setError_message(temMessage+"开始日期大于等于结束日期错误");
						model.setFlag(false);
						timeFlag1=false;
						
						
					}
					if (end_time.compareTo(currentTime) <= 0&&timeFlag2) {
						model.setProduct_code(product_code);
						String temMessage = model.getError_message();
						if(!"".equals(temMessage)) {
							temMessage=temMessage+",";
						}
						model.setError_message(temMessage+"存在结束日期小于当前日期错误");
						timeFlag2=false;
						model.setFlag(false);
						
					}
				}
					//获取位置信息
					if(null == row.getCell(3) || row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK) {
						model.setProduct_code(product_code);
						String temMessage = model.getError_message();
						if(!"".equals(temMessage)) {
							temMessage=temMessage+",";
						}
						model.setError_message(temMessage+"存在位置为空错误");
						model.setFlag(false);
						
						
					}
					else {
						if(row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING) {
							position = row.getCell(3).getStringCellValue().trim();
						} else if(row.getCell(3).getCellType()==Cell.CELL_TYPE_NUMERIC) {
							Double a = row.getCell(3).getNumericCellValue();
							position = new DecimalFormat("#").format(a).trim(); 
						}
						
					}
					
					 if(timeFlag1&&timeFlag2) {
						//同一个时间段内位置不能重复--与导入的数据进行对比
							temMap.put(position, start_time+"="+end_time);
							int resultNum = checkTableDateData(listMap,temMap);
		                    if(resultNum==0) {
		                    	model.setProduct_code(product_code);
		                    	String temMessage = model.getError_message();
								if(!"".equals(temMessage)) {
									temMessage=temMessage+",";
								}
								model.setError_message(temMessage+"导入数据中存在相同位置时间重叠错误");
								model.setFlag(false);
								timeFlag1=false;
								timeFlag2=false;
								
		                    }
		                    else {
		                    	listMap.add(temMap);
		                    }
							//最后进行同一个时间段内位置不能重复--与数据库中的数据进行对比的校验
							if (timeFlag1&&timeFlag2&&0 < this.checkTimeRepeat(columnCode,position,start_time,end_time,null)){
								model.setProduct_code(product_code);
								String temMessage = model.getError_message();
								if(!"".equals(temMessage)) {
									temMessage=temMessage+",";
								}
								model.setError_message(temMessage+"同一位置存在与库中时间重叠错误");
								model.setFlag(false);
								timeFlag1=false;
								timeFlag2=false;
								
							}		
					 }
					 
				       //广告语
				       if(null != row.getCell(4) && row.getCell(4).getCellType() != Cell.CELL_TYPE_BLANK) {
				    	   
				    		if(row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING) {
								video_ad = row.getCell(4).getStringCellValue().trim();
							} else if(row.getCell(4).getCellType()==Cell.CELL_TYPE_NUMERIC) {
								Double a = row.getCell(4).getNumericCellValue();
								video_ad = new DecimalFormat("#").format(a).trim(); 
							}
				    		
							model.setVideo_ad(video_ad);

						}
				       
				       //封面商品视频连接
				       if(null != row.getCell(5) && row.getCell(5).getCellType() != Cell.CELL_TYPE_BLANK) {
				    		if(row.getCell(5).getCellType() == Cell.CELL_TYPE_STRING) {
								video_link = row.getCell(5).getStringCellValue().trim();
							} else if(row.getCell(5).getCellType()==Cell.CELL_TYPE_NUMERIC) {
								Double a = row.getCell(5).getNumericCellValue();
								video_link = new DecimalFormat("#").format(a).trim(); 
							}
				    		
							model.setVideo_link(video_link);
					 
			            }  
				       
						//時間做一下处理，没有时分秒给他拼接上当前时间的时分秒
				       if(model.isFlag()) {
				    	   if(start_time.trim().length()==10) {
				    		   start_time=currentTime.replaceAll(currentTime.substring(0,10), start_time.trim());
				    	   }
				    	   if(end_time.trim().length()==10) {
				    		   end_time =currentTime.replaceAll(currentTime.substring(0,10), end_time.trim());
				    	   }
				    	  
				    		model.setStart_time(start_time);
							model.setEnd_time(end_time);
							model.setPosition(position);
							model.setProduct_code(product_code);
					        resultModel.add(model);
				       }
				       else {
					        resultModel.add(model);
					        break;
					       } 

				       }
				
				else {
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
	private int checkTimeRepeat(String columnCode,String position,String startTime,String endTime,String floorModel){
		String sWhere =" column_code='"+columnCode+"' and position='"+position+"' and (('"+startTime+"' between start_time and end_time) or ('"+endTime+"' between start_time and end_time) or (start_time between '"+startTime+"' and '"+endTime+"') or (end_time between '"+startTime+"' and '"+endTime+"')) and is_delete='449746250002'";
		if (StringUtils.isNotEmpty(floorModel)){
			sWhere =sWhere+" and floor_model='"+floorModel+"'";
		}
		MDataMap map = DbUp.upTable("fh_apphome_column_content").oneWhere("uid", "", sWhere);
		if (null == map || map.isEmpty()) {
			return 0;
		}
		return 1;
	}
	private int checkTableDateData(List<Map<String,Object>> list,Map map){
         if(list!=null&&list.size()>0) {
        	 Set<Map.Entry<String,Object>> entrySet = map.entrySet();
        	 Iterator<Map.Entry<String, Object>> it= entrySet.iterator();
        	 String key = "";
        	 String value = "";
        	 if(it.hasNext()) {
        		 Map.Entry<String, Object> me = it.next();
        		 key = me.getKey();
        		 value = me.getValue().toString();
        	 }
        	 for (Map temMap : list) {
        		 if(temMap.containsKey(key)) {
        			 String temMapValue = temMap.get(key).toString();
            		 String[] temMapSt = temMapValue.split("=");
            		 String[] mapSt = value.split("=");
            		 if(temMapSt[0].compareTo(mapSt[0]) <= 0&&temMapSt[1].compareTo(mapSt[0]) > 0||
            				 temMapSt[0].compareTo(mapSt[0]) > 0&&temMapSt[0].compareTo(mapSt[1])<0) {
            			 return 0;
            		 }
        		 }
        		 
			}
         }
         return 1;
	}
	
	
public static class ImportModel {

		private String product_code = "";
		
		private String start_time = "";
		
		private String end_time ="";
		
		private String position = "";

		private String video_ad = "";
		
		private String video_link = "";
		
		private String product_name = "";
		
		private String product_price = "0.00";
		
		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getProduct_price() {
			return product_price;
		}

		public void setProduct_price(String product_price) {
			this.product_price = product_price;
		}

			
		public String getVideo_ad() {
			return video_ad;
		}

		public void setVideo_ad(String video_ad) {
			this.video_ad = video_ad;
		}

		public String getVideo_link() {
			return video_link;
		}

		public void setVideo_link(String video_link) {
			this.video_link = video_link;
		}

		public String getProduct_code() {
			return product_code;
		}

		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}

		public String getStart_time() {
			return start_time;
		}

		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}

		public String getEnd_time() {
			return end_time;
		}

		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
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

	private boolean isEmpty(Object obj) {
		return null == obj || obj.toString().equals("");
	}
	
}
