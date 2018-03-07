package com.axotix.aurasync;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
  Button button;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Get the view from activity_main.xml
    setContentView(R.layout.activity_main);

    // Locate the button in activity_main.xml
    button = (Button) findViewById(R.id.startSensingButton);

    // Capture button clicks
    button.setOnClickListener(new OnClickListener() {
      public void onClick(View arg0) {

        // Start NewActivity.class
        Intent myIntent = new Intent(MainActivity.this, SenseActivity.class);
        startActivity(myIntent);
      }
    });
  }
}