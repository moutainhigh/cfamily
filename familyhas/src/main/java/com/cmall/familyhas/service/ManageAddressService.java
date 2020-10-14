package com.cmall.familyhas.service;

import java.util.Map;
import java.util.UUID;

import com.cmall.familyhas.api.input.AddressInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootResultWeb;


/** 
* @ClassName: ManageAddressService 
* @Description: 管理收货地址
* @author 张海生
* @date 2015-7-2 上午11:43:56 
*  
*/
public class ManageAddressService extends BaseClass {
	
	/** 
	* @Description:保存用户地址
	* @param input 地址实体
	* @param manageCode 平台编号
	* @param memberCode 用户code
	* @author 张海生
	* @date 2015-7-2 下午2:57:24
	* @return RootResultWeb 
	* @throws 
	*/
	public RootResultWeb saveAddress(AddressInput input, String manageCode, String memberCode){
		RootResultWeb result = new RootResultWeb();
		MDataMap updateMap = new MDataMap(); 
		String areaCode = input.getArea_code();
		//updateMap.put("address_id", WebHelper.upCode("DZ"));
		updateMap.put("address_name", input.getReceive_person());
		updateMap.put("area_code", areaCode);
		updateMap.put("address_street", input.getAddress());
		updateMap.put("address_postalcode", input.getPostcode());
		updateMap.put("address_mobile", input.getMobilephone());
		updateMap.put("address_default", input.getFlag_default());
		updateMap.put("address_code", memberCode);
		updateMap.put("app_code", manageCode);
		/* address_default  1:默认    0：不默认
		 * 修改：如果是默认地址，修改后变为不默认 。  添加：如果添加的一条是默认地址，把原先已有的默认地址设为不默认
		 */
		int count = DbUp.upTable("nc_address").dataCount("address_code=:address_code and app_code=:app_code", updateMap);
		if(count>20){
			result.inErrorMessage(916421254);//收货地址不能超过20个
		}else{
			//获取省市区的汉子
			if(!"".equals(areaCode)){
				String area_sql="SELECT a.`code` as area_code,c.`name` as prov_name,b.`name` as city_name,a.`name` as area_name,b.`code` as city_code,c.`code` as prov_code from sc_tmp a  LEFT JOIN sc_tmp b on b.`code`=CONCAT(LEFT(a.`code`,4),'00') LEFT JOIN sc_tmp c on c.`code`=CONCAT(LEFT(a.`code`,2),'0000') where RIGHT(a.`code`,2)<>'00' AND a.`code`=:area_code";
				Map<String, Object> areaMap=DbUp.upTable("sc_tmp").dataSqlOne(area_sql, new MDataMap("area_code",areaCode));
				if(areaMap!=null && !"".equals(areaMap) && areaMap.size()>0){
					updateMap.put("address_province", (String)areaMap.get("prov_name")+(String)areaMap.get("city_name")+(String)areaMap.get("area_name"));
//					updateMap.put("address_city", "");
//					updateMap.put("address_county", "");
				}
			}
			if("1".equals(input.getFlag_default())){
				DbUp.upTable("nc_address").dataUpdate(new MDataMap("address_default","0","address_code", memberCode), "address_default", "address_code");
			}
			if(count==0){
				updateMap.put("address_default", "1");
			}
			updateMap.put("address_id", WebHelper.upCode("DZ"));
			updateMap.put("create_time", DateUtil.getNowTime());
			updateMap.put("update_time", DateUtil.getNowTime());
			DbUp.upTable("nc_address").dataInsert(updateMap);//保存收货地址
			
			// 成功保存返回地址编号
			result.setResultMessage(updateMap.get("address_id"));
		}
		return result;
	}

}
