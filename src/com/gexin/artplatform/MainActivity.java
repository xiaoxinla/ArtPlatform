package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.gexin.artplatform.adapter.FragmentVPAdapter;
import com.gexin.artplatform.fragment.DiscoverFragment;
import com.gexin.artplatform.fragment.HomeFragment;
import com.gexin.artplatform.fragment.OfflineFragment;
import com.gexin.artplatform.fragment.QuestionFragment;
import com.gexin.artplatform.fragment.StudentFragment;
import com.gexin.artplatform.fragment.TeacherFragment;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.ChangeColorIconWithText;

public class MainActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {

	private static final String TAG = "MainActivity";
	private String state = "";
	
	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private HomeFragment homeFragment;
	private FragmentVPAdapter mAdapter;
	private QuestionFragment questionFragment;
	private DiscoverFragment discoverFragment;
	private StudentFragment fragmentStudent;
	private TeacherFragment fragmentTeacher;
	private OfflineFragment offlineFragment;

	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		getActionBar().setDisplayShowHomeEnabled(false);

		initView();
		initDatas();
		mViewPager.setAdapter(mAdapter);
		initEvent();

	}

	/**
	 * 初始化所有事件
	 */
	private void initEvent() {

		mViewPager.setOnPageChangeListener(this);

	}

	private void initDatas() {
		homeFragment = new HomeFragment();
		mTabs.add(homeFragment);
		questionFragment = new QuestionFragment();
		mTabs.add(questionFragment);
		discoverFragment = new DiscoverFragment();
		mTabs.add(discoverFragment);
		state = (String) SPUtil.get(this, "LOGIN", "NONE");
		if(state.equals("STUDENT")){
			fragmentStudent = new StudentFragment();
			mTabs.add(fragmentStudent);
		}else if (state.equals("TEACHER")) {
			fragmentTeacher = new TeacherFragment();
			mTabs.add(fragmentTeacher);
		}else {
			offlineFragment = new OfflineFragment();
			mTabs.add(offlineFragment);
		}

		mAdapter = new FragmentVPAdapter(getSupportFragmentManager(),mTabs);
		
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mViewPager.setOffscreenPageLimit(4);

		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);

		one.setIconAlpha(1.0f);

	}

	@Override
	public void onClick(View v) {
		clickTab(v);

	}

	/**
	 * 点击Tab按钮
	 * 
	 * @param v
	 */
	private void clickTab(View v) {
		resetOtherTabs();

		switch (v.getId()) {
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		}
	}

	/**
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicators.size(); i++) {
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// Log.e("TAG", "position = " + position + " ,positionOffset =  "
		// + positionOffset);
		if (positionOffset > 0) {
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}
	
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 实现按两下返回键退出功能
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				moveTaskToBack(false);
				finish();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		Log.v(TAG, "onResume");
		updateLoginState();
		super.onResume();
	}
	
	private void updateLoginState(){
		String tmpstate = (String) SPUtil.get(this, "LOGIN", "NONE");
		if(tmpstate.equals(state)){
			return ;
		}
		Log.v(TAG, "tmpstate:"+tmpstate);
		mTabs.clear();
		homeFragment = new HomeFragment();
		mTabs.add(homeFragment);
		questionFragment = new QuestionFragment();
		mTabs.add(questionFragment);
		discoverFragment = new DiscoverFragment();
		mTabs.add(discoverFragment);
		state = (String) SPUtil.get(this, "LOGIN", "NONE");
		if(state.equals("STUDENT")){
			fragmentStudent = new StudentFragment();
			mTabs.add(fragmentStudent);
		}else if (state.equals("TEACHER")) {
			fragmentTeacher = new TeacherFragment();
			mTabs.add(fragmentTeacher);
		}else {
			offlineFragment = new OfflineFragment();
			mTabs.add(offlineFragment);
		}
		mAdapter.notifyDataSetChanged();
	}
}
