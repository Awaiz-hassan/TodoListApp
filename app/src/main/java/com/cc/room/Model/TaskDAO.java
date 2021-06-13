package com.cc.room.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface TaskDAO {

    @Query("SELECT * FROM tasks")
    List<TaskModel> getAllTasks();

    @Insert
    void AddTask(TaskModel task);

    @Update
    void UpdateTask(TaskModel task);

    @Delete
    void RemoveTask(TaskModel task);
}
