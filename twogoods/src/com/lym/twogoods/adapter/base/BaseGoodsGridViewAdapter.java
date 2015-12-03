package com.lym.twogoods.adapter.base;

import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.PullGridViewFragment;
import com.lym.twogoods.manager.ThumbnailMapManager;
import com.lym.twogoods.screen.GoodsScreen;
import com.lym.twogoods.utils.TimeUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 基本的以GridView形式展示的商品适配器
 * 
 * @author 麦灿标
 *
 */
public class BaseGoodsGridViewAdapter extends BaseGoodsListAdapter {

	private final static String TAG = "BaseGoodsGridViewAdapter";
	
	private PullGridViewFragment mFragment;
	
	public BaseGoodsGridViewAdapter(Activity at, List<Goods> goodsList, PullGridViewFragment fragment) {
		super(at, goodsList);
		mFragment = fragment;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.app_base_goods_gridview_item, null);
			viewHolder = new ItemViewHolder();
			
			viewHolder.app_base_goods_gridview_item_iv = (ImageView) convertView.findViewById(R.id.app_base_goods_gridview_item_iv);
			viewHolder.app_base_goods_gridview_item_description = (TextView) convertView.findViewById(R.id.app_base_goods_gridview_item_description);
			viewHolder.app_base_goods_gridview_item_price = (TextView) convertView.findViewById(R.id.app_base_goods_gridview_item_price);
			viewHolder.app_base_goods_gridview_item_publishlocation = (TextView) convertView.findViewById(R.id.app_base_goods_gridview_item_publishlocation);
			viewHolder.app_base_goods_gridview_item_publishtime = (TextView) convertView.findViewById(R.id.app_base_goods_gridview_item_publishtime);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}
		
		setItemContent(viewHolder, (Goods)getItem(position));
		
		return convertView;
	}
	
	private void setItemContent(ItemViewHolder viewHolder, Goods item) {
		if(viewHolder == null || item == null) {
			return;
		}
		//第一张商品图片
		String url = item.getPictureUrlList().size() > 0 ? item.getPictureUrlList().get(0) : null;
		LayoutParams params = viewHolder.app_base_goods_gridview_item_iv.getLayoutParams();
		if(params == null) {
			params = new LinearLayout.LayoutParams(0, 0);
			viewHolder.app_base_goods_gridview_item_iv.setLayoutParams(params);
		}
		params.width = params.height = GoodsScreen.getNearbyGoodsPictureThumbnailSpecification(mActivity, mFragment.getHorizontalExtraDistance()).getWidth();
		String bmobFileName = null;
		List<String> picFileUrlList = item.getPicFileUrlList();
		if(picFileUrlList != null && picFileUrlList.size() > 0) {
			bmobFileName = picFileUrlList.get(0);
		}
		ThumbnailMapManager.loadGoodsPictureThumnail(mActivity, bmobFileName, viewHolder.app_base_goods_gridview_item_iv, ThumbnailMapManager.DisplayType.GridViewType);
		//描述
		viewHolder.app_base_goods_gridview_item_description.setText(item.getDescription());
		//价格
		viewHolder.app_base_goods_gridview_item_price.setText("￥" + item.getPrice());
		//发布位置
		viewHolder.app_base_goods_gridview_item_publishlocation.setText(item.getPublish_location());
		//发布时间
		viewHolder.app_base_goods_gridview_item_publishtime.setText(TimeUtil.getDescriptionTimeFromTimestamp(item.getPublish_time()));
	}
	
	private static class ItemViewHolder {
		
		private ImageView app_base_goods_gridview_item_iv;
		
		private TextView app_base_goods_gridview_item_description;
		
		private TextView app_base_goods_gridview_item_price;
		
		private TextView app_base_goods_gridview_item_publishlocation;
		
		private TextView app_base_goods_gridview_item_publishtime;
	}

}
