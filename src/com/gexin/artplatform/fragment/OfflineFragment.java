package com.gexin.artplatform.fragment;

import com.gexin.artplatform.LoginActivity;
import com.gexin.artplatform.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class OfflineFragment extends Fragment {
	private LinearLayout lay;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.off_line, container,
				false);
		lay = (LinearLayout) view.findViewById(R.id.offline_layout);
		lay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}
}
