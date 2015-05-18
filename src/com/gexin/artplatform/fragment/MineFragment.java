package com.gexin.artplatform.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.LoginActivity;
import com.gexin.artplatform.R;
import com.gexin.artplatform.imagecache.utils.ImageCache;
import com.gexin.artplatform.student.StudentChase;
import com.gexin.artplatform.student.StudentComment;
import com.gexin.artplatform.student.StudentFans;
import com.gexin.artplatform.student.StudentFavorite;
import com.gexin.artplatform.student.StudentFocus;
import com.gexin.artplatform.student.StudentGallery;
import com.gexin.artplatform.student.StudentSubscribe;
import com.gexin.artplatform.utils.SPUtil;

public class MineFragment extends Fragment {

	private static final int LOGIN_REQUEST = 0;
	private Button btnExit;
	private LinearLayout llFocus, llFans, llCollect;
	private RelativeLayout rlWork, rlPump, rlComment, rlSubscribe;
	private TextView tvFocus, tvFans, tvCollect, tvWork, tvPump, tvComment,
			tvSubscribe, tvName;
	private ImageView ivHeader;

	private int job = -1;// -1为未登录，0为学生，1为教师
	private ImageCache imageCache;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		initView(view);

		return view;
	}

	private void initView(View view) {
		llFocus = (LinearLayout) view.findViewById(R.id.ll_focus_fragment_mine);
		llFans = (LinearLayout) view.findViewById(R.id.ll_fans_fragment_mine);
		llCollect = (LinearLayout) view
				.findViewById(R.id.ll_collect_fragment_mine);
		rlWork = (RelativeLayout) view.findViewById(R.id.rl_work_fragment_mine);
		rlPump = (RelativeLayout) view.findViewById(R.id.rl_pump_fragment_mine);
		rlComment = (RelativeLayout) view
				.findViewById(R.id.rl_comment_fragment_mine);
		rlSubscribe = (RelativeLayout) view
				.findViewById(R.id.rl_subscribe_fragment_mine);
		tvFocus = (TextView) view.findViewById(R.id.tv_focusnum_fragment_mine);
		tvFans = (TextView) view.findViewById(R.id.tv_fansnum_fragment_mine);
		tvCollect = (TextView) view
				.findViewById(R.id.tv_collectnum_fragment_mine);
		tvWork = (TextView) view.findViewById(R.id.tv_work_fragment_mine);
		tvPump = (TextView) view.findViewById(R.id.tv_pump_fragment_mine);
		tvComment = (TextView) view.findViewById(R.id.tv_comment_fragment_mine);
		tvSubscribe = (TextView) view
				.findViewById(R.id.tv_subscribe_fragment_mine);
		tvName = (TextView) view.findViewById(R.id.tv_name_fragment_mine);
		btnExit = (Button) view.findViewById(R.id.btn_exit_fragment_mine);
		ivHeader = (ImageView) view.findViewById(R.id.iv_header_fragment_mine);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case LOGIN_REQUEST:
			if (resultCode == Activity.RESULT_CANCELED) {
				job = -1;
				SPUtil.put(getActivity(), "LOGIN", "NONE");
				btnExit.setText("登录");
			} else if (resultCode == Activity.RESULT_OK) {
				String state = (String) SPUtil.get(getActivity(), "LOGIN", "NONE");
				if(state.equals("STUDENT")){
					job=0;
				}else if(state.equals("TEACHER")){
					job=1;
				}
				btnExit.setText("退出登录");
				setDataToView();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setDataToView() {
		String name = (String) SPUtil.get(getActivity(), "name", "未设置");
		String avatarUrl = (String) SPUtil.get(getActivity(), "avatarUrl", "");
		int followNum = (Integer) SPUtil.get(getActivity(), "followNum", 0);
		int fanNum = (Integer) SPUtil.get(getActivity(), "fanNum", 0);
		int collectionNum = (Integer) SPUtil.get(getActivity(),
				"collectionNum", 0);
		int workNum = (Integer) SPUtil.get(getActivity(), "workNum", 0);
		int askNum = (Integer) SPUtil.get(getActivity(), "askNum", 0);
		int commentNum = (Integer) SPUtil.get(getActivity(), "commentNum", 0);
		int subscriptionNum = (Integer) SPUtil.get(getActivity(),
				"subscriptionNum", 0);
		if(name.isEmpty()){
			name = "未设置";
		}
		if(job==-1){
			name = "未登录";
		}
		tvName.setText(name);
		tvFocus.setText(""+followNum);
		tvFans.setText(""+fanNum);
		tvCollect.setText(""+collectionNum);
		if(workNum!=0&&job!=-1){
			tvWork.setText("我的作品("+workNum+")");
		}
		if(askNum!=0&&job!=-1){
			tvPump.setText("我的追问("+askNum+")");
		}
		if(commentNum!=0&&job!=-1){
			tvComment.setText("我的评论("+commentNum+")");
		}
		if(subscriptionNum!=0&&job!=-1){
			tvSubscribe.setText("我的订阅("+subscriptionNum+")");
		}
		if(job!=-1){
			imageCache.displayImage(ivHeader, avatarUrl, R.drawable.ic_contact_picture);
		}
	}

	private void initData() {
		imageCache = ImageCache.getInstance(getActivity());
		String state = (String) SPUtil.get(getActivity(), "LOGIN", "NONE");
		if (state.equals("STUDENT")) {
			job = 0;
		} else if (state.equals("TEACHER")) {
			job = 1;
		}
		if (job == -1) {
			btnExit.setText("登录");
		} else {
			btnExit.setText("退出登录");
		}
		setDataToView();
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SPUtil.put(getActivity(), "LOGIN", "NONE");
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivityForResult(intent, LOGIN_REQUEST);
				SPUtil.clear(getActivity());
			}
		});
		rlPump.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							StudentChase.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		rlComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							StudentComment.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		llFans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(), StudentFans.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		llCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							StudentFavorite.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		llFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							StudentFocus.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		rlSubscribe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							StudentSubscribe.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		rlWork.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							StudentGallery.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}
}
