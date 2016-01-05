package com.lym.twogoods.network;

import java.util.List;

import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.BaseFragment;

import android.widget.AbsListView;

/**
 * GridView数据加载器
 * 
 * @author mao
 *
 */
public class GridViewLoader extends AbsListViewLoader{

	public GridViewLoader(BaseFragment fragment, AbsListView absListView, BaseGoodsListAdapter adapter,
			List<Goods> goodsList) {
		super(fragment, absListView, adapter, goodsList);
	}

}
