package com.lym.twogoods.adapter.base;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.manager.ImageLoaderHelper;
import com.lym.twogoods.manager.UniversalImageLoaderConfigurationManager;
import com.lym.twogoods.manager.UniversalImageLoaderManager;
import com.lym.twogoods.manager.UniversalImageLoaderOptionManager;
import com.lym.twogoods.screen.GoodsScreen;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.ui.StoreDetailActivity;
import com.lym.twogoods.utils.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * <p>
 * 	基本的商品列表适配器
 * </p>
 * <p>
 * 	所有商品展示列表ListView的Adapter都必须继承自该类
 * </p>
 * 
 * @author 麦灿标
 * */
public class BaseGoodsListViewAdapter extends BaseGoodsListAdapter{

	private final static String TAG = "BaseGoodsListAdapter";
	
	public BaseGoodsListViewAdapter(Activity at, List<Goods> goodsList) {
		super(at, goodsList);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ItemViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ItemViewHolder();
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.app_base_goods_listview_item, null);
			
			viewHolder.base_goods_listview_item_user_layout = (RelativeLayout) convertView.findViewById(R.id.base_goods_listview_item_user_layout);
			viewHolder.base_goods_listview_item_headpic = (ImageView) convertView.findViewById(R.id.base_goods_listview_item_headpic);
			viewHolder.base_goods_listview_item_username = (TextView) convertView.findViewById(R.id.base_goods_listview_item_username);
			viewHolder.base_goods_listview_item_publishtime = (TextView) convertView.findViewById(R.id.base_goods_listview_item_publishtime);
			viewHolder.base_goods_listview_item_publishlocation = (TextView) convertView.findViewById(R.id.base_goods_listview_item_publishlocation);
			viewHolder.base_goods_listview_item_price = (TextView) convertView.findViewById(R.id.base_goods_listview_item_price);
			viewHolder.base_goods_listview_item_operation = (TextView) convertView.findViewById(R.id.base_goods_listview_item_operation);
			viewHolder.base_goods_listview_item_description = (TextView) convertView.findViewById(R.id.base_goods_listview_item_description);
			viewHolder.base_goods_gridview_item_pictures = (GridView) convertView.findViewById(R.id.base_goods_gridview_item_pictures);
			
			//设置头像大小
			PictureThumbnailSpecification headPictureThumbnailSpecification = GoodsScreen.getUserHeadPictureThumbnailSpecification(mActivity);
			LayoutParams params = viewHolder.base_goods_listview_item_headpic.getLayoutParams();
			if(params == null) {
				params = new RelativeLayout.LayoutParams(headPictureThumbnailSpecification.getHeight(), headPictureThumbnailSpecification.getWidth());
				viewHolder.base_goods_listview_item_headpic.setLayoutParams(params);
			} else {
				params.width = headPictureThumbnailSpecification.getWidth();
				params.height = headPictureThumbnailSpecification.getHeight();
			}
			
			convertView.setTag(viewHolder);
		} 
		viewHolder = (ItemViewHolder) convertView.getTag();
		
		Goods item = null;
		try {
			item = (Goods) getItem(position);
		} catch(Exception e) {
			e.printStackTrace();
		}
		//设置基本内容
		setItemContent(viewHolder, item);
		//设置特殊内容
		setCustomContent(viewHolder, item);
		
		return convertView;
		//return LayoutInflater.from(mActivity).inflate(R.layout.app_base_goods_listview_item, null);
	}
	
	private void setItemContent(ItemViewHolder viewHolder, final Goods item) {
		if(viewHolder == null || item == null) {
			return;
		}
		//头像
		ImageLoaderHelper.loadUserHeadPictureThumnail(mActivity, viewHolder.base_goods_listview_item_headpic, item.getHead_url(), null);
		//用户名
		viewHolder.base_goods_listview_item_username.setText(item.getUsername());
		//发布时间
		viewHolder.base_goods_listview_item_publishtime.setText(TimeUtil.getDescriptionTimeFromTimestamp(item.getPublish_time()));
		//发布位置
		viewHolder.base_goods_listview_item_publishlocation.setText(item.getPublish_location());
		//价格,注意要转换为字符串
		viewHolder.base_goods_listview_item_price.setText("￥" + item.getPrice());
		//商品描述
		viewHolder.base_goods_listview_item_description.setText(item.getDescription());
		//左右可滑动图片
		//final ArrayList<String> picturesUrlList = item.getPictureUrlList();
		final ArrayList<String> picturesUrlList = item.getPicFileUrlList();
		if(picturesUrlList != null && picturesUrlList.size() > 0) {
			GoodsPictureListAdapter adapter = new GoodsPictureListAdapter(mActivity, picturesUrlList);
			PictureThumbnailSpecification goodsPictureThumbnailSpecification = GoodsScreen.getIndexGoodsPictureThumbnailSpecification(mActivity);
			LayoutParams params = viewHolder.base_goods_gridview_item_pictures.getLayoutParams();
			int picturesSize = picturesUrlList.size();
			//用getHorizontalSpacing()要求API Level为16或以上
			params.width = goodsPictureThumbnailSpecification.getWidth() *  picturesSize
					+ mActivity.getResources().getDimensionPixelSize(R.dimen.app_base_goods_listview_item_picture_interval) * (picturesSize <= 0 ? 0 : picturesSize - 1);
			viewHolder.base_goods_gridview_item_pictures.setNumColumns(picturesUrlList.size());
			viewHolder.base_goods_gridview_item_pictures.setAdapter(adapter);
			//点击事件
			viewHolder.base_goods_gridview_item_pictures.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(mActivity, DisplayPicturesActivity.class);
					intent.putStringArrayListExtra("picturesUrlList", picturesUrlList);
					intent.putExtra("currentIndex", position);
					mActivity.startActivity(intent);
				}
			});
		} else {//防止View重用错乱
			viewHolder.base_goods_gridview_item_pictures.setAdapter(null);
		}
		//用户相关信息子布局点击事件,跳转到某一用户主页
		viewHolder.base_goods_listview_item_user_layout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					break;
				case MotionEvent.ACTION_UP:
					Intent intent = new Intent(mActivity, StoreDetailActivity.class);
					User user = buildUserByGoods(item);
					intent.putExtra("user", user);
					mActivity.startActivity(intent);
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	/**
	 *	<p>
	 *		子类设置每个Item自定义内容
	 *	</p> 
	 * 
	 * @param viewHolder 每个Item的ViewHolder
	 * @param item 相应的实体对象
	 * 
	 * */
	protected void setCustomContent(ItemViewHolder viewHolder, Goods item) {
	}
	
	//由Goods对象转User对象
	private User buildUserByGoods(Goods g) {
		if(g == null) {
			return null;
		}
		User user = new User();
		user.setUsername(g.getUsername());
		user.setHead_url(g.getHead_url());
		return user;
	}
	
	@SuppressWarnings("unused")
	protected static class ItemViewHolder {
		
		/** 用户相关信息子布局 */
		public RelativeLayout base_goods_listview_item_user_layout;
		
		/** 头像 */
		public ImageView base_goods_listview_item_headpic;
		
		/** 用户名 */
		public TextView base_goods_listview_item_username;
		
		/** 发布时间 */
		public TextView base_goods_listview_item_publishtime;
		
		/** 发布位置 */
		public TextView base_goods_listview_item_publishlocation;
		
		/** 价格 */
		public TextView base_goods_listview_item_price;
		
		/** 操作 */
		public TextView base_goods_listview_item_operation;
		
		/** 商品描述 */
		public TextView base_goods_listview_item_description;
		
		/** 左右可滑动图片 */
		public GridView base_goods_gridview_item_pictures;
	}
}













