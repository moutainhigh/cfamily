package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.result.ApiForFriendLinksResult;
import com.cmall.familyhas.api.result.ApiForFriendLinksResult.Mess2;
import com.cmall.familyhas.util.DateUtilA;
import com.cmall.familyhas.util.MessageStatusUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/***
 * 家有汇首页底部的友情连接
 * @author jlin
 *
 */
public class ApiForPcFriendLinks extends RootApiForManage<ApiForFriendLinksResult, RootInput> {

	public ApiForFriendLinksResult Process(RootInput inputParam, MDataMap mRequestMap) {
		ApiForFriendLinksResult re = new ApiForFriendLinksResult();
		
		
		String sql="SELECT a.category_code,a.parent_code,a.category_name,a.sort,b.`status`,b.mess_category,b.flag_out_link,b.flag_show_time,b.show_time,b.out_link,b.create_time from " +
				"(SELECT category_code,parent_code,category_name,sort from hp_message_category where parent_code='449716040011' and manage_code=:manage_code) a " +
				"inner JOIN (SELECT `status`,mess_category,flag_out_link,flag_show_time,show_time,out_link,create_time from hp_message where manage_code=:manage_code) b on a.category_code=b.mess_category ORDER BY a.sort";
		
		//查询出所有的相关的信息
		List<Map<String, Object>> list=DbUp.upTable("hp_message_category").dataSqlList(sql, new MDataMap("manage_code",getManageCode()));
		
		if(list!=null&&list.size()>0){
			
			String yes="449746250001";
			Map<String, Mess2> datamap=new LinkedHashMap<String, Mess2>(list.size());
			Map<String, String> datamap_time=new HashMap<String, String>();
			
			for (Map<String, Object> map : list) {
				
				String category_code=(String)map.get("category_code");
//				String parent_code=(String)map.get("parent_code");
				String category_name=(String)map.get("category_name");
//				String sort=(String)map.get("sort");
				String status=(String)map.get("status");
				String flag_show_time=(String)map.get("flag_show_time");
				String show_time=(String)map.get("show_time");
				String flag_out_link=(String)map.get("flag_out_link");
				String out_link=(String)map.get("out_link");
				String create_time=(String)map.get("create_time");
				
				
				if(!MessageStatusUtil.status(yes, status, flag_show_time, show_time)){//如果状态可用，则添加到列表
					continue;
				}
				
				
				Mess2 mess2 = new Mess2();
				mess2.setCategory_note(category_name);
				mess2.setDetail_url(new MessageStatusUtil().getMesscUrl(yes, flag_out_link, out_link, category_code, category_name));
				
				//此处数据存储map 过滤数据 
				Mess2 mess2_tmp =datamap.get(category_code);
				if(mess2_tmp==null){
					datamap.put(category_code, mess2);
					datamap_time.put(category_code, create_time);
				}else{
					//比对一下创建时间,取创建时间最大的
					String mess2_tmp_time=datamap_time.get(category_code);
					if(DateUtilA.compare(mess2_tmp_time, create_time)<0){
						datamap.put(category_code, mess2);
					}
				}
				
			}
			
			for (Map.Entry<String, Mess2> map : datamap.entrySet()) {
				
				re.getMessList().add(map.getValue());
				
			}
			
			
		}
		
		return re;
	}

}
