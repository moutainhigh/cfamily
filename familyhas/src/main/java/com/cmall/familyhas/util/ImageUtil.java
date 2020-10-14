/*package com.cmall.familyhas.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.cmall.familyhas.model.ImageWordsContent;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;


public class ImageUtil {
	 // 宽度
    private static final int WIDTH = 450;
    // 高度
    private static final int HEIGHT = 400;
    // 双字节字符字体大小
    private static final int DOUBLE_FONT_SIZE = 15;
    // 单字节字符字体大小
    private static final int SINGLE_FONT_SIZE = 8;
    // 行间距
    private static final int LINE_HEIGHT = 30;
    // 水印透明度
    private static float alpha = 1.0f;
    // 水印横向位置
    private static int positionWidth = 53;
    // 水印纵向位置
    private static int positionHeight = 317;
    // 水印文字字体
    private static Font font = new Font("宋体", Font.BOLD, 115);
    // 水印文字颜色
    private static Color color = new Color(230, 230, 230);
    *//**
     * 生成图片
     * @throws IOException
     *//*
    public static void createImage(ImageWordsContent imageContent,int width ,int height) throws IOException {
        OutputStream os = null;
        width=1330;
        height=750;
        
        // 1.创建空白图片
        BufferedImage image = new BufferedImage(
        		width, height, BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics graphic = image.getGraphics();
        // 3.设置画笔颜色
        graphic.setColor(Color.white);
        // 4.绘制矩形背景
        graphic.fillRect(0, 0, width, height);
        // 5.绘制矩形边框
        graphic.setColor(Color.lightGray);
        graphic.drawRect(0, 0, WIDTH-10, HEIGHT-10);
        //strList：字符串数组，里面存储的是需要显示的文字，每个元素换行显示
        //sizes：长度数组，对应上面的字符串数组，对于每个字符串，记录标题长度，需要加粗显示
        //记录文字行数
        int wordRow = 0;
        int sumLength = 0;
        String regex = "[^\\x00-\\xff]";//匹配双字节字符
        Matcher matcher = null;
        Pattern pattern = Pattern.compile(regex);
        boolean rs = false;//记录当前字符的匹配情况
        boolean flag = false;//记录前一字符的匹配情况
        for(int j = 0; j<strList.length; j++){
        	wordRow = wordRow + 1;
            sumLength = 0;//记录当前字符的横坐标位置
            String stroutput = strList[j];//每个字符串元素
            int size = stroutput.length();//每个字符串数组元素的长度
            for(int num = 0; num < size; num ++){
                String schar = stroutput.charAt(num) + "";
                matcher = pattern.matcher(schar);
                // 字符串是否与正则表达式相匹配
                rs = matcher.matches();
                if(num < sizes[j]) {    //标题显示格式
                    // 设置颜色
                    graphic.setColor(Color.black);
                    // 设置字体大小，标题需要加粗显示
                    graphic.setFont(new Font(null, Font.BOLD, DOUBLE_FONT_SIZE));
                }else {                 //正文显示格式
                    // 设置颜色
                    graphic.setColor(Color.darkGray);
                    // 设置字体大小，正文普通字体显示
                    graphic.setFont(new Font(null, Font.PLAIN, DOUBLE_FONT_SIZE));
                }
                *//**
                 * 单字节字符，正常情况下应该是占用8个位置的，但是如果前面1个字符是双字节字符的话，
                 * 如果加8的话，会和前面的字符重叠，因为字符本身是要占用空间的，sumLength只是横坐标的位置，
                 * 而并没有考虑占位的问题，所以，如果前面是双字节字符的话，横坐标应该加15；
                 * 前面如果是单字节字符，加8就可以了。
                 *//*
                if(rs){
                    //双字节字符
                    sumLength = sumLength + DOUBLE_FONT_SIZE;
                }else{
                    //单字节字符
                    if(flag){
                        //前一个字符是双字节字符
                        sumLength = sumLength + DOUBLE_FONT_SIZE;
                    }else{
                        sumLength = sumLength + SINGLE_FONT_SIZE;
                    }
                }
                flag = rs;
                if(sumLength > WIDTH - DOUBLE_FONT_SIZE*2){
                    //每一行的前后都要留有一定空白，横坐标已经超出长度，需要折行
                    height = height + 1;
                    if(rs){ //前面是双字节字符，横坐标设置为15
                        sumLength = DOUBLE_FONT_SIZE;
                    }else{  //前面是单字节字符，横坐标设置为8
                        sumLength = SINGLE_FONT_SIZE;
                    }
                }
                // 画字符
                graphic.drawString(schar, sumLength, height*LINE_HEIGHT);
            }
        }
        // 6.返回图片
        os = new FileOutputStream(targetPath);
        ImageIO.write(image, "JPG", os);
        os.close();

        System.out.println("返回图片。。。。。。");
    }

    //添加文字水印
    public static void markImageByText(String logoText, String srcImgPath,
                                       String targerPath, Integer degree) {

        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),(double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(font);
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            g.drawString(logoText, positionWidth, positionHeight);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "JPG", os);

            System.out.println("图片完成添加水印文字");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //添加图片水印
    public static void markImageByIcon(String iconPath, String srcImgPath,
                                       String targerPath, Integer degree) {
        OutputStream os = null;
        try {
            Image srcImg = ImageIO.read(new File(srcImgPath));

            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);

            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2, (double) buffImg
                                .getHeight() / 2);
            }

            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(iconPath);

            // 得到Image对象。
            Image img = imgIcon.getImage();


            //float alpha = 0.1f; // 透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));

            // 表示水印图片的位置
            g.drawImage(img, positionWidth-1, positionHeight-1, null);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

            g.dispose();

            os = new FileOutputStream(targerPath);

            // 生成图片
            ImageIO.write(buffImg, "JPG", os);

            System.out.println("图片完成添加Icon印章。。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
*/