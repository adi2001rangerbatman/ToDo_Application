package com.example.todo_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class More_Info extends AppCompatActivity {

    private ImageView back_btn,developer_info,user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        back_btn = findViewById(R.id.back);
        developer_info = findViewById(R.id.developer_info_btn);
        user_info = findViewById(R.id.user_info_btn);



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(More_Info.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        developer_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(More_Info.this, Developer_Info.class);
                startActivity(intent);
                finish();
            }
        });

        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(More_Info.this, User_info.class);
                startActivity(intent);
                finish();
            }
        });
    }

}