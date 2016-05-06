package com.luajava.ndk.util;

public interface LogInterface {

	public void w(String tag,String msg);
	public void w(String msg);
	public void d(String tag, String msg);
	
	public void d(String msg);
	
	public void i(String msg);
	
	public void i(String tag, String msg);
	
	public void e(String msg);
	public void e(String tag, String msg);
	
	public void v(String tag, String msg);
	
	public void e(Exception e);
	
	public void e(String tag, String msg, Exception e);
}
