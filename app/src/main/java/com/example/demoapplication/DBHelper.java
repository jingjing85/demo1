package com.example.demoapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.fragment.app.FragmentActivity;


public class DBHelper extends SQLiteOpenHelper {
    Context context;

    public DBHelper(String name, Context context, int i){
        super(context, "demo.db", null, 1);

    }

    public DBHelper(Context context){
        super(context, "demo.db", null, 1);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table user(Id integer primary key autoincrement,username varchar(20), password varchar(20), repassword varchar(20))");
        sqLiteDatabase.execSQL("create Table information(Id integer primary key autoincrement,realname varchar(20), age integer, height Double, weight Double, diseasehistory String)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       sqLiteDatabase.execSQL("Drop Table if exists user");
        sqLiteDatabase.execSQL("Drop Table if exists information");
        onCreate(sqLiteDatabase);

    }

}
