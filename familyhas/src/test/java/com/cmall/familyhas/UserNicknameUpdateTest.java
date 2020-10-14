package com.cmall.familyhas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.cmall.familyhas.api.ApiUserNickNameUpdate;
import com.srnpr.zapdata.dbdo.DbUp;

public class UserNicknameUpdateTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException { 
		ApiUserNickNameUpdate u = new ApiUserNickNameUpdate();
		
		long start = System.currentTimeMillis();
		
		String sql = "select * from membercenter.mc_member_name_temp";
		List<Map<String, Object>> list = DbUp.upTable("mc_member_name_temp").dataSqlList(sql, null); 
		if(list.size() == 0){
			return;
		}
		
		ExecutorService executor = Executors.newCachedThreadPool();
		int size = 10000; // 单组list大小  10000
		int count = list.size() / size; // TreeMap 的分组数       10008/20 = 500 余 8 
		int count_ = list.size() - count * size; // 余数 
		Map<Integer , List<Map<String, Object>>> mapgroup = new TreeMap<Integer , List<Map<String, Object>>>();
		for(int i = 0 ; i < count ; i ++){
			mapgroup.put(i , list.subList(i*size , size*(i+1)));
		}
		if(count_ != 0){
			mapgroup.put(count, list.subList(count*size, list.size())); 
		}
		
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();   
		try {
			for (Map.Entry<Integer, List<Map<String, Object>>> entry : mapgroup.entrySet()) {
				TaskNicknameUpdate task = new TaskNicknameUpdate(entry.getValue() , u);
				futureList.add(executor.submit(task)); 
			}
		} catch (Exception e) {
			e.printStackTrace();  
		}finally {
			executor.shutdown();  
		}
		
		for (Future<Integer> fs : futureList){  
			while(!fs.isDone()){ 
				System.out.println("执行未完成.......................");
				Thread.sleep(500); 
			}
			fs.get();
		}
		
		
		long end = System.currentTimeMillis();
		System.out.println("总共耗时：" + +(end - start) + " 毫秒");
	}
}















































