package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForGetStoreDistrictInput;
import com.cmall.familyhas.api.result.ApiForGetStoreDistrictResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/***
 * 查询仓库覆盖的区域
 * @author jlin
 *
 */
public class ApiForGetStoreDistrict extends RootApi<ApiForGetStoreDistrictResult, ApiForGetStoreDistrictInput> {

	public ApiForGetStoreDistrictResult Process(ApiForGetStoreDistrictInput inputParam,MDataMap mRequestMap) {
		ApiForGetStoreDistrictResult result=new ApiForGetStoreDistrictResult();
		
		//倒叙的形式查询，减少对数据库的请求
		//查询省市区域 表
		List<Map<String, Object>> pcgovList= DbUp.upTable("sc_tmp").dataSqlList(" SELECT code,name from sc_tmp  where `code` LIKE '%00' and code_lvl <> 4 ORDER BY `code` ", null);//查出所有的省市信息
		List<Map<String, Object>> govList= DbUp.upTable("sc_tmp").dataSqlList("SELECT DISTINCT g.`code`,g.`name`  from sc_store_district d, sc_tmp g where d.district_code=g.`code` and "
				+ "g.use_yn = 'Y' and d.express_type in ('10','30') ORDER BY g.`code` ", null);
		
		Map<String, String> govMap=new LinkedHashMap<String, String>();
		
		//修改数据结构,并起到去重的作用
		for (Map<String, Object> map : pcgovList) {
			String code = (String)map.get("code");
			String name = (String)map.get("name");
			if(code==null||name==null){
				continue;
			}
			govMap.put(code, name);
		}
		
		for (Map<String, Object> map : govList) {
			String code = (String)map.get("code");
			String name = (String)map.get("name");
			if(code==null||name==null){
				continue;
			}
			govMap.put(code, name);
		}
		
		//区域规律 ：code 共6位， 后四位为0000 为一级省市， 后两位为00为二级市县，其他为区域
		Map<String,ApiForGetStoreDistrictResult.Province> provinceMap=new LinkedHashMap<String,ApiForGetStoreDistrictResult.Province>();
		
		for (Map.Entry<String, String> map : govMap.entrySet()) {
			String code = map.getKey();
			String name = map.getValue();
			
			if(!"00".equals(code.substring(4))){ //倒叙 只操作区域信息
				String pcode=code.substring(0,2)+"0000";//省代码
				String ccode=code.substring(0,4)+"00";//市代码
				
				if(provinceMap.containsKey(pcode)){
					ApiForGetStoreDistrictResult.Province province = provinceMap.get(pcode);
					List<ApiForGetStoreDistrictResult.City> cityList =province.getCityList();
					boolean bcflag=true;
					for (ApiForGetStoreDistrictResult.City city : cityList) {
						if(city.getCityID().equals(ccode)){
							List<ApiForGetStoreDistrictResult.District> districtList = city.getDistrictList();
							
							ApiForGetStoreDistrictResult.District district=new ApiForGetStoreDistrictResult.District();
							district.setDistrictID(code);
							district.setDistrict(name);
							districtList.add(district);
							
							bcflag=false;
							break;
						}
					}
					
					if(bcflag){ //没有找到上一级节点,创建上一级节点
						String cname = govMap.get(ccode);
						if(cname==null){
							continue;
						}
						
						ApiForGetStoreDistrictResult.City city=new ApiForGetStoreDistrictResult.City();
						city.setCityID(ccode);
						city.setCityName(cname);
						List<ApiForGetStoreDistrictResult.District> districtList = city.getDistrictList();
						
						ApiForGetStoreDistrictResult.District district=new ApiForGetStoreDistrictResult.District();
						district.setDistrictID(code);
						district.setDistrict(name);
						districtList.add(district);
						city.setDistrictList(districtList);
						
						List<ApiForGetStoreDistrictResult.City> cityList1=province.getCityList();
						cityList1.add(city);
					}
					
				}else{
					
					String pname = govMap.get(pcode);
					
					if(pname==null){
						continue;
					}
					
					ApiForGetStoreDistrictResult.Province province=new ApiForGetStoreDistrictResult.Province();
					province.setProvinceID(pcode);
					province.setProvinceName(pname);
					List<ApiForGetStoreDistrictResult.City> cityList1=province.getCityList();
					
					String cname = govMap.get(ccode);
					ApiForGetStoreDistrictResult.City city=new ApiForGetStoreDistrictResult.City();
					city.setCityID(ccode);
					city.setCityName(cname);
					List<ApiForGetStoreDistrictResult.District> districtList = city.getDistrictList();
					
					ApiForGetStoreDistrictResult.District district=new ApiForGetStoreDistrictResult.District();
					district.setDistrictID(code);
					district.setDistrict(name);
					districtList.add(district);
					city.setDistrictList(districtList);
					cityList1.add(city);
					
					provinceMap.put(pcode, province);
				}
			}
		}
		
		

		List<ApiForGetStoreDistrictResult.Province> provinceList=new ArrayList<ApiForGetStoreDistrictResult.Province>();
		
		for (Map.Entry<String,ApiForGetStoreDistrictResult.Province> map : provinceMap.entrySet()) {
			provinceList.add(map.getValue());
		}
		
		result.setList(provinceList);
		return result;
	}

}
