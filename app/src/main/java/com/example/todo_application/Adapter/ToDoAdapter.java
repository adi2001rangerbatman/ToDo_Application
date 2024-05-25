package com.example.todo_application.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_application.AddNewTask;
import com.example.todo_application.Home;
import com.example.todo_application.Model.ToDoModel;
import com.example.todo_application.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> todoList;
    private Home home;
    private FirebaseFirestore firestore;

    public ToDoAdapter(Home mainActivity,List<ToDoModel>todoList){

        this.todoList = todoList;
        home = mainActivity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(home).inflate(R.layout.activity_todo_list_view,parent,false);
        firestore = FirebaseFirestore.getInstance();


        return new MyViewHolder(view);
    }

    //delete the task
    public void deleteTask(int position){
        ToDoModel toDoModel = todoList.get(position);
        firestore.collection("task").document(toDoModel.TaskID).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return home;
    }
    //edit the task
    public void editTask(int position){
        ToDoModel toDoModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("id",toDoModel.TaskID);


        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(home.getSupportFragmentManager(),addNewTask.getTag());



    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel = todoList.get(position);
        holder.checkBox.setText(toDoModel.getTask());
        holder.checkBox.setChecked(toBoolean(toDoModel.getStatus()));


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    firestore.collection("task").document(toDoModel.TaskID).update("status",1);

                }else{
                    firestore.collection("task").document(toDoModel.TaskID).update("status",0);
                }

            }
        });
    }
    private boolean toBoolean(int status){
        return status !=0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }

}
