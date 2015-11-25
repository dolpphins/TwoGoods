package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.publish.adapter.PublishGridViewAdapter;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.publish.ui.PublishGoodsActivity;
import com.lym.twogoods.screen.PublishGoodsScreen;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.utils.DatabaseHelper;
import com.lym.twogoods.utils.SensitiveUtils;
import com.lym.twogoods.widget.WrapContentViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * <p>
 * 发布Fragment
 * </p>
 * 
 * @author 龙宇文
 * */
public class PublishFragment extends BaseFragment {

	private String TAG = "PublishFragment";
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

	// 货品信息相关
	private Goods goodsBean;
	private EmotionViewPagerAdapter emotionViewPagerAdapter;

	// 发布货品图片适配器
	private PublishGridViewAdapter publishGridViewAdapter;

	// 上传图片加载器
	private ProgressDialog progressDialog;

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
		// 清空图片信息
		PublishConfigManger.publishPictureUrl.clear();
		PublishConfigManger.pictureCloudUrl.clear();
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
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle("正在发布货品信息");
		progressDialog.setMessage("稍等一下......");
		progressDialog.setCancelable(true);
		progressDialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(true);

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
						Intent intent = new Intent(getActivity(),
								DisplayPicturesActivity.class);
						intent.putStringArrayListExtra(
								"picturesUrlList",
								(ArrayList<String>) PublishConfigManger.publishPictureUrl);
						intent.putExtra("currentIndex", position);
						startActivity(intent);
					}
				});
		// 编辑框失去焦点时隐藏表情
		et_publish_fragment_description
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						publishGoodsActivity.hideEmotionLayout();
					}
				});
		et_publish_fragment_description
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						publishGoodsActivity.hideEmotionLayout();
					}
				});

		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				PublishConfigManger.pictureCloudUrl.clear();
				progressDialog.dismiss();
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
		goodsBean.setHead_url(UserInfoManager.getInstance().getmCurrent()
				.getHead_url());
		goodsBean
				.setPictureUrlList((ArrayList<String>) PublishConfigManger.pictureCloudUrl);
		goodsBean.setUsername(UserInfoManager.getInstance().getmCurrent()
				.getUsername());
	}

	/*
	 * 
	 * 发布商品到服务器上
	 */
	private void publishGoods() {
		// 最后一个条件是判断上传图片文件是否完全成功才去决定是否上传信息到服务器
		if (judgeDescription()
				&& judgeGoods()
				&& (PublishConfigManger.pictureCloudUrl.size() == PublishConfigManger.publishPictureUrl
						.size())) {
			getGoodsData();
			goodsBean.save(getActivity(), new SaveListener() {

				@Override
				public void onSuccess() {
					progressDialog.dismiss();
					Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT)
							.show();
					getActivity().finish();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(getActivity(), "发布失败", Toast.LENGTH_SHORT)
							.show();
				}
			});
		} else {
			Toast.makeText(getActivity(), "发布失败", Toast.LENGTH_SHORT).show();
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
	 * 图片信息上传
	 */
	public void pictureUpload() {
		if (!PublishConfigManger.publishPictureUrl.isEmpty()) {
			// List转String[]
			final String[] files = PublishConfigManger.publishPictureUrl
					.toArray(new String[PublishConfigManger.publishPictureUrl
							.size()]);
			progressDialog.show();
			BmobProFile.getInstance(getActivity()).uploadBatch(files,
					new UploadBatchListener() {
						@Override
						public void onError(int arg0, String arg1) {
							Log.v(TAG, "pictureUpload上传失败" + arg1);
							progressDialog.dismiss();
						}

						@Override
						public void onProgress(int arg0, int arg1, int arg2,
								int arg3) {
							Log.i("PublishFrament", "onProgress :" + arg0
									+ "---" + arg1 + "---" + arg2 + "----"
									+ arg3);
						}

						@Override
						public void onSuccess(boolean arg0, String[] arg1,
								String[] arg2, BmobFile[] arg3) {
							if (arg0) {
								for (int i = 0; i < arg3.length; i++) {
									PublishConfigManger.pictureCloudUrl
											.add(arg3[i].getUrl());
								}
								publishGoods();
							}
						}

					});
		}
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
			int row = PublishConfigManger.publishPictureUrl.size()
					/ PublishConfigManger.PUBLISH_PICTURE_GRIDVIEW_COLUMN;
			if (PublishConfigManger.publishPictureUrl.size()
					% PublishConfigManger.PUBLISH_PICTURE_GRIDVIEW_COLUMN != 0) {
				row++;
			}
			PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
			specification = PublishGoodsScreen
					.getPublishPictureThumbnailSpecification(getActivity());
			int height = specification.getHeight() * row + (row - 1)
					* specification.getHeight()
					/ PublishConfigManger.PICTURE_RATE;
			LayoutParams params = gv_publish_fragment_photo.getLayoutParams();
			params.height = height;
			gv_publish_fragment_photo.setVerticalSpacing(specification
					.getHeight() / PublishConfigManger.PICTURE_RATE);
			gv_publish_fragment_photo.setHorizontalSpacing(specification
					.getHeight() / PublishConfigManger.PICTURE_RATE);
			gv_publish_fragment_photo.setAdapter(publishGridViewAdapter);
		}
	}

}
