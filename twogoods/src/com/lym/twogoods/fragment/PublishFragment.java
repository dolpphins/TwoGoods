package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.bmob.btp.callback.UploadListener;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.Location;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.nearby.ui.SelectCityActivity;
import com.lym.twogoods.publish.adapter.PublishGridViewAdapter;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.publish.ui.PublishGoodsActivity;
import com.lym.twogoods.publish.ui.PublishSpinner;
import com.lym.twogoods.publish.util.DataMangerUtils;
import com.lym.twogoods.screen.PublishGoodsScreen;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.utils.DatabaseHelper;
import com.lym.twogoods.utils.SensitiveUtils;
import com.lym.twogoods.widget.WrapContentViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
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
	private Context context;
	private PublishGoodsActivity publishGoodsActivity;
	// 定义控件
	private RelativeLayout rl_publish_fragment_main;
	private ImageView iv_publish_fragment_voice;
	private TextView tv_publish_fragment_text_number;
	private PublishSpinner sp_publish_fragment_type;
	private PublishSpinner sp_publish_fragment_date;
	private LinearLayout ll_publish_fragment_category;
	private LinearLayout ll_publish_fragment_date;
	private LinearLayout ll_publish_fragment_tel;
	private LinearLayout ll_publish_fragment_price;
	private LinearLayout ll_publish_fragment_location;
	private EditText et_publish_fragment_tel;
	private EditText et_publish_fragment_price;
	private EditText et_publish_fragment_description;
	private TextView tv_publish_fragment_position_set;
	private GridView gv_publish_fragment_photo;
	private WrapContentViewPager vp_publish_fragement_emoji;

	// 设置日期
	private List<String> items = new ArrayList<String>();
	// 货品信息相关
	private Goods goodsBean;
	private EmotionViewPagerAdapter emotionViewPagerAdapter;
	private Location location = null;

	// 发布货品图片适配器
	private PublishGridViewAdapter publishGridViewAdapter;

	// 上传图片加载器
	private ProgressDialog progressDialog;
	private boolean goodsIsNeedDelet = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View publishfragment = inflater.inflate(
				R.layout.publish_fragment_replace, container, false);

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
		PublishConfigManger.voiceUrl = "";
		PublishConfigManger.voicePath = "";
		rl_publish_fragment_main = (RelativeLayout) view
				.findViewById(R.id.rl_publish_fragment_main);
		ll_publish_fragment_category = (LinearLayout) view
				.findViewById(R.id.ll_publish_fragment_category);
		ll_publish_fragment_date = (LinearLayout) view
				.findViewById(R.id.ll_publish_fragment_date);
		ll_publish_fragment_location = (LinearLayout) view
				.findViewById(R.id.ll_publish_fragment_location);
		ll_publish_fragment_price = (LinearLayout) view
				.findViewById(R.id.ll_publish_fragment_price);
		ll_publish_fragment_tel = (LinearLayout) view
				.findViewById(R.id.ll_publish_fragment_tel);
		iv_publish_fragment_voice = (ImageView) view
				.findViewById(R.id.iv_publish_fragment_voice);
		tv_publish_fragment_text_number = (TextView) view
				.findViewById(R.id.tv_publish_fragment_text_number);
		sp_publish_fragment_type = (PublishSpinner) view
				.findViewById(R.id.sp_publish_fragment_category);
		sp_publish_fragment_date = (PublishSpinner) view
				.findViewById(R.id.sp_publish_fragment_date);
		et_publish_fragment_description = (EditText) view
				.findViewById(R.id.et_publish_fragment_description);
		et_publish_fragment_price = (EditText) view
				.findViewById(R.id.et_publish_fragment_price);
		et_publish_fragment_tel = (EditText) view
				.findViewById(R.id.et_publish_fragment_tel);
		tv_publish_fragment_position_set = (TextView) view
				.findViewById(R.id.tv_publish_fragment_position_set);
		publishGoodsActivity = (PublishGoodsActivity) getActivity();
		context = publishGoodsActivity.getApplicationContext();
		vp_publish_fragement_emoji = (WrapContentViewPager) publishGoodsActivity
				.attrachEmotionViewPager();
		gv_publish_fragment_photo = (GridView) view
				.findViewById(R.id.gv_publish_fragment_photo);
		// 货品信息相关
		goodsBean = new Goods();
		progressDialog = PublishConfigManger.getLoadProgressDialog(
				getActivity(), "正在发布货品信息", "稍等一下......", true);
	}

	/*
	 * 
	 * 为控件添加监听事件或者设置属性
	 */
	private void initEvent() {
		et_publish_fragment_description.addTextChangedListener(mTextWatcher);
		setSpinner();
		ll_publish_fragment_category.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sp_publish_fragment_type.performClick();
			}
		});
		ll_publish_fragment_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sp_publish_fragment_date.performClick();
			}
		});
		ll_publish_fragment_tel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_publish_fragment_tel.requestFocus();
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
			}
		});
		ll_publish_fragment_price.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_publish_fragment_price.requestFocus();
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
			}
		});
		ll_publish_fragment_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						SelectCityActivity.class);
				intent.putExtra(
						PublishConfigManger.publishActivityIdentificationKey,
						"PublishGoodsActivity");
				startActivityForResult(intent,
						PublishConfigManger.PUBLISH_REQUESTCODE);
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

		// 为进度框设置取消监听器
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				new AlertDialog.Builder(getActivity())
						.setMessage("货品信息正努力上传着......" + '\n' + "你真的要放弃吗")
						.setPositiveButton("我确定...",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										goodsIsNeedDelet = true;
										getActivity().finish();
									}
								})
						.setNegativeButton("耐心等一下...",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										progressDialog.show();
									}
								}).show();
			}
		});
	}

	/*
	 * 
	 * 为分类和日期下拉框设置内容
	 */
	private void setSpinner() {
		// 设置分类内容
		sp_publish_fragment_type
				.setList((ArrayList<String>) PublishConfigManger.publishGoodsType);
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				PublishConfigManger.publishGoodsType);
		sp_publish_fragment_type.setAdapter(typeAdapter);

		// 设置价格内容
		for (int i = 0; i < 30; i++) {
			items.add((i + 1) + "");
		}
		sp_publish_fragment_date.setList((ArrayList<String>) items);
		ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, items);
		sp_publish_fragment_date.setAdapter(dateAdapter);
		// 這样子嵌套其它控件的方法。
		/*
		 * et_publish_fragment_description.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * sp_publish_fragment_date.performClick(); } });
		 */
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
		goodsBean.setCategory(sp_publish_fragment_type.getSelectedItem()
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
		goodsBean.setHead_url(UserInfoManager.getInstance().getmCurrent()
				.getHead_url());
		goodsBean.setLocation_latitude(location.getLatitude());
		goodsBean.setLocation_longitude(location.getLongitude());
		goodsBean
				.setPictureUrlList((ArrayList<String>) PublishConfigManger.pictureCloudUrl);
		goodsBean.setVoice_url(PublishConfigManger.voiceUrl);
		goodsBean.setUsername(UserInfoManager.getInstance().getmCurrent()
				.getUsername());
	}

	/*
	 * 
	 * 发布商品到服务器上
	 */
	private void publishGoods() {
		// 最后一个条件是判断上传图片文件是否完全成功才去决定是否上传信息到服务器
		if ((PublishConfigManger.pictureCloudUrl.size() == PublishConfigManger.publishPictureUrl
				.size())) {
			getGoodsData();
			goodsBean.save(publishGoodsActivity, new SaveListener() {

				@Override
				public void onSuccess() {
					progressDialog.dismiss();
					Toast.makeText(publishGoodsActivity, "发布成功",
							Toast.LENGTH_SHORT).show();
					Log.v(TAG, "发布成功");
					if (goodsIsNeedDelet) {
						deleteGoods();
					} else {
						getActivity().finish();
					}
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					progressDialog.dismiss();
					Log.v(TAG, "失败的原因：" + arg1);
					Toast.makeText(publishGoodsActivity, "发布失败",
							Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			progressDialog.dismiss();
			Toast.makeText(getActivity(), "发布失败", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * <p>
	 * 用户放弃发布货品的时候，但是不能取消发布的线程操作，只好等货品发布成功再执行删除操作。
	 * </p>
	 */
	private void deleteGoods() {
		goodsBean.delete(publishGoodsActivity, new DeleteListener() {

			@Override
			public void onSuccess() {
				Log.v(TAG, "删除成功");
				Toast.makeText(publishGoodsActivity, "删除成功", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				deleteGoods();
			}
		});
	}

	/**
	 * <p>
	 * 判断货品描述是否合法，存在敏感词汇自动屏蔽
	 * </p>
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

	/**
	 * <p>
	 * 图片信息上传,内含Goods上传函数
	 * </p>
	 */
	public void pictureUpload() {
		if (DataMangerUtils.judgeGoods(context,
				et_publish_fragment_description, sp_publish_fragment_type,
				et_publish_fragment_tel, et_publish_fragment_price,
				sp_publish_fragment_date, tv_publish_fragment_position_set)) {
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
								progressDialog.dismiss();
								Log.v(TAG, "pictureUpload上传失败" + arg1);
								progressDialog.dismiss();
							}

							@Override
							public void onProgress(int arg0, int arg1,
									int arg2, int arg3) {
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
									ArrayList<String> picFileUrlList = new ArrayList<String>();
									for (int i = 0; i < arg1.length; i++) {
										picFileUrlList.add(arg1[i]);
									}
									goodsBean.setPicFileUrlList(picFileUrlList);
									uploadVoice();
								}
							}

						});
			} else {
				progressDialog.show();
				uploadVoice();
			}
		}
	}

	/**
	 * <p>
	 * 语音上传
	 * </p>
	 */
	private void uploadVoice() {
		if (!PublishConfigManger.voicePath.equals("")) {
			Log.v(TAG, "语音本地路径" + PublishConfigManger.voicePath);
			BmobProFile.getInstance(getActivity()).upload(
					PublishConfigManger.voicePath, new UploadListener() {

						@Override
						public void onError(int arg0, String arg1) {
							progressDialog.dismiss();
							Toast.makeText(getActivity(), "语音上传失败",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(String fileName, String url,
								BmobFile file) {
							PublishConfigManger.voiceUrl = file.getUrl();
							Log.v(TAG, "语音网络URL" + PublishConfigManger.voiceUrl);
							publishGoods();
						}

						@Override
						public void onProgress(int arg0) {

						}
					});
		} else {
			publishGoods();
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

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public EditText getEditTextDescription() {
		return et_publish_fragment_description;
	}

	public EditText getEditTextTel() {
		return et_publish_fragment_tel;
	}

	public EditText getEditTextPrice() {
		return et_publish_fragment_price;
	}

	public TextView getTextViewLocation() {
		return tv_publish_fragment_position_set;
	}

	/**
	 * <p>
	 * 取得最外层布局
	 * </p>
	 * 
	 * @return 返回最外层RelativeLayout
	 */
	public RelativeLayout getRelativeLayoutMain() {
		return rl_publish_fragment_main;
	}

	/**
	 * <p>
	 * 取得语音控件
	 * </p>
	 * 
	 * @return 返回最外层RelativeLayout
	 */
	public ImageView getVoiceImageView() {
		return iv_publish_fragment_voice;
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case PublishConfigManger.PUBLISH_RESULT_OK:
			location = (Location) data
					.getSerializableExtra(PublishConfigManger.publishBackActivityIdentificationKey);
			tv_publish_fragment_position_set.setText(location.getDescription());
			break;
		default:
			break;
		}
	}
	
}
