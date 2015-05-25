package com.gexin.artplatform.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.RoomDetailActivity;

public class HomeListAdapter extends BaseAdapter {

	private List<Map<String, Object>> mList;
	private Context mContext;

	public HomeListAdapter(List<Map<String, Object>> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Map<String, Object> map = mList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.home_list_item, null);
			holder.tvClickNum = (TextView) convertView
					.findViewById(R.id.tv_clicknum_home_item);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content_home_item);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name_home_item);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvClickNum.setText("µã»÷"+map.get("clickNum"));
		holder.tvContent.setText((String)map.get("content"));
		holder.tvName.setText((String)map.get("name"));
		holder.tvName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mContext.startActivity(new Intent(mContext,RoomDetailActivity.class));
			}
		});
		
		return convertView;
	}

	private static class ViewHolder {
		TextView tvName;
		TextView tvContent;
		TextView tvClickNum;
	}

}
