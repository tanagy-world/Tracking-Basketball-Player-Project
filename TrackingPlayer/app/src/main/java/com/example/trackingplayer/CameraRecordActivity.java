/*
 * @author jy-jeon
 *
 * @details
 * Activity to record a video
 */


package com.example.trackingplayer;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraRecordActivity  extends AppCompatActivity implements OnRequestPermissionsResultCallback{

    MediaRecorder recorder;
    private int is_recording;
    private CameraSurfaceView surface;
    private SurfaceHolder holder;
    private Button recordBtn;
    String filename;
    final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TrackingBasketball";
    private final int PERMISSIONS_REQUEST_CODE = 100;
    private final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_record);
        checkPermission();

        recordBtn = (Button) findViewById(R.id.recordBtn);
        is_recording = 0;//stop

        recordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(is_recording == 0)
                    startRecorder();
                else
                    stopRecorder();
            }
        });
    }

    /*
     * Displays a preview on the screen, when not recording.
     */
    private void startPreview(){
        surface = new CameraSurfaceView(this);
        holder = surface.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        FrameLayout frame = (FrameLayout) findViewById(R.id.videoLayout);
        frame.addView(surface);
    }

    /*
     * Function to start recording.
     * called when the start button is pressed.
     */
    private void startRecorder() {
        recorder = new MediaRecorder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        File file = new File(path);
        if(!file.exists())
            file.mkdirs();

        filename = path+"/"+dateFormat.format(new Date(System.currentTimeMillis()))+".mp4";
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
            is_recording = 1;
            recordBtn.setText("STOP");
        } catch (Exception e) {
            Log.e("SampleVideoRecorder","Exception: ", e);
            is_recording = 0;
            recorder.release();
            recorder=null;
        }
    }

    /*
     * Function to stop recording.
     * called when the stop button is pressed.
     */
    private void stopRecorder() {
        is_recording = 0;
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder=null;
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

    /*
     * Check app has the required permissions and requests the missing permissions.
     */
    private void checkPermission(){
        int permission_cnt=0;
        for(String cur_permission:REQUIRED_PERMISSIONS)
            if(ContextCompat.checkSelfPermission(this, cur_permission)==PackageManager.PERMISSION_GRANTED)
                permission_cnt++;
        if ( permission_cnt==3) {
            // already have all permissions
            startPreview();
        }
        ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
    }

    /*
     * Callback function called when user responds to requested permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        if ( requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if ( check_result ) {
                startPreview();
            }
        }
    }
}
