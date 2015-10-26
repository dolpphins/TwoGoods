package com.lym.twogoods.adapter.base;

import java.util.List;

import com.lym.twogoods.utils.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 
 *  
 * 
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
		
		ImageView iv = new ImageView(mContext);
		String url = (String) getItem(position);
		Bitmap bm = ImageUtil.getImageThumbnail(url, 68, 68);
		iv.setImageBitmap(bm);
		
		return iv;
	}

	
}
