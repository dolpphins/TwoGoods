package com.lym.twogoods.fragment;

import java.io.IOException;
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
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.publish.adapter.PublishGridViewAdapter;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.publish.ui.PublishGoodsActivity;
import com.lym.twogoods.publish.util.PublishBimp;
import com.lym.twogoods.screen.PublishGoodsScreen;
import com.lym.twogoods.utils.DatabaseHelper;
import com.lym.twogoods.utils.SensitiveUtils;
import com.lym.twogoods.widget.WrapContentViewPager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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

	// 上下文
	private PublishGoodsActivity publishGoodsActivity;
	// 定义控件
	private TextView tv_publish_fragment_text_number;
	private Spinner sp_publish_fragment_sort;
	private Spinner sp_publish_fragment_date;
	private EditText et_publish_fragment_tel;
	private EditText et_publish_fragment_price;
	private EditText et_publish_fragment_description;
	private TextView tv_publish_fragment_position_set;
	private GridView gv_publish_fragment_photo;
	private Button btn_publish_fragment_position;
	private WrapContentViewPager vp_publish_fragement_emoji;

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

	// 发布货品图片适配器
	private PublishGridViewAdapter publishGridViewAdapter;

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
		PublishConfigManger.publishPictureUrl.clear();
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
		/*
		 * iv_publish_fragment_add_photo = (ImageView) view
		 * .findViewById(R.id.iv_publish_fragment_add_photo);
		 * iv_publish_fragment_add_smile = (ImageView) view
		 * .findViewById(R.id.iv_publish_fragment_add_smile);
		 * iv_publish_fragment_add_voice = (ImageView) view
		 * .findViewById(R.id.iv_publish_fragment_add_voice);
		 */
		/*
		 * vp_publish_fragement_emoji = (WrapContentViewPager) view
		 * .findViewById(R.id.vp_publish_fragment_emoji);
		 */
		publishGoodsActivity = (PublishGoodsActivity) getActivity();
		vp_publish_fragement_emoji = (WrapContentViewPager) publishGoodsActivity
				.attrachEmotionViewPager();
		btn_publish_fragment_position = (Button) view
				.findViewById(R.id.btn_publish_fragment_position);
		/*
		 * ll_publish_fragment_emoji = (LinearLayout) view
		 * .findViewById(R.id.ll_publish_fragment_emoji);
		 */
		gv_publish_fragment_photo = (GridView) view
				.findViewById(R.id.gv_publish_fragment_photo);
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

		et_publish_fragment_description
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mEmotionLayoutIsShowing) {
							hideEmotionLayout();
						}
					}
				});
		et_publish_fragment_price.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mEmotionLayoutIsShowing) {
					hideEmotionLayout();
				}
			}
		});
		et_publish_fragment_tel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mEmotionLayoutIsShowing) {
					hideEmotionLayout();
				}
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

		// 定位监听按钮
		btn_publish_fragment_position.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isFirst = true;
				locationClient.requestLocation();
			}
		});
		gv_publish_fragment_photo
				.setNumColumns(PublishConfigManger.PUBLISH_PICTURE_GRIDVIEW_COLUMN);
		gv_publish_fragment_photo
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						LayoutInflater inflater = LayoutInflater
								.from(getActivity());
						View inflateView = inflater.inflate(
								R.layout.publish_picture_gridview_item_dialog,
								null);
						final AlertDialog alertDialog = new AlertDialog.Builder(
								getActivity()).create();
						ImageView imageView = (ImageView) inflateView
								.findViewById(R.id.iv_publish_gridview_item_dialog);
						try {
							imageView.setImageBitmap(PublishBimp
									.revitionImageSize(PublishConfigManger.publishPictureUrl
											.get(position)));
						} catch (IOException e) {
							e.printStackTrace();
						}
						alertDialog.setView(inflateView);
						alertDialog.show();
						imageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								alertDialog.cancel();
							}
						});
					}
				});
	}

	/*
	 * 
	 * 表情键盘切换与隐藏的关系
	 */

	private void hideEmotionLayout() {
		vp_publish_fragement_emoji.setVisibility(View.GONE);
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
		if (!PublishConfigManger.publishPictureUrl.isEmpty()) {
			goodsBean
					.setPictureUrlList((ArrayList<String>) PublishConfigManger.publishPictureUrl);
		}
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

	public EditText getEditTextDescription() {
		return et_publish_fragment_description;
	}

	public void notifyGridView(List<String> paths) {
		if (PublishConfigManger.publishPictureUrl.size() != 0) {
			publishGridViewAdapter = new PublishGridViewAdapter(getActivity(),
					PublishConfigManger.publishPictureUrl);
			int row = PublishConfigManger.publishPictureUrl.size() / 3;
			if (PublishConfigManger.publishPictureUrl.size() % 3 != 0) {
				row++;
			}
			int interval = getResources().getDimensionPixelSize(
					R.dimen.app_publish_fragment_gridview_verticalspacing);
			PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
			specification = PublishGoodsScreen
					.getPublishPictureThumbnailSpecification(getActivity());
			int height = specification.getHeight() * row + (row - 1) * interval;
			LayoutParams params = gv_publish_fragment_photo.getLayoutParams();
			params.height = height;
			gv_publish_fragment_photo.setAdapter(publishGridViewAdapter);
			// publishGridViewAdapter.notifyDataSetChanged();
		}
	}
}
