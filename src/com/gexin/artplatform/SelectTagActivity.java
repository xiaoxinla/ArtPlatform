package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;

public class SelectTagActivity extends Activity {

	private static final String TAG = "SelectTagActivity";
	private String[] tagNames = { "����", "ɫ��", "��д", "���", "����", "����", "�ͻ�",
			"�鷨", "�²�" };
	private List<Boolean> checkFlags = new ArrayList<Boolean>();
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	private ListView mListView;
	private TitleBar titleBar;
	private LinearLayout llLeft, llRight;

	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tag);

		initView();
		initData();
	}

	private void initData() {
		for (String str : tagNames) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tagName", str);
			map.put("checkicon", R.drawable.check1);
			list.add(map);
			checkFlags.add(false);
		}
		adapter = new SimpleAdapter(this, list, R.layout.tag_item,
				new String[] { "tagName", "checkicon" }, new int[] {
						R.id.tv_name_tag_item, R.id.iv_check_tag_item });
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				if (checkFlags.get(arg2)) {
//					checkFlags.set(arg2, false);
//				} else {
//					checkFlags.set(arg2, true);
//				}
				list.clear();
				for (int i = 0; i < tagNames.length; i++) {
					if(i==arg2){
						checkFlags.set(i, true);
					}else {
						checkFlags.set(i, false);
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tagName", tagNames[i]);
					if (checkFlags.get(i)) {
						map.put("checkicon", R.drawable.check2);
					} else {
						map.put("checkicon", R.drawable.check1);
					}
					list.add(map);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_tags_select);
		titleBar = (TitleBar) findViewById(R.id.tb_select_tag);
		initTitleBar();
	}

	private void initTitleBar() {
		llLeft = new LinearLayout(this);
		llLeft.setGravity(Gravity.CENTER_VERTICAL);
		TextView tvLeft = new TextView(this);
		tvLeft.setText("��һ��");
		tvLeft.setTextSize(20);
		tvLeft.setTextColor(Color.WHITE);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		llLeft.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llLeft.addView(tvLeft, params);

		llRight = new LinearLayout(this);
		llRight.setGravity(Gravity.CENTER_VERTICAL);
		TextView tvRight = new TextView(this);
		tvRight.setText("�ύ");
		tvRight.setTextSize(20);
		tvRight.setTextColor(Color.WHITE);
		llRight.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llRight.addView(tvRight, params);
		titleBar.setLeftView(llLeft);
		titleBar.setRightView(llRight);

		llLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "LeftClick");
				finish();
			}
		});

		llRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "RightClick");
				postProblem();
			}
		});
	}

	private void postProblem() {
		String userId = (String) SPUtil.get(this, "userId", "");
		String tag = "";
		for(int i=0;i<checkFlags.size();i++){
			if(checkFlags.get(i)){
				tag = tagNames[i];
				break;
			}
		}
		if(tag.isEmpty()){
			Toast.makeText(this, "��Ϊ������ӱ�ǩ", Toast.LENGTH_SHORT).show();
			return ;
		}
		final String postAPI = Constant.SERVER_URL + Constant.USER_API + "/"
				+ userId + "/problem";
		String content = getIntent().getStringExtra("content");
		Handler handler = new HttpHandler(this) {

			@Override
			protected void succeed(JSONObject jObject) {
				success(jObject);
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("content", content));
		list.add(new BasicNameValuePair("tag", tag));
		new HttpConnectionUtils(handler).post(postAPI, list);
	}

	private void success(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				Toast.makeText(this, "���ʳɹ�", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(SelectTagActivity.this,
						MainActivity.class));
				finish();
			} else {
				Toast.makeText(this, "����ʧ��", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
