package com.example.yoshi1125hisa.batterytask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int scale;
    private int level;
    TextView scaleTextView;
    TextView tempTextView;


    // フィールド
    float batteryTemperature;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // バッテリー温度を取得
            // BatteryManager.EXTRA_TEMPERATURE で取得できる値は ℃x10 なので、
            // 100 という値が取れた場合は 10.0℃ 。

            batteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10.0f;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //受信を開始
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver,filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //受信停止
        unregisterReceiver(myReceiver);
    }

    //受信機
    public BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                // 電池残量の最大値
                scale = intent.getIntExtra("scale", 0);
                // 電池残量
                level = intent.getIntExtra("level", 0);
            }
            scaleTextView = findViewById(R.id.textView);
            tempTextView = findViewById(R.id.textView2);
            scaleTextView.setText("バッテリー残量: "+level+"%");
            tempTextView.setText("バッテリー温度: "+batteryTemperature+"℃");
        }
    };
}
