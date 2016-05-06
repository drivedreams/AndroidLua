package com.luajava.ndk.lua;

import java.util.List;


/**
 * <ul>
 * <li>{@link LuaResult#result} means result of LUA, 0 means normal, else means error </li>
 * <li>{@link LuaResult#successResult} means success value of LUA</li>
 * <li>{@link LuaResult#errorMsg} means error message of LUA result</li>
 * </ul>
 * 
 * @author Haihai.zhang drivedreams@163.com
 *
 **/
public class LuaResult {
	public final static int SUCCESS_RESULT_CODE = 0;
	/**	Result code of LUA **/
	public int result;
	/**	Result value of LUA **/
	public List<Object> successResult = null;
	public String errorMsg = null;
	public LuaResult(int _result) {
		super();
		result = _result;
	}
	
	public LuaResult(int _result, List<Object> _successResult, String _errorMsg){
		result = _result;
		successResult = _successResult;
		errorMsg = _errorMsg;
	}
	
	
}
