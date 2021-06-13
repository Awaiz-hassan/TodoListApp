package com.cc.room.DataBase;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.cc.room.Model.TaskDAO;
import com.cc.room.Model.TaskModel;

@Database(entities = {TaskModel.class},version = 1)
public abstract class TaskDataBase extends RoomDatabase {
    public abstract TaskDAO taskDAO();

}
