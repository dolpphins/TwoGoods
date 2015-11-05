package com.lym.twogoods.nearby.utils;

import java.util.Comparator;

import com.lym.twogoods.nearby.NearbyPositionModelBean;

/**
 * 对listview数据根据ABCDEFG...来排序
 * 
 * @author 龙宇文
 *
 * */
public class PinyinComparator implements Comparator<NearbyPositionModelBean> {

	public int compare(NearbyPositionModelBean o1, NearbyPositionModelBean o2) {
		if (o1.getPositionLetters().equals("@")
				|| o2.getPositionLetters().equals("#")) {
			return -1;
		} else if (o1.getPositionLetters().equals("#")
				|| o2.getPositionLetters().equals("@")) {
			return 1;
		} else {
			return o1.getPositionLetters().compareTo(o2.getPositionLetters());
		}
	}

}
