package com.luajava.ndk.lua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luajava.ndk.lua.LuaJavaWrapper.VarType;
import com.luajava.ndk.util.FileMan;
import com.luajava.ndk.util.MLog;
import com.luajava.ndk.util.Settings;

import android.content.Context;

/**
 * 
 * @author Haihai.zhang drivedreams@163.com
 * 
 **/
public class ScriptEngine {
	public static final String ENCODING = "UTF-8";
	public final static String SCRIPTS_FOLDER = "scripts/";
	private static String LUA_SUFFIX = ".lua";
	private final static String TAG = Settings.TAG;

	private Map<String, Object> mDevcieProps = new HashMap<String, Object>();
	private Map<String, Object> mRomInfos = new HashMap<String, Object>();
	private List<String> mImports;

	private Context context;
	private LuaJavaWrapper luaJavaWrapper;
	private static ScriptEngine mScriptEngine = null;
	private String dir;
	
	public static ScriptEngine getInstance(Context context) {
		if (mScriptEngine == null) {
			return (mScriptEngine = new ScriptEngine(context));
		} else {
			return mScriptEngine;
		}
	}

	public void initialiseVM() {

		if (mImports == null)
			mImports = new ArrayList<String>();
		mImports.clear();
		luaJavaWrapper = LuaJavaWrapper.newInstance(context);
	}

	public void releaseVM() {
		luaJavaWrapper.drop();
		luaJavaWrapper = null;
	}

	public static String getScriptFromScriptFolder(Context context,
			String scriptName) {
		MLog.w(TAG, "getScript:" + scriptName);
		String script = FileMan.readFromAssets(SCRIPTS_FOLDER + scriptName
				+ LUA_SUFFIX, ENCODING);
		return script;
	}

	public boolean runBuffer(byte[] buf, String name) {
		// TODO 添加解压压缩过的脚本。 反编译编译过的脚本
		// Android 默认是UTF-8
		String scriptStr = new String(buf);
		return luaJavaWrapper.loadScript(scriptStr);
	}

	public static String getScriptFromAssets(Context context,
			String relativePath) {
		MLog.w(TAG, "getScript:" + relativePath);
		String script = FileMan.readFromAssets(relativePath + LUA_SUFFIX,
				ENCODING);
		return script;

	}

	public boolean call(String methodName, List<LuaVar> args,
			List<VarType> orderedResultTypes) {
		LuaResult result = luaJavaWrapper.call(methodName, args,
				orderedResultTypes);
		return result.result == 0;
	}

	public static String getScript(Context context, String path) {
		// TODO 添加网络获取功能
		MLog.w(TAG, "getScript:" + path);
		String script = FileMan.readFile(path);
		return script;
	}

	public boolean setDeviceInfo(Map<String, Object> devInfos) {
		mDevcieProps = devInfos;
		return true;
	}

	public Map<String, Object> getDeviceInfo() {
		return mDevcieProps;
	}

	public Map<String, Object> getRomInfo() {
		return mRomInfos;
	}

	private ScriptEngine(Context context) {
		this.context = context;
		dir = FileMan.getWorkDir();
	}

	public Map<String, Object> get_rom() {
		return getRomInfo();
	}

	public Map<String, Object> get_device_info() {
		return getDeviceInfo();
	}

	public int waitFor(long millionSeconds) {
		try {
			Thread.sleep(millionSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
	public void breakPoint(String str){
		MLog.d("[BP]" + str);
	}
}
