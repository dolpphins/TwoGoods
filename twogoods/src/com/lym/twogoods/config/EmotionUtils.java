package com.lym.twogoods.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.lym.twogoods.R;

/**
 * 表情配置文件工具类
 * 
 * @author 龙宇文
 * 
 * */
public class EmotionUtils implements Serializable {

	public static Map<String, Integer> emojiMap;
	static {
		emojiMap = new HashMap<String, Integer>();
		emojiMap.put("[呵呵]", R.drawable.common_m1);
		emojiMap.put("[嘻嘻]", R.drawable.common_m10);
		emojiMap.put("[哈哈]", R.drawable.common_m11);
		emojiMap.put("[爱你]", R.drawable.common_m12);
		emojiMap.put("[挖鼻屎]", R.drawable.common_m13);
		emojiMap.put("[吃惊]", R.drawable.common_m14);
		emojiMap.put("[晕]", R.drawable.common_m15);
		emojiMap.put("[泪]", R.drawable.common_m16);
		emojiMap.put("[馋嘴]", R.drawable.common_m17);
		emojiMap.put("[抓狂]", R.drawable.common_m18);
		emojiMap.put("[哼]", R.drawable.common_m19);
		emojiMap.put("[可爱]", R.drawable.common_m2);
		emojiMap.put("[怒]", R.drawable.common_m21);
		emojiMap.put("[汗]", R.drawable.common_m22);
		emojiMap.put("[害羞]", R.drawable.common_m23);
		emojiMap.put("[睡觉]", R.drawable.common_m24);
		emojiMap.put("[钱]", R.drawable.common_m25);
		emojiMap.put("[偷笑]", R.drawable.common_m26);
		emojiMap.put("[笑cry]", R.drawable.common_m27);
		emojiMap.put("[doge]", R.drawable.common_m28);
		emojiMap.put("[喵喵]", R.drawable.common_m29);
		emojiMap.put("[酷]", R.drawable.common_m3);
		emojiMap.put("[衰]", R.drawable.common_m31);
		emojiMap.put("[闭嘴]", R.drawable.common_m32);
		emojiMap.put("[鄙视]", R.drawable.common_m33);
		emojiMap.put("[花心]", R.drawable.common_m34);
		emojiMap.put("[鼓掌]", R.drawable.common_m35);
		emojiMap.put("[悲伤]", R.drawable.common_m36);
		emojiMap.put("[思考]", R.drawable.common_m37);
		emojiMap.put("[生病]", R.drawable.common_m38);
		emojiMap.put("[亲亲]", R.drawable.common_m39);
		emojiMap.put("[怒骂]", R.drawable.common_m4);
		emojiMap.put("[太开心]", R.drawable.common_m41);
		emojiMap.put("[懒得理你]", R.drawable.common_m42);
		emojiMap.put("[右哼哼]", R.drawable.common_m43);
		emojiMap.put("[左哼哼]", R.drawable.common_m44);
		emojiMap.put("[嘘]", R.drawable.common_m45);
		emojiMap.put("[委屈]", R.drawable.common_m46);
		emojiMap.put("[吐]", R.drawable.common_m47);
		emojiMap.put("[可怜]", R.drawable.common_m48);
		emojiMap.put("[打哈气]", R.drawable.common_m49);
		emojiMap.put("[挤眼]", R.drawable.common_m5);
		emojiMap.put("[失望]", R.drawable.common_m51);
		emojiMap.put("[顶]", R.drawable.common_m52);
		emojiMap.put("[疑问]", R.drawable.common_m53);
		emojiMap.put("[困]", R.drawable.common_m54);
		emojiMap.put("[感冒]", R.drawable.common_m55);
		emojiMap.put("[拜拜]", R.drawable.common_m56);
		emojiMap.put("[黑线]", R.drawable.common_m57);
		emojiMap.put("[阴险]", R.drawable.common_m58);
		emojiMap.put("[打脸]", R.drawable.common_m59);
		emojiMap.put("[傻眼]", R.drawable.common_m6);
		emojiMap.put("[猪头]", R.drawable.common_m61);
		emojiMap.put("[熊猫]", R.drawable.common_m62);
		emojiMap.put("[兔子]", R.drawable.common_m63);
	}

	public static int getImgByName(String imgName) {
		Integer integer = emojiMap.get(imgName);
		return integer == null ? -1 : integer;
	}
}
