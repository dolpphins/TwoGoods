package com.lym.twogoods.widget;

import com.lym.twogoods.utils.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * 圆形ImageView
 * 
 * @author 麦灿标
 * */
public class CircleImageView extends ImageView {

	private Context mContext;
	
	private Rect rect;
	private Paint paint;
	
	private Drawable mPreDrawable;
	private Bitmap mBitmap;
	
	public CircleImageView(Context context) {
		this(context, null);
	}
	
	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		
		init();
	}
	
	private void init() {
		rect = new Rect();
		paint = new Paint();
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		Drawable mDrawable = getDrawable();
//		if(mDrawable != null) {
//			int width = mDrawable.getIntrinsicWidth();
//			int height = mDrawable.getIntrinsicHeight();
//			int diameter = width > height ? height : width;
//			setMeasuredDimension(diameter, diameter);
//		} else {
//			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		}
//	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		Drawable mDrawable = getDrawable();
		if(mDrawable != null && mDrawable != mPreDrawable) {
			mPreDrawable = mDrawable;
			//只创建一次
			mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
			mBitmap = ImageUtil.toRoundBitmap(mBitmap);
			mBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), getHeight(), false);
		}
		if(mBitmap != null) {
			rect.left = 0;
			rect.top = 0;
			rect.right = getWidth();
			rect.bottom = getHeight();
			paint.reset();
			
			canvas.drawBitmap(mBitmap, null, rect, paint);
		} else {
			super.onDraw(canvas);
		}
	}
}
