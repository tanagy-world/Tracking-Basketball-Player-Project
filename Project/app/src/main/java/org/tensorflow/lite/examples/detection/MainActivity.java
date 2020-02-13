package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.tensorflow.lite.examples.detection.MotorControl.BluetoothMotorControl;

public class MainActivity extends AppCompatActivity {
    Button btn_camera;
    Button btn_bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_camera = (Button)findViewById(R.id.btn_Camera);
        btn_bluetooth = (Button)findViewById(R.id.btn_bluetooth);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnClickListener", "Camera Button Clicked!");
                // 액티비티 실행
                Intent i = new Intent(MainActivity.this, DetectorActivity.class);
                startActivity(i);
            }
        });

        btn_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnClickListener", "Bluetooth Button Clicked!");
                // 액티비티 실행
                Intent i = new Intent(MainActivity.this, BluetoothMotorControl.class);
                startActivity(i);
            }
        });

    }

}
