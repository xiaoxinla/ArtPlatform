package com.gexin.artplatform;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;

public class TeacherDetailActivity extends Activity {

	private TitleBar titleBar;
	private LinearLayout llBack,llAsk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_detail);
		
		initView();
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_teacher_detail);
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

			llAsk = new LinearLayout(this);
			TextView tvAsk = new TextView(this);
			tvAsk.setText("向他提问");
			tvAsk.setTextSize(20);
			tvAsk.setTextColor(Color.WHITE);
			params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(10, 0, 10, 0);
			llAsk.setGravity(Gravity.CENTER_VERTICAL);
			llAsk.addView(tvAsk, params);
			llAsk.setBackgroundResource(R.drawable.selector_titlebar_btn);
			titleBar.setRightView(llAsk);

			llBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
			llAsk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String userId = (String) SPUtil.get(TeacherDetailActivity.this,
							"userId", "");
					if (userId.isEmpty()) {
						Toast.makeText(TeacherDetailActivity.this, "请先登录再提问",
								Toast.LENGTH_SHORT).show();
						return;
					}
					startActivity(new Intent(TeacherDetailActivity.this,
							PostProblemActivity.class));
				}
			});
	}
}
