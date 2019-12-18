/*
 * @author jy-jeon
 *
 * @details
 * Activity to record a video
 */


package com.example.trackingplayer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.InetAddresses;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.*;

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
                Intent intent = new Intent(MainActivity.this, BlueToothActivity.class);
                startActivity(intent);
            }
        });

    }
}
