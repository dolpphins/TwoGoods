package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.GoodsCategory;
import com.lym.twogoods.config.GoodsCategory.Category;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.fragment.base.HeaderPullListFragment;
import com.lym.twogoods.index.adapter.CategoryGridViewAdapter;
import com.lym.twogoods.index.adapter.IndexGoodsListAdapter;
import com.lym.twogoods.index.adapter.SortListViewAdapter;
import com.lym.twogoods.index.interf.DropDownAble;
import com.lym.twogoods.index.manager.GoodsSortManager;
import com.lym.twogoods.index.manager.GoodsSortManager.GoodsSort;
import com.lym.twogoods.index.widget.DropdownLinearLayout;
import com.lym.twogoods.index.widget.MaskLayer;
import com.lym.twogoods.local.bean.LocalGoods;
import com.lym.twogoods.ui.GoodsDetailActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * <p>主页Fragment</p>
 * 
 * @author 麦灿标
 * */
public class IndexFragment extends HeaderPullListFragment implements DropDownAble {

	private final static String TAG = "IndexFragment";
	
	//外层帧布局
	private FrameLayout frameLayout;
	
	/**
	 * 头部
	 * */
	//头部高度
	private int mRealHeadHeight;
	private View realHead;
	
	/**
	 * 头部分类相关
	 * */
	private LinearLayout index_fragment_head_category;
	private ImageView index_fragment_head_category_iv;
	private GridView index_fragment_head_category_dropdown_gv;
	private CategoryGridViewAdapter categoryAdapter;
	private List<Category> categoryData;
	private DropdownLinearLayout categoryDropdownLayout;
	//标记是否正在显示分类下拉布局
	private boolean isShowingCategoryLayout;
	//分类下拉布局高度
	private int mCategoryDropdownLayoutHeight;
	
	/**
	 * 头部排序相关
	 * */
	private LinearLayout index_fragment_head_sort;
	private ImageView index_fragment_head_sort_iv;
	private ListView index_fragment_head_sort_dropdown_lv;
	private SortListViewAdapter sortAdapter;
	private List<GoodsSort> sortData;
	private DropdownLinearLayout sortDropdownLayout;
	//标记是否正在显示排序下拉布局
	private boolean isShowingSortLayout;
	//排序下拉布局高度
	private int mSortDropdownLayoutHeight;
	
	/**
	 * 遮罩层
	 * */
	private MaskLayer maskLayer;
	
	
	/**
	 * 商品列表 
	 * */
	//商品ListView适配器
	private IndexGoodsListAdapter mListViewAdapter;
	//商品数据List
	private List<Goods> mGoodsList = new ArrayList<Goods>();
	//当前分类
	private Category mCurrentCategory;
	private int mCategoryPosition;
	//
	private GoodsSort mCurrentGoodsSort;
	private int mGoodsSortPosition;
	
	//获取数据相关
	private long mPerPageCount = 10;
	private int mCurrentPageIndex = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initData();
	}
	
	@Override
	protected View onCreateHeaderView() {
		//仅占位作用
		View v = mAttachActivity.getLayoutInflater().inflate(R.layout.index_fragment_head_layout, null);
		v.setVisibility(View.INVISIBLE);		
		return v;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		ViewGroup wrapper = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
		//外层帧布局
		frameLayout = new FrameLayout(mAttachActivity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		frameLayout.setLayoutParams(params);
		
		//遮罩层
		maskLayer = new MaskLayer(mAttachActivity);
		maskLayer.setOnTouchDropDownAbleListener(this);
		//分类下拉布局
		categoryDropdownLayout = (DropdownLinearLayout) inflater.inflate(R.layout.index_fragment_head_category_dropdown_layout, null);
		//排序下拉布局
		sortDropdownLayout = (DropdownLinearLayout) inflater.inflate(R.layout.index_fragment_head_sort_dropdown_layout, null);
		
		//真正的头部
		realHead = inflater.inflate(R.layout.index_fragment_head_layout, null);
		//设置布局参数
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		categoryDropdownLayout.setLayoutParams(lp);
		sortDropdownLayout.setLayoutParams(lp);
		realHead.setLayoutParams(lp);
		LayoutParams maskLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		maskLayer.setLayoutParams(maskLp);
		maskLayer.hide();//隐藏遮罩层
		
		//隐藏头部下拉布局
		hideDropdownView();
		categoryDropdownLayout.setAlpha(0);//默认不可见
		sortDropdownLayout.setAlpha(0);//默认不可见
		
		//GridView
		index_fragment_head_category_dropdown_gv = (GridView) categoryDropdownLayout.findViewById(R.id.index_fragment_head_category_dropdown_gv);
		//ListView
		index_fragment_head_sort_dropdown_lv = (ListView) sortDropdownLayout.findViewById(R.id.index_fragment_head_sort_dropdown_lv);
		
		index_fragment_head_category = (LinearLayout) realHead.findViewById(R.id.index_fragment_head_category);
		index_fragment_head_sort = (LinearLayout) realHead.findViewById(R.id.index_fragment_head_sort);
		index_fragment_head_category_iv = (ImageView) realHead.findViewById(R.id.index_fragment_head_category_iv);
		index_fragment_head_sort_iv = (ImageView) realHead.findViewById(R.id.index_fragment_head_sort_iv);
		
		initView();
		
		frameLayout.addView(wrapper);
		frameLayout.addView(maskLayer);
		frameLayout.addView(categoryDropdownLayout);
		frameLayout.addView(sortDropdownLayout);
		frameLayout.addView(realHead);
		
		//为头部设置点击事件
		setOnclickForHeadLayout();
		//为ListView设置事件
		setOnclickForListView();
		
		return frameLayout;
	}
	
	private void hideDropdownView() {
		categoryDropdownLayout.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				if(mCategoryDropdownLayoutHeight <= 0 || mRealHeadHeight <= 0) {
					mCategoryDropdownLayoutHeight = categoryDropdownLayout.getHeight();
					mRealHeadHeight = realHead.getHeight();
					if(mCategoryDropdownLayoutHeight > 0 && mRealHeadHeight > 0) {
						//设置初始位置
						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						lp.setMargins(0, mRealHeadHeight, 0, 0);
						categoryDropdownLayout.setLayoutParams(lp);
						//开始执行隐藏动画
						hideHeadDropdownLayoutAnimation(categoryDropdownLayout, -mCategoryDropdownLayoutHeight);
						isShowingCategoryLayout = false;
					}
				}
				return true;//一定要返回true,原因待查
			}
		});
		
		sortDropdownLayout.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				if(mSortDropdownLayoutHeight <= 0 || mRealHeadHeight <= 0) {
					mSortDropdownLayoutHeight = sortDropdownLayout.getHeight();
					mRealHeadHeight = realHead.getHeight();
					if(mSortDropdownLayoutHeight > 0 && mRealHeadHeight > 0) {
						//设置初始位置
						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						lp.setMargins(0, mRealHeadHeight, 0, 0);
						sortDropdownLayout.setLayoutParams(lp);
						//开始执行隐藏动画
						hideHeadDropdownLayoutAnimation(sortDropdownLayout, -mSortDropdownLayoutHeight);
						isShowingSortLayout = false;
					}
				}
				return true;//一定要返回true,原因待查
			}
		});
		
	}
	
	private void initData() {
		//分类下拉布局数据
		categoryData = new ArrayList<Category>();
		Category[] categorys = GoodsCategory.Category.values();
		if(categorys != null) {
			int i = 0;
			for(Category c : categorys) {
				categoryData.add(c);
				if(c.equals(Category.ALL)) {
					mCurrentCategory = Category.ALL;//默认为所有
					mCategoryPosition = i;
				}
				i++;
			}
		}
		categoryAdapter = new CategoryGridViewAdapter(mAttachActivity, categoryData);
		categoryAdapter.setDefaultSelectedCategory(mCurrentCategory);//分类默认为所有
		
		//排序下拉布局数据
		sortData = new ArrayList<GoodsSortManager.GoodsSort>();
		GoodsSort[] sorts = GoodsSort.values();
		if(sorts != null) {
			int i = 0;
			for(GoodsSort g : sorts) {
				sortData.add(g);
				if(GoodsSort.NEWEST_PUBLISH.equals(g)) {
					mGoodsSortPosition = i;
					mCurrentGoodsSort = g;
				}
				i++;
			}
		}
		//注意第二三个参数不要搞错
		sortAdapter = new SortListViewAdapter(mAttachActivity, sortData);
		sortAdapter.setDefaultGoodsSort(mCurrentGoodsSort);
		
		mListViewAdapter = new IndexGoodsListAdapter(mAttachActivity, mGoodsList);
		
		//List<Goods> tempList = GoodsData.getGoodsData(mAttachActivity);
		//mGoodsList.addAll(tempList);
		//读取缓存
		OrmDatabaseHelper ormHelper = new OrmDatabaseHelper(mAttachActivity);
		Dao<LocalGoods, Integer> dao = ormHelper.getGoodsDao();
		QueryBuilder<LocalGoods, Integer> builder = dao.queryBuilder();
		//加载所有缓存数据
//		//分页
//		try {
//			builder.offset(mCurrentPageIndex * mPerPageCount);
//			builder.limit(mPerPageCount);
//		} catch(Exception e) {
//			e.printStackTrace();
//			//不支持分页
//		}
		//查询指定分类
		String all = getResources().getString(R.string.category_all);
		String currentCategoryString = GoodsCategory.getString(mAttachActivity, mCurrentCategory);
		if(!all.equals(currentCategoryString)) {
			try {
				builder.where().eq("categoty", currentCategoryString);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		//查询结果排序
		String sort = GoodsSortManager.getColumnString(mCurrentGoodsSort);
		builder.orderBy(sort, true);
		//开始查询
		try {
			List<LocalGoods> goodsList = builder.query();
			if(goodsList != null) {
				Log.i(TAG, "database query size:" + goodsList.size());
				int size = goodsList.size();
				for(int i = 0; i < size; i++) {
					mGoodsList.add(LocalGoods.toGoods(goodsList.get(i)));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
//		BmobQuery<Goods> query = new BmobQuery<Goods>();
//		query.setSkip(mCurrentPageIndex * mPerPageCount);
//		query.setLimit(mPerPageCount);
//		String all = getResources().getString(R.string.category_all);
//		String currentCategoryString = GoodsSortManager.getString(mAttachActivity, mCurrentGoodsSort);
//		if(!all.equals(currentCategoryString)) {
//			query.addWhereEqualTo("category", currentCategoryString);
//		}
//		query.findObjects(mAttachActivity, new FindListener<Goods>() {
//			
//			@Override
//			public void onSuccess(List<Goods> goodsList) {
//				if(goodsList == null) {
//					
//				} else if(goodsList.size() <= 0) {
//					
//				} else if(goodsList.size() < mPerPageCount) {
//					mGoodsList.addAll(goodsList);
//				} else {
//					mGoodsList.addAll(goodsList);
//				}
//			}
//			
//			@Override
//			public void onError(int arg0, String arg1) {
//				
//			}
//		});
	}
	
	private void initView() {

		index_fragment_head_category_dropdown_gv.setAdapter(categoryAdapter);
		index_fragment_head_sort_dropdown_lv.setAdapter(sortAdapter);
		mListView.setAdapter(mListViewAdapter);
	}
	
	
	//为头部布局设置所有点击事件
	private void setOnclickForHeadLayout() {
		if(index_fragment_head_category != null) {
			//分类点击事件
			index_fragment_head_category.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//预处理
					if(isShowingSortLayout) {
						filpUpArrowAnimation(index_fragment_head_sort_iv);
						hideDropdownAnimation(sortDropdownLayout, -mSortDropdownLayoutHeight);
						isShowingSortLayout = false;
					}
					
					if(!isShowingCategoryLayout) {
						//箭头从上到下翻转动画
						filpDownArrowAnimation(index_fragment_head_category_iv);
						//显示分类下拉布局动画
						showDropdownAnimation(categoryDropdownLayout, -mCategoryDropdownLayoutHeight);
						//显示遮罩层
						maskLayer.show();
						isShowingCategoryLayout = true;
					} else {
						//箭头从下到上翻转动画
						filpUpArrowAnimation(index_fragment_head_category_iv);
						//隐藏分类下拉布局动画
						hideDropdownAnimation(categoryDropdownLayout, -mCategoryDropdownLayoutHeight);
						//隐藏遮罩层
						maskLayer.hide();
						isShowingCategoryLayout = false;
					}
				}
			});
		}
		
		if(index_fragment_head_sort != null) {
			//排序点击事件
			index_fragment_head_sort.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//预处理
					if(isShowingCategoryLayout) {
						filpUpArrowAnimation(index_fragment_head_category_iv);
						hideDropdownAnimation(categoryDropdownLayout, -mCategoryDropdownLayoutHeight);
						isShowingCategoryLayout = false;
					}
					
					if(!isShowingSortLayout) {
						//箭头从上到下翻转动画
						filpDownArrowAnimation(index_fragment_head_sort_iv);
						//显示分类下拉布局动画
						showDropdownAnimation(sortDropdownLayout, -mSortDropdownLayoutHeight);
						//显示遮罩层
						maskLayer.show();
						isShowingSortLayout = true;
					} else {
						//箭头从下到上翻转动画
						filpUpArrowAnimation(index_fragment_head_sort_iv);
						//隐藏分类下拉布局动画
						hideDropdownAnimation(sortDropdownLayout, -mSortDropdownLayoutHeight);
						//隐藏遮罩层
						maskLayer.hide();
						isShowingSortLayout = false;
					}
				}
			});
		}
		
		//分类下拉布局GridView
		if(index_fragment_head_category_dropdown_gv != null) {
			index_fragment_head_category_dropdown_gv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					categoryAdapter.setCategoryItemStatus(mCategoryPosition, false); 
					mCategoryPosition = position;
					mCurrentCategory = categoryAdapter.getCurrentCategory(mCategoryPosition);
					categoryAdapter.setCategoryItemStatus(mCategoryPosition, true);
					
					filpUpArrowAnimation(index_fragment_head_category_iv);
					hideDropdownAnimation(categoryDropdownLayout, -mCategoryDropdownLayoutHeight);
					isShowingCategoryLayout = false;
					maskLayer.hide();
				}
			});
		}
		
		//
		if( index_fragment_head_sort_dropdown_lv != null ) {
			index_fragment_head_sort_dropdown_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					sortAdapter.setItemStatus(mGoodsSortPosition, false);
					mGoodsSortPosition = position;
					mCurrentGoodsSort = sortAdapter.getCurrentGoodsSort(mGoodsSortPosition);
					sortAdapter.setItemStatus(mGoodsSortPosition, true);
					
					filpUpArrowAnimation(index_fragment_head_sort_iv);
					hideDropdownAnimation(sortDropdownLayout, -mSortDropdownLayoutHeight);
					isShowingSortLayout = false;
					maskLayer.hide();
				}
			});
		}
	}
	
	//初始隐藏头部下拉布局动画
	private void hideHeadDropdownLayoutAnimation(View v, int offset) {
		if(v != null) {
			TranslateAnimation tranAnim = new TranslateAnimation(0, 0, 0, offset);
			tranAnim.setDuration(1);
			tranAnim.setFillAfter(true);
			v.startAnimation(tranAnim);
			((DropdownLinearLayout)v).requestAllowDispatchTouchEvent(false);//设置不允许分发事件
		}
	}

	//箭头从上到下翻转动画
	private void filpDownArrowAnimation(ImageView iv) {
		if(iv != null) {
			//箭头翻转动画
			Animation flipAnim = AnimationUtils.loadAnimation(mAttachActivity, R.anim.index_fragment_head_arrow_flip_down);
			flipAnim.setFillAfter(true);
			iv.startAnimation(flipAnim);
		}
	}
	
	//箭头从下到上翻转动画
	private void filpUpArrowAnimation(ImageView iv) {
		if(iv != null) {
			//箭头翻转动画
			Animation flipAnim = AnimationUtils.loadAnimation(mAttachActivity, R.anim.index_fragment_head_arrow_flip_up);
			flipAnim.setFillAfter(true);
			iv.startAnimation(flipAnim);
		}
	}
	
	//头部下拉布局显示动画
	private void showDropdownAnimation(View v, int offset) {
		if(v != null) {
			v.setVisibility(View.VISIBLE);
			//先设置透明度为1.0完全不透明
			v.setAlpha(1.0f);
			
			TranslateAnimation tranAnim = new TranslateAnimation(0, 0, offset, 0);
			tranAnim.setFillAfter(true);
			tranAnim.setDuration(300);
			v.startAnimation(tranAnim);
			((DropdownLinearLayout)v).requestAllowDispatchTouchEvent(true);//设置允许分发事件
		}
	}
	
	//头部下拉布局隐藏动画
	private void hideDropdownAnimation(View v, int offset) {
		if(v != null) {
			TranslateAnimation tranAnim = new TranslateAnimation(0, 0, 0, offset);
			tranAnim.setFillAfter(true);
			tranAnim.setDuration(300);
			v.startAnimation(tranAnim);
			((DropdownLinearLayout)v).requestAllowDispatchTouchEvent(false);//设置不允许分发事件
		}
	}
	
	@Override
	public void hideAllDropdownAnimation() {
		if(isShowingCategoryLayout) {
			filpUpArrowAnimation(index_fragment_head_category_iv);
			hideDropdownAnimation(categoryDropdownLayout, -mCategoryDropdownLayoutHeight);
			isShowingCategoryLayout = false;
		}
		if(isShowingSortLayout) {
			filpUpArrowAnimation(index_fragment_head_sort_iv);
			hideDropdownAnimation(sortDropdownLayout, -mSortDropdownLayoutHeight);
			isShowingSortLayout = false;
		}
	}
	
	/**
	 * <p>
	 * 	是否正在显示分类下拉布局
	 * </p>
	 * 
	 * @return 正在显示返回true,否则返回false.
	 * */
	public boolean isShowingCategoryLayout() {
		return isShowingCategoryLayout;
	}
	
	/**
	 * <p>
	 * 	是否正在显示排序下拉布局
	 * </p>
	 * 
	 * @return 正在显示返回true,否则返回false.
	 * */
	public boolean isShowingSortLayout() {
		return isShowingSortLayout;
	}
	
	private void setOnclickForListView() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(TAG, "onItemClick position:" + position);
				//注意position从1开始
				Intent intent = new Intent(mAttachActivity, GoodsDetailActivity.class);
				intent.putExtra("goods", mGoodsList.get(position - 1));
//				ArrayList<String> picturesUrlList = new ArrayList<String>();
//				picturesUrlList.add("http://cdn.pcbeta.attachment.inimc.com/data/attachment/forum/201312/31/181215p0jnzo60mp0oenz0.jpg");
//				picturesUrlList.add("http://download.pchome.net/wallpaper/pic-1853-4-1024x768.jpg");
//				picturesUrlList.add("http://img2.imgtn.bdimg.com/it/u=2110278011,3816929467&fm=21&gp=0.jpg");
//				picturesUrlList.add("http://img5.imgtn.bdimg.com/it/u=1457497701,2659227830&fm=21&gp=0.jpg");
//				picturesUrlList.add("http://img3.imgtn.bdimg.com/it/u=4028451469,3760090854&fm=21&gp=0.jpg");
//				intent.putStringArrayListExtra("picturesUrlList", picturesUrlList);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onRefresh() {
		super.onRefresh();
		System.out.println("onRefresh");
		
		
		
		
	}
	
	@Override
	public void onLoadMore() {
		super.onLoadMore();
		System.out.println("onLoadMore");
	}
	
}
