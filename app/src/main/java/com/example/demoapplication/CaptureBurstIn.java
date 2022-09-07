package com.example.demoapplication;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.view.Surface;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CaptureBurstIn {
    CaptureRequest.Builder mStillBuilder = null;
    CameraCaptureSession mCaptureSession = null;
    CameraDevice mCameraDevice = null;
    Surface mImageSurface;
    ImageReader mImageReader;
    public CaptureBurstIn(CameraDevice cameraDevice, CameraCaptureSession captureSession, ImageReader imageReader){
        mCameraDevice = cameraDevice;
        mCaptureSession = captureSession;
        mImageReader = imageReader;
        mImageSurface= mImageReader.getSurface();
        mImageReader.setOnImageAvailableListener(mReaderImgListener, null);
        try {
            mStillBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            mStillBuilder.set(CaptureRequest.CONTROL_CAPTURE_INTENT, CaptureRequest.CONTROL_CAPTURE_INTENT_STILL_CAPTURE);
            mStillBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
            mStillBuilder.set(CaptureRequest.NOISE_REDUCTION_MODE, CaptureRequest.NOISE_REDUCTION_MODE_OFF);
            mStillBuilder.set(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE_OFF);
            mStillBuilder.set(CaptureRequest.EDGE_MODE, CaptureRequest.EDGE_MODE_OFF);
            mStillBuilder.addTarget(mImageSurface);
            CaptureRequest request = mStillBuilder.build();
            List<CaptureRequest> requests = new ArrayList<>();
            for(int i=0;i<30;i++)
                requests.add(request);
            int sequenceId = mCaptureSession.captureBurst(requests, null, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private int imageIndex = 1;
    private void writeImageToFile() {
//        String filePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/001.jpg";
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/"+"burst_"+ System.currentTimeMillis()+"_" + imageIndex +".jpg";
        imageIndex++;
        Image image = mImageReader.acquireNextImage();
        if (image == null) {
            return;
        }

//       rootBuilder.setParam(CaptureRequest.JPEG_ORIENTATION, 90);    //旋转90度

        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filePath));
            fos.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                fos = null;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                image.close();
                image = null;
            }
        }
    }

    ImageReader.OnImageAvailableListener mReaderImgListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            writeImageToFile();
        }
    };
}
