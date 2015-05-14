package com.gexin.artplatform.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.utils.TimeUtil;

public class QuestionAdapter extends BaseAdapter {

	private static final String TAG = "QuestionAdapter";
	private Context mContext;
	private List<Problem> mList;

	public QuestionAdapter(Context mContext, List<Problem> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.v(TAG, "getView");
		ViewHolder holder = null;
		Problem item = mList.get(position);
		// Log.v(TAG, "item:" + item.toString());
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.question_item, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_header_question_item);
			// holder.ivComment = (ImageView) convertView
			// .findViewById(R.id.iv_iscomment_question_item);
			holder.ivPic = (ImageView) convertView
					.findViewById(R.id.iv_pic_question_item);
			// holder.tvArea = (TextView) convertView
			// .findViewById(R.id.tv_area_question_item);
			 holder.tvCommentor = (TextView) convertView
			 .findViewById(R.id.tv_commentor_question_item);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content_question_item);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name_question_item);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time_question_item);
			holder.tvType = (TextView) convertView
					.findViewById(R.id.tv_type_question_item);
			// holder.tvIscomment = (TextView) convertView
			// .findViewById(R.id.tv_iscomment_question_item);
			holder.tvZan = (TextView) convertView
					.findViewById(R.id.tv_zan_question_item);
			holder.tvAnsNum = (TextView) convertView
					.findViewById(R.id.tv_ansnum_question_item);
			holder.llZan = (LinearLayout) convertView
					.findViewById(R.id.ll_zan_question_item);
			holder.llAns = (LinearLayout) convertView
					.findViewById(R.id.ll_ans_question_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int header = R.drawable.ic_contact_picture;
		Bitmap bitmap = null;
		int ansNum = item.getAnswerNum();
		int zan = item.getZan();
		String name = item.getName();
		String time = TimeUtil.getStandardDate(item.getTimestamp());
		String type = item.getTag();
		String content = item.getContent();
		String commentor = "";
		holder.ivHeader.setImageResource(header);
		holder.ivPic.setImageBitmap(bitmap);
		holder.tvName.setText(name);
		holder.tvTime.setText(time);
		holder.tvType.setText(type);
		holder.tvContent.setText(content);
		// holder.tvCommentor.setText(commentor);
		holder.tvZan.setText("" + zan);
		holder.tvAnsNum.setText("" + ansNum);
		 if (commentor == null || commentor.isEmpty()) {
			 holder.tvCommentor.setVisibility(View.GONE);
		 } else {
			 holder.tvCommentor.setVisibility(View.VISIBLE);
			 holder.tvCommentor.setText(commentor);
		 }
		holder.llZan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "ZanClick");
			}
		});
		holder.llAns.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "AnsClick");
			}
		});

		return convertView;
	}

	private final class ViewHolder {
		ImageView ivHeader;
		// ImageView ivComment;
		ImageView ivPic;
		TextView tvName;
		TextView tvCommentor;
		TextView tvTime;
		// TextView tvArea;
		TextView tvContent;
		TextView tvType;
		// TextView tvIscomment;
		TextView tvZan;
		TextView tvAnsNum;
		LinearLayout llZan;
		LinearLayout llAns;
	}

}
