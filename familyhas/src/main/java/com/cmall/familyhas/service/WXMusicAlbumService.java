package com.cmall.familyhas.service;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.util.AddShuiyin;
import com.cmall.familyhas.util.AlbumPicHandler;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.HttpUtil;
import com.cmall.familyhas.webfunc.MerchantApproveFunc;
import com.srnpr.xmassystem.load.LoadWXMusicAlbumToken;
import com.srnpr.xmassystem.modelevent.PlusModelwxMusicAlbumToken;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webmethod.WebUpload;
import com.srnpr.zapweb.webmodel.MWebResult;





/**
 *微信小程序-音乐相册
 *
 */
public class WXMusicAlbumService extends BaseClass {
	
	final int width = 300;
	final int height = 240;
	final int widthAlbum = 690;
	int heightAlbum = 388;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	static AtomicInteger idx = new AtomicInteger();
	public Map<String, Object> doProcess(org.json.JSONObject dataJson, Object retPicUrlObj) {
		
		
		 Map<String, Object> result = new HashMap<String, Object>();
		 String oldPicUrl = retPicUrlObj.toString();
		 String zipPicUrl = oldPicUrl;
		 String review_state="1"; //默认审核通过
		 String access_token = "";
		 
		try {
			
			 //上传图片做旋转角度处理(iphone/数码相机拍照有exif 属性来限定照片角度，需要做校验处理)
			 //URL url = new URL(oldPicUrl);
			 //Image img = ImageIO.read(url);
			 byte[] markImageByMoreIcon0 = AddShuiyin.markImageByMoreIconForMusicAlbum(MerchantApproveFunc.class.getResourceAsStream("/play.png"), "jpg", null,oldPicUrl,width,0,false);
			 MWebResult remoteUpload0 = WebUpload.create().remoteUpload("upload", WebHelper.upUuid()+oldPicUrl.substring(oldPicUrl.lastIndexOf(".")), markImageByMoreIcon0);
			 zipPicUrl = remoteUpload0.getResultObject().toString();
			 //对上传的图片进行违规审核校验
			 access_token=getToken();
			 if(StringUtils.isNotBlank(access_token)) {
				review_state=checkPic(zipPicUrl,access_token);
			  }
			 
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 String openId = dataJson.getString("open_id");
		 String templateId = dataJson.getString("template_id");
		 String albumId = dataJson.getString("album_id"); 
		 String syAlbumPic="";
		 String paramUid="";
		 String sPic="";
		 MDataMap UserPicDataMap = new MDataMap();
		 //MDataMap subResultDataMap = new MDataMap();
		 List< Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		 UserPicDataMap.put("uid", WebHelper.upUuid());
		 UserPicDataMap.put("open_id",openId );
		 
		try {
			 if(StringUtils.isBlank(albumId)||"0".equals(albumId)) {
				 Map<String,Object> subResultDataMap = new HashMap<String,Object>();
					//相册表
					 albumId = WebHelper.upCode("AM");
					 MDataMap mDataMap = new MDataMap();
					 MDataMap mPicDataMap = new MDataMap();
					 mDataMap.put("open_id",openId);
					 mDataMap.put("album_id", albumId);
					 mDataMap.put("use_flag", "N");
					 mDataMap.put("template_id", templateId);
					 mDataMap.put("uid", WebHelper.upUuid());
					 mDataMap.put("create_time", FormatHelper.upDateTime());
					 mDataMap.put("update_time",  FormatHelper.upDateTime());
					 DbUp.upTable("hp_music_album").dataInsert(mDataMap);
					 
				   //相册图片表	 
					 String imgId = WebHelper.upCode("IMGID");
					 paramUid=WebHelper.upUuid();
					 mPicDataMap.put("uid",paramUid );
					 mPicDataMap.put("review_state",review_state );
					 mPicDataMap.put("album_id",albumId);
					 mPicDataMap.put("img_sort","1");
					 mPicDataMap.put("img_id",imgId);
					 mPicDataMap.put("preview_img", zipPicUrl);
					 mPicDataMap.put("main_img", oldPicUrl);
					 mPicDataMap.put("album_img", syAlbumPic);
					 mPicDataMap.put("share_img", sPic);
					 mPicDataMap.put("create_time", FormatHelper.upDateTime());
					 mPicDataMap.put("update_time", FormatHelper.upDateTime());
					 DbUp.upTable("hp_music_album_img").dataInsert(mPicDataMap);
					 
					 UserPicDataMap.put("album_img", syAlbumPic);
					 UserPicDataMap.put("review_state", review_state);
					 UserPicDataMap.put("share_img", sPic);
					 UserPicDataMap.put("preview_img",zipPicUrl );
					 UserPicDataMap.put("main_img",oldPicUrl );
					 UserPicDataMap.put("img_id",imgId );
					 
					 if(review_state.equals("1")) {
						 subResultDataMap.put("img_id",imgId );
						 subResultDataMap.put("img_sort",1 );
						 subResultDataMap.put("img_preview_url", zipPicUrl);
						 subResultDataMap.put("img_url", oldPicUrl);
						 subResultDataMap.put("album_img", syAlbumPic);
						 subResultDataMap.put("share_img", sPic);
						 list.add(subResultDataMap); 
					 }
					 
				 }else {
					 Map<String,Object> subResultDataMap = new HashMap<String,Object>();
					 List<Map<String, Object>> dataSqlList = DbUp.upTable("hp_music_album_img").dataSqlList("select * from hp_music_album_img where album_id=:album_id and review_state='1' order by img_sort asc", new MDataMap("album_id",albumId));
					 int len = dataSqlList.size();
					 for (Map<String, Object> map : dataSqlList) {
						 Map<String,Object> ssubResultDataMap = new HashMap<String,Object>();
						 ssubResultDataMap.put("img_id",map.get("img_id").toString() );
						 ssubResultDataMap.put("img_sort",(Integer.parseInt(map.get("img_sort").toString())));
						 ssubResultDataMap.put("img_preview_url", map.get("preview_img").toString());
						 ssubResultDataMap.put("img_url", map.get("main_img").toString());
						 ssubResultDataMap.put("album_img", map.get("album_img").toString());
						 ssubResultDataMap.put("share_img", map.get("share_img").toString());
						 list.add(ssubResultDataMap);
					}
					 
					 //相册图片表	 
					 MDataMap mPicDataMap = new MDataMap();
					 String imgId = WebHelper.upCode("IMGID");
					 paramUid=WebHelper.upUuid();
					 mPicDataMap.put("uid",paramUid );
					 mPicDataMap.put("review_state",review_state );
					 mPicDataMap.put("album_id",albumId);
					 mPicDataMap.put("img_sort",(Integer.parseInt(dataSqlList.get(len-1).get("img_sort").toString())+1)+"");
					 mPicDataMap.put("img_id",imgId);
					 mPicDataMap.put("preview_img", zipPicUrl);
					 mPicDataMap.put("album_img", syAlbumPic);
					 mPicDataMap.put("share_img", sPic);
					 mPicDataMap.put("main_img", oldPicUrl);
					 mPicDataMap.put("create_time", FormatHelper.upDateTime());
					 mPicDataMap.put("update_time", FormatHelper.upDateTime());
					 DbUp.upTable("hp_music_album_img").dataInsert(mPicDataMap);
					 
					 UserPicDataMap.put("album_img", syAlbumPic);
					 UserPicDataMap.put("review_state", review_state);
					 UserPicDataMap.put("share_img", sPic);
					 UserPicDataMap.put("preview_img",zipPicUrl );
					 UserPicDataMap.put("main_img",oldPicUrl );
					 UserPicDataMap.put("img_id",imgId );
					 
					 if(review_state.equals("1")) {
					 subResultDataMap.put("img_id",imgId );
					 subResultDataMap.put("img_sort",(Integer.parseInt(dataSqlList.get(len-1).get("img_sort").toString())+1));
					 subResultDataMap.put("img_preview_url", zipPicUrl);
					 subResultDataMap.put("img_url", oldPicUrl);
					 subResultDataMap.put("album_img", syAlbumPic);
					 subResultDataMap.put("share_img", sPic);
					 list.add(subResultDataMap);
					 }
				 }
				  //用户上传图片表
			     UserPicDataMap.put("create_time", DateUtil.getSysDateString());
				 DbUp.upTable("hp_music_album_user_pic").dataInsert(UserPicDataMap);
				 //优化，开新线程处理
				 ExecutorService fixedThreadPool = null;
					try {
						 fixedThreadPool = Executors.newFixedThreadPool(1);
						 fixedThreadPool.execute(new AlbumPicHandler(MerchantApproveFunc.class.getResourceAsStream("/play.png"), "jpg", null,oldPicUrl,widthAlbum,heightAlbum,true,oldPicUrl,paramUid,UserPicDataMap.get("uid"),"album_img"));
						 fixedThreadPool.execute(new AlbumPicHandler(MerchantApproveFunc.class.getResourceAsStream("/play.png"), "jpg", null,oldPicUrl,width,height,true,oldPicUrl,paramUid,UserPicDataMap.get("uid"),"share_img"));
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						/**
						 * 关闭线程池
						 */
						if (fixedThreadPool != null) {
							fixedThreadPool.shutdown();
						}
					}
				if(review_state.equals("1")) {
					MDataMap dataMap = DbUp.upTable("hp_music_album").one("album_id",albumId);
					result.put("success", "true");
					result.put("album_id",albumId );
					result.put("template_id",Integer.parseInt(dataMap.get("template_id").toString()));
					result.put("user_template_imgs", list);	
					
					}
				else {
					MDataMap dataMap = DbUp.upTable("hp_music_album").one("album_id",albumId);
					result.put("success", "false");
					result.put("album_id",albumId );
					result.put("template_id",Integer.parseInt(dataMap.get("template_id").toString()));
					result.put("user_template_imgs", list);
					result.put("message", "存在违规图片!");
				 }
								
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", "false");
		}
		return result;
		
	}
	
	
	/**
     * 上传图片审核校验
     * @param multipartFile
     * @return
     */
    public  String checkPic(String imgUrl, String accessToken) {
        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();

            CloseableHttpResponse response = null;

            HttpPost request = new HttpPost("https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken);
            request.addHeader("Content-Type", "application/octet-stream");
            URL imageURL = new URL(imgUrl);
            BufferedImage originalImage=ImageIO.read(imageURL);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            byte[] byt =baos.toByteArray();

            request.setEntity(new ByteArrayEntity(byt, ContentType.create("image/jpg")));
            response = httpclient.execute(request);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");// 转成string
            JSONObject jso = JSONObject.parseObject(result);
            //System.out.println(jso + "-------------验证效果");

            Object errcode = jso.get("errcode");
            int errCode = (int) errcode;
            if (errCode == 0) {
                return "1";
            } else if (errCode == 87014) {
                //System.out.println("图片内容违规-----------");
                return "0";
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("----------------调用腾讯内容过滤系统出错------------------");
            return "1";
        }
    }
	   
	   public Map<String,String> getWXToken() {
		    Date now = new Date();
		   /*String hostUrl ="https://small-wx-gate.huijiayou.cn/Merchant/ReceiveMerchantRequest.aspx";
		    String channeId = "16";
		    String merchantId = "10013";
		    String sKey = "c7jj9gwck946atpmbb745fsstjr3wuyk0giiqdc7gid";*/
		    String hostUrl = bConfig("zapweb.wxgate_url");
		    String channeId = bConfig("zapweb.send_xcx_channelid");
		    String merchantId = bConfig("zapweb.wxgate_merchant_id");
		    String sKey = bConfig("zapweb.wxgate_merchant_key");
		    Map<String, Object> param = new HashMap<String, Object>();
		    List<String> paramList = new ArrayList<String>();

		    String orderno = dateFormat.format(now) + (1000 + idx.incrementAndGet());
		    String tradetime = dateFormat.format(now);
		    String expiretime = dateFormat.format(new Date(now.getTime() + 3600000 * 24));
		    String fixtime = dateFormat.format(now);
	        paramList.add("merchantid="+merchantId);
	        paramList.add("tradetype=SendWX");
	        paramList.add("tradecode=Wx_token");
	        paramList.add("orderno="+orderno);
	        paramList.add("tradetime="+tradetime);
	        paramList.add("sender=");
	        paramList.add("v="+"1.1");
	        paramList.add("receivers=");
	        paramList.add("message=");
	        paramList.add("expiretime="+expiretime);
	        paramList.add("maxsendcount=1");
	        paramList.add("wxtype="+"TOKEN");
	        paramList.add("tradekeyid=");
	        paramList.add("channelid="+channeId);
	        paramList.add("isfixtime=0");
	        paramList.add("fixtime="+fixtime);              
	        //签名参数封装
	        param.put("merchantid",merchantId );
	        param.put("tradetype", "SendWX");
	        param.put("tradecode","Wx_token");
	        param.put("orderno",orderno);
	        param.put("tradetime",tradetime);
	        param.put("sender","");
	        param.put("v","1.1" );
	        param.put("receivers", "");
	        param.put("message","" );
	        param.put("expiretime",expiretime);
	        param.put("maxsendcount", "1");
	        param.put("wxtype","TOKEN" );
	        param.put("tradekeyid", "");
	        param.put("channelid",channeId );
	        param.put("isfixtime","0" );
	        param.put("fixtime",fixtime);
	        String signature = createSignature(param,sKey);
	        paramList.add( "mac="+signature);
	        String paramStr = "";
	        paramStr=StringUtils.join(paramList,"&");
	        String returnStr = HttpUtil.post(hostUrl, paramStr, "","application/x-www-form-urlencoded");
	        Map<String,String> map = new HashMap<String,String>();
	        if(StringUtils.isNotBlank(returnStr)) { 
	        	 try {
	        		 returnStr= URLDecoder.decode(returnStr, "gb2312");
		        	 
	 	        	String[] split = returnStr.split("&");
	 	        	for (String string : split) {
 	        		if(string.contains("resultmessage")) {
 		        		String[] split2 = string.split("=");
 		        		String val = split2[1];
 		        		if(StringUtils.isNotBlank(val)) {
	        			Object parse = JSONObject.parse(val);
	        			net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(parse);
	        			Object object = fromObject.get("access_token");
	        			Object object2 = fromObject.get("expires_in");
	        			map.put("access_token", object==null?"":object.toString());
	        			map.put("expires_in", object2==null?"":object2.toString());
	        			return map;
 		        		}
 	        		}

	 				}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        }
	        return map;
	   }
	   
		private String createSignature(Map<String, Object> param,String merchantKey) {
			List<String> dataList = new ArrayList<String>();
			Set<Map.Entry<String, Object>> entryList = param.entrySet();
			for (Map.Entry<String, Object> entry : entryList) {
				if (entry.getValue() != null && !entry.getValue().toString().trim().isEmpty()) {
					dataList.add(entry.getValue().toString().trim());
				}
			}
			Collections.sort(dataList);

			dataList.add(merchantKey);
			String text = StringUtils.join(dataList, "");

			text = DigestUtils.md5Hex(text);
			return text;
		}
		
		public  String  getToken() {
			LoadWXMusicAlbumToken loadJDToken = new LoadWXMusicAlbumToken();
			loadJDToken.deleteInfoByCode("wxMA");
			PlusModelwxMusicAlbumToken plusModelJDToken = loadJDToken.upInfoByCode(new PlusModelQuery("wxMA"));
			String access_token = plusModelJDToken.getAccess_token();
			String currentTime = DateUtil.getSysDateTimeString();
			if(StringUtils.isBlank(access_token)) {
			    Map<String, String> wxToken = getWXToken();
			    access_token = wxToken.get("access_token").toString();
			    String ei = wxToken.get("expires_in").toString();
				String eTime = DateUtil.addDateSec(currentTime, Integer.parseInt(ei));
				DbUp.upTable("sc_token").dataInsert(new MDataMap("uid",WebHelper.upUuid(),"access_token",access_token,"code","wxMA","expires_time",eTime,"create_time",currentTime));
				loadJDToken.deleteInfoByCode("wxMA");
			}else {
				String expires_time = plusModelJDToken.getExpires_time();
				if(!DateUtil.compareDateTime(currentTime, expires_time)) {
					Map<String, String> wxToken = getWXToken();
					access_token = wxToken.get("access_token").toString();
					String ei = wxToken.get("expires_in").toString();
					String eTime = DateUtil.addDateSec(currentTime, Integer.parseInt(ei));
					DbUp.upTable("sc_token").dataInsert(new MDataMap("uid",WebHelper.upUuid(),"access_token",access_token,"code","wxMA","expires_time",eTime,"create_time",currentTime));
					loadJDToken.deleteInfoByCode("wxMA");
				}
			}
			return access_token;
		}
		
		
		

}
