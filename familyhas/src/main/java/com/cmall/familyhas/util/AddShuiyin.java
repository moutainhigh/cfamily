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


public class AddShuiyin {
	/**
	   * 给图片不同位置添加多个图片水印、可设置水印图片旋转角度
	   * @param icon 水印图片路径（如：F:/images/icon.png）
	   * @param source 没有加水印的源图片链接
	   * @param imageType 图片类型（如：jpg）
	   * @param degree 水印图片旋转角度，为null表示不旋转
	   */
	  public static byte[] markImageByMoreIcon(InputStream icon,String source,String imageType,Integer degree) {
	    byte[] result = null;
	    try {
	      //将icon加载到内存中
	      Image ic = ImageIO.read(icon);
	      //icon高度
	      int icheight = ic.getHeight(null);
	      //将源图片读到内存中
	      Image img = ImageIO.read(new URL(source));
	      //图片宽
	      int width = img.getWidth(null);
	      //图片高
	      int height = img.getHeight(null);
	      BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	      //创建一个指定 BufferedImage 的 Graphics2D 对象
	      Graphics2D g = bi.createGraphics();
	      //x,y轴默认是从0坐标开始
	      int x = 0;
	      int y = 0;
	      //默认两张水印图片的间隔高度是水印图片的1/3
	      int temp = icheight/3;
	      int space = 1;
	      if(height>=icheight){
	      space = height/icheight;
	      if(space>=2){
	      temp = y = icheight/2;
	      if(space==1||space==0){
	      x = 0;
	      y = 0;
	      }
	      }
	      }else{
	      x = 0;
	      y = 0;
	      }
	      //设置对线段的锯齿状边缘处理
	      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	      //呈现一个图像，在绘制前进行从图像空间到用户空间的转换
	      g.drawImage(img.getScaledInstance(width,height,Image.SCALE_SMOOTH),0,0,null);
	      for(int i=0;i<space;i++){
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
	      float clarity = 0.6f;
	      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,clarity));
	      //表示水印图片的坐标位置(x,y)
	      //g.drawImage(con, 300, 220, null);
	      g.drawImage(con, x, y, null);
	      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	      y+=(icheight+temp);
	      }
	      g.dispose();
	    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
	    ImageIO.write(bi, imageType, arrayOutputStream); // 保存图片
	      result = arrayOutputStream.toByteArray();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }
	  public static byte[] markImageByMoreIconForMusicAlbum(InputStream icon,String imageType,Integer degree,String originUrl,int width,int height,boolean flag) {
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

		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    return result;
		  }

}
