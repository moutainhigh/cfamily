package com.cmall.familyhas.webfunc;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.apache.commons.lang.StringUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncWHAdvertise extends RootFunc{

	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
	
		MWebResult mResult = new MWebResult();
		MDataMap upSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid = upSubMap.get("uid");
		int imgHeight = 0;
		int imgWidth = 0;
		try {
			//读取上传图片的宽，高，入库
			URL url = new URL(upSubMap.get("advertise_img").toString());
			BufferedImage image = ImageIO.read(url);
			imgHeight=image.getHeight();
			imgWidth = image.getWidth();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mResult.setResultCode(0);
			return mResult;
		}
		if(StringUtils.isBlank(uid)) {//add
            MDataMap paramMap = new MDataMap(); 
			uid = WebHelper.upUuid();
			paramMap.put("uid", uid);
			paramMap.put("advertise_code", WebHelper.upCode("ADVE"));
			paramMap.put("adver_entry_type", upSubMap.get("adver_entry_type").toString());
			paramMap.put("advertise_img", upSubMap.get("advertise_img").toString());
			paramMap.put("advertise_img_height", imgHeight+"");
			paramMap.put("advertise_img_width", imgWidth+"");
			paramMap.put("url_link", upSubMap.get("url_link").toString());
			paramMap.put("start_time", upSubMap.get("start_time").toString());
			paramMap.put("end_time", upSubMap.get("end_time").toString());
			paramMap.put("is_share", upSubMap.get("is_share").toString());
			paramMap.put("share_title", StringUtils.isBlank(upSubMap.get("share_title"))?"":upSubMap.get("share_title").toString());
			paramMap.put("share_content", StringUtils.isBlank(upSubMap.get("share_content"))?"":upSubMap.get("share_content").toString());
			paramMap.put("share_img_url", StringUtils.isBlank(upSubMap.get("share_img_url"))?"":upSubMap.get("share_img_url").toString());
			
			DbUp.upTable("fh_advertisement_info").dataInsert(paramMap);
			
		}else {//edit
			MDataMap paramMap = new MDataMap(); 
			paramMap.put("uid", uid);
			paramMap.put("adver_entry_type", upSubMap.get("adver_entry_type").toString());
			paramMap.put("advertise_img", upSubMap.get("advertise_img").toString());
			paramMap.put("advertise_img_height", imgHeight+"");
			paramMap.put("advertise_img_width", imgWidth+"");
			paramMap.put("url_link", upSubMap.get("url_link").toString());
			paramMap.put("start_time", upSubMap.get("start_time").toString());
			paramMap.put("end_time", upSubMap.get("end_time").toString());
			paramMap.put("is_share", upSubMap.get("is_share").toString());
			paramMap.put("share_title", StringUtils.isBlank(upSubMap.get("share_title"))?"":upSubMap.get("share_title").toString());
			paramMap.put("share_content", StringUtils.isBlank(upSubMap.get("share_content"))?"":upSubMap.get("share_content").toString());
			paramMap.put("share_img_url", StringUtils.isBlank(upSubMap.get("share_img_url"))?"":upSubMap.get("share_img_url").toString());
			
			DbUp.upTable("fh_advertisement_info").dataUpdate(paramMap, "adver_entry_type,advertise_img,advertise_img_height,advertise_img_width,"
					+ "url_link,start_time,end_time,is_share,share_title,share_content,share_img_url", "uid");
		}
		
		return mResult;
	}

}
