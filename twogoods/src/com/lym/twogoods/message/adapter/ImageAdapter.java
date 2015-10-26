package com.lym.twogoods.message.adapter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.lym.twogoods.R;
import com.lym.twogoods.config.DiskCacheManager;
import com.lym.twogoods.message.MessageConstant;
import com.lym.twogoods.message.viewHolder.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter
{
	protected LayoutInflater mInflater;
	public List<String> mSelectedImage;
	protected Context mContext;
	protected List<String> mDatas;
	protected final int mItemLayoutId;
	public Map<String, List<String>> mMap;
	private Handler mHandler;
	//设置一次最多可以发送多少张相片，默认20张
	private int selectCount = 20;

	public ImageAdapter(Context context, List<String>selectedPics,int itemLayoutId,
			List<String>dirPaths,Handler handler)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mItemLayoutId = itemLayoutId;
		mDatas = dirPaths;
		mHandler = handler;
		if(selectedPics==null)
			mSelectedImage = new LinkedList<String>();
		else
			mSelectedImage = selectedPics;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public String getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	/*
	 * 获取已经被选择的相片
	 */
	public List<String> getSelectPics()
	{
		return mSelectedImage;
	}
	/**
	 * 设置一次最多可以发送多少张相片
	 * @param count
	 */
	public void setSelectCount(int count)
	{
		selectCount = count;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
		if(position==0){
			viewHolder.setImageResource(R.id.id_item_image, R.drawable.message_chat_camera);
			viewHolder.setImageBitmap(R.id.id_item_select, null);
			ImageView camera = viewHolder.getView(R.id.id_item_image);
			camera.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					Toast.makeText(mContext, "点击了拍照", Toast.LENGTH_LONG).show();
					Message message = new Message();
					message.what = MessageConstant.OPEN_CAMERA;
					mHandler.sendMessage(message);
				}
			});
			return viewHolder.getConvertView();
		}else{
		convert(viewHolder, (position-1));
		return viewHolder.getConvertView();
		}

	}
	
	public void convert(ViewHolder helper, final int location) {
		//设置no_pic
				helper.setImageResource(R.id.id_item_image, R.drawable.message_chat_pictures_no);
				//设置no_selected
						helper.setImageResource(R.id.id_item_select,
								R.drawable.message_chat_picture_unselected);
				//设置图片
				helper.setImageByUrl(R.id.id_item_image, mDatas.get(location));
				
				final ImageView mImageView = helper.getView(R.id.id_item_image);
				final ImageView mSelect = helper.getView(R.id.id_item_select);
				
				mImageView.setColorFilter(null);
				//设置ImageView的点击事件
				mImageView.setOnClickListener(new OnClickListener()
				{
					//选择，则将图片变暗，反之则反之
					@Override
					public void onClick(View v)
					{
						
						// 已经选择过该图片
						if (mSelectedImage.contains(mDatas.get(location)))
						{
							mSelectedImage.remove(mDatas.get(location));
							mSelect.setImageResource(R.drawable.message_chat_picture_unselected);
							mImageView.setColorFilter(null);
						} else
						// 未选择该图片
						{
							if(mSelectedImage.size()==selectCount){
								Toast.makeText(mContext, "一次只能选择"+selectCount+"张相片", Toast.LENGTH_LONG).show();
								return;
							}else{
								
								mSelectedImage.add(mDatas.get(location));
								mSelect.setImageResource(R.drawable.message_chat_pictures_selected);
								mImageView.setColorFilter(Color.parseColor("#77000000"));
							}
						}

					}
				});
				
				/**
				 * 已经选择过的图片，显示出选择过的效果
				 */
				if (mSelectedImage.contains(mDatas.get(location)))
				{
					mSelect.setImageResource(R.drawable.message_chat_pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}

	}
	

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent)
	{
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}
	
	

}
