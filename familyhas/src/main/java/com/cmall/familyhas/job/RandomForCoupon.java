package com.cmall.familyhas.job;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.srnpr.zapweb.helper.WebHelper;

public class RandomForCoupon {

	/**
	   * 产生随机字符串
	   * */
	private static Random randGen = new Random();;
	private static char[] numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();;

	/**
	 *@param length 长度
	 *@param num 生成数量
	 *@param start 开头字符串
	 * 
	 */
	public Set<String> randomString(int length,int num,String start) {
	         if (length < 1||num < 1) {
	             return null;
	         }
	         Set<String> set = new HashSet<String>();
	         for (int j = 0; j < num; j++) {
	        	 char [] randBuffer = new char[length];
		         for (int i=0; i<randBuffer.length; i++) {
		          randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
		         }
		         set.add(start+new String(randBuffer));
	         }
	         return set;
	}
	public String bulisSql(String key) {
		String sql="INSERT INTO `ordercenter`.`oc_coupon_type` (`zid`, `uid`, `coupon_type_code`, `coupon_type_name`, `activity_code`, `money`, `limit_money`, `total_money`, `privide_money`, `surplus_money`, `start_time`, `end_time`, `status`, `multi_account`, `account_useTime`, `cdkey`, `produce_type`, `creater`, `create_time`, `updater`, `update_time`) " +
				"VALUES (0, REPLACE(UUID(),'-',''), '"+WebHelper.upCode("CT")+"', '"+key+"', 'AC150529100002', '20', '20', '20', '0', '20', '2015-05-31 00:00:00', '2015-06-08 00:00:00', '4497469400030002', '449746250002', '1', '"+key+"', '4497471600040002', 'system', '2015-05-29 22:22:22', 'system', '2015-05-29 22:22:22');";
		return sql;
	}
}
