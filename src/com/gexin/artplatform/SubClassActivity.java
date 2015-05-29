package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gexin.artplatform.adapter.DiscoverGridAdapter;
import com.gexin.artplatform.bean.Classification;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;

public class SubClassActivity extends Activity {

	private List<Classification> mList = new ArrayList<Classification>();
	private String name, classId;
	private DiscoverGridAdapter adapter;
	private Gson gson = new Gson();

	private TitleBar titleBar;
	private GridView mGridView;
	private LinearLayout llBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_class);

		name = getIntent().getStringExtra("name");
		classId = getIntent().getStringExtra("classId");

		initView();
		initData();
	}

	private void initData() {
		titleBar.setTitle(name);
		String url = Constant.SERVER_URL + "/api/classification/" + classId;
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				dealResponse(jObject);
			}
		};
		new HttpConnectionUtils(handler).get(url);
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_subclass);
		mGridView = (GridView) findViewById(R.id.gv_activity_subclass);

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

	protected void dealResponse(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				Classification classification = gson.fromJson(jObject
						.getJSONObject("classification").toString(),
						Classification.class);
				if (classification.getSubclass() != null
						&& !classification.getSubclass().isEmpty()) {
					mList.clear();
					mList.addAll(classification.getSubclass());
					adapter = new DiscoverGridAdapter(this, mList);
					mGridView.setAdapter(adapter);
					mGridView.setNumColumns(2);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
