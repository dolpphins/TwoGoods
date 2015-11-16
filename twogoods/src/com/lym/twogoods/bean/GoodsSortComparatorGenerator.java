package com.lym.twogoods.bean;

import java.util.Comparator;

/**
 * 商品排序比较器生成器
 * 
 * @author 麦灿标
 * */
public class GoodsSortComparatorGenerator{

	/**
	 * 获取一个按商品发布时间排序的比较器
	 * 
	 * @return 返回按商品发布时间排序的比较器
	 * */
	public static Comparator<Goods> newGoodsPublishNewestSortComparator() {
		return new GoodsPublishNewestSortComparator();
	}
	
	/**
	 * 获取一个按商品价格排序的比较器
	 * 
	 * @return 返回按商品价格排序的比较器
	 * */
	public static Comparator<Goods> newGoodsPriceLowestSortComparator() {
		return new GoodsPriceLowestSortComparator();
	}
	
	/**
	 * 获取一个按商品关注量排序的比较器
	 * 
	 * @return 返回按商品关注量排序的比较器
	 * */
	public static Comparator<Goods> newGoodsFocusMostSortComarator() {
		return new GoodsFocusMostSortComarator();
	}
	
	/**
	 * 获取一个按商品浏览量排序的比较器
	 * 
	 * @return 返回按商品浏览量排序的比较器
	 * */
	public static Comparator<Goods> newGoodsFBrowseMostSortComarator() {
		return new GoodsFBrowseMostSortComarator();
	}
	
	
	/**
	 * 按发布时间排序比较器(新发布的排在前面)
	 * */
	public static class GoodsPublishNewestSortComparator implements Comparator<Goods> {

		@Override
		public int compare(Goods lhs, Goods rhs) {
			return (int) (lhs.getPublish_time() - rhs.getPublish_time());
		}
	}
	
	/**
	 * 按商品价格排序(价格低的排在前面)
	 * */
	public static class GoodsPriceLowestSortComparator implements Comparator<Goods> {
		
		@Override
		public int compare(Goods lhs, Goods rhs) {
			return lhs.getPrice() - rhs.getPrice();
		}
	}

	/**
	 * 按关注量排序(关注量高的排在前面)
	 * */
	public static class GoodsFocusMostSortComarator implements Comparator<Goods> {

		@Override
		public int compare(Goods lhs, Goods rhs) {
			return lhs.getFocus_num() - rhs.getFocus_num();
		}
		
	}
	
	/**
	 * 按浏览量排序(浏览量高的排在前面)
	 * */
	public static class GoodsFBrowseMostSortComarator implements Comparator<Goods> {

		@Override
		public int compare(Goods lhs, Goods rhs) {
			return lhs.getBrowse_num() - rhs.getBrowse_num();
		}
		
	}
}
