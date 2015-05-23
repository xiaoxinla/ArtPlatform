package com.gexin.artplatform.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.adapter.DiscoverGridAdapter;
import com.gexin.artplatform.bean.Classification;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DiscoverFragment extends Fragment {

	private static final String TAG = "DiscoverFragment";
	private List<Map<String, Object>> discoverList = new ArrayList<Map<String,Object>>();
	// private int[] discoverIcons = { R.drawable.discover_icon1,
	// R.drawable.discover_icon2, R.drawable.discover_icon3,
	// R.drawable.discover_icon4, R.drawable.discover_icon5,
	// R.drawable.discover_icon6, R.drawable.discover_icon7 };

	private GridView mGridView;
	private Gson gson = new Gson();
	private DiscoverGridAdapter adapter;

	private static final String Discover_API = Constant.SERVER_URL
			+ Constant.Discover_API + "/index";

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

	@SuppressLint("HandlerLeak")
	private void initData() {
		adapter = new DiscoverGridAdapter(getActivity(),discoverList);
		mGridView.setAdapter(adapter);
		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					String response = (String) msg.obj;
					try {
						JSONObject jObject = new JSONObject(
								response == null ? "" : response.trim());
						if (jObject != null) {
							success(jObject);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}

		};
		new HttpConnectionUtils(handler).get(Discover_API);
	}

	private void success(JSONObject jObject) {
		int state = -1;
		List<Classification> tempList = null;
		 Log.i(TAG, "jObject:"+jObject.toString());
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				tempList = gson.fromJson(jObject.getJSONArray("contentData")
						.toString(), new TypeToken<List<Classification>>() {
				}.getType());
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("title", tempList.get(i).getTitle());
						map.put("icon", tempList.get(i).getIcon());
						discoverList.add(map);
					}
					adapter.notifyDataSetChanged();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Log.i(TAG, "success:" + tempList);

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initData();
	}


}
