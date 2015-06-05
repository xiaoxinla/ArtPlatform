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
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyWorkActivity extends Activity {
	
	private static final String TAG = "MyWorkActivity";
	private static final String Gallary_API = Constant.SERVER_URL
			+ Constant.USER_API;
	private List<String> UrlList = new ArrayList<String>();
	private List<Problem> problemList = new ArrayList<Problem>();
	private Gson gson = new Gson();
	private GallaryGridAdapter adapter;
	private GridView mGridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_gallery);
		initData();
	}
	
	@SuppressLint("HandlerLeak")
	private void initData() {
		mGridView = (GridView) findViewById(R.id.gv_Gallary);
		String userId = (String) SPUtil.get(MyWorkActivity.this, "userId", "");
		String api = Gallary_API + "/" + (String) SPUtil.get(this, "userId", "") + "/problems";
		if(!userId.isEmpty()){
			api+="?userId="+userId;
		}
		adapter = new GallaryGridAdapter(MyWorkActivity.this, UrlList);
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
							List<Problem> tempList = success(jObject);
							if (tempList != null) {
								Log.i(TAG, "tempList != null");
								problemList.clear();
								problemList.addAll(tempList);
								for(int i = 0; i < problemList.size(); i++)
									UrlList.add(problemList.get(i).getImage());
								adapter.notifyDataSetChanged();
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

		new HttpConnectionUtils(handler).get(api);
	}
	
	private List<Problem> success(JSONObject jObject) {
		int state = -1;
		List<Problem> tempList = null;
		Log.i(TAG, "jObject:" + jObject.toString());
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				tempList = gson.fromJson(jObject.getJSONArray("problems")
						.toString(), new TypeToken<List<Problem>>() {
				}.getType());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tempList;
	}

}
