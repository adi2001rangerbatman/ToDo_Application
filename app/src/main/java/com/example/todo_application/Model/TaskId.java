package com.example.todo_application.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class TaskId {

    @Exclude
    public  String TaskID;

    public <T extends TaskId> T withId(@NonNull final String id){
        this.TaskID = id;
        return (T) this;
    }
}
