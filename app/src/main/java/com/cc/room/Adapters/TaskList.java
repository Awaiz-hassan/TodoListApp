package com.cc.room.Adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cc.room.Client.DataBaseClient;
import com.cc.room.DataBase.TaskDataBase;
import com.cc.room.Model.TaskModel;
import com.cc.room.R;
import com.cc.room.Views.Tasks;

import java.util.List;

public class TaskList extends RecyclerView.Adapter<TaskList.TaskViewHol> {

    private final Context context;
    private List<TaskModel> tasklist;

    public TaskList(Context context,List<TaskModel> tasklist) {

        this.context = context;
        this.tasklist=tasklist;
    }

    @NonNull
    @Override
    public TaskList.TaskViewHol onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_item_task,parent,false);
        return new TaskViewHol(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskList.TaskViewHol holder, int position) {
        String TaskTitle=tasklist.get(position).getTask();
        String TaskDesc=tasklist.get(position).getDesc();
        holder.title.setText(TaskTitle);
        holder.desc.setText(TaskDesc);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddTasks(holder,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(tasklist!=null)
        return tasklist.size();
        else return 0;
    }

    public static class TaskViewHol extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView desc;
        public TaskViewHol(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.list_item_title);
            desc=itemView.findViewById(R.id.list_item_desc);
        }
    }
    private void DialogAddTasks(TaskList.TaskViewHol holder, int position){
        TextView TaskTitle,TaskDesc;
        Button saveTask;
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        View AddTaskDialogue= LayoutInflater.from(context).inflate(R.layout.fragment_add_task,null);
        alertDialog.setView(AddTaskDialogue);
        TaskTitle=AddTaskDialogue.findViewById(R.id.Title);
        TaskDesc=AddTaskDialogue.findViewById(R.id.Desc);
        saveTask=AddTaskDialogue.findViewById(R.id.Save);
        TaskTitle.setText(tasklist.get(position).getTask());
        TaskDesc.setText(tasklist.get(position).getDesc());
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
                task.setPrimaryKey(tasklist.get(position).getPrimaryKey());
                task.setTask(Tittle);
                task.setDesc(Description);
                new Thread(() -> {
                    DataBaseClient.getInstance(context.getApplicationContext()).getTaskDataBase().taskDAO().UpdateTask(task);
                    new Handler(Looper.getMainLooper()).postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    holder.title.setText(task.getTask());
                                    holder.desc.setText(task.getDesc());
                                    tasklist.get(position).setTask(Tittle);
                                    tasklist.get(position).setDesc(Description);
                                }
                            },1
                    );
                }).start();
                taskAlertAddTask.dismiss();
            }
        });
    }

}
