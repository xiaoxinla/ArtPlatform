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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;

public class LoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
	private static final String LOGIN_API = Constant.SERVER_URL
			+ Constant.USER_API + "/login";
	private Button login;
	private EditText etUsername, etPassword;

	private String username, password;
	private Handler handler = new HttpHandler(this) {

		@Override
		protected void succeed(JSONObject jObject) {
			Log.v(TAG, jObject.toString());
			success(jObject);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		login = (Button) findViewById(R.id.login_login_btn);
		etUsername = (EditText) findViewById(R.id.login_user_edit);
		etPassword = (EditText) findViewById(R.id.login_passwd_edit);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = etUsername.getText().toString();
				password = etPassword.getText().toString();
				if (username.isEmpty() || password.isEmpty()) {
					return;
				}
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				// list.add(new BasicNameValuePair("isTeacher",
				// String.valueOf(0)));
				list.add(new BasicNameValuePair("email", username));
				list.add(new BasicNameValuePair("password", password));
				Log.v(TAG, "param:" + list.toString());
				new HttpConnectionUtils(handler).post(LOGIN_API, list);
			}
		});

	}

	private void success(JSONObject jObject) {

		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				JSONObject jsonObject = jObject.getJSONObject("user");
				int subscriptionNum = jsonObject.getInt("subscriptionNum");
				int followNum = jsonObject.getInt("followNum");
				int phone = jsonObject.getInt("phone");
				int fanNum = jsonObject.getInt("fanNum");
				int workNum = jsonObject.getInt("workNum");
				int askNum = jsonObject.getInt("askNum");
				int commentNum = jsonObject.getInt("commentNum");
				int gender = jsonObject.getInt("gender");
				int collectionNum = jsonObject.getInt("collectionNum");
				String school = jsonObject.getString("school");
				String email = jsonObject.getString("email");
				String userId = jsonObject.getString("userId");
				String name = jsonObject.getString("name");
				String avatarUrl = jsonObject.getString("avatarUrl");
				String province = jsonObject.getJSONObject("place").getString(
						"province");
				String city = jsonObject.getJSONObject("place").getString(
						"city");

				SPUtil.put(this, "subscriptionNum", subscriptionNum);
				SPUtil.put(this, "followNum", followNum);
				SPUtil.put(this, "phone", phone);
				SPUtil.put(this, "fanNum", fanNum);
				SPUtil.put(this, "workNum", workNum);
				SPUtil.put(this, "askNum", askNum);
				SPUtil.put(this, "commentNum", commentNum);
				SPUtil.put(this, "gender", gender);
				SPUtil.put(this, "collectionNum", collectionNum);
				SPUtil.put(this, "school", school);
				SPUtil.put(this, "email", email);
				SPUtil.put(this, "userId", userId);
				SPUtil.put(this, "name", name);
				SPUtil.put(this, "avatarUrl", avatarUrl);
				SPUtil.put(this, "province", province);
				SPUtil.put(this, "city", city);
			} else {
				Toast.makeText(this, "请检查用户名 或密码", Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, MainActivity.class);
		LoginActivity.this.finish();
		startActivity(intent);
		Log.v(TAG, "success");
	}
}
