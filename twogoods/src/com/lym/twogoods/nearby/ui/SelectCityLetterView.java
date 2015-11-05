package com.lym.twogoods.nearby.ui;

import com.lym.twogoods.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * <p>
 * 选择城市字母栏
 * </p>
 * 
 * @author 龙宇文
 * */
public class SelectCityLetterView extends View {

	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

	// 26个字母
	private static String[] letter = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "#" };
	private int choose = -1;// 选中
	private Paint paint = new Paint();
	private int letterSize = 20;

	public SelectCityLetterView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public SelectCityLetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SelectCityLetterView(Context context) {
		super(context);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		// 获取每一个字母的高度
		int singleHeight = height / letter.length;
		for (int i = 0; i < letter.length; i++) {
			paint.setColor(Color.rgb(33, 65, 98));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(letterSize);
			// 选中的状态
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// 字母的x坐标等于字母栏的中间-字符串宽度的一半
			float x = width / 2 - paint.measureText(letter[i]) / 2;
			float y = singleHeight * i + singleHeight;
			canvas.drawText(letter[i], x, y, paint);
			paint.reset();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * letter.length);

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;
			invalidate();
			break;

		default:
			setBackgroundResource(R.drawable.selectcity_letterview_background);
			if (oldChoose != c) {
				if (c >= 0 && c < letter.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(letter[c]);
					}
					choose = c;
					invalidate();
				}
			}
			break;
		}
		return true;
	}

	/*
	 * 
	 * 向外公开接口
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
}
