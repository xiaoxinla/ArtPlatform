package com.gexin.artplatform.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference ��װ������
 * �������е�commit����ʹ����SharedPreferencesCompat.apply���������
 * Ŀ���Ǿ����ܵ�ʹ��apply����commit
 * ��Ϊcommit������ͬ���ģ��������Ǻܶ�ʱ���commit��������UI�߳��У��Ͼ���IO�������������첽��
 * ��������ʹ��apply���������apply�첽�Ľ���д�룻
 * 
 * @author xiaoxin 2015-4-5
 */
public class SPUtil {

	// �������ֻ��е��ļ���
	public static final String FILE_NAME = "config";

	/** 
     * �������ݵķ�����������Ҫ�õ��������ݵľ������ͣ�Ȼ��������͵��ò�ͬ�ı��淽�� 
     *  ����ʱ������ͬ�Ḳ��
     * @param context ����������
     * @param key ����
     * @param object ��Ӧ��ֵ
     */ 
	public static void put(Context context, String key, Object object) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}
	
	/** 
     * ��ȡ���ݵķ��������Ǹ���Ĭ��ֵ�õ���������ݵľ������ͣ�Ȼ���������ڵķ�����ȡֵ 
     *  
     * @param context ����������
     * @param key ����
     * @param defaultObject Ĭ��ֵ���޷��ҵ��ü���Ӧ��ֵ��ʱ���ֵ��
     * @return �õ�������
     */ 
    public static Object get(Context context, String key, Object defaultObject)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
   
        if (defaultObject instanceof String)  
        {  
            return sp.getString(key, (String) defaultObject);  
        } else if (defaultObject instanceof Integer)  
        {  
            return sp.getInt(key, (Integer) defaultObject);  
        } else if (defaultObject instanceof Boolean)  
        {  
            return sp.getBoolean(key, (Boolean) defaultObject);  
        } else if (defaultObject instanceof Float)  
        {  
            return sp.getFloat(key, (Float) defaultObject);  
        } else if (defaultObject instanceof Long)  
        {  
            return sp.getLong(key, (Long) defaultObject);  
        }  
   
        return null;  
    }  
   
    /** 
     * �Ƴ�ĳ��keyֵ�Ѿ���Ӧ��ֵ 
     * @param context ����������
     * @param key ����
     */ 
    public static void remove(Context context, String key)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sp.edit();  
        editor.remove(key);  
        SharedPreferencesCompat.apply(editor);  
    }  
   
    /** 
     * ����������� 
     * @param context ����������
     */ 
    public static void clear(Context context)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        SharedPreferences.Editor editor = sp.edit();  
        editor.clear();  
        SharedPreferencesCompat.apply(editor);  
    }  
   
    /** 
     * ��ѯĳ��key�Ƿ��Ѿ����� 
     * @param context ����������
     * @param key ����
     * @return ���ڷ���true�������ڷ���false
     */ 
    public static boolean contains(Context context, String key)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        return sp.contains(key);  
    }  
   
    /** 
     * �������еļ�ֵ�� 
     *  
     * @param context ����������
     * @return �洢���м�ֵ�Ե�Map
     */ 
    public static Map<String, ?> getAll(Context context)  
    {  
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,  
                Context.MODE_PRIVATE);  
        return sp.getAll();  
    }  

	/**
	 * ����һ�����SharedPreferencesCompat.apply������һ��������
	 * @author xiaoxin
	 * 2015-4-5
	 */
	private static class SharedPreferencesCompat {
		//�������
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * �������apply�ķ���
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * ����ҵ���ʹ��applyִ�У�����ʹ��commit
		 * 
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			editor.commit();
		}
	}

}
