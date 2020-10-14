package com.cmall.familyhas.webfunc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改商品分类标签
 * 
 * @author zhangbo
 * 
 */
public class FuncEditProClassifyLabels extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mWebResult = new MWebResult();
		String loginUser = UserFactory.INSTANCE.create().getLoginName();
		String updateTime = DateUtil.getNowTime();
		String startTime = mDataMap.get("zw_f_start_time");
		String endTime = mDataMap.get("zw_f_end_time");
		String labelName = mDataMap.get("zw_f_label_name");
		String uid = mDataMap.get("zw_f_uid");
		if(StringUtils.isBlank(labelName)) {
			mWebResult.setResultCode(941901133);
			mWebResult.setResultMessage("标签名称不能为空");
			return mWebResult;
		}
		if(StringUtils.isBlank(startTime)||StringUtils.isBlank(endTime)) {
			mWebResult.setResultCode(941901133);
			mWebResult.setResultMessage("非法的时间范围");
			return mWebResult;
		}
		if(!DateUtil.compareDateTime(startTime, endTime)) {
			mWebResult.setResultCode(941901133);
			mWebResult.setResultMessage("开始时间不能大于结束时间");
			return mWebResult;
		}
		String attributeProduct = mDataMap.get("zw_f_attribute_product");
		if(StringUtils.isBlank(attributeProduct)) {
			mWebResult.setResultCode(941901133);
			mWebResult.setResultMessage("所属商品不能为空");
			return mWebResult;
		}
//		if(this.checkRepeatPro(attributeProduct)) {
//			mWebResult.setResultCode(941901133);
//			mWebResult.setResultMessage("所属商品已被占用");
//			return mWebResult;
//		}
	
		//原型给的图片非必填，若必填，则放开
//		String listPic = mDataMap.get("zw_f_list_pic");
//		String infoPic = mDataMap.get("zw_f_info_pic");
//		if(StringUtils.isBlank(listPic) && StringUtils.isBlank(infoPic)){
//			mWebResult.setResultCode(941901133);
//			mWebResult.setResultMessage("请至少上传一个标签图片");
//			return mWebResult;
//		}
		
		// 546需求添加,保存商品列表标签的宽高
		try {
			// 获取商品列表标签的宽高
			String listPic = mDataMap.get("zw_f_list_pic");
			URL url = new URL(listPic);
	        URLConnection connection = url.openConnection();
	        connection.setDoOutput(true);
	        BufferedImage listImage = ImageIO.read(connection.getInputStream()); 
			int height = listImage.getHeight();
			int width = listImage.getWidth();
			mDataMap.put("zw_f_list_pic_height", height+"");
			mDataMap.put("zw_f_list_pic_width", width+"");
		} catch (IOException e) {
			e.printStackTrace();
			mWebResult.setResultCode(941901133);
			mWebResult.setResultMessage("获取商品列表标签图片宽高有误!");
			return mWebResult;
		}
		
		//标签名判重复
		int count = this.checkRepeat(labelName,uid);
		if(count==0) {
			mDataMap.put("zw_f_update_time", updateTime);
			mDataMap.put("zw_f_update_user", loginUser);
			mWebResult = super.funcDo(sOperateUid, mDataMap);
			//不知道这个方法是做什么使的
			//XmasKv.upFactory(EKvSchema.ProductLabels).del(mDataMap.get("zw_f_label_code"));
		}
		else {
			mWebResult.setResultCode(941901133);
			mWebResult.setResultMessage("标签名已存在");
			return mWebResult;
		}
		return mWebResult;
       
	}
	
	private int checkRepeat(String labelName,String uid){
		int count = 0;
		if (StringUtils.isNotBlank(labelName)) {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			String sSql = "select label_code from pc_product_classify_labels where label_name=:label_name  and flag_enable='1' and uid !=:uid";
			list =DbUp.upTable("pc_product_classify_labels").dataSqlList(sSql, new MDataMap("label_name",labelName,"uid",uid));
			count = list.size();
		}
		return count;		
	}

}
