package com.lym.twogoods.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 登录注册信息校验器
 * 
 * @author 麦灿标
 *
 */
public class LoginValidator {

    public static boolean isPhoneNumber(String phone) {  
        if (TextUtils.isEmpty(phone)) {  
            return false;  
        }  
        String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";  
        Pattern p = Pattern.compile(regExp); 
        Matcher m = p.matcher(phone);  
        return m.find();  
    }  
  
    
    public static boolean isErHuoNumber(String str) {  
        if (TextUtils.isEmpty(str)) {  
            return false;  
        }  
        String regExp = "^[\u4e00-\u9fa5a-zA-Z0-9]{4,10}$";  
        Pattern p = Pattern.compile(regExp); 
        Matcher m = p.matcher(str);  
        return m.find();  
    } 
    
    //MD5
    public static boolean isPassword(String pswd) {  
        if (TextUtils.isEmpty(pswd)) {  
            return false;  
        }
        return true;
//        String regExp = "^[a-zA-Z0-9]{6,15}$";  
//        Pattern p = Pattern.compile(regExp); 
//        Matcher m = p.matcher(pswd);  
//        return m.find();  
    }  
  
    public static boolean isSecurityCode(String pswd) {  
        if (TextUtils.isEmpty(pswd)) {  
            return false;  
        }  
        String regExp = "^[a-zA-Z0-9]{6}$";  
        Pattern p = Pattern.compile(regExp); 
        Matcher m = p.matcher(pswd);  
        return m.find();  
    }  
}
