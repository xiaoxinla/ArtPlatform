package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;

public class RegisterActivity extends Activity {

	private static final String TAG = "RegisterAcitvity";
	private static final String REGISTER_API = Constant.SERVER_URL
			+ Constant.USER_API;
	private EditText etUsername;
	private EditText etName;
	private EditText etPassword;
	private Button btnConfirm;
//	private Spinner mSpinner;

//	private String[] jobs = { "学生", "教师" };
//	private ArrayAdapter<String> adapter;

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
//		mSpinner = (Spinner) findViewById(R.id.sp_job_reg);
		etName = (EditText) findViewById(R.id.et_name_reg);
//		adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, jobs);
//		mSpinner.setAdapter(adapter);
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String name = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				//默认为学生
				int isTeacher = 0;
				if (name.isEmpty() || password.isEmpty()) {
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
					list.add(new BasicNameValuePair("email", name));
					list.add(new BasicNameValuePair("password", password));
					list.add(new BasicNameValuePair("isTeacher", String
							.valueOf(isTeacher)));
					httpConnectionUtils.post(REGISTER_API, list);
				}
			}
		});
	}
	
	private void success(JSONObject jObject){
		
	}

}
