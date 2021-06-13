package com.cc.room.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tasks")
public class TaskModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int primaryKey;
    @ColumnInfo(name = "task")
    private String Task;
    @ColumnInfo(name = "desc")
    private String Desc;

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setTask(String task) {
        Task = task;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getTask() {
        return Task;
    }

    public String getDesc() {
        return Desc;
    }
}
