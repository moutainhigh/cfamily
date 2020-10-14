package com.cmall.familyhas;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 链接单台redis 服务器，  取缓存数据
 * @author fq
 *
 */
public class CallRedisServer {
	
	
	private static ExecutorService service;
	

    public static void main(String[] args) {
    	
        		
		int threadSize = initThreadPool(6);
		for (int i=600000 ;i < 1200000 ;i+= 100000) {
			CopyRedisKeyValue runModel = new CopyRedisKeyValue(i , 100000);
			service.execute(runModel);
		}
		
		//等待所有线程执行完毕退出释放锁
		service.shutdown();
        try {  
            boolean loop = true;  
            do {  
                loop = !service.awaitTermination(2, TimeUnit.SECONDS);
            } while(loop);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }
        		
        	
    }
    
    public static int initThreadPool(int listSize) {
		service = Executors.newFixedThreadPool(listSize);
		return listSize;
	}

}
