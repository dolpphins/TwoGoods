package com.lym.twogoods.nearby.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.config.SharePreferencesConfiguration;
import com.lym.twogoods.nearby.NearbyPositionModelBean;
import com.lym.twogoods.nearby.adapter.NearbyHotCityGridViewAdapter;
import com.lym.twogoods.nearby.adapter.SelectCityPositionListViewAdapter;
import com.lym.twogoods.nearby.config.CityManger;
import com.lym.twogoods.nearby.config.NearbyConfig;
import com.lym.twogoods.nearby.utils.CharacterParser;
import com.lym.twogoods.nearby.utils.PinyinComparator;
import com.lym.twogoods.screen.NearbyScreen;
import com.lym.twogoods.ui.base.BackFragmentActivity;
import com.lym.twogoods.utils.SharePreferencesManager;

/**
 * <p>
 * 选择城市Activity
 * </p>
 * 
 * @author 龙宇文
 * */
public class SelectCityActivity extends BackFragmentActivity {

	private String TAG = "ReplaceActivity";
	// 定义控件
	private SelectCityClearEditText et_nearby_select_city_input;
	private Button btn_nearby_select_city_input_cancel;
	private ListView lv_nearby_select_city_search_result;
	private LinearLayout ll_nearby_select_city_dingwei;
	private LinearLayout ll_nearby_select_city_replace_hidelayout;
	private GridView gv_nearby_select_city_hot_city;
	// 热门城市相关
	private NearbyHotCityGridViewAdapter nearbyHotCityGridViewAdapter;
	// 汉字转为拼音的类
	private CharacterParser characterParser;
	private List<NearbyPositionModelBean> dataList;
	// 根据拼音来排列ListView里面的数据类
	private PinyinComparator comparator;
	// 筛选结果适配器
	private SelectCityPositionListViewAdapter selectCityPositionListViewAdapter;
	// 定位相关
	private Geocoder geocoder;
	private boolean isFirst = false;
	private LocationClient locationClient;
	private MylocationListen mylocationListen;
	// 加载器
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_select_city_activity);
		init();
		initEvent();

	}

	private void init() {
		et_nearby_select_city_input = (SelectCityClearEditText) findViewById(R.id.et_nearby_select_city_replace_input);
		btn_nearby_select_city_input_cancel = (Button) findViewById(R.id.btn_nearby_select_city_replace_input_cancel);
		lv_nearby_select_city_search_result = (ListView) findViewById(R.id.lv_nearby_select_city_replace_search_result);
		ll_nearby_select_city_dingwei = (LinearLayout) findViewById(R.id.ll_nearby_select_city_replace_dingwei);
		ll_nearby_select_city_replace_hidelayout = (LinearLayout) findViewById(R.id.ll_nearby_select_city_replace_hidelayout);
		gv_nearby_select_city_hot_city = (GridView) findViewById(R.id.gv_nearby_select_city_replace_hot_city);

		gridViewSetting();
		initLocation();
		characterParser = CharacterParser.getInstance();
		comparator = new PinyinComparator();
		// 初始化数据
		dataList = fillData(CityManger.allcity);
		// 根据a-z排序
		Collections.sort(dataList, comparator);
		selectCityPositionListViewAdapter = new SelectCityPositionListViewAdapter(
				getApplicationContext(), dataList);
		lv_nearby_select_city_search_result
				.setAdapter(selectCityPositionListViewAdapter);
		// ProgressDialog初始化
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("正在保存位置信息");
		progressDialog.setMessage("稍等一下......");
		progressDialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
	}

	private void initEvent() {
		et_nearby_select_city_input
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							btn_nearby_select_city_input_cancel
									.setVisibility(View.VISIBLE);
						} else {
							btn_nearby_select_city_input_cancel
									.setVisibility(View.GONE);
						}
					}
				});
		et_nearby_select_city_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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
		btn_nearby_select_city_input_cancel
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 点击取消的时候，Edittext失去焦点，隐藏键盘，内容为空
						et_nearby_select_city_input.clearFocus();
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								et_nearby_select_city_input.getWindowToken(), 0);
						et_nearby_select_city_input.setText("");
					}
				});
		lv_nearby_select_city_search_result
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 设置Location
						progressDialog.show();
						setLocation(getLocation(((NearbyPositionModelBean) selectCityPositionListViewAdapter
								.getItem(position)).getName()));
						Log.v(TAG, UserInfoManager.getInstance()
								.getCurrentLocation().getDescription()
								+ '\n'
								+ UserInfoManager.getInstance()
										.getCurrentLocation().getLatitude()
								+ '\n'
								+ UserInfoManager.getInstance()
										.getCurrentLocation().getLongitude());
						finish();
						// 隐藏操作
						/*
						 * et_nearby_select_city_input.clearFocus();
						 * InputMethodManager imm = (InputMethodManager)
						 * getSystemService(INPUT_METHOD_SERVICE);
						 * imm.hideSoftInputFromWindow(
						 * et_nearby_select_city_input.getWindowToken(), 0);
						 * et_nearby_select_city_input.setText("");
						 */
					}
				});

		ll_nearby_select_city_dingwei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isFirst = true;
				progressDialog.show();
				locationClient.requestLocation();
			}
		});
	}

	/**
	 * 
	 * 初始化加载热门城市
	 * 
	 */

	/*
	 * 
	 * 定位相关初始化
	 */
	private void initLocation() {
		geocoder = new Geocoder(getApplicationContext(), Locale.CHINA);
		locationClient = new LocationClient(getApplicationContext());
		mylocationListen = new MylocationListen();
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		locationClient.setLocOption(option);
		locationClient.registerLocationListener(mylocationListen);
	}

	/*
	 * 
	 * 热门城市GridView相关配置
	 */
	private void gridViewSetting() {
		PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
		specification = NearbyScreen.getHotCityItemThumbnailSpecification(this);
		nearbyHotCityGridViewAdapter = new NearbyHotCityGridViewAdapter(
				getApplicationContext(), CityManger.hot_city,
				SelectCityActivity.this);
		LayoutParams params = (LayoutParams) gv_nearby_select_city_hot_city
				.getLayoutParams();
		params.height = (int) (specification.getHeight()
				* (NearbyConfig.HOT_CITY_COUNT / NearbyConfig.CITY_COLUMNS) + (NearbyConfig.HOT_CITY_COUNT
				/ NearbyConfig.CITY_COLUMNS - 1)
				* getResources().getDimension(R.dimen.division));
		gv_nearby_select_city_hot_city.setNumColumns(NearbyConfig.CITY_COLUMNS);
		gv_nearby_select_city_hot_city.setVerticalSpacing((int) getResources()
				.getDimension(R.dimen.division));
		gv_nearby_select_city_hot_city
				.setHorizontalSpacing((int) getResources().getDimension(
						R.dimen.division));
		gv_nearby_select_city_hot_city.setAdapter(nearbyHotCityGridViewAdapter);
		gv_nearby_select_city_hot_city
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						progressDialog.show();
						setLocation(getLocation((String) (nearbyHotCityGridViewAdapter
								.getItem(position))));
						Log.v(TAG, UserInfoManager.getInstance()
								.getCurrentLocation().getDescription()
								+ '\n'
								+ UserInfoManager.getInstance()
										.getCurrentLocation().getLatitude()
								+ '\n'
								+ UserInfoManager.getInstance()
										.getCurrentLocation().getLongitude());
						finish();
					}
				});
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
	@SuppressLint("DefaultLocale")
	private void filterData(String filterStr) {
		List<NearbyPositionModelBean> filterDateList = new ArrayList<NearbyPositionModelBean>();
		if (!TextUtils.isEmpty(filterStr)) {
			// 当输入框中有数据的时候，隐藏其它组件，只显示listview（筛选结果）和Edittext那部分
			ll_nearby_select_city_replace_hidelayout.setVisibility(View.GONE);
			lv_nearby_select_city_search_result.setVisibility(View.VISIBLE);
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
		} else {
			ll_nearby_select_city_replace_hidelayout
					.setVisibility(View.VISIBLE);
			lv_nearby_select_city_search_result.setVisibility(View.GONE);
			filterDateList = dataList;
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
				com.lym.twogoods.bean.Location currentLocation = new com.lym.twogoods.bean.Location();
				currentLocation.setDescription(location.getAddrStr());
				currentLocation.setLatitude(location.getLatitude() + "");
				currentLocation.setLongitude(location.getLongitude() + "");
				setLocation(currentLocation);
				Log.v(TAG, UserInfoManager.getInstance().getCurrentLocation()
						.getDescription()
						+ '\n'
						+ UserInfoManager.getInstance().getCurrentLocation()
								.getLatitude()
						+ '\n'
						+ UserInfoManager.getInstance().getCurrentLocation()
								.getLongitude());
				finish();
			}
		}

	}

	/**
	 * <p>
	 * 通过位置获取Location对象
	 * </p>
	 * 
	 * @param description
	 *            地理位置
	 * @return
	 */
	private com.lym.twogoods.bean.Location getLocation(String description) {
		com.lym.twogoods.bean.Location location = new com.lym.twogoods.bean.Location();
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocationName(description, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addresses.size() != 0) {
			Address address = addresses.get(0);
			location.setDescription(description);
			location.setLatitude("" + address.getLatitude());
			location.setLongitude("" + address.getLongitude());
		} else {
			Log.v(TAG, "没有成功");
		}
		return location;
	}

	/**
	 * <p>
	 * 设置Location实体类对象
	 * </p>
	 * 
	 * @param description
	 *            地理位置
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 */
	private void setLocation(com.lym.twogoods.bean.Location location) {
		UserInfoManager.getInstance().setCurrentLocation(location);
		writeSharePreference(location);
		progressDialog.dismiss();
	}

	/**
	 * <p>
	 * 将Location对象写入SharePreference中
	 * 
	 * @param location
	 */
	private void writeSharePreference(com.lym.twogoods.bean.Location location) {
		SharePreferencesManager sharePreferencesManager = SharePreferencesManager
				.getInstance();
		sharePreferencesManager.setLocationString(getApplicationContext(),
				SharePreferencesConfiguration.LOCATION_DESCRIPTION_KEY,
				location.getDescription());
		sharePreferencesManager.setLocationString(getApplicationContext(),
				SharePreferencesConfiguration.LOCATION_LATITUDE_KEY,
				location.getLatitude());
		sharePreferencesManager.setLocationString(getApplicationContext(),
				SharePreferencesConfiguration.LOCATION_LONGITUDE_KEY,
				location.getLongitude());
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
}
