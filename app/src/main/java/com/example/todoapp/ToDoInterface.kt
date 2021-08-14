package com.example.todoapp

interface ToDoInterface {
    fun updateTodoText(todo: ToDo)
    fun deleteTodoText(todo: ToDo, position : Int)
}