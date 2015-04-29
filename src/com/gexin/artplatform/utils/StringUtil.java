package com.gexin.artplatform.utils;

import android.annotation.SuppressLint;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * �ַ���������
 * 
 * @author xiaoxin 20150-4-29
 */
public class StringUtil {

	/**
	 * �ж��ַ����Ƿ�Ϊ��
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}

	@SuppressLint("SimpleDateFormat")
	public static String convertTimestampToString(long input) {
		Timestamp timestamp = new Timestamp(input);
		String tsStr = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		tsStr = sdf.format(timestamp);
		return tsStr;
	}
}
