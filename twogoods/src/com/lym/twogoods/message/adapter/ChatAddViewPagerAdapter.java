package com.lym.twogoods.message.adapter;

import com.lym.twogoods.R;
import com.lym.twogoods.message.config.ChatBottomConfig;
import com.lym.twogoods.ui.SendPictureActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
/**
 * 点击添加按钮后底部显示的viewpager的适配器
 * @author 尧俊锋
 *
 */
public class ChatAddViewPagerAdapter extends PagerAdapter{

	private final static String TAG = "ChatAddViewPagerAdapter";
	private Context mContext;
	private GridView mGridView[];
	/**用来和Activity通信*/
	private Handler mHandler;
	
	public ChatAddViewPagerAdapter(Context context,Handler handler)
	{
		mContext = context;
		mHandler = handler;
		init();
	}
	
	private void init()
	{
		mGridView = new GridView[ChatBottomConfig.pagerNum];
		for(int i = 0;i<ChatBottomConfig.pagerNum;i++)
		{
			mGridView[i] = new GridView(mContext);
			mGridView[i].setNumColumns(ChatBottomConfig.columnNum);
			mGridView[i].setAdapter(new ChatButtonGridViewAdapter(mContext));
			mGridView[i].setSelector(new ColorDrawable(Color.TRANSPARENT));
			setItemClickEventListnerForGrodView(mGridView[i]);
		}
	}
	
	/**
	 * gridview的item点击事件
	 * @param gridView
	 */
	private void setItemClickEventListnerForGrodView(GridView gridView) {
		if(gridView==null)
			return;
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch(position)
				{
				case 0:
					Message msgP = new Message();
					msgP.what = ChatBottomConfig.MSG_SEND_PIC;
					mHandler.sendMessage(msgP);
					break;
				case 1:
					Message msgV = new Message();
					msgV.what = ChatBottomConfig.MSG_SEND_VOICE;
					mHandler.sendMessage(msgV);
					break;
				case 2:
					Message msgL = new Message();
					msgL.what = ChatBottomConfig.MSG_SEND_LOC;
					mHandler.sendMessage(msgL);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public int getCount() {
		return ChatBottomConfig.pagerNum;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewGroup)container).removeView(mGridView[position]);
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewGroup)container).addView(mGridView[position], 0);
		return mGridView[position];
	}
	
}
