package com.gexin.artplatform.utils;

import android.annotation.SuppressLint;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtil {

	/**
	 * ��ʱ���תΪ����"�����ڶ��֮ǰ"���ַ���
	 * 
	 * @param timeStr
	 *            ʱ���
	 * @return
	 */
	public static String getStandardDate(long t) {

		StringBuffer sb = new StringBuffer();

		long time = System.currentTimeMillis() - t;
		long mill = (long) Math.ceil(time / 1000);// ��ǰ

		long minute = (long) Math.ceil(time / 60 / 1000.0f);// ����ǰ

		long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// Сʱ

		long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// ��ǰ

		if (day - 1 > 0) {
			sb.append(day + "��");
		} else if (hour - 1 > 0) {
			if (hour >= 24) {
				sb.append("1��");
			} else {
				sb.append(hour + "Сʱ");
			}
		} else if (minute - 1 > 0) {
			if (minute == 60) {
				sb.append("1Сʱ");
			} else {
				sb.append(minute + "����");
			}
		} else if (mill - 1 > 0) {
			if (mill == 60) {
				sb.append("1����");
			} else {
				sb.append(mill + "��");
			}
		} else {
			sb.append("�ո�");
		}
		if (!sb.toString().equals("�ո�")) {
			sb.append("ǰ");
		}
		return sb.toString();
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getDateString(long timestamp){
		Timestamp stamp= new Timestamp(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(stamp);
	}
}
