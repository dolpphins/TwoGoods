package com.lym.twogoods.ui;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.async.MultiPicturesAsyncTask;
import com.lym.twogoods.async.MultiPicturesAsyncTaskExecutor;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.ui.base.BackActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * <p>
 * 	查看图片Activity
 * </p>
 * <p>
 * 	你必须使用<pre>intent.putStringArrayListExtra("picturesUrlList",list);</pre>
 * 	传递要显示的图片的网络url过来.
 * </p>
 * <p>
 * 	该Activity图片加载完默认磁盘缓存路径为{@link DiskCacheManager#getDefaultPictureCachePath()},你可以通过
 * 	传递你自定义的缓存路径来定义自己的缓存路径,代码如:<pre>intent.putString("diskCachePath",path);</pre>
 * </p>
 * <p>
 * 	你也可以设置当前显示哪张图片,通过以下代码设置:
 * 	<pre>intent.putExtra("currentIndex", currentIndex);</pre>
 * </p>
 * @author 麦灿标
 * */
public class DisplayPicturesActivity extends BackActivity implements MultiPicturesAsyncTask.OnMultiPicturesAsyncTaskListener,
													OnPageChangeListener {

	private final static String TAG = "DisplayPicturesActivity";
		
	//数据集
	private List<String> picturesUrlList;
	//PhotoViewAttacher集合
	private List<PhotoViewAttacher> photoViewAttacherList;
	
	private ViewPager app_dispaly_pictures_viewpager;

	private RelativeLayout[] mWrapLayouts;
	private ImageView[] mImageViews;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_dispaly_pictures_activity);
		setActionBarColor(Color.parseColor("#000000"));
		
		app_dispaly_pictures_viewpager = (ViewPager) findViewById(R.id.app_dispaly_pictures_viewpager);
			
		picturesUrlList = getIntent().getStringArrayListExtra("picturesUrlList");
		
		if(picturesUrlList != null) {
			
			int currentIndex = getIntent().getIntExtra("currentIndex", 0);
			app_dispaly_pictures_viewpager.setOnPageChangeListener(this);
			app_dispaly_pictures_viewpager.setAdapter(new PicturesViewPagerAdapter());
			app_dispaly_pictures_viewpager.setCurrentItem(currentIndex);
			Log.i(TAG, "currentIndex:" + currentIndex);
			setTitleTip(currentIndex);
			createImageViews();
			//开始获取图片
			//String[] params = (String[]) picturesUrlList.toArray();//报类转型异常
			String[] params = new String[picturesUrlList.size()];
			for(int i = 0; i < picturesUrlList.size(); i++) {
				params[i] = picturesUrlList.get(i);
			}
			MultiPicturesAsyncTaskExecutor executor = new MultiPicturesAsyncTaskExecutor(this);
			executor.setOnMultiPicturesAsyncTaskListener(this);
			String diskCachePath = getIntent().getStringExtra("diskCachePath");
			if(TextUtils.isEmpty(diskCachePath)) {
				//查看商品图片缓存目录
				diskCachePath = DiskCacheManager.getInstance(this).getDefaultPictureCachePath();
			}
			executor.setDiskCacheDir(diskCachePath);
			executor.execute(params);
		}
		
	
	}
	
	private void createImageViews() {
		photoViewAttacherList = new ArrayList<PhotoViewAttacher>();
		mWrapLayouts = new RelativeLayout[picturesUrlList.size()];
		mImageViews = new ImageView[picturesUrlList.size()];
		for(int i = 0; i < mImageViews.length; i++) {
			mWrapLayouts[i] = new RelativeLayout(this);
			mWrapLayouts[i].setBackgroundColor(Color.parseColor("#000000"));
			mImageViews[i] = new ImageView(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			mImageViews[i].setLayoutParams(params);
			mImageViews[i].setImageResource(R.drawable.picture_loading_icon);
			mWrapLayouts[i].addView(mImageViews[i]);
			PhotoViewAttacher attacher = new PhotoViewAttacher(mImageViews[i]);
			photoViewAttacherList.add(attacher);
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int position) {
		setTitleTip(position);
	}
	
	//注意position从0开始
	private void setTitleTip(int position) {
		int index = 0;
		if(picturesUrlList.size() > 0) {
			index = position + 1;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(index);
		sb.append("/");
		sb.append(picturesUrlList.size());
		TextView tv = setCenterTitle(sb.toString());
		if(tv != null) {
			tv.setTextColor(Color.parseColor("#ffffff"));
		}
	}
	
	private class PicturesViewPagerAdapter extends PagerAdapter {
		
		@Override
		public int getCount() {
			if(picturesUrlList != null) {
				return picturesUrlList.size();
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
	public void onPreExecute(int position) {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.display_pictures_loading_anim);
		mImageViews[position].startAnimation(anim);
	}

	@Override
	public void onProgressUpdate(int position, Integer... values) {
	}

	@Override
	public void onPostExecute(int position, Bitmap result) {
		//清除动画
		mImageViews[position].clearAnimation();
		//获取图片成功
		if(result != null) {
			LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mImageViews[position].setLayoutParams(params);
			mImageViews[position].setImageBitmap(result);
		} else {
			mImageViews[position].setImageBitmap(null);
		}
	}
}
