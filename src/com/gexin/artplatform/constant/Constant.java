package com.gexin.artplatform.constant;

import android.os.Environment;

public class Constant {

	private Constant(){
		
	}
	public static final String CACHE_DIR = "imagecache/";

	public static String APP_PATH = Environment.getExternalStorageDirectory()
			+ "/gexin/";

	// ��������ַ
	public static String SERVER_URL = "http://120.26.192.176/msq";
	// �û����API
	public static String USER_API = "/api/user";
	// �ʴ����API
	public static String PROBLEM_API = "/api/problem";
	// ����ҳ���API
	public static String Discover_API = "/api/classification";
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
}
