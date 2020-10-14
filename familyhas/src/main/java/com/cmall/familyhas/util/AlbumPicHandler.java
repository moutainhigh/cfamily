package com.cmall.familyhas.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webmethod.WebUpload;
import com.srnpr.zapweb.webmodel.MWebResult;


public class AlbumPicHandler implements Runnable{

	   private InputStream icon;
	   private String imageType;
	   private Integer degree;
	   private String originUrl;
	   private int width;
	   private int height;
	   boolean flag;
	   private String oldPicUrl;
	   private String id1;
	   private String id2;
	   private String fieldName;
	   
	   public AlbumPicHandler(InputStream icon,String imageType,Integer degree,String originUrl,int width,int height,boolean flag,String oldPicUrl,String id1,String id2,String fieldName) {
			this.icon = icon;
			this.imageType = imageType;
			this.degree = degree;
			this.originUrl = originUrl;
			this.width = width;
			this.height = height;
			this.flag = flag;
			this.oldPicUrl = oldPicUrl;
			this.id1 = id1;
			this.id2 = id2;
			this.fieldName = fieldName;
		}
	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		    byte[] result = null;
		    try {	    	
		    	
		      //将icon加载到内存中
		      Image ic = ImageIO.read(icon);
		      //icon高度
		      int icheight = ic.getHeight(null);
		      int icWidth = ic.getWidth(null);
		      //对上传的图片做旋转校验处理
		      URL url = new URL(originUrl);
		     //利用HttpURLConnection对象,我们可以从网络中获取网页数据.
		      HttpURLConnection oConn = (HttpURLConnection) url.openConnection();
			  oConn.setDoInput(true);
			  oConn.connect();
			  InputStream temIS = oConn.getInputStream();
		      Metadata readMetadata = ImageMetadataReader.readMetadata(temIS);	 
		      ExifIFD0Directory db = readMetadata.getFirstDirectoryOfType(ExifIFD0Directory.class);	  
		      //获取exif旋转信息  
		      int orientation=1;
		      if(db!=null&&db.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
		    	  orientation=db.getInt(ExifIFD0Directory.TAG_ORIENTATION);
		      }
		    
		      Integer turn=360;  
		      //确定旋转度数  
		      if(orientation==0||orientation==1) {
		    	  turn=360;  
		      } else if(orientation==3) {  
		          turn=180;  
		      } else if(orientation==6) {  
		          turn=90;  
		      } else if(orientation==8) {  
		          turn=270;  
		      } 

		      //将源图片读到内存中
		      Image img = ImageIO.read(url);
		      //对高度做重新审计
		      if(!flag&&(orientation==1||orientation==0)) {
		    	  int tranWidth = img.getWidth(null);
				  int tranHeight = img.getHeight(null);
				  double dScale = Double.parseDouble(String.valueOf(width)) / tranWidth;
		    	  height = (int) (dScale*tranHeight);
		      }
		      else if(!flag) {
					 //旋转之后的高宽互调
					 int tranWidth = img.getHeight(null);
					 int tranHeight = img.getWidth(null);
				     double dScale = Double.parseDouble(String.valueOf(width)) / tranWidth;
				     height = (int) (tranHeight*dScale); 
		      }
		      BufferedImage newImg = RotateImage.Rotate(img, turn); 
		      BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		      //创建一个指定 BufferedImage 的 Graphics2D 对象
		      Graphics2D g = bi.createGraphics();
		      //x,y轴默认是从0坐标开始
		      int x = width/2-(icheight/2);
		      int y = height/2-(icWidth/2);
		      //设置对线段的锯齿状边缘处理
		      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		      //呈现一个图像，在绘制前进行从图像空间到用户空间的转换
		      g.drawImage(newImg.getScaledInstance(width,height,Image.SCALE_SMOOTH),0,0,null);
		      if (null != degree) {
		        //设置水印旋转
		        g.rotate(Math.toRadians(degree),(double) bi.getWidth() / 2, (double) bi.getHeight() / 2);
		      }
		      //水印图象的路径 水印一般为gif或者png的，这样可设置透明度
		      //ImageIcon imgIcon = new ImageIcon(icon);
		      //得到Image对象。
		      //Image con = imgIcon.getImage();
		      Image con = ic;
		      //透明度，最小值为0，最大值为1
		      float clarity = 0f;
		      if(flag) {
		    	  clarity = 1f;  
		      }
		      else {
		    	  clarity = 0f;
		      }
		      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,clarity));
		      //表示水印图片的坐标位置(x,y)
		      //g.drawImage(con, 300, 220, null);
		      g.drawImage(con, x, y, null);
		      g.dispose();
		     ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		     ImageIO.write(bi, imageType, arrayOutputStream); // 保存图片
		     result = arrayOutputStream.toByteArray();
		     String sFileName = WebHelper.upUuid();
		     MWebResult sRemoteUpload =WebUpload.create().remoteUpload("upload", sFileName+oldPicUrl.substring(oldPicUrl.lastIndexOf(".")), result);
		     String picUrl =sRemoteUpload.getResultObject().toString();
		     DbUp.upTable("hp_music_album_img").dataUpdate(new MDataMap("uid",id1,fieldName,picUrl), fieldName, "uid");
		     DbUp.upTable("hp_music_album_user_pic").dataUpdate(new MDataMap("uid",id2,fieldName,picUrl), fieldName, "uid");
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	    }


}
