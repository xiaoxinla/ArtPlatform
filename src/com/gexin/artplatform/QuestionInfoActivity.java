package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.bean.Answer;
import com.gexin.artplatform.bean.AnswerContent;
import com.gexin.artplatform.bean.Comment;
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
	private String userId = "";
	private String replyTo = "";

	private ImageView ivHeader;
	private ImageView ivPic;
	private ImageView ivZan;
	private TextView tvName;
	private TextView tvTime;
	// private TextView tvCommentor;
	private TextView tvContent;
	private TextView tvType;
	private TextView tvAnsNum;
	private TextView tvZan;
	private TitleBar titleBar;
	private LinearLayout llBack;
	private EditText etComment;
	private LinearLayout llComment;
	private LinearLayout llAnswer;
	private LinearLayout llZan;
	private Button btnSubmit;
	private ImageButton ibtnFocus;
	private TextView tvAnswerNum;
	private TextView tvCommentNum;
	private View answerLine, commentLine;

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
		userId = (String) SPUtil.get(this, "userId", "");
		String problemId = getIntent().getStringExtra("problemId");
		String api = Constant.SERVER_URL + Constant.PROBLEM_API + "/"
				+ problemId;
		if (!userId.isEmpty()) {
			api += "?userId=" + userId;
		}
		new HttpConnectionUtils(handler).get(api);
	}

	private void initView() {
		ivHeader = (ImageView) findViewById(R.id.iv_header_question_info);
		ivPic = (ImageView) findViewById(R.id.iv_pic_question_info);
		ivZan = (ImageView) findViewById(R.id.iv_zan_question_info);
		tvContent = (TextView) findViewById(R.id.tv_content_question_info);
		// tvCommentor = (TextView)
		// findViewById(R.id.tv_commentor_question_info);
		tvAnsNum = (TextView) findViewById(R.id.tv_ans_question_info);
		tvName = (TextView) findViewById(R.id.tv_name_question_info);
		tvTime = (TextView) findViewById(R.id.tv_time_question_info);
		tvType = (TextView) findViewById(R.id.tv_type_question_info);
		tvZan = (TextView) findViewById(R.id.tv_zan_question_info);
		titleBar = (TitleBar) findViewById(R.id.tb_question_info);
		etComment = (EditText) findViewById(R.id.et_comment_question_info);
		btnSubmit = (Button) findViewById(R.id.btn_comment_question_info);
		ibtnFocus = (ImageButton) findViewById(R.id.ibtn_interest_question_info);
		llComment = (LinearLayout) findViewById(R.id.ll_area_comment_question_info);
		llAnswer = (LinearLayout) findViewById(R.id.ll_area_answer_question_info);
		llZan = (LinearLayout) findViewById(R.id.ll_zan_question_info);
		tvAnswerNum = (TextView) findViewById(R.id.tv_answernum_question_info);
		tvCommentNum = (TextView) findViewById(R.id.tv_commentnum_question_info);
		answerLine = findViewById(R.id.v_answer_line_activity_question_info);
		commentLine = findViewById(R.id.v_comment_line_activity_question_info);
		initTitleBar();
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = etComment.getText().toString();
				if (!content.isEmpty()) {
					postComment(content);
				}
			}
		});

		ibtnFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postFocus(problem.getUserId());
			}
		});
		String status = (String) SPUtil.get(this, "LOGIN", "NONE");
		if (status.equals("TEACHER")) {
			btnSubmit.setText("�ش�");
		}
	}

	protected void postComment(String content) {
		String status = (String) SPUtil.get(this, "LOGIN", "NONE");
		if (userId.isEmpty()) {
			Toast.makeText(this, "���ȵ�¼������", Toast.LENGTH_SHORT).show();
			return;
		}
		final String commentAPI = Constant.SERVER_URL + "/api/user/" + userId
				+ "/comment";
		final String answerAPI = Constant.SERVER_URL + "/api/user/" + userId
				+ "/answer";
		Handler handler = new HttpHandler(this) {

			@Override
			protected void succeed(JSONObject jObject) {
				Log.v(TAG, "succeed:" + jObject.toString());
				commentSucceed(jObject);
			}

		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (replyTo != null && !replyTo.isEmpty()) {
			list.add(new BasicNameValuePair("replyTo", replyTo));
		}
		list.add(new BasicNameValuePair("content", content));
		list.add(new BasicNameValuePair("problemId", problem.get_id()));
		if (status.equals("STUDENT")) {
			new HttpConnectionUtils(handler).post(commentAPI, list);
		} else {
			new HttpConnectionUtils(handler).post(answerAPI, list);
		}
	}

	private void commentSucceed(JSONObject jObject) {
		int state = -1;
		try {
			state = jObject.getInt("stat");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (state == 1) {
			Toast.makeText(this, "�����ɹ�", Toast.LENGTH_SHORT).show();
			etComment.setText("");
		} else {
			Toast.makeText(this, "����ʧ��", Toast.LENGTH_SHORT).show();
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
				// userId = problem.getUserId();
				List<Comment> commentList = problem.getCommentList();
				List<Answer> answerList = problem.getAnswerList();
				if (problem.getTag() != null && problem.getTag().size() != 0) {
					String tmpStr = problem.getTag().toString();
					try {
						tag = tmpStr.substring(1, tmpStr.length() - 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					tag = "δ����";
				}
				String content = problem.getContent();
				final String imageUrl = problem.getImage();
				// String commentor = "XXX����";
				tvContent.setText(content);
				tvTime.setText(time);
				tvName.setText(name);
				tvType.setText(tag);
				tvAnsNum.setText(ansNum + "");
				tvZan.setText(zan + "");
				// tvCommentor.setText(commentor);
				if (problem.getIsZan() == 1) {
					ivZan.setImageResource(R.drawable.zan_icon_2);
				} else {
					ivZan.setImageResource(R.drawable.zan_icon_1);
				}
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
				llZan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						postZan();
					}
				});

				ivPic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (imageUrl != null && !imageUrl.isEmpty()) {
							Intent intent = new Intent(
									QuestionInfoActivity.this,
									LargeImageActivity.class);
							List<String> images = new ArrayList<String>();
							images.add(imageUrl);
							intent.putStringArrayListExtra("images",
									(ArrayList<String>) images);
							startActivity(intent);
						}
					}
				});

				if (commentList != null && !commentList.isEmpty()) {
					setComment(commentList);
					tvCommentNum.setVisibility(View.VISIBLE);
					tvCommentNum.setText("������(" + commentList.size() + ")");
				} else {
					tvCommentNum.setVisibility(View.GONE);
					commentLine.setVisibility(View.GONE);
				}
				if (answerList != null && !answerList.isEmpty()) {
					setAnswer(answerList);
					tvAnswerNum.setVisibility(View.VISIBLE);
					tvAnswerNum.setText("�ش�(" + answerList.size() + ")");
				} else {
					tvAnswerNum.setVisibility(View.GONE);
					answerLine.setVisibility(View.GONE);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private void postZan() {
		String zanAPI = Constant.SERVER_URL + "/api/problem/"
				+ problem.get_id() + "/zan";
		if (userId.isEmpty()) {
			Toast.makeText(this, "���ȵ�¼", Toast.LENGTH_SHORT).show();
			return;
		}
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							if (problem.getIsZan() == 1) {
								problem.setIsZan(0);
								ivZan.setImageResource(R.drawable.zan_icon_1);
								problem.setZan(problem.getZan() - 1);
								tvZan.setText(problem.getZan() + "");
								Toast.makeText(QuestionInfoActivity.this,
										"ȡ���޳ɹ�", Toast.LENGTH_SHORT).show();
							} else {
								problem.setIsZan(1);
								ivZan.setImageResource(R.drawable.zan_icon_2);
								problem.setZan(problem.getZan() + 1);
								tvZan.setText(problem.getZan() + "");
								Toast.makeText(QuestionInfoActivity.this,
										"�޳ɹ�", Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", userId));
		if (problem.getIsZan() == 1) {
			list.add(new BasicNameValuePair("zan", "-1"));
		} else {
			list.add(new BasicNameValuePair("zan", "1"));
		}
		new HttpConnectionUtils(handler).put(zanAPI, list);
	}

	@SuppressLint("InflateParams")
	private void setAnswer(List<Answer> answerList) {
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		for (final Answer answer : answerList) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.problem_answer_item, null);
			ImageView tmpIvHeader = (ImageView) view
					.findViewById(R.id.iv_header_answer_item);
			TextView tmpTvName = (TextView) view
					.findViewById(R.id.tv_name_answer_item);
			TextView tmpTvContent = (TextView) view
					.findViewById(R.id.tv_content_answer_item);
			TextView tmpTvTime = (TextView) view
					.findViewById(R.id.tv_time_answer_item);
			LinearLayout tmpLlAsk = (LinearLayout) view
					.findViewById(R.id.ll_ask_answer_item);
			ImageView tmpIvFocus = (ImageView) view
					.findViewById(R.id.iv_interest_answer_item);
			tmpTvTime.setText(TimeUtil.getStandardDate(answer.getUpdateTime()));
			tmpTvName.setSingleLine(true);
			tmpTvName.setTextColor(Color.parseColor("#445bc8"));
			String tmpContent = "";
			for (AnswerContent content : answer.getContent()) {
				tmpContent += content.getContent();
			}
			tmpTvContent.setText(tmpContent);
			tmpTvName.setText(answer.getUserName());
			ImageLoader.getInstance().displayImage(answer.getAvatarUrl(),
					tmpIvHeader, imageOptions);
			llAnswer.addView(view);
			tmpLlAsk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(QuestionInfoActivity.this,
							PostProblemActivity.class);
					intent.putExtra("teacherId", answer.getUserId());
					startActivity(intent);
				}
			});
			tmpIvFocus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					postFocus(answer.getUserId());
				}
			});
		}
	}

	@SuppressLint("HandlerLeak")
	protected void postFocus(String id) {
		String followApi = Constant.SERVER_URL + "/api/user/" + userId
				+ "/follow";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject(
								(String) msg.obj);
						if (jsonObject.getInt("stat") == 1) {
							Toast.makeText(QuestionInfoActivity.this,
									"��ע�ɹ�", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(QuestionInfoActivity.this,
									"��עʧ��", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(QuestionInfoActivity.this,
								"��עʧ��", Toast.LENGTH_SHORT).show();
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", id));
		list.add(new BasicNameValuePair("follow", 1 + ""));
		new HttpConnectionUtils(handler).post(followApi, list);
	}

	@SuppressLint("InflateParams")
	private void setComment(List<Comment> list) {
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		for (final Comment comment : list) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.comment_item, null);
			ImageView tmpIvHeader = (ImageView) view
					.findViewById(R.id.iv_header_comment_item);
			TextView tmpTvName = (TextView) view
					.findViewById(R.id.tv_name_comment_item);
			TextView tmpTvTime = (TextView) view
					.findViewById(R.id.tv_time_comment_item);
			TextView tmpTvContent = (TextView) view
					.findViewById(R.id.tv_content_comment_item);

			tmpTvContent.setText(comment.getContent());
			tmpTvName.setSingleLine(true);
			tmpTvName.setTextColor(Color.parseColor("#445bc8"));
			tmpTvName.setText(comment.getFromUserName());
			tmpTvTime.setText(TimeUtil.getStandardDate(comment.getTimestamp()));
			ImageLoader.getInstance().displayImage(
					comment.getFromUserAvatarUrl(), tmpIvHeader, imageOptions);
			llComment.addView(view);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String hint = comment.getFromUserName();
					if (hint.length() > 10) {
						hint = hint.substring(0, 10) + "...";
					}
					etComment.setHint("�ظ� " + hint);
					replyTo = comment.getFromUser();
					Log.v(TAG, "replyTo:" + replyTo);
				}
			});
		}
	}
}
