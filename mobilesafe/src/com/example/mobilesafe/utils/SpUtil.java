package com.example.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {

	private static SharedPreferences sp;
	/**
	 * 写入boolean变量到sp中
	 * @param context 上下文环境
	 * @param key 存储节点的名称
	 * @param value 存储节点的值 boolean
	 */
	public static void putBoolean(Context context,String key, Boolean value){
		if(sp == null){
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	};
	
	/**
	 * 从sp中获取boolean变量
	 * @param context 上下文环境
	 * @param key 存储节点的名称
	 * @param defValue 没有此节点的默认值
	 * @return 默认值或者此节点读到的结果
	 */
	public static boolean getBoolean(Context context,String key, Boolean defValue){
		if(sp == null){
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * 写入String 到sp中
	 * @param context 上下文环境
	 * @param key 存储节点的名称
	 * @param value 存储节点的值 String
	 */
	public static void putString(Context context,String key, String value){
		if(sp == null){
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	
	/**
	 * 从sp中获取String
	 * @param context 上下文环境
	 * @param key 存储节点的名称
	 * @param defValue 没有此节点的默认值
	 * @return 默认值或者此节点读到的结果
	 */
	public static String getString(Context context,String key, String defValue){
		if(sp == null){
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
}
