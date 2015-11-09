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
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.message.MessageConfig;
import com.lym.twogoods.publish.adapter.PublishGridViewAdapter;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.ui.SendPictureActivity;
import com.lym.twogoods.utils.DatabaseHelper;
import com.lym.twogoods.utils.SensitiveUtils;
import com.lym.twogoods.widget.WrapContentViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
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
	private GridView gv_publish_fragment_photo;
	private ImageView iv_publish_fragment_add_photo;
	private ImageView iv_publish_fragment_add_smile;
	private ImageView iv_publish_fragment_add_voice;
	private WrapContentViewPager vp_publish_fragement_emoji;
	private LinearLayout ll_publish_fragment_emoji;
	private Button btn_publish_fragment_position;

	// 设置日期
	private List<String> items = new ArrayList<String>();

	// 定位相关
	private boolean isFirst = false;
	private LocationClient locationClient;
	private MylocationListen mylocationListen;
	private double longitude;
	private double latitude;

	// 标记表情布局是否弹出
	private boolean mEmotionLayoutIsShowing = false;

	// 货品信息相关
	private Goods goodsBean;
	private EmotionViewPagerAdapter emotionViewPagerAdapter;
	private String TGA="PublishFrament";
	
	//发布货品图片适配器
	private PublishGridViewAdapter adapter;

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
		vp_publish_fragement_emoji = (WrapContentViewPager) view
				.findViewById(R.id.vp_publish_fragment_emoji);
		btn_publish_fragment_position = (Button) view
				.findViewById(R.id.btn_publish_fragment_position);
		ll_publish_fragment_emoji = (LinearLayout) view
				.findViewById(R.id.ll_publish_fragment_emoji);
		gv_publish_fragment_photo=(GridView) view.findViewById(R.id.gv_publish_fragment_photo);
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
		
		et_publish_fragment_description.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mEmotionLayoutIsShowing) {
					hideEmotionLayout();
				}
			}
		});
		et_publish_fragment_price.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mEmotionLayoutIsShowing) {
					hideEmotionLayout();
				}
			}
		});
		et_publish_fragment_tel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mEmotionLayoutIsShowing) {
					hideEmotionLayout();
				}
			}
		});
		
		//点击发送图片时
		iv_publish_fragment_add_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),SendPictureActivity.class);
				intent.putExtra("picCount", PublishConfigManger.picCount);
				startActivityForResult(intent, PublishConfigManger.requestCode);
			}
		});
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
				toggleEmotionLayout();
				if (mEmotionLayoutIsShowing) {
					//隐藏键盘
					InputMethodManager imm=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
		adapter=new PublishGridViewAdapter(getActivity(), PublishConfigManger.picsPath);
		gv_publish_fragment_photo.setAdapter(adapter);
	}

	/*
	 * 
	 * 表情键盘切换与隐藏的关系
	 */
	private void toggleEmotionLayout() {
		if (mEmotionLayoutIsShowing) {
			hideEmotionLayout();
		} else {
			showEmotionLayout();
		}
	}

	private void showEmotionLayout() {
		ll_publish_fragment_emoji.setVisibility(View.VISIBLE);
		mEmotionLayoutIsShowing = true;
	}

	private void hideEmotionLayout() {
		ll_publish_fragment_emoji.setVisibility(View.GONE);
		mEmotionLayoutIsShowing = false;
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
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_publish_fragment_sort.setAdapter(sortaAdapter);

		// 设置价格内容
		for (int i = 0; i < 30; i++) {
			items.add((i + 1) + "");
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
		// 上传头像
		// goodsBean.setHead_url(sSpManager.getString(getActivity(), name, mode,
		// key, defValue))
		// goodsBean.setHead_url(UserInfoManager.getInstance().getmCurrent().getHead_url());
	}

	/*
	 * 
	 * 发布商品到服务器上
	 */
	public void publishGoods() {
		if (judgeDescription() && judgeGoods()) {
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
					Log.v("publishGoods", arg1);
				}
			});
		}
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
	 * 判断货品描述是否为空
	 */
	public boolean judgeGoods() {
		if (((et_publish_fragment_description.getText().toString()).equals(""))
				|| ((sp_publish_fragment_sort.getSelectedItem().toString())
						.equals(""))
				|| ((et_publish_fragment_tel.getText().toString()).equals(""))
				|| ((et_publish_fragment_price.getText().toString()).equals(""))
				|| ((sp_publish_fragment_date.getSelectedItem().toString())
						.equals(""))
				|| ((tv_publish_fragment_position_set.getText().toString())
						.equals(""))) {
			Toast.makeText(getActivity(), "你发布的商品信息不全哦", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	/*
	 * 
	 * 初始化表情面板内容
	 */
	private void initEmotion() {
		emotionViewPagerAdapter = new EmotionViewPagerAdapter(getActivity(),
				et_publish_fragment_description);
		vp_publish_fragement_emoji.setAdapter(emotionViewPagerAdapter);
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
	/*
	 * 回调函数，取得照片路径
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case MessageConfig.SEND_CAMERA_PIC:
			PublishConfigManger.picPath=data.getExtras().getString("picture");
			Toast.makeText(getActivity(), PublishConfigManger.picPath, Toast.LENGTH_LONG).show();
			sendPicture(PublishConfigManger.picPath);
			break;

		case MessageConfig.SEND_LOCAL_PIC:
			PublishConfigManger.picsPath = data.getExtras().getStringArrayList("pictures");
			Toast.makeText(getActivity(), "共发送本地图片"+PublishConfigManger.picsPath.size()+"张", Toast.LENGTH_LONG).show();
			for(String s:PublishConfigManger.picsPath)
			Log.v(TGA, s);
			sendPicture(PublishConfigManger.picsPath);
			break;
		default:
			break;
		}
	}
	//单一图片上传
	private void sendPicture(String filePath) {
		
	}
	//多张图片上传
	private void sendPicture(ArrayList<String> filesPath) {
		
	}
}
