package com.cmall.familyhas.util;

/**
 * 获得随机数工具类
 * @remark 
 * @author 任宏斌
 * @date 2020年2月26日
 */
public class RandomUtil {

	/**
	 * 随机获得区间内的一个整数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandomBetween(int start,int end) {

        int num=(int) (Math.random()*(end-start+1)+start);
        return num;
    }
	
}
