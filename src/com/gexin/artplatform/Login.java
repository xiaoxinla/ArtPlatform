package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;

public class Login extends Activity {

	private static final String TAG = "LoginActivity";
	private Button login;
	private EditText etUsername, etPassword;

	private static final String LOGIN = "/login";
	private String username, password;
	private Handler handler = new HttpHandler(this) {

		@Override
		protected void succeed(JSONObject jObject) {
			super.succeed(jObject);
			Log.v(TAG, jObject.toString());
			success();
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
//				list.add(new BasicNameValuePair("isTeacher", String.valueOf(0)));
				list.add(new BasicNameValuePair("email", username));
				list.add(new BasicNameValuePair("password", password));
				Log.v(TAG, "param:"+list.toString());
				new HttpConnectionUtils(handler).post(Constant.SERVER_URL
						+ Constant.USER_API + LOGIN, list);
			}
		});

	}

	private void success() {
		SPUtil.put(Login.this, "username", username);
		SPUtil.put(Login.this, "password", password);

		Intent intent = new Intent();
		intent.setClass(Login.this, MainActivity.class);
		Login.this.finish();
		startActivity(intent);
	}
}
