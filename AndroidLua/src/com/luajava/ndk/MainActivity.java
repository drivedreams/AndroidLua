package com.luajava.ndk;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;

import com.luajava.ndk.lua.LuaJavaWrapper;
import com.luajava.ndk.lua.ScriptEngine;
import com.luajava.ndk.util.FileMan;
import com.luajava.ndk.util.MLog;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new Thread(){

			@Override
			public void run() {
				ScriptEngine.getInstance(MainActivity.this).initialiseVM();
				LuaJavaWrapper luaVm = LuaJavaWrapper.newInstance(MainActivity.this);
				
				luaVm.registerGlobalObject("javaFunctions", new JavaFunctions());
				
				String logscript = FileMan.readFromAssets("scripts/m_log.lua", ScriptEngine.ENCODING);
				luaVm.loadScript(logscript);
				String script = FileMan.readFromAssets("scripts/lua_test.lua", ScriptEngine.ENCODING);
				luaVm.loadScript(script);
				luaVm.call("callJavaFunction", null, null);
				
				super.run();
			}
        	
        }.start();
    }

    private class JavaFunctions{
    	public void doWithTableParam(LuaObject data){
    		MLog.d("----------> doWithTableParam");
    		try {
				String data1 = data.getLuaState().getLuaObject(data, "data1").getString();
				MLog.d("----------> data1" + data1);
			} catch (LuaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
