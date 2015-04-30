package com.gexin.artplatform.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.gexin.artplatform.LoginActivity;
import com.gexin.artplatform.R;
import com.gexin.artplatform.RegisterActivity;
import com.gexin.artplatform.student.StudentChase;
import com.gexin.artplatform.student.StudentComment;
import com.gexin.artplatform.student.StudentFans;
import com.gexin.artplatform.student.StudentFavorite;
import com.gexin.artplatform.student.StudentFocus;
import com.gexin.artplatform.student.StudentFriend;
import com.gexin.artplatform.student.StudentGallery;
import com.gexin.artplatform.student.StudentSubscribe;

public class FragmentStudent extends Fragment {
	private Button exit, btnReg;
	private View student_chase, student_comment, student_fans,
			student_favorite, student_focus, student_friend, student_subscribe,
			student_gallery;
	public static final String LOGIN = "Login";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.student_me, container, false);
		exit = (Button) view.findViewById(R.id.exit_login);
		btnReg = (Button) view.findViewById(R.id.btn_reg_me);
		student_chase = view.findViewById(R.id.student_chase);
		student_comment = view.findViewById(R.id.student_comment);
		student_fans = view.findViewById(R.id.student_fans);
		student_favorite = view.findViewById(R.id.student_favorite);
		student_focus = view.findViewById(R.id.student_focus);
		student_friend = view.findViewById(R.id.student_friend);
		student_subscribe = view.findViewById(R.id.student_subscribe);
		student_gallery = view.findViewById(R.id.student_gallery);

		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sp = getActivity().getSharedPreferences(
						LOGIN, Activity.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString("login", "off");
				editor.commit();
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				getActivity().finish();
				startActivity(intent);
			}
		});
		student_chase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), StudentChase.class);
				startActivity(intent);
			}
		});
		student_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), StudentComment.class);
				startActivity(intent);
			}
		});
		student_fans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), StudentFans.class);
				startActivity(intent);
			}
		});
		student_favorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), StudentFavorite.class);
				startActivity(intent);
			}
		});
		student_focus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), StudentFocus.class);
				startActivity(intent);
			}
		});
		student_friend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), StudentFriend.class);
				startActivity(intent);
			}
		});
		student_subscribe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						StudentSubscribe.class);
				startActivity(intent);
			}
		});
		student_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), StudentGallery.class);
				startActivity(intent);
			}
		});
		btnReg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), RegisterActivity.class));
			}
		});
		return view;
	}

}
