package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class LargeImageActivity extends Activity {

	private static final String TAG = "LargeImageActivity";
	private List<String> imageList;
	private LargeImageAdapter adapter;
	private boolean isShow = false;

	private ViewPager mViewPager;
	private TitleBar titleBar;
	private LinearLayout llBack;
	private LinearLayout llMore;
	private LinearLayout llCmdFrame;
	private TextView tvCollect;
	private TextView tvShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_large_image);

		initView();
		initData();
	}

	private void initData() {
		imageList = getIntent().getStringArrayListExtra("images");
		adapter = new LargeImageAdapter(imageList, this);
		mViewPager.setAdapter(adapter);
		// ImageLoader.getInstance().displayImage(imageUrl, ivLargeImage,
		// picOptions, new ImageLoadingListener() {
		//
		// @Override
		// public void onLoadingStarted(String arg0, View arg1) {
		//
		// }
		//
		// @Override
		// public void onLoadingFailed(String arg0, View arg1,
		// FailReason arg2) {
		//
		// }
		//
		// @Override
		// public void onLoadingComplete(String arg0, View arg1,
		// Bitmap arg2) {
		// mAttacher = new PhotoViewAttacher(ivLargeImage);
		// mAttacher.setScaleType(ScaleType.FIT_CENTER);
		// }
		//
		// @Override
		// public void onLoadingCancelled(String arg0, View arg1) {
		//
		// }
		// });

	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_large_image);
		titleBar = (TitleBar) findViewById(R.id.tb_large_image);
		llCmdFrame = (LinearLayout) findViewById(R.id.ll_cmd_large_image);
		tvCollect = (TextView) findViewById(R.id.tv_collect_large_image);
		tvShare = (TextView) findViewById(R.id.tv_share_large_image);
		initTitleBar();

		tvCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				addCollection();
				isShow = false;
				llCmdFrame.setVisibility(View.GONE);

			}
		});
		tvShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(LargeImageActivity.this, "分享成功",
						Toast.LENGTH_SHORT).show();
				isShow = false;
				llCmdFrame.setVisibility(View.GONE);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private void addCollection() {
		int index = mViewPager.getCurrentItem();
		String imageUrl = imageList.get(index);
		String userId = (String) SPUtil.get(this, "userId", "");
		Log.v(TAG, "url:" + imageUrl);
		if (userId.isEmpty()) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			return;
		}
		String url = Constant.SERVER_URL + "/api/user/" + userId
				+ "/collection";
		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							Toast.makeText(LargeImageActivity.this, "收藏成功",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(LargeImageActivity.this, "收藏失败",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(LargeImageActivity.this, "收藏失败",
								Toast.LENGTH_SHORT).show();
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}

		};
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("imageUrl",imageUrl));
		new HttpConnectionUtils(handler).put(url, data);
	}

	private void initTitleBar() {
		llBack = new LinearLayout(this);
		ImageView ivBack = new ImageView(this);
		ivBack.setImageResource(R.drawable.back_icon);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		llBack.addView(ivBack, params);
		llBack.setGravity(Gravity.CENTER_VERTICAL);
		llBack.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llBack.setPadding(20, 0, 20, 0);
		titleBar.setLeftView(llBack);

		llMore = new LinearLayout(this);
		ImageView ivMore = new ImageView(this);
		ivMore.setImageResource(R.drawable.more_icon);
		llMore.addView(ivMore, params);
		llMore.setGravity(Gravity.CENTER_VERTICAL);
		llMore.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llMore.setPadding(20, 0, 20, 0);
		titleBar.setRightView(llMore);

		llBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		llMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isShow) {
					isShow = false;
					llCmdFrame.setVisibility(View.GONE);
				} else {
					isShow = true;
					llCmdFrame.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// adapter.clearAttacher();
		super.onDestroy();
	}

	private static class LargeImageAdapter extends PagerAdapter {

		private List<String> mList;
		private LayoutInflater inflater;
		private DisplayImageOptions picOptions;

		// private PhotoViewAttacher mAttacher;

		public LargeImageAdapter(List<String> mList, Context context) {
			super();
			this.mList = mList;
			this.inflater = LayoutInflater.from(context);
			picOptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		}

		// public void clearAttacher() {
		// if(mAttacher!=null){
		// mAttacher.cleanup();
		// }
		// }

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_large_page, view,
					false);
			assert imageLayout != null;
			final ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.iv_large_image_item);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.pb_large_image_item);

			ImageLoader.getInstance().displayImage(mList.get(position),
					imageView, picOptions, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
							}
							Toast.makeText(view.getContext(), message,
									Toast.LENGTH_SHORT).show();

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
							// mAttacher = new PhotoViewAttacher(imageView);
							// mAttacher.setScaleType(ScaleType.FIT_CENTER);
						}
					});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}
