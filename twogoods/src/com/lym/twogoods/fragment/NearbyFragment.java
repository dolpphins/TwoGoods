package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.adapter.NearbyAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.Location;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.GoodsCategory;
import com.lym.twogoods.fragment.base.PullGridViewFragment;
import com.lym.twogoods.index.manager.GoodsSortManager;
import com.lym.twogoods.network.AbsListViewLoader;
import com.lym.twogoods.network.ListViewOnLoaderListener;
import com.lym.twogoods.ui.GoodsDetailActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;


/**
 * <p>
 * 	附近Fragment
 * </p>
 * 
 * @author 麦灿标
 * */
public class NearbyFragment extends PullGridViewFragment{

	private final static String TAG = "NearbyFragment";
	
	private String mSelectedCity;
	
	@Override
	public void onStart() {
		super.onStart();
		
		//判断是否需要加载数据
		Location location = UserInfoManager.getInstance().getCurrentLocation();
		if(location != null) {
			String newDescription = location.getDescription();
			if(TextUtils.isEmpty(mSelectedCity) 
					|| (!TextUtils.isEmpty(newDescription)
					&& !mSelectedCity.equals(location.getDescription()))) {
				mSelectedCity = location.getDescription();
				loadDataInit();
			}
		}
	}
	
	@Override
	protected void onCreateViewAfter() {
		super.onCreateViewAfter();
		
	}
	
	@Override
	protected BmobQuery<Goods> onCreateBmobQuery() {
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		Location location = UserInfoManager.getInstance().getCurrentLocation();
		BmobGeoPoint point = new BmobGeoPoint(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
		query.addWhereWithinKilometers("geoPoint", point, 1000000.0);
		return query;
	}
}
