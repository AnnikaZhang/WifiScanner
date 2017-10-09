package com.example.annika.wifiscanner;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		//Bugly
	//	CrashReport.initCrashReport(getApplicationContext(), "900017049", false);
		CrashReport.initCrashReport(getApplicationContext(), "138b1f302e", true);
	}
}
