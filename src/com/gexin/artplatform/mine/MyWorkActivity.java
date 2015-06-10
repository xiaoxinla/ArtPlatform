package com.gexin.artplatform.mine;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gexin.artplatform.LargeImageActivity;
import com.gexin.artplatform.R;
import com.gexin.artplatform.adapter.GallaryGridAdapter;
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
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
	private LinearLayout llBack;
	private TitleBar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_gallery);
		initTitleBar();
		mGridView = (GridView) findViewById(R.id.gv_Gallary);
		initData();
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MyWorkActivity.this,
						LargeImageActivity.class);
				intent.putStringArrayListExtra("images",
						(ArrayList<String>) UrlList);
				intent.putExtra("index", arg2);
				startActivity(intent);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private void initData() {
		String userId = (String) SPUtil.get(MyWorkActivity.this, "userId", "");
		String api = Gallary_API + "/"
				+ (String) SPUtil.get(this, "userId", "") + "/problems";
		if (!userId.isEmpty()) {
			api += "?userId=" + userId;
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
								for (int i = 0; i < problemList.size(); i++)
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

	private void initTitleBar() {
		titleBar = (TitleBar) findViewById(R.id.tb_mywork);
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
				Log.v(TAG, "BackClick");
				finish();
			}
		});
	}

}
