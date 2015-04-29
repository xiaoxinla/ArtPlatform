package com.gexin.artplatform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.FileUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.ImageUtil;
import com.gexin.artplatform.utils.SPUtil;

public class PostProblemActivity extends Activity {

	private static final String TAG = "PostProblemActivity";
	protected static final int ALBUM_REQUEST_CODE = 0;
	protected static final int CAMERA_REQUEST_CODE = 1;
	private static final String IMAGEDIR = Constant.APP_PATH + "image/";

	private EditText etContent;
	private ImageButton ibtnImage;
	private Button btnConfirm;
	private Handler handler = new HttpHandler(this) {

		@Override
		protected void succeed(JSONObject jObject) {
			super.succeed(jObject);
			success(jObject);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_problem);

		initView();
	}

	private void initView() {
		etContent = (EditText) findViewById(R.id.et_content_postproblem);
		ibtnImage = (ImageButton) findViewById(R.id.ibtn_image_postproblem);
		btnConfirm = (Button) findViewById(R.id.btn_confirm_postproblem);

		ibtnImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showPicDialog();
			}
		});
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = etContent.getText().toString();
				if (content.isEmpty()) {
					Toast.makeText(PostProblemActivity.this, "���ⲻ��Ϊ��",
							Toast.LENGTH_SHORT).show();
					return;
				}
				hideSoftKeyboard();
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("content", content));
				String userId = (String) SPUtil.get(PostProblemActivity.this,
						"userId", "");
				new HttpConnectionUtils(handler).post(Constant.SERVER_URL
						+ Constant.USER_API + "/" + userId + "/problem", list);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bitmap bitmap;
			switch (requestCode) {
			case ALBUM_REQUEST_CODE:
				String imagePath = FileUtil.getRealFilePath(this,
						data.getData());
				Log.v(TAG, imagePath);
				bitmap = ImageUtil.getCompressedImage(imagePath, 50);
				;
				if (bitmap == null) {
					ibtnImage.setImageResource(R.drawable.ic_menu_btn_add);
				} else {
					ibtnImage.setImageBitmap(bitmap);
				}
				break;
			case CAMERA_REQUEST_CODE:
				int picCount = (Integer) SPUtil.get(this, "picCount", 0);
				String picName = "pic" + picCount + ".jpg";
				bitmap = ImageUtil.getCompressedImage(IMAGEDIR + picName, 50);
				if (bitmap == null) {
					ibtnImage.setImageResource(R.drawable.ic_menu_btn_add);
				} else {
					ibtnImage.setImageBitmap(bitmap);
				}
				break;

			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void showPicDialog() {
		new AlertDialog.Builder(this)
				.setTitle("ѡ��ͼƬ")
				.setNegativeButton("���", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// �öԻ�����ʧ
						dialog.dismiss();
						// ACTION_PICK�������ݼ�����ѡ��һ�����أ��ٷ��ĵ���������
						// Activity Action:
						// Pick an item from the data, returning what was
						// selected.
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						// ����������Դ������
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, ALBUM_REQUEST_CODE);
					}
				})
				.setPositiveButton("����", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
						/**
						 * ������仹�������ӣ����ÿ������չ��ܣ�����Ϊʲô�п������գ���ҿ��Բο����¹ٷ�
						 * �ĵ���you_sdk_path/docs/guide/topics/media/camera.html
						 */
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// ��ͼƬ����Ŀ¼�������Ŀ¼�����ڣ��򴴽���Ŀ¼
						File dirFile = new File(IMAGEDIR);
						if (!dirFile.exists()) {
							dirFile.mkdirs();
						}
						int picCount = (Integer) SPUtil.get(
								PostProblemActivity.this, "picCount", 0);
						picCount++;
						// ��ͼƬ���浽��Ŀ¼��
						intent.putExtra(
								MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(IMAGEDIR, "pic"
										+ picCount + ".jpg")));
						SPUtil.put(PostProblemActivity.this, "picCount",
								picCount);
						startActivityForResult(intent, CAMERA_REQUEST_CODE);
					}
				}).show();
	}

	protected void success(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				Toast.makeText(this, "���ʳɹ�", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();
			} else {
				Toast.makeText(this, "����ʧ��", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void hideSoftKeyboard() {
		Log.v(TAG, "hideSoftKeyboard");
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(PostProblemActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
