package com.gexin.artplatform.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.gexin.artplatform.PostProblemActivity;
import com.gexin.artplatform.QuestionInfo;
import com.gexin.artplatform.R;
import com.gexin.artplatform.adapter.QuestionAdapter;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.entity.Problem;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.NetUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 问答Fragment
 * 
 * @author xiaoxin 2015-4-29
 */
public class QuestionFragment extends Fragment {

	private static final String TAG = "QuestionFragment";
	private static final int POST_REQUEST_CODE = 1;
	private static final String PROBLEMS_API = Constant.SERVER_URL
			+ Constant.PROBLEM_API + "s";

	private PullToRefreshListView mListView;
	private Button btnAsk;
	private QuestionAdapter adapter;
	private List<Problem> problems;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_question, container,
				false);
		initView(view);
		return view;
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 */
	private void initView(View view) {
		mListView = (PullToRefreshListView) view.findViewById(R.id.lv_question);
		btnAsk = (Button) view.findViewById(R.id.btn_ask_question);

		btnAsk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(getActivity(),
						PostProblemActivity.class), POST_REQUEST_CODE);
			}
		});
		// 设置为上下两侧都可以拉动
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				new GetLatestDataTask().execute();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new GetNextDataTask().execute();
			}
		});

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Problem problem = (Problem) adapter.getItem(arg2);
				Intent intent = new Intent(getActivity(), QuestionInfo.class);
				intent.putExtra("problemId", problem.getId());
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化数据
	 */
	@SuppressLint("HandlerLeak")
	private void initData() {
		problems = new ArrayList<Problem>();
		adapter = new QuestionAdapter(getActivity(), problems);
		mListView.setAdapter(adapter);
		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					String response = (String) msg.obj;
					try {
						JSONObject jObject = new JSONObject(
								response == null ? "" : response.trim());
						success(jObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}

		};

		new HttpConnectionUtils(handler).get(PROBLEMS_API);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "requestCode:" + requestCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case POST_REQUEST_CODE:

				break;

			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class GetLatestDataTask extends
			AsyncTask<Void, Void, List<Problem>> {

		@Override
		protected List<Problem> doInBackground(Void... params) {
			// Simulates a background job.
			String result = "";
			result = NetUtil.connect(NetUtil.GET, PROBLEMS_API, null);
			// Log.v(TAG, "result:" + result);
			try {
				JSONObject jObject = new JSONObject(result == null ? ""
						: result.trim());
				List<Problem> tempList = analyzeJson(jObject);
				if (tempList != null) {
					problems.clear();
					problems.addAll(tempList);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return problems;
		}

		@Override
		protected void onPostExecute(List<Problem> result) {
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	private class GetNextDataTask extends AsyncTask<Void, Void, List<Problem>> {

		@Override
		protected List<Problem> doInBackground(Void... params) {
			// Simulates a background job.
			String result = "";
			String prm = "?skip=" + problems.size();
			result = NetUtil.connect(NetUtil.GET, PROBLEMS_API + prm, null);
			// Log.v(TAG, "result:" + result);
			try {
				JSONObject jObject = new JSONObject(result == null ? ""
						: result.trim());
				List<Problem> tempList = analyzeJson(jObject);
				if (tempList != null) {
					problems.addAll(tempList);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return problems;
		}

		@Override
		protected void onPostExecute(List<Problem> result) {
			Log.v(TAG, "problemNum:" + problems.size());
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * 获取后台数据成功执行的操作
	 * 
	 * @param jObject
	 */
	private void success(JSONObject jObject) {
		List<Problem> tempList = analyzeJson(jObject);
		if (tempList != null) {
			problems.clear();
			problems.addAll(tempList);
			adapter.notifyDataSetChanged();
		}

	}

	private List<Problem> analyzeJson(JSONObject jObject) {
		List<Problem> list = null;
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				list = new ArrayList<Problem>();
				JSONArray jsonArray = jObject.getJSONArray("problems");
				JSONObject jsonObject;
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					list.add(Problem.analyzeJson(jsonObject));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
