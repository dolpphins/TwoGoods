package com.lym.twogoods.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")

/**
 * 与时间操作相关的工具类
 * 
 * @author yao
 *
 * */
public class TimeUtil {
	
	public final static String FORMAT_YEAR = "yyyy";
	public final static String FORMAT_MONTH_DAY = "MM月dd日";
	
	public final static String FORMAT_DATE = "yyyy-MM-dd";
	public final static String FORMAT_TIME = "HH:mm";
	public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日  hh:mm";
	
	public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
	public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm";
	public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	private static final int YEAR = 365 * 24 * 60 * 60;// 年
	private static final int MONTH = 30 * 24 * 60 * 60;// 月
	private static final int DAY = 24 * 60 * 60;// 天
	private static final int HOUR = 60 * 60;// 小时
	private static final int MINUTE = 60;// 分钟

	/**
	 * 根据时间戳获取描述性时间，如3分钟前，1天前
	 * 
	 * @param timestamp
	 *            时间戳 的时间单位为毫秒
	 * @return 时间字符串
	 * 
	 * @author yao
	 * 
	 */
	public static String getDescriptionTimeFromTimestamp(long timestamp) {
		long currentTime = System.currentTimeMillis();
		long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
		String timeStr = null;
		if (timeGap > YEAR) {
			timeStr = timeGap / YEAR + "年前";
		} else if (timeGap > MONTH) {
			timeStr = timeGap / MONTH + "个月前";
		} else if (timeGap > DAY) {// 1天以上
			timeStr = timeGap / DAY + "天前";
		} else if (timeGap > HOUR) {// 1小时-24小时
			timeStr = timeGap / HOUR + "小时前";
		} else if (timeGap > MINUTE) {// 1分钟-59分钟
			timeStr = timeGap / MINUTE + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}

	/**
	 * 获取当前日期的指定格式的字符串
	 * 
	 * @param format
	 *            指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
	 * @return
	 * 
	 * @author yao
	 */
	public static String getCurrentTime(String format) {
		if (format == null || format.trim().equals("")) {
			sdf.applyPattern(FORMAT_DATE_TIME);
		} else {
			sdf.applyPattern(format);
		}
		return sdf.format(new Date());
	}

	/**
	 * Date转换为String类型
	 * @param date 要转换的Date
	 * @param formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	 * @return String
	 * 
	 * @author yao
	 */
 	public static String dateToString(Date data, String formatType) {
 		if(formatType==null)
 			formatType = FORMAT_DATE_TIME;
 		return new SimpleDateFormat(formatType).format(data);
 	}
 	
 	/**
	 * long转换为String类型
	 * @param currentTime 要转换的long类型时间
	 * @param formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	 * @return Date
	 * 
	 * @author yao
	 */
 
 	public static String longToString(long currentTime, String formatType){
 		String strTime="";
		Date date = longToDate(currentTime, formatType);// long类型转成Date类型
		strTime = dateToString(date, formatType); // date类型转成String 
 		return strTime;
 	}
 
 	/**
	 * String转换为Date类型
	 * @param strTime 要转换的String
	 * @param formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	 * @return Date
	 * 
	 * @author yao
	 */
 	public static Date stringToDate(String strTime, String formatType){
 		if(formatType==null)
 			formatType = FORMAT_DATE_TIME;
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return date;
 	}
 
 	/**
	 * long转换为Date类型
	 * @param currentTime要转换的long类型的时间
	 * @param formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	 * @return 
	 * 
	 * @author yao
	 */
 	public static Date longToDate(long currentTime, String formatType){
 		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
 		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
 		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
 		return date;
 	}
 
 	
 	/**
	 * string类型转换为long类型 strTime的时间格式和formatType的时间格式必须相同
	 * @param strTime要转换的String类型的时间
	 * @param formatType时间格式
	 * @return
	 * 
	 * @author yao
	 */
 	public static long stringToLong(String strTime, String formatType){
 		if(formatType==null)
 			formatType = FORMAT_DATE_TIME;
 		Date date = stringToDate(strTime, formatType); // String类型转成date类型
 		if (date == null) {
 			return 0;
 		} else {
 			long currentTime = dateToLong(date); // date类型转成long类型
 			return currentTime;
 		}
 	}
 
 	/**
	 * 从Date类型转化为Long类型
	 * @return
	 * 
	 * @author yao
	 */
 	public static long dateToLong(Date date) {
 		return date.getTime();
 	}
 	
 	/**
	 * 获取当前时间 默认格式是yy-MM-dd HH:mm
	 * @return
	 * 
	 * @author yao
	 */
	 	
	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		return format.format(new Date(time));
	}
	/**
	 *获取日期  格式是yyyy-MM-dd 
	 * @param time
	 * @return
	 */
	public static String getDayTime(long time)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date(time));
	}
	
	/**
	 * 获取当前时间的小时和分钟
	 * @return
	 * 
	 * @author yao
	 */

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	/** 
	  * 获取聊天时间：因为sdk的时间默认到秒故应该乘1000
	  * @Title: getChatTime
	  * @Description: TODO
	  * @param  timesamp
	  * 
	  * @return String
	  * 
	  * @throws
	  */
	public static String getChatTime(long timesamp) {
		long clearTime = timesamp;
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(clearTime);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result =  getHourAndMin(clearTime);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(clearTime);
			break;
		case 2:
			result = "前天 " + getHourAndMin(clearTime);
			break;

		default:
			result = getDayTime(clearTime);
			break;
		}

		return result;
	}
	
	/**
	 * 获取当前时间，单位是毫秒
	 * @return
	 * 
	 * @author yao
	 */
	
	public static Long getCurrentMilliSecond()
	{
		
		return System.currentTimeMillis();
	}
	
}