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

public class FuncWHAdvertColumn extends RootFunc{

	

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap upSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid = upSubMap.get("uid");
		if(StringUtils.isNotBlank(uid)) {
			int imgHeight = 0;
			int imgWidth = 0;
			try {
				//读取上传图片的宽，高，入库
				URL url = new URL(upSubMap.get("img_url").toString());
				BufferedImage image = ImageIO.read(url);
				imgHeight=image.getHeight();
				imgWidth = image.getWidth();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mResult.setResultCode(0);
				return mResult;
			}
			MDataMap paramMap = new MDataMap();
			paramMap.put("uid", uid);
			paramMap.put("img_url", upSubMap.get("img_url").toString());
			paramMap.put("img_height", String.valueOf(imgHeight));
			paramMap.put("img_width", String.valueOf(imgWidth));
			paramMap.put("link_type", upSubMap.get("link_type").toString());
			paramMap.put("link_url", upSubMap.get("link_url").toString());
			paramMap.put("sort_num", upSubMap.get("sort_num").toString());
			paramMap.put("is_share", upSubMap.get("is_share").toString());
			paramMap.put("share_title", StringUtils.isBlank(upSubMap.get("share_title"))?"":upSubMap.get("share_title").toString());
			paramMap.put("share_content", StringUtils.isBlank(upSubMap.get("share_content"))?"":upSubMap.get("share_content").toString());
			paramMap.put("share_img_url", StringUtils.isBlank(upSubMap.get("share_img_url"))?"":upSubMap.get("share_img_url").toString());
			
			DbUp.upTable("fh_advert_column").dataUpdate(paramMap, "img_url,img_height,img_width,link_type,link_url,sort_num,is_share,share_title,share_content,share_img_url", "uid");
		}else {
			String advertise_code = upSubMap.get("advertise_code").toString();
			MDataMap one = DbUp.upTable("fh_advert").one("advertise_code",advertise_code);
			String programa_num = one.get("programa_num");
			int count = DbUp.upTable("fh_advert_column").count("advertise_code",advertise_code);
			if(!(count < Integer.parseInt(programa_num))) {
				mResult.setResultCode(0);
				mResult.setResultMessage("实际广告数量已达设置数量上限,不允许添加!");
				return mResult;
			}
			int imgHeight = 0;
			int imgWidth = 0;
			try {
				//读取上传图片的宽，高，入库
				URL url = new URL(upSubMap.get("img_url").toString());
				BufferedImage image = ImageIO.read(url);
				imgHeight=image.getHeight();
				imgWidth = image.getWidth();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mResult.setResultCode(0);
				return mResult;
			}
	        MDataMap paramMap = new MDataMap(); 
			uid = WebHelper.upUuid();
			paramMap.put("uid", uid);
			paramMap.put("advertise_code",advertise_code);
			paramMap.put("programa_code", WebHelper.upCode("GRAM"));
			paramMap.put("img_url", upSubMap.get("img_url").toString());
			paramMap.put("img_height", String.valueOf(imgHeight));
			paramMap.put("img_width", String.valueOf(imgWidth));
			paramMap.put("link_type", upSubMap.get("link_type").toString());
			paramMap.put("link_url", upSubMap.get("link_url").toString());
			paramMap.put("sort_num", upSubMap.get("sort_num").toString());
			paramMap.put("is_share", upSubMap.get("is_share").toString());
			paramMap.put("share_title", StringUtils.isBlank(upSubMap.get("share_title"))?"":upSubMap.get("share_title").toString());
			paramMap.put("share_content", StringUtils.isBlank(upSubMap.get("share_content"))?"":upSubMap.get("share_content").toString());
			paramMap.put("share_img_url", StringUtils.isBlank(upSubMap.get("share_img_url"))?"":upSubMap.get("share_img_url").toString());
			
			DbUp.upTable("fh_advert_column").dataInsert(paramMap);
		}
		return mResult;
	}

}
