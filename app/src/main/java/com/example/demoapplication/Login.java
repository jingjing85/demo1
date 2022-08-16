package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private TextView editTextUserName,editTextPassword;
    private Button btnConfirm;
    private String userName, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DBHelper db = new DBHelper("information", this, 1);
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = editTextUserName.getText().toString();
                pass = editTextPassword.getText().toString();

                if(userName.isEmpty()){
                    Toast.makeText(Login.this,"Please enter your username", Toast.LENGTH_SHORT).show();
                    return;
                }else if(pass.isEmpty()){
                    Toast.makeText(Login.this,"Please enter your password", Toast.LENGTH_SHORT).show();
                }
                else{

                    Cursor cursor = sqLiteDatabase.rawQuery("select * from user where username = ?", new String[]{userName});
                    if(cursor.getCount() == 0){
                        Toast.makeText(Login.this,"The user doesn't exist. Please register first.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    while (cursor.moveToNext()){
                       String pwd = cursor.getString(2);
                       if (pwd.equals(pass)){
                           Toast.makeText(Login.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(Login.this, MainActivity.class);
                           startActivity(intent);
                       }else{
                           Toast.makeText(Login.this, "The password isn't correct.", Toast.LENGTH_SHORT).show();
                       }
                    }



                   /* Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user WHERE username=? AND password=?", new String[]{userName, pass});
                    if(cursor.getCount() > 0){
                        Toast.makeText(Login.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, TakePhotos.class);
                        startActivity(intent);
                        }
                    else{
                            Toast.makeText(Login.this, "Login failed!", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                }
        });
    }
}