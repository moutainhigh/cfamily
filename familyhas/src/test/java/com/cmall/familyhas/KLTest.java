package com.cmall.familyhas;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import com.srnpr.zapcom.topdo.TopTest;
import com.srnpr.zapweb.helper.WebHelper;


public class KLTest extends TopTest {

	   private BufferedImage image;
	    private int screenWidth = 2250;  //屏幕的宽度
	   // private int screenHeight = 4002; //屏幕的高度
	   // private double pixelRatio = 3; //屏幕的高度
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
	    
	    //商品名称
	    private String proName = "测试商品名称哈哈哈哈哈哈哈哈哈试商品名称哈哈哈哈哈哈哈哈哈试商品名称哈哈哈哈哈哈哈哈哈";
	    
	    
	    ByteArrayInputStream byteArrayInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	    //生成图片文件
	    @SuppressWarnings("restriction")
	    public void createImage(String fileLocation) {
	        BufferedOutputStream bos = null;
	        if(image != null){
	        	   try {
	                   
	        		   File w2 = new File(fileLocation);
	        		   ImageIO.write(image,  "jpg" , w2); 
	                   ImageIO.write(image,  "jpg" , byteArrayOutputStream); 
	                   image.flush();
	                   
	                   byte[] bytes = byteArrayOutputStream.toByteArray();
	                   byteArrayInputStream = new ByteArrayInputStream(bytes);
	                   
	               } catch (Exception e) {
	                  e.printStackTrace();
	               }finally{
	                   if (byteArrayOutputStream!=null) {
	                       try {
	                           byteArrayOutputStream.close();
	                       } catch (IOException ex) {
	                    	   ex.printStackTrace();
	                       }
	                   }
	                   if (byteArrayInputStream!=null) {
	                       try {
	                           byteArrayInputStream.close();
	                       } catch (IOException ex) {
	                    	   ex.printStackTrace();
	                       }
	                   }

	               }
	        	   }
	    }
	 
	    public void graphicsGeneration(String name, String id, String classname, String imgurl) {
	        
	        
	        image = new BufferedImage(screenWidth, contentHeight, BufferedImage.TYPE_INT_RGB);
	        
	        
	        //设置图片的背景色
	        Graphics2D main = image.createGraphics();
	        main.setColor(Color.white);
	        main.drawRoundRect((int)(screenWidth*0.04), (int)(screenWidth*0.01),screenWidth, contentHeight, 20, 20);
	        main.fillRect(0, 0,screenWidth, contentHeight);
	         
	        
	 
	        //描绘商品主图
	        Graphics mainPic = image.createGraphics();
	        BufferedImage bimg = null;
	        try {
	           bimg = javax.imageio.ImageIO.read(new java.io.File(imgurl));
	           if(bimg!=null){
		            mainPic.drawImage(bimg, pageX, mainImgY, mainImgWidth, mainImgWidth, null);
		            mainPic.dispose();
		        }
	        } catch (IOException e) {
	        	e.printStackTrace();
	        	LogFactory.getLog("读取商品主图失败");
	        }
	        
	       //字体设置
	        Graphics proNameG = image.createGraphics();
	       // main.setFont(new Font("宋体",  Font.SANS_SERIF, 18));	  //这种字体设置不了      
	       Font proNameFont = new Font("黑体",  Font.PLAIN,18*6);
	       proNameG.setFont(proNameFont);
	       proNameG.setColor(new Color(34, 34, 34));
	       String[] split = proName.split(""); //这个方法是将一个字符串分割成字符串数组
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
	      	 if(StringUtils.isNotBlank("秒杀价")) {
	           Graphics2D activeTag = image.createGraphics();
	 	       // main.setFont(new Font("宋体",  Font.SANS_SERIF, 13));	  //这种字体设置不了      
	 	       Font tagFont = new Font("微软雅黑",  Font.PLAIN, (int)(13*6));
	 	       
	 	       activeTag.setColor(new Color(255,80,80));
	 	       int tagWidth = activeTag.getFontMetrics(tagFont).stringWidth("秒杀价");
	 	       activeTag.fillRect(pageX, tagY, tagWidth+10*6, 20*6);
	 	       activeTag.setColor(Color.white);
	 	       activeTag.setFont(tagFont);
	 	       activeTag.drawString("秒杀价", pageX+5*6,tagY+15*6);
	        }
	        //活动价
	        if(StringUtils.isNotBlank("99")) {
	        	Graphics2D priceTag = image.createGraphics();
	        	String priceTagstr = "￥";
	        	Font tagFont = new Font("微软雅黑",  Font.BOLD, (int)(13*6));
	        	priceTag.setColor(new Color(255,97,0));
	        	priceTag.setFont(tagFont);
	        	priceTag.drawString(priceTagstr, pageX,activePriceY);
	        	
	        	Graphics2D activetePrice = image.createGraphics();
	        	String activetePricestr = "99";
	        	Font priceFont = new Font("微软雅黑",  Font.PLAIN, (int)(30*6));
	        	activetePrice.setColor(new Color(255,97,0));
	        	activetePrice.setFont(priceFont);
	        	activetePrice.drawString( activetePricestr, pageX+15*6,activePriceY);
	        	
	        }
	        
	        //活动结束价格
	            if(StringUtils.isNotBlank("199")) {
	          String endPriceStr = "活动结束价:";
	          Graphics2D endPrice = image.createGraphics();
	 	      Font font = new Font("微软雅黑",  Font.PLAIN, (int)(13*6));
	 	      int tagWidth = endPrice.getFontMetrics(font).stringWidth(endPriceStr);
	 	      endPrice.setColor(new Color(153,153,153));
	 	      endPrice.setFont(font);
	 	      endPrice.drawString(endPriceStr, pageX,endPriceY);
	          
	 	      String ePrice = "￥199";
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
	        	qrImg = javax.imageio.ImageIO.read(new java.io.File("C:\\Users\\Administrator\\Desktop\\13.png"));
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
	 	      	

	        String imgName =   WebHelper.upUuid();
	        imgName=imgName.toString()+".png";
	        createImage("C:\\Users\\Administrator\\Desktop\\zhangbo"+imgName);
	         
	    }
	
	// 获取京东接口全部商品清单
	@Test
	public void test() {

	        try {
	            graphicsGeneration("ewew", "1", "12", "C:\\Users\\Administrator\\Desktop\\3cbdfae635d140cc803b446c15529108.png");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }


	}

}
