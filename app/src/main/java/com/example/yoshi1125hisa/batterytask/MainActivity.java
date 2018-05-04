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
    private int health;
    private String technology = "";
    TextView scaleTextView;
    TextView tempTextView;
    TextView messageTextView;
    TextView healthTextView;
    TextView techTextView;


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

    public class PowerConnectionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

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
                health = intent.getIntExtra("health",0);
                technology = intent.getStringExtra("technology");



            }


            scaleTextView = findViewById(R.id.textView);
            tempTextView = findViewById(R.id.textView2);
            messageTextView = findViewById(R.id.textView3);
            healthTextView = findViewById(R.id.textView4);
            techTextView = findViewById(R.id.textView5);

            techTextView.setText(technology);
            scaleTextView.setText("バッテリー残量: "+level+"%");
            if (batteryTemperature <= 0){
                tempTextView.setText("バッテリー温度が取得できません");
            }else {
                tempTextView.setText("バッテリー温度: " + batteryTemperature + "℃");
            }
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;


            String batteryStatus = "";

            switch (status) {
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                   batteryStatus  = "充電状況が取得できません";
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    batteryStatus = "充電中です";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    batteryStatus = "充電していません";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    batteryStatus = "充電していません";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    batteryStatus = "充電完了です";
                    break;
            }

            String healthStatus = "";

            switch (health) {
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    healthStatus = "Unknown";
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthStatus = "Good";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthStatus = "Overheat";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthStatus = "Dead";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthStatus = "Voltage";
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthStatus = "Unspecified failure";
                    break;
            }

            healthTextView.setText(healthStatus);

            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            String chargeStatus = "";

            if(usbCharge){
             chargeStatus = "充電方法:USB";
            }else if(acCharge) {
            chargeStatus = "充電方法:AC";
            }else{
                chargeStatus = "取得できません";
            }

            if(isCharging) {
                messageTextView.setText(batteryStatus+" "+chargeStatus);
            }else{
            messageTextView.setText("充電していません");


            }
        }
    };
}
