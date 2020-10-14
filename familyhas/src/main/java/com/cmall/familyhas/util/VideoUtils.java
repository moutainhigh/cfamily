package com.cmall.familyhas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import com.cmall.familyhas.service.WXMusicAlbumService;
import com.srnpr.zapcom.baseclass.BaseClass;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;

/**
 * @desc 视频工具类
 * @author Angel Joy
 * @date 2019-12-19 16:57:00
 */
public class VideoUtils extends BaseClass{
	public static void main(String[] args) {
		VideoUtils utils = new VideoUtils();
		WXMusicAlbumService wxMusicAlbumService = new WXMusicAlbumService();
		String token = wxMusicAlbumService.getToken();
		System.out.println(utils.checkContent("级", token));
	}

	public static void demo(String path) {
		File source = new File(path);
		Encoder encoder = new Encoder();
		FileChannel fc = null;
		String size = "";
		try {
			it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(source);
			long ls = m.getDuration();
			System.out.println("此视频时长为:" + ls / 60000 + "分" + (ls) / 1000 + "秒！");
			// 视频帧宽高
			System.out.println("此视频高度为:" + m.getVideo().getSize().getHeight());
			System.out.println("此视频宽度为:" + m.getVideo().getSize().getWidth());
			System.out.println("此视频格式为:" + m.getFormat());
			FileInputStream fis = new FileInputStream(source);
			fc = fis.getChannel();
			BigDecimal fileSize = new BigDecimal(fc.size());
			size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "MB";
			System.out.println("此视频大小为" + size);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fc) {
				try {
					fc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param path
	 * @return Map
	 */
	public static Map<String, Object> getVoideMsg(String path) {

		Map<String, Object> map = new HashMap<String, Object>();
		File file = new File(path);
		Encoder encoder = new Encoder();
		FileChannel fc = null;
		String size = "";

		if (file != null) {
			try {
				it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(file);
				long ls = m.getDuration();

				FileInputStream fis = new FileInputStream(file);
				fc = fis.getChannel();
				BigDecimal fileSize = new BigDecimal(fc.size());
				size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "MB";

				map.put("height", m.getVideo().getSize().getHeight());
				map.put("width", m.getVideo().getSize().getWidth());
				map.put("format", m.getFormat());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != fc) {
					try {
						fc.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return map;
	}

	/**
	 * 上传图片审核校验
	 * 
	 * @param multipartFile
	 * @return
	 */
	public String checkPics(byte[] img, String accessToken) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			CloseableHttpResponse response = null;
			String url = bConfig("familyhas.wx_check_url");
			HttpPost request = new HttpPost(url+"wxa/img_sec_check?access_token=" + accessToken);
			request.addHeader("Content-Type", "application/octet-stream");

			request.setEntity(new ByteArrayEntity(img, ContentType.create("image/jpg")));
			response = httpclient.execute(request);
			HttpEntity httpEntity = response.getEntity();
			String result = EntityUtils.toString(httpEntity, "UTF-8");// 转成string
			JSONObject jso = JSONObject.parseObject(result);

			Object errcode = jso.get("errcode");
			int errCode = (int) errcode;
			if (errCode == 0) {
				return "1";
			} else if (errCode == 87014) {
				// System.out.println("图片内容违规-----------");
				return "0";
			}
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----------------调用腾讯内容过滤系统出错------------------");
			return "1";
		}
	}
	
	/**
	 * 上传图片审核校验
	 * 
	 * @param multipartFile
	 * @return
	 */
	public boolean checkContent(String counts, String accessToken) {
		try {
			String url = bConfig("familyhas.wx_check_url")+"/wxa/msg_sec_check?access_token=" + accessToken;
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("content", counts);
			String result = HttpUtil.post(url, JSONObject.toJSONString(data), "UTF-8");
			if(StringUtils.isEmpty(result)) {
				return true;
			}
			JSONObject jo = JSONObject.parseObject(result);
			Integer code = jo.getInteger("errcode");
			if(code == 0) {
				return true;
			}else if(code == 87014) {
				return  false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----------------调用腾讯内容过滤系统出错------------------");
			return true;
		}
	}
	
	/**
	 * 获取request的上传文件
	 * 
	 * @param request
	 * @return
	 */
	public static List<FileItem> upFileFromRequest(HttpServletRequest request) {

		List<FileItem> items = null;
		String sContentType = request.getContentType();
		if (StringUtils.contains(sContentType, "multipart/form-data")) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				items = upload.parseRequest(request);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}

		return items;

	}

	/**
	 * 
	 * 迭代删除文件夹
	 * 
	 * @param dirPath
	 *            文件夹路径
	 * 
	 */

	public static boolean deleteDir(String dirPath) {
		boolean flag = false;
		File file = new File(dirPath);// 读取
		if (file.isFile()) {// 判断是否是文件夹
			flag = file.delete();// 删除
		} else {
			File[] files = file.listFiles(); // 获取文件
			if (files == null) {
				flag = file.delete();// 删除
			} else {
				for (int i = 0; i < files.length; i++) {
					deleteDir(files[i].getAbsolutePath());
				}
				flag = file.delete();// 删除
			}
		}
		return flag;
	}

	/**
	 * 强制删除文件
	 * 
	 * @param file
	 */
	public static void deleteFileOrDirectory(File file) {
		if (null != file) {
			if (!file.exists()) {
				return;
			}
			int i;
			// file 是文件
			if (file.isFile()) {
				boolean result = file.delete();
				// 限制循环次数，避免死循环
				for (i = 0; !result && i++ < 10; result = file.delete()) {
					// 垃圾回收
					System.gc();
					result = file.delete();
					if(result) {
						return;
					}
				}
				return;
			}
			// file 是目录
			File[] files = file.listFiles();
			if (null != files) {
				for (i = 0; i < files.length; ++i) {
					deleteFileOrDirectory(files[i]);
				}
			}
			file.delete();
		}

	}

	/**
	 * 
	 * @param filePath
	 * @param path
	 * @param l
	 *            时长
	 * @return
	 */
	public static String cut(String filePath, String path, long lo, String ext) {
		File file = new File(filePath);
		Encoder encoder = new Encoder();
		long l = 0l;
		try {
			it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(file);
			l = m.getDuration();// 时长
		} catch (EncoderException e1) {
			e1.printStackTrace();
		}
		if (l <= lo) {
			return filePath;
		}
		long size = file.length();
		long lon = file.length() * lo / l;// 需要截取文件大小
		String name = "";
		try {
			RandomAccessFile raf1 = new RandomAccessFile(file, "r");

			byte[] bytes = new byte[1024];// 值设置越小，则各个文件的字节数越接近平均值，但效率会降低，这里折中，取1024
			int len = -1;
			name = path + UUID.randomUUID().toString().replaceAll("-", "") + ext;// 生成新文件路径
			File file2 = new File(name);
			RandomAccessFile raf2 = new RandomAccessFile(file2, "rw");
			while ((len = raf1.read(bytes)) != -1) {// 读到文件末尾时，len返回-1，结束循环
				raf2.write(bytes, 0, len);
				long l2 = raf2.length();
				if (l2 > lon)// 当生成的新文件字节数大于lon时，结束循环
					break;
			}
			raf2.close();
			raf1.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;

	}

	// 合并文件
	public static void merge(String path) {
		File file = new File(path);
		try {
			RandomAccessFile target = new RandomAccessFile(file, "rw");
			for (int i = 0; i < 10; i++) {
				File file2 = new File("G:\\test2\\source" + i + ".avi");
				RandomAccessFile src = new RandomAccessFile(file2, "r");
				byte[] bytes = new byte[1024];// 每次读取字节数
				int len = -1;
				while ((len = src.read(bytes)) != -1) {
					target.write(bytes, 0, len);// 循环赋值
				}
				src.close();
			}
			target.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
