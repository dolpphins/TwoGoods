package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.async.MultiPicturesAsyncTask;
import com.lym.twogoods.async.MultiPicturesAsyncTaskExecutor;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.GoodsComment;
import com.lym.twogoods.bean.GoodsFocus;
import com.lym.twogoods.bean.Report;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.GoodsConfiguration;
import com.lym.twogoods.dialog.FastLoginDialog;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.index.adapter.GoodsCommentListViewAdapter;
import com.lym.twogoods.index.interf.OnGoodsCommentReplyListener;
import com.lym.twogoods.index.interf.OnPublishCommentListener;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.manager.ImageLoaderHelper;
import com.lym.twogoods.message.listener.RecordPlayClickListener;
import com.lym.twogoods.message.ui.ChatActivity;
import com.lym.twogoods.screen.DisplayUtils;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.ui.StoreDetailActivity;
import com.lym.twogoods.utils.NetworkHelper;
import com.lym.twogoods.utils.TimeUtil;
import com.lym.twogoods.widget.EmojiTextView;
import com.lym.twogoods.widget.WrapContentViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * <p>
 * 	商品详情Fragment
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsDetailFragment extends PullListFragment implements MultiPicturesAsyncTask.OnMultiPicturesAsyncTaskListener,
																	OnPageChangeListener{

	private final static String TAG = "GoodsDetailFragment";
	
	//商品信息
	private Goods mData;
	
	//头部布局
	private LinearLayout mHeaderLayout;
	private WrapContentViewPager mPicturesViewPager;
	
	private RelativeLayout[] mWrapLayouts;
	private ImageView[] mImageViews;
	
	//包含详细信息所有控件
	private DetailMessageViewHolder detailMessageViewHolder = new DetailMessageViewHolder();
	
	/**
	 * 关注相关
	 */
	private boolean mInitFocusInit = false;
	private GoodsFocus mGoodsFocusItem;
	/**
	 * 评论相关
	 * */
	private List<GoodsComment> mGoodsCommentList = new ArrayList<GoodsComment>();
	private int perPageCount = 10;
	private GoodsCommentListViewAdapter mGoodsCommentListViewAdapter;
	private OnGoodsCommentReplyListener mOnGoodsCommentReplyListener;
	
	/**
	 * 举报相关
	 */
	private AlertDialog mReportDialog;
	private EditText report_input;
	private Button report_ok;
	private Button report_cancel;
	
	/**
	 * 语音相关
	 */
	private RecordPlayClickListener mRecognitionListener;
	
	/**
	 * 构造函数
	 * 
	 * @param data 注意不能为空
	 * */
	public GoodsDetailFragment(Goods data,OnGoodsCommentReplyListener l) {
		mData = data;
		mOnGoodsCommentReplyListener = l;
		//如果为null,那么使用默认值
		if(mData == null) {
			Log.i(TAG, "goods detail activity get the goods data is null");
			mData = new Goods();
		}
		//没有图片则创建空集合
		if(mData.getPictureUrlList() == null) {
			Log.i(TAG, "goods detail activity get the goods data's picturesUrlList is null");
			ArrayList<String> picturesUrlList = new ArrayList<String>();
			mData.setPictureUrlList(picturesUrlList);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		//禁止上拉和下拉
		setMode(Mode.NONE);
		
		mHeaderLayout = (LinearLayout) LayoutInflater.from(mAttachActivity).inflate(R.layout.index_goods_detail_fragment_header, null);
		initHeaderView();
	
		initListView();

		//开始获取数据
		getData();
		//开始更新数据
		updateData();
		
		return v;
	}

	
	private void initHeaderView() {
		mPicturesViewPager = (WrapContentViewPager) mHeaderLayout.findViewById(R.id.index_goods_detail_pictures_vp);
		if(mPicturesViewPager != null) {
			mWrapLayouts = new RelativeLayout[mData.getPictureUrlList().size()];
			mImageViews = new ImageView[mData.getPictureUrlList().size()];
			for(int i = 0; i < mWrapLayouts.length; i++) {
				mWrapLayouts[i] = new RelativeLayout(mAttachActivity);
				mImageViews[i] = new ImageView(mAttachActivity);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				params.addRule(Gravity.CENTER);
				mImageViews[i].setLayoutParams(params);
				mImageViews[i].setScaleType(ScaleType.CENTER);
				mImageViews[i].setImageResource(R.drawable.message_chat_pictures_no);
				mWrapLayouts[i].addView(mImageViews[i]);
				setOnClickForImageView(mImageViews[i], i);
			}
			
			PicturesViewPagerAdapter adapter = new PicturesViewPagerAdapter();
			
			//强制设置高度
			int height = (int) (DisplayUtils.getScreenWidthPixels(mAttachActivity) / GoodsConfiguration.GOODS_PICTURE_SCALE);
			mPicturesViewPager.requestForceHeight(height);
			//mPicturesViewPager.requestDisallowInterceptTouchEvent(true);
			mPicturesViewPager.setAdapter(adapter);
		}
		
		//detailMessageViewHolder.index_goods_detail_browse_num = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_browse_num);
		detailMessageViewHolder.index_goods_detail_contact = (ImageView) mHeaderLayout.findViewById(R.id.index_goods_detail_contact);
		detailMessageViewHolder.index_goods_detail_description = (EmojiTextView) mHeaderLayout.findViewById(R.id.index_goods_detail_description);
		detailMessageViewHolder.index_goods_detail_focus = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_focus);
		detailMessageViewHolder.index_goods_detail_fouse_num = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_fouse_num);
		detailMessageViewHolder.index_goods_detail_head_picture = (ImageView) mHeaderLayout.findViewById(R.id.index_goods_detail_head_picture);
		detailMessageViewHolder.index_goods_detail_phone = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_phone);
		detailMessageViewHolder.index_goods_detail_price = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_price);
		detailMessageViewHolder.index_goods_detail_publish_location = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_publish_location);
		detailMessageViewHolder.index_goods_detail_publish_time = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_publish_time);
		detailMessageViewHolder.index_goods_detail_report = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_report);
		detailMessageViewHolder.index_goods_detail_username = (TextView) mHeaderLayout.findViewById(R.id.index_goods_detail_username);
		detailMessageViewHolder.index_goods_detail_voice = (ImageView) mHeaderLayout.findViewById(R.id.index_goods_detail_voice);
		//设置数据
		if(mData != null) {
			Log.i(TAG, "mData != null");
			//描述
			float emojiSize = (float) (detailMessageViewHolder.index_goods_detail_description.getTextSize() * 1.75); 
			detailMessageViewHolder.index_goods_detail_description.setEmojiSize((int) emojiSize);
			detailMessageViewHolder.index_goods_detail_description.setText(mData.getDescription());
			//关注数
			detailMessageViewHolder.index_goods_detail_fouse_num.setText("关注数 " + mData.getFocus_num());
			//浏览数
			//detailMessageViewHolder.index_goods_detail_browse_num.setText("浏览数 " + mData.getBrowse_num() + "");
			//联系方式
			detailMessageViewHolder.index_goods_detail_phone.setText("联系方式 " + mData.getPhone());
			//价格
			detailMessageViewHolder.index_goods_detail_price.setText("￥" + mData.getPrice());
			//发布时间
			detailMessageViewHolder.index_goods_detail_publish_time.setText(TimeUtil.getDescriptionTimeFromTimestamp(mData.getPublish_time()));
			//发布位置
			detailMessageViewHolder.index_goods_detail_publish_location.setText(mData.getPublish_location());
			//用户名
			detailMessageViewHolder.index_goods_detail_username.setText(mData.getUsername());
			
			//头像
			initHeadPicture();
			
			//关注
			initForFocus();
			
			//语音
			initForVoice();
			
			//联系商家
			initForContact();
			
			//举报
			initForReport();
		}
	}
	
	private void initListView() {
		//当弹出软键盘时如果ListView最后一条Item可见那么将ListView顶上去(要配合windowSoftInputMode="adjustResize")
		mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
		//请求强制拦截触摸事件,以解决滚动冲突问题
		mListView.requestForceInterceptTouchEvent(true);
		mListView.addHeaderView(mHeaderLayout);
		mGoodsCommentListViewAdapter = new GoodsCommentListViewAdapter(mAttachActivity, mGoodsCommentList);
		mListView.setAdapter(mGoodsCommentListViewAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 1) {
					return;
				}
				final GoodsComment goodsComment = mGoodsCommentList.get(position - 2);
				AlertDialog.Builder builder = new AlertDialog.Builder(mAttachActivity);
				String[] items = new String[]{"回复"};
				builder.setItems(items, new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if(!UserInfoManager.getInstance().isLogining()) {
							Toast.makeText(mAttachActivity, "请先登录", Toast.LENGTH_SHORT).show();
							return;
						}
						User user = UserInfoManager.getInstance().getmCurrent();
						if(user.getUsername().equals(goodsComment.getUsername())) {
							return;
						}
						switch(which) {
						//回复
						case 0:
							if(mOnGoodsCommentReplyListener != null) {
								mOnGoodsCommentReplyListener.onReply(goodsComment);
							}
							break;
						}
					}
				});
				builder.show();
			}
		});
	}
	
	private void setOnClickForImageView(ImageView iv, final int position) {
		if(iv == null) {
			return;
		}
		iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mAttachActivity, DisplayPicturesActivity.class);
				intent.putStringArrayListExtra("picturesUrlList", mData.getPictureUrlList());
				intent.putExtra("currentIndex", position);
				startActivity(intent);
			}
		});
	}
	
	private void getData() {
		//获取图片
		MultiPicturesAsyncTaskExecutor executor = new MultiPicturesAsyncTaskExecutor(mAttachActivity);
		executor.setOnMultiPicturesAsyncTaskListener(this);
		//String diskCachePath = getIntent().getStringExtra("diskCachePath");
		String diskCachePath = null;
		if(TextUtils.isEmpty(diskCachePath)) {
			//查看商品图片缓存目录
			diskCachePath = DiskCacheManager.getInstance(mAttachActivity).getDefaultPictureCachePath();
		}
		executor.setDiskCacheDir(diskCachePath);
		
		String[] params = new String[mData.getPictureUrlList().size()];
		for(int i = 0; i < mData.getPictureUrlList().size(); i++) {
			params[i] = mData.getPictureUrlList().get(i);
		}
		
		executor.execute(params);
		
		//尝试加载评论信息
		tryLoadCommentDataFromNetwork();
	}
	
	private void updateData() {
//		BmobQuery<Goods> query = new BmobQuery<Goods>();
//		query.addWhereEqualTo("objectId", mData.getObjectId());
//		query.findObjects(mAttachActivity, new FindListener<Goods>() {
//			
//			@Override
//			public void onSuccess(List<Goods> goodsList) {
//				if(goodsList != null && goodsList.size() == 1) {
//					Goods item = goodsList.get(0);
//					item.setBrowse_num(item.getBrowse_num() + 1);
//					item.update(mAttachActivity);
//				}
//			}
//			@Override
//			public void onError(int arg0, String arg1) {				
//			}
//		});
	}
	
	//尝试从网络获取评论信息
	private void tryLoadCommentDataFromNetwork() {
		if(!checkNetworkCondition()) {
			//网络不可用
		} else {
			loadCommentDataFromNetwork();
		}
	}
	
	//检查网络状态
	private boolean checkNetworkCondition() {
		if(NetworkHelper.isNetworkAvailable(mAttachActivity)) {
			return true;
		} else {
			return false;
		}
	}
	
	//通过网络加载评论唯一入口
	private void loadCommentDataFromNetwork() {
		BmobQuery<GoodsComment> query = new BmobQuery<GoodsComment>();
		query.setSkip(mGoodsCommentList.size());
		query.setLimit(perPageCount);
		query.addWhereEqualTo("good_objectId", mData.getObjectId());
		query.findObjects(mAttachActivity, new FindListener<GoodsComment>() {
			
			@Override
			public void onSuccess(List<GoodsComment> goodsCommentList) {
				handleLoadCommentDataFromNetworkFinish(goodsCommentList);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				handleLoadCommentDataFromNetworkFinish(null);
			}
		});
	}
	
	//通过网络加载评论唯一出口
	private void handleLoadCommentDataFromNetworkFinish(List<GoodsComment> goodsCommentList) {
		if(goodsCommentList == null) {
			
		} else if(goodsCommentList.size() <= 0) {
			
		} else {
			mGoodsCommentList.addAll(goodsCommentList);
			mGoodsCommentListViewAdapter.notifyDataSetChanged();
		}
	}
	
	private void initHeadPicture() {
		ImageLoaderHelper.loadUserHeadPictureThumnail(mAttachActivity,
				detailMessageViewHolder.index_goods_detail_head_picture, mData.getHead_url(), null);
		//用户相关信息子布局点击事件,跳转到某一用户主页
		detailMessageViewHolder.index_goods_detail_head_picture.setOnTouchListener(new OnTouchListener() {
					
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					break;
				case MotionEvent.ACTION_UP:
					Intent intent = new Intent(mAttachActivity, StoreDetailActivity.class);
					User user = buildUserByGoods(mData);
					intent.putExtra("user", user);
					mAttachActivity.startActivity(intent);
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	//由Goods对象转User对象
	private User buildUserByGoods(Goods g) {
		if(g == null) {
			return null;
		}
		User user = new User();
		user.setUsername(g.getUsername());
		user.setHead_url(g.getHead_url());
		return user;
	}
	
	//初始化关注
	private void initForFocus() {
		setClickForFocus();
		requestFocusData();
	}
	
	private void requestFocusData() {
		//请求初始化数据
		if(UserInfoManager.getInstance().isLogining()) {
			final User user = UserInfoManager.getInstance().getmCurrent();
			if(user.getUsername().equals(mData.getUsername())) {
				detailMessageViewHolder.index_goods_detail_focus.setVisibility(View.GONE);
			} else {
				BmobQuery<GoodsFocus> query = new BmobQuery<GoodsFocus>();
				query.addWhereEqualTo("username", user.getUsername());
				query.addWhereEqualTo("goods_objectId", mData.getObjectId());
				query.findObjects(mAttachActivity, new FindListener<GoodsFocus>() {
					
					@Override
					public void onSuccess(List<GoodsFocus> goodsFocusList) {
						if(goodsFocusList != null && goodsFocusList.size() == 0) {
							detailMessageViewHolder.index_goods_detail_focus.setText("关注");
//									mGoodsFocusItem = new GoodsFocus();
//									mGoodsFocusItem.setUsername(user.getUsername());
						} else if(goodsFocusList != null && goodsFocusList.size() == 1) {
							detailMessageViewHolder.index_goods_detail_focus.setText("已关注");
							mGoodsFocusItem = goodsFocusList.get(0);
						}
						mInitFocusInit = true;
					}
					
					@Override
					public void onError(int arg0, String arg1) {						
					}
				});
			}
		} else {
			
		}
	}
	
	//item可能为null
	private void setClickForFocus() {
		detailMessageViewHolder.index_goods_detail_focus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!UserInfoManager.getInstance().isLogining()) {
					//提示登录
					showFastLoginDialog();
				} else {
					//已关注
					if(mGoodsFocusItem != null) {
						mGoodsFocusItem.delete(mAttachActivity, new DeleteListener() {
							@Override
							public void onSuccess() {
								detailMessageViewHolder.index_goods_detail_focus.setText("关注");
								mGoodsFocusItem = null;
								//更新关注数
								mData.setFocus_num(mData.getFocus_num() - 1);
								mData.update(mAttachActivity);
								updateUI();
							}
							@Override
							public void onFailure(int arg0, String arg1) {}
						});
					//未关注
					} else if(mInitFocusInit) {
						mGoodsFocusItem = new GoodsFocus();
						mGoodsFocusItem.setUsername(UserInfoManager.getInstance().getmCurrent().getUsername());
						mGoodsFocusItem.setGoods_objectId(mData.getObjectId());
						mGoodsFocusItem.save(mAttachActivity, new SaveListener() {
							@Override
							public void onSuccess() {
								detailMessageViewHolder.index_goods_detail_focus.setText("已关注");
								mData.setFocus_num(mData.getFocus_num() + 1);
								mData.update(mAttachActivity);
								updateUI();
							}
							@Override
							public void onFailure(int arg0, String arg1) {
								mGoodsFocusItem = null;//请求关注失败
							}
						});
					}
				}
			}
		});
	}
	
	//未测试
	private void initForVoice() {
		String url = mData.getVoice_url();
		Log.i(TAG, "voice url:" + url);
		if(TextUtils.isEmpty(url)) {
			detailMessageViewHolder.index_goods_detail_voice.setVisibility(View.GONE);
		} else {
			if(mRecognitionListener == null) {
				mRecognitionListener = new RecordPlayClickListener(mAttachActivity, url,
						detailMessageViewHolder.index_goods_detail_voice, true, false);
			}
		}
	}
	
	private void initForContact() {
		String username = UserInfoManager.getInstance().isLogining() ? UserInfoManager.getInstance().getmCurrent().getUsername() : null; 
		if(username != null && username.equals(mData.getUsername())) {
			detailMessageViewHolder.index_goods_detail_contact.setVisibility(View.GONE);
		} else {
			detailMessageViewHolder.index_goods_detail_contact.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!UserInfoManager.getInstance().isLogining()) {
						//提示登录
						showFastLoginDialog();
					} else {
						//跳转到聊天页面
						User user = new User();
						user.setUsername(mData.getUsername());
						user.setHead_url(mData.getHead_url());
						Intent intent = new Intent(mAttachActivity, ChatActivity.class);
						intent.putExtra("otherUser", user);
						mAttachActivity.startActivity(intent);
						mAttachActivity.finish();
					}
				}
			});
		}
	}
	
	private void initForReport() {
		initWriteReportDialog();
		String username = UserInfoManager.getInstance().isLogining() ? UserInfoManager.getInstance().getmCurrent().getUsername() : null; 
		if(username != null && username.equals(mData.getUsername())) {
			detailMessageViewHolder.index_goods_detail_report.setVisibility(View.GONE);
		} else {
			detailMessageViewHolder.index_goods_detail_report.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!UserInfoManager.getInstance().isLogining()) {
						//提示登录
						showFastLoginDialog();
					} else {
						//弹出举报框
						mReportDialog.show();
					}
				}
			});
		}
	}
	
	private void initWriteReportDialog() {
		mReportDialog = new AlertDialog.Builder(mAttachActivity).create();
		mReportDialog.setCanceledOnTouchOutside(false);
        View v = LayoutInflater.from(mAttachActivity).inflate(R.layout.write_report_layout, null);
        mReportDialog.setView(v);

        report_input = (EditText) v.findViewById(R.id.report_input);
        report_cancel = (Button) v.findViewById(R.id.report_cancel);
        report_ok = (Button) v.findViewById(R.id.report_ok);

        report_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	mReportDialog.cancel();
            }
        });
        report_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = report_input.getText().toString();
                if(!TextUtils.isEmpty(text)) {
                	//再次检查是否登录
                	if(UserInfoManager.getInstance().isLogining()) {
                		String username = UserInfoManager.getInstance().getmCurrent().getUsername();
                		Report report = new Report();
                        report.setMessage(text);
                        report.setGoods_objectId(mData.getObjectId());
                        report.setTime(System.currentTimeMillis());
                        report.setUsername(username);
                        report.save(mAttachActivity, new SaveListener() {
							
							@Override
							public void onSuccess() {
								mReportDialog.cancel();
								Toast.makeText(mAttachActivity, "举报成功", Toast.LENGTH_SHORT).show();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								mReportDialog.cancel();
								Toast.makeText(mAttachActivity, "举报失败", Toast.LENGTH_SHORT).show();
							}
						});
                	}
                }
                report_input.setText("");
            }
        });
    }
	
	/**
	 * 发表评论
	 * 
	 * @param comment
	 * @param replyUsername
	 * @param replyId
	 */
	public void publishComment(String comment, String replyUsername, String replyObjectId, final OnPublishCommentListener listener) {
		final GoodsComment goodsComment = new GoodsComment();
		User user = UserInfoManager.getInstance().getmCurrent();
		goodsComment.setUsername(user.getUsername());
		goodsComment.setComment_username(replyUsername);
		goodsComment.setContent(comment);
		goodsComment.setGood_objectId(mData.getObjectId());
		goodsComment.setParent_objectId(replyObjectId);
		goodsComment.setTime(System.currentTimeMillis());
		goodsComment.save(mAttachActivity, new SaveListener() {
			
			@Override
			public void onSuccess() {
				notifyPublishNewComment(goodsComment);
				if(listener != null) {
					listener.onSuccess();
				}
				//将评论列表滚到最底部
				mListView.setSelection(mListView.getCount() - 1);
				//更新
				BmobQuery<Goods> query = new BmobQuery<Goods>();
				query.addWhereEqualTo("objectId", mData.getObjectId());
				query.findObjects(mAttachActivity, new FindListener<Goods>() {
					
					@Override
					public void onSuccess(List<Goods> goodsList) {
						if(goodsList != null && goodsList.size() == 1) {
							Goods item = goodsList.get(0);
							item.setComment_num(item.getComment_num() + 1);
							item.update(mAttachActivity);
						}
					}
					@Override
					public void onError(int arg0, String arg1) {				
					}
				});
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				//Toast.makeText(mAttachActivity, "发表评论失败", Toast.LENGTH_SHORT).show();
				if(listener != null) {
					listener.onFail();
				}
			}
		});
	}
	
	/**
	 * 通知发表新的评论,该方法会导致UI更新
	 * 
	 * @param goodsComment
	 */
	public void notifyPublishNewComment(GoodsComment goodsComment) {
		if(goodsComment != null) {
			mGoodsCommentList.add(goodsComment);
			mGoodsCommentListViewAdapter.notifyDataSetChanged();
			
		}
	}
	
	private void showFastLoginDialog() {
		FastLoginDialog dialog = new FastLoginDialog(mAttachActivity);
		dialog.setOnFastLoginListener(new GoodsDetailOnFastLoginListener());
		dialog.show();
	}
	
	//更新UI
	private void updateUI() {
		//关注数
		detailMessageViewHolder.index_goods_detail_fouse_num.setText("关注数 " + mData.getFocus_num());
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {		
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {	
	}

	@Override
	public void onPageSelected(int arg0) {
	}

	@Override
	public void onPreExecute(int position) {
		
	}

	@Override
	public void onProgressUpdate(int position, Integer... values) {
	}

	@Override
	public void onPostExecute(int position, Bitmap result) {
		if(result != null) {
			//更改ImageView显示类型
			mImageViews[position].setScaleType(ScaleType.CENTER_CROP);
			mImageViews[position].setImageBitmap(result);
		}
	}
	
	private class PicturesViewPagerAdapter extends PagerAdapter {
		
		@Override
		public int getCount() {
			if(mData.getPictureUrlList() != null) {
				return mData.getPictureUrlList().size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(mWrapLayouts[position % mWrapLayouts.length]);
			
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(mWrapLayouts[position % mWrapLayouts.length], 0);
			return mWrapLayouts[position % mWrapLayouts.length];
		}
		
	}
	
	private static class DetailMessageViewHolder {
		
		/** 描述 */
		private EmojiTextView index_goods_detail_description;

		/** 价格 */
		private TextView index_goods_detail_price;
		
		/** 关注数 */
		private TextView index_goods_detail_fouse_num;
		
		/** 浏览数 */
		//private TextView index_goods_detail_browse_num;
		
		/** 头像 */
		private ImageView index_goods_detail_head_picture; 
		
		/** 用户名 */
		private TextView index_goods_detail_username;
		
		/** 发布时间 */
		private TextView index_goods_detail_publish_time;
		
		/** 发布位置 */
		private TextView index_goods_detail_publish_location;
		
		/** 联系方式 */
		private TextView index_goods_detail_phone;
		
		/** 关注/取消关注 */
		private TextView index_goods_detail_focus;
		
		/** 语音 */
		private ImageView index_goods_detail_voice;
		
		/** 联系商家 */
		private ImageView index_goods_detail_contact;
		
		/** 举报 */
		private TextView index_goods_detail_report;
	}
	
	//快速登录监听器
	private class GoodsDetailOnFastLoginListener implements FastLoginDialog.OnFastLoginListener {

		@Override
		public void onError(int errorCode) {			
		}

		@Override
		public void onSuccess(User user) {
			requestFocusData();
		}
	}

}
