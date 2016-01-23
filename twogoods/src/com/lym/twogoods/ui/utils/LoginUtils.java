package com.lym.twogoods.ui.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
/**
 * <p>
 * 		登录相关辅助类
 * </p>
 * @author lyw
 *
 */
public class LoginUtils {

	/**
	 *<p>
	 *		判断是否需要隐藏键盘
	 *</p>
	 * @param v		触发的view	
	 * @param event	触发的事件
	 * @return		
	 */
	public  static boolean isShouldHideInput(View v, MotionEvent event) {  
	    if (v != null && (v instanceof EditText)) {  
	        int[] leftTop = { 0, 0 };  
	        //获取输入框当前的location位置  
	        v.getLocationInWindow(leftTop);  
	        int left = leftTop[0];  
	        int top = leftTop[1];  
	        int bottom = top + v.getHeight();  
	        int right = left + v.getWidth();  
	        if (event.getX() > left && event.getX() < right  
	                && event.getY() > top && event.getY() < bottom) {  
	            // 点击的是输入框区域，保留点击EditText的事件  
	            return false;  
	        } else {  
	            return true;  
	        }  
	    }  
	    return false;  
	}
}
