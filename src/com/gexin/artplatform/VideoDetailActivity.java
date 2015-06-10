package com.gexin.artplatform;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.bean.FileInfo;
import com.gexin.artplatform.bean.VideoItem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.services.DownloadService;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VideoDetailActivity extends Activity {

	private static final String TAG = "VideoDetailActivity";
	private String videoId = "";
	private Gson gson = new Gson();
	private VideoItem videoItem = null;
	private FileInfo fileInfo= null;

	private TitleBar titleBar;
	private LinearLayout llBack;
	private TextView tvTitle;
	private TextView tvDescription;
	private TextView tvName;
	private ImageView ivFocus;
	private ImageView ivVideo;
	private ImageButton ibtnPlay;
	private ProgressBar pbProgress;
	private RelativeLayout rlTeacherInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		videoId = getIntent().getStringExtra("id");
		initView();
		initData();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadService.ACTION_UPDATE);
		registerReceiver(mReceiver, filter);
	}

	private void initData() {
		String url = Constant.SERVER_URL + "/api/video/" + videoId;
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				dealResponse(jObject);
			}
		};
		new HttpConnectionUtils(handler).get(url);
	}

	private void dealResponse(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				videoItem = gson.fromJson(
						jObject.getJSONObject("video").toString(), VideoItem.class);
				ImageLoader.getInstance().displayImage(videoItem.getImageUrl(), ivVideo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "�޷�ȡ����Ƶ��Ϣ", Toast.LENGTH_SHORT).show();
		}
		
		fileInfo = new FileInfo(0, videoItem.getVideoUrl(),
				getFileNameFromUrl(videoItem.getVideoUrl()), 0, 0);
		tvTitle.setText(videoItem.getTitle());
		tvDescription.setText(videoItem.getDescription());
		titleBar.setTitle(videoItem.getTitle());
		String name = videoItem.getName();
		if(name!=null&&!name.isEmpty()){
			tvName.setText(videoItem.getName());
			rlTeacherInfo.setVisibility(View.VISIBLE);
		}else {
			rlTeacherInfo.setVisibility(View.GONE);
		}
	}


	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_video_detail);
		tvTitle = (TextView) findViewById(R.id.tv_title_video_detail);
		tvDescription = (TextView) findViewById(R.id.tv_describe_video_detail);
		ibtnPlay = (ImageButton) findViewById(R.id.ibtn_play_video_detail);
		ivVideo = (ImageView) findViewById(R.id.iv_shot_video_detail);
		pbProgress = (ProgressBar) findViewById(R.id.pb_activity_video_detail);
		tvName = (TextView) findViewById(R.id.tv_name_video_detail);
		ivFocus = (ImageView) findViewById(R.id.iv_focus_video_detail);
		rlTeacherInfo = (RelativeLayout) findViewById(R.id.rl_teacher_video_detail);
		initTitleBar();
		pbProgress.setMax(100);
		ibtnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (existFile(fileInfo.getFileName())) {
					Log.v(TAG, "exist:" + true);
					String path = DownloadService.DOWNLOAD_PATH
							+ fileInfo.getFileName();
					Log.v(TAG, "path:" + path);
					Uri uri = Uri.parse("file://" + path);
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

	// ����UI�Ĺ㲥
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (DownloadService.ACTION_UPDATE.equals(arg1.getAction())) {
				int finished = arg1.getIntExtra("finished", 0);
				pbProgress.setProgress(finished);
				if (finished == 100) {
					pbProgress.setVisibility(View.GONE);
					Toast.makeText(VideoDetailActivity.this, "�������",
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
