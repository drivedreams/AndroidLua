package com.luajava.ndk.util;

public class MLog {
	private static LogInterface logInterface;
	static{
		if(Settings.Debug){
			logInterface = new DebugLog();
		}else{
			logInterface = new NullDebugLog();
		}
	}
	public static void w(String tag,String msg){
		logInterface.w(tag, msg);
	}
	public static void w(String msg){
		logInterface.w(msg);
	}
	public static void d(String tag, String msg){
		logInterface.d(tag, msg);
	}
	
	public static void d(String msg){
		logInterface.d(msg);
	}
	
	public static void i(String msg){
		logInterface.i(msg);
	}
	
	public static void i(String tag, String msg){
		logInterface.i(tag, msg);
	}
	
	public static void e(String msg){
		logInterface.e( msg);
	}
	public static void e(String tag, String msg){
		logInterface.e(tag, msg);
	}
	
	public static void v(String tag, String msg){
		logInterface.v(tag, msg);
	}
	
	public static void e(Exception e){
		logInterface.e(e);
	}
	
	public static void e(String tag, String msg, Exception e){
		logInterface.e(tag, msg, e);
	}
}
