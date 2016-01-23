package com.lym.twogoods.adapter.base;

import java.util.List;

import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.manager.ThumbnailMapManager;
import com.lym.twogoods.screen.GoodsScreen;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * <p>
 * 	商品列表每一个item中图片列表适配器
 * </p>
 *  
 * @author 麦灿标
 * */
public class GoodsPictureListAdapter extends BaseAdapter{

	private Context mContext;
	
	private List<String> mPictureUrlList;
	
	public GoodsPictureListAdapter(Context context, List<String> pictureUrlList) {
		mContext = context;
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
		ImageView iv = null;
		if(convertView == null) {
			iv = new ImageView(mContext);
			iv.setScaleType(ScaleType.CENTER_CROP);
			PictureThumbnailSpecification specification = GoodsScreen.getIndexGoodsPictureThumbnailSpecification();
			AbsListView.LayoutParams params = new AbsListView.LayoutParams(specification.getWidth(), specification.getHeight());
			iv.setLayoutParams(params);
		} else {
			iv = (ImageView) convertView;
		}
		
		String url = (String) getItem(position);
		setItemContent(url, iv);
		
		return iv;
	}

	private void setItemContent(final String url, final ImageView imageView) {	
		ThumbnailMapManager.loadGoodsPictureThumnail(mContext, url, imageView, ThumbnailMapManager.DisplayType.ListViewType);
	}
	
	/**
	 * 重新设置数据集
	 * 
	 * @param pictureUrlList
	 */
	public void resetData(List<String> pictureUrlList) {
		mPictureUrlList = pictureUrlList;
	}
}
