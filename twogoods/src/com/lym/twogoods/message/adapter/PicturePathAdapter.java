package com.lym.twogoods.message.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.lym.twogoods.R;
import com.lym.twogoods.message.viewHolder.ViewHolder;

/**
 * 在发送图片时图片路径选择的适配器,继承
 * @author yao
 *
 */
public class PicturePathAdapter extends PicturePathBaseAdapter<String>
{

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public List<String> mSelectedImage = new LinkedList<String>();

	/**
	 * 文件夹路径
	 */
	public String mDirPath;

	public PicturePathAdapter(Context context, List<String>selectedPics,
			List<String> mDatas, int itemLayoutId,String dirPath){
		
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
		this.mSelectedImage = selectedPics;
	}
	
	/*
	 * 获取已经被选择的相片
	 */
	public List<String> getSelectedPics(){
		return mSelectedImage;
	}

	@Override
	public void convert(final ViewHolder helper, final String item)
	{
		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.message_chat_pictures_no);
		//设置no_selected
		helper.setImageResource(R.id.id_item_select,
						R.drawable.message_chat_picture_unselected);
		//设置图片
			
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
		//}
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
				if (mSelectedImage.contains(mDirPath + "/" + item))
				{
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.message_chat_picture_unselected);
					mImageView.setColorFilter(null);
				} else{// 未选择该图片
					if(mSelectedImage.size()==5){
						Toast.makeText(mContext, "只能选择5张相片", Toast.LENGTH_LONG).show();
					}else{
					mSelectedImage.add(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.message_chat_pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
					}
				}

			}
		});
		
		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if(mSelectedImage.contains(mDirPath + "/" + item))
		{
			mSelect.setImageResource(R.drawable.message_chat_pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}
}
