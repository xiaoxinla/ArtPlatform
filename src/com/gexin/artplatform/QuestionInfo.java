package com.gexin.artplatform;

import org.json.JSONObject;

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionInfo extends Activity {

	private ImageView ivHeader;
	private ImageView ivComment;
	private ImageView ivPic;
	private ImageView ivPraise;
	private TextView tvName;
	private TextView tvCommentor;
	private TextView tvTime;
	private TextView tvArea;
	private TextView tvContent;
	private TextView tvType;
	private TextView tvCount;
	private TextView tvIscomment;
	private TextView tvZan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_info);

		tvContent = (TextView) findViewById(R.id.tv_content_question_info);
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				super.succeed(jObject);
				tvContent.setText(jObject.toString());
			}
		};
		String problemId = getIntent().getStringExtra("problemId");
		new HttpConnectionUtils(handler).get(Constant.SERVER_URL
				+ Constant.PROBLEM_API + "/" + problemId);
	}
}
