package com.luajava.ndk.lua;

import java.util.ArrayList;
import java.util.List;

import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import com.luajava.ndk.util.FileMan;
import com.luajava.ndk.util.MLog;
import com.luajava.ndk.util.Settings;


import android.content.Context;

/**
 * 
 * @author Haihai.zhang drivedreams@163.com
 * 
 **/
public class LuaJavaWrapper {

	private static final String TAG = Settings.TAG;
	private LuaState L;
	private Context context;

	/**
	 * 变量类型
	 * 
	 * @author haihai.zhang
	 * 
	 */
	public static enum VarType {
		INT, DOUBLE, STRING, BOOLEAN, OBJECT,
	}

	/**
	 * 获取Lua和Java交互的封装类。
	 * 
	 * @param context
	 * @return
	 */
	public static LuaJavaWrapper newInstance(Context context) {

		LuaJavaWrapper luaJavaWrapper = new LuaJavaWrapper(context);

		return luaJavaWrapper;
	}

	/**
	 * 引入脚本
	 * 
	 * @param script
	 */
	public synchronized boolean loadScript(String script) {
		// 加载脚本
		int code = L.LdoString(script);
		return code == 0;
	}

	/**
	 * 调用 Lua 脚本中指定的函数
	 * 
	 * @param methodName
	 *            函数名称
	 * @param args
	 *            （可以为null）给定函数调用所需参数的类型和参数值（e.g new Pair<ArgumentType,
	 *            Object>("double", 100)）。<b> 注：此参数的顺序必须与Lua函数的参数顺序一致</b>
	 * @param orderedResultTypes
	 *            （可以为null）返回值类型集合（Lua可以返回多个值）.<b> 注：此参数的顺序必须与Lua函数的返回值顺序一致</b>
	 * @return
	 */
	public synchronized LuaResult call(String methodName, List<LuaVar> args,
			List<VarType> orderedResultTypes) {
		MLog.i(TAG, "函数名称：" + methodName);
		
		
		L.getField(LuaState.LUA_GLOBALSINDEX, "printTraceBack");
		int errorFucIndex = L.getTop();
		L.getField(LuaState.LUA_GLOBALSINDEX, methodName);
		// 给定参数为空时认为参数个数为0
		if (args == null) {
			args = new ArrayList<LuaVar>();
		}

		// 给定的结果类型集合为空时认为无返回值
		if (orderedResultTypes == null) {
			orderedResultTypes = new ArrayList<LuaJavaWrapper.VarType>();
		}

		for (int i = 0; i < args.size(); i++) {
			pushArg(args.get(i));
		}

		MLog.i(TAG, "方法名：" + methodName + "|参数个数：" + args.size() + "|返回值个数："
				+ orderedResultTypes.size());

		int errCode = L.pcall(args.size(), orderedResultTypes.size(), errorFucIndex);

		if (errCode != 0) {
			MLog.e(TAG, "ErrCode:" + errCode);
			return new LuaResult(errCode, null, "Call method [" + methodName
					+ "] failed");
		}
		List<Object> resultValues = new ArrayList<Object>();
		// Lua的返回值是存储在栈中的，所以在获取返回值的时候需要倒序获取
		for (int i = orderedResultTypes.size() - 1; i >= 0; i--) {
			MLog.i(TAG, "循环获取返回值 ...");

			// 保存返回值到result中
			// Lua的返回值是存储在栈中的，所以在获取返回值的时候需要倒序获取
			Object value;
			try {
				int index = L.getTop() - (orderedResultTypes.size() - i - 1);
				MLog.w(TAG, "Return" + index);
				value = getObjectValue(orderedResultTypes.get(i),
						L.getLuaObject(index));
				MLog.w(TAG, "Return" + value.toString());

				resultValues.add(0, value);

			} catch (LuaException e) {
				MLog.e(TAG, "获取返回值失败", e);
				// return new LuaResult(errCode, resultValues, "");
			}

		}

		if (resultValues.size() == orderedResultTypes.size()) {
			return new LuaResult(LuaResult.SUCCESS_RESULT_CODE, resultValues,
					"");
		} else {
			return new LuaResult(-1, resultValues,
					"The count of the results is not right.");
		}

	}

	/**
	 * 注册一个可以在整个脚本任意位置调用的方法(e.g 日志打印)
	 * 
	 * @param obj
	 */
	public synchronized void registerGlobalFunction(String functionName,
			final Function function) {

		try {
			JavaFunction functionWrapper = new JavaFunction(L) {

				@Override
				public int execute() throws LuaException {

					MLog.i(TAG, "  functionWrapper execute ..");
					// 从栈中获取接收到的参数，传递给function
					List<LuaVar> params = new ArrayList<LuaVar>();
					for (int i = 2; i <= L.getTop(); i++) {
						int type = L.type(i);
						String stype = L.typeName(type);
						String val = null;
						VarType vartype;
						if (stype.equals("userdata")) {
							vartype = VarType.OBJECT;
							Object obj = L.toJavaObject(i);
							if (obj != null)
								val = obj.toString();
						} else if (stype.equals("boolean")) {
							vartype = VarType.BOOLEAN;
							val = L.toBoolean(i) ? "true" : "false";
						} else {
							vartype = VarType.STRING;
							val = L.toString(i);
						}

						params.add(new LuaVar(vartype, val));

					}
					MLog.i(TAG, "  function.execute ..");
					List<LuaVar> res = function.execute(params);

					// 若返回值为空则认为无返回值
					if (res == null) {
						return 0;
					}

					for (int i = 0; i < res.size(); i++) {
						pushArg(res.get(i));
					}
					MLog.i(TAG, "返回值个数: " + res.size());
					return res.size();
				}
			};
			functionWrapper.register(functionName);
			MLog.w(TAG, "Registered function: " + functionName);

		} catch (LuaException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 在Lua脚本中注册一个全局可用的全局Java对象，注意在Lua中调用方法用“：”而不是“.”
	 * 
	 * @param objectName
	 *            在Lua脚本中的名称
	 * @param obj
	 *            Java对象
	 */
	public synchronized void registerGlobalObject(String objectName, Object obj) {
		MLog.i(TAG, "Register a java object:" + objectName);
		L.pushJavaObject(obj);
		L.setGlobal(objectName);

	}

	/**
	 * 在Lua脚本中注册一个全局可用的全局字符串
	 * 
	 * @param objectName
	 *            在Lua脚本中的名称
	 * @param obj
	 *            Java对象
	 */
	public synchronized void registerGlobalString(String name, String content) {
		MLog.i(TAG, "Register a String:" + content);
		L.pushString(content);
		L.setGlobal(name);
	}

	/**
	 * 获取脚本中的全局变量
	 * 
	 * @param type
	 *            变量类型
	 * @param name
	 *            变量名称
	 * @return
	 */
	public synchronized Object getGlobalObject(VarType type, String name) {
		L.getField(LuaState.LUA_GLOBALSINDEX, name);
		LuaObject lobj = L.getLuaObject(L.getTop());
		Object resultObj = null;
		try {
			resultObj = getObjectValue(type, lobj);
		} catch (LuaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultObj;

	}

	/**
	 * 获取脚本中的全局变量
	 * 
	 * @param varType
	 *            变量类型
	 * @param name
	 *            变量名称
	 * @return
	 */
	public synchronized LuaObject getGlobalLuaObject(String name) {
		MLog.i(TAG, "Try to get global field:" + name);
		L.getField(LuaState.LUA_GLOBALSINDEX, name);
		LuaObject lobj = L.getLuaObject(L.getTop());
		MLog.i(TAG, "getGlobalLuaObject:" + lobj.toString());
		return lobj;

	}

	/**
	 * 关闭LuaState，移除Objects
	 */
	public synchronized void drop() {
		L.close();
		L = null;
	}

	/**
	 * 初始化 注：此函数会在脚本加载之前执行
	 */
	private synchronized void init() {
		// 设置Lua的打印日志是会打印函数名，行号等信息。
		L.openDebug();
		loadScript(ScriptEngine.getScriptFromScriptFolder(context, "m_log"));
		loadScript(ScriptEngine
				.getScriptFromScriptFolder(context, "trace_back"));
		FileMan fileUtils = new FileMan();
		// 注册参数用于接收Lua中table数据。
		registerGlobalObject("LuaParams", LuaParams.class);
		//网络操作
		
		// 设置文件操作根目录
		registerGlobalString("DIR", FileMan.getWorkDir());
		// 注册文件操作Util
		registerGlobalObject("fileUtils", fileUtils);

		registerGlobalObject("man", ScriptEngine.getInstance(context));
		registerGlobalObject("lua_wrapper", this);
		
		// 注册多脚本相互引用函数
		registerGlobalFunction("import", new Function() {
			@Override
			public List<LuaVar> execute(List<LuaVar> params) {
				for (int i = 0; i < params.size(); i++) {
					LuaVar param = params.get(i);
					loadScript(new String(ScriptEngine.getScriptFromAssets(context, "scripts/" + param.value.toString())
							));
				}
				return new ArrayList<LuaVar>();
			}
		});

		registerGlobalObject("LuaLog", LuaLog.class);
		//无需注册，通过反射获取Java类
		registerGlobalFunction("reflectJavaClass", new Function() {

			@Override
			public List<LuaVar> execute(List<LuaVar> params) {
				LuaVar pa = params.get(0);
				String className = (String) pa.value;
				
				try {
					Class<?> cls = null;
					cls = Class.forName(className);
					List<LuaVar> ls = new ArrayList<LuaVar>();
					ls.add(new LuaVar(VarType.OBJECT, cls));
					return ls;
				} catch (ClassNotFoundException e) {
					MLog.e(e);
				}
				return null;
			}
			
		});
		
		// 注册多脚本相互引用函数
		registerGlobalFunction("Log", new Function() {

			@Override
			public List<LuaVar> execute(List<LuaVar> params) {
				// TODO Auto-generated method stub
				for (int i = 0; i < params.size(); i++) {
					LuaVar param = params.get(i);
					MLog.i(TAG, "[Lua-log|" + param.type + "]:" + param.value);

				}
				return new ArrayList<LuaVar>();
			}
		});

	}

	/**
	 * 根据变量类型指定设置参数方法
	 * 
	 * @param arg
	 */
	private synchronized void pushArg(LuaVar arg) {

		switch (arg.type) {
		case INT:
			L.pushInteger(Integer.parseInt(arg.value.toString()));
			break;
		case DOUBLE:
			MLog.w(TAG, "Push double:" + arg.value);
			L.pushNumber(Double.parseDouble(arg.value.toString()));
			break;
		case STRING:
			L.pushString(arg.value.toString());
			break;
		case BOOLEAN:
			L.pushBoolean(Boolean.parseBoolean(arg.value.toString()));
			break;
		case OBJECT:
			L.pushJavaObject(arg.value);
		default:
			break;

		}
	}

	private LuaJavaWrapper(Context context) {
		this.context = context;
		L = LuaStateFactory.newLuaState();
		// 加载Lua标准库,否则一些Lua基本函数无法使用
		L.openLibs();
		init();
	}

	/**
	 * 根据指定类型获取值
	 * 
	 * @param type
	 * @param luaObject
	 * @return
	 * @throws LuaException
	 */
	private synchronized Object getObjectValue(VarType type, LuaObject luaObject)
			throws LuaException {
		switch (type) {
		// Lua没有int类型，需要在这里强制类型转换。
		case INT:
			return (int) luaObject.getNumber();
		case DOUBLE:
			return luaObject.getNumber();
		case STRING:
			return luaObject.getString();
		case OBJECT:
			return luaObject.getObject();
		case BOOLEAN:
			return luaObject.getBoolean();
		default:
			return null;
		}

	}

	/**
	 * 在Lua中注册Java方法的接口，Lua中执行对应方法时会回调该接口的execute方法。
	 * 
	 * @author 
	 * 
	 **/
	public interface Function {
		/**
		 * 调用函数时会自动调用该接口中的此函数。在此函数中实现需要的功能
		 * 
		 * @param params
		 *            参数
		 * @return （可以为null，表示无返回值）返回执行结果列表，列表的每个单元都应当包含变量类型和变量值。
		 */
		public List<LuaVar> execute(List<LuaVar> params);
	}

}
