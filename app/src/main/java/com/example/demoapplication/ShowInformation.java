package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ShowInformation extends AppCompatActivity {
    String TAG = "TAG";
    private SQLiteDatabase db;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_information);

        Cursor cursor = db.rawQuery("select * from information", null);
        if (cursor.getCount() == 0) {
            Toast.makeText(ShowInformation.this,"No information Exists", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowInformation.this);
        builder.setCancelable(true);
        builder.setTitle("Personal Information");
        builder.setMessage(buffer.toString());
        builder.show();
        db = helper.getWritableDatabase();
        Query();
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
    }





}