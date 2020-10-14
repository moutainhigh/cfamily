package com.cmall.familyhas.job.ccvideo;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;

import com.cmall.familyhas.api.model.CcVideo;
import com.cmall.familyhas.service.cc.CCVideoService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 获取 上传CC视频的图片以及视频时长
 * @author Angel Joy
 *
 */
public class JobForGetCCVideoImgAndDuration extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		String sql = "SELECT * FROM productcenter.pc_product_evaluation_video WHERE exce_times <= 10 AND status = 1";
		List<Map<String,Object>> list = DbUp.upTable("pc_product_evaluation_video").dataSqlList(sql, null);
		CCVideoService ccservice = new CCVideoService();
		for(Map<String,Object> map : list) {
			if(map == null) {
				continue;
			}
			String img = "";
			String duration = "";
			MDataMap mmap = new MDataMap(map);
			Integer exce_times = Integer.parseInt(StringUtils.isEmpty(mmap.get("exce_times"))?"0":mmap.get("exce_times"));
			exce_times ++;
			String ccvids = mmap.get("ccvid");
			boolean flag = true;
			MDataMap evaluationMap = DbUp.upTable("nc_order_evaluation").one("ccvids",ccvids);
			MDataMap evaluationAppendMap = DbUp.upTable("nc_order_evaluation_append").one("ccvids",ccvids);
			if(evaluationMap !=null && !evaluationMap.isEmpty()) {
				flag = false;
			}
			mmap.put("exce_times", exce_times+"");
			if(evaluationAppendMap !=null && !evaluationAppendMap.isEmpty()) {
				flag = false;
			}
			if(flag) {//如果还未提交带评价，此条视频暂不处理。次数+1
				DbUp.upTable("pc_product_evaluation_video").dataUpdate(mmap, "exce_times", "uid");
				continue;
			}
			String ccvidArray[] = ccvids.split("\\|");
			String status = "2";
			for(int i = 0;i<ccvidArray.length;i++) {
				CcVideo ccvideo = ccservice.getVideoInfo(ccvids);
				if(ccvideo != null) {
					img += (ccvideo.getImg()+"|");
					duration += (ccvideo.getTime()+"|");
				}else {
					status = "1";
				}
			}
			if(StringUtils.isNotEmpty(duration)) {
				duration = duration.substring(0, duration.length()-1);
			}
			if(StringUtils.isNotEmpty(img)) {
				img = img.substring(0, img.length()-1);
			}
			mmap.put("duration", duration);
			mmap.put("pic", img);
			mmap.put("status", status);
			DbUp.upTable("pc_product_evaluation_video").dataUpdate(mmap, "duration,exce_times,pic,status", "uid");
			//如果视频正常，需要更新评价表
			if("2".equals(status)&&evaluationMap != null && !evaluationMap.isEmpty()) {
				evaluationMap.put("duration", duration);
				evaluationMap.put("ccpics", img);
				evaluationMap.put("ccvids", ccvids);
				DbUp.upTable("nc_order_evaluation").dataUpdate(evaluationMap, "duration,ccpics,ccvids", "uid");
			}
			//如果视频正常，需要更新追评表。
			if("2".equals(status)&&evaluationAppendMap != null && !evaluationAppendMap.isEmpty()) {
				evaluationAppendMap.put("duration", duration);
				evaluationAppendMap.put("ccpics", img);
				evaluationAppendMap.put("ccvids", ccvids);
				DbUp.upTable("nc_order_evaluation_append").dataUpdate(evaluationAppendMap, "duration,ccpics,ccvids", "uid");
			}
		}
	}

}
