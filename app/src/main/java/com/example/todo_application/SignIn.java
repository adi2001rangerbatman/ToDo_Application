package com.example.todo_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private EditText username,password;
    private TextView register;
    private Button login;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.login_user);
        password = findViewById(R.id.login_password);
        register = findViewById(R.id.create_acc_txt);
        login = findViewById(R.id.login_btn);
        //progressBar =findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login_username = username.getText().toString().trim();
                String login_password = password.getText().toString().trim();

                // Credential Validation
                if(TextUtils.isEmpty(login_username)){
                    username.setError("Username is Required !");
                    return;
                }
                if(TextUtils.isEmpty(login_password)){
                    password.setError("Password is Required!");
                    return;
                }
                if(password.length()<6){
                    password.setError("Password must be at least 6 Characters");
                    return;
                }
                // authenticate the user

                auth.signInWithEmailAndPassword(login_username,login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignIn.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Home.class));
                        }else {

                            Toast.makeText(SignIn.this, "Logging Error !"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent=new Intent(SignIn.this,Signup.class);
                startActivity(signupintent);
                finish();
            }
        });




    }
}