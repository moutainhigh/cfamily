package com.cmall.familyhas.webfunc;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.cmall.systemcenter.service.FlowService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 商品价格审批，导入文件批量审核
 */
public class FuncBatchSkuPriceFlow extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String pageCode = mDataMap.get("zw_f_uid");
		String uploadFileUrl = mDataMap.get("uploadFile");
		if(StringUtils.isBlank(uploadFileUrl)){
			mResult.setResultCode(0);
			mResult.setResultMessage("未找到上传的文件");
			return mResult;
		}
		
		if(!ArrayUtils.contains(new String[]{"page_chart_v_sc_flow_mian_skuprice_cw","page_chart_v_sc_flow_mian_skuprice_yy"}, pageCode)){
			mResult.setResultCode(0);
			mResult.setResultMessage("不支持的审核流程");
			return mResult;
		}
		
		List<String[]> uploadDataList = null ; 
		try {
			uploadDataList = parseUploadDataList(uploadFileUrl);
		} catch (Exception e) {
			mResult.setResultCode(0);
			mResult.setResultMessage("解析文件失败: "+e);
			return mResult;
		}
		
		Map<String, List<String[]>> groupDataList = groupByFlowCode(uploadDataList);
		Map<String, String[]> prepareResultMap = new HashMap<String, String[]>();
		
		// 财务审核通过
		if("page_chart_v_sc_flow_mian_skuprice_cw".equalsIgnoreCase(pageCode)){
			prepareResultMap = prepare(groupDataList, "4497172300130001");
		}else if("page_chart_v_sc_flow_mian_skuprice_yy".equalsIgnoreCase(pageCode)){
			prepareResultMap = prepare(groupDataList, "4497172300130004");
		}
		
		int pass = 0,reject = 0,ignore = 0,error = 0;
		
		FlowService fs = new FlowService();
		MUserInfo userInfo = UserFactory.INSTANCE.create();
		String roleCode = StringUtils.defaultIfBlank(userInfo.getUserRole(), WebConst.CONST_WEB_EMPTY);
		
		// 审核流程
		Set<Entry<String, List<String[]>>> entrySet = groupDataList.entrySet();
		String[] prepareResult;
		RootResult result = null;
		for(Entry<String, List<String[]>> entry : entrySet){
			result = null;
			prepareResult = prepareResultMap.get(entry.getKey());
			
			// 忽略
			if("I".equalsIgnoreCase(prepareResult[0])){
				ignore += entry.getValue().size();
			}
			
			// 审核通过
			if("Y".equalsIgnoreCase(prepareResult[0])){
				
				// 财务审核通过
				if("page_chart_v_sc_flow_mian_skuprice_cw".equalsIgnoreCase(pageCode)){
					result = fs.ChangeFlow(entry.getKey(), "4497172300130001", "4497172300130002", userInfo.getUserCode(),roleCode,"同意",
							new MDataMap("to_status","4497172300130002","flow_code",entry.getKey(),"current_status","4497172300130001","remark","同意"));
				}
				
				// 运营审核通过
				if("page_chart_v_sc_flow_mian_skuprice_yy".equalsIgnoreCase(pageCode)){
					result = fs.ChangeFlow(entry.getKey(), "4497172300130004", "4497172300130001", userInfo.getUserCode(),roleCode,"同意",
							new MDataMap("to_status","4497172300130001","flow_code",entry.getKey(),"current_status","4497172300130004","remark","同意"));
				//批量导入添加自动审核
					this.autoJudgeSkipCW(entry.getKey(), "4497172300130004", "4497172300130001", userInfo.getUserCode(),roleCode,"同意",
							new MDataMap("to_status","4497172300130001","flow_code",entry.getKey(),"current_status","4497172300130004","remark","同意"));
				}
				
				if(result != null && result.getResultCode() == 1){
					pass += entry.getValue().size();
				}else{
					error++;
				}
			}
			
			// 审核驳回
			if("N".equalsIgnoreCase(prepareResult[0])){
				
				// 财务审核驳回
				if("page_chart_v_sc_flow_mian_skuprice_cw".equalsIgnoreCase(pageCode)){
					result= fs.ChangeFlow(entry.getKey(), "4497172300130001", "4497172300130003", userInfo.getUserCode(),roleCode,prepareResult[1],
							new MDataMap("to_status","4497172300130002","flow_code",entry.getKey(),"current_status","4497172300130001","remark",prepareResult[1]));
				}
				
				// 运营审核驳回
				if("page_chart_v_sc_flow_mian_skuprice_yy".equalsIgnoreCase(pageCode)){
					result = fs.ChangeFlow(entry.getKey(), "4497172300130004", "4497172300130005", userInfo.getUserCode(),roleCode,prepareResult[1],
							new MDataMap("to_status","4497172300130005","flow_code",entry.getKey(),"current_status","4497172300130004","remark",prepareResult[1]));
				}
				
				if(result != null && result.getResultCode() == 1){
					reject += entry.getValue().size();
				}else{
					error++;
				}
			}
		}
		
		mResult.setResultMessage("[审核完成] 通过： "+pass+", 驳回： "+reject+", 忽略: "+ignore+", 失败： "+error);
		return mResult;
	}
	
	
	private void autoJudgeSkipCW(String fc, String fromStatus, String toStatus, String userCode, String roleCode,
			String remark, MDataMap mSubMap) {
		// TODO Auto-generated method stub
		//方案一：创建一个虚拟系统用户并赋予财务审核权限   
		//方案二：给运营赋予临时财务审核权限，然后对这个操作流程做一个系统审核标记
		//测试/灰度
//          if(!roleCode.contains("4677031800010005"))
//        {
//        	roleCode = roleCode+"|4677031800010005";
//        }
		//生产
	    if(!roleCode.contains("4677031800010021"))
        {
        	roleCode = roleCode+"|4677031800010021";
        }
		
		FlowService fs = new FlowService();
		if("4497172300130004".equals(fromStatus)&&"4497172300130001".equals(toStatus)) {
			List<Map<String,Object>> listMap =DbUp.upTable("pc_skuprice_change_flow").dataSqlList("select * from pc_skuprice_change_flow  where  flow_code = :flow_code ", new MDataMap("flow_code",fc));
			if(listMap!=null) {
				boolean flag = true;
				for (Map<String, Object> map : listMap) {
					BigDecimal cost_price = new BigDecimal(map.get("cost_price").toString());
					BigDecimal sell_price = new BigDecimal(map.get("sell_price").toString());
					//float pencent =(sell_price.floatValue()-cost_price.floatValue())/sell_price.floatValue();
					BigDecimal pencent = sell_price.subtract(cost_price).divide(sell_price,5,RoundingMode.HALF_EVEN);
					if(pencent.compareTo(new BigDecimal(bConfig("familyhas.autoShenHeRate")))==-1) {
						flag = false;
						break;
					}
				}
				if(flag) {
					fs.ChangeFlow(fc, "4497172300130001", "4497172300130002", userCode,roleCode,"系统自动审核通过",mSubMap);
					//对这条操作记录进行自动标记 操作表：sc_flow_history   pc_skuprice_change_flow
					MDataMap md = new MDataMap();
					md.put("flow_code", fc);
					md.put("current_status", "4497172300130002");
					md.put("status", "4497172300130002");
					md.put("auto_flag", "1");
					DbUp.upTable("sc_flow_history").dataUpdate(md, "auto_flag", "flow_code,current_status");
					DbUp.upTable("pc_skuprice_change_flow").dataUpdate(md, "auto_flag", "flow_code,status");
				}
			}
			
		}

	}
	
	/**
	 * 解析上传的数据
	 */
	private List<String[]> parseUploadDataList(String url) throws Exception{
		InputStream in = null;
		List<String[]> dataList = null; 
		try {
			in = new URL(url).openStream();
			Workbook wb = new HSSFWorkbook(in);
			Sheet sheet = wb.getSheetAt(0);
			int start = sheet.getFirstRowNum();
			int end = sheet.getLastRowNum();
			dataList = new ArrayList<String[]>((int)(end/0.75+1));
			
			Row row;
			String[] cols = null;
			while(start < end){
				start++; // 忽略第一行标题
				
				cols = new String[14];
				row = sheet.getRow(start);
				
				// 流程编号
				cols[0] = getCellValue(row.getCell(0,Row.RETURN_NULL_AND_BLANK));
				// 商品编号
				cols[1] = getCellValue(row.getCell(1,Row.RETURN_NULL_AND_BLANK));
				// SKU编号
				cols[2] = getCellValue(row.getCell(2,Row.RETURN_NULL_AND_BLANK));
				// SKU名称
				cols[3] = getCellValue(row.getCell(3,Row.RETURN_NULL_AND_BLANK));
				// 原成本价
				cols[4] = getCellValue(row.getCell(4,Row.RETURN_NULL_AND_BLANK));
				// 变更后成本价
				cols[5] = getCellValue(row.getCell(5,Row.RETURN_NULL_AND_BLANK));
				// 原销售价
				cols[6] = getCellValue(row.getCell(6,Row.RETURN_NULL_AND_BLANK));
				// 变更后销售价
				cols[7] = getCellValue(row.getCell(7,Row.RETURN_NULL_AND_BLANK));
				// 开始日期
				cols[8] = getCellValue(row.getCell(8,Row.RETURN_NULL_AND_BLANK));
				// 结束日期
				cols[9] = getCellValue(row.getCell(9,Row.RETURN_NULL_AND_BLANK));
				// 原毛利率
				cols[10] = getCellValue(row.getCell(10,Row.RETURN_NULL_AND_BLANK));
				// 变更后毛利率
				cols[11] = getCellValue(row.getCell(11,Row.RETURN_NULL_AND_BLANK));
				// 审核
				cols[12] = getCellValue(row.getCell(12,Row.RETURN_NULL_AND_BLANK));
				// 审批意见
				cols[13] = getCellValue(row.getCell(13,Row.RETURN_NULL_AND_BLANK));
				
				// 忽略流程编号为空的
				if(StringUtils.isBlank(cols[0])){
					continue;
				}
				
				dataList.add(cols);
			}
			return dataList;
		} catch (Exception e) {
			throw e;
		} finally {
			if(in != null){
				IOUtils.closeQuietly(in);
			}
		}
	}
	
	/**
	 * 根据流程编码对数据分组
	 */
	private Map<String, List<String[]>> groupByFlowCode(List<String[]> uploadDataList){
		Map<String, List<String[]>> groupList = new HashMap<String, List<String[]>>();
		for(String[] data : uploadDataList){
			if(StringUtils.isBlank(data[0])) continue; // 流程编码为空则默认数据无效
			if(!groupList.containsKey(data[0])){
				groupList.put(data[0], new ArrayList<String[]>());
			}
			
			groupList.get(data[0]).add(data);
		}
		return groupList;
	}
	
	/**
	 * 预处理，解析出各流程的处理最终结果
	 * {flowcode : [status, remark]}
	 */
	private Map<String,String[]> prepare(Map<String, List<String[]>> groupDataList, String status){
		Map<String,String[]> prepareResult = new HashMap<String, String[]>();
		Set<Entry<String, List<String[]>>> entrySet = groupDataList.entrySet();
		
		List<String[]> listValue;  // 待审批明细
		Set<String> remark; // 审批意见
		int flag = 0; // 审批结果  0 忽略  01 通过  10 拒绝
		
		Map<String,String[]> originalFlowData;
		for(Entry<String, List<String[]>> entry : entrySet){
			listValue = entry.getValue();
			remark = new HashSet<String>();
			flag = 0;
			originalFlowData = getOriginalFlowData(entry.getKey());
			
			for(String[] vs : listValue){
				// 任何一个明细的审批结果为空则忽略对应的流程
				// 任何一个明细的审批结果不是 Y 或 N 则忽略对应的流程
				if(StringUtils.isBlank(vs[12]) || (!"Y".equalsIgnoreCase(vs[12]) && !"N".equalsIgnoreCase(vs[12]))){
					flag = 0;
					break;
				}
				
				// SKU的个数对不上则忽略流程
				if(listValue.size() != originalFlowData.size()){
					flag = 0;
					break;
				}
				
				// 检查状态，不是当前处理的流程状态则直接忽略
				if(!status.equalsIgnoreCase(originalFlowData.get(vs[1]+vs[2])[6])){
					flag = 0;
					break;
				}
				
				// 未查询到原始数据忽略此次的流程
				if(originalFlowData == null || originalFlowData.get(vs[1]+vs[2]) == null){
					flag = 0;
					break;
				}
				
				// 修改了价格或日期也忽略此次的流程
				if(!compareNumber(originalFlowData.get(vs[1]+vs[2])[0],vs[4])
						|| !compareNumber(originalFlowData.get(vs[1]+vs[2])[1],vs[5])
						|| !compareNumber(originalFlowData.get(vs[1]+vs[2])[2],vs[6])
						|| !compareNumber(originalFlowData.get(vs[1]+vs[2])[3],vs[7])
						|| !StringUtils.equalsIgnoreCase(originalFlowData.get(vs[1]+vs[2])[4],vs[10])
						|| !StringUtils.equalsIgnoreCase(originalFlowData.get(vs[1]+vs[2])[5],vs[11])){
					flag = 0;
					break;
				}
				
				if("Y".equalsIgnoreCase(vs[12])){
					flag = flag | 0x01;  // 通过标识
				}else{
					flag = flag | 0x10;  // 驳回标识
				}
				
				if(StringUtils.isNotBlank(vs[13])){
					remark.add(vs[13]);
				}
			}
			
			if(flag == 0){ // 流程忽略
				prepareResult.put(entry.getKey(), new String[]{"I",StringUtils.join(remark,";")});
			}else if(flag == 0x01){ // 流程通过
				prepareResult.put(entry.getKey(), new String[]{"Y",StringUtils.join(remark,";")});
			}else{ // 流程驳回
				prepareResult.put(entry.getKey(), new String[]{"N",StringUtils.join(remark,";")});
			}
		}
		
		return prepareResult;
	}
	
	/**
	 * 单个流程的明细数据
	 * {product_code+product_code : [cost_price_old,cost_price,sell_price_old,sell_price,start_time,end_time]}
	 */
	private Map<String,String[]> getOriginalFlowData(String flowCode){
		String sql = "select product_code, sku_code, cost_price_old, cost_price, sell_price_old, sell_price, start_time, end_time,status from pc_skuprice_change_flow where flow_code = :flow_code";
		List<Map<String, Object>> dataList = DbUp.upTable("pc_skuprice_change_flow").dataSqlList(sql, new MDataMap("flow_code", flowCode));
		
		Map<String,String[]> mapData = new HashMap<String, String[]>();
		for(Map<String, Object> map : dataList){
			mapData.put(""+map.get("product_code")+map.get("sku_code"), new String[]{
				map.get("cost_price_old").toString(),
				map.get("cost_price").toString(),
				map.get("sell_price_old").toString(),
				map.get("sell_price").toString(),
				map.get("start_time").toString(),
				map.get("end_time").toString(),
				map.get("status").toString(),
			});
		}
		return mapData;
	}
	
	private String getCellValue(Cell cell){
		if(cell == null) return "";
		cell.setCellType(Cell.CELL_TYPE_STRING);
		return StringUtils.trimToEmpty(cell.getStringCellValue());
	}
	
	private boolean compareNumber(String v1, String v2){
		if(v1 == null || v2 == null) return false;
		return new BigDecimal(v1).compareTo(new BigDecimal(v2)) == 0;
	}
}
