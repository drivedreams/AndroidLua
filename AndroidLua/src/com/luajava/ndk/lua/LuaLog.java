package com.luajava.ndk.lua;

import com.luajava.ndk.util.Settings;

import android.util.Log;


/**
 *
 * @author Haihai.zhang drivedreams@163.com
 *
 **/
public class LuaLog {
	private final static String TAG = Settings.TAG;
	public static void w(String msg){
		if(!Settings.Debug) return;
		Log.w(TAG, msg);
	}
	
	public static void d(String msg){
		if(!Settings.Debug) return;
		Log.d(TAG, msg);
	}
	
	
	public static void i(String msg){
		if(!Settings.Debug) return;
		Log.i(TAG, msg);
	}
	
	public static void e(String msg){
		if(!Settings.Debug) return;
		Log.e(TAG, msg);
	}
	
	public static void v(String msg){
		if(!Settings.Debug) return;
		Log.v(TAG, msg);
	}
	
}
