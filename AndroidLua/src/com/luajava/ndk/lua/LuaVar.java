package com.luajava.ndk.lua;

import com.luajava.ndk.lua.LuaJavaWrapper.VarType;

/**
 *	自定义的Lua变量，包括变量类型和变量值
 * @author Haihai.zhang drivedreams@163.com
 *
 **/
public class LuaVar{
	public VarType type = null;
	public Object value = null;
	public LuaVar(VarType type, Object value){
		this.type = type;
		this.value = value;
	}
	
}