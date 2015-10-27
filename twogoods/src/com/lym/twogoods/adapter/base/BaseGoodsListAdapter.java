package com.lym.twogoods.adapter.base;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.screen.GoodsScreen;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.utils.ImageUtil;
import com.lym.twogoods.utils.TimeUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
public class BaseGoodsListAdapter extends BaseAdapter{

	private final static String TAG = "BaseGoodsListAdapter";
	
	private Activity mActivity;
	
	private List<Goods> mGoodsList;
	
	public BaseGoodsListAdapter(Activity at, List<Goods> goodsList) {
		super();
		mActivity = at;
		mGoodsList = goodsList;
		
		
	}
	
	@Override
	public int getCount() {
		Log.i(TAG, "count:" + mGoodsList.size());
		if(mGoodsList != null) {
			return mGoodsList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mGoodsList != null && position < mGoodsList.size()) {
			return mGoodsList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ItemViewHolder viewHolder = new ItemViewHolder();
		if(convertView == null) {
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.app_base_goods_listview_item, null);
			
			viewHolder.base_goods_listview_item_headpic = (ImageView) convertView.findViewById(R.id.base_goods_listview_item_headpic);
			viewHolder.base_goods_listview_item_username = (TextView) convertView.findViewById(R.id.base_goods_listview_item_username);
			viewHolder.base_goods_listview_item_publishtime = (TextView) convertView.findViewById(R.id.base_goods_listview_item_publishtime);
			viewHolder.base_goods_listview_item_publishlocation = (TextView) convertView.findViewById(R.id.base_goods_listview_item_publishlocation);
			viewHolder.base_goods_listview_item_price = (TextView) convertView.findViewById(R.id.base_goods_listview_item_price);
			viewHolder.base_goods_listview_item_operation = (TextView) convertView.findViewById(R.id.base_goods_listview_item_operation);
			viewHolder.base_goods_listview_item_description = (TextView) convertView.findViewById(R.id.base_goods_listview_item_description);
			viewHolder.base_goods_gridview_item_pictures = (GridView) convertView.findViewById(R.id.base_goods_gridview_item_pictures);
			
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
		setCustomContent(viewHolder);
		
		return convertView;
	}
	
	private void setItemContent(ItemViewHolder viewHolder, Goods item) {
		if(viewHolder == null || item == null) {
			return;
		}
//		//之后要换成直接调用工具类ImageUtil方法
		String path = item.getHead_url();
//		Bitmap bm = BitmapFactory.decodeFile(path);
		PictureThumbnailSpecification headPictureThumbnailSpecification = GoodsScreen.getUserHeadPictureThumbnailSpecification(mActivity);
		Bitmap bm = ImageUtil.getImageThumbnail(path, headPictureThumbnailSpecification.getWidth(), headPictureThumbnailSpecification.getHeight());
		
		//头像
		viewHolder.base_goods_listview_item_headpic.setImageBitmap(bm);
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
		final ArrayList<String> picturesUrlList = new ArrayList<String>();
		int pictureCount = item.getPic_num();
		for(int i = 0; i < pictureCount; i++) {
			String s = item.getPic_baseurl() + item.getPic_prefix() + "_" + i + ".jpg";
			picturesUrlList.add(s);
		}
		GoodsPictureListAdapter adapter = new GoodsPictureListAdapter(mActivity, picturesUrlList);
		PictureThumbnailSpecification goodsPictureThumbnailSpecification = GoodsScreen.getGoodsPictureThumbnailSpecification(mActivity);
		LayoutParams params = viewHolder.base_goods_gridview_item_pictures.getLayoutParams();
		params.width = goodsPictureThumbnailSpecification.getWidth() * picturesUrlList.size();
		viewHolder.base_goods_gridview_item_pictures.setNumColumns(picturesUrlList.size());
		viewHolder.base_goods_gridview_item_pictures.setAdapter(adapter);
		//点击事件
		viewHolder.base_goods_gridview_item_pictures.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mActivity, DisplayPicturesActivity.class);
				intent.putStringArrayListExtra("picturesUrlList", picturesUrlList);
				mActivity.startActivity(intent);
			}
		});
	}
	
	/**
	 *	<p>
	 *		子类设置每个Item自定义内容
	 *	</p> 
	 * 
	 * @param viewHolder 每个Item的ViewHolder
	 * 
	 * */
	protected void setCustomContent(ItemViewHolder viewHolder) {
	}
	
	
	@SuppressWarnings("unused")
	private static class ItemViewHolder {
		
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













