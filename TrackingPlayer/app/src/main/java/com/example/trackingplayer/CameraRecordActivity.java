package com.example.trackingplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraRecordActivity extends AppCompatActivity {

    MediaPlayer player;
    MediaRecorder recorder;
    private int btn_state;
    private CameraSurfaceView surface;
    private SurfaceHolder holder;
    private Button recordBtn;
    String filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_record);


        surface = new CameraSurfaceView(this);
        holder = surface.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        FrameLayout frame = (FrameLayout) findViewById(R.id.videoLayout);
        frame.addView(surface);

        recordBtn = (Button) findViewById(R.id.recordBtn);
        btn_state = 0;//stop

        recordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(btn_state == 0)
                    start_recorder();
                else
                    stop_recorder();
            }
        });
    }

    private void start_recorder() {
        recorder = new MediaRecorder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        filename = "/sdcard/"+dateFormat.format(new Date(System.currentTimeMillis()));
        surface.getCamera().unlock();
        recorder.setCamera(surface.getCamera());
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        recorder.setOrientationHint(90);
        recorder.setPreviewDisplay(holder.getSurface());
        recorder.setOutputFile(filename);

        try{
            recorder.prepare();
            recorder.start();
            btn_state = 1;
            recordBtn.setText("STOP");
        } catch (Exception e) {
            Log.e("SampleVideoRecorder","Exception: ", e);
            btn_state = 0;
            recorder.release();
            recorder=null;
        }
    }
    private void stop_recorder() {
        btn_state = 0;
        recorder.stop();
        recorder.reset();
        recorder.release();
        //recorder=null;
        recordBtn.setText("START");

        //save recorded video
        ContentValues values = new ContentValues(10);
        values.put(MediaStore.MediaColumns.TITLE, "RecordedVideo");
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis()/1000);
        values.put(MediaStore.MediaColumns.MIME_TYPE , "video/mp4");
        values.put(MediaStore.Audio.Media.DATA, filename);
        Uri videoUri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        if(videoUri==null){
            Log.d("SampleVideoRecorder", "Video insert failed");
            return;
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, videoUri));
    }
}
