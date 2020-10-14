package com.cmall.familyhas.api;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiHomeColumnInput;
import com.cmall.familyhas.api.model.HomeColumn;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.model.HomeColumnPc;
import com.cmall.familyhas.api.result.ApiHomeColumnPcResult;
import com.cmall.familyhas.api.result.ApiHomeColumnResult;
import com.cmall.familyhas.model.TopThreeColumn;
import com.cmall.familyhas.service.HomeColumnService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * 惠家有首页版式栏目(PC端调用)
 * 
 * @author zhaoxq
 * 
 */
public class ApiPcHomeColumn extends
		RootApiForMember<ApiHomeColumnPcResult, ApiHomeColumnInput> implements Cloneable{

	public ApiHomeColumnPcResult Process(ApiHomeColumnInput inputParam,
			MDataMap mRequestMap) {

		ApiHomeColumnPcResult re = new ApiHomeColumnPcResult();
		ApiHomeColumn api = new ApiHomeColumn();
		api.checkAndInit(inputParam, mRequestMap);
		
		//调用APP商品详情接口
		ApiHomeColumnResult result = api.Process(inputParam, mRequestMap);
		clone(result, re);
		List<HomeColumnPc> columnList = re.getColumnList();
		for(int i = 0;i<columnList.size();i++){
			HomeColumnPc column = (HomeColumnPc)columnList.get(i);
			if("4497471600010015".equals(column.getColumnType())){
				List<HomeColumnContent> contentList = column.getContentList();
				for(int j= 0;j<contentList.size();j++){
					HomeColumnContent content = contentList.get(j);
					String floorModel = content.getFloorModel();
					//底部广告位
					if("4497471600220003".equals(floorModel)){
						column.getAd3List().add(content);
					//中间轮播广告位
					}else if("4497471600220002".equals(floorModel)){
						column.getAd1List().add(content);
					//左侧底部品牌位
					}else if("4497471600220004".equals(floorModel)){
						column.getLogoList().add(content);
					//左侧广告位
					}else if("4497471600220001".equals(floorModel)){
						column.setAd2linktype(content.getShowmoreLinktype());
						column.setAd2linkvalue(content.getShowmoreLinkvalue());
						column.setAd2picture(content.getPicture());
					//右侧热销商品位
					}else if("4497471600220005".equals(floorModel)){
						column.getHotPointList().add(content);
					}
				}
				column.getContentList().clear();
				
			}
		}
		return re;
	}

	private void clone(ApiHomeColumnResult a,ApiHomeColumnPcResult b) {
		
		TopThreeColumn topThreeColumn = a.getTopThreeColumn();
		TopThreeColumn pcTopThreeColumn = new TopThreeColumn();
		b.setTopThreeColumn(topThreeColumn);
		
		List<HomeColumn> topThreeColumnList = topThreeColumn.getTopThreeColumnList();
		List<HomeColumn> pcTopThreeColumnList = new ArrayList<HomeColumn>();
		List<HomeColumn> columnList = a.getColumnList();
		b.setSysTime(a.getSysTime());
		for(HomeColumn homeColumn:topThreeColumnList){
			//Class<?> type = homeColumn.getClass();
			HomeColumn topColumn = new HomeColumn();
			//cloneHomeColumn(type,topColumn,homeColumn);
			try {
				BeanUtils.copyProperties(topColumn, homeColumn);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} 
			
			pcTopThreeColumnList.add(topColumn);
		}
		pcTopThreeColumn.setTopThreeColumnList(pcTopThreeColumnList);
		b.setTopThreeColumn(pcTopThreeColumn);
		
		for(HomeColumn homeColumn:columnList){
			//Class<?> type = homeColumn.getClass();
			HomeColumnPc homeColumnPc = new HomeColumnPc();
			//cloneHomeColumn(type,homeColumnPc,homeColumn);
			try {
				BeanUtils.copyProperties(homeColumnPc, homeColumn);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} 
			b.getColumnList().add(homeColumnPc);
		}
	}
	
	private void cloneHomeColumn(Class<?> type,HomeColumn topColumn,HomeColumn homeColumn){
		try {
			do {
				
				for (Field f : type.getDeclaredFields()) {
					String fieldName = f.getName();
					String firstCharUpper =  fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					String getFieldName = "get" + firstCharUpper;
					String setFieldName = "set" + firstCharUpper;
					Object data = type.getMethod(getFieldName).invoke(homeColumn);					
					if((data instanceof Integer)){
						type.getMethod(setFieldName,Integer.TYPE).invoke(topColumn,data);
					}else if((data instanceof List)){
						type.getMethod(setFieldName,List.class).invoke(topColumn,data);
					}else if((data instanceof Map)){
						type.getMethod(setFieldName,Map.class).invoke(topColumn,data);
					}else{
						type.getMethod(setFieldName,data.getClass()).invoke(topColumn,data);
					}							
				}
				type = type.getSuperclass();
				
			} while (null != type);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
