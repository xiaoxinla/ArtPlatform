package com.gexin.artplatform.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.gexin.artplatform.R;

public class DiscoverFragment extends Fragment {

	private static final String TAG = "DiscoverFragment";
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private String[] discoverItems = { "高分考卷", "名师课堂", "优秀画室", "素描", "色彩",
			"速写", "设计" };
	private int[] discoverIcons = { R.drawable.discover_icon1,
			R.drawable.discover_icon2, R.drawable.discover_icon3,
			R.drawable.discover_icon4, R.drawable.discover_icon5,
			R.drawable.discover_icon6, R.drawable.discover_icon7 };

	private GridView mGridView;
	private SimpleAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_discover, container,
				false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.gv_discover_fragment);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initGridView();
	}

	private void initGridView() {
		for (int i = 0; i < discoverItems.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", discoverIcons[i]);
			map.put("name", discoverItems[i]);
			list.add(map);
		}
		adapter = new SimpleAdapter(getActivity(), list,
				R.layout.discover_item, new String[] { "icon", "name" },
				new int[] { R.id.iv_discover_item, R.id.tv_discover_item });
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.v(TAG, "ClickItem:" + discoverItems[arg2]);
			}
		});
	}
}
