package com.cmall.familyhas.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.util.RandomUtil;
import com.cmall.familyhas.util.TencentUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.live.v20180801.LiveClient;
import com.tencentcloudapi.live.v20180801.models.DropLiveStreamRequest;
import com.tencentcloudapi.live.v20180801.models.DropLiveStreamResponse;
import com.tencentyun.TLSSigAPIv2;

public class LiveService extends BaseClass{
	/**
	 * 推流url
	 */
	private final String pushurlformat = "rtmp://%s/%s/%s?%s";
	/**
	 * 创建群组url
	 */
	private final String creategroupurlformat = "https://console.tim.qq.com/v4/group_open_http_svc/create_group?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json";
	/**
	 * 解散群组url
	 */
	private final String dropgroupurlformat = "https://console.tim.qq.com/v4/group_open_http_svc/destroy_group?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json";
	/**
	 * 禁言或取消禁言url
	 */
	private final String openorstopurlformat = "https://console.tim.qq.com/v4/group_open_http_svc/destroy_group?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json";
	private Credential credential = new Credential(bConfig("familyhas.tencent.live.secretId"),bConfig("familyhas.tencent.live.secretKey"));
    private static final char[] DIGITS_LOWER =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
    
    /**
          *  获取推送直播url接口
     * @param appName APP名称 非必传
     * @param key   该名称对应的key 非必传
     * @param streamName 房间号id
     * @param txTime  过期时间 unix 时间戳(单位为秒)
     * @return
     */
    public String getPushUrl(String appName,String key, String streamName, long txTime) {
    	appName = StringUtils.isBlank(appName)?bConfig("familyhas.tencent.live.appName"):appName;
    	key = StringUtils.isBlank(key)?bConfig("familyhas.tencent.live.key"):key;
    	return String.format(pushurlformat, bConfig("familyhas.tencent.pushlive.domain"),appName,streamName,getSafeUrl(key,streamName,txTime));
    }
    /**
     *  获取拉流直播url接口
	* @param appName APP名称 非必传
	* @param key   该名称对应的key 非必传
	* @param streamName 房间号id
	* @param txTime  过期时间 unix 时间戳(单位为秒)
	* @return
	*/
	public String getPullUrl(String appName,String key, String streamName, long txTime) {
		appName = StringUtils.isBlank(appName)?bConfig("familyhas.tencent.live.appName"):appName;
		key = StringUtils.isBlank(key)?bConfig("familyhas.tencent.live.key"):key;
		return String.format(pushurlformat, bConfig("familyhas.tencent.pulllive.domain"),appName,streamName,getSafeUrl(key,streamName,txTime));
	}
    /**
          *  关闭直播流接口
     * @param appName APP名称 非必传
     * @param streamName 房间号id
     */
    public boolean dropLiveStream(String appName, String streamName) {
    	appName = StringUtils.isBlank(appName)?bConfig("familyhas.tencent.live.appName"):appName;
    	DropLiveStreamRequest req = new DropLiveStreamRequest();
    	req.setAppName(appName);
    	req.setDomainName(bConfig("familyhas.tencent.pushlive.domain"));
    	req.setStreamName(streamName);
    	DropLiveStreamResponse dropLiveStream = null;
    	String requestId = null;
    	try {
			dropLiveStream = new LiveClient(credential, null).DropLiveStream(req);
		} catch (TencentCloudSDKException e) {
			e.printStackTrace();
			requestId = e.getRequestId();
			return false;
		}
    	if(StringUtils.isBlank(requestId)) {
    		requestId = dropLiveStream.getRequestId();
    	}
    	return true;
    }
    /**
          * 获取IM的userSig接口
     * @param sdkappid 应用id  非必传
     * @param userid  userid
     * @param expire  过期时间(单位为秒)
     * @return userSig
     */
    public String genSig(String sdkappid,String userid,long expire,String secret) {
    	sdkappid = StringUtils.isBlank(sdkappid)?bConfig("familyhas.tencent.im.sdkappid"):sdkappid;
    	secret = StringUtils.isBlank(secret)?bConfig("familyhas.tencent.im.secret"):secret;
    	TLSSigAPIv2 api = new TLSSigAPIv2(Long.parseLong(sdkappid), secret);
    	return api.genSig(userid, expire);
    }
    public static void main(String[] args) {
//		String sdkappid = "1400389132";
//    	String appAccount = "admin";
//    	String secret = "1c0d67118cf204542261e47df98581e8d52c8f9fe930e203acf04c7779d80561";
//    	String createGroup = new LiveService().createGroup(sdkappid,appAccount,secret,0l,"哈哈");
//    	System.out.println(createGroup);
    	System.out.println(new LiveService().getPushUrl("huijiayou_cloud", "59e92b8aca668fd9ac3b64524f9a1496", "abc", 1593446400));
 //   	new LiveService().dropGroup(sdkappid, appAccount, secret, 0l, "@TGS#2XZ7XGRGC");
	}
    /**
          * 创建群组接口
     * @param sdkappid 非必传
     * @param appAccount 非必传
     * @param secret  非必传
     * @param expire 过期时间:单位为秒   传0就是取默认值  默认一天
     * @param groupName
     * @return 返回分组id groupid
     */
    public String createGroup(String sdkappid,String appAccount,String secret,long expire,String groupName) {
    	sdkappid = StringUtils.isBlank(sdkappid)?bConfig("familyhas.tencent.im.sdkappid"):sdkappid;
    	appAccount = StringUtils.isBlank(appAccount)?bConfig("familyhas.tencent.im.appAccount"):appAccount;
    	secret = StringUtils.isBlank(secret)?bConfig("familyhas.tencent.im.secret"):secret;
    	if(expire == 0) {
    		expire = 24*60*60;
    	}
    	String usersig = genSig(sdkappid,appAccount,expire,secret);
    	int randomBetween = RandomUtil.getRandomBetween(0, 999999999);
    	String creategroupurl = String.format(creategroupurlformat, sdkappid,appAccount,usersig,randomBetween);
    	WebClientSupport sup = new WebClientSupport();
    	JSONObject param = new JSONObject();
    	param.put("Type", "AVChatRoom");
    	param.put("Name", groupName);
    	try {
			String upPostJson = sup.upPostJson(creategroupurl, param.toJSONString());
			JSONObject parseObject = JSONObject.parseObject(upPostJson);
			Integer integer = parseObject.getInteger("ErrorCode");
			if(integer == 0) {
				return  parseObject.getString("GroupId");
			}else {
				System.out.println("错误信息:"+parseObject.getString("ErrorInfo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return  "-1";
    }
    /**
         * 解散群组接口
     * @param sdkappid 非必传
     * @param appAccount 非必传
     * @param secret 非必传
     * @param expire  过期时间:单位为秒   传0就是取默认值  默认一天
     * @param groupid 组id
     */
    public void dropGroup(String sdkappid,String appAccount,String secret,long expire,String groupid) {
    	sdkappid = StringUtils.isBlank(sdkappid)?bConfig("familyhas.tencent.im.sdkappid"):sdkappid;
    	appAccount = StringUtils.isBlank(appAccount)?bConfig("familyhas.tencent.im.appAccount"):appAccount;
    	secret = StringUtils.isBlank(secret)?bConfig("familyhas.tencent.im.secret"):secret;
    	if(expire == 0) {
    		expire = 24*60*60;
    	}
    	TLSSigAPIv2 api = new TLSSigAPIv2(Long.parseLong(sdkappid), secret);
    	String usersig = api.genSig(appAccount, expire);
    	int randomBetween = RandomUtil.getRandomBetween(0, 999999999);
    	String dropgroupurl = String.format(dropgroupurlformat, sdkappid,appAccount,usersig,randomBetween);
    	WebClientSupport sup = new WebClientSupport();
    	JSONObject param = new JSONObject();
    	param.put("GroupId", groupid);
    	try {
			String upPostJson = sup.upPostJson(dropgroupurl, param.toJSONString());
			JSONObject parseObject = JSONObject.parseObject(upPostJson);
			Integer integer = parseObject.getInteger("ErrorCode");
			if(integer == 0) {
			}else {
				System.out.println("错误信息:"+parseObject.getString("ErrorInfo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public String openOrStop(String sdkappid,String appAccount,String secret,String groupid) {
    	sdkappid = StringUtils.isBlank(sdkappid)?bConfig("familyhas.tencent.im.sdkappid"):sdkappid;
    	appAccount = StringUtils.isBlank(appAccount)?bConfig("familyhas.tencent.im.appAccount"):appAccount;
    	secret = StringUtils.isBlank(secret)?bConfig("familyhas.tencent.im.secret"):secret;
        return "";
    }
    /**
     * 获取上传视频签名接口
     * @param secretId
     * @param secretKey
     * @param expire 过期时间(单位为秒) 默认一天
     * @return
     */
    public String uploadVideo(String secretId,String secretKey,int expire) {
    	TencentUtil sign = new TencentUtil();
    	secretId = StringUtils.isBlank(secretId)?bConfig("familyhas.tencent.live.secretId"):secretId;
    	secretKey = StringUtils.isBlank(secretKey)?bConfig("familyhas.tencent.live.secretKey"):secretKey;
        sign.setSecretId(secretId);
        sign.setSecretKey(secretKey);
        long t = System.currentTimeMillis() / 1000;
        sign.setCurrentTime(t);
        int r = new Random().nextInt(java.lang.Integer.MAX_VALUE);
        sign.setRandom(r);
        expire = 3600 * 24;
        sign.setSignValidDuration(expire);
        try {
            String signature = sign.getUploadSignature();
            return signature;
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }
        return "";
    }
    
    private static String getSafeUrl(String key, String streamName, long txTime) {
        String input = new StringBuilder().
                            append(key).
                            append(streamName).
                            append(Long.toHexString(txTime).toUpperCase()).toString();
        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                        messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
        }
        return txSecret == null ? "" :
                           new StringBuilder().
                           append("txSecret=").
                           append(txSecret).
                           append("&").
                           append("txTime=").
                           append(Long.toHexString(txTime).toUpperCase()).
                           toString();
        }
    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];
        for (int i = 0, j = 0; i < data.length; i++) {
                out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
                out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }
}
