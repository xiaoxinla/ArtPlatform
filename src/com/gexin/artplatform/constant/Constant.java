package com.gexin.artplatform.constant;

import android.os.Environment;

public class Constant {

	public static String APP_PATH = Environment.getExternalStorageDirectory()
			+ "/gexin/";

	// 服务器地址
	public static String SERVER_URL = "http://198.58.117.134:5001";
	// 用户相关API
	public static String USER_API = "/api/user";
	// 问答相关API
	public static String PROBLEM_API = "/api/problem";
}
