package com.gexin.artplatform;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.gexin.artplatform.constant.Constant;

public class WelcomeActivity extends Activity {

	private static final int sleepTime = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final View view = View.inflate(this, R.layout.activity_welcome, null);
		setContentView(view);
		super.onCreate(savedInstanceState);
		
		createFileDirs();
		
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		view.startAnimation(animation);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 进入主页面
				startActivity(new Intent(WelcomeActivity.this,
						MainActivity.class));
				finish();
			}
		}).start();
	}
	
	private void createFileDirs(){
		File dirFile = new File(Constant.APP_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
	}
}
