package com.cmall.familyhas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.RegexConst;
import com.srnpr.zapdata.dbdo.DbUp;

public class MemberChange {

	private static BufferedWriter orderlog = null;
	private static BufferedWriter addresslog = null;
	private static BufferedWriter infostarlog = null;
	private static BufferedWriter loginlog = null;
	private static BufferedWriter memberinfolog = null;
	
	
	public static void main(String[] args) throws Exception {
		long starTime = System.currentTimeMillis();

		//System.out.println("=====================================");
		//DbUp.upTable("mc_login_info").dataSqlList("select * from mc_login_info", new MDataMap());
		//DbUp.upTable("oc_orderinfo").dataSqlList("select * from oc_orderinfo", new MDataMap());
		//DbUp.upTable("nc_address").dataSqlList("select * from nc_address", new MDataMap());
		//System.out.println("=====================================");
		
		orderlog = new BufferedWriter(new FileWriter("D:\\log\\order.log", true));
		addresslog = new BufferedWriter(new FileWriter("D:\\log\\address.log", true));
		infostarlog = new BufferedWriter(new FileWriter("D:\\log\\infostar.log", true));
		loginlog = new BufferedWriter(new FileWriter("D:\\log\\login.log", true));
		memberinfolog = new BufferedWriter(new FileWriter("D:\\log\\memberinfo.log", true));

		modifyOrder();
		modifyAddress();
		modifyMemberInfo();
		modifyLoginInfo();
		
		//modifyStarInfo();
		long endTime = System.currentTimeMillis();
		long Time = endTime - starTime;
		System.out.println("程序运行时间： " + Time/1000.0 + "秒");
		System.out.println("over");

		orderlog.close();
		addresslog.close();
		infostarlog.close();
		loginlog.close();
	}
	
	private static void log(BufferedWriter writer, String log){
		try {
			writer.write(log);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//修改订单信息
	public static void modifyOrder(){
		long s = System.currentTimeMillis();
		System.out.println("===================================订单合并开始==================================");
		try {
			List<MDataMap> list = null;
			MDataMap pcDatamap = null;
			MDataMap appDatamap = null;
			List<MDataMap> pcOrderList = null;
			MDataMap mDataMap = null;
			int start = 0;
			while(true){
				list = DbUp.upTable("mc_login_info").query("zid,login_name,member_code", "zid asc", "manage_code='SI2009' AND zid > "+start, null, 0, 1000);
				if(list.isEmpty()) break;
				
				// 得到最后一条的zid
				start = NumberUtils.toInt(list.get(list.size() - 1).get("zid"));
				if(start == 0) {
					System.out.print("modifyOrder >>  异常zid值: "+list.get(list.size() - 1));
					break;
				}
				
				System.out.println("modifyOrder process... "+start);
				
				for (int i = 0; i < list.size(); i++) {
					pcDatamap = list.get(i);// 一条SI2009会员的登录信息记录
					if (Pattern.matches(RegexConst.REGEX_DEFINE_MOBILE, pcDatamap.get("login_name"))) {// 匹配手机号
						appDatamap = DbUp.upTable("mc_login_info").one("login_name", pcDatamap.get("login_name"), "manage_code", "SI2003");
						pcOrderList = DbUp.upTable("oc_orderinfo").queryByWhere("buyer_code", pcDatamap.get("member_code"),"seller_code","SI2009");
						
						if (null != pcOrderList && pcOrderList.size() != 0) {
							for (int j = 0; j < pcOrderList.size(); j++) {
								mDataMap = pcOrderList.get(j);
								if(appDatamap != null){
									if(mDataMap.get("buyer_code").equals(appDatamap.get("member_code"))) continue;
									
									log(orderlog, "U," + mDataMap.get("zid") + "," + pcOrderList.get(j).get("buyer_code") + "," + appDatamap.get("member_code"));
									
									// 更新原PC的用户编码为惠家有的用户编码
									mDataMap.put("seller_code", "SI2003");
									mDataMap.put("buyer_code", appDatamap.get("member_code"));
									DbUp.upTable("oc_orderinfo").dataUpdate(mDataMap, "buyer_code", "zid");
								}else{
									if(mDataMap.get("seller_code").equals("SI2003")) continue;
									log(orderlog, "S," + mDataMap.get("zid") + "," + pcOrderList.get(j).get("buyer_code"));
									
									// 如果惠家有没有对应用户编码则只更新seller_code
									mDataMap.put("seller_code", "SI2003");
									DbUp.upTable("oc_orderinfo").dataUpdate(mDataMap, "buyer_code", "zid");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("===================================订单合并出错啦==================================");
		}
		System.out.println("===================================订单合并结束==================================");
		System.out.println("订单合并执行耗时："+(System.currentTimeMillis() - s)/1000+" 秒");
	}
	
	//合并地址
	public static void modifyAddress() {
		long s = System.currentTimeMillis();
		System.out.println("===================================地址合并开始==================================");
		try {
			MDataMap pcDatamap = null;
			MDataMap appDatamap = null;
			List<MDataMap> list = null;
			List<MDataMap> pcAddressList = null;
			MDataMap mDataMap = null;
			int start = 0;
			while(true){
				list = DbUp.upTable("mc_login_info").query("zid,login_name,member_code", "zid asc", "manage_code='SI2009' AND zid > "+start, null, 0, 1000);
				if(list.isEmpty()) break;
				
				// 得到最后一条的zid
				start = NumberUtils.toInt(list.get(list.size() - 1).get("zid"));
				if(start == 0) {
					System.out.print("modifyOrder >>  异常zid值: "+list.get(list.size() - 1));
					break;
				}
				
				System.out.println("modifyAddress process... "+start);
				
				// 遍历PC端会员，将PC端会员信息修改为APP端会员地址信息
				for (int i = 0; i < list.size(); i++) {
					pcDatamap = list.get(i);// 一条SI2009会员的登录信息记录
					if (Pattern.matches(RegexConst.REGEX_DEFINE_MOBILE, pcDatamap.get("login_name"))) {

						appDatamap = DbUp.upTable("mc_login_info").one("login_name", pcDatamap.get("login_name"), "manage_code", "SI2003");
						pcAddressList = DbUp.upTable("nc_address").queryByWhere("address_code", pcDatamap.get("member_code"),"app_code","SI2009");
						
						if (null != pcAddressList) {
							for (int j = 0; j < pcAddressList.size(); j++) {
								mDataMap = pcAddressList.get(j);
								
								if(appDatamap != null){
									if(mDataMap.get("address_code").equals(appDatamap.get("member_code"))) continue;
									log(addresslog,"U," + mDataMap.get("zid") + "," + mDataMap.get("address_code") + "," + appDatamap.get("member_code"));
									
									if (mDataMap.get("address_default") == "1") {
										mDataMap.put("address_default", "0");
									}
									mDataMap.put("address_code", appDatamap.get("member_code"));
									mDataMap.put("app_code", "SI2003");
									DbUp.upTable("nc_address").dataUpdate(mDataMap, "address_default,address_code,app_code", "zid");
								}else{
									if(mDataMap.get("app_code").equals("SI2003")) continue;
									log(addresslog, "S," + mDataMap.get("zid"));
									
									mDataMap.put("app_code", "SI2003");
									DbUp.upTable("nc_address").dataUpdate(mDataMap, "app_code", "zid");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("==========================地址合并出错啦========================");
		}
		System.out.println("==========================地址合并结束========================");
		System.out.println("地址合并执行耗时："+(System.currentTimeMillis() - s)/1000+" 秒");
	}
	
	// 合并用户基础信息
	public static void modifyMemberInfo(){
		long s = System.currentTimeMillis();
		System.out.println("===================================用户基础信息合并开始==================================");

		try {
			List<MDataMap> list = null;
			MDataMap appDatamap = null;
			MDataMap pcDatamap = null;
			MDataMap hjyDataMap = null;
			int start = 0;
			String memberCode;
			while(true){
				list = DbUp.upTable("mc_login_info").query("zid,login_name,member_code", "zid asc", "manage_code='SI2009' AND zid > "+start, null, 0, 1000);
				if(list.isEmpty()) break;
				
				// 得到最后一条的zid
				start = NumberUtils.toInt(list.get(list.size() - 1).get("zid"));
				if(start == 0) {
					System.out.print("modifyMemberInfo >>  异常zid值: "+list.get(list.size() - 1));
					break;
				}
				
				System.out.println("modifyMemberInfo process... "+start);
				
				for (int i = 0; i < list.size(); i++) {
					pcDatamap = list.get(i);// 一条SI2009会员的登录信息记录
					if (Pattern.matches(RegexConst.REGEX_DEFINE_MOBILE, pcDatamap.get("login_name"))) {// 匹配手机号
						appDatamap = DbUp.upTable("mc_login_info").one("login_name", pcDatamap.get("login_name"), "manage_code", "SI2003");

						if (appDatamap != null) {
							hjyDataMap = DbUp.upTable("mc_member_info").one("member_code", appDatamap.get("member_code"));
						} else {
							hjyDataMap = null;
						}
						
						memberCode = pcDatamap.get("member_code");
						if(hjyDataMap == null){
							// 如果PC会员没有对应的惠家有会员的则补全数据
							pcDatamap = DbUp.upTable("mc_member_info").one("member_code", memberCode);
							if(pcDatamap != null){
								pcDatamap.put("manage_code", "SI2003");
								DbUp.upTable("mc_member_info").dataUpdate(pcDatamap, "manage_code", "zid");
								log(memberinfolog, "U," + pcDatamap.get("zid"));
							}else{
								log(memberinfolog, "E," + memberCode);
							}
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			System.out.println("==========================用户基础信息合并出错啦========================");
		}
		System.out.println("==========================用户基础信息合并结束========================");
		System.out.println("用户基础信息合并执行耗时："+(System.currentTimeMillis() - s)/1000+" 秒");
	}
	
	//修改会员扩展信息
	public static void modifyStarInfo(){
		long s = System.currentTimeMillis();
		System.out.println("===================================会员扩展合并开始==================================");
		try {
			List<MDataMap> list = null;
			MDataMap updateMap = null;
			MDataMap jyDataMap = null;
			MDataMap appDatamap = null;
			MDataMap pcDatamap = null;
			MDataMap starDataMap = null;
			int start = 0;
			while(true){
				list = DbUp.upTable("mc_login_info").query("zid,login_name,member_code", "zid asc", "manage_code='SI2009' AND zid > "+start, null, 0, 1000);
				if(list.isEmpty()) break;
				
				// 得到最后一条的zid
				start = NumberUtils.toInt(list.get(list.size() - 1).get("zid"));
				if(start == 0) {
					System.out.print("modifyStarInfo >>  异常zid值: "+list.get(list.size() - 1));
					break;
				}
				
				System.out.println("modifyStarInfo process... "+start);
				
				for (int i = 0; i < list.size(); i++) {
					pcDatamap = list.get(i);// 一条SI2009会员的登录信息记录
					if (Pattern.matches(RegexConst.REGEX_DEFINE_MOBILE, pcDatamap.get("login_name"))) {// 匹配手机号
						appDatamap = DbUp.upTable("mc_login_info").one("login_name", pcDatamap.get("login_name"), "manage_code", "SI2003");
						jyDataMap = getJYUserInfo(pcDatamap.get("member_code"));

						if (appDatamap != null) {
							starDataMap = DbUp.upTable("mc_extend_info_star").one("member_code", appDatamap.get("member_code"));
						} else {
							starDataMap = null;
						}
						
						if(StringUtils.isBlank(jyDataMap.get("member_code"))){
							// 扩展表中查询不到对应编码的数据 (MI150110100795)
							log(infostarlog, "E," + pcDatamap.get("member_code"));
							continue;
						}

						// 根据mc_extend_info_star中是否有值判断是插入还是更新
						if (starDataMap == null) {
							// 新增一条记录惠家有的账户数据
							/*
							updateMap = new MDataMap();
							updateMap.put("member_code", jyDataMap.get("member_code"));
							updateMap.put("app_code", "SI2003");
							updateMap.put("nickname", StringUtils.trimToEmpty(jyDataMap.get("nickname")));
							updateMap.put("member_group", "4497465000020001");
							updateMap.put("member_sex", StringUtils.trimToEmpty(jyDataMap.get("gender")));
							updateMap.put("email", StringUtils.trimToEmpty(jyDataMap.get("email")));
							updateMap.put("mobile_phone", ifEmpty(jyDataMap.get("mobile"), pcDatamap.get("login_name")));
							updateMap.put("member_level", "4497465000030001");
							updateMap.put("create_time", StringUtils.trimToEmpty(jyDataMap.get("create_time")));
							updateMap.put("member_avatar", StringUtils.trimToEmpty(jyDataMap.get("head_pic")));
							updateMap.put("status", ifEmpty(jyDataMap.get("status"), "449746600001"));
							updateMap.put("member_name", StringUtils.trimToEmpty(jyDataMap.get("member_name")));
							updateMap.put("birthday", StringUtils.trimToEmpty(jyDataMap.get("birthday")));

							updateMap.put("old_code", ifEmpty(jyDataMap.get("homehas_code"), jyDataMap.get("old_code")));
							updateMap.put("member_sign", StringUtils.trimToEmpty(jyDataMap.get("member_sign")));
							updateMap.put("vip_type", ifEmpty(jyDataMap.get("vip_type"), "4497469400050002"));
							updateMap.put("vip_level", ifEmpty(jyDataMap.get("vip_level"), "4497469400060001"));
							updateMap.put("member_type", ifEmpty(jyDataMap.get("member_type"), "1"));
							updateMap.put("realName", StringUtils.trimToEmpty(jyDataMap.get("realName")));
							updateMap.put("points", ifEmpty(jyDataMap.get("points"), "0"));
							updateMap.put("emai_status", ifEmpty(jyDataMap.get("emai_status"), "0"));
							updateMap.put("mobile_status", ifEmpty(jyDataMap.get("mobile_status"), "0"));

							DbUp.upTable("mc_extend_info_star").dataInsert(updateMap);
							log(infostarlog, "I," + updateMap.get("member_code"));
							*/
							
						} else {
							// 更新原惠家有的账户数据
							/*
							updateMap = new MDataMap();
							updateMap.put("member_code", StringUtils.trimToEmpty(starDataMap.get("member_code")));
							updateMap.put("old_code", ifEmpty(jyDataMap.get("homehas_code"), jyDataMap.get("old_code")));
							updateMap.put("member_sign", StringUtils.trimToEmpty(jyDataMap.get("member_sign")));
							updateMap.put("vip_type", ifEmpty(jyDataMap.get("vip_type"), "4497469400050002"));
							updateMap.put("vip_level", ifEmpty(jyDataMap.get("vip_level"), "4497469400060001"));
							updateMap.put("member_type", ifEmpty(jyDataMap.get("member_type"), "1"));
							updateMap.put("realName", StringUtils.trimToEmpty(jyDataMap.get("realName")));
							updateMap.put("points", ifEmpty(jyDataMap.get("points"), "0"));
							updateMap.put("emai_status", ifEmpty(jyDataMap.get("emai_status"), "0"));
							updateMap.put("mobile_status", ifEmpty(jyDataMap.get("mobile_status"), "0"));
							DbUp.upTable("mc_extend_info_star").dataUpdate(updateMap, "old_code,member_sign,vip_type,vip_level", "member_code");
							log(infostarlog, "U," + starDataMap.get("zid") + "," + updateMap.get("old_code"));
							*/
						}

					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("===================================会员扩展合并出错啦==================================");
		}
		System.out.println("===================================会员扩展合并结束==================================");
		System.out.println("会员扩展执行耗时："+(System.currentTimeMillis() - s)/1000+" 秒");
	}
		
	// 合并登录信息
	public static void modifyLoginInfo() {
		long s = System.currentTimeMillis();
		System.out.println("===================================会员登录信息合并开始==================================");
		try {
			MDataMap pcDatamap = null;
			MDataMap appDatamap = null;
			List<MDataMap> list = null;
			int start = 0;
			while(true){
				list = DbUp.upTable("mc_login_info").query("zid,login_name,member_code", "zid asc", "manage_code='SI2009' AND zid > "+start, null, 0, 1000);
				if(list.isEmpty()) break;
				
				// 得到最后一条的zid
				start = NumberUtils.toInt(list.get(list.size() - 1).get("zid"));
				if(start == 0) {
					System.out.print("modifyLoginInfo >>  异常zid值: "+list.get(list.size() - 1));
					break;
				}
				
				System.out.println("modifyLoginInfo process... "+start);
				
				for (int i = 0; i < list.size(); i++) {
					pcDatamap = list.get(i);

					if (Pattern.matches(RegexConst.REGEX_DEFINE_MOBILE, pcDatamap.get("login_name"))) {

						appDatamap = DbUp.upTable("mc_login_info").one("login_name", pcDatamap.get("login_name"), "manage_code", "SI2003");

						// 仅是PC会员,则将PC的manage_code的SI2009改成APP
						// ->SI2003；否则将PC会员信息逻辑删除
						if (appDatamap == null) {
							pcDatamap.put("manage_code", "SI2003");
							DbUp.upTable("mc_login_info").dataUpdate(pcDatamap, "manage_code", "zid");
							log(loginlog, "U," + pcDatamap.get("zid"));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("==========================会员登录信息合并出错啦========================");
		}
		System.out.println("==========================会员登录合并结束========================");
		System.out.println("会员基础合并执行耗时："+(System.currentTimeMillis() - s)/1000+" 秒");
	}
	
	// 根据memberCode获取用户信息，合并了mc_extend_info_homehas和mc_extend_info_homepool数据
	private static MDataMap getJYUserInfo(String memberCode){
		MDataMap homeHasDataMap = DbUp.upTable("mc_extend_info_homehas").one("member_code", memberCode);
		MDataMap homePoolDataMap = DbUp.upTable("mc_extend_info_homepool").one("member_code", memberCode);
		
		if(homeHasDataMap == null) homeHasDataMap = new MDataMap();
		if(homePoolDataMap == null) homePoolDataMap = new MDataMap();
		
		homeHasDataMap.putAll(homePoolDataMap);
		
		return homeHasDataMap;
	}
	
	/**
	 * 返回两个值里面非空的那个，如果都是null则返回空字符串
	 * @param v1
	 * @param v2
	 * @return
	 */
	private static String ifEmpty(String v1, String v2){
		if(StringUtils.isNotEmpty(v1)) return v1;
		return StringUtils.trimToEmpty(v2);
	}
	
		/*一条龙服务 ~~~*/
	    //这是合并mc_extend_info_homepool到mc_extend_info_star，删选mc_login_info
/*		public static void change() {
			List<MDataMap> list=DbUp.upTable("mc_login_info").queryByWhere("manage_code","SI2009");
			for (int i = 0; i < list.size(); i++) {
				MDataMap pcDatamap=list.get(i);//一条SI2009会员的登录信息记录
				
				String login_name=pcDatamap.get("login_name");
				boolean bFlagCheck=Pattern.matches(RegexConst.REGEX_DEFINE_MOBILE,login_name);
				if(bFlagCheck){
					
					MDataMap appDatamap=DbUp.upTable("mc_login_info").one("login_name",login_name,"manage_code","SI2003");
					
					//表示会员是SI2009的但不是SI2003的人信息
					if(appDatamap==null){
						
						pcDatamap.put("manage_code", "SI2003");		
						//将家有汇的manage_code的SI2009改成慧家有SI2003。
						DbUp.upTable("mc_login_info").dataUpdate(pcDatamap, "manage_code", "zid");
						
						//将家有汇的会员信息添加到慧家有的个人信息
						MDataMap homepoolmemberInfo=DbUp.upTable("mc_extend_info_homepool").one("member_code",pcDatamap.get("member_code"));
						if(homepoolmemberInfo!=null){
							MDataMap insertMap=new MDataMap();
							//属于原来家有汇的会员信息字段
							insertMap.put("member_code", homepoolmemberInfo.get("member_code"));
							insertMap.put("member_name", homepoolmemberInfo.get("member_name"));
							insertMap.put("mobile_phone", homepoolmemberInfo.get("mobile"));
							insertMap.put("email", homepoolmemberInfo.get("email"));
							insertMap.put("nickname", homepoolmemberInfo.get("nickname"));
							insertMap.put("member_sex", homepoolmemberInfo.get("gender"));
							insertMap.put("member_avatar", homepoolmemberInfo.get("head_pic"));
							insertMap.put("birthday", homepoolmemberInfo.get("birthday"));
							insertMap.put("old_code", homepoolmemberInfo.get("old_code"));
							insertMap.put("member_sign", homepoolmemberInfo.get("member_sign"));
							insertMap.put("realName", homepoolmemberInfo.get("realName"));
							insertMap.put("emai_status", homepoolmemberInfo.get("emai_status"));
							insertMap.put("mobile_status", homepoolmemberInfo.get("mobile_status"));
							insertMap.put("vip_type", homepoolmemberInfo.get("vip_type"));
							insertMap.put("vip_level", homepoolmemberInfo.get("vip_level"));
							insertMap.put("member_level", homepoolmemberInfo.get("vip_level"));
							insertMap.put("points", homepoolmemberInfo.get("points"));
							insertMap.put("status", homepoolmemberInfo.get("status"));
							insertMap.put("create_time", homepoolmemberInfo.get("create_time"));
							insertMap.put("member_type", homepoolmemberInfo.get("member_type"));
							insertMap.put("member_group", "4497465000020001");
							//app端的初始化字段
							insertMap.put("app_code", "SI2003");
							DbUp.upTable("mc_extend_info_star").dataInsert(insertMap);
						}
						List<MDataMap> pcOrderList1=DbUp.upTable("oc_orderinfo").queryByWhere("buyer_code",pcDatamap.get("member_code"));
						if(null != pcOrderList1 || pcOrderList1.size() !=0){
							for (int j = 0; j < pcOrderList1.size(); j++) {
								MDataMap mDataMap=pcOrderList1.get(j);
								mDataMap.put("seller_code", "SI2003");
								DbUp.upTable("oc_orderinfo").dataUpdate(mDataMap, "seller_code", "zid");
							}
						}
						List<MDataMap> pcAddressList1=DbUp.upTable("nc_address").queryByWhere("address_code",pcDatamap.get("member_code"));
						if(null != pcAddressList1 || pcAddressList1.size() !=0){
							for (int j = 0; j < pcAddressList1.size(); j++) {
								MDataMap mDataMap=pcAddressList1.get(j);
								mDataMap.put("app_code", "SI2003");
								DbUp.upTable("nc_address").dataUpdate(mDataMap, "app_code", "zid");
							}
						}
						
						
					  }
					//表示会员既是家有汇的又是慧家有的，更新会员字段
					else{//app和pc都注册的会员删除信息。
						

						//查看app下有没有扩展信息，如果没有，查看家有汇下的信息，如果有，则将家有汇扩展信息合并到惠家有，
						MDataMap appmemberInfo=DbUp.upTable("mc_extend_info_star").one("member_code",appDatamap.get("member_code"));
						if(appmemberInfo==null){
							MDataMap homepoolInfo=DbUp.upTable("mc_extend_info_homepool").one("member_code",pcDatamap.get("member_code"));
							if(homepoolInfo!=null){
								MDataMap inserthomeMap=new MDataMap();
								//属于原来家有汇的会员信息字段
								inserthomeMap.put("member_code", homepoolInfo.get("member_code"));
								inserthomeMap.put("member_name", homepoolInfo.get("member_name"));
								inserthomeMap.put("mobile_phone", homepoolInfo.get("mobile"));
								inserthomeMap.put("email", homepoolInfo.get("email"));
								inserthomeMap.put("nickname", homepoolInfo.get("nickname"));
								inserthomeMap.put("member_sex", homepoolInfo.get("gender"));
								inserthomeMap.put("member_avatar", homepoolInfo.get("head_pic"));
								inserthomeMap.put("birthday", homepoolInfo.get("birthday"));
								inserthomeMap.put("old_code", homepoolInfo.get("old_code"));
								inserthomeMap.put("member_sign", homepoolInfo.get("member_sign"));
								inserthomeMap.put("realName", homepoolInfo.get("realName"));
								inserthomeMap.put("emai_status", homepoolInfo.get("emai_status"));
								inserthomeMap.put("mobile_status", homepoolInfo.get("mobile_status"));
								inserthomeMap.put("vip_type", homepoolInfo.get("vip_type"));
								inserthomeMap.put("vip_level", homepoolInfo.get("vip_level"));
								inserthomeMap.put("member_level", homepoolInfo.get("vip_level"));
								inserthomeMap.put("points", homepoolInfo.get("points"));
								inserthomeMap.put("status", homepoolInfo.get("status"));
								inserthomeMap.put("create_time", homepoolInfo.get("create_time"));
								inserthomeMap.put("member_type", homepoolInfo.get("member_type"));
								inserthomeMap.put("member_group", "4497465000020001");
								//app端的初始化字段
								inserthomeMap.put("app_code", "SI2003");
								DbUp.upTable("mc_extend_info_star").dataInsert(inserthomeMap);
							}
						}
						

						String pcmemberCode=pcDatamap.get("member_code");
						String appmemberCode=appDatamap.get("member_code");
						List<MDataMap> pcOrderList=DbUp.upTable("oc_orderinfo").queryByWhere("buyer_code",pcmemberCode);
						if(null != pcOrderList || pcOrderList.size() !=0){
							for (int j = 0; j < pcOrderList.size(); j++) {
								MDataMap mDataMap=pcOrderList.get(j);
								mDataMap.put("buyer_code", appmemberCode);
								mDataMap.put("seller_code", "SI2003");
								DbUp.upTable("oc_orderinfo").dataUpdate(mDataMap, "buyer_code,seller_code", "zid");
							}
						}
						//修改用户地址信息
						List<MDataMap> pcAddressList=DbUp.upTable("nc_address").queryByWhere("address_code",pcmemberCode);
						if(null != pcAddressList || pcAddressList.size() !=0){
							for (int j = 0; j < pcAddressList.size(); j++) {
								
								MDataMap mDataMap=pcAddressList.get(j);
								
								if(mDataMap.get("address_default")=="1"){
									mDataMap.put("address_default", "0");
								}
								mDataMap.put("address_code", appmemberCode);
								mDataMap.put("app_code", "SI2003");
								DbUp.upTable("nc_address").dataUpdate(mDataMap, "address_default,address_code,app_code", "zid");
							}
							
						}
						MDataMap deleteDataMap=new MDataMap();
						deleteDataMap.put("if_delete", "0");
						deleteDataMap.put("zid", pcDatamap.get("zid"));
						DbUp.upTable("mc_login_info").dataUpdate(deleteDataMap,"if_delete","zid");
					}
				}
			}
		}*/
		
}



    
 
