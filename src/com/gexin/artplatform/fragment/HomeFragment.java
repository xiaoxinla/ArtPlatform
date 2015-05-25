package com.gexin.artplatform.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gexin.artplatform.CityInflateActivity;
import com.gexin.artplatform.HomeItemInfoActivity;
import com.gexin.artplatform.R;
import com.gexin.artplatform.adapter.HomeListAdapter;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class HomeFragment extends Fragment {

	private static final String TAG = "HomeFragment";
	private int sortStyle = 0;// 0表示热度优先，1表示时间优先
	private List<Map<String, Object>> dataList;
	private HomeListAdapter adapter;

	private TitleBar titleBar;
	private LinearLayout llAddr;
	private LinearLayout llTimeFirst;
	private LinearLayout llHotFirst;
	private View vLeftLine;
	private View vRightLine;
	private TextView tvHotFirst;
	private TextView tvTimeFirst;
	private PullToRefreshListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		dataList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "画室" + i);
			map.put("clickNum", 200 * i);
			map.put("id", 100 + i);
			map.put("content", map.hashCode()+"");
			dataList.add(map);
		}
		adapter = new HomeListAdapter(dataList, getActivity());
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(getActivity(),HomeItemInfoActivity.class));
			}
		});
	}

	private void initView(View view) {
		titleBar = (TitleBar) view.findViewById(R.id.tb_fragment_home);
		llTimeFirst = (LinearLayout) view
				.findViewById(R.id.ll_timefirst_fragment_home);
		llHotFirst = (LinearLayout) view
				.findViewById(R.id.ll_hotfirst_fragment_home);
		tvHotFirst = (TextView) view
				.findViewById(R.id.tv_hotfirst_fragment_home);
		tvTimeFirst = (TextView) view
				.findViewById(R.id.tv_timefirst_fragment_home);
		vLeftLine = view.findViewById(R.id.v_leftline_fragment_home);
		vRightLine = view.findViewById(R.id.v_rightline_fragment_home);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.lv_fragment_home);

		initTitleBar();
		llHotFirst.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tvHotFirst.setTextColor(Color.parseColor("#cdfe6060"));
				vLeftLine.setVisibility(View.VISIBLE);
				tvTimeFirst.setTextColor(Color.parseColor("#cd504f4f"));
				vRightLine.setVisibility(View.GONE);
				sortStyle = 0;
			}
		});
		llTimeFirst.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tvHotFirst.setTextColor(Color.parseColor("#cd504f4f"));
				vLeftLine.setVisibility(View.GONE);
				tvTimeFirst.setTextColor(Color.parseColor("#cdfe6060"));
				vRightLine.setVisibility(View.VISIBLE);
				sortStyle = 1;
			}
		});

	}

	private void initTitleBar() {
		llAddr = new LinearLayout(getActivity());
		ImageView ivAddr = new ImageView(getActivity());
		ivAddr.setImageResource(R.drawable.map_icon);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		llAddr.addView(ivAddr, params);
		TextView tvAddr = new TextView(getActivity());
		String addr = (String) SPUtil.get(getActivity(), "addr", "广州");
		tvAddr.setText(addr);
		tvAddr.setTextSize(20);
		tvAddr.setTextColor(Color.WHITE);
		llAddr.addView(tvAddr);
		llAddr.setGravity(Gravity.CENTER_VERTICAL);
		llAddr.setBackgroundResource(R.drawable.selector_titlebar_btn);
		titleBar.setLeftView(llAddr);

		llAddr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(),
						CityInflateActivity.class));
			}
		});
	}
}
