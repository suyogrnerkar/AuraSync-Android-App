package com.axotix.aurasync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.IntentFilter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class SenseActivity extends Activity {

  private String maxRange, lightSensorReading;
  TextView textLIGHT_available, textLIGHT_reading, textWIFI_available;
  private List<HardwareObject> lSensor;
  private String API_URL = "http://192.168.2.5:3000/api/data";
  JSONObject jsonParam = new JSONObject();
  private String wifi;
  final Handler handler = new Handler();
  final int delay = 2000; //milliseconds
  Runnable runnable =new Runnable(){
    public void run(){
      sendPost(API_URL);
      Log.i("JSON", jsonParam.toString());
      handler.postDelayed(this, delay);
    }
  };

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  private BroadcastReceiver WifiReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      int WifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
          WifiManager.WIFI_STATE_UNKNOWN);

      switch (WifiState) {
        case WifiManager.WIFI_STATE_ENABLED: {
          setContentView(R.layout.activity_sense);

          if(!isOnline(context)) {
            Toast.makeText(context, "Waiting for connectivity !", Toast.LENGTH_SHORT).show();
            finish();
          }

          textWIFI_available = (TextView)findViewById(R.id.WIFI_available);
          wifi = ((WifiManager)  getApplicationContext()
              .getSystemService(Context.WIFI_SERVICE))
              .getConnectionInfo().getSSID().replaceAll("^\"|\"$", "");;
          textWIFI_available.setText("WIFI: " + wifi);

          try {
            jsonParam.put("wifi", wifi);
          } catch (JSONException e) {
            e.printStackTrace();
          }

          textLIGHT_available = (TextView)findViewById(R.id.LIGHT_available);
          textLIGHT_reading = (TextView)findViewById(R.id.LIGHT_reading);
          SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
          Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
          if (LightSensor == null) {
            Toast.makeText(context, "The device has no light sensor !", Toast.LENGTH_SHORT).show();
            finish();
          }
          else {

            maxRange = String.valueOf(LightSensor.getResolution());
            mySensorManager.registerListener(LightSensorListener, LightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            textLIGHT_available.setText("Light Sensor Available: " + LightSensor.getVendor() +
                "\nSensor Name:  " + LightSensor.getName() +
                "\nSensor Resolution:  " + LightSensor.getResolution() +
                "\nSensor Type:  " + LightSensor.getType()+
                "\nSensor Reporting Mode:  " + LightSensor.getReportingMode() +
                "\nSensor Min Delay:  " + LightSensor.getMinDelay() +
                "\nSensor Max Delay:  " + LightSensor.getMaxDelay() +
                "\nSensor Version:  " + LightSensor.getVersion() +
                "\nSensor Range:  " + LightSensor.getMaximumRange() +
                "\nSensor Power: " + LightSensor.getPower());

          }
        }
        break;

        case WifiManager.WIFI_STATE_ENABLING: {
          Toast.makeText(context,"Wifi enabling",Toast.LENGTH_LONG).show();

        }
        break;
        case WifiManager.WIFI_STATE_DISABLED: {
          Toast.makeText(context,"Wifi is disabled, please enable wifi and try again. " +
              "Need an active WIFI connection for the app to function.",Toast.LENGTH_LONG).show();
          finish();
        }
        break;

        case WifiManager.WIFI_STATE_DISABLING: {
        }
        break;

        case WifiManager.WIFI_STATE_UNKNOWN: {
        }
        break;

      }
    }
  };

  public boolean isOnline(Context context) {

    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    //should check null because in airplane mode it will be null
    return (netInfo != null && netInfo.isConnected());
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  protected void onResume(){
    super.onResume();
    registerReceiver(WifiReceiver, new IntentFilter( WifiManager.WIFI_STATE_CHANGED_ACTION));
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  protected void onPause(){
    super.onPause();
    unregisterReceiver(WifiReceiver);
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    handler.postDelayed(runnable, delay);
  }

  public void onStop () {
    handler.removeCallbacks(runnable);
    super.onStop();
  }

  public void sendPost(final String API_URL) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          URL url = new URL(API_URL);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("POST");
          conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
          conn.setRequestProperty("Accept","application/json");
          conn.setDoOutput(true);
          conn.setDoInput(true);

          Log.i("JSON", jsonParam.toString());
          DataOutputStream os = new DataOutputStream(conn.getOutputStream());
          //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
          os.writeBytes(jsonParam.toString());

          os.flush();
          os.close();

          Log.i("STATUS", String.valueOf(conn.getResponseCode()));
          Log.i("MSG" , conn.getResponseMessage());

          conn.disconnect();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    thread.start();
  }

  private final SensorEventListener LightSensorListener
      = new SensorEventListener(){

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
      if(event.sensor.getType() == Sensor.TYPE_LIGHT){
        // int newValue = (int) (255f * value / maxValue);
        lightSensorReading = String.valueOf(((40000 - event.values[0])/10)/4);
        lSensor = new ArrayList<HardwareObject>();
        lSensor.add(new HardwareObject("Max Range", maxRange));
        textLIGHT_reading.setText("Luminosity : " + lightSensorReading);
        try {
          jsonParam.put("luminosity", lightSensorReading);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }


  };

}