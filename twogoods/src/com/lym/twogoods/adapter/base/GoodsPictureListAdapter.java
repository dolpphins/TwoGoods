package com.lym.twogoods.adapter.base;

import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.screen.GoodsScreen;
import com.lym.twogoods.manager.UniversalImageLoaderConfigurationManager;
import com.lym.twogoods.manager.UniversalImageLoaderManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * <p>
 * 	商品列表每一个item中图片列表适配器
 * </p>
 *  
 * @author 麦灿标
 * */
public class GoodsPictureListAdapter extends BaseAdapter{

	private Activity mAcitity;
	
	private List<String> mPictureUrlList;
	
	public GoodsPictureListAdapter(Activity activity, List<String> pictureUrlList) {
		mAcitity = activity;
		mPictureUrlList = pictureUrlList;
	}
	
	@Override
	public int getCount() {
		if(mPictureUrlList == null) {
			return 0;
		}
		return mPictureUrlList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPictureUrlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override  
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView iv = new ImageView(mAcitity);
		PictureThumbnailSpecification specification = GoodsScreen.getGoodsPictureThumbnailSpecification(mAcitity);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(specification.getWidth(), specification.getHeight());
		iv.setLayoutParams(params);
		
		String url = (String) getItem(position);

		setItemContent(url, iv);
		
		
		return iv;
	}

	private void setItemContent(String url, ImageView imageView) {	
		
		ImageLoaderConfiguration configuration = UniversalImageLoaderConfigurationManager
				.getGoodsListPictureThumbnailImageLoaderConfiguration(mAcitity.getApplicationContext());
		ImageLoader imageLoader = UniversalImageLoaderManager.getImageLoader(configuration);
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.common_m1)
				.showImageForEmptyUri(R.drawable.common_m10)
				.showImageOnFail(R.drawable.common_m11)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.build();
		
		imageLoader.displayImage(url, imageView, options);
	}
	
}
