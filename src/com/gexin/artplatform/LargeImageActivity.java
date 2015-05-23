package com.gexin.artplatform;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gexin.artplatform.view.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class LargeImageActivity extends Activity {

	private String imageUrl = "";
	private PhotoViewAttacher mAttacher;

	private ImageView ivLargeImage;
	private TitleBar titleBar;
	private LinearLayout llBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_large_image);

		initView();
		initData();
	}

	private void initData() {
		DisplayImageOptions picOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageUrl = getIntent().getStringExtra("url");
		ImageLoader.getInstance().displayImage(imageUrl, ivLargeImage,
				picOptions, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						mAttacher = new PhotoViewAttacher(ivLargeImage);
						mAttacher.setScaleType(ScaleType.FIT_CENTER);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});

	}

	private void initView() {
		ivLargeImage = (ImageView) findViewById(R.id.iv_large_image);
		titleBar = (TitleBar) findViewById(R.id.tb_large_image);
		initTitleBar();
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

		llBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		if (mAttacher != null) {
			mAttacher.cleanup();
		}
		super.onDestroy();
	}
}
