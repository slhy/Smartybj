package com.saw.smartybj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Administrator
 * @创建时间 2016-6-11 下午9:18:06
 * @描述 TODO
 */
public class SpTools {
	/**设置布尔类型的值
	 * @param context 上下文对象
	 * @param key 关键字
	 * @param value 对应的值
	 */
	public static void setBoolean(Context context,String key,boolean value) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.CONFIGFILE, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();//提交保存键值对
	}
	/**
	 * 获取布尔类型的值
	 * @param context 上下文对象
	 * @param key 关键字
	 * @param defValue 默认值
	 * @return 对应的值
	 */
	public static boolean getBoolean(Context context,String key,boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.CONFIGFILE, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	/**设置String类型的值
	 * @param context 上下文对象
	 * @param key 关键字
	 * @param value 对应的值
	 */
	public static void setString(Context context,String key,String defValue) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.CONFIGFILE, Context.MODE_PRIVATE);
		sp.edit().putString(key, defValue).commit();//提交保存键值对
	}
	/**
	 * 获取String类型的值
	 * @param context 上下文对象
	 * @param key 关键字
	 * @param defValue 默认值
	 * @return 对应的值
	 */
	public static String getString(Context context,String key,String defValue) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.CONFIGFILE, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
}
