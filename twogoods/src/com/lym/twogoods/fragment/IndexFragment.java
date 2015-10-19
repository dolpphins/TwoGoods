package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.GoodsCategory;
import com.lym.twogoods.config.GoodsCategory.Category;
import com.lym.twogoods.fragment.base.HeaderPullListFragment;
import com.lym.twogoods.index.adapter.IndexGoodsListAdapter;
import com.lym.twogoods.index.manager.GoodsSortManager;
import com.lym.twogoods.index.manager.GoodsSortManager.GoodsSort;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
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
public class IndexFragment extends HeaderPullListFragment{

	private final static String TAG = "IndexFragment";
	
	
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
	private ArrayAdapter<String> categoryAdapter;
	private List<String> categoryData;
	private View categoryDropdownLayout;
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
	private ArrayAdapter<String> sortAdapter;
	private List<String> sortData;
	private View sortDropdownLayout;
	//标记是否正在显示排序下拉布局
	private boolean isShowingSortLayout;
	//排序下拉布局高度
	private int mSortDropdownLayoutHeight;
	
	
	/**
	 * 商品列表 
	 * */
	//商品ListView适配器
	private IndexGoodsListAdapter mAdapter;
	//
	private List<Goods> mGoodsList;
	
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
		FrameLayout frameLayout = new FrameLayout(mAttachActivity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		frameLayout.setLayoutParams(params);
		
		//分类下拉布局
		categoryDropdownLayout = inflater.inflate(R.layout.index_fragment_head_category_dropdown_layout, null);
		//排序下拉布局
		sortDropdownLayout = inflater.inflate(R.layout.index_fragment_head_sort_dropdown_layout, null);
		
		//真正的头部
		realHead = inflater.inflate(R.layout.index_fragment_head_layout, null);
		//设置布局参数
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		categoryDropdownLayout.setLayoutParams(lp);
		sortDropdownLayout.setLayoutParams(lp);
		realHead.setLayoutParams(lp);
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
		frameLayout.addView(categoryDropdownLayout);
		frameLayout.addView(sortDropdownLayout);
		frameLayout.addView(realHead);
		
		//设置点击事件
		setOnclickForHeadLayout();
		 
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
						hideHeadDropdownLayoutAnimation(categoryDropdownLayout);
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
						hideHeadDropdownLayoutAnimation(sortDropdownLayout);
						isShowingSortLayout = false;
					}
				}
				return true;//一定要返回true,原因待查
			}
		});
		
	}
	
	private void initData() {
		//分类下拉布局数据
		categoryData = new ArrayList<String>();
		Category[] categorys = GoodsCategory.Category.values();
		if(categorys != null) {
			for(Category c : categorys) {
				categoryData.add(GoodsCategory.getString(mAttachActivity, c));
			}
		}
		categoryAdapter = new ArrayAdapter<String>(mAttachActivity, R.layout.index_fragment_head_category_dropdown_item,
				R.id.index_fragment_head_category_name, categoryData);
		
		//排序下拉布局数据
		sortData = new ArrayList<String>();
		GoodsSort[] sorts = GoodsSort.values();
		if(sorts != null) {
			for(GoodsSort g : sorts) {
				sortData.add(GoodsSortManager.getString(mAttachActivity, g));
			}
		}
		//注意第二三个参数不要搞错
		sortAdapter = new ArrayAdapter<String>(mAttachActivity, R.layout.index_fragment_head_sort_dropdown_item,
				R.id.index_fragment_head_sort_name, sortData);
		
		mGoodsList = new ArrayList<Goods>();
		mAdapter = new IndexGoodsListAdapter(mAttachActivity, mGoodsList);
	}
	
	private void initView() {

		index_fragment_head_category_dropdown_gv.setAdapter(categoryAdapter);
		index_fragment_head_sort_dropdown_lv.setAdapter(sortAdapter);
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
						hideDropdownAnimation(sortDropdownLayout);
						isShowingSortLayout = false;
					}
					
					if(!isShowingCategoryLayout) {
						//箭头从上到下翻转动画
						filpDownArrowAnimation(index_fragment_head_category_iv);
						//显示分类下拉布局动画
						showDropdownAnimation(categoryDropdownLayout);
						isShowingCategoryLayout = true;
					} else {
						//箭头从下到上翻转动画
						filpUpArrowAnimation(index_fragment_head_category_iv);
						//隐藏分类下拉布局动画
						hideDropdownAnimation(categoryDropdownLayout);
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
						hideDropdownAnimation(categoryDropdownLayout);
						isShowingCategoryLayout = false;
					}
					
					if(!isShowingSortLayout) {
						//箭头从上到下翻转动画
						filpDownArrowAnimation(index_fragment_head_sort_iv);
						//显示分类下拉布局动画
						showDropdownAnimation(sortDropdownLayout);
						isShowingSortLayout = true;
					} else {
						//箭头从下到上翻转动画
						filpUpArrowAnimation(index_fragment_head_sort_iv);
						//隐藏分类下拉布局动画
						hideDropdownAnimation(sortDropdownLayout);
						isShowingSortLayout = false;
					}
				}
			});
		}
	}
	
	//初始隐藏头部下拉布局动画
	private void hideHeadDropdownLayoutAnimation(View v) {
		if(v != null) {
			TranslateAnimation tranAnim = new TranslateAnimation(0, 0, 0, -mCategoryDropdownLayoutHeight);
			tranAnim.setDuration(1);
			tranAnim.setFillAfter(true);
			v.startAnimation(tranAnim);
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
	private void showDropdownAnimation(View v) {
		if(v != null) {
			//先设置透明度为1.0完全不透明
			v.setAlpha(1.0f);
			
			TranslateAnimation tranAnim = new TranslateAnimation(0, 0, -mCategoryDropdownLayoutHeight, 0);
			tranAnim.setFillAfter(true);
			tranAnim.setDuration(300);
			v.startAnimation(tranAnim);
		}
	}
	
	//头部下拉布局隐藏动画
	private void hideDropdownAnimation(View v) {
		if(v != null) {
			TranslateAnimation tranAnim = new TranslateAnimation(0, 0, 0, -mCategoryDropdownLayoutHeight);
			tranAnim.setFillAfter(true);
			tranAnim.setDuration(300);
			v.startAnimation(tranAnim);
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
}
