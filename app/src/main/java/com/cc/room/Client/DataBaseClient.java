package com.cc.room.Client;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cc.room.DataBase.TaskDataBase;

public class DataBaseClient {
    private Context context;
    private static DataBaseClient instance;
    private final TaskDataBase taskDataBase;

    private DataBaseClient(Context context) {
        this.context = context;
        taskDataBase= Room.databaseBuilder(context,TaskDataBase.class,"TASK_LIST").build();
    }
    public static DataBaseClient getInstance(Context context){
        if(instance==null){
            instance=new DataBaseClient(context);
        }
        return instance;
    }

    public TaskDataBase getTaskDataBase(){
        return taskDataBase;
    }

}















