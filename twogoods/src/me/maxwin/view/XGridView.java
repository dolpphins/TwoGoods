package me.maxwin.view;

import com.lym.twogoods.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class XGridView extends LinearLayout{

	private final static String TAG = "XGridView";
	
	private XListViewHeader mHeaderView;
	private RelativeLayout mHeaderViewContent;
	private int mHeaderViewHeight;
	
	private GridView mGridView;
	
	private Context mContext;
	
	public XGridView(Context context) {
		this(context, null);
	}
	
	public XGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public XGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init(context);
	}

	private void init(Context context) {
		//设置为垂直方向,默认为水平方向
		setOrientation(LinearLayout.VERTICAL);
		
		mHeaderView = new XListViewHeader(context);
		mGridView = new GridView(context);
		initGridView();
		
		removeAllViews();
		addView(mHeaderView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		addView(mGridView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}
	
	private void initGridView() {
		mGridView.setOnTouchListener(new OnGridViewTouchListener());
	}
	
	/**
	 * 设置适配器
	 * 
	 * @param adapter 要设置的适配器
	 */
	public void setAdapter(ListAdapter adapter) {
		mGridView.setAdapter(adapter);
	}
	
	/**
	 * 设置列数
	 * 
	 * @param numColumns 指定的列数
	 */
	public void setNumColumns(int numColumns) {
		mGridView.setNumColumns(numColumns);
	}
	
	/**
	 * 设置伸展模式
	 * 
	 * @param stretchMode 指定的设置模式
	 */
	public void setStretchMode(int stretchMode) {
		mGridView.setStretchMode(stretchMode);
	}
	
	/**
	 * 获取与该GridView相关联的AbsListView对象
	 * 
	 * @return 返回与该GridView相关联的AbsListView对象
	 */
	public AbsListView getAbsListView() {
		return mGridView;
	}
	
	/**
	 * 设置水平方向上Item之间的间距
	 * 
	 * @param horizontalSpacing 指定的水平方向上Item之间的间距
	 */
	public void setHorizontalSpacing(int horizontalSpacing) {
		mGridView.setHorizontalSpacing(horizontalSpacing);
	}
	
	/**
	 * 设置两行之间的间距
	 * 
	 * @param verticalSpacing 指定的两行之间的间距
	 */
	public void setVerticalSpacing(int verticalSpacing) {
		mGridView.setVerticalSpacing(verticalSpacing);
	}
	
	/**
	 * 设置滚动条可不可用
	 * 
	 * @param verticalScrollBarEnabled true表示可用,false表示不可用
	 */
	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
		mGridView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
	}
	
	/**
	 * 设置Item点击事件监听器
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		mGridView.setOnItemClickListener(listener);
	}
	
	
	/**
	 * GridView触摸事件监听器
	 */
	private class OnGridViewTouchListener implements OnTouchListener {

		private float mLastY;
		
		private boolean mIsPullHeaderView;
		
		private int mHeaderViewVisibleHeight;
		
		private final static float OFFSET_RADIO = 1.8f;
		
		private Scroller mScroller;
		
		
		public OnGridViewTouchListener() {
			mScroller = new Scroller(mContext, new DecelerateInterpolator());
			
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mLastY == -1) {
				mLastY = event.getRawY();
			}
			
			int action = event.getAction();
			
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				mLastY = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				float deltaY = event.getRawY() - mLastY;
				mLastY = event.getRawY();
				
				if(mGridView.getFirstVisiblePosition() == 0) {
					View child = mGridView.getChildAt(0);
					if(child.getTop() >= 0) {
							if(deltaY > 0) {
								updateHeaderHeight(deltaY / OFFSET_RADIO);
							}
						}
					}
				break;
			case MotionEvent.ACTION_UP: 
				
				break;
			}
			return false;
		}
		
		private void updateHeaderHeight(float delta) {
			mHeaderView.setVisiableHeight((int) delta
					+ mHeaderView.getVisiableHeight());
			System.out.println("delta:" + delta);
			System.out.println("mHeaderView.getVisiableHeight():" + mHeaderView.getVisiableHeight());
//			if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
//				if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
//					mHeaderView.setState(XListViewHeader.STATE_READY);
//				} else {
//					mHeaderView.setState(XListViewHeader.STATE_NORMAL);
//				}
//			}
//			setSelection(0); // scroll to top each time
		}
		
	}
}
