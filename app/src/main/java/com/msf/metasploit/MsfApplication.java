package com.msf.metasploit;


import android.app.Application;

import com.msf.metasploit.rpc.MsfController;
import com.msf.metasploit.rpc.MsfMain;

public class MsfApplication extends Application {
	
	private static MsfApplication sInstance;

	private static MsfMain msfMain;
	private static MsfController msfController;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		msfMain = new MsfMain();
		msfController = new MsfController(msfMain);
	}

	public static MsfApplication getApplication() {
		return sInstance;
	}

	public static MsfMain getMsfMain() {
		return msfMain;
	}

	public static MsfController getMsfController() {
		return msfController;
	}
}
