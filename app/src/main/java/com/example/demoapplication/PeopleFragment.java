package com.example.demoapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PeopleFragment extends Fragment {
    private static final int TAKE_PHOTO = 1;
    private static final int TAKE_VIDEO = 2;
    private Button btnTakePhoto, btnTakeVideo, share, btnSelectVideo;
    private ImageView showPhoto;
    private TextView Phone;
    private Uri imageUri;
    private String strVideoPath = "";


    private boolean isShooting = false;//
    private int shooting_num = 0;//

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        showPhoto = view.findViewById(R.id.showPhoto);
        Phone = view.findViewById(R.id.Phone);
        share = view.findViewById(R.id.btnSharing);

        btnSelectVideo = view.findViewById(R.id.btnSelectVideo);

        btnTakeVideo = view.findViewById(R.id.btnTakeVideo);

//        btnSelectPhoto = view.findViewById(R.id.btnSelectPhoto);
//        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(getActivity(), TakePictures.class);
//                startActivity(in);
//            }
//        });

        Button btnTakePhoto = view.findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, TAKE_PHOTO);
                } else {
//                      openCamera();
//                    Intent in = new Intent(getActivity(), TakePictures.class);
                    Intent in = new Intent(getActivity(), BurstCapture.class);
                    startActivity(in);
                }
            }
//            private void openCamera() {
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.TITLE, "new picture");
//                values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
//                imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
//                Intent intent  = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(intent, 1);
//                startActivity(intent);
//
//
//            }
        });


        showPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 6);

            }
        });

        btnTakeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, TAKE_VIDEO);
                } else {
                    videoMethod();
                }
            }
            private void videoMethod(){
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(intent, TAKE_VIDEO);
            }
        });

        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonenum = "10086";
                callPhone(phonenum);
                Phone.setText(phonenum);
            }
        });
        return view;
    }
    public void callPhone(String phoneNum){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.CALL_PHONE};
            for (String str : permissions) {
                if (getActivity().checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}

