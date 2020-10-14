package com.cmall.familyhas;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.load.LoadProductInfo;

public class BatchRefreshProductCache {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = FileUtils.readLines(new File("C:\\Users\\Administrator\\Desktop\\test.txt"));
		LoadProductInfo load = new LoadProductInfo();
		for(String line : lines) {
			if(StringUtils.isNotBlank(line)) {
				load.deleteInfoByCode(line);
			}
		}
	}

}
