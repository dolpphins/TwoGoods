package com.lym.twogoods.ui;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.ui.base.BackActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import uk.co.senab.photoview.PhotoViewAttacher;

public class DisplayPicturesActivity extends BackActivity {

	private final static String TAG = "DisplayPicturesActivity";
	
	private final float FLING_BOUND = 5000.0f;
	
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
			Log.i(TAG, "picturesUrlList size is:" + picturesUrlList.size());
			app_dispaly_pictures_viewpager.setAdapter(new PicturesViewPagerAdapter());
			createImageViews();
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
	
	
}
