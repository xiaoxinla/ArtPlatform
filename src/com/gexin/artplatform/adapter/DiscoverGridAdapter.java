package com.gexin.artplatform.adapter;

import java.util.List;
import java.util.Map;

import com.gexin.artplatform.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DiscoverGridAdapter extends BaseAdapter {

	private List<Map<String, Object>> mList;
	private Context mContext;

	public DiscoverGridAdapter(Context context, List<Map<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Map<String, Object> map = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.discover_item, null);
			holder = new ViewHolder();
			holder.ivIcon = (ImageView) convertView
					.findViewById(R.id.iv_discover_item);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_discover_item);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		String title = (String) map.get("title");
		String url = (String) map.get("icon");
		holder.tvTitle.setText(title);
		ImageLoader.getInstance().displayImage(url, holder.ivIcon);
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivIcon;
		TextView tvTitle;
	}
}
