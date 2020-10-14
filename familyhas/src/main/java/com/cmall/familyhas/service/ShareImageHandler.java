package com.cmall.familyhas.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;

public class ShareImageHandler  {
	
	    private BufferedImage image;
	    private int screenWidth = 2250;  //屏幕的宽度
	    // private int screenHeight = 4002; //屏幕的高度
	    private int pageX = (int)(screenWidth - (screenWidth + screenWidth * 0.906) / 2) ;
	    //内容宽高
	    //  private int  contentWidth = screenWidth;
	    private int  contentHeight = (int)(screenWidth * (0.067 + 0.906 + 0.213) + 120*6); 
	    //商品主图坐标
	    private int mainImgWidth =(int)(screenWidth * 0.906); 
	    private int mainImgY =(int)(screenWidth * 0.048); 
	    //标题Y轴位置
	    private int titlePageY = (int)(screenWidth* 0.067 + screenWidth * 0.906 + 30*6); 
	    //标签y轴位置
	    private int tagY = (int)(screenWidth * 0.067 + screenWidth * 0.906 + 85*6); 
	    //活动价y坐标
	    private int activePriceY = (int)(screenWidth * 0.067 + screenWidth * 0.906 + 150*6); 
	    //结束价格y坐标位置
	    private int endPriceY = (int)(screenWidth * 0.067 + screenWidth * 0.906 + 175*6); 
	    //二维码y坐标,宽/高,下面文字y坐标
	    private int qrCodeY  = (int)(screenWidth * 0.067 + screenWidth * 0.906 + 75*6); 
	    private int qrCodeWidth  = (int)(screenWidth * 0.213);
	    private int qrCodeHeight  =(int)(screenWidth * 0.213);
	    private int codeTipsY  =(int)(screenWidth * 0.067 + screenWidth * 0.906 + 175*6); 
	    private String prefix="%26";
	    private String suffix="%3D";
        public void getShareImage( HttpServletRequest request,HttpServletResponse response) {
	
			 String productName =request.getParameter("name");
			 String saleTag = request.getParameter("sale");
			 String edPrice = request.getParameter("endPrice");
			 String salePrice =request.getParameter("salePrice");
			 String imgUrl = request.getParameter("imgUrl");
			 String pid =request.getParameter("pid");
			 String fxrcode =StringUtils.isBlank(request.getParameter("fxrcode"))?"":request.getParameter("fxrcode");
			 String iconUrl =  TopConfig.Instance.bConfig("familyhas.product_share_icon");
			 {
				// 小程序推广赚分享参数唯一编号
			    MDataMap mDataMap = new MDataMap();
			    String shortContent = "pid="+pid+"&fc="+fxrcode+"&sfrom=";
				String shortCode = WebHelper.upCode("F");
				mDataMap.put("code", shortCode);
				mDataMap.put("content", shortContent);
				mDataMap.put("create_time", DateUtil.getNowTime());
				DbUp.upTable("fh_short_code").dataInsert(mDataMap );
				iconUrl = iconUrl+pid+prefix+shortCode;
			 }

		     image = new BufferedImage(screenWidth, contentHeight, BufferedImage.TYPE_INT_RGB);
		    
		    //设置图片的背景色
		    Graphics2D main = image.createGraphics();
		    main.setColor(Color.white);
		    main.drawRoundRect((int)(screenWidth*0.04), (int)(screenWidth*0.01),screenWidth, contentHeight, 20, 20);
		    main.fillRect(0, 0,screenWidth, contentHeight);
		
		    //描绘商品主图
		    Graphics mainPic = image.createGraphics();
		    //BufferedImage bimg = null;
		    try {
		       URL url = new URL(imgUrl);
		      // bimg=ImageIO.read(url);这种方式容易出现图片读取出现红色，放弃
	           Image image2 = Toolkit.getDefaultToolkit().getImage(url); 
	          //如果图片过大,可能会出现延迟,为了防止这种情况发生我们可以在图片完全被加载进来
	           MediaTracker mediaTracker = new MediaTracker(new Container());
	           mediaTracker.addImage(image2, 0);
	           mediaTracker.waitForID(0);
		       if(image2!=null){
		            mainPic.drawImage(image2, pageX, mainImgY, mainImgWidth, mainImgWidth, null);
		            mainPic.dispose();
		        }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	LogFactory.getLog("读取商品主图失败");
		    }
		    
		   //字体设置
		    Graphics proNameG = image.createGraphics();
		    Font proNameFont = new Font("黑体",  Font.PLAIN,18*6);
		   proNameG.setFont(proNameFont);
		   proNameG.setColor(new Color(34, 34, 34));
		   String[] split = productName.split(""); //这个方法是将一个字符串分割成字符串数组
		   String temp = "";
		   List<String> row = new ArrayList<String>();
		   //做商品名称换行处理
		    for (int a = 0; a < split.length; a++) {
		        if (proNameG.getFontMetrics(proNameFont).stringWidth(temp) < mainImgWidth) {
		          temp += split[a];
		        } else {
		          a--; //这里添加了a-- 是为了防止字符丢失，效果图中有对比
		          row.add(temp);
		          temp = "";
		        }
		      }
		    row.add(temp);
		    int size = 0;
		    //如果数组长度大于2 则截取前两个+...
		    if (row.size() > 2) {
		    	String indexContent = row.get(1).toString();
		    	indexContent =indexContent.substring(0, indexContent.length()-2)+"...";
		    	row.set(1, indexContent);
		    	size=2;
		    } 
		    else {
		    	size=row.size();
		    }
		    for (int b = 0; b < size; b++) {
		        proNameG.drawString(row.get(b), pageX, titlePageY+b * 30*6);
		      }
		    
		    
		   //活动标签
		  	 if(StringUtils.isNotBlank(saleTag)) {
		       Graphics2D activeTag = image.createGraphics();
		           Font tagFont = new Font("微软雅黑",  Font.PLAIN, (int)(13*6));
			       activeTag.setColor(new Color(255,80,80));
			       int tagWidth = activeTag.getFontMetrics(tagFont).stringWidth(saleTag);
			       activeTag.fillRect(pageX, tagY, tagWidth+10*6, 20*6);
			       activeTag.setColor(Color.white);
			       activeTag.setFont(tagFont);
			       activeTag.drawString(saleTag, pageX+5*6,tagY+15*6);
		    }
		    //活动价
		    if(StringUtils.isNotBlank(salePrice)) {
		    	Graphics2D priceTag = image.createGraphics();
		    	String priceTagstr = "￥";
		    	Font tagFont = new Font("微软雅黑",  Font.BOLD, (int)(13*6));
		    	priceTag.setColor(new Color(255,97,0));
		    	priceTag.setFont(tagFont);
		    	priceTag.drawString(priceTagstr, pageX,activePriceY);
		    	
		    	Graphics2D activetePrice = image.createGraphics();
		    	String activetePricestr = salePrice;
		    	Font priceFont = new Font("微软雅黑",  Font.PLAIN, (int)(30*6));
		    	activetePrice.setColor(new Color(255,97,0));
		    	activetePrice.setFont(priceFont);
		    	activetePrice.drawString( activetePricestr, pageX+15*6,activePriceY);
		    	
		    }
		    
		    //活动结束价格
		        if(StringUtils.isNotBlank(edPrice)) {
		      String endPriceStr = "活动结束价:";
		      Graphics2D endPrice = image.createGraphics();
		          Font font = new Font("微软雅黑",  Font.PLAIN, (int)(13*6));
			      int tagWidth = endPrice.getFontMetrics(font).stringWidth(endPriceStr);
		 	      endPrice.setColor(new Color(153,153,153));
			      endPrice.setFont(font);
			      endPrice.drawString(endPriceStr, pageX,endPriceY);
		      
			      String ePrice = "￥"+edPrice;
		          Graphics2D endPriceVal = image.createGraphics();
		          Font pfont = new Font("微软雅黑",  Font.PLAIN, (int)(13*6));
		 	      int pWidth = endPrice.getFontMetrics(pfont).stringWidth(ePrice);
		 	      int height = endPrice.getFontMetrics(pfont).getHeight();
		 	      endPriceVal.setColor(new Color(153,153,153));
			      endPriceVal.setFont(pfont);
			      endPriceVal.drawString(ePrice, pageX+tagWidth+2*6,endPriceY);
			      endPriceVal.setStroke(new BasicStroke(3.0f));
			      endPriceVal.drawLine(pageX+tagWidth+2*6, endPriceY-height/4, pageX+tagWidth+2*6+pWidth, endPriceY-height/4);
			      	
		     }
		    
			    //二维码绘制
			    Graphics  qrCode = image.getGraphics();
			    BufferedImage qrImg = null;
			    try {
			    	 URL url = new URL(iconUrl);
			    	 qrImg=ImageIO.read(url);
			       if(qrImg!=null){
			    	   qrCode.drawImage(qrImg, (int)(pageX+screenWidth*0.68), qrCodeY, qrCodeWidth, qrCodeHeight, null);
			    	   qrCode.dispose();
			        }
			    } catch (IOException e) {
			    	e.printStackTrace();
			    	LogFactory.getLog("读取二维码图失败");
			    }
		    
		     //二维码下面的文字
		      String codeTipWords = "扫描或长按小程序码";
		      Graphics2D codeTip = image.createGraphics();
		      Font pfont = new Font("微软雅黑",  Font.PLAIN, 9*6);
		      codeTip.setColor(new Color(153,153,153));
		      codeTip.setFont(pfont);
		      codeTip.drawString(codeTipWords,(int)(pageX+screenWidth*0.68+(qrCodeWidth-82.5*6)/2),codeTipsY);
			  
		      //渲染结果写出
			      try {
			    	/*String imgName =   WebHelper.upUuid();
				    imgName=imgName.toString()+".png";
			    	File w2 = new File("C:\\Users\\Administrator\\Desktop\\zhangbo"+imgName);
		   		    ImageIO.write(image,  "jpg" , w2);  */ 
			    	response.setHeader("Content-Type", "image/jpeg");
					ImageIO.write(image,  "jpg" , response.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
    
        }
	
}
