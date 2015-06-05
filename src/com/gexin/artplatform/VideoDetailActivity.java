package com.gexin.artplatform;

import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.bean.FileInfo;
import com.gexin.artplatform.services.DownloadService;
import com.gexin.artplatform.view.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VideoDetailActivity extends Activity {

	private static final String TAG = "VideoDetailActivity";
	private String mUrl = "";
	private String mDescription = "";
	private String mTitle = "";
	private String mImageUrl = "";
	private TitleBar titleBar;
	private LinearLayout llBack;
	private TextView tvTitle;
	private TextView tvDescription;
	private ImageView ivVideo;
	private ImageButton ibtnPlay;
	private ProgressBar pbProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		mUrl = getIntent().getStringExtra("url");
		mTitle = getIntent().getStringExtra("title");
		mDescription = getIntent().getStringExtra("description");
		mImageUrl = getIntent().getStringExtra("image");
		initView();
		initData();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadService.ACTION_UPDATE);
		registerReceiver(mReceiver, filter);
	}

	private void initData() {
		if (mUrl != null && !mUrl.isEmpty()) {
		}
		if (mTitle != null && !mTitle.isEmpty()) {
			titleBar.setTitle(mTitle);
			tvTitle.setText(mTitle);
		}
		if (mDescription != null && !mDescription.isEmpty()) {
			tvDescription.setText(mDescription);
		}
		ImageLoader.getInstance().displayImage(mImageUrl, ivVideo);
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_video_detail);
		tvTitle = (TextView) findViewById(R.id.tv_title_video_detail);
		tvDescription = (TextView) findViewById(R.id.tv_describe_video_detail);
		ibtnPlay = (ImageButton) findViewById(R.id.ibtn_play_video_detail);
		ivVideo = (ImageView) findViewById(R.id.iv_shot_video_detail);
		pbProgress = (ProgressBar) findViewById(R.id.pb_activity_video_detail);
		initTitleBar();
		pbProgress.setMax(100);
		final FileInfo fileInfo = new FileInfo(0, mUrl,
				getFileNameFromUrl(mUrl), 0, 0);
		ibtnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "url:" + mUrl);
				if (existFile(fileInfo.getFileName())) {
					Log.v(TAG, "exist:" + true);
					String path = DownloadService.DOWNLOAD_PATH
							+ fileInfo.getFileName();
					Log.v(TAG, "path:" + path);
					Uri uri = Uri.parse("file://"+path);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "video/*");
					startActivity(intent);
				} else {
					Intent intent = new Intent(VideoDetailActivity.this,
							DownloadService.class);
					intent.setAction(DownloadService.ACTION_START);
					intent.putExtra("fileInfo", fileInfo);
					startService(intent);
				}
			}
		});
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

	// 更新UI的广播
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (DownloadService.ACTION_UPDATE.equals(arg1.getAction())) {
				int finished = arg1.getIntExtra("finished", 0);
				pbProgress.setProgress(finished);
				if (finished == 100) {
					pbProgress.setVisibility(View.GONE);
					Toast.makeText(VideoDetailActivity.this, "下载完成",
							Toast.LENGTH_SHORT).show();
					ibtnPlay.setVisibility(View.VISIBLE);
				} else {
					pbProgress.setVisibility(View.VISIBLE);
					ibtnPlay.setVisibility(View.GONE);
				}
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	};

	private String getFileNameFromUrl(String url) {
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		return fileName;
	}

	private boolean existFile(String fileName) {
		File file = new File(DownloadService.DOWNLOAD_PATH, fileName);
		if (file.exists()) {
			return true;
		}
		return false;
	}
}
