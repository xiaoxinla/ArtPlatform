package com.gexin.artplatform;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.FileUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.ImageUtil;
import com.gexin.artplatform.utils.NetUtil;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.ActionSheet;
import com.gexin.artplatform.view.ActionSheet.MenuItemClickListener;
import com.gexin.artplatform.view.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoActivity extends Activity {

	protected static final int ALBUM_REQUEST_CODE = 0;
	protected static final int CAMERA_REQUEST_CODE = 1;
	protected static final int MODIFY_REQUEST_CODE = 2;
	public static final String ACTION_HEADER_MODIFY = "ACTION_HEADER_MODIFY";
	private static final int POST_IMAGE_SUCCESS = 3;
	private static final String POST_IMAGE_API = Constant.SERVER_URL
			+ "/api/image";
	private static final String TAG = "UserInoActivity";
	private static final String IMAGEDIR = Constant.APP_PATH + "image/";
	private String imagePath = "";
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private SimpleAdapter adapter;
	private String[] titles = { "����", "�Ա�", "ʡ��", "���", "ѧУ", "�޸�����", "�ֻ���" };
	private String[] gradeArray = { "����", "�߶�", "��һ", "����", "��ѧ", "ҵ��" };
	private List<String> values = new ArrayList<String>();
	private int mIndex;
	private DisplayImageOptions headerOptions;

	private ListView mListView;
	private Button btnExit;
	private ImageView ivHeader;
	private RelativeLayout rlHeader;
	private LinearLayout llBack;
	private TitleBar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		setTheme(R.style.ActionSheetStyleIOS7);
		initView();
		initData();
	}

	private void initData() {

		String name = (String) SPUtil.get(this, "name", "δ����");
		int gender = (Integer) SPUtil.get(this, "gender", 0);
		String sex = "";
		if (gender == 0) {
			sex = "Ů";
		} else if (gender == 1) {
			sex = "��";
		} else {
			sex = "δ����";
		}
		String province = (String) SPUtil.get(this, "province", "δ����");
		String school = (String) SPUtil.get(this, "school", "δ����");
		int grade = (Integer) SPUtil.get(this, "grade", 0);
		String status = gradeArray[grade];
		if (name.isEmpty()) {
			name = "δ����";
		}
		if (province.isEmpty()) {
			province = "δ����";
		}
		if (status.isEmpty()) {
			status = "δ����";
		}
		if (school.isEmpty()) {
			school = "δ����";
		}
		values.add(name);
		values.add(sex);
		values.add(province);
		values.add(status);
		values.add(school);
		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", titles[i]);
			if (i < values.size()) {
				map.put("content", values.get(i));
			} else {
				map.put("content", "");
			}
			mList.add(map);
		}
		adapter = new SimpleAdapter(this, mList, R.layout.user_info_item,
				new String[] { "title", "content" }, new int[] {
						R.id.tv_title_user_info_item,
						R.id.tv_content_user_info_item });
		mListView.setAdapter(adapter);
		
		headerOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_contact_picture)
		.showImageForEmptyUri(R.drawable.ic_contact_picture)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true).cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
		String avatarUrl = (String) SPUtil.get(this, "avatarUrl", "");
		ImageLoader.getInstance().displayImage(avatarUrl, ivHeader,headerOptions);
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_activity_userinfo);
		btnExit = (Button) findViewById(R.id.btn_exit_activity_userinfo);
		ivHeader = (ImageView) findViewById(R.id.iv_header_activity_userinfo);
		rlHeader = (RelativeLayout) findViewById(R.id.rl_userinfo_activity_userinfo);
		titleBar = (TitleBar) findViewById(R.id.tb_userinfo_activity);
		ivHeader = (ImageView) findViewById(R.id.iv_header_activity_userinfo);

		initTitleBar();
		rlHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showHeaderSelectDialog();
			}
		});
		ivHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showHeaderSelectDialog();
			}
		});
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SPUtil.clear(UserInfoActivity.this);
				setResult(RESULT_OK);
				finish();
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mIndex = arg2;
				Intent intent = new Intent(UserInfoActivity.this,
						ModifyUserInfoActivity.class);
				intent.putExtra("index", arg2);
				intent.putExtra("title", titles[arg2]);
				intent.putExtra("value", (String) mList.get(arg2)
						.get("content"));
				startActivityForResult(intent, MODIFY_REQUEST_CODE);
			}
		});
	}

	private void showHeaderSelectDialog() {
		ActionSheet sheet = new ActionSheet(this);
		sheet.setCancelButtonTitle("ȡ��");
		sheet.addItems("����", "�������ѡȡ");
		sheet.setItemClickListener(new MenuItemClickListener() {

			@Override
			public void onItemClick(int itemPosition) {
				if (itemPosition == 1) {
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					// ����������Դ������
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(intent, ALBUM_REQUEST_CODE);
				}
				if (itemPosition == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// ��ͼƬ����Ŀ¼�������Ŀ¼�����ڣ��򴴽���Ŀ¼
					File dirFile = new File(IMAGEDIR);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					int picCount = (Integer) SPUtil.get(UserInfoActivity.this,
							"picCount", 0);
					picCount++;
					// ��ͼƬ���浽��Ŀ¼��
					intent.putExtra(
							MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(IMAGEDIR, "pic" + picCount
									+ ".jpg")));
					SPUtil.put(UserInfoActivity.this, "picCount", picCount);
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
				}
			}

		});
		sheet.showMenu();
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
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bitmap bitmap;
			switch (requestCode) {
			case ALBUM_REQUEST_CODE:
				imagePath = FileUtil.getRealFilePath(this, data.getData());
				bitmap = ImageUtil.getCompressedImage(imagePath, 50);
				if (bitmap == null) {
					ivHeader.setImageResource(R.drawable.add_icon);
				} else {
					ivHeader.setImageBitmap(bitmap);
				}
				uploadImage(imagePath);
				break;
			case CAMERA_REQUEST_CODE:
				int picCount = (Integer) SPUtil.get(this, "picCount", 0);
				String picName = "pic" + picCount + ".jpg";
				imagePath = IMAGEDIR + picName;
				bitmap = ImageUtil.getCompressedImage(imagePath, 50);
				if (bitmap == null) {
					ivHeader.setImageResource(R.drawable.add_icon);
				} else {
					ivHeader.setImageBitmap(bitmap);
				}
				break;
			case MODIFY_REQUEST_CODE:
				if (resultCode == RESULT_OK) {
					String value = data.getStringExtra("value");
					if (value.isEmpty()) {
						value = "δ����";
					}
					mList.get(mIndex).put("content", value);
					if (mIndex == 5 || mIndex == 6) {
						mList.get(mIndex).put("content", "");
					}
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("HandlerLeak")
	private void uploadImage(String imagePath2) {
		final String userId = (String) SPUtil.get(this, "userId", "");
		new Thread(new Runnable() {
			private String imageUrl = "";
			private String modifyApi = Constant.SERVER_URL + Constant.USER_API
					+ "/" + userId ;

			Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case HttpConnectionUtils.DID_SUCCEED:
						String response = (String) msg.obj;
						Log.v(TAG, "response:" + response);
						try {
							JSONObject jObject = new JSONObject(response);
							int state = jObject.getInt("stat");
							if (state == 1) {
								Toast.makeText(getApplicationContext(),
										"ͷ����³ɹ�", Toast.LENGTH_SHORT).show();
								SPUtil.put(UserInfoActivity.this, "avatarUrl",
										imageUrl);
								Intent intent = new Intent();
								intent.putExtra("avatarUrl", imageUrl);
								intent.setAction(ACTION_HEADER_MODIFY);
								UserInfoActivity.this.sendBroadcast(intent);
							} else {
								Toast.makeText(getApplicationContext(),
										"ͷ�����ʧ��", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "ͷ�����ʧ��",
									Toast.LENGTH_SHORT).show();
						}

						break;

					case POST_IMAGE_SUCCESS:
						uploadHeader();
					default:
						break;
					}
					super.handleMessage(msg);
				}

			};

			@Override
			public void run() {
				String imageResult = NetUtil.uploadFile(new File(imagePath),
						POST_IMAGE_API);
				if (imageResult != null && !imageResult.isEmpty()) {
					try {
						JSONObject jObject = new JSONObject(imageResult);
						int state = jObject.getInt("stat");
						if (state == 1) {
							imageUrl = jObject.getString("url");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (!imageUrl.isEmpty()) {
						handler.sendEmptyMessage(POST_IMAGE_SUCCESS);
					}
				}

			}

			private void uploadHeader() {
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				if (imageUrl != null && !imageUrl.isEmpty()) {
					list.add(new BasicNameValuePair("avatarUrl", imageUrl));
				}
				new HttpConnectionUtils(handler).put(modifyApi, list);
			}
		}).start();

	}
}
