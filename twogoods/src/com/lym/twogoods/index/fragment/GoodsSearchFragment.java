package com.lym.twogoods.index.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lym.twogoods.R;
import com.lym.twogoods.fragment.base.HeaderListFragment;
import com.lym.twogoods.index.adapter.GoodsSearchHistoryListAdapter;
import com.lym.twogoods.index.ui.GoodsSearchResultActivity;
import com.lym.twogoods.screen.DisplayUtils;
import com.lym.twogoods.utils.NetworkHelper;
import com.lym.twogoods.utils.SharePreferencesManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 搜贰货Fragment
 * 
 * @author 麦灿标
 * */
public class GoodsSearchFragment extends HeaderListFragment {

	//头部布局控件
	private EditText index_goods_search_edittext;
	private TextView index_goods_search_button;
	private TextView index_goods_search_input_delete;
	
	//历史记录
	private List<String> mHistoryList;
	//保存历史记录的SharePreferences文件名
	private final static String sHistorySp = "search_history";
	private final static String sHistoryKey = "history";
	
	//历史记录列表头部布局
	private View mHistoryTip;
	//历史记录列表底部布局
	private View mHistoryFooter;
	private TextView index_goods_search_history_clear;
	private GoodsSearchHistoryListAdapter mHistoryAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//读取历史记录
		Set<String> historySet = SharePreferencesManager.getInstance().getStringSet(mAttachActivity, 
				sHistorySp, Context.MODE_PRIVATE, sHistoryKey, null);
		if(historySet == null) {
			mHistoryList = new ArrayList<String>();
		} else {
			mHistoryList = new ArrayList<String>(historySet);
		}
	}
	
	@Override
	protected View onCreateHeaderView() {
		View v = LayoutInflater.from(mAttachActivity).inflate(R.layout.index_goods_search_header, null);
		
		index_goods_search_edittext = (EditText) v.findViewById(R.id.index_goods_search_edittext);
		index_goods_search_button = (TextView) v.findViewById(R.id.index_goods_search_button);
		index_goods_search_input_delete = (TextView) v.findViewById(R.id.index_goods_search_input_delete);
		setEventForHeaderView();
		
 		return v;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		initView();
		
		return v;
	}
	
	private void setEventForHeaderView() {
		if(index_goods_search_button != null) {
			index_goods_search_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					prepareSearchForClick();
				}
			});
		}
		if(index_goods_search_edittext != null) {
			index_goods_search_edittext.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					String text = s.toString();
					if(TextUtils.isEmpty(text)) {
						//隐藏删除按钮
						index_goods_search_input_delete.setVisibility(View.INVISIBLE);
					} else {
						//显示删除按钮
						index_goods_search_input_delete.setVisibility(View.VISIBLE);
					}
					
					List<String> historyListTemp = getListForStartWith(text);
					if(historyListTemp == null || historyListTemp.size() <= 0) {
						hideHistoryList();
					} else {
						mHistoryAdapter.setDataList(historyListTemp);
						mHistoryAdapter.notifyDataSetChanged();
						showHistoryList();
					}
				}
			});
		}
		if(index_goods_search_input_delete != null) {
			index_goods_search_input_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					index_goods_search_edittext.setText("");
				}
			});
		}
	}
	
	
	private void initView() {
		int padding = DisplayUtils.dp2px(mAttachActivity, 15.0f);
		mListView.setPadding(padding, padding, padding, padding);
		//历史记录ListView头部
		mHistoryTip = LayoutInflater.from(mAttachActivity).inflate(R.layout.index_goods_search_history_tip, null);
		mListView.setHeaderDividersEnabled(false);
		//历史记录ListView底部
		mHistoryFooter = LayoutInflater.from(mAttachActivity).inflate(R.layout.index_goods_search_history_footer, null);
		index_goods_search_history_clear = (TextView) mHistoryFooter.findViewById(R.id.index_goods_search_history_clear);
		setClickEventForClearHistory();
		mListView.setFooterDividersEnabled(false);
		
		mListView.addHeaderView(mHistoryTip, null, false);//不可点击
		mListView.addFooterView(mHistoryFooter, null, false);//不可点击
		mHistoryAdapter = new GoodsSearchHistoryListAdapter(mAttachActivity, mHistoryList);
		mListView.setAdapter(mHistoryAdapter);
		
		setClickEventForListView();
		
		//开始默认显示搜索历史记录列表
		showHistoryList();
	}
	
	private void setClickEventForListView() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//System.out.println("position:" + position);
				//System.out.println("mHistoryList size:" + mHistoryList.size());
				//注意如果ListView有HeaderView那么第一个真正的Item的position为2
				
				int index = position - 2;
				if(index >= 0 && index < mHistoryList.size()) {
					String keyword = mHistoryList.get(index);
					startSearchActivity(keyword);
				}
			}
		});
	}
	
	//获取历史记录列表中以某个字符串开始的集合
	private List<String> getListForStartWith(String startText) {
		if(TextUtils.isEmpty(startText)) {
			return mHistoryList;
		}
		List<String> tempList = new ArrayList<String>();
		for(String s : mHistoryList) {
			if(s.startsWith(startText)) {
				tempList.add(s);
			}
		}
		return tempList;
	}
	
	//搜索
	private void prepareSearchForClick() {
		if(check()) {
			//添加到历史记录中
			String keyword = index_goods_search_edittext.getText().toString();
			if(!mHistoryList.contains(keyword)) {
				mHistoryList.add(keyword);
				//保存到SharePrefrences中
				Set<String> historySet = new HashSet<String>(mHistoryList);
				System.out.println("historySet size:" + historySet.size());
				boolean b = SharePreferencesManager.getInstance().putStringSet(mAttachActivity,
						sHistorySp, Context.MODE_PRIVATE, sHistoryKey, historySet);
				System.out.println(b);
			}
			//开始搜索
			trySearchFromNetwork(keyword);
		}
	}
	
	//检查是否符合搜索条件
	private boolean check() {
		if(index_goods_search_edittext != null) {
			String keyword = index_goods_search_edittext.getText().toString();
			if(!TextUtils.isEmpty(keyword)) {
				return true;
			} else {
				//这里如果连续点击多次会不断得显示多次，而Toast的cancel只针对正在showing有效，因此必须使用自定义
				//的Toast队列对Toast进行管理
				Toast.makeText(mAttachActivity, "请输入关键字", Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}
	
	private void showHistoryList() {
		if(mHistoryList != null && mHistoryList.size() > 0) {
			//mListView.addHeaderView(mHistoryTip, null, false);//不可点击
			//mListView.addFooterView(mHistoryFooter, null, false);//不可点击
			//mListView.setAdapter(mHistoryAdapter);
			mListView.setVisibility(View.VISIBLE);
		}
	}
	
	private void hideHistoryList() {
		//mListView.removeHeaderView(mHistoryTip);
		//mListView.removeFooterView(mHistoryFooter);
		//mListView.setAdapter(null);
		mListView.setVisibility(View.GONE);
	}
	
	private void setClickEventForClearHistory() {
		if(index_goods_search_history_clear != null) {
			index_goods_search_history_clear.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mHistoryList != null && mHistoryList.size() > 0) {
						SharePreferencesManager.getInstance().removeValue(mAttachActivity, 
									sHistorySp, Context.MODE_PRIVATE, sHistoryKey);
						mHistoryList.clear();
						hideHistoryList();
					}
				}
			});
		}
	}
	
	private void trySearchFromNetwork(String keyword) {
		//检查网络
		if(!NetworkHelper.isNetworkAvailable(mAttachActivity)) {
			Toast.makeText(mAttachActivity, "网络不可用", Toast.LENGTH_SHORT).show();
		} else {
			startSearchActivity(keyword);
		}
	}
	
	private void startSearchActivity(String keyword) {
		//先设置编辑框
		index_goods_search_edittext.setText(keyword);
		
		Intent intent = new Intent(mAttachActivity, GoodsSearchResultActivity.class);
		intent.putExtra("keyword", keyword);
		startActivity(intent);
	}
	
}
