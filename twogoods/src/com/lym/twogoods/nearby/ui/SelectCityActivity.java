package com.lym.twogoods.nearby.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lym.twogoods.R;
import com.lym.twogoods.nearby.NearbyPositionModelBean;
import com.lym.twogoods.nearby.adapter.NearbyHotCityListviewAdapter;
import com.lym.twogoods.nearby.adapter.SelectCityPositionListViewAdapter;
import com.lym.twogoods.nearby.ui.SelectCityLetterView.OnTouchingLetterChangedListener;
import com.lym.twogoods.ui.base.BackFragmentActivity;
import com.lym.twogoods.utils.SharePreferencesManager;
import com.lym.twogoods.nearby.utils.CharacterParser;
import com.lym.twogoods.nearby.utils.PinyinComparator;

/**
 * <p>
 * 选择城市Activity
 * </p>
 * 
 * @author 龙宇文
 * */
public class SelectCityActivity extends BackFragmentActivity {

	// 控件定义相关
	private ListView lv_select_city_city;
	private ListView lv_select_city_sort;
	private TextView tv_select_city_position_set;
	private TextView tv_select_city_locate;
	private SelectCityLetterView selectCityLetterView;
	private SelectCityClearEditText selectCityClearEditText;

	// 定位相关
	private boolean isFirst = false;
	private LocationClient locationClient;
	private MylocationListen mylocationListen;

	// 热门城市数组
	List<List<String>> mList = new ArrayList<List<String>>();
	List<String> list1 = new ArrayList<String>(Arrays.asList("上海市", "北京市",
			"深圳市", "广州市"));
	List<String> list2 = new ArrayList<String>(Arrays.asList("苏州市", "杭州市",
			"天津市", "东莞市"));
	List<String> list3 = new ArrayList<String>(Arrays.asList("武汉市", "哈尔滨市",
			"黑龙江市", "佛山市"));
	private List<String> list=new ArrayList<String>();

	// 适配器
	private NearbyHotCityListviewAdapter adapter;
	private SelectCityPositionListViewAdapter selectCityPositionListViewAdapter;

	// 汉字转为拼音的类
	private CharacterParser characterParser;
	private List<NearbyPositionModelBean> dataList;

	// 根据拼音来排列ListView里面的数据类
	private PinyinComparator comparator;
	
	//SharePreferences管理相关
	SharePreferencesManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_select_city_activity);
		init();
	}

	/*
	 * 
	 * 初始化
	 */
	private void init() {
		lv_select_city_city = (ListView) findViewById(R.id.lv_select_city_city);
		mList.add(list1);
		mList.add(list2);
		mList.add(list3);
		adapter = new NearbyHotCityListviewAdapter(getApplicationContext(),
				mList);
		lv_select_city_city.setAdapter(adapter);
		// 定位相关
		locationClient = new LocationClient(getApplicationContext());
		mylocationListen = new MylocationListen();
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		locationClient.setLocOption(option);
		locationClient.registerLocationListener(mylocationListen);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		comparator = new PinyinComparator();

		selectCityClearEditText = (SelectCityClearEditText) findViewById(R.id.et_nearby_select_city_input);
		selectCityLetterView = (SelectCityLetterView) findViewById(R.id.lv_nearby_select_city_letter_sort);
		lv_select_city_sort = (ListView) findViewById(R.id.lv_nearby_select_city_position_sort);
		tv_select_city_position_set=(TextView) findViewById(R.id.tv_select_city_position_set);
		tv_select_city_locate=(TextView) findViewById(R.id.tv_select_city_locate);
		tv_select_city_locate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isFirst = true;
				locationClient.requestLocation();
			}
		});

		selectCityLetterView
				.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

					@Override
					public void onTouchingLetterChanged(String s) {
						// 该字母第一次出现的位置
						int position = selectCityPositionListViewAdapter
								.getPositionForSection(s.charAt(0));
						if (position != -1) {
							lv_select_city_sort.setSelection(position);
						}
					}
				});

		lv_select_city_sort.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 通过适配器来获取当前位置所对应的对象
				Toast.makeText(
						getApplicationContext(),
						((NearbyPositionModelBean) selectCityPositionListViewAdapter
								.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
				tv_select_city_position_set
						.setText(((NearbyPositionModelBean) selectCityPositionListViewAdapter
								.getItem(position)).getName());
				//设置位置缓存
				setPositionSharePreferences(((NearbyPositionModelBean) selectCityPositionListViewAdapter
								.getItem(position)).getName());
				
			}
		});
		// 设置城市到数组中
		dataList = fillData(getResources().getStringArray(R.array.position));
		// 根据a-z排序
		Collections.sort(dataList, comparator);
		selectCityPositionListViewAdapter = new SelectCityPositionListViewAdapter(
				getApplicationContext(), dataList);
		lv_select_city_sort.setAdapter(selectCityPositionListViewAdapter);

		// 根据输入框输入值的改变来过滤
		selectCityClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		//SharePreferences管理
		manager=SharePreferencesManager.getInstance();
	}

	/*
	 * 
	 * 为城市列表填充数据
	 */
	@SuppressLint("DefaultLocale")
	private List<NearbyPositionModelBean> fillData(String[] data) {
		List<NearbyPositionModelBean> mList = new ArrayList<NearbyPositionModelBean>();
		for (int i = 0; i < data.length; i++) {
			NearbyPositionModelBean bean = new NearbyPositionModelBean();
			bean.setName(data[i]);
			// 汉字转拼音
			String pinyin = characterParser.getSelling(data[i]);
			String firstChar = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (firstChar.matches("[A-Z]")) {
				bean.setPositionLetters(firstChar);
			} else {
				bean.setPositionLetters("#");
			}
			mList.add(bean);
		}
		return mList;
	}

	/*
	 * 
	 * 根据输入框中的值来过滤数据并更新ListView
	 */
	private void filterData(String filterStr) {
		List<NearbyPositionModelBean> filterDateList = new ArrayList<NearbyPositionModelBean>();
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = dataList;
		} else {
			filterDateList.clear();
			for (NearbyPositionModelBean bean : dataList) {
				String name = bean.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())
						|| (characterParser.getSelling(name)).toUpperCase()
								.startsWith(filterStr.toString())) {
					filterDateList.add(bean);
				}
			}
		}
		// 根据a-z排序
		Collections.sort(filterDateList, comparator);
		selectCityPositionListViewAdapter.updateListView(filterDateList);
	}

	/*
	 * 
	 * 自定义定位监听器
	 */
	private class MylocationListen implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (isFirst) {
				Toast.makeText(getApplicationContext(), location.getAddrStr(),
						Toast.LENGTH_SHORT).show();
				tv_select_city_position_set.setText(location.getAddrStr());
				//设置位置缓存
				setPositionSharePreferences(location.getAddrStr());
				isFirst = false;
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		if (!locationClient.isStarted()) {
			locationClient.start();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		locationClient.stop();
	}
	
	//设置位置缓存信息
	private void setPositionSharePreferences(String position){
		manager.setLocationString(getApplicationContext(), "position", position);
	}
}
