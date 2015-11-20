package com.lym.twogoods.index.adapter;

import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.GoodsComment;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.index.interf.OnGoodsCommentReplyListener;
import com.lym.twogoods.manager.ImageLoaderHelper;
import com.lym.twogoods.utils.TimeUtil;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * <p>
 * 	商品评论ListView适配器
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsCommentListViewAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<GoodsComment> mGoodsCommentList;
	
	public GoodsCommentListViewAdapter(Context context, List<GoodsComment> goodsCommentList) {
		mContext = context;
		mGoodsCommentList = goodsCommentList;
	}
	
	@Override
	public int getCount() {
		if(mGoodsCommentList != null) {
			return mGoodsCommentList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewHolder viewHolder;
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.index_goods_detail_comment_listview_item, null);
			viewHolder = new ItemViewHolder();
			viewHolder.index_goods_detail_comment_item_content = (TextView) convertView.findViewById(R.id.index_goods_detail_comment_item_content);
			viewHolder.index_goods_detail_comment_item_hp = (ImageView) convertView.findViewById(R.id.index_goods_detail_comment_item_hp);
			viewHolder.index_goods_detail_comment_item_publish_time = (TextView) convertView.findViewById(R.id.index_goods_detail_comment_item_publish_time);
			viewHolder.index_goods_detail_comment_item_username = (TextView) convertView.findViewById(R.id.index_goods_detail_comment_item_username);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}
		
		GoodsComment item = mGoodsCommentList.get(position);
		
		setItemContent(viewHolder, item);
		
		return convertView;
	}
	
	private void setItemContent(ItemViewHolder viewHolder, final GoodsComment item) {
		ImageLoaderHelper.loadUserHeadPictureThumnail(mContext, 
				viewHolder.index_goods_detail_comment_item_hp, item.getHead_url(), null);
		viewHolder.index_goods_detail_comment_item_username.setText(item.getUsername());
		viewHolder.index_goods_detail_comment_item_publish_time.setText(TimeUtil.getDescriptionTimeFromTimestamp(item.getTime()));
		viewHolder.index_goods_detail_comment_item_content.setText(item.getContent());
	}
	
	private static class ItemViewHolder {
		
		/** 头像 */
		private ImageView index_goods_detail_comment_item_hp;
		
		/** 用户名 */
		private TextView index_goods_detail_comment_item_username;
		
		/** 发表时间 */
		private TextView index_goods_detail_comment_item_publish_time;
		
		/** 内容 */
		private TextView index_goods_detail_comment_item_content;
	}

}
