package com.cmall.familyhas.webfunc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncAddForChannelDetails extends RootFunc{

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		// 定义插入数据库
		MDataMap mInsertMap = new MDataMap();

		recheckMapField(mResult, mPage, mAddMaps);

		if (mResult.upFlagTrue()) {
			//判断时间是否合法
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long l = 0l;
			long l2 = 0l;
			String start_time = mAddMaps.get("start_time");
			String end_time = mAddMaps.get("end_time");
			Date currentDate = new Date();
			try {
				Date startDate = formatter.parse(start_time);
				Date endDate = formatter.parse(end_time);
				l2 = endDate.getTime() - currentDate.getTime();
				l = endDate.getTime() - startDate.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(l2<0){
				mResult.setResultMessage("结束时间小于当前时间，不合法，请重新填写");
				mResult.setResultCode(10000);
				return mResult;
			}
			if(l<0){
				mResult.setResultMessage("开始时间大于结束时间，不合法，请重新填写");
				mResult.setResultCode(10000);
				return mResult;
			}
			//判断积分、加价价格、库存是否为零
			Integer jf_cost = Integer.parseInt(mAddMaps.get("jf_cost"));
			if(jf_cost == 0){
				mResult.setResultMessage("积分不能为0");
				mResult.setResultCode(10000);
				return mResult;
			}
			String channel_uid = mAddMaps.get("channel_uid");
			MDataMap channelTypeF = DbUp.upTable("fh_apphome_channel").one("uid",channel_uid);
			String channelType = channelTypeF.get("channel_type");
			if("449748130001".equals(channelType)){//类型为加价的时候校验加价价格不能为空和0
				String extra_charges = mAddMaps.get("extra_charges");
				double extraC = Double.parseDouble(extra_charges);
				if(0 == extraC){
					mResult.setResultMessage("加价金额不能为0");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			Integer allow_count = Integer.parseInt(mAddMaps.get("allow_count"));
			if(allow_count == 0){
				mResult.setResultMessage("活动允许最大库存不能为0");
				mResult.setResultCode(10000);
				return mResult;
			}
			String join_up_end_time = mAddMaps.get("join_up_end_time");//活动报名截止时间
			long l3 = 0l;
			long l4 = 0l;
			if(!StringUtils.isEmpty(join_up_end_time)){//不为空时
				try {
					Date join_up = formatter.parse(join_up_end_time);
					l3 = join_up.getTime() - currentDate.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(l3<0){
					mResult.setResultMessage("活动报名时间小于当前时间，不合法，请重新填写");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			String activity_start_time = mAddMaps.get("activity_start_time");//活动开始时间
			if(!StringUtils.isEmpty(activity_start_time)){//不为空时
				try {
					Date activity_start = formatter.parse(activity_start_time);
					l4 = activity_start.getTime() - currentDate.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(l4<0){
					mResult.setResultMessage("活动举办时间小于当前时间，不合法，请重新填写");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			long l5 = 0l;
			if(!StringUtils.isEmpty(join_up_end_time)&&!StringUtils.isEmpty(activity_start_time)){
				try {
					Date activity_start = formatter.parse(activity_start_time);
					Date join_up = formatter.parse(join_up_end_time);
					l5 = join_up.getTime() - activity_start.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(l5>0){
					mResult.setResultMessage("活动举办时间小于报名截止时间，不合法，请重新填写");
					mResult.setResultCode(10000);
					return mResult;
				}
			}

			//判断登陆用户是否为空
			String loginname=UserFactory.INSTANCE.create().getLoginName();
			if(loginname==null||"".equals(loginname)){
				mResult.inErrorMessage(941901073);
				return mResult;
			}
			String product_code = mAddMaps.get("product_code");
			String sSql = "select sku_code from pc_skuinfo where product_code = "+"\'"+product_code+"\'";
			List<Map<String,Object>> skus = DbUp.upTable("pc_skuinfo").dataSqlList(sSql, null);
			PlusSupportStock plusSupportStock = new PlusSupportStock();
			int stock_num = 0;
			for(Map<String,Object> map : skus){
				int count = plusSupportStock.upSalesStock(MapUtils.getString(map, "sku_code"));
				if(stock_num == 0){
					stock_num = count;
				}else if(stock_num > count){
					stock_num = count;
				}
			}
			/*库存数*/
			/*促销库存*/
			
			if(allow_count>stock_num){
				
				mResult.inErrorMessage(941901106);
				mResult.setResultMessage("剩余库存数为："+stock_num+",允许兑换数不能大于该库存");
				return mResult;
		    	
				
			}
			
			if(allow_count>stock_num){
				
				mResult.inErrorMessage(941901106);
				return mResult;
		    	
				
			}
			
			// 循环所有结构 初始化插入map
			for (MWebField mField : mPage.getPageFields()) {
				
				if (mAddMaps.containsKey(mField.getFieldName())
						&& StringUtils.isNotEmpty(mField.getColumnName())) {

					String sValue = mAddMaps.get(mField.getFieldName());

					mInsertMap.put(mField.getColumnName(), sValue);
				}
			}
		}
		
		if (mResult.upFlagTrue()) {
			String uid = mInsertMap.get("uid");
			String start_time = mInsertMap.get("start_time");
			String end_time = mInsertMap.get("end_time");
			String product_code = mInsertMap.get("product_code");
			String channel_uid = mInsertMap.get("channel_uid");
			String product_name = mInsertMap.get("title");
			if(StringUtils.isBlank(uid)){
				if(!StringUtils.isEmpty(product_code)){//积分兑换或是加价购
					String sql = "select * from fh_apphome_channel_details where product_code = "+"\'"+product_code+"\'"+" and channel_uid = "+"\'"+channel_uid+"\' and"
							+ " end_time >= "+"\'"+start_time+"\' and start_time <= "+"\'"+end_time+"\'";
					List<Map<String,Object>> list = DbUp.upTable("fh_apphome_channel_details").dataSqlList(sql, null);
					if(list.size()>0){
						mResult.inErrorMessage(10000);
						mResult.setResultMessage("该栏目相同时间内已存在商品编号为："+product_code+",商品名称为："+product_name+"的商品！！！");
						return mResult;
					}
				}
				DbUp.upTable(mPage.getPageTable()).dataInsert(mInsertMap);
			}else{
				if(!StringUtils.isEmpty(product_code)){//积分兑换或是加价购
					String sql = "select * from fh_apphome_channel_details where product_code = "+"\'"+product_code+"\'"+" and channel_uid = "+"\'"+channel_uid+"\' and"
							+ " end_time >= "+"\'"+start_time+"\' and start_time <= "+"\'"+end_time+"\' and uid != \'"+uid+"\'" ;
					List<Map<String,Object>> list = DbUp.upTable("fh_apphome_channel_details").dataSqlList(sql, null);
					if(list.size()>0){
						mResult.inErrorMessage(10000);
						mResult.setResultMessage("该栏目相同时间内已存在商品编号为："+product_code+",商品名称为："+product_name+"的商品！！！");
						return mResult;
					}
				}
				DbUp.upTable(mPage.getPageTable()).dataUpdate(mInsertMap, null, "uid");
			}
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;
	}

}
