package com.gexin.artplatform.utils;

import android.content.Context;
import android.util.Log;

import com.gexin.artplatform.bean.User;
import com.google.gson.Gson;

public class JSONUtil {

	public static void analyseLoginJSON(Context context, String jsonObject) {
		Gson gson = new Gson();
		User user = gson.fromJson(jsonObject, User.class);
		Log.v("AfterGson:", user.toString());
		user.putToSP(context);
	}

}
