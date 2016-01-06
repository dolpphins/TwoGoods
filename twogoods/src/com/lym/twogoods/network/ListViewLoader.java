package com.lym.twogoods.network;

import java.util.ArrayList;
import java.util.List;

import com.lym.loader.HeadPictureLoader;
import com.lym.loader.HeadPictureLoader.OnLoadHeadPictureUrlFinishListener;
import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsListViewAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsListViewAdapter.ItemViewHolder;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.BaseFragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * ListView数据加载器
 * 
 * @author mao
 *
 */
public class ListViewLoader extends AbsListViewLoader {

	public ListViewLoader(BaseFragment fragment, AbsListView absListView, BaseGoodsListAdapter adapter,
			List<Goods> goodsList) {
		super(fragment, absListView, adapter, goodsList);
	}
	
	@Override
	protected void onLoadHeadPicture(AbsListView view) {

		//从Header算起
		int firstIndex = view.getFirstVisiblePosition();
		int lastIndex = view.getLastVisiblePosition();
		int visibleItemCount = lastIndex - firstIndex + 1;
		
		//如果停止滚动,那么开始异步加载数据,包括头像，图片等
		requestLoadHeadPicture(firstIndex, visibleItemCount);
	}
	
	/**
	 * 请求加载头像,该方法只有在列表处于静止(没有滚动)状态才会加载头像
	 * 
	 * @param start 从start位置开始
	 * @param count 指定要加载的Item数
	 */
	public void requestLoadHeadPicture(int start, int count) {
		if(mScrollStatus == OnScrollListener.SCROLL_STATE_IDLE) {
			//头像
			List<HeadPictureLoader.HeadPictureTask> tasks = new ArrayList<HeadPictureLoader.HeadPictureTask>();
			//
			for(int i = 0; i < count && i < mAbsListView.getChildCount(); i++) {
				//getChildAt并没有包含Header
				View v = mAbsListView.getChildAt(i);
				//System.out.println(v == null ? "v null" : "v not null");
				if(v != null) {
					BaseGoodsListViewAdapter.ItemViewHolder holder = (ItemViewHolder) v.getTag();
					//holder可能为空
					//System.out.println(holder == null ? "holder null" : "holder not null");
					if(holder != null) {
						int index = start + i - 1;
						if(index < 0 || index >= mGoodsList.size()) {
							continue;
						}
						Goods goods = mGoodsList.get(index);
						String username = holder.username;
						if(!TextUtils.isEmpty(username) && username.equals(goods.getUsername())) {
							continue;
						}
						holder.username = goods.getUsername();
						HeadPictureLoader.HeadPictureTask task = new HeadPictureLoader.HeadPictureTask();
						task.setGUID(goods.getGUID());
						task.setUsername(goods.getUsername());
						task.setIv(holder.base_goods_listview_item_headpic);
						tasks.add(task);
					}
				}
			}
			if(tasks.size() > 0) {
				HeadPictureLoader.getInstance().submit(mFragment.getActivity().getApplicationContext(), tasks, new OnLoadHeadPictureUrlFinishListener() {
					
					@Override
					public void onFinish(String username, String url) {
						if(!TextUtils.isEmpty(url)) {
							for(Goods goods : mGoodsList) {
								String goodsUsername = goods.getUsername();
								if(goodsUsername.equals(username)) {
									goods.setHead_url(url);
								}
							}	
						}
					}
				});
			}
		}
	}
}
