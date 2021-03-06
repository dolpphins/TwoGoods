package com.lym.twogoods.adapter.base;

import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.PullGridViewFragment;
import com.lym.twogoods.manager.ThumbnailMapManager;
import com.lym.twogoods.network.BmobQueryHelper;
import com.lym.twogoods.network.BmobQueryHelper.OnUsername2HeadPictureListener;
import com.lym.twogoods.screen.GoodsScreen;
import com.lym.twogoods.utils.TimeUtil;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
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
	
	public BaseGoodsGridViewAdapter(Context context, List<Goods> goodsList, PullGridViewFragment fragment) {
		super(context, goodsList);
		mFragment = fragment;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.app_base_goods_gridview_item, null);
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
	
	private void setItemContent(ItemViewHolder viewHolder, final Goods item) {
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
		params.width = params.height = GoodsScreen.getNearbyGoodsPictureThumbnailSpecification(mFragment.getHorizontalExtraDistance()).getWidth();
		String bmobFileName = null;
		List<String> picFileUrlList = item.getPicFileUrlList();
		if(picFileUrlList != null && picFileUrlList.size() > 0) {
			bmobFileName = picFileUrlList.get(0);
		}
		ThumbnailMapManager.loadGoodsPictureThumnail(mContext, bmobFileName, viewHolder.app_base_goods_gridview_item_iv, ThumbnailMapManager.DisplayType.GridViewType);
		//描述
		viewHolder.app_base_goods_gridview_item_description.setText(item.getDescription());
		//价格
		viewHolder.app_base_goods_gridview_item_price.setText("￥" + item.getPrice());
		//发布位置
		viewHolder.app_base_goods_gridview_item_publishlocation.setText(item.getPublish_location());
		//发布时间
		viewHolder.app_base_goods_gridview_item_publishtime.setText(TimeUtil.getDescriptionTimeFromTimestamp(item.getPublish_time()));
		
		//重新获取头像地址
		if(TextUtils.isEmpty(item.getHead_url())) {
			BmobQueryHelper.queryHeadPictureByUsername(mContext, item.getUsername(), new OnUsername2HeadPictureListener() {
				
				@Override
				public void onSuccess(String headUrl) {
					//设置缓存
					for(Goods goods : mGoodsList) {
						if(item.getUsername().equals(goods.getUsername())) {
							goods.setHead_url(headUrl);
						}
					}
				}
				@Override
				public void onError(int errorcode, String errormsg) {}
			});
		}
	}
	
	private static class ItemViewHolder {
		
		private ImageView app_base_goods_gridview_item_iv;
		
		private TextView app_base_goods_gridview_item_description;
		
		private TextView app_base_goods_gridview_item_price;
		
		private TextView app_base_goods_gridview_item_publishlocation;
		
		private TextView app_base_goods_gridview_item_publishtime;
	}

}
