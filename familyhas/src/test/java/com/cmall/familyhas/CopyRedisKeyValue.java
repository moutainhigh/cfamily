package com.cmall.familyhas;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.kvsupport.RedisCall;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class CopyRedisKeyValue implements Runnable{

	private int start ;
	private int end ;
	
	public CopyRedisKeyValue (int start , int end) {
		this.start = start;
		this.end = end;
	}
	
    public void copyData(){
    	/**
    	 * 老redis
    	 */
    	JedisPoolConfig config = new JedisPoolConfig();
        /** 生成连接池 **/
    	JedisPool pool = new JedisPool(config, "10.20.2.2", 7000);
        /** 从连接池获取连接 **/
    	Jedis oldClient = pool.getResource();
    	
        /**
         * 新redis
         */
    	RedisCall  redisCall = new RedisCall("10.20.2.2:7003,10.20.2.3:7003,10.20.2.4:7003,10.20.2.2:7004,10.20.2.3:7004,10.20.2.4:7004,10.20.2.2:7005,10.20.2.3:7005,10.20.2.4:7005");
    	
        try {
        	MDataMap one = DbUp.upTable("mc_member_info").one("member_code","MI141013101864");
        	if(null != one) {
	        	List<MDataMap> queryAll = DbUp.upTable("mc_member_info").query("member_code", "zid", "manage_code='SI2003'", new MDataMap() , start , end);
	        	System.out.println("数据大小："+queryAll.size());
	        	String preKey = "xs-ShopCart-";
	        	for (int i =0 ;i<queryAll.size() ;i++) {
	    			String member_code = queryAll.get(i).get("member_code");
	    			System.out.println(i+"redis_key:"+preKey + member_code);
	    			String shopCart = oldClient.get(preKey + member_code);
	    			if(StringUtils.isNotBlank(shopCart)) {
	    				redisCall.set(preKey + member_code, shopCart);
	    			}
	    		}
        	}
        	
        } catch (Exception e) {
            System.out.println("exception ......");
        } finally {
            /** 业务操作完成，将连接返回给连接池 **/
            if (null != oldClient) {
                pool.returnBrokenResource(oldClient);
            }
        }
        /** 应用关闭时，释放连接池资源 **/
        pool.destroy();
    }
	
	
	@Override
	public void run() {
		copyData();
	}

}
