/*
 * @author jy-jeon
 *
 * @details
 * Activity to record a video
 */


package com.example.trackingplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity  extends Activity {

    private Button recordBtn;
    private Button bluetoothBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordBtn = (Button) findViewById(R.id.recordBtn);
        bluetoothBtn = (Button)findViewById(R.id.blueToothBtn);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this, CameraRecordActivity.class);
                startActivity(intent);

            }
        });

        bluetoothBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, BLTMotorControl.class);
                startActivity(intent);
            }
        });

    }
}
