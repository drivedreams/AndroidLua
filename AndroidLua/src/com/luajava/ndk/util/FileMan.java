package com.luajava.ndk.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.http.util.EncodingUtils;

import android.content.res.AssetManager;
import android.os.Environment;

/**
 * 
 * @author Haihai.zhang drivedreams@163.com
 * 
 **/
public class FileMan {
	static String TAG = Settings.TAG;
	public final static String SDCARD_WORK_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/work";

	

	/**
	 * 获取文件暂存目录
	 * 
	 * @param context
	 * @return
	 */
	public static String getWorkDir() {

		File workDir = UtilBase.getContext().getFilesDir();
		MLog.w(Settings.TAG,
				"Sucessed geting work directory:" + workDir.getAbsolutePath());
		return workDir.getAbsolutePath();

	}



	/**
	 * 获取Assets中文件并转化为字符串
	 * 
	 * @param context
	 * @param fileName
	 *            文件名（包括在assets下的路径）
	 * @param ENCODING
	 *            编码方式
	 * @return
	 */
	public static String readFromAssets(String relativeFilePath, String ENCODING) {
		String luaScript = "";
		try {
			InputStream in = UtilBase.getContext().getAssets()
					.open(relativeFilePath);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			luaScript = EncodingUtils.getString(buffer, ENCODING);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return luaScript;
	}

	/**
	 * 获取Assets中文件并转化为字符串
	 * 
	 * @param context
	 * @param fileName
	 *            文件名（包括在assets下的路径）
	 * @param ENCODING
	 *            编码方式
	 * @return
	 */
	public static byte[] readBytesFromAssets(String relativeFilePath,
			String ENCODING) {
		byte[] buffer = null;
		try {
			InputStream in = UtilBase.getContext().getAssets()
					.open(relativeFilePath);
			int length = in.available();
			buffer = new byte[length];
			in.read(buffer);

		} catch (IOException e) {
			//MLog.e(e);
		}
		return buffer;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param path
	 * @return 文件内容
	 */
	public static String readFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		String resultStr = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			try {
				while (br.read() != -1) {
					resultStr = resultStr + br.readLine() + "\n";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return resultStr;
	}

	/**
	 * 读取流数据
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public String readStream(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		byte[] buffer = new byte[1024];
		while (is.read(buffer) != -1) {
			sb.append(buffer);
		}
		is.close();
		return sb.toString();
	}

	/**
	 * 拷贝Assets中的文件到指定目标路径
	 * 
	 * @param relativeFilePath
	 *            需要拷贝的文件的相对路径，包括文件名
	 * @param targetDir
	 *            目标路径
	 * @param targetFileName
	 *            目标文件名
	 */
	public static int copyAssetsFile(String relativeFilePath, String targetDir,
			String targetFileName) {
		MLog.i(TAG, "copy assets file: " + relativeFilePath + " to:"
				+ targetDir + File.separator + targetFileName);
		AssetManager assetManager = UtilBase.getContext().getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(relativeFilePath);
			File outFile = new File(targetDir, targetFileName);
			out = new FileOutputStream(outFile);
			catchStream(in, out);
			in.close();
			in = null;
			out.close();
			out = null;
		} catch (IOException e) {
			MLog.e(TAG, "Failed to copy asset file: " + relativeFilePath, e);
			return -1;
		}
		MLog.i(TAG, "Successed to copy asset file: " + relativeFilePath);
		return 0;

	}

	


	/**
	 * 复制文件流
	 * 
	 * @param in
	 *            未关闭
	 * @param out
	 *            未关闭
	 * @throws IOException
	 */
	private static void catchStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		out.flush();
	}


}
