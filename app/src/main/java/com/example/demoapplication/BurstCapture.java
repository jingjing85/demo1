package com.example.demoapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;

import javax.security.auth.callback.Callback;

public class BurstCapture  extends AppCompatActivity {
    private static final String TAG = "BurstCapture";
    private Handler mHandler = new Handler();


    private String mPermissionDes;
    public AutoFitTextureView mAutoFitTextureView;
    public CameraManager mCameraManager;
    public Button mBurstCaptureBtn, stopTakingPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burst_capture);

        initView();
        requestPermission("ask permissions", null,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    public SurfaceTexture mSurfaceTexture;
    private CaptureBurstIn mCaptureBurst;
    private ImageReader mImageReader;
    private Surface mImageReaderSurface;

    private void initView() {
        mAutoFitTextureView = findViewById(R.id.preview_view);
        Size bestSize = getBestSize();
        if (bestSize != null) {
            mAutoFitTextureView.setAspectRation(bestSize.getHeight(), bestSize.getWidth());
        }
        mAutoFitTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                mSurfaceTexture = surface;
                if (mSurfaceTexture == null) {
                    Log.e(TAG, "======== surfaceTexture == null =========");
                    return;
                }
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

            }
        });
        mBurstCaptureBtn = findViewById(R.id.burstCaptureBTN);
        stopTakingPictures = findViewById(R.id.StopBTN);
        mImageReader = ImageReader.newInstance(bestSize.getHeight(), bestSize.getWidth(), ImageFormat.JPEG, 2);
        mImageReaderSurface = mImageReader.getSurface();
//        mBurstCaptureBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                CaptureBurstIn mBurstCapture = new CaptureBurstIn(mCameraDevice, mCaptureSession, mImageReader);
//            }
//        });



    }

    public void StartRepeatTakingPictures(View view) {
        mHandler.postDelayed(mToastRunnable, 1000);
    }

    public void StopTakingPictures(View view) {
        mHandler.removeCallbacks(mToastRunnable);
        Toast.makeText(BurstCapture.this, "Stop taking pictures", Toast.LENGTH_SHORT).show();

    }

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(BurstCapture.this, "Repeat taking pictures", Toast.LENGTH_SHORT).show();
            CaptureBurstIn mBurstCapture = new CaptureBurstIn(mCameraDevice, mCaptureSession, mImageReader);
            mHandler.postDelayed(this, 3000);

        }
    };


    private void openCamera() {
        try {
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCameraManager.openCamera("0", mCameraStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.d(TAG, "exception");
        }
    }
    private CameraDevice mCameraDevice;
    private Surface mPreviewSurface;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;
    private CameraCaptureSession mCaptureSession;
    private CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            Log.d(TAG, "run");
            mCameraDevice = camera;
            try {
                mPreviewSurface = new Surface(mSurfaceTexture);
                mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mPreviewRequestBuilder.addTarget(mPreviewSurface);
                mPreviewRequest = mPreviewRequestBuilder.build();
                mCameraDevice.createCaptureSession(Arrays.asList(mPreviewSurface, mImageReader.getSurface()), mSessionsStateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.d(TAG, "exception");
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            Log.d(TAG, "disconnect");
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.d(TAG, "error");
        }
    };

    private CameraCaptureSession.StateCallback mSessionsStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            if (null == mCameraDevice) {
                return;
            }
            mCaptureSession = session;

            try {
                mCaptureSession.setRepeatingRequest(mPreviewRequest, null, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.d(TAG, "exception");
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
            Log.d(TAG, "fail");
        }
    };

    private CameraCharacteristics mCharacteristics;
    private Size getBestSize() {
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        CameraCharacteristics characteristics = null;
        try {
            characteristics = cameraManager.getCameraCharacteristics("0");
            mCharacteristics = characteristics;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        int phone_width = point.x;
        int phone_height = point.y;
        Log.e(TAG, "phone_width = " + phone_width + ", phone_height = " + phone_height);

        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        ArrayList<Size> sizeList = new ArrayList<Size>(Arrays.asList(map.getOutputSizes(SurfaceTexture.class)));
        for (Size size : sizeList) {
            Log.e(TAG, "width = " + size.getWidth() + ", height = " + size.getHeight());
            double ratio = (double) size.getHeight() / (double) size.getWidth();
            int height = (int) (phone_width / ratio);
            Log.e(TAG, "ratio = " + ratio + ", height = " + height);
            if (height > phone_height) {
                continue;
            } else {
                return size;
            }
        }

        return null;
    }

    public void requestPermission(String permissionDes, Callback callback, @NonNull String... permissions){
        mPermissionDes = permissionDes;
        if (checkPermissions(permissions)){

        } else {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    public boolean checkPermissions(@NonNull String... permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return checkselfPermissions(permissions);
        }
        return true;
    }
    public boolean checkselfPermissions(@NonNull String... permissions){
        boolean granted = true;
        for (String permission : permissions){
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED){
                granted = false;
                break;
            }
        }
        return granted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean granted = true;
        for(int i = 0; i < grantResults.length; i++){
            if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                granted = false;
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){
                    showPromptDialog();
                }else {
//                    if (callback != null) callback.failed();
                }
                break;
            }
        }
        if (granted){
//            if (callback != null) callback.success();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showPromptDialog(){
        new AlertDialog
                .Builder(this)
                .setTitle("apply ")
                .setMessage(mPermissionDes)
                .setCancelable(false)
                .setPositiveButton("setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toAppSetting();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        if (callback != null) callback.failed();
                    }
                }).show();
    }

    public void toAppSetting(){
        Intent settingIntent = null;
        if (Build.VERSION.SDK_INT >= 9){
            settingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            settingIntent.setData(Uri.fromParts("package", getPackageName(), null));
            settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }else {
            settingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            settingIntent.setAction(Intent.ACTION_VIEW);
            settingIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            settingIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
            settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(settingIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}