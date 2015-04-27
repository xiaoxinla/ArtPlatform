package com.gexin.artplatform.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gexin.artplatform.QuestionInfo;
import com.gexin.artplatform.R;
import com.gexin.artplatform.adapter.QuestionAdapter;

public class QuestionFragment extends Fragment {

	private ListView mListView;
	private TextView timeFirst, hotFirst;
	private QuestionAdapter adapter;
	private List<Map<String, Object>> questionList;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_question, container,
				false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mListView = (ListView) view.findViewById(R.id.lv_question);
		timeFirst = (TextView) view.findViewById(R.id.tv_timefirst_question);
		hotFirst = (TextView) view.findViewById(R.id.tv_hotfirst_question);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(getActivity(),QuestionInfo.class));
			}
		});
	}

	private void initData() {
		questionList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("header", R.drawable.ic_launcher);
			map.put("count", i);
			map.put("name", "小新xiaoxin");
			map.put("time", "一天前");
			map.put("type", "素描");
			if (i % 2 == 0) {
				map.put("area", "广东");
			}
			if (i % 3 == 0) {
				map.put("commentor", "latest");
			}
			map.put("content", map.toString());
			questionList.add(map);
		}
		adapter = new QuestionAdapter(getActivity(), questionList);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
