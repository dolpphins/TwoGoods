package com.lym.twogoods.fragment;

import java.util.ArrayList;

import com.lym.twogoods.R;
import com.lym.twogoods.async.MultiPicturesAsyncTask;
import com.lym.twogoods.async.MultiPicturesAsyncTaskExecutor;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.GoodsConfiguration;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.index.adapter.GoodsCommentListViewAdapter;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.screen.DisplayUtils;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.widget.WrapContentViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import me.maxwin.view.XListView;


/**
 * <p>
 * 	商品详情Fragment
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsDetailFragment extends PullListFragment implements MultiPicturesAsyncTask.OnMultiPicturesAsyncTaskListener,
																	OnPageChangeListener{

	private final static String TAG = "GoodsDetailFragment";
	
	//商品信息
	private Goods mData;
	
	//头部布局
	private LinearLayout mHeaderLayout;
	private WrapContentViewPager mPicturesViewPager;
	
	private RelativeLayout[] mWrapLayouts;
	private ImageView[] mImageViews;
	
	//包含详细信息所有控件
	private DetailMessageViewHolder detailMessageViewHolder = new DetailMessageViewHolder();
	
	/**
	 * 构造函数
	 * 
	 * @param data 注意不能为空
	 * */
	public GoodsDetailFragment(Goods data) {
		mData = data;
		//如果为null,那么使用默认值
		if(mData == null) {
			Log.i(TAG, "goods detail activity get the goods data is null");
			mData = new Goods();
		}
		//没有图片则创建空集合
		if(mData.getPictureUrlList() == null) {
			Log.i(TAG, "goods detail activity get the goods data's picturesUrlList is null");
			ArrayList<String> picturesUrlList = new ArrayList<String>();
			mData.setPictureUrlList(picturesUrlList);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		//设置只允许上拉
		setMode(Mode.PULLUP);
		
		mHeaderLayout = (LinearLayout) LayoutInflater.from(mAttachActivity).inflate(R.layout.index_goods_detail_fragment_header, null);
		initHeaderView();
		
		//当弹出软键盘时如果ListView最后一条Item可见那么将ListView顶上去(要配合windowSoftInputMode="adjustResize")
		mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
		//请求强制拦截触摸事件,以解决滚动冲突问题
		mListView.requestForceInterceptTouchEvent(true);
		mListView.addHeaderView(mHeaderLayout);
		mListView.setAdapter(new GoodsCommentListViewAdapter());

		//开始获取数据
		getData();
		
		return v;
	}

	
	private void initHeaderView() {
		mPicturesViewPager = (WrapContentViewPager) mHeaderLayout.findViewById(R.id.index_goods_detail_pictures_vp);
		if(mPicturesViewPager != null) {
			mWrapLayouts = new RelativeLayout[mData.getPictureUrlList().size()];
			mImageViews = new ImageView[mData.getPictureUrlList().size()];
			for(int i = 0; i < mWrapLayouts.length; i++) {
				mWrapLayouts[i] = new RelativeLayout(mAttachActivity);
				mImageViews[i] = new ImageView(mAttachActivity);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				params.addRule(Gravity.CENTER);
				mImageViews[i].setLayoutParams(params);
				mImageViews[i].setScaleType(ScaleType.CENTER);
				mImageViews[i].setImageResource(R.drawable.message_chat_pictures_no);
				mWrapLayouts[i].addView(mImageViews[i]);
				setOnClickForImageView(mImageViews[i]);
			}
			
			PicturesViewPagerAdapter adapter = new PicturesViewPagerAdapter();
			
			//强制设置高度
			int height = (int) (DisplayUtils.getScreenWidthPixels(mAttachActivity) / GoodsConfiguration.GOODS_PICTURE_SCALE);
			mPicturesViewPager.requestForceHeight(height);
			//mPicturesViewPager.requestDisallowInterceptTouchEvent(true);
			mPicturesViewPager.setAdapter(adapter);
		}
		
		detailMessageViewHolder.index_goods_detail_browse_num = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_browse_num);
		detailMessageViewHolder.index_goods_detail_contact = (ImageView) mHeaderLayout.findViewById(R.id.index_goods_detail_contact);
		detailMessageViewHolder.index_goods_detail_description = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_description);
		detailMessageViewHolder.index_goods_detail_focus = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_focus);
		detailMessageViewHolder.index_goods_detail_fouse_num = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_fouse_num);
		detailMessageViewHolder.index_goods_detail_head_picture = (ImageView) mHeaderLayout.findViewById(R.id.index_goods_detail_head_picture);
		detailMessageViewHolder.index_goods_detail_phone = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_phone);
		detailMessageViewHolder.index_goods_detail_price = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_price);
		detailMessageViewHolder.index_goods_detail_publish_location = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_publish_location);
		detailMessageViewHolder.index_goods_detail_publish_time = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_publish_time);
		detailMessageViewHolder.index_goods_detail_report = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_report);
		detailMessageViewHolder.index_goods_detail_username = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_username);
		detailMessageViewHolder.index_goods_detail_voice = (ImageView) mHeaderLayout.findViewById(R.id.index_goods_detail_voice);
		//设置数据
		
		
		
	}
	
	private void setOnClickForImageView(ImageView iv) {
		if(iv == null) {
			return;
		}
		iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mAttachActivity, DisplayPicturesActivity.class);
				intent.putStringArrayListExtra("picturesUrlList",mData.getPictureUrlList());
				startActivity(intent);
			}
		});
	}
	
	private void getData() {
		//获取图片
		MultiPicturesAsyncTaskExecutor executor = new MultiPicturesAsyncTaskExecutor(mAttachActivity);
		executor.setOnMultiPicturesAsyncTaskListener(this);
		//String diskCachePath = getIntent().getStringExtra("diskCachePath");
		String diskCachePath = null;
		if(TextUtils.isEmpty(diskCachePath)) {
			//查看商品图片缓存目录
			diskCachePath = DiskCacheManager.getInstance(mAttachActivity).getDefaultPictureCachePath();
		}
		executor.setDiskCacheDir(diskCachePath);
		
		String[] params = new String[mData.getPictureUrlList().size()];
		for(int i = 0; i < mData.getPictureUrlList().size(); i++) {
			params[i] = mData.getPictureUrlList().get(i);
		}
		
		executor.execute(params);
	}
	
	private class PicturesViewPagerAdapter extends PagerAdapter {
		
		@Override
		public int getCount() {
			if(mData.getPictureUrlList() != null) {
				return mData.getPictureUrlList().size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(mWrapLayouts[position % mWrapLayouts.length]);
			
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(mWrapLayouts[position % mWrapLayouts.length], 0);
			return mWrapLayouts[position % mWrapLayouts.length];
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {		
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {	
	}

	@Override
	public void onPageSelected(int arg0) {
	}

	@Override
	public void onPreExecute(int position) {
		
	}

	@Override
	public void onProgressUpdate(int position, Integer... values) {
	}

	@Override
	public void onPostExecute(int position, Bitmap result) {
		if(result != null) {
			//更改ImageView显示类型
			mImageViews[position].setScaleType(ScaleType.CENTER_CROP);
			mImageViews[position].setImageBitmap(result);
		}
	}
	
	
	
	
	
	
	private static class DetailMessageViewHolder {
		
		/** 描述 */
		private TextView index_goods_detail_description;

		/** 价格 */
		private TextView index_goods_detail_price;
		
		/** 关注数 */
		private TextView index_goods_detail_fouse_num;
		
		/** 浏览数 */
		private TextView index_goods_detail_browse_num;
		
		/** 头像 */
		private ImageView index_goods_detail_head_picture; 
		
		/** 用户名 */
		private TextView index_goods_detail_username;
		
		/** 发布时间 */
		private TextView index_goods_detail_publish_time;
		
		/** 发布位置 */
		private TextView index_goods_detail_publish_location;
		
		/** 联系方式 */
		private TextView index_goods_detail_phone;
		
		/** 关注/取消关注 */
		private TextView index_goods_detail_focus;
		
		/** 语音 */
		private ImageView index_goods_detail_voice;
		
		/** 联系商家 */
		private ImageView index_goods_detail_contact;
		
		/** 举报 */
		private TextView index_goods_detail_report;
	}

}
