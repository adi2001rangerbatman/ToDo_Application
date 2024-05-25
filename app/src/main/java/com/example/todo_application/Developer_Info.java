package com.example.todo_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Developer_Info extends AppCompatActivity {

    private ImageView back_btn;
    private Button ok_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        back_btn= findViewById(R.id.back);
        ok_btn = findViewById(R.id.ok_btn);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Developer_Info.this, More_Info.class);
                startActivity(intent);
                finish();
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Developer_Info.this, Home.class);
                startActivity(intent);
                finish();
            }
        });


    }
}