package com.gexin.artplatform.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.gexin.artplatform.bean.Article;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class HomeFragment extends Fragment {

	private static final String TAG = "HomeFragment";
	private static final String ARTICLE_API = Constant.SERVER_URL
			+ "/api/articles";
	private List<Article> dataList = new ArrayList<Article>();
	private HomeListAdapter adapter;
	private Gson gson = new Gson();

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
		adapter = new HomeListAdapter(dataList, getActivity());
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						HomeItemInfoActivity.class);
				intent.putExtra("id", dataList.get(arg2-1).getArticleId());
				startActivity(intent);
			}
		});
		getData(1);
	}
	
	@SuppressLint("HandlerLeak")
	private void getData(int option){
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					dealResponse((String) msg.obj);
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		String api = genApiUrl(option, 0);
		new HttpConnectionUtils(handler).get(api);
	}

	private String genApiUrl(int i, long timestamp) {
		String api = ARTICLE_API + "?sort=" + i;
		if (timestamp != 0) {
			api += "?timestamp=" + timestamp;
		}
		return api;
	}

	private void dealResponse(String obj) {
		int state = 0;
		try {
			JSONObject jsonObject = new JSONObject(obj);
			state = jsonObject.getInt("stat");
			if (state == 1) {
				List<Article> tmpList = gson.fromJson(jsonObject.getJSONArray("articles")
						.toString(), new TypeToken<List<Article>>() {
				}.getType());
				dataList.clear();
				dataList.addAll(tmpList);
				Log.v(TAG,"dataList:"+dataList.toString());
				adapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
				getData(1);
			}
		});
		llTimeFirst.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tvHotFirst.setTextColor(Color.parseColor("#cd504f4f"));
				vLeftLine.setVisibility(View.GONE);
				tvTimeFirst.setTextColor(Color.parseColor("#cdfe6060"));
				vRightLine.setVisibility(View.VISIBLE);
				getData(0);
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
		String addr = (String) SPUtil.get(getActivity(), "addr", "π„÷›");
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
