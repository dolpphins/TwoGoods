package com.lym.twogoods.adapter.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.GetAccessUrlListener;
import com.bmob.btp.callback.ThumbnailListener;
import com.lym.twogoods.ThumbnailMap;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.manager.ImageLoaderHelper;
import com.lym.twogoods.manager.ThumbnailMapManager;
import com.lym.twogoods.screen.GoodsScreen;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.bmob.v3.datatype.BmobFile;

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
		iv.setScaleType(ScaleType.CENTER_CROP);
		PictureThumbnailSpecification specification = GoodsScreen.getIndexGoodsPictureThumbnailSpecification(mAcitity);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(specification.getWidth(), specification.getHeight());
		iv.setLayoutParams(params);
		
		String url = (String) getItem(position);
		setItemContent(url, iv);
		
		return iv;
	}

	private void setItemContent(final String url, final ImageView imageView) {	
		ThumbnailMapManager.loadGoodsPictureThumnail(mAcitity, url, imageView, ThumbnailMapManager.DisplayType.ListViewType);
	}
}
