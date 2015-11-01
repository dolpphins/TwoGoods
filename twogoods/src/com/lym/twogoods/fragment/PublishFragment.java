package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lym.twogoods.R;
import com.lym.twogoods.adapter.PublishEmotionGvAdapter;
import com.lym.twogoods.adapter.PublishEmotionPagerAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.EmotionUtils;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.screen.DisplayUtils;
import com.lym.twogoods.utils.DatabaseHelper;
import com.lym.twogoods.utils.SensitiveUtils;
import com.lym.twogoods.utils.SharePreferencesManager;

import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.SaveListener;

/**
 * <p>
 * 发布Fragment
 * </p>
 * 
 * @author 龙宇文
 * */
public class PublishFragment extends BaseFragment {

	// 定义控件
	private TextView tv_publish_fragment_text_number;
	private Spinner sp_publish_fragment_sort;
	private Spinner sp_publish_fragment_date;
	private EditText et_publish_fragment_tel;
	private EditText et_publish_fragment_price;
	private EditText et_publish_fragment_description;
	private TextView tv_publish_fragment_position_set;
	private ImageView iv_publish_fragment_add_photo;
	private ImageView iv_publish_fragment_add_smile;
	private ImageView iv_publish_fragment_add_voice;
	private ViewPager vp_publish_fragement_emoji;
	private LinearLayout ll_publish_fragment_emoji;
	private Button btn_publish_fragment_position;

	// 设置日期
	private List<String> items = new ArrayList<String>();

	// 定义适配器
	private PublishEmotionPagerAdapter publishEmotionPagerAdapter;

	// 定位相关
	private boolean isFirst = false;
	private LocationClient locationClient;
	private MylocationListen mylocationListen;
	private double longitude;
	private double latitude;

	// 货品信息相关
	private Goods goodsBean;
	private SharePreferencesManager sSpManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View publishfragment = inflater.inflate(R.layout.publish_fragment,
				container, false);

		init(publishfragment);
		initEvent();
		initEmotion();
		return publishfragment;
	}

	/*
	 * 
	 * 控件初始化
	 */
	private void init(View view) {
		tv_publish_fragment_text_number = (TextView) view
				.findViewById(R.id.tv_publish_fragment_text_number);
		sp_publish_fragment_sort = (Spinner) view
				.findViewById(R.id.sp_publish_fragment_category);
		sp_publish_fragment_date = (Spinner) view
				.findViewById(R.id.sp_publish_fragment_date);
		et_publish_fragment_description = (EditText) view
				.findViewById(R.id.et_publish_fragment_description);
		et_publish_fragment_price = (EditText) view
				.findViewById(R.id.et_publish_fragment_price);
		et_publish_fragment_tel = (EditText) view
				.findViewById(R.id.et_publish_fragment_tel);
		tv_publish_fragment_position_set = (TextView) view
				.findViewById(R.id.tv_publish_fragment_position_set);
		iv_publish_fragment_add_photo = (ImageView) view
				.findViewById(R.id.iv_publish_fragment_add_photo);
		iv_publish_fragment_add_smile = (ImageView) view
				.findViewById(R.id.iv_publish_fragment_add_smile);
		iv_publish_fragment_add_voice = (ImageView) view
				.findViewById(R.id.iv_publish_fragment_add_voice);
		vp_publish_fragement_emoji = (ViewPager) view
				.findViewById(R.id.vp_publish_fragment_emoji);
		btn_publish_fragment_position = (Button) view
				.findViewById(R.id.btn_publish_fragment_position);
		ll_publish_fragment_emoji = (LinearLayout) view
				.findViewById(R.id.ll_publish_fragment_emoji);
		// 定位相关
		locationClient = new LocationClient(getActivity());
		mylocationListen = new MylocationListen();
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		locationClient.setLocOption(option);
		locationClient.registerLocationListener(mylocationListen);

		// 货品信息相关
		goodsBean = new Goods();
		sSpManager = SharePreferencesManager.getInstance();
	}

	/*
	 * 
	 * 为控件添加监听事件或者设置属性
	 */
	private void initEvent() {
		et_publish_fragment_description.addTextChangedListener(mTextWatcher);
		setSpinner();
		iv_publish_fragment_add_smile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ll_publish_fragment_emoji.getVisibility() == View.VISIBLE) {
					ll_publish_fragment_emoji.setVisibility(View.GONE);
				} else {
					ll_publish_fragment_emoji.setVisibility(View.VISIBLE);
				}
			}
		});

		// 定位监听按钮
		btn_publish_fragment_position.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isFirst = true;
				locationClient.requestLocation();
			}
		});
	}

	/*
	 * 
	 * 为分类和日期下拉框设置内容
	 */
	private void setSpinner() {
		// 设置分类内容
		ArrayAdapter<CharSequence> sortaAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.publish_sort_array,
						android.R.layout.simple_spinner_item);
		sortaAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_publish_fragment_sort.setAdapter(sortaAdapter);
		// 设置价格内容
		for (int i = 0; i < 30; i++) {
			items.add((i + 1)+"");
		}
		ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, items);
		sp_publish_fragment_date.setAdapter(dateAdapter);
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		private int editStart;

		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			editEnd = et_publish_fragment_description.getSelectionEnd();
			editStart = et_publish_fragment_description.getSelectionStart();
			et_publish_fragment_description
					.removeTextChangedListener(mTextWatcher);
			while (s.length() > 200) {
				s.delete(editStart - 1, editEnd);
				editStart--;
				editEnd--;
			}
			et_publish_fragment_description.setSelection(editStart);
			et_publish_fragment_description
					.addTextChangedListener(mTextWatcher);
			setLeftCount();
		}
	};

	/**
	 * 刷新剩余输入字数
	 */
	private void setLeftCount() {
		tv_publish_fragment_text_number.setText(String
				.valueOf((200 - et_publish_fragment_description.getText()
						.toString().length())));
	}

	/**
	 * <p>
	 * 获取注册信息，并把它放到goodsBean
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void getGoodsData() {
		// goodsBean.setUsername(sSpManager.getStringSet(getActivity(),
		// "loginmessage(MD5).xml", MODE_PRIVATE, key, defValue))
		goodsBean.setDescription(et_publish_fragment_description.getText()
				.toString());
		goodsBean.setCategory(sp_publish_fragment_sort.getSelectedItem()
				.toString());
		goodsBean.setPhone(et_publish_fragment_tel.getText().toString());
		goodsBean.setPrice(Integer.parseInt(et_publish_fragment_price.getText()
				.toString()));
		goodsBean.setExpire(Integer.parseInt(sp_publish_fragment_date
				.getSelectedItem().toString()));
		goodsBean.setGUID(DatabaseHelper.getUUID().toString());
		goodsBean.setPublish_time(System.currentTimeMillis());
		goodsBean.setPublish_location(tv_publish_fragment_position_set
				.getText().toString());
		goodsBean.setLocation_latitude(String.valueOf(latitude));
		goodsBean.setLocation_longitude(String.valueOf(longitude));

	}

	/*
	 * 
	 * 发布商品到服务器上
	 */
	public void publishGoods() {
		getGoodsData();
		goodsBean.save(getActivity(), new SaveListener() {

			@Override
			public void onSuccess() {
				Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(getActivity(), "发布失败", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/*
	 * 判断货品描述是否合法，存在敏感词汇自动屏蔽
	 */
	public boolean judgeDescription() {
		for (String regex : SensitiveUtils.sensitiveWords) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(et_publish_fragment_description
					.getText().toString());
			if (matcher.find()) {
				(et_publish_fragment_description.getText().toString())
						.replaceAll(regex, "**");
				Toast.makeText(getActivity(), "你发布的商品描述带有敏感词汇",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	/*
	 * 
	 * 初始化表情面板内容
	 */
	private void initEmotion() {
		// 设置面板的宽高，间隔
		int screenWidth = DisplayUtils.getScreenWidthPixels(getActivity());
		int spacing = DisplayUtils.dp2px(getActivity(), 8);
		int itemWidth = (screenWidth - spacing * 8) / 7;
		int gvHeight = itemWidth * 3 + spacing * 4;

		List<GridView> gvs = new ArrayList<GridView>();
		List<String> emotionNames = new ArrayList<String>();
		for (String emojiName : EmotionUtils.emojiMap.keySet()) {
			emotionNames.add(emojiName);
			if (emotionNames.size() == 20) {
				GridView gv = createEmotionGridView(emotionNames, screenWidth,
						spacing, itemWidth, gvHeight);
				gvs.add(gv);

				emotionNames = new ArrayList<String>();
			}
		}

		if (emotionNames.size() > 0) {
			GridView gv = createEmotionGridView(emotionNames, screenWidth,
					spacing, itemWidth, gvHeight);
			gvs.add(gv);
		}
		publishEmotionPagerAdapter = new PublishEmotionPagerAdapter(gvs);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				screenWidth, gvHeight);
		ll_publish_fragment_emoji.setLayoutParams(params);
		vp_publish_fragement_emoji.setAdapter(publishEmotionPagerAdapter);
	}

	/*
	 * 创建显示表情的GridView
	 */
	private GridView createEmotionGridView(List<String> emotionNames,
			int gvWidth, int padding, int itemWidth, int gvHeight) {
		GridView gv = new GridView(getActivity());
		gv.setNumColumns(7);
		gv.setPadding(padding, padding, padding, padding);
		gv.setHorizontalSpacing(padding);
		gv.setVerticalSpacing(padding);

		LayoutParams params = new LayoutParams(gvWidth, gvHeight);
		gv.setLayoutParams(params);

		PublishEmotionGvAdapter adapter = new PublishEmotionGvAdapter(
				getActivity(), emotionNames, itemWidth);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == parent.getAdapter().getCount() - 1) {
					// 如果点击了最后一个回退按钮,则调用删除键事件
					et_publish_fragment_description
							.dispatchKeyEvent(new KeyEvent(
									KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
				} else {
					// 如果点击了表情,则添加到输入框中
					String emotionName = (String) parent.getAdapter().getItem(
							position);
					Drawable drawable = getResources().getDrawable(
							EmotionUtils.emojiMap.get(emotionName));
					SpannableString ss = new SpannableString("*");
					drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight());
					ImageSpan span = new ImageSpan(drawable,
							ImageSpan.ALIGN_BASELINE);
					ss.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					et_publish_fragment_description.getText()
							.insert(et_publish_fragment_description
									.getSelectionStart(), ss);
				}
			}
		});
		return gv;
	}

	/*
	 * 
	 * 自定义定位监听器
	 */
	private class MylocationListen implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (isFirst) {
				Toast.makeText(getActivity(), location.getAddrStr(),
						Toast.LENGTH_SHORT).show();
				tv_publish_fragment_position_set.setText(location.getAddrStr());
				isFirst = false;
				latitude = location.getLatitude();
				longitude = location.getLongitude();
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
}
