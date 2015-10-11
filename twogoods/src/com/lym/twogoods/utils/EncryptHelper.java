package com.lym.twogoods.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptHelper {
	
	/**
	 * 对字符进行加密
	 * @param val 被加密的字符
	 * @return 加密后的结果
	 * @throws NoSuchAlgorithmException
	 * 
	 * @author yao
	 */
	public static String getMD5(String val) throws NoSuchAlgorithmException{  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        md5.update(val.getBytes());  
        byte[] m = md5.digest();//加密  
        return getString(m);  
	}  
	
	/**
	 * 将加密后得到的字节数组转化为String 
	 * @param b
	 * @return
	 * 
	 * @author yao
	 */
    private static String getString(byte[] b){  
        StringBuffer sb = new StringBuffer();  
         for(int i = 0; i < b.length; i ++){  
          sb.append(b[i]);  
         }  
         return sb.toString();  
}  
}
