package com.gexin.artplatform.adapter;


import java.util.List;
import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Fans;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFansListAdapter extends BaseAdapter {

	private Context context;
	private List<Fans> mList;
	private DisplayImageOptions avatarOptions;
	private String[] userType = {"学生","老师","画室"};

	public MyFansListAdapter(Context context, List<Fans> mList) {
		this.context = context;
		this.mList = mList;
		avatarOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Fans fans = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fans_list_item, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_fans_avatar);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_fans_name);
			holder.tvInfo = (TextView) convertView
					.findViewById(R.id.tv_fans_info);
			holder.tvFollowTA = (TextView) convertView
					.findViewById(R.id.tv_fans_operation);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(fans.getName());
		holder.tvInfo.setText(userType[fans.getuType()]);
		holder.tvFollowTA.setText("关注TA");
		ImageLoader.getInstance().displayImage(fans.getAvatarUrl(),
				holder.ivHeader, avatarOptions);
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivHeader;
		TextView tvName;
		TextView tvInfo;
		TextView tvFollowTA;
	}
}
