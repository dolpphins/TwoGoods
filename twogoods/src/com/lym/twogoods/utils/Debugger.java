package com.lym.twogoods.utils;

import java.util.Map;

import android.content.Context;
import android.util.Log;

/**
 * 与调试操作相关的工具类
 * 
 * @author yao
 *
 * */
public class Debugger {
	
	private static Context mContext;
	 
	public Debugger(Context context)
	{
		mContext = context;
	}
	
	/**
	 * 打印当前进程的信息。
	 * */
	public static void debug()
	{
		Map<Thread, StackTraceElement[]> threadStack = Thread.getAllStackTraces();
		StackTraceElement[] mStackTraceElement = threadStack.get(Thread.currentThread());
		for(StackTraceElement s:mStackTraceElement)
		{
			Log.i(" debug ",s.toString());
		}
	}
}
