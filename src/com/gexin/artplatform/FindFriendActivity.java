package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;

import com.gexin.artplatform.view.TitleBar;

public class FindFriendActivity extends Activity {

	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private SimpleAdapter adapter;

	private LinearLayout llBack;
	private TitleBar titleBar;
	private ListView mListView;
	private SearchView mSearchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_friend);

		initView();
		initData();
	}

	private void initData() {
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "Í«≥∆" + i);
			mList.add(map);
		}
		adapter = new SimpleAdapter(this, mList, R.layout.find_friend_item,
				new String[] { "name" },
				new int[] { R.id.tv_name_find_friend_item });
		mListView.setAdapter(adapter);
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_find_friend);
		mListView = (ListView) findViewById(R.id.lv_activity_find_friend);
		mSearchView = (SearchView) findViewById(R.id.sv_activity_find_friend);

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
}
