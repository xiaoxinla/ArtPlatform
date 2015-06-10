package com.gexin.artplatform.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Follow;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyFollowListAdapter extends BaseAdapter {

	private Context context;
	private List<Follow> mList;
	private DisplayImageOptions avatarOptions;
	private String[] userType = { "ѧ��", "��ʦ", "����" };

	public MyFollowListAdapter(Context context, List<Follow> mList) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final Follow follow = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fans_list_item, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_fans_avatar);
			holder.tvInfo = (TextView) convertView
					.findViewById(R.id.tv_fans_info);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_fans_name);
			holder.tvCancleFollow = (TextView) convertView
					.findViewById(R.id.tv_fans_operation);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(follow.getName());
		holder.tvInfo.setText(userType[follow.getuType()]);
		holder.tvCancleFollow.setText("ȡ����ע");
		ImageLoader.getInstance().displayImage(follow.getAvatarUrl(),
				holder.ivHeader, avatarOptions);
		holder.tvCancleFollow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postCancleFollow(follow.get_id(), position);
			}
		});
		return convertView;
	}

	@SuppressLint("HandlerLeak")
	protected void postCancleFollow(String id, final int position) {
		String userId = (String) SPUtil.get(context, "userId", "");
		String followApi = Constant.SERVER_URL + "/api/user/" + userId
				+ "/follow";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							Toast.makeText(context, "ȡ����ע�ɹ�",
									Toast.LENGTH_SHORT).show();
							mList.remove(position);
							notifyDataSetChanged();

						} else {
							Toast.makeText(context, "ȡ����עʧ��",
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(context, "ȡ����עʧ��", Toast.LENGTH_SHORT)
								.show();
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
		list.add(new BasicNameValuePair("follow", 0 + ""));
		new HttpConnectionUtils(handler).post(followApi, list);
	}

	private static class ViewHolder {
		ImageView ivHeader;
		TextView tvName;
		TextView tvInfo;
		TextView tvCancleFollow;
	}
}
