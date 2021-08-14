package com.example.todoapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ToDo::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun todoAppDao() : ToDoDao

    companion object {
        const val DB_NAME = "to_do_db"
        const val TABLE_NAME = "todo"
    }

}