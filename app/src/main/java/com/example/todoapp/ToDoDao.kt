package com.example.todoapp

import androidx.room.*

// DAO - Data Access Objects
//CRUD - create , read , update, delete

@Dao
interface ToDoDao {

    @Insert
    suspend fun insertToDo(todo: ToDo) : Long

    @Query("SELECT * FROM " + ToDoDatabase.TABLE_NAME)
    suspend fun fetchList() : MutableList<ToDo>

    @Update
    suspend fun updateTodo(todo: ToDo)

    @Delete
    suspend fun deleteTodo(todo: ToDo)

}