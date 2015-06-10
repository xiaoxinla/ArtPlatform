package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.view.TitleBar;

public class RegisterActivity extends Activity {

	private static final String TAG = "RegisterAcitvity";
	private static final String REGISTER_API = Constant.SERVER_URL
			+ Constant.USER_API;
	private EditText etUsername;
	private EditText etName;
	private EditText etPassword;
	private Button btnConfirm;
	private TitleBar titleBar;
	private LinearLayout llBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
	}

	private void initView() {
		etUsername = (EditText) findViewById(R.id.et_username_reg);
		etPassword = (EditText) findViewById(R.id.et_password_reg);
		btnConfirm = (Button) findViewById(R.id.btn_confirm_reg);
		etName = (EditText) findViewById(R.id.et_name_reg);
		titleBar = (TitleBar) findViewById(R.id.tb_reg_activity);
		initTitleBar();
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String phone = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				// 默认为学生
				if (phone.isEmpty() || password.isEmpty()) {
					Toast.makeText(RegisterActivity.this, "请完整填写信息",
							Toast.LENGTH_SHORT).show();
				} else {
					Handler handler = new HttpHandler(RegisterActivity.this) {

						@Override
						protected void succeed(JSONObject jObject) {
							Log.v(TAG, "succeed:" + jObject.toString());
							success(jObject);
						}

					};
					HttpConnectionUtils httpConnectionUtils = new HttpConnectionUtils(
							handler);
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("phone", phone));
					list.add(new BasicNameValuePair("password", password));
					httpConnectionUtils.post(REGISTER_API, list);
				}
			}
		});
	}

	private void success(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 0) {
				Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("phone", etUsername.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
				Log.v(TAG, "BackClick");
				finish();
			}
		});
	}

}
