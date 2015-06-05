package com.gexin.artplatform;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.gexin.artplatform.bean.Fans;
import com.gexin.artplatform.bean.User;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewOtherUserActivity extends Activity {
	
	private String TAG = "ViewOtherUserActivity";
	private String userId;
	private User user;
	private Gson gson = new Gson();
	private LinearLayout llFocus, llFans, llCollect;
	private RelativeLayout rlWork, rlPump, rlComment, rlSubscribe, rlHeader;
	private TextView tvFocus, tvFans, tvCollect, tvWork, tvPump, tvComment,
			tvSubscribe, tvName;
	private ImageView ivHeader;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mine);
		
		Bundle bundle = this.getIntent().getExtras();
		userId = bundle.getString("userId");
		String api = Constant.SERVER_URL + Constant.USER_API + "/" + userId;
		initView();
		
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					String response = (String) msg.obj;
					try {
						JSONObject jObject = new JSONObject(
								response == null ? "" : response.trim());
						if (jObject != null) {
							user = success(jObject);
							setDataToView();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}

		};
		
		new HttpConnectionUtils(handler).get(api);

		
	}
	
	private User success(JSONObject jObject) {
		int state = -1;
		User temp = null;
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				temp = gson.fromJson(jObject.getJSONObject("user").toString(),
						new TypeToken<User>() {
						}.getType());
				if (temp != null) {
					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	private void initView() {
		llFocus = (LinearLayout) findViewById(R.id.ll_focus_fragment_mine);
		llFans = (LinearLayout) findViewById(R.id.ll_fans_fragment_mine);
		llCollect = (LinearLayout) findViewById(R.id.ll_collect_fragment_mine);
		rlWork = (RelativeLayout) findViewById(R.id.rl_work_fragment_mine);
		rlPump = (RelativeLayout) findViewById(R.id.rl_pump_fragment_mine);
		rlComment = (RelativeLayout) findViewById(R.id.rl_comment_fragment_mine);
		rlSubscribe = (RelativeLayout) findViewById(R.id.rl_subscribe_fragment_mine);
		rlHeader = (RelativeLayout) findViewById(R.id.rl_userinfo_fragment_mine);
		tvFocus = (TextView) findViewById(R.id.tv_focusnum_fragment_mine);
		tvFans = (TextView) findViewById(R.id.tv_fansnum_fragment_mine);
		tvCollect = (TextView) findViewById(R.id.tv_collectnum_fragment_mine);
		tvWork = (TextView) findViewById(R.id.tv_work_fragment_mine);
		tvPump = (TextView) findViewById(R.id.tv_pump_fragment_mine);
		tvComment = (TextView) findViewById(R.id.tv_comment_fragment_mine);
		tvSubscribe = (TextView) findViewById(R.id.tv_subscribe_fragment_mine);
		tvName = (TextView) findViewById(R.id.tv_name_fragment_mine);
		ivHeader = (ImageView) findViewById(R.id.iv_header_fragment_mine);
	}
	
	private void setDataToView() {
		String name = user.getName();
		String avatarUrl = user.getAvatarUrl();
		int followNum = user.getFollowNum();
		int fanNum = user.getFanNum();
		int collectionNum = user.getCollectionNum();
		int workNum = user.getWorkNum();
		int askNum = user.getAskNum();
		int commentNum = user.getCommentNum();
		int subscriptionNum = user.getSubscriptionNum();
		if (name.isEmpty()) {
			name = "未设置";
		}
		tvName.setText(name);
		tvFocus.setText("" + followNum);
		tvFans.setText("" + fanNum);
		tvCollect.setText("" + collectionNum);
		if (workNum != 0 ) {
			tvWork.setText("我的作品(" + workNum + ")");
		}
		if (askNum != 0) {
			tvPump.setText("我的提问(" + askNum + ")");
		}
		if (commentNum != 0) {
			tvComment.setText("我的评论(" + commentNum + ")");
		}
		if (subscriptionNum != 0) {
			tvSubscribe.setText("我的订阅(" + subscriptionNum + ")");
		}
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_contact_picture)
					.showImageForEmptyUri(R.drawable.ic_contact_picture)
					.showImageOnFail(R.drawable.ic_contact_picture)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoader.getInstance()
					.displayImage(avatarUrl, ivHeader, options);
		
	}
}
