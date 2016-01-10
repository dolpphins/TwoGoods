package com.lym.twogoods.utils;

import java.util.Random;

/**
 * 与随机数有关工具类
 * 
 * @author mao
 *
 */
public class RandomUtils {

	/**
	 * 生成一个包含9位数字的9位随机字符串,该方法不保证
	 * 生成的随机字符串不重复,但理论上重复的概率很小.
	 *  
	 * @return 生成的9位数字随机字符串
	 */
	public static String generateDigits() {

		StringBuilder sb = new StringBuilder();
		//取低6位
		sb.append(System.currentTimeMillis() / 10000000);
		//取低3位(保证每台设备都不一样)
		sb.append(System.nanoTime() / 1000);
		//随机生成低2位
		Random random = new Random();
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		
		return sb.toString();
	}
}
