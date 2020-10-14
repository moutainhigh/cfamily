package com.cmall.familyhas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.itextpdf.text.log.SysoCounter;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.WebClientRequest;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapcom.topdo.TopDir;


/**
 * 使用HttpClient的jar,发送http请求
 * 
 * @author zhanghao
 * 
 */
public class HttpUtil extends BaseClass{

	/**
	 * 发送HTTP post请求
	 * 
	 * @param url
	 *            请求的地址
	 * @param data
	 *            发送的数据(现阶段的数据格式为json格式的字符串)
	 * @param charsetName
	 *            字符集 默认UTF-8
	 * @return http响应返回的字符串
	 */
	public static String post(String url, String data, String charsetName) {
		HttpUtil hu = new HttpUtil();
		String result;
		try {
			result = hu.getHttps(url,data);
			return result;
		} catch (KeyManagementException | ParseException
				| NoSuchAlgorithmException | KeyStoreException
				| CertificateException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Data:"+data);
		return null;
//		Integer statusCode = -1;
//		// Create HttpClient Object
//		DefaultHttpClient client = new DefaultHttpClient();
//		// Send data by post method in HTTP protocol,use HttpPost instead of
//		// PostMethod which was occurred in former version
//		HttpPost post = new HttpPost(url);
//		// Construct a string entity
//		StringEntity entity;
//		try {
//			entity = new StringEntity(data, charsetName);
//			entity.setContentType("application/json;charset=UTF-8");
//			// Set XML entity
//			post.setEntity(entity);
//			// Set content type of request header
//			post.setHeader("accept", "application/json");
//			post.setHeader("Content-Type", "application/json;charset=UTF-8");
//			// Execute request and get the response
//			HttpResponse response = client.execute(post);
//
//			// Response Header - StatusLine - status code
//			statusCode = response.getStatusLine().getStatusCode();
//
//			if (statusCode != HttpStatus.SC_OK) {
//				throw new HttpException("Http Status is error.");
//			}
//			HttpEntity entityRsp = response.getEntity();
//			StringBuffer result = new StringBuffer();
//			BufferedReader rd = new BufferedReader(new InputStreamReader(
//					entityRsp.getContent(), HTTP.UTF_8));
//			String tempLine = rd.readLine();
//			while (tempLine != null) {
//				result.append(tempLine);
//				tempLine = rd.readLine();
//			}
//
//			if (entityRsp != null) {
//				entityRsp.consumeContent();
//			}
//			return result.toString();
//
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return createOutResult("编码错误!");
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//			return createOutResult("协议错误!");
//		} catch (IOException e) {
//			e.printStackTrace();
//			return createOutResult("IO通讯错误!");
//		} catch (HttpException e) {
//			e.printStackTrace();
//			return createOutResult("通讯错误!");
//		}
	}
	
	/**
	 * 调用处理逻辑 返回操作
	 * 
	 * @param sRequestString
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 */
	private String getHttps(String sUrl, String sRequestString)
			throws ParseException, IOException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, CertificateException {
		WebClientRequest webClientRequest = new WebClientRequest();

		String sDir = bConfig("groupcenter.homehas_key");

		if (StringUtils.isEmpty(sDir)) {
			TopDir topDir = new TopDir();
			sDir = topDir.upCustomPath("") + "tomcat.keystore";
		}

		webClientRequest.setFilePath(sDir);
		webClientRequest.setUrl(sUrl);

		HttpEntity httpEntity = new StringEntity(sRequestString,
				TopConst.CONST_BASE_ENCODING);

		webClientRequest.setConentType("application/json");
		//webClientRequest.setConentType("application/x-www-form-urlencoded");
		

		webClientRequest
				.setPassword(bConfig("groupcenter.rsync_homehas_password"));

		bLogInfo(0, EntityUtils.toString(httpEntity));

		webClientRequest.setHttpEntity(httpEntity);

		String sResponseString = WebClientSupport.upHttpsPost(webClientRequest);

		return sResponseString;
	}
	
	
	public static String post(String url, String data, String charsetName,String contentType) {
		HttpUtil hu = new HttpUtil();
		String result;
		try {
			result = hu.getHttps(url,data,contentType);
			return result;
		} catch (KeyManagementException | ParseException
				| NoSuchAlgorithmException | KeyStoreException
				| CertificateException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Data:"+data);
		return null;

	}
	/**
	 * 调用处理逻辑 返回操作
	 * 
	 * @param sRequestString
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 */
	public String getHttps(String sUrl, String sRequestString,String contentType)
			throws ParseException, IOException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, CertificateException {
		WebClientRequest webClientRequest = new WebClientRequest();

		String sDir = bConfig("groupcenter.homehas_key");

		if (StringUtils.isEmpty(sDir)) {
			TopDir topDir = new TopDir();
			sDir = topDir.upCustomPath("") + "tomcat.keystore";
		}

		webClientRequest.setFilePath(sDir);
		webClientRequest.setUrl(sUrl);

		HttpEntity httpEntity = new StringEntity(sRequestString,
				TopConst.CONST_BASE_ENCODING);

		webClientRequest.setConentType(contentType);
		//webClientRequest.setConentType("application/x-www-form-urlencoded");
		

		webClientRequest
				.setPassword(bConfig("groupcenter.rsync_homehas_password"));

		bLogInfo(0, EntityUtils.toString(httpEntity));

		webClientRequest.setHttpEntity(httpEntity);

		String sResponseString = WebClientSupport.upHttpsPost(webClientRequest);

		return sResponseString;
	}
	/**
	 * 包装返回结果
	 * 
	 * @param callResult
	 * @return
	 */
	public static String createOutResult(String callResult) {
		StringBuffer reVol = new StringBuffer();

		reVol.append("{\"success\":false,\"message\":\"");
		reVol.append(callResult);
		reVol.append("\"}");
		return reVol.toString();
	}

	public static void main(String[] args) {
		String apiKey = "2PTwX2QNHSghSm8UoagS32s5bQG7I1hR";
        String userId = "7408202D39A123FF";
        String videoid = "2BF4165AAAF813B09C33DC5901307461";
//        String videoid = "DEC0D0AD62F2F52A9C33DC5901307461";
        Date date = new Date();
        String time = date.getTime()+"";
        String params2 = "userid="+userId;
        String params3 = "videoid="+videoid;
        String totalStr = params2+"&"+params3;
        String params1 = "time="+time;
        String md5Parmas = MD5Util.MD5(totalStr+"&"+params1+"&salt="+apiKey);
        String videoHost = "http://union.bokecc.com/api/mobile";
        String allurl = videoHost.concat("?").concat(totalStr).concat("&").concat(params1).concat("&").concat("hash=").concat(md5Parmas);
        System.out.println(allurl);
        String result = HttpUtil.post(allurl, "{}", "UTF-8");
		System.out.println(result);
		String temp = HttpUtil.xmlElements(result);  
		System.out.println(temp);

	}
	
	public static String xmlElements(String xmlDoc) {
        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        String linkvalue = "";
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
            System.out.println(root.getName());//输出根元素的名称（测试）
            //得到根元素所有子元素的集合
            List jiedian = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Namespace ns = root.getNamespace();
            Element et = null;            
            for(int i=0;i<jiedian.size();i++){
                et = (Element) jiedian.get(i);//循环依次得到子元素
                if(linkvalue.equals("")){
                	linkvalue = et.getText();
                }
                if(et.getAttribute("quality").getValue().equals("20")){
                	linkvalue = et.getText();
                }
               
                System.out.println(et.getText());
            }          
        } catch (JDOMException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return linkvalue;
    }
}
