package com.example.annika.wifiscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Service;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.net.wifi.ScanResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainService extends Service {
    public  List<Map<String,String>>mlist = new ArrayList<Map<String,String>>();
    private static List<ScanResult> resultList = new ArrayList<ScanResult>();
    private WifiManager mwifiManager;
    @Override
    public IBinder onBind(Intent agr0){
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mwifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }
    private final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                resultList = mwifiManager.getScanResults();

//                for (ScanResult scanResult : resultList) {
//                    Map<String,String> map = new HashMap<String, String>();
//                    map.put("wifi_name",scanResult.SSID);
//                    map.put("wifi_bssid",scanResult.BSSID);
//                    mlist.add(map);
//                }
                Intent broadCastIntent = new Intent();
                broadCastIntent.putExtra("result",(Serializable) resultList);
                broadCastIntent.setAction("com.androidwifi.opensuccess");
                sendBroadcast(broadCastIntent);
                mwifiManager.startScan();
            }

        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mwifiManager.isWifiEnabled()) {
            mwifiManager.setWifiEnabled(true);
        }
            mwifiManager.startScan();
            //开始搜索，当搜索到可用的wifi时，将发送WifiManager.SCAN_RESULTS_AVAILABLE_ACTION的广播
          return START_STICKY;
        }


    @Override
    public void onDestroy(){
        unregisterReceiver(wifiReceiver);
        super.onDestroy();
    }
}
