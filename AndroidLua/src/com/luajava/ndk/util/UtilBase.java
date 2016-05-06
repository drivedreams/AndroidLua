package com.luajava.ndk.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.widget.Toast;

/**
 * 
 * @author Haihai.zhang drivedreams@163.com
 * 
 **/
public class UtilBase {
	private static Context mContext;

	public static void setContext(Context wmContext) {
		UtilBase.mContext = wmContext;
	}

	public static Context getContext() {
		return mContext;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void copyToClipBoard(String content, String msg) {
		if (VERSION.SDK_INT < VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboardManager.setText(content);
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		} else {
			android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboardManager.setPrimaryClip(android.content.ClipData.newPlainText(null, content));
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
		}
	}
	
	public static Map<String, String> getVersionMap(){
		PackageManager pm =  mContext.getPackageManager();
		Map<String, String> versions = new HashMap<String, String>();
		try {
			android.content.pm.PackageInfo pinfo = pm.getPackageInfo(mContext.getPackageName(), 0);
			versions.put("version_code", String.valueOf(pinfo.versionCode));
			versions.put("version_name", pinfo.versionName);
			return versions;
		} catch (NameNotFoundException e) {
			MLog.e(e);
		}
		return new HashMap<String, String>();
	}
}
