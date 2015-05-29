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
import com.gexin.artplatform.utils.SPUtil;

public class LoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
	private static final String LOGIN_API = Constant.SERVER_URL
			+ Constant.USER_API + "/login";
	protected static final int WEIBO_LOGIN_REQUEST = 0;
	// private int mode = 0; //0表示登录模式，1表示注册模式

	private Button btnLogin, btnReg, btnWeiboLog;
	private EditText etUsername, etPassword;
	private String username, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();

	}

	private void initView() {
		btnLogin = (Button) findViewById(R.id.login_login_btn);
		btnReg = (Button) findViewById(R.id.login_reg_btn);
		btnWeiboLog = (Button) findViewById(R.id.btn_weibolog_activity_login);
		etUsername = (EditText) findViewById(R.id.login_user_edit);
		etPassword = (EditText) findViewById(R.id.login_passwd_edit);

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = etUsername.getText().toString();
				password = etPassword.getText().toString();
				if (username.isEmpty() || password.isEmpty()) {
					Toast.makeText(LoginActivity.this, "请完整填写账号和密码",
							Toast.LENGTH_SHORT).show();
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

		btnReg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

		btnWeiboLog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent();   
			    intent.setClass(LoginActivity.this, WeiboLoginActivity.class);
			    startActivityForResult(intent, WEIBO_LOGIN_REQUEST); 
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case WEIBO_LOGIN_REQUEST:
			if(resultCode==RESULT_OK){
				setResult(RESULT_OK);
				int isTeacher = (Integer) SPUtil.get(this, "isTeacher", 8);
				Log.v(TAG, "isTeacher:"+isTeacher);
				finish();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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
				Toast.makeText(this, "请检查账号或密码", Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
