package com.cmall.familyhas.webfunc;

import com.srnpr.xmassystem.AppimageZoom;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @descriptions 删除图片缓存
 * 
 * @refactor 覆盖默认的删除方法并重写
 * @author Lizf
 * @date 2017-4-26-下午5:44:52
 * @version 1.0.0
 */
public class DeleteImageZoom extends FuncEdit {

	/**
	 * @descriptions 此处根据图片路径进行删除缓存
	 *  
	 * @param sOperateUid
	 * @param mDataMap
	 * @return mResult
	 * 
	 * @refactor 
	 * @author Lizf
	 * @date 2017-4-26-下午5:44:52
	 * @version 1.0.0.1
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap){
		MWebResult mResult = new MWebResult();
		AppimageZoom imagezoom = new AppimageZoom();
//		String [] Picturepath = [];
//		imagezoom.ImageZoom();
		return mResult;
	}

}
