package com.lym.twogoods.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	        String regExp = "^[\u4e00-\u9fa5a-zA-Z0-9]{4,15}$";  
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
	  
	  
	    
}  