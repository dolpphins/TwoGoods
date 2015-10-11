package com.lym.twogoods.utils;

import java.util.UUID;

public class DatabaseHelper{
	/**
	 * 生成GUID
	 * 
	 * @author yao
	 */
	
	public UUID getUUID()
	{
		UUID uid = UUID.randomUUID();
		return uid;
	}
}
