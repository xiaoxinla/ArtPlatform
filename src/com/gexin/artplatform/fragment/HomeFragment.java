package com.gexin.artplatform.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;

public class HomeFragment extends Fragment {

	private static final String TAG = "HomeFragment";
	private TitleBar titleBar;
	private LinearLayout llAddr;

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
	}

	private void initView(View view) {
		titleBar = (TitleBar) view.findViewById(R.id.tb_fragment_home);
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
				Log.v(TAG, "AddrClick");
			}
		});
	}
}
