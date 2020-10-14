package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForBigWheelCheckPrizesInput;
import com.cmall.familyhas.api.model.DailyWinningInformation;
import com.cmall.familyhas.api.model.HuodongEventJl;
import com.cmall.familyhas.api.result.ApiForBigWheelCheckPrizesResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 大转盘查看我的奖品接口
 * @author lgx
 *
 */
public class ApiForBigWheelCheckPrizes extends RootApiForVersion<ApiForBigWheelCheckPrizesResult, ApiForBigWheelCheckPrizesInput> {

	
	public ApiForBigWheelCheckPrizesResult Process(ApiForBigWheelCheckPrizesInput inputParam, MDataMap mRequestMap) {
		ApiForBigWheelCheckPrizesResult result = new ApiForBigWheelCheckPrizesResult();
		List<DailyWinningInformation> list = new ArrayList<DailyWinningInformation>();
		List<HuodongEventJl> list2 = new ArrayList<HuodongEventJl>();
		
		String sql1 = "SELECT * FROM lc_huodong_event_jl WHERE member_code = '"+getOauthInfo().getUserCode()+"' AND jp_type in ('4497471600470001','4497471600470002','4497471600470003') ORDER BY zj_time DESC";
		List<Map<String, Object>> jlList = DbUp.upTable("lc_huodong_event_jl").dataSqlList(sql1, new MDataMap());
		if(jlList != null && jlList.size() > 0) {
			// 基础时间
			String base_time = "";
			for (int i = 0; i < jlList.size(); i++) {
				Map<String, Object> map = jlList.get(i);
				// 中奖信息
				HuodongEventJl huodongEventJl = new HuodongEventJl();
				
				String zj_time = jlList.get(i).get("zj_time").toString().substring(0, 10);
				
				if(base_time.equals(zj_time)) {
					// 时间相同,表明是同一天的抽奖记录,添加中奖信息
					huodongEventJl = saveInformation(map);
					list2.add(huodongEventJl);
				}else {
					// 时间不同,表明不是同一天,检验是否是第一次进入
					if(list2 != null && list2.size() > 0) {
						// 有中奖信息,添加每日中奖信息
						// 每日中奖信息
						DailyWinningInformation dailyWinningInformation = new DailyWinningInformation();
						dailyWinningInformation.setZjTime(base_time);
						dailyWinningInformation.setList(list2);
						list.add(dailyWinningInformation);
						// 添加完之后,更新基础时间,先清空中奖信息,再添加另一天的中奖信息
						base_time = zj_time;
						list2 = new ArrayList<HuodongEventJl>();
						huodongEventJl = saveInformation(map);
						list2.add(huodongEventJl);
					}else {
						// 第一次进入,更新基础时间,添加中奖信息
						base_time = zj_time;
						huodongEventJl = saveInformation(map);
						list2.add(huodongEventJl);
					}
				}
				// 保存最后一天的信息
				if(i == jlList.size()-1) {
					// 每日中奖信息
					DailyWinningInformation dailyWinningInformation1 = new DailyWinningInformation();
					dailyWinningInformation1.setZjTime(base_time);
					dailyWinningInformation1.setList(list2);
					list.add(dailyWinningInformation1);
				}
			}
			
			result.setList(list);
		}
		
		return result;
	}

	private HuodongEventJl saveInformation(Map<String, Object> map) {
		HuodongEventJl huodongEventJl = new HuodongEventJl();
		huodongEventJl.setJpCode(map.get("jp_code").toString());
		huodongEventJl.setJpCodeSeq(map.get("jp_code_seq").toString());
		huodongEventJl.setJpTitle(map.get("jp_title").toString());
		huodongEventJl.setJpType(map.get("jp_type").toString());
		huodongEventJl.setLjYn(map.get("lj_yn").toString());
		huodongEventJl.setZjTime(map.get("zj_time").toString());
		return huodongEventJl;
	}

}
