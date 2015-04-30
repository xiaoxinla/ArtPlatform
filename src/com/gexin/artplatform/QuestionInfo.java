package com.gexin.artplatform;

import org.json.JSONException;
import org.json.JSONObject;

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.entity.Problem;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.StringUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionInfo extends Activity {

	private ImageView ivHeader;
	private ImageView ivPic;
	private ImageView ivPraise;
	private TextView tvName;
	private TextView tvTime;
	private TextView tvArea;
	private TextView tvContent;
	private TextView tvType;
	private TextView tvCount;
	private TextView tvZan;
	private TextView tvViewNum;

	private Problem problem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_info);

		initView();

		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				super.succeed(jObject);
				tvContent.setText(jObject.toString());
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
		ivPraise = (ImageView) findViewById(R.id.iv_praise_question_info);
		tvContent = (TextView) findViewById(R.id.tv_content_question_info);
		tvArea = (TextView) findViewById(R.id.tv_area_question_info);
		tvCount = (TextView) findViewById(R.id.tv_count_question_info);
		tvName = (TextView) findViewById(R.id.tv_name_question_info);
		tvTime = (TextView) findViewById(R.id.tv_time_question_info);
		tvType = (TextView) findViewById(R.id.tv_type_question_info);
		tvViewNum = (TextView) findViewById(R.id.tv_viewnum_question_info);
		tvZan = (TextView) findViewById(R.id.tv_zan_question_info);
	}

	private void setDataToView(JSONObject jObject) {
		int state;
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				JSONObject jsonObject = jObject.getJSONObject("problem");
				problem = Problem.analyzeJson(jsonObject);
				int count = problem.getAnswerNum();
				int zan = problem.getZan();
				int viewNum = problem.getViewNum();
				String name = problem.getName();
				String time = StringUtil.convertTimestampToString(problem
						.getTimestamp());
				String content = problem.getContent();
				Bitmap bitmap = null;
				tvContent.setText(content);
				tvTime.setText(time);
				tvName.setText(name);
				tvCount.setText(count + "");
				tvViewNum.setText(viewNum + "");
				tvZan.setText(zan + "");
				ivPic.setImageBitmap(bitmap);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
