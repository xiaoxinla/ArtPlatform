package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gexin.artplatform.adapter.CitySortAdapter;
import com.gexin.artplatform.bean.CityItem;
import com.gexin.artplatform.utils.HanziToPinyin;
import com.gexin.artplatform.view.SideBar;
import com.gexin.artplatform.view.SideBar.onTouchLetterChangeListener;

public class CityInflateActivity extends Activity {

	private static final String TAG = "CityInflateActivity";
	private List<CityItem> cityList = new ArrayList<CityItem>();
	private CitySortAdapter adapter;

	private ListView mListView;
	private SideBar mSideBar;
	private SearchView mSearchView;
	private TextView tvLetter;
	private View headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_inflate);

		initView();
		initData();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_city_inflate);
		mSideBar = (SideBar) findViewById(R.id.sb_city_inflate);
		mSearchView = (SearchView) findViewById(R.id.sv_city_inflate);
		tvLetter = (TextView) findViewById(R.id.tv_letter_city_inflate);
		// initHeader();
	}

	private void initHeader() {
		headerView = LayoutInflater.from(this).inflate(
				R.layout.city_list_header, null);
		GridView gridView = (GridView) headerView
				.findViewById(R.id.gv_city_header);
		String[] hotCitys = { "北京", "上海", "成都", "杭州", "广州", "沈阳", "昆明", "深圳",
				"武汉", "西安", "南京", "长沙" };
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
		for (String str : hotCitys) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("city", str);
			tmpList.add(map);
		}
		SimpleAdapter gridAdapter = new SimpleAdapter(this, tmpList,
				R.layout.city_header_item, new String[] { "city" },
				new int[] { R.id.tv_name_city_header });
		gridView.setAdapter(gridAdapter);
		mListView.addHeaderView(headerView);
	}

	private void initData() {
		cityList = CityItem.getCityList();
		Log.v(TAG, "size:" + cityList.size());
		try {
			Collections.sort(cityList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!HanziToPinyin.getInstance().getHasChinaCollator()) {
			mSideBar.setVisibility(View.GONE);
		}
		adapter = new CitySortAdapter(cityList, this);
		mListView.setAdapter(adapter);
		mSideBar.setLetterShow(tvLetter);
		mSideBar.setTouchLetterChangeListener(new onTouchLetterChangeListener() {

			@Override
			public void onTouchLetterChange(String letter) {
				int position = adapter.getPositionForSection(letter.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}
			}
		});
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				onQueryChange(arg0);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				onQueryChange(arg0);
				return false;
			}
		});
	}

	@SuppressLint("DefaultLocale")
	private void onQueryChange(String arg0) {
		if (arg0.isEmpty()) {
			adapter.updateData(cityList);
		} else {
			List<CityItem> tmpList = new ArrayList<CityItem>();
			arg0 = arg0.toLowerCase();
			for (CityItem item : cityList) {
				if (item.getName().contains(arg0)
						|| item.getFullPinyin().contains(arg0)
						|| item.getSimplePinyin().contains(arg0)) {
					tmpList.add(item);
				}
			}
			adapter.updateData(tmpList);
		}
	}
}
