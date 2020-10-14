package com.cmall.familyhas;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopTest;
import com.srnpr.zapdata.dbdo.DbUp;

public class BackPic extends TopTest {
	
	ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<String>();

	public void abc(String[] args) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File("")));
			String sql = null;
			while((sql = reader.readLine()) != null) {
				String[] sqlArray = sql.split(",");
				if(sqlArray.length != 2) {
					continue;
				}
				
				List<Map<String, Object>> list = DbUp.upTable(sqlArray[1]).dataSqlList(sqlArray[0], new MDataMap());
				if(list == null || list.isEmpty()) {
					continue;
				}
				
				for(Map<String, Object> urlMap : list) {
					Collection<Object> urls = urlMap.values();
					Iterator<Object> it = urls.iterator();
					while(it.hasNext()) {
						Object obj =  it.next();
						if(obj == null || obj.toString().length() == 0) {
							continue;
						}
						
						String url = obj.toString();
						if(!url.contains("staticfiles")) {
							System.out.println("spec:" + url);
							continue;
						}
						
						String[] urlArray = url.split(",");
						if(urlArray.length == 1) {
							urlArray = url.split("|");
						}
						
						for(String fileUrl : urlArray) {
							if(urlQueue.contains(fileUrl)) {
								continue;
							}
							byte[] btImg = getImageFromNetByUrl(url);
							if (null != btImg && btImg.length > 0) {
								System.out.println("读取到：" + btImg.length + " 字节");
								String fileName = "百度.gif";
								writeImageToDisk(btImg, fileName);
							} else {
								System.out.println("没有从该连接获得内容");
							}
							urlQueue.offer(fileUrl);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e) {
			
		}
	}

	/**
	 * 将图片写入到磁盘
	 * 
	 * @param img
	 *            图片数据流
	 * @param fileName
	 *            文件保存时的名称
	 */
	public static void writeImageToDisk(byte[] img, String fileName) {
		FileOutputStream fops = null;
		try {
			File file = new File("C:\\" + fileName);
			fops = new FileOutputStream(file);
			fops.write(img);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fops != null) {
				try {
					fops.flush();
					fops.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("图片已经写入到C盘");
		}
	}

	/**
	 * 根据地址获得数据的字节流
	 * 
	 * @param strUrl
	 *            网络连接地址
	 * @return
	 */
	public static byte[] getImageFromNetByUrl(String strUrl) {
		InputStream inStream = null;
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(3 * 1000);
			conn.setReadTimeout(3 * 1000);
			inStream = conn.getInputStream();// 通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		byte[] data = null;
		ByteArrayOutputStream outStream = null;
		try {
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(outStream != null) {
				data = outStream.toByteArray();
				outStream.close();
			}
		}
		return data;
	}
	
	public String getDestFileName(String url) {
		url = StringUtils.substringAfter(url, "staticfiles");
//		String url.split("/");
		return "";
	}
}
