package com.luajava.ndk.util;

import android.util.Log;
/**
 * 
 * @author Haihai.zhang drivedreams@163.com
 *
 */
public class DebugLog  implements LogInterface{

	@Override
	public void w(String tag,String msg){
		if(!Settings.Debug) return;
		Log.w(tag, getStackInfo(new Throwable()) + msg);
	}
	
	@Override
	public void w(String msg){
		if(!Settings.Debug) return;
		Log.w(Settings.TAG, getStackInfo(new Throwable()) + msg);
	}
	
	@Override
	public void d(String tag, String msg){
		if(!Settings.Debug) return;
		Log.d(tag, getStackInfo(new Throwable()) + msg);
	}
	
	@Override
	public void d(String msg){
		if(!Settings.Debug) return;
		Log.d(Settings.TAG, getStackInfo(new Throwable()) + msg);
	}
	
	@Override
	public void i(String msg){
		if(!Settings.Debug) return;
		Log.i(Settings.TAG, getStackInfo(new Throwable()) + msg);
	}
	
	@Override
	public void i(String tag, String msg){
		if(!Settings.Debug) return;
		StackTraceElement ste = new Throwable().getStackTrace()[1];  
		Log.i(tag, "[" + ste.getFileName() + ":" + ste.getLineNumber() + "] " + msg);
	}
	
	@Override
	public void e(String msg){
		if(!Settings.Debug) return;
		Log.e(Settings.TAG, getStackInfo(new Throwable()) + msg);
	}
	
	@Override
	public void e(String tag, String msg){
		if(!Settings.Debug) return;
		Log.e(tag, getStackInfo(new Throwable()) + msg);
	}
	
	@Override
	public  void v(String tag, String msg){
		if(!Settings.Debug) return;
		Log.v(tag, getStackInfo(new Throwable()) + msg);
	}
	
	@Override
	public void e(Exception e){
		if(!Settings.Debug) return;
		e(Settings.TAG, e.getMessage(), e);
		e.printStackTrace();
	}
	
	@Override
	public void e(String tag, String msg, Exception e){
		if(!Settings.Debug) return;
		Log.e(tag, getStackInfo(new Throwable()) + msg, e);
		e.printStackTrace();
	}
	
	private String getStackInfo(Throwable throwable){
		StackTraceElement[] stes = throwable.getStackTrace();  
		if(stes != null && stes.length > 0){
			return "[" + stes[1].getFileName() + ":" + stes[1].getLineNumber() + "] ";
		}else{
			return "";
		}
	}
}
