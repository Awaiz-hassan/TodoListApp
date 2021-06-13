package com.cc.room.Views;

import android.os.Bundle;

import com.cc.room.Adapters.TaskList;
import com.cc.room.Client.DataBaseClient;
import com.cc.room.Model.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.cc.room.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class Tasks extends AppCompatActivity {
    private Button saveTask;
    private TextInputEditText TaskTitle,TaskDesc;
    private FloatingActionButton fab;
    public static List<TaskModel> taskList;
    private RecyclerView recyclerView;
    public static TaskList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bindings();
        getAllTasks();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddTasks();
            }
        });
    }
    private void DialogAddTasks(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Tasks.this);
        View AddTaskDialogue= LayoutInflater.from(Tasks.this).inflate(R.layout.fragment_add_task,null);
        alertDialog.setView(AddTaskDialogue);
        TaskTitle=AddTaskDialogue.findViewById(R.id.Title);
        TaskDesc=AddTaskDialogue.findViewById(R.id.Desc);
        saveTask=AddTaskDialogue.findViewById(R.id.Save);
        AlertDialog taskAlertAddTask=alertDialog.create();
        taskAlertAddTask.show();
        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Tittle=TaskTitle.getText().toString();
                String Description=TaskDesc.getText().toString();
                if(Tittle.isEmpty()){
                    TaskTitle.requestFocus();
                    TaskTitle.setError("Title cannot be empty");
                    return;
                }
                else if(Description.isEmpty()){
                    TaskDesc.requestFocus();
                    TaskDesc.setError("Description cannot be Empty");
                    return;
                }
                TaskModel task= new TaskModel();
                task.setTask(Tittle);
                task.setDesc(Description);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataBaseClient.getInstance(Tasks.this).getTaskDataBase().taskDAO().AddTask(task);
                        new Handler(Looper.getMainLooper()).postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        taskList.add(task);
                                        adapter.notifyDataSetChanged();
                                    }
                                },1
                        );
                    }
                }).start();
                taskAlertAddTask.dismiss();
            }
        });
    }

    private void SetRecyclerView(){
        if(adapter==null){
            adapter=new TaskList(Tasks.this,taskList);
            recyclerView.setLayoutManager(new LinearLayoutManager(Tasks.this));
            recyclerView.setAdapter(adapter);
            ItemTouchHelper helper=new ItemTouchHelper(simpleCallback);
            helper.attachToRecyclerView(recyclerView);
            adapter.notifyDataSetChanged();
        }
    }
    private void getAllTasks(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                taskList=DataBaseClient.getInstance(Tasks.this).getTaskDataBase().taskDAO().getAllTasks();
                new Handler(Looper.getMainLooper()).postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                SetRecyclerView();
                            }
                        },1
                );
            }
        }).start();
    }
    private void bindings(){

        fab = findViewById(R.id.fab);
        recyclerView=findViewById(R.id.RecyclerView);
    }
    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    DataBaseClient.getInstance(Tasks.this).getTaskDataBase().taskDAO().RemoveTask(taskList.get(viewHolder.getAdapterPosition()));
                    new Handler(Looper.getMainLooper()).postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    taskList.remove(viewHolder.getAdapterPosition());
                                    adapter.notifyDataSetChanged();
                                }
                            },1
                    );
                }
            }).start();

        }
    };


}












