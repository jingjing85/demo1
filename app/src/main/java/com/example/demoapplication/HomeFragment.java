package com.example.demoapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    //DBHelper DB;
    String TAG = "TAG";
    private Button btnEdit, btnDisplay;
    private SQLiteDatabase db;
    DBHelper helper;

    @Nullable
    @Override
    public View  onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);



        helper = new DBHelper("information",null,1);


        btnEdit = getActivity().findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper = new DBHelper( "information", view.getContext(), 1);
                db = helper.getWritableDatabase();
                Intent in = new Intent(getActivity(), personalInformation.class);
                startActivity(in);
            }
        });

        btnDisplay = getActivity().findViewById(R.id.btnDisplay);
        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), ShowInformation.class);
                startActivity(in);
            }
        });
    }
}

           /* @Override
            public void onClick(View view) {
               // db = helper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from information", null);
                if (cursor.getCount() == 0) {
                    Toast.makeText(getActivity(), "No information Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (cursor.moveToNext()) {
                    buffer.append("ID: " + cursor.getString(0) + "  ");
                    buffer.append("RealName: " + cursor.getString(1) + "  ");
                    buffer.append("Age: " + cursor.getString(2));
                    buffer.append("Height: " + cursor.getString(3));
                    buffer.append("Weight: " + cursor.getString(4));
                    buffer.append("diseasehistory: " + cursor.getString(5));
                    buffer.append("\n");
                }
                // Toast.makeText(MainActivity.this, "Total # of Contacts ::: " + Integer.toString(cursor.getCount()), Toast.LENGTH_SHORT).show();
                // textViewShow.setText(buffer.toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Personal Information");
                builder.setMessage(buffer.toString());
                builder.show();


               /* db = helper.getWritableDatabase();
                Query();
            }
        });
    }

    private void Query() {
        Cursor cursor = db.query("information", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String realname = cursor.getString(cursor.getColumnIndex("realname"));
                @SuppressLint("Range") String age = cursor.getString(cursor.getColumnIndex("age"));
                @SuppressLint("Range") String height = cursor.getString(cursor.getColumnIndex("height"));
                @SuppressLint("Range") String weight = cursor.getString(cursor.getColumnIndex("weight"));
                @SuppressLint("Range") String diseasehistory = cursor.getString(cursor.getColumnIndex("diseasehistory"));
                Log.i(TAG, "realname:" + realname);
                Log.i(TAG, "age:" + age);
                Log.i(TAG, "height:" + height);
                Log.i(TAG, "weight:" + weight);
                Log.i(TAG, "diseasehistory:" + diseasehistory);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }*/
