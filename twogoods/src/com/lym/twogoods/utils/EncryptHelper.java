package com.lym.twogoods.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 与加密操作相关的工具类
 * 
 * @author yao
 *
 * */
public class EncryptHelper {
	
	/**
	 * 对字符进行MD5加密
	 * 
	 * @param val 被加密的字符
	 * @return 加密后的结果
	 * @throws 
	 * 
	 * @author yao
	 */
	public static String getMD5(String val) {  
		if(StringUtil.isEmpty(val))
			return null;
        MessageDigest md5;
        byte[] m;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(val.getBytes());  
	        m = md5.digest();//加密  
		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成的 catch 块
			System.out.println("NoSuchAlgorithmException at EncryptHelper");
			return null;
		}  
        return StringUtil.byte2String(m);  
	}  
	
	
}
