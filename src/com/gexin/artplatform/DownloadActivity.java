package com.gexin.artplatform;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gexin.artplatform.bean.FileInfo;
import com.gexin.artplatform.services.DownloadService;

public class DownloadActivity extends Activity {

	private TextView tvFileName;
	private ProgressBar pbProgress;
	private Button btnStart, btnPause;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);

		initView();
		initData();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadService.ACTION_UPDATE);
		registerReceiver(mReceiver, filter);
	}

	private void initData() {
		pbProgress.setMax(100);
		final FileInfo fileInfo = new FileInfo(0,
				"http://iweixun.sinaapp.com/Contacts.apk",
				"Contacts.apk", 0, 0);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(DownloadActivity.this,DownloadService.class);
				intent.setAction(DownloadService.ACTION_START);
				intent.putExtra("fileInfo", fileInfo);
				startService(intent);
			}
		});
		btnPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(DownloadActivity.this,DownloadService.class);
				intent.setAction(DownloadService.ACTION_STOP);
				intent.putExtra("fileInfo", fileInfo);
				startService(intent);
			}
		});
	}

	private void initView() {
		tvFileName = (TextView) findViewById(R.id.tv_filename_activity_download);
		pbProgress = (ProgressBar) findViewById(R.id.pb_progress_activity_download);
		btnStart = (Button) findViewById(R.id.btn_start_activity_download);
		btnPause = (Button) findViewById(R.id.btn_pause_activity_download);
	}
	
	//更新UI的广播
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (DownloadService.ACTION_UPDATE.equals(arg1.getAction())) {
				int finished = arg1.getIntExtra("finished", 0);
				pbProgress.setProgress(finished);
			}
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	};
}
