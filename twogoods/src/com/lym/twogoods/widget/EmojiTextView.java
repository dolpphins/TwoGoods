package com.lym.twogoods.widget;

import com.lym.twogoods.R;
import com.lym.twogoods.config.EmotionConfiguration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * 可显示表情的TextView
 * 
 * @author 麦灿标
 * */
public class EmojiTextView extends TextView{

	private final static String TAG = "EmojiTextView";
	
	private Context mContext;
	
	private ImageGetter mImageGetter;
	
	private float mEmojiSize;
	
	public EmojiTextView(Context context) {
		this(context, null);
	}
	
	public EmojiTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EmojiTextView);      
		int emojiSize = array.getDimensionPixelSize(R.styleable.EmojiTextView_emojiSize, 20);
		setEmojiSize(emojiSize);
		array.recycle();
		init();
	}
	
	private void init() {
		mImageGetter = new EmojiImageGetter();
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if(!TextUtils.isEmpty(text)) {
			if(text instanceof String) {
				String htmlText = EmotionConfiguration.toHtml((String)text);
				Spanned spannedText = Html.fromHtml(htmlText, mImageGetter, null);
				super.setText(spannedText, type);
			} else {
				super.setText(text, type);
			}
		}
	}
	
	/**
	 * 设置表情大小
	 * 
	 * @param 指定的大小
	 * */
	public void setEmojiSize(float size) {
		if(size > 0) {
			mEmojiSize = size;
		}
	}
	
	private class EmojiImageGetter implements Html.ImageGetter {
		
		@Override
		public Drawable getDrawable(String source) {
			Drawable d = EmotionConfiguration.getEmojiDrawableFromString(mContext, source);
			if(d != null) {
				int width = 0;
				int height = 0;
				if(mEmojiSize > 0) {
					width = (int) mEmojiSize;
					height = width;
				} else {
					width = (int) (getTextSize() * 1.25);
					height = width;
				}
				d.setBounds(0, 0, width, height);
			}
			return d;
		}
	}
}
