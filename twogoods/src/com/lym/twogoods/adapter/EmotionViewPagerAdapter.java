package com.lym.twogoods.adapter;

import com.lym.twogoods.config.EmotionConfiguration;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;

/**
 * <p>
 * 	表情ViewPager适配器
 * </p>
 * 
 * @author 麦灿标
 * */
public class EmotionViewPagerAdapter extends PagerAdapter{

	private final static String TAG = "EmotionViewPagerAdapter";
	
	private Context mContext;
	
	/** 接受表情输入的EditText */
	private EditText mAttachEditText;
	
	public GridView[] mEmotionGridView;
	
	/**
	 * 构造函数
	 * 
	 * @param context 上下文
	 * @param edittext 接受表情输入的EditText
	 * */
	public EmotionViewPagerAdapter(Context context, EditText edittext) {
		mContext = context;
		mAttachEditText = edittext;
		init();
	}
	
	private void init() {
		//Log.i(TAG, mEmotionGridView.length + "");
		mEmotionGridView = new GridView[EmotionConfiguration.EMOTION_TOTAL_PAGE_NUMBER];
		for(int i = 0; i < mEmotionGridView.length; i++) {
			mEmotionGridView[i] = new GridView(mContext);
			mEmotionGridView[i].setNumColumns(EmotionConfiguration.EMOTION_NUMBER_PER_LINE);
			setItemClickEventForGridView(mEmotionGridView[i], i);
			BaseAdapter adapter = new EmotionGridViewAdapter(mContext, i);
			mEmotionGridView[i].setAdapter(adapter);
		}
	}
	
	@Override
	public int getCount() {
		return EmotionConfiguration.EMOTION_TOTAL_PAGE_NUMBER;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewGroup)container).removeView(mEmotionGridView[position % mEmotionGridView.length]);
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewGroup)container).addView(mEmotionGridView[position % mEmotionGridView.length], 0);
		return mEmotionGridView[position % mEmotionGridView.length];
	}
	
	private void setItemClickEventForGridView(GridView gv, final int pageIndex) {
		if(gv == null) {
			return;
		}
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(TAG, "pageIndex:" + pageIndex);
				Log.i(TAG, "position:" + position);
				if(mAttachEditText != null) {
					if(EmotionConfiguration.isDelete(pageIndex, position)) {
						mAttachEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
						return;
					}
					Drawable d = EmotionConfiguration.getEmotionDrawableOnPosition(mContext, pageIndex, position);
					if(d != null) {
						d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
						SpannableString ss = new SpannableString("*");
						ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
						ss.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						mAttachEditText.getText().insert(mAttachEditText.getSelectionStart(), ss);
					}
				}
				
			}
		});
	}

}
