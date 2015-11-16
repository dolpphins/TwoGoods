package com.lym.twogoods.index.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import com.lym.twogoods.R;
import com.lym.twogoods.config.GoodsCategory;
import com.lym.twogoods.config.GoodsCategory.Category;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * <p>
 * 	the GridView adapter of index fragment's header category dropdown layout.
 * </p>
 * 
 * @author mao
 * */
public class CategoryGridViewAdapter extends BaseAdapter{

	/** 上下文 */
	private Context mContext;
	
	/** 数据集 */
	private List<Category> mCategoryData;
	
	/**  current category  */
	private Category mCurrentCategory = Category.ALL;
	
	private HashMap<Integer, GoodsCategory.Category> categoryMap = new HashMap<Integer, GoodsCategory.Category>();
	private WeakHashMap<GoodsCategory.Category, View> viewCacheMap = new WeakHashMap<GoodsCategory.Category, View>();
	
	public CategoryGridViewAdapter(Context context, List<Category> categoryData) {
		mContext = context;
		mCategoryData = categoryData;
	}
	
	@Override
	public int getCount() {
		if(mCategoryData == null) {
			return 0;
		}
		return mCategoryData.size();
	}

	@Override
	public Object getItem(int position) {
		return mCategoryData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ItemViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.index_fragment_head_category_dropdown_item, null);
			viewHolder.index_fragment_head_category_name = (TextView) convertView.findViewById(R.id.index_fragment_head_category_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (CategoryGridViewAdapter.ItemViewHolder) convertView.getTag();
		}
		
		Category item = (Category) getItem(position);
		if(!viewCacheMap.containsKey(item)) {
			viewCacheMap.put(item, viewHolder.index_fragment_head_category_name);
		}
		if(!categoryMap.containsKey(Integer.valueOf(position))) {
			categoryMap.put(position, item);
		}
		
		setItemContent(viewHolder, item);
		
		return convertView;
	}
	
	@SuppressLint("NewApi")
	private void setItemContent(ItemViewHolder viewHolder, Category item) {
		if(viewHolder == null || item == null) {
			return;
		}
		viewHolder.index_fragment_head_category_name.setText(GoodsCategory.getString(mContext, item));
		if(mCurrentCategory != null) {
			setCategoryItemStatus(mCurrentCategory, true);
			mCurrentCategory = null;
		}
	}
	
	/**
	 * <p>
	 * 	set the item's status of the specified position
	 * </p>
	 * 
	 * @param position 
	 * @param isSelected
	 * */
	public void setCategoryItemStatus(int position, boolean isSelected) {
		if(position >= 0 && mCategoryData != null && position < mCategoryData.size()) {
			Category c = categoryMap.get(Integer.valueOf(position));
			if(c != null) {
				setCategoryItemStatus(c, isSelected);
			}
		}
	}
	
	/**
	 * <p>
	 * 	set the item's status of the specified category
	 * </p>
	 * 
	 * @param position 
	 * @param isSelected
	 * */
	@SuppressLint("NewApi")
	public void setCategoryItemStatus(Category c, boolean isSelected) {
		View v = viewCacheMap.get(c);
		if(v != null) {
			if(isSelected) {
				v.setBackground(mContext.getResources().getDrawable(R.drawable.index_fragment_head_category_item_background_sel));
			} else {
				v.setBackground(mContext.getResources().getDrawable(R.drawable.index_fragment_head_category_item_background_nor));
			}
		}
	}
	
	/**
	 * <p>
	 * 	get current category by position
	 * </p>
	 * 
	 * @param position current category position
	 * 
	 * @return current category
	 * */
	public Category getCurrentCategory(int position) {
		return (Category)categoryMap.get(Integer.valueOf(position));
	}
	
//	/**
//	 * <p>
//	 * 	get the current category
//	 * </p>
//	 * 
//	 * @return the current category
//	 * */
//	public Category getCurrentCategory() {
//		return mCurrentCategory;
//	}
	
	/**
	 * <p>
	 * 	set the default category 
	 * </p>
	 * 
	 * @param c
	 * */
	public void setDefaultSelectedCategory(Category c) {
		mCurrentCategory = c;
	}
	
	private class ItemViewHolder {
		
		private TextView index_fragment_head_category_name;
	}

}
