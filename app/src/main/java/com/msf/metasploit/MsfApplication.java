package com.msf.metasploit;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.msf.metasploit.model.MsfMain;

public class MsfApplication extends Application {
	
	private static MsfApplication sInstance;
	private static MsfMain msfMain;

	private SharedPreferences sharedPreferences;

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);

		sInstance = this;
		msfMain = new MsfMain(this);
	}

	public static MsfApplication getApplication() {
		return sInstance;
	}

	public static MsfMain getMsfMain() {
		return msfMain;
	}
	
}
