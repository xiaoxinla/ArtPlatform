package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.gexin.artplatform.fragment.FragmentStudent;
import com.gexin.artplatform.fragment.QuestionFragment;
import com.gexin.artplatform.fragment.TabFragment;
import com.gexin.artplatform.view.ChangeColorIconWithText;

public class MainActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {

	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private String[] mTitles = new String[] { "主页", "问答", "图库", "院校", "个人" };
	private FragmentPagerAdapter mAdapter;
	private QuestionFragment questionFragment;
	FragmentStudent fragment_student_me = new FragmentStudent();

	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayShowHomeEnabled(false);

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
		for (String title : mTitles) {
			if (title.equals("问答")) {
				questionFragment = new QuestionFragment();
				mTabs.add(questionFragment);
				continue;
			}
			if (title == "个人") {
				mTabs.add(fragment_student_me);
				continue;
			}
			TabFragment tabFragment = new TabFragment();
			Bundle bundle = new Bundle();
			bundle.putString(TabFragment.TITLE, title);
			tabFragment.setArguments(bundle);
			mTabs.add(tabFragment);
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mTabs.get(position);
			}
		};
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);
		ChangeColorIconWithText five = (ChangeColorIconWithText) findViewById(R.id.id_indicator_five);
		mTabIndicators.add(five);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);

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
		case R.id.id_indicator_five:
			mTabIndicators.get(4).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(4, false);
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
}
