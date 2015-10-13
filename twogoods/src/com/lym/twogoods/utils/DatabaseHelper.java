package com.lym.twogoods.utils;

import java.util.UUID;

/**
 * 与数据库操作相关的工具类
 * 
 * @author yao
 *
 * */
public class DatabaseHelper{
	
	/**
	 * 生成GUID,即通用唯一识别码。标准的UUID格式为：
	 * xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxx (8-4-4-4-12)
	 * 
	 * @author yao
	 * */
	public static UUID getUUID()
	{
		UUID uid = UUID.randomUUID();
		return uid;
	}
}
