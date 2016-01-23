package com.lym.twogoods.adapter.base;

import java.util.ArrayList;
import java.util.List;

import com.lym.loader.HeadPictureLoader;
import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.screen.GoodsScreen;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.ui.StoreDetailActivity;
import com.lym.twogoods.utils.StringUtil;
import com.lym.twogoods.utils.TimeUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
	
	private OnItemViewClickListener mOnItemViewClickListener;
	
	public BaseGoodsListViewAdapter(Context context, List<Goods> goodsList) {
		super(context, goodsList);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//System.out.println("getView");
		//android.os.Debug.startMethodTracing();
		ItemViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ItemViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.app_base_goods_listview_item, parent, false);
			
			viewHolder.base_goods_listview_item_user_layout = (RelativeLayout) convertView.findViewById(R.id.base_goods_listview_item_user_layout);
			viewHolder.base_goods_listview_item_headpic = (ImageView) convertView.findViewById(R.id.base_goods_listview_item_headpic);
			viewHolder.base_goods_listview_item_username = (TextView) convertView.findViewById(R.id.base_goods_listview_item_username);
			viewHolder.base_goods_listview_item_publishtime = (TextView) convertView.findViewById(R.id.base_goods_listview_item_publishtime);
			viewHolder.base_goods_listview_item_publishlocation = (TextView) convertView.findViewById(R.id.base_goods_listview_item_publishlocation);
			viewHolder.base_goods_listview_item_price = (TextView) convertView.findViewById(R.id.base_goods_listview_item_price);
			viewHolder.base_goods_listview_item_operation = (TextView) convertView.findViewById(R.id.base_goods_listview_item_operation);
			viewHolder.base_goods_listview_item_description = (TextView) convertView.findViewById(R.id.base_goods_listview_item_description);
			viewHolder.base_goods_gridview_item_pictures = (GridView) convertView.findViewById(R.id.base_goods_gridview_item_pictures);
			
			//同一设置默认样式
			setDefaultStyle(viewHolder);
			
			//设置头像大小
			PictureThumbnailSpecification headPictureThumbnailSpecification = GoodsScreen.getUserHeadPictureThumbnailSpecification(mContext);
			LayoutParams params = viewHolder.base_goods_listview_item_headpic.getLayoutParams();
			if(params == null) {
				params = new RelativeLayout.LayoutParams(headPictureThumbnailSpecification.getHeight(), headPictureThumbnailSpecification.getWidth());
				viewHolder.base_goods_listview_item_headpic.setLayoutParams(params);
			} else {
				params.width = headPictureThumbnailSpecification.getWidth();
				params.height = headPictureThumbnailSpecification.getHeight();
			}
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}
		
		Goods item = null;
		try {
			item = (Goods) getItem(position);
		} catch(Exception e) {
			e.printStackTrace();
		}
		//先清除View缓存
		clearViewCache(viewHolder, item);
		//设置基本内容
		setItemContent(viewHolder, item);
		//设置特殊内容
		setCustomContent(viewHolder, item);
		//android.os.Debug.stopMethodTracing();
		
		return convertView;
	}
	
	private void setItemContent(final ItemViewHolder viewHolder, final Goods item) {
		if(viewHolder == null || item == null) {
			return;
		}
		//ImageLoaderHelper.loadUserHeadPictureThumnail(mActivity, viewHolder.base_goods_listview_item_headpic, "", null);
		//头像
		//android.os.Debug.startMethodTracing();
		//卡顿代码
//		if(TextUtils.isEmpty(item.getHead_url())) {
//			BmobQueryHelper.queryHeadPictureByUsername(mActivity, item.getUsername(), new OnUsername2HeadPictureListener() {
//				
//				@Override
//				public void onSuccess(String headUrl) {
//					ImageLoaderHelper.loadUserHeadPictureThumnail(mActivity, viewHolder.base_goods_listview_item_headpic, headUrl, null);
//					//设置缓存
//					List<Goods> goodsList = new ArrayList<Goods>();
//					for(Goods goods : mGoodsList) {
//						if(item.getUsername().equals(goods.getUsername())) {
//							goods.setHead_url(headUrl);
//							goodsList.add(goods);
//						}
//					}
//				}
//				
//				@Override
//				public void onError(int errorcode, String errormsg) {				
//					ImageLoaderHelper.loadUserHeadPictureThumnail(mActivity, viewHolder.base_goods_listview_item_headpic, null, null);
//				}
//			});
//		} else {
//			ImageLoaderHelper.loadUserHeadPictureThumnail(mActivity, viewHolder.base_goods_listview_item_headpic, item.getHead_url(), null);
//		}
		//android.os.Debug.stopMethodTracing();
		//加载头像优化代码
		//完全不管,直接采用默认头像就行,等停止滑动再去加载真正的头像
		
		//用户名
		String time = TimeUtil.getDescriptionTimeFromTimestamp(item.getPublish_time());
		float textSize = mContext.getResources().getDimensionPixelSize(R.dimen.app_base_goods_listview_item_publishtime_textSize);
		int minpixels =  (int) StringUtil.getTextLength(time+"阿尧", textSize);
		viewHolder.base_goods_listview_item_username.setMinWidth(minpixels);
		viewHolder.base_goods_listview_item_username.setText(item.getUsername());
		//发布时间
		viewHolder.base_goods_listview_item_publishtime.setText(time);
		//发布位置
		viewHolder.base_goods_listview_item_publishlocation.setText(item.getPublish_location());
		//价格,注意要转换为字符串
		viewHolder.base_goods_listview_item_price.setText("￥" + item.getPrice());
		//商品描述
		viewHolder.base_goods_listview_item_description.setText(item.getDescription());
		//左右可滑动图片
		final ArrayList<String> picturesUrlList = item.getPictureUrlList();
		final ArrayList<String> picFileUrlList = item.getPicFileUrlList();
		if(picFileUrlList != null && picFileUrlList.size() > 0) {
			//尝试使用缓存的Adapter
			GoodsPictureListAdapter adapter = (GoodsPictureListAdapter) viewHolder.base_goods_gridview_item_pictures.getAdapter();
			if(adapter == null) {
				adapter = new GoodsPictureListAdapter(mContext, picFileUrlList);
			} else {
				adapter.resetData(picFileUrlList);
			}
			PictureThumbnailSpecification goodsPictureThumbnailSpecification = GoodsScreen.getIndexGoodsPictureThumbnailSpecification();
			LayoutParams params = viewHolder.base_goods_gridview_item_pictures.getLayoutParams();
			int picturesSize = picFileUrlList.size();
			//用getHorizontalSpacing()要求API Level为16或以上
			params.width = goodsPictureThumbnailSpecification.getWidth() *  picturesSize
					+ mContext.getResources().getDimensionPixelSize(R.dimen.app_base_goods_listview_item_picture_interval) * (picturesSize <= 0 ? 0 : picturesSize - 1);
			viewHolder.base_goods_gridview_item_pictures.setNumColumns(picFileUrlList.size());
			viewHolder.base_goods_gridview_item_pictures.setAdapter(adapter);
			//点击事件
			viewHolder.base_goods_gridview_item_pictures.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					
					if(mOnItemViewClickListener != null) {
						mOnItemViewClickListener.onClickPictures(picturesUrlList, position);;
					}
					Intent intent = new Intent(mContext, DisplayPicturesActivity.class);
					intent.putStringArrayListExtra("picturesUrlList", picturesUrlList);
					intent.putExtra("currentIndex", position);
					mContext.startActivity(intent);
				}
			});
		}
		//用户相关信息子布局点击事件,跳转到某一用户主页
		viewHolder.base_goods_listview_item_user_layout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					break;
				case MotionEvent.ACTION_UP:
					User user = buildUserByGoods(item);
					if(mOnItemViewClickListener != null) {
						mOnItemViewClickListener.onClickUser(user);
					}
					Intent intent = new Intent(mContext, StoreDetailActivity.class);
					intent.putExtra("user", user);
					mContext.startActivity(intent);
					
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	private void setDefaultStyle(ItemViewHolder viewHolder) {
		viewHolder.base_goods_listview_item_username.setTextColor(Color.BLACK);
		viewHolder.base_goods_listview_item_publishtime.setTextColor(Color.BLACK);
		viewHolder.base_goods_listview_item_publishlocation.setTextColor(Color.BLACK);
		viewHolder.base_goods_listview_item_price.setTextColor(Color.RED);
		viewHolder.base_goods_listview_item_operation.setTextColor(Color.BLACK);
		viewHolder.base_goods_listview_item_description.setTextColor(Color.BLACK);
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
	
	//清除View缓存,否则会发生错乱
	//优化:1.由于text会被直接覆盖,因此为避免UI重复绘制没必要清除
	//    2.如果发现该缓存View显示的头像就是我们新的Item需要显示的头像,那么也不用清除,并做一定的标记
	private void clearViewCache(ItemViewHolder viewHolder, Goods item) {
		if(viewHolder != null && item != null) {
			//该优化有时会导致头像错乱,原因待查
			//String preUsrname = viewHolder.username;
			//if(!TextUtils.isEmpty(preUsrname) && preUsrname.equals(item.getUsername())) {
			//} else {
				boolean mayLoaded = HeadPictureLoader.getInstance().tryLoadFromMenoryCache(mContext, item.getUsername(), viewHolder.base_goods_listview_item_headpic);
				if(!mayLoaded) {
					viewHolder.base_goods_listview_item_headpic.setImageResource(R.drawable.user_default_head);
				}	
			//}
		}
	}
	
	public void setOnItemViewClickListener(OnItemViewClickListener l) {
		mOnItemViewClickListener = l;
	}
	
	public OnItemViewClickListener getOnItemViewClickListener() {
		return mOnItemViewClickListener;
	}
	
	/**
	 * Item View点击事件监听接口
	 * 
	 * @author mao
	 *
	 */
	public static interface OnItemViewClickListener {
		
		/**
		 * 当点击商品列表某一Item的图片缩略图时回调该方法
		 * 
		 * @param pictures 图片url集合
		 * @param position 点击的图片的位置，从0开始
		 */
		void onClickPictures(ArrayList<String> pictures, int position);
		
		/**
		 * 当点击商品列表某一Item的用户区域时回调该方法
		 * 
		 * @param user 对应的用户
		 */
		void onClickUser(User user);
	}
	
	public static class ItemViewHolder {
		
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
		
		/** 该Item上一次显示的数据集Item的username,优化缓存使用 */
		public String username;
	}
}
