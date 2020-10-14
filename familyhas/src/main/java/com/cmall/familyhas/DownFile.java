package com.cmall.familyhas;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class DownFile {

	public static void main(String[] args) {
		// String small_seller_codes =
		// "'SF03100702','SF03100637','SF03100678','SF03100678','SF03100593','SF03100595','SF03100618','SF03100342','SF03100414','SF03100503','SF03100507','SF03100759','SF03100668','SF03100119','SF03100671','SF03100468','SF03100083','SF03100167','SF03100582','SF03100671','SF03100582','SF03100434','SF03100630','SF03100119','SF03100074','SF03100214','SF03100074','SF03100074','SF03100610','SF03100610','SF03100364','SF03100597','SF03100074','SF03100317','SF03150827100001','SF03150827100001','SF03150827100001','SF03150827100001','SF03150827100001','SF03100737','SF03100613','SF03100317','SF03100614','SF03100317','SF03100712','SF03100724','SF03100694','SF03100584','SF03100706','SF03100087','SF03100448','SF03100064','SF03100758','SF03100336','SF03100759','SF03100647','SF03100087','SF03100119','SF03150819100001','SF03100663','SF03100243','SF03100535','SF03100504','SF03100496','SF03150819100001','SF03100526','SF03100139','SF03100281','SF03100282','SF03100762','SF03100685','SF03100686'";
		String small_seller_codes = "'SF03100519','SF03100326'";
		List<Map<String, Object>> list = DbUp.upTable("pc_qualification_info").dataSqlList(
				"SELECT seller_qualification_code,small_seller_code,qualification_name,qualification_pic FROM productcenter.pc_qualification_info WHERE small_seller_code IN ("
						+ small_seller_codes + ")",
				new MDataMap());
		String path = "D://qualification/";
		for (Map<String, Object> map : list) {
			MDataMap info = new MDataMap(map);
			String seller_qualification_code = info.get("seller_qualification_code");
			String small_seller_code = info.get("small_seller_code");
			String qualification_name = info.get("qualification_name");
			String[] qualification_pics = info.get("qualification_pic").split("\\|");
			URL urlfile = null;
			HttpURLConnection httpUrl = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			if (qualification_pics.length > 0) {
				for (int i = 0; i < qualification_pics.length; i++) {
					String url = qualification_pics[i];
					String type = url.substring(url.lastIndexOf(".") + 1, url.length());
					File file = new File(path + small_seller_code);
					if (!file.exists()) {
						file.mkdirs();
					}
					File f = new File(path + small_seller_code + "/" + seller_qualification_code + qualification_name+"_"+i
							+ "." + type);
					try {
						urlfile = new URL(url);
						httpUrl = (HttpURLConnection) urlfile.openConnection();
						httpUrl.connect();
						bis = new BufferedInputStream(httpUrl.getInputStream());
						bos = new BufferedOutputStream(new FileOutputStream(f));
						int len = 2048;
						byte[] b = new byte[len];
						while ((len = bis.read(b)) != -1) {
							bos.write(b, 0, len);
						}
						bos.flush();
						bis.close();
						httpUrl.disconnect();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							bis.close();
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}

		// String url =
		// "http://image.sycdn.ichsy.com/cfiles/staticfiles/upload/27562/af233c3f264e490e8042b0856d2da59e.png";
		// System.out.println(url.split("\\|")[0]);
	}
}
