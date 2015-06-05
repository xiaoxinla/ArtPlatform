package com.gexin.artplatform.mine;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.GridView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.adapter.GallaryGridAdapter;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyCollectActivity extends Activity {
	private static final String TAG = "MyCollectActivity";
	private static final String Collection_API = Constant.SERVER_URL
			+ Constant.USER_API;
	private List<String> UrlList = new ArrayList<String>();
	private Gson gson = new Gson();
	private GallaryGridAdapter adapter;
	private GridView mGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_favorite);
		
		initData();
		
	}
	
	@SuppressLint("HandlerLeak")
	private void initData() {
		mGridView = (GridView) findViewById(R.id.gv_collection);
		String userId = (String) SPUtil.get(MyCollectActivity.this, "userId", "");
		String api = Collection_API + "/" + (String) SPUtil.get(this, "userId", "") + "/collection";
		if(!userId.isEmpty()){
			api+="?userId="+userId;
		}
		adapter = new GallaryGridAdapter(MyCollectActivity.this, UrlList);
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
							UrlList.clear();
							UrlList.addAll(success(jObject));
							adapter.notifyDataSetChanged();
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

		new HttpConnectionUtils(handler).get(api);
	}
	
	private List<String> success(JSONObject jObject) {
		int state = -1;
		List<String> tempList = null;
		Log.i(TAG, "jObject:" + jObject.toString());
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				tempList = gson.fromJson(jObject.getJSONArray("collection")
						.toString(), new TypeToken<List<String>>() {
				}.getType());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tempList;
	}
}
