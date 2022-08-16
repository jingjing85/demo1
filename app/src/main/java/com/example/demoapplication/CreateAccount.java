package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    private Button btnRegister;
    private EditText editTextUserName, editTextPassword, editTextRepassword;
    private String userName, Pass, rePass;
    private SQLiteDatabase mDB;
    DBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        String context;
        DBHelper db = new DBHelper("information", this, 1);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();



        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepassword = findViewById(R.id.editTextRepassword);
        helper = new DBHelper("information", null, 1);
        mDB = helper.getWritableDatabase();

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper = new DBHelper("information", view.getContext(), 1);
                mDB = helper.getWritableDatabase();
                userName = editTextUserName.getText().toString();
                Pass = editTextPassword.getText().toString();
                rePass = editTextRepassword.getText().toString();
                if(userName.isEmpty()) {
                    Toast.makeText(CreateAccount.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Pass.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }else if(rePass.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Please reenter password ", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!Pass.equals(rePass)){
                    Toast.makeText(CreateAccount.this, "Two passwords are not equal", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Cursor cursor = mDB.query("user",null, "username = ?", new String[]{userName}, null, null, null);
                    if(cursor.getCount() != 0){
                        Toast.makeText(CreateAccount.this, "The username exists.", Toast.LENGTH_SHORT).show();
                    }else{
                        ContentValues cv = new ContentValues();
                        cv.put("username", userName);
                        cv.put("password", Pass);
                        cv.put("repassword", rePass);
                        long result = mDB.insert("user", null, cv);
                        if(result != 0){
                            Toast.makeText(CreateAccount.this, "Register successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateAccount.this, Login.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(CreateAccount.this, "Register failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

    }
}