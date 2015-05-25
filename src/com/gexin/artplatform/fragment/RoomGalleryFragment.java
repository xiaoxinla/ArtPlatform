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
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.gexin.artplatform.R;

public class RoomGalleryFragment extends Fragment {

	private static final String TAG = "RoomGalleryFragment";
	private SimpleAdapter adapter;
	private int[] imageArray = { R.drawable.ic_contact_picture,
			R.drawable.ic_empty, R.drawable.ic_error, R.drawable.ic_launcher,
			R.drawable.ic_menu_emoticons, R.drawable.ic_menu_friendslist,
			R.drawable.ic_menu_home, R.drawable.ic_menu_myplaces,
			R.drawable.ic_menu_notifications, R.drawable.ic_menu_star };
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private GridView mGridView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_room_gallery, container,
				false);
		mGridView = (GridView) view.findViewById(R.id.gv_fragment_room_gallery);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		for (int id : imageArray) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			mList.add(map);
		}
		adapter = new SimpleAdapter(getActivity(), mList,
				R.layout.room_gallery_item, new String[] { "id" },
				new int[] { R.id.iv_room_gallery_item });
		mGridView.setAdapter(adapter);
		Log.v(TAG, "childCount:" + adapter.getCount());
	}
}
