package com.gexin.artplatform.fragment;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.gexin.artplatform.adapter.BitmapAdapter;
import com.gexin.artplatform.bean.Classification;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DiscoverFragment extends Fragment {

	private static final String TAG = "DiscoverFragment";
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private String[] discoverTitle = {};
	private String[] discoverImgUrl = {};
	// private int[] discoverIcons = { R.drawable.discover_icon1,
	// R.drawable.discover_icon2, R.drawable.discover_icon3,
	// R.drawable.discover_icon4, R.drawable.discover_icon5,
	// R.drawable.discover_icon6, R.drawable.discover_icon7 };

	private List<Classification> classificationList;
	private GridView mGridView;
	private Gson gson = new Gson();
	private SimpleAdapter adapter;

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
		classificationList = new ArrayList<Classification>();
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
							List<Classification> tempList = success(jObject);
							if (tempList != null) {
								classificationList.clear();
								classificationList.addAll(tempList);
								discoverTitle = new String[classificationList
										.size()];
								discoverImgUrl = new String[classificationList
										.size()];
								for (int i = 0; i < classificationList.size(); i++) {
									discoverTitle[i] = classificationList
											.get(i).getTitle().toString();
									discoverImgUrl[i] = classificationList
											.get(i).getIcon().toString();
									Log.i(TAG, discoverTitle[i]);
									Log.i(TAG, discoverImgUrl[i]);
								}
								initGridView();
							}
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

	private List<Classification> success(JSONObject jObject) {
		int state = -1;
		List<Classification> tempList = null;
		// Log.i(TAG, "jObject:"+jObject.toString());
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				tempList = gson.fromJson(jObject.getJSONArray("contentData")
						.toString(), new TypeToken<List<Classification>>() {
				}.getType());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Log.i(TAG, "success:" + tempList);
		return tempList;

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initData();
	}

	@SuppressLint("HandlerLeak")
	private void initGridView() {
//		for (int i = 0; i < discoverTitle.length; i++) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			// map.put("icon", discoverIcons[i]);
//			map.put("name", discoverTitle[i]);
//			list.add(map);
//		}
		adapter = new BitmapAdapter(getActivity(), list,
				R.layout.discover_item, new String[] { "icon", "name" },
				new int[] { R.id.iv_discover_item, R.id.tv_discover_item });

		final Handler adaptHhandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				
				switch (msg.what){
				case 1:
					mGridView.setAdapter(adapter);
					break;
				default:
					break;
				}
			}
		};
		
		new Thread() {

			public void run() {
				int i = 0;
				while (i < discoverImgUrl.length) {
					try {
						URL uri = new URL(discoverImgUrl[i]);
						URLConnection conn = uri.openConnection();
						conn.connect();
						InputStream is = conn.getInputStream();
						Bitmap bmp = BitmapFactory.decodeStream(is);
						is.close();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("icon", bmp);
						map.put("name", discoverTitle[i]);
						list.add(map);
						adaptHhandler.sendEmptyMessage(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
				}
			}
		}.start();
		


		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.v(TAG, "ClickItem:" + discoverTitle[arg2]);
			}
		});
	}

}
