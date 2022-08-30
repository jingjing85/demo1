package com.example.demoapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.hardware.Camera.PictureCallback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;


import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;


public class TakePictures extends Activity implements SurfaceHolder.Callback,
        View.OnClickListener, PictureCallback{

    private static final String CAMERA_CONTROLL = "CAMERA_CONTROLL";
    private Button btnTakePhoto;
    private Camera camera;
    private SurfaceView imageSView;
    private SurfaceHolder surfaceHolder;
    private Handler timerUpdateHandler;
    private boolean timerRunning = false;
    private int currentTimer = 10;
    private static int RUN_time=1500;

    public void onPictureTaken(byte[] data, Camera camera) {
        // TODO Auto-generated method stub
        Uri imageFileUri = getContentResolver().insert(  Media.EXTERNAL_CONTENT_URI, new ContentValues());
        String pathpicture=  getRealFilePath( TakePictures.this, imageFileUri );
        Log.e("PicDir", pathpicture);
        Toast.makeText(TakePictures.this, pathpicture, Toast.LENGTH_LONG)
                .show();


        try {
            OutputStream imageFileOS = getContentResolver().openOutputStream(
                    imageFileUri);

            imageFileOS.write(data);
            imageFileOS.flush();
            imageFileOS.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        camera.startPreview();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pictures);

        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        imageSView = findViewById(R.id.mSurfaceView);
        surfaceHolder = imageSView.getHolder();

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        surfaceHolder.addCallback(this);

//        startButton.setOnClickListener(this);

        timerUpdateHandler = new Handler();

        thread.start();
        thread1.start();
    }


    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            int i=5;
            while( i>0)
            {
                i--;
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(RUN_time);

                    currentTimer = 2;
                    if (!timerRunning) {
                        timerRunning = true;
                        timerUpdateHandler.post(timerUpdateTask);  }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
    });

    private Runnable timerUpdateTask = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (currentTimer > 1) {
                currentTimer--;
                timerUpdateHandler.postDelayed(timerUpdateTask, 1000);
            } else {
                camera.takePicture(null, null, null, TakePictures.this);
                timerRunning = false;
                currentTimer = 10;
            }
        }
    };

    Thread thread1 = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            TakePictures.this.onDestroy();
        }
    });


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        try {
            camera = Camera.open(0);
        } catch (Exception e) {
            Log.e(CAMERA_CONTROLL, e.getMessage());
        }
        try {
            camera.setPreviewDisplay(holder);
            Camera.Parameters parameters = camera.getParameters();
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
            }
            List<String> colorEffects = parameters.getSupportedColorEffects();
            Iterator<String> cei = colorEffects.iterator();
            while (cei.hasNext()) {
                String currentEffect = cei.next();
                if (currentEffect.equals(Camera.Parameters.EFFECT_SOLARIZE)) {
                    parameters
                            .setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
                    break;
                }
            }
            camera.setParameters(parameters);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            camera.release();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();

    }

    String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    @Override
    public void onClick(View view) {

    }

    public void onDestroy() {

        super.onDestroy();
        finish();
    }
}
