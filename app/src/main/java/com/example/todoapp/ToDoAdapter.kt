package com.example.todoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter(var context : Context, var todoList : MutableList<ToDo>, val toDoInterface: ToDoInterface ) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var todo_title : TextView = itemView.findViewById(R.id.todo_title)
        val delete_button : ImageView = itemView.findViewById(R.id.delete_button)
        val edit_button : ImageView = itemView.findViewById(R.id.edit_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return ToDoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        var todo = todoList[position]
        holder.todo_title.text = todo.name

        holder.edit_button.setOnClickListener {
            toDoInterface.updateTodoText(todo)
        }
        holder.delete_button.setOnClickListener {
            toDoInterface.deleteTodoText(todo, position)
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun setList(list : MutableList<ToDo>) {
        todoList = list
        notifyDataSetChanged()
    }



}