package com.example.annika.wifiscanner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private  ListView msearchList;
    private TextView textView;
    private  Button start,stop;
    private  WifisAdapter wifisAdapter;
    List<ScanResult> results;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final BroadcastReceiver WifiReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.androidwifi.opensuccess")) {
                results=(List<ScanResult>) intent.getSerializableExtra("result");
                wifisAdapter=new WifisAdapter(results);
                msearchList.setAdapter(wifisAdapter);
                Message msg=new Message();
                msg.what=0x123;
                mHandler.sendMessage(msg);

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Connect the devices from activity_main.xml
        start= (Button) findViewById(R.id.start_scan);
        stop= (Button) findViewById(R.id.stop_scan);
        msearchList = (ListView) findViewById(R.id.resultlist);
        textView=(TextView)findViewById(R.id.notice);
        // Register WifiReceiver
        registerReceiver(WifiReceiver,new IntentFilter("com.androidwifi.opensuccess"));
        // Create Intent to call service
        final Intent intent =new Intent(this , MainService.class);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                startService(intent);
                textView.setText("开始---！");
            }
        });
        stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                stopService(intent);
                textView.setText("停止---！");
            }
        });

    }
    Handler mHandler =new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case 0x123:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg=new Message();
                            msg.what=0x124;
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                try{
                                File targetFile = new File(Environment.getExternalStorageDirectory() + "/wifiscanner.txt");
                                FileOutputStream outputStream = new FileOutputStream(targetFile, true);
                                    for (ScanResult result : results) {
                                        outputStream.write((simpleDateFormat.format(new Date())+"\t"+result.SSID+"\t"+result.BSSID+"\n").getBytes());
                                    }
                                    outputStream.close();

                                    msg.obj= "文件编写成功";
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {

                                msg.obj= "文件编写失败";


                            }
                            mHandler.sendMessage(msg);

                        }
                    }).start();
                    break;
                case 0x124:
                    textView.setText(simpleDateFormat.format(new Date())+"-"+(String) msg.obj);

                    textView.setText(simpleDateFormat.format(new Date())+"-"+(String) msg.obj);
                    break;
                default:
                    break;
            }


        }
    };


}
