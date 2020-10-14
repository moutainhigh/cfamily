package com.cmall.familyhas.job.ccvideo;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;

import com.cmall.familyhas.upload.Uploader;
import com.cmall.familyhas.upload.VideoInfo;
import com.cmall.familyhas.util.VideoUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForUploadLocalVideoToCC extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		String sql = "SELECT * FROM productcenter.pc_product_evaluation_video WHERE status = 0";
		List<Map<String,Object>> list = DbUp.upTable("pc_product_evaluation_video").dataSqlList(sql, null);
		for(Map<String,Object> map :list) {
			if(map == null) {
				continue;
			}
			MDataMap mmap = new MDataMap(map);
			String local_ccuid = mmap.get("local_ccuid");
			boolean flag = this.checkSubmit(local_ccuid);
			if(!flag) {
				continue;
			}
			String paths = mmap.get("path");
			if(StringUtils.isEmpty(paths)) {
				continue;
			}
			String pathArray [] = paths.split("\\|");
			String ccvid = "";
			String updatePath = "";
			for(int i = 0;i<pathArray.length;i++) {
				String filepath = pathArray[i];
				String filepaths [] = filepath.split("\\/");
				String path = filepath.replace(filepaths[filepaths.length-1], "");
				String ext = filepaths[filepaths.length-1].split("\\.")[1];
				String newPath = VideoUtils.cut(filepath, path, 30000l, ext);
				if(!newPath.equals(filepath)) {//生成了新的视频文件，旧文件删除
					boolean delFlag = VideoUtils.deleteDir(filepath);
					if(!delFlag) {
						bLogError(0,"文件删除失败："+filepath);
					}
				}
				VideoInfo videoInfo = new VideoInfo(new File(newPath));
				Uploader upload = new Uploader(videoInfo);
				ccvid += (upload.upload()+"|");
				updatePath += (newPath+"|");
			}
			if(!StringUtils.isEmpty(ccvid)) {
				ccvid = ccvid.substring(0, ccvid.length()-1);
				mmap.put("status", "1");
			}
			updatePath = updatePath.substring(0, updatePath.length()-1);
			mmap.put("ccvid", ccvid);
			mmap.put("path", updatePath);
			DbUp.upTable("pc_product_evaluation_video").dataUpdate(mmap, "path,ccvid,status", "uid");
		}
	}

	/**
	 * 校验是否提交评价了，如果未提交评价的不处理
	 * @param local_ccuid
	 * @return
	 */
	private boolean checkSubmit(String local_ccuid) {
		//判断评论表是否记录
		Integer count = DbUp.upTable("nc_order_evaluation").count("local_ccuid",local_ccuid);
		if(count > 0) {
			return true;
		}
		//判断追评表是否有记录
		count = DbUp.upTable("nc_order_evaluation_append").count("local_ccuid",local_ccuid);
		if(count > 0) {
			return true;
		}
		//均无记录的话，返回false；
		return false;
	}

}
