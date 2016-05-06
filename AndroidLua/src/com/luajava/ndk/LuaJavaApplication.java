package com.luajava.ndk;

import com.luajava.ndk.util.UtilBase;

import android.app.Application;

public class LuaJavaApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		UtilBase.setContext(getApplicationContext());
	}

	
}
