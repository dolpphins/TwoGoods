package com.lym.twogoods.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lym.twogoods.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * <p>
 * 	表情配置类
 * </p>
 * <p>
 * 	注意该类所有操作都包括删除按钮
 * </p>
 * 
 * @author 麦灿标
 * */
public class EmotionConfiguration {

	/** 每一行显示的表情数 ,注意包括删除按钮*/
	public final static int EMOTION_NUMBER_PER_LINE = 7;
	
	/** 每一页显示的表情行数 */
	public final static int EMOTION_LINE_NUMBER_PER_PAGE = 3;
	
	/** 每一页显示的表情总数,包括删除按钮 */
	public final static int EMOTION_NUMBER_PER_PAGE = EMOTION_NUMBER_PER_LINE * EMOTION_LINE_NUMBER_PER_PAGE;
	
	/** 总页数 */
	public final static int EMOTION_TOTAL_PAGE_NUMBER;
	
	/** 表情集合,包括删除按钮 */
	public final static List<String> sEmotionList = new ArrayList<String>(); 
	
	public final static String deleteString = "[删除]";
	
	/** 每个表情的padding,单位:dip */
	public final static int EmotionPadding = 8;
	
	/** 表情映射 */
	public final static Map<String, Integer> sEmojiMap = new LinkedHashMap<String, Integer>();//用LinkedHashMap保证顺序不变
	static {
		sEmojiMap.put("[微笑]", R.drawable.common_m1);
		sEmojiMap.put("[撇嘴]", R.drawable.common_m2);
		sEmojiMap.put("[色]", R.drawable.common_m3);
		sEmojiMap.put("[发呆]", R.drawable.common_m4);
		sEmojiMap.put("[得意]", R.drawable.common_m5);
		sEmojiMap.put("[流泪]", R.drawable.common_m6);
		sEmojiMap.put("[害羞]", R.drawable.common_m7);
		sEmojiMap.put("[闭嘴]", R.drawable.common_m8);
		sEmojiMap.put("[睡]", R.drawable.common_m9);
		sEmojiMap.put("[大哭]", R.drawable.common_m10);
		sEmojiMap.put("[尴尬]", R.drawable.common_m11);
		sEmojiMap.put("[发怒]", R.drawable.common_m12);
		sEmojiMap.put("[调皮]", R.drawable.common_m13);
		sEmojiMap.put("[呲牙]", R.drawable.common_m14);
		sEmojiMap.put("[惊讶]", R.drawable.common_m15);
		sEmojiMap.put("[难过]", R.drawable.common_m16);
		sEmojiMap.put("[酷]", R.drawable.common_m17);
		sEmojiMap.put("[冷汗]", R.drawable.common_m18);
		sEmojiMap.put("[抓狂]", R.drawable.common_m19);
		sEmojiMap.put("[吐]", R.drawable.common_m20);
		sEmojiMap.put("[偷笑]", R.drawable.common_m21);
		sEmojiMap.put("[愉快]", R.drawable.common_m22);
		sEmojiMap.put("[白眼]", R.drawable.common_m23);
		sEmojiMap.put("[傲慢]", R.drawable.common_m24);
		sEmojiMap.put("[饥饿]", R.drawable.common_m25);
		sEmojiMap.put("[困]", R.drawable.common_m26);
		sEmojiMap.put("[惊恐]", R.drawable.common_m27);
		sEmojiMap.put("[流汗]", R.drawable.common_m28);
		sEmojiMap.put("[憨笑]", R.drawable.common_m29);
		sEmojiMap.put("[悠闲]", R.drawable.common_m30);
		sEmojiMap.put("[奋斗]", R.drawable.common_m31);
		sEmojiMap.put("[咒骂]", R.drawable.common_m32);
		sEmojiMap.put("[疑问]", R.drawable.common_m33);
		sEmojiMap.put("[嘘]", R.drawable.common_m34);
		sEmojiMap.put("[晕]", R.drawable.common_m35);
		sEmojiMap.put("[疯了]", R.drawable.common_m36);
		sEmojiMap.put("[衰]", R.drawable.common_m37);
		sEmojiMap.put("[骷髅]", R.drawable.common_m38);
		sEmojiMap.put("[敲打]", R.drawable.common_m39);
		sEmojiMap.put("[再见]", R.drawable.common_m40);
		sEmojiMap.put("[擦汗]", R.drawable.common_m41);
		sEmojiMap.put("[抠鼻]", R.drawable.common_m42);
		sEmojiMap.put("[鼓掌]", R.drawable.common_m43);
		sEmojiMap.put("[糗大了]", R.drawable.common_m44);
		sEmojiMap.put("[坏笑]", R.drawable.common_m45);
		sEmojiMap.put("[左哼哼]", R.drawable.common_m46);
		sEmojiMap.put("[右哼哼]", R.drawable.common_m47);
		sEmojiMap.put("[哈欠]", R.drawable.common_m48);
		sEmojiMap.put("[鄙视]", R.drawable.common_m49);
		sEmojiMap.put("[委屈]", R.drawable.common_m50);
		sEmojiMap.put("[快哭了]", R.drawable.common_m51);
		sEmojiMap.put("[阴险]", R.drawable.common_m52);
		sEmojiMap.put("[亲亲]", R.drawable.common_m53);
		sEmojiMap.put("[吓]", R.drawable.common_m54);
		sEmojiMap.put("[可怜]", R.drawable.common_m55);
		
		
		int tempTotalNumber = sEmojiMap.size() / (EMOTION_NUMBER_PER_PAGE - 1);
		if(sEmojiMap.size() % (EMOTION_NUMBER_PER_PAGE - 1) != 0) {
			tempTotalNumber++;
		}
		EMOTION_TOTAL_PAGE_NUMBER = tempTotalNumber;
		
		Set<String> keys = sEmojiMap.keySet();
		int j = 0;
		for(String s : keys) {
			if(j == EMOTION_NUMBER_PER_PAGE - 1) {
				sEmotionList.add(deleteString);
				j = 0;
			}
			sEmotionList.add(s);
			j++;
		}
		//最后添加删除图标
		int size = sEmotionList.size();
		if(size > 0 || !deleteString.equals(sEmotionList.get(size - 1))) {
			sEmotionList.add(deleteString);
		}
		
	}
	
	/**
	 * <p>
	 * 	获取指定位置的表情位图
	 * </p>
	 * 
	 * @param context 上下文,不能为null.
	 * @param pageIndex 页索引,必须大于等于0且小于{@value #EMOTION_TOTAL_PAGE_NUMBER}
	 * @param position 页内索引,必须大于等于0且小于该页表情数
	 * 
	 * @return 获取成功返回相应的表情位图,获取失败返回null.
	 * */
	public static Bitmap getEmotionBitmapOnPosition(Context context, int pageIndex, int position) {
		if(context == null) {
			return null;
		}
		Drawable d = getEmotionDrawableOnPosition(context, pageIndex, position);
		if(d != null) {
			BitmapDrawable bd = (BitmapDrawable)d;
			return bd.getBitmap();
		} else {
			return null;
		}
	}
	
	/**
	 * <p>
	 * 	获取指定页指定位置的Drawable
	 * </p>
	 * 
	 * @param context 山上下文
	 * @param pageIndex 页索引,必须大于等于0且小于{@value #EMOTION_TOTAL_PAGE_NUMBER}
	 * @param position 页内索引,必须大于等于0且小于该页表情数
	 * 
	 * @return 获取成功返回相应的Drawable对象,获取失败返回null.
	 * */
	public static Drawable getEmotionDrawableOnPosition(Context context, int pageIndex, int position) {
		if(context == null) {
			return null;
		}
		if(!check(pageIndex, position)) {
			return null;
		}
		String key = getEmotionStringOnPosition(pageIndex, position);
		if(TextUtils.isEmpty(key)) {
			return null;
		} else {
			if(deleteString.equals(key)) {
				return context.getResources().getDrawable(R.drawable.emotion_delete_icon_nor);
			} else if(sEmojiMap.containsKey(key)) {
				return context.getResources().getDrawable(sEmojiMap.get(key));
			} else {
				return null;
			}
		}
	}
	
	/**
	 * <p>
	 * 	获取某一页的表情数,包括删除按钮
	 * </p>
	 * 
	 * @param pageIndex 指定的页
	 * 
	 * @return 获取成功返回相应的值,获取失败返回0
	 * */
	public static int getEmotionCountOnPage(int pageIndex) {
		if(pageIndex < 0 || pageIndex >= EMOTION_TOTAL_PAGE_NUMBER) {
			return 0;
		}
		if(pageIndex < EMOTION_TOTAL_PAGE_NUMBER - 1) {
			return EMOTION_NUMBER_PER_PAGE;
		} else {
			int preCount = pageIndex * EMOTION_NUMBER_PER_PAGE;
			int count = sEmotionList.size() - preCount;
			return count < 0 ? 0 : count;
		}
	}
	
	/**
	 * <p>
	 * 	判断是否点击了删除图标
	 * </p>
	 * 
	 * @param pageIndex 页索引
	 * @param position 页内位置
	 * 
	 * @return 如果是点击了删除图标返回true,否则返回false.注意如果参数不合法也会返回false.
	 * */
	public static boolean isDelete(int pageIndex, int position) {
		if(!check(pageIndex, position)) {
			return false;
		}
		int index = EMOTION_NUMBER_PER_PAGE * pageIndex + position;
		String key = sEmotionList.get(index);
		if(deleteString.equals(key)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * 	获取指定位置的表情字符串
	 * </p>
	 * 
	 * @param pageIndex 页索引
	 * @param position 页内位置
	 * 
	 * @return 获取成功返回相应的表情字符串,失败返回null.
	 * */
	public static String getEmotionStringOnPosition(int pageIndex, int position) {
		if(check(pageIndex, position)) {
			int index = EMOTION_NUMBER_PER_PAGE * pageIndex + position;
			return sEmotionList.get(index);
		}
		return null;
	}
	
	//检查位置数据合法性
	private static boolean check(int pageIndex, int position) {
		if(pageIndex < 0 || pageIndex >= EMOTION_TOTAL_PAGE_NUMBER 
				         || position < 0) {
			return false;
		}
		if(pageIndex < EMOTION_TOTAL_PAGE_NUMBER - 1 && position >= EMOTION_NUMBER_PER_PAGE) {
			return false;
		}
		if(pageIndex == EMOTION_TOTAL_PAGE_NUMBER - 1 && position >= getEmotionCountOnPage(pageIndex)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 通过表情文本获取相应的表情Drawable对象
	 * 
	 * @param context 上下文
	 * @param text 表情文本
	 *                                                                                                                                                                                                                                                                                                                                                                                                                                                     
	 * @return 获取成功返回相应的表情Drawable对象.失败返回null.
	 * */
	public static Drawable getEmojiDrawableFromString(Context context, String text) {
		if(context == null || TextUtils.isEmpty(text)) {
			return null;
		}
		Integer id = sEmojiMap.get(text);
		if(id != null) {
			return context.getResources().getDrawable(id);
		}
		return null;
	}
	
	/**
	 * 将指定的文本转换为html格式文本,其中所有表情标签都会被
	 * 更换成<img src="">
	 * 
	 * @return text 要转换的普通文本
	 * 
	 * @return 返回转换后的文本,如果原文本不含有表情标签那么将原样返回.
	 * */
	public static String toHtml(String text) {
		if(TextUtils.isEmpty(text)) {
			return text;
		} else {
			Set<String> keySet = sEmojiMap.keySet();
			if(keySet != null && keySet.size() > 0) {
				StringBuilder sb = null;
				for(String s : keySet) {
					sb = new StringBuilder();
					sb.append("<img src=\"");
					sb.append(s);
					sb.append("\">");
					if(text.contains(s)) {
						s = s.replaceAll("\\[", "\\\\[");
						s = s.replaceAll("\\]", "\\\\]");
						text = text.replaceAll(s, sb.toString());
					}
				}
			}
			return text;
		}
	}
}
