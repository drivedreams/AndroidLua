package com.luajava.ndk.lua;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * 用于 LUA 向Java传递多个参数
 * @author Haihai.zhang drivedreams@163.com
 *
 **/
public class LuaParams {

	TreeMap<String, Object> params = new TreeMap<String, Object>();
	
	public static LuaParams newInstance(){
		
		return new LuaParams();
	}
	
	public void put(Object value){
		params.put(params.size() + "", value);
	}
	
	public void put(String key, Object value){
		params.put(key, value);
	}
	
	/**
	 * 根据给定key获取参数
	 * @param key
	 * @return
	 */
	public Object get(String key){
		return params.get(key);
	}
	
	/**
	 * 获取所有参数键值对
	 * @return
	 */
	public TreeMap<String, Object> getAll(){
		return params;
	}
	
	/**
	 * 获取所有参数值
	 * @return
	 */
	public List<Object> getAllValues(){
		List<Object> valueList = new ArrayList<Object>();
		for (String key : params.keySet()) {
			valueList.add(params.get(key));
		}
		return valueList;
	}
	
	/**
	 * 获取参数个数
	 * @return
	 */
	public int size(){
		return params.size();
	}
	
	@Override
	public String toString(){
		StringBuffer str = new StringBuffer("");
		if(params.size() <= 0) return null;
		str.append("{");
		for (String key : params.keySet()) {
			str.append("[" + key + ":" + params.get(key) + "]" + ","); 
		}
		str.deleteCharAt(str.lastIndexOf(","));
		str.append("}");
		
		return str.toString() ;
		
	}
}
