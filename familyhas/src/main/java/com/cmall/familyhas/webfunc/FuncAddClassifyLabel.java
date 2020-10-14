package com.cmall.familyhas.webfunc;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加商品分类标签
 * @author zhangb
 *
 */
public class FuncAddClassifyLabel extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mWebResult = new MWebResult();
		String loginUser = UserFactory.INSTANCE.create().getLoginName();
		String updateTime = DateUtil.getNowTime();
		String startTime = mDataMap.get("zw_f_start_time");
		String endTime = mDataMap.get("zw_f_end_time");
		String labelName = mDataMap.get("zw_f_label_name");
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
		if(this.checkRepeatPro(attributeProduct)) {
			mWebResult.setResultCode(941901133);
			mWebResult.setResultMessage("所属商品已被占用");
			return mWebResult;
		}
	
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
		}
		
		//标签名判重复
		int count = this.checkRepeat(labelName);
		if(count==0) {
			mDataMap.put("zw_f_label_code",WebHelper.upCode("LB"));
			mDataMap.put("zw_f_update_time", updateTime);
			mDataMap.put("zw_f_update_user", loginUser);
			mDataMap.put("zw_f_flag_enable", "1");
			
			mWebResult = super.funcDo(sOperateUid, mDataMap);
		}
		else {
			mWebResult.setResultCode(941901133);
			mWebResult.setResultMessage("标签名已存在");
			return mWebResult;
		}
		return mWebResult;
       
	}
	
	private int checkRepeat(String labelName){
		int count = 0;
		if (StringUtils.isNotBlank(labelName)) {
			count = DbUp.upTable("pc_product_classify_labels").count("label_name",labelName,"flag_enable","1");
		}
		return count;		
	}
	private boolean checkRepeatPro(String attributePro) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String[] string =  attributePro.split(",");
		if (StringUtils.isNotBlank(attributePro)) {
			String sSql = "select attribute_product from pc_product_classify_labels where flag_enable=:flag_enable";
			 list =DbUp.upTable("pc_product_classify_labels").dataSqlList(sSql, new MDataMap("flag_enable","1"));
		     if(list.size()>0) {
		    	 for(int i=0;i<list.size();i++) {
		    		 for(int j=0;j<string.length;j++) {
		    			 if(list.get(i).get("attribute_product").toString().contains(string[j])) {
		    				 return true;
		    			 }
		    		 }
		    	 }
		     }
		}
		return false;	
	}
	
}
