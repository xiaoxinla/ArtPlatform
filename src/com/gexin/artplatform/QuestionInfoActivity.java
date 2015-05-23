package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.utils.TimeUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class QuestionInfoActivity extends Activity {

	private static final String TAG = "QuestionInfoActivity";

	private ImageView ivHeader;
	private ImageView ivPic;
	private TextView tvName;
	private TextView tvTime;
	private TextView tvCommentor;
	private TextView tvContent;
	private TextView tvType;
	private TextView tvAnsNum;
	private TextView tvZan;
	private TitleBar titleBar;
	private LinearLayout llBack;
	private EditText etComment;
	private Button btnSubmit;

	private Problem problem;
	private Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_info);

		initView();

		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				Log.v(TAG, "jObject:" + jObject.toString());
				setDataToView(jObject);
			}
		};
		String problemId = getIntent().getStringExtra("problemId");
		new HttpConnectionUtils(handler).get(Constant.SERVER_URL
				+ Constant.PROBLEM_API + "/" + problemId);
	}

	private void initView() {
		ivHeader = (ImageView) findViewById(R.id.iv_header_question_info);
		ivPic = (ImageView) findViewById(R.id.iv_pic_question_info);
		tvContent = (TextView) findViewById(R.id.tv_content_question_info);
		tvCommentor = (TextView) findViewById(R.id.tv_commentor_question_info);
		tvAnsNum = (TextView) findViewById(R.id.tv_ans_question_info);
		tvName = (TextView) findViewById(R.id.tv_name_question_info);
		tvTime = (TextView) findViewById(R.id.tv_time_question_info);
		tvType = (TextView) findViewById(R.id.tv_type_question_info);
		tvZan = (TextView) findViewById(R.id.tv_zan_question_info);
		titleBar = (TitleBar) findViewById(R.id.tb_question_info);
		etComment = (EditText) findViewById(R.id.et_comment_question_info);
		btnSubmit = (Button) findViewById(R.id.btn_comment_question_info);
		initTitleBar();
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = etComment.getText().toString();
				String replyTo = null;
				if (!content.isEmpty()) {
					postComment(content, replyTo);
				}
			}
		});

	}

	protected void postComment(String content, String replyTo) {
		String userId = (String) SPUtil.get(this, "userId", "");
		if (userId.isEmpty()) {
			Toast.makeText(this, "请先登录再评论", Toast.LENGTH_SHORT).show();
			return;
		}
		final String commentAPI = Constant.SERVER_URL + "/api/user/" + userId
				+ "/comment";
		Handler handler = new HttpHandler(this) {

			@Override
			protected void succeed(JSONObject jObject) {
				Log.v(TAG, "succeed:" + jObject.toString());
				commentSucceed(jObject);
			}

		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (replyTo != null) {
			list.add(new BasicNameValuePair("replyTo", replyTo));
		}
		list.add(new BasicNameValuePair("content", content));
		list.add(new BasicNameValuePair("problemId", problem.get_id()));
		new HttpConnectionUtils(handler).post(commentAPI, list);
	}

	private void commentSucceed(JSONObject jObject) {
		int state = -1;
		try {
			state = jObject.getInt("stat");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (state == 1) {
			Toast.makeText(this, "评论成功", Toast.LENGTH_SHORT).show();
			etComment.setText("");
		} else {
			Toast.makeText(this, "评论失败", Toast.LENGTH_SHORT).show();
		}
	}

	private void initTitleBar() {
		llBack = new LinearLayout(this);
		ImageView ivBack = new ImageView(this);
		ivBack.setImageResource(R.drawable.back_icon);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		llBack.addView(ivBack, params);
		llBack.setGravity(Gravity.CENTER_VERTICAL);
		llBack.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llBack.setPadding(20, 0, 20, 0);
		titleBar.setLeftView(llBack);

		llBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "BackClick");
				finish();
			}
		});
	}

	private void setDataToView(JSONObject jObject) {
		int state;
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				JSONObject jsonObject = jObject.getJSONObject("problem");
				problem = gson.fromJson(jsonObject.toString(), Problem.class);
				Log.v(TAG, "problem:" + problem.toString());
				int ansNum = problem.getAnswerNum();
				int zan = problem.getZan();
				String name = problem.getName();
				String avatarUrl = problem.getAvatarUrl();
				String time = TimeUtil.getStandardDate(problem.getTimestamp());
				String tag = "";
				if (problem.getTag() != null && problem.getTag().size() != 0) {
					String tmpStr = problem.getTag().toString();
					try {
						tag = tmpStr.substring(1, tmpStr.length() - 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					tag = "未设置";
				}
				String content = problem.getContent();
				final String imageUrl = problem.getImage();
				String commentor = "XXX画室";
				tvContent.setText(content);
				tvTime.setText(time);
				tvName.setText(name);
				tvType.setText(tag);
				tvAnsNum.setText(ansNum + "");
				tvZan.setText(zan + "");
				tvCommentor.setText(commentor);
				DisplayImageOptions headerOptions = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_contact_picture)
						.showImageForEmptyUri(R.drawable.ic_contact_picture)
						.showImageOnFail(R.drawable.ic_error)
						.cacheInMemory(true).cacheOnDisk(true)
						.considerExifParams(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				DisplayImageOptions picOptions = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_empty)
						.showImageOnFail(R.drawable.ic_error)
						.cacheInMemory(true).cacheOnDisk(true)
						.considerExifParams(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				ImageLoader.getInstance().displayImage(avatarUrl, ivHeader,
						headerOptions);
				if (imageUrl != null && !imageUrl.isEmpty()) {
					ivPic.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(imageUrl, ivPic,
							picOptions);
				} else {
					ivPic.setVisibility(View.GONE);
				}
				ivPic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (imageUrl != null && !imageUrl.isEmpty()) {
							Intent intent = new Intent(
									QuestionInfoActivity.this,
									LargeImageActivity.class);
							intent.putExtra("url", imageUrl);
							startActivity(intent);
						}
					}
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
