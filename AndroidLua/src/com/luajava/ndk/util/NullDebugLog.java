package com.luajava.ndk.util;

public class NullDebugLog  implements LogInterface {

	@Override
	public void w(String tag, String msg) {
	}

	@Override
	public void w(String msg) {
	}

	@Override
	public void d(String tag, String msg) {
	}

	@Override
	public void d(String msg) {
	}

	@Override
	public void i(String msg) {
	}

	@Override
	public void i(String tag, String msg) {
		
	}

	@Override
	public void e(String msg) {
		
	}

	@Override
	public void e(String tag, String msg) {
		
	}

	@Override
	public void v(String tag, String msg) {
		
	}

	@Override
	public void e(Exception e) {
		
	}

	@Override
	public void e(String tag, String msg, Exception e) {
		
	}

}
