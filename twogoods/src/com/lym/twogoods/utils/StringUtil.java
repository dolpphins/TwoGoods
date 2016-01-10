package com.lym.twogoods.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;


/**
 * 与String操作相关的工具类
 * 
 * @author yao
 *
 * */
public class StringUtil {  
	  
	  /**
	   *  判断一个字符串是否为空.  </p>
	   *  
	   * @param str 被判断的字符
	   * 
	   * @return 如果是空，返回true，否则返回false
	   * 
	   * @author yao
	   **/
	    public static boolean isEmpty(String str) {  
	        if (str == null || "null".equals(str) || str.length() == 0) {  
	            return true;  
	        } else {  
	            return false;  
	        }  
	    }  
	  
	    /**
	     *  判断一个字符串是否为电话号码.  </p>
	     *  
	     * @param phone 被判断的字符
	     * 
	     * @return 如果是正确的电话号码，返回true，否则返回false
	     * 
	     * @author yao
	     */
	    public static boolean isPhoneNumber(String phone) {  
	        if (isEmpty(phone)) {  
	            return false;  
	        }  
	        String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";  
	        Pattern p = Pattern.compile(regExp); 
	        Matcher m = p.matcher(phone);  
	        return m.find();  
	    }  
	  
	    
	    /**
	     *  判断一个字符串是否为合格的贰货号.  </p>
	     *  
	     * @param str 被判断的字符
	     * 
	     * @return 如果是合格的贰货号，返回true，否则返回false
	     * 
	     * @author yao
	     */
	    public static boolean isErHuoNumber(String str) {  
	        if (isEmpty(str)) {  
	            return false;  
	        }  
	        String regExp = "^[\u4e00-\u9fa5a-zA-Z0-9]{4,10}$";//最大长度修改为10  
	        Pattern p = Pattern.compile(regExp); 
	        Matcher m = p.matcher(str);  
	        return m.find();  
	    } 
	    
	    /**
	     *  判断一个字符串是否为合格的密码.  </p>
	     *  
	     * @param pswd 被判断的字符
	     * 
	     * @return 如果是合格的，返回true，否则返回false
	     * 
	     * @author yao
	     */
	    public static boolean isPassword(String pswd) {  
	        if (isEmpty(pswd)) {  
	            return false;  
	        }  
	        String regExp = "^[a-zA-Z0-9]{6,15}$";  
	        Pattern p = Pattern.compile(regExp); 
	        Matcher m = p.matcher(pswd);  
	        return m.find();  
	    }  
	  
	    
	    /**
	     *  判断一个字符串是否为合格的验证码.验证码是六位字母或者数字  </p>
	     *  
	     * @param pswd 被判断的字符
	     * 
	     * @return 如果是合格的验证码，返回true，否则返回false
	     * 
	     * @author yao
	     */
	    public static boolean isSecurityCode(String pswd) {  
	        if (isEmpty(pswd)) {  
	            return false;  
	        }  
	        String regExp = "^[a-zA-Z0-9]{6}$";  
	        Pattern p = Pattern.compile(regExp); 
	        Matcher m = p.matcher(pswd);  
	        return m.find();  
	    }  
	  
	  
	  /**
	   * 将输入流转化为String 
	   * 
	   * @param is 输入流
	   * 
	   * @return
	   * 
	   * @author yao
	   */
	    public static String inputStream2String(InputStream is) {  
	        BufferedReader in = new BufferedReader(new InputStreamReader(is));  
	        StringBuffer buffer = new StringBuffer();  
	        String line = "";  
	        try {  
	            while ((line = in.readLine()) != null) {  
	                buffer.append(line);  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return buffer.toString();  
	    }  
	    
	    
	    /**
		 * 将byte数组转化为String 
		 * @param b
		 * @return
		 * 
		 * @author yao
		 */
	    public static String byte2String(byte[] b){  
	        StringBuffer sb = new StringBuffer();  
	         for(int i = 0; i < b.length; i ++){  
	          sb.append(b[i]);  
	         }  
	         return sb.toString();  
	    }  
	    
	    /**
		 * 计算byte数组 有多少KB
		 * @param b
		 * @return Byte数组的大小，单位为KB
		 * 
		 * @author yao
		 * */
	    public static String byte2KB(byte[] b){ 
	    	//java.text包里面的DecimalFormat 类 
	    	double d = 1024;
	    	
	    	DecimalFormat format = new DecimalFormat("#0.0"); 
	    	String buf = format.format(((double)b.length)/d).toString();  
	        return buf+"KB";
	    } 
	    
	    /**
		 * 计算byte数组 有多少MB
		 * @param b
		 * @return Byte数组的大小，单位为MB
		 * 
		 * @author yao
		 * */
	    public static String byte2MB(byte[] b){  
	    	//java.text包里面的DecimalFormat 类 
	    	double d = 1024*1024;
	    	
	    	DecimalFormat format = new DecimalFormat("#0.0"); 
	    	String buf = format.format(((double)b.length)/d).toString();  	         
	        return buf+"MB";
	    }  
	    
	    /**
		 * 计算byte数组的大小是多少GB
		 * @param b
		 * @return Byte数组的大小，单位为GB
		 * 
		 * @author yao
		 * */
	    public static String byte2GB(byte[] b){  
	    	//java.text包里面的DecimalFormat 类 
	    	double d = 1024*1024*1024;
	    	
	    	DecimalFormat format = new DecimalFormat("#0.0"); 
	    	String buf = format.format(((double)b.length)/d).toString();  	         
	        return buf+"MB";
	    }  
	  
	    /**
	     * 将字节大小转换为符合正常习惯的单位字符串
	     * 
	     * @param size 注意不能小于0
	     * @return 返回相应的字符串,如果size小于0件返回null.
	     * 
	     * @author 麦灿标
	     */
	    public static String byteSize2String(long size) {
	    	if(size < 0) {
	    		return null;
	    	//小于1KB
	    	} else if(size < 1024) {
	    		return size + "B";
	    	} else { 
	    		DecimalFormat f = new DecimalFormat("#.00");
	    		long KB = 1024;
	    		long MB = 1024 * KB;
	    		long GB = 1024 * MB;
	    		long TB = 1024 * GB;
	    		//小于1MB
	    		if(size < MB) {
	    			return f.format(((double)size) / KB) + "KB";
	    		} else if(size < GB) {
	    			return f.format(((double)size) / MB) + "MB";
	    		} else if(size < TB) {
	    			return f.format(((double)size) / GB) + "GB";
	    		} else {
	    			return f.format(((double)size) / TB) + "TB";
	    		}
	    	}
	    }
	    /**
	     * <p>
	     * 		设置字体
	     * </p>
	     * @param activity
	     * @param view		要设置字体的控件
	     * @param path		字体的路径
	     */
	    public static void setTextFont(Activity activity,TextView view,String path) {
	    	Typeface tf=Typeface.createFromAsset(activity.getAssets(), path);
			view.setTypeface(tf);
		}
	    
	    /**
	     * 获取指定文本在指定字体大小下的宽度
	     * 
	     * @param text 指定的文本
	     * @param size 指定的字体大小
	     * @return 返回指定文本在指定字体大小下的宽度
	     */
	    public static float getTextLength(String text, float size) {
	        Paint paint = new Paint();
	        paint.setTextSize(size);
	        return paint.measureText(text);
	    }

	    
}  