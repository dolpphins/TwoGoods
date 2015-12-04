package com.lym.twogoods.message.config;

import com.lym.twogoods.R;

/**
 * 有关ChatActivity底部布局的配置
 * @author yao
 *
 */
public class ChatBottomConfig {
	
	
	/**viewpager要显示页数*/
	public static final int pagerNum = 1;
	
	/**一个gridview要显示的列数*/
	public static final int columnNum = 3;
	
	/**点击发送图片*/
	public static final int MSG_SEND_PIC = 20;
	
	/**点击发送语音*/
	public static final int MSG_SEND_VOICE =30;
	
	/**点击发送位置*/
	public static final int MSG_SEND_LOC = 40;
	
	
	
	/**gridview条目图片,要增加条目直接在此处增加图片*/
	public static final Integer imgs[] = {R.drawable.message_chat_add_picture_press,R.drawable.message_chat_add_picture_press,
		R.drawable.message_chat_add_location_press};
	
	
	/**gridview条目title,要增加条目直接在此处增加title*/
	public static final String titles[] = {"图片","语音","位置"};
	
	public static Integer[] getImages()
	{
		return imgs;
	}
	public static String[] getTitles()
	{
		return titles;
	}
}
