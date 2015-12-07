package me.maxwin.view;

import com.lym.twogoods.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Scroller;

public class XGridView extends LinearLayout{

	private final static String TAG = "XGridView";
	
	private XListViewHeader mHeaderView;
	
	private ScrollGridView mGridView;
	
	private Context mContext;
	
	private Scroller mScroller;
	
	private int mHeaderViewHeight;
	
	private boolean mIsRefreshing;
	
	/** 刷新监听器 */
	private IAbsListViewListener mGridViewListener;
	
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
		mGridView = new ScrollGridView(context);
		initGridView();
		
		removeAllViews();
		addView(mHeaderView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		addView(mGridView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		
		mScroller = new Scroller(context, new DecelerateInterpolator());

		mHeaderViewHeight = (int) context.getResources().getDimension(R.dimen.xlistview_header_height);
	}
	
	private void initGridView() {
		mGridView.setOnTouchListener(new OnGridViewTouchListener());
		mGridView.setOverScrollMode(View.OVER_SCROLL_NEVER);
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
	 * 设置Item Selector
	 * 
	 * @param sel 注意不能为null
	 */
	public void setSelector(Drawable sel) {
		mGridView.setSelector(sel);
	}
	
	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if(mIsRefreshing) {
			
		} else {
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
	}

	private void scrollHeaderView() {
		int dy = -mHeaderView.getVisiableHeight();
		if(mHeaderView.getState() == XListViewHeader.STATE_READY && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
			dy += mHeaderViewHeight;
		}
		mScroller.startScroll(0, mHeaderView.getVisiableHeight(), 0, dy);
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()) {
			mHeaderView.setVisiableHeight(mScroller.getCurrY());
			postInvalidate();//重要
		}
		super.computeScroll();
	}
	
	public void stopRefresh() {
		if(mIsRefreshing) {
			mIsRefreshing = false;
			mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			scrollHeaderView();
		}
	}
	
	public void setXGridViewListener(IAbsListViewListener l) {
		mGridViewListener = l;
	}
	
	public IAbsListViewListener getXGridViewListener() {
		return mGridViewListener;
	}
	
	/**
	 * GridView触摸事件监听器
	 */
	private class OnGridViewTouchListener implements OnTouchListener {

		private float mLastY;
		
		private final static float OFFSET_RADIO = 1.8f;
		
		private boolean mDisablePerformClick = false;
		
		public OnGridViewTouchListener() {
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
					if(mGridView.computeVerticalScrollOffset() == 0) {
						if(deltaY <= 0 && mHeaderView.getVisiableHeight() <= 0) {
							break;
						}
						updateHeaderHeight(deltaY / OFFSET_RADIO);
						mDisablePerformClick = true;
						return true;
					}
				}
				break;
			//case MotionEvent.ACTION_UP:
			  default:
				scrollHeaderView();
				if(mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					mIsRefreshing = true;
					if(mGridViewListener != null) {
						mGridViewListener.onRefresh();
					}
				}
				if(mDisablePerformClick) {
					mDisablePerformClick = false;
					return true;
				}
				break;
			}
			return false;
		}
	}
}
