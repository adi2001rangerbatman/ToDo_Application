package com.example.todo_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    public static final String TAG = "TAG";
    private EditText username,password,mobile_no;
     private Button register;
     ProgressBar progressBar;
     FirebaseAuth auth;
     FirebaseFirestore firestore;
     String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        username = findViewById(R.id.create_username);
        password = findViewById(R.id.create_password);
        register = findViewById(R.id.register_btn);
        mobile_no = findViewById(R.id.phone);
       // progressBar = findViewById(R.id.signup_progressBar);


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //already logged in checking
        if (auth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String create_username = username.getText().toString().trim();
                String create_password = password.getText().toString().trim();

                String userE =  username.getText().toString();
                String phone = mobile_no.getText().toString();

                // Credential Validation
                if(TextUtils.isEmpty(create_username)){
                    username.setError("Username is Required !");
                    return;
                }
                if(TextUtils.isEmpty(create_password)){
                    password.setError("Password is Required!");
                    return;
                }
                if(password.length()<6){
                    password.setError("Password must be at least 6 Characters");
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    mobile_no.setError("Mobile Number is Required!");
                    return;
                }


                //progressBar.setVisibility(View.VISIBLE);


                // Register the user in firebase
                auth.createUserWithEmailAndPassword(create_username,create_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this, "User Registration Successfully", Toast.LENGTH_SHORT).show();

                            UID=auth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("users").document(UID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("User_Name",userE);
                            user.put("Mobile_No",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"onSuccess: User Profile is Created for " + UID);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),Home.class));
                        }
                        else{
                            Toast.makeText(Signup.this, "Registration Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

    }
}