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
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.entity.Problem;
import com.gexin.artplatform.utils.StringUtil;

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
			holder.ivComment = (ImageView) convertView
					.findViewById(R.id.iv_iscomment_question_item);
			holder.ivPic = (ImageView) convertView
					.findViewById(R.id.iv_pic_question_item);
			holder.tvArea = (TextView) convertView
					.findViewById(R.id.tv_area_question_item);
			holder.tvCommentor = (TextView) convertView
					.findViewById(R.id.tv_latestcommentor_question_item);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content_question_item);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name_question_item);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time_question_item);
			holder.tvType = (TextView) convertView
					.findViewById(R.id.tv_type_question_item);
			holder.tvCount = (TextView) convertView
					.findViewById(R.id.tv_count_question_item);
			holder.tvIscomment = (TextView) convertView
					.findViewById(R.id.tv_iscomment_question_item);
			holder.tvZan = (TextView) convertView
					.findViewById(R.id.tv_zan_question_item);
			holder.ivPraise = (ImageView) convertView
					.findViewById(R.id.iv_praise_question_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int header = R.drawable.ic_contact_picture;
		Bitmap bitmap = null;
		int count = item.getAnswerNum();
		int zan = item.getZan();
		String name = item.getName();
		String area = "Œ¥…Ë÷√";
		String time = StringUtil.convertTimestampToString(item.getTimestamp());
		String type = "Œ¥…Ë÷√";
		String content = item.getContent();
		String commentor = "Œ¥…Ë÷√";
		holder.ivHeader.setImageResource(header);
		holder.ivPic.setImageBitmap(bitmap);
		holder.tvName.setText(name);
		holder.tvArea.setText(area);
		holder.tvTime.setText(time);
		holder.tvType.setText(type);
		holder.tvCount.setText("" + count);
		holder.tvContent.setText(content);
		holder.tvCommentor.setText(commentor);
		holder.tvZan.setText("" + zan);
		if (commentor == null || commentor.isEmpty()) {
			holder.ivComment.setVisibility(View.GONE);
			holder.tvIscomment.setVisibility(View.GONE);
		} else {
			holder.ivComment.setVisibility(View.VISIBLE);
			holder.tvIscomment.setVisibility(View.VISIBLE);
		}

		holder.ivPraise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "PraiseClick");
			}
		});

		return convertView;
	}

	private final class ViewHolder {
		ImageView ivHeader;
		ImageView ivComment;
		ImageView ivPic;
		ImageView ivPraise;
		TextView tvName;
		TextView tvCommentor;
		TextView tvTime;
		TextView tvArea;
		TextView tvContent;
		TextView tvType;
		TextView tvCount;
		TextView tvIscomment;
		TextView tvZan;
	}

}
