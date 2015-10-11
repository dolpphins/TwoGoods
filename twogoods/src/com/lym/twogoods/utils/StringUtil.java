package com.lym.twogoods.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {  
	  
	  /**
	   *  判断一个字符串是否为空.  </p>
	   *  
	   * @param str 被判断的字符
	   * 
	   * @return 如果是空，返回true，否则返回false
	   * 
	   * @author yao
	   */
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
	  
	    
	}  