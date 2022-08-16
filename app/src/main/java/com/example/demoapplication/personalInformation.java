package com.example.demoapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class personalInformation extends AppCompatActivity {
    private Button btnSubmit;
    private String realName, age, height, weight, diseasehistory;
    private EditText editTextRealName, editTextAge, editTextHeight, editTextWeight, editTextDisease;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        DBHelper db = new DBHelper("information", this, 1);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        editTextRealName = findViewById(R.id.editTextRealName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextDisease = findViewById(R.id.editTextDisease);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realName = editTextRealName.getText().toString();
                age = editTextAge.getText().toString();
                height = editTextHeight.getText().toString();
                weight = editTextWeight.getText().toString();
                diseasehistory = editTextDisease.getText().toString();

                if(realName.isEmpty()) {
                    Toast.makeText(personalInformation.this, "Please enter real name", Toast.LENGTH_SHORT).show();
                    return;
                }else if(age.isEmpty()){
                    Toast.makeText(personalInformation.this, "Please enter age", Toast.LENGTH_SHORT).show();
                    return;
                }else if(height.isEmpty()){
                    Toast.makeText(personalInformation.this, "Please enter height", Toast.LENGTH_SHORT).show();
                    return;
                }else if(weight.isEmpty()){
                    Toast.makeText(personalInformation.this, "Please enter weight", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Cursor cursor = sqLiteDatabase.query("information", null, "realName = ?", new String[]{realName}, null, null, null);
                    if (cursor.getCount() != 0) {
                        Toast.makeText(personalInformation.this, "The name exists.", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put("realName", realName);
                        cv.put("age", age);
                         cv.put("height", height);
                        cv.put("weight", weight);
                         cv.put("diseasehistory", diseasehistory);
                        long result = sqLiteDatabase.insert("information", null, cv);
                        if (result != 0) {
                            Toast.makeText(personalInformation.this, "submit successfully!", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(personalInformation.this, MainActivity.class);
                            startActivity(in);
                        } else {
                            Toast.makeText(personalInformation.this, "submit failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });





    }
}
