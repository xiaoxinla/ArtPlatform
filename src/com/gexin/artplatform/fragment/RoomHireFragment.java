package com.gexin.artplatform.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gexin.artplatform.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RoomHireFragment extends Fragment {

	private List<Map<String, Object>> mList;
	private SimpleAdapter adapter;

	private ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_room_commentlist,
				container, false);
		mListView = (ListView) view.findViewById(R.id.lv_fragment_room_common);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		mList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("job", "校长助理" + i);
			map.put("salary", "薪酬面议" + i);
			map.put("require", "会画画" + i);
			mList.add(map);
		}
		adapter = new SimpleAdapter(getActivity(), mList,
				R.layout.room_hire_item, new String[] { "job", "salary",
						"require" }, new int[] { R.id.tv_job_room_hire,
						R.id.tv_salary_room_hire, R.id.tv_require_room_hire });
		mListView.setAdapter(adapter);
	}
}
