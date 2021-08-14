package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateTodoActivity : AppCompatActivity() {
    lateinit var todoDatabse: ToDoDatabase
    lateinit var editText : EditText

    companion object{
        const val PREVIOUS_TODO = "PreviousTodo"
    }
    var isBeingUpdated = false
    var previousTodo : ToDo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)

        if(intent.hasExtra(PREVIOUS_TODO)){
            isBeingUpdated = true
            previousTodo = intent.extras?.get(PREVIOUS_TODO) as ToDo
        }

        todoDatabse = Room.databaseBuilder(applicationContext, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()

        editText = findViewById(R.id.todo_EditText)
        val saveButton : Button = findViewById(R.id.SaveButton)

        if(isBeingUpdated) {
            editText.setText(previousTodo?.name.toString())
        }

        saveButton.setOnClickListener{
            val enteredText = editText.text.toString()

            if(TextUtils.isEmpty(enteredText)){
                Toast.makeText(this, "Text cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(isBeingUpdated) {
                previousTodo?.let {
                    it.name = enteredText
                    updateRow(it)
                }

            }else{
                val todo = ToDo()
                todo.name = enteredText
                insertRow(todo)
            }

        }
    }

    private fun updateRow(todo: ToDo) {
        GlobalScope.launch(Dispatchers.IO) {
            todoDatabse.todoAppDao().updateTodo(todo)
            launch(Dispatchers.Main) {
                startMainActivity()
            }
        }
    }


    private fun insertRow(todo: ToDo) {
        GlobalScope.launch(Dispatchers.IO) {
            // this helps insert this data on background thread
            val id = todoDatabse.todoAppDao().insertToDo(todo)
            println(Thread.currentThread().name)

            launch(Dispatchers.Main) {
                // UI Related
                todo.todoId = id
                println(Thread.currentThread().name)

                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this@CreateTodoActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val enteredText = editText.text.toString()
        //Save the to-do on pressing back button
        if(TextUtils.isEmpty(enteredText).not()) {
            if(isBeingUpdated) {
                previousTodo?.let{
                    it.name = editText.text.toString()
                    updateRow(it)
                }
            }else{
                val todo = ToDo()
                todo.name = editText.text.toString()
                insertRow(todo)
            }
        }
        super.onBackPressed()
    }
}