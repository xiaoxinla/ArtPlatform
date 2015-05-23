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
import com.gexin.artplatform.utils.JSONUtil;

public class LoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
	private static final String LOGIN_API = Constant.SERVER_URL
			+ Constant.USER_API + "/login";
	private Button login, reg;
	private EditText etUsername, etPassword;
	private String username, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		login = (Button) findViewById(R.id.login_login_btn);
		reg = (Button) findViewById(R.id.login_reg_btn);
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
				Handler handler = new HttpHandler(LoginActivity.this) {

					@Override
					protected void succeed(JSONObject jObject) {
						Log.v(TAG, jObject.toString());
						success(jObject);
					}

				};
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("account", username));
				list.add(new BasicNameValuePair("password", password));
				Log.v(TAG, "param:" + list.toString());
				new HttpConnectionUtils(handler).post(LOGIN_API, list);
			}
		});

		reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

	}

	private void success(JSONObject jObject) {

		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				JSONUtil.analyseLoginJSON(this, jObject.getJSONObject("user")
						.toString());
				Log.v(TAG, "success");
				Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();
			} else {
				Toast.makeText(this, "请检查用户名 或密码", Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
