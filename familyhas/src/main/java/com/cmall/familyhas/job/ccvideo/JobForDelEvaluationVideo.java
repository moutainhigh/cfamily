package com.cmall.familyhas.job.ccvideo;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;

import com.cmall.familyhas.util.VideoUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 删除评价视频本地文件
 * @author Angel Joy
 * @date 2019-12-23 10:08:26
 */
public class JobForDelEvaluationVideo extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		String sql = "SELECT * FROM productcenter.pc_product_evaluation_video where is_delete = 0 and status = 2";
		List<Map<String,Object>> paths = DbUp.upTable("pc_product_evaluation_video").dataSqlList(sql, null);
		for(Map<String,Object> map : paths) {
			String path = map !=null && map.get("path")!=null?map.get("path").toString():"";
			if(StringUtils.isEmpty(path)) {
				continue;
			}
			String pathArray[] = path.split("\\|");
			for(int i = 0;i<pathArray.length;i++) {
				File file = new File(pathArray[i]);
				if(file.exists()) {//存在
					VideoUtils.deleteFileOrDirectory(file);
				}
			}
			boolean flag = true;
			for(int i = 0;i<pathArray.length;i++) {
				File file = new File(pathArray[i]);
				if(file.exists()) {//存在
					flag = false;
				}
			}
			if(flag) {//不存在临时文件了，需要置删除状态为已删除。
				MDataMap mmap = new MDataMap(map);
				mmap.put("is_delete", "1");
				DbUp.upTable("pc_product_evaluation_video").dataUpdate(mmap, "is_delete", "uid");
			}
		}
	}

}
