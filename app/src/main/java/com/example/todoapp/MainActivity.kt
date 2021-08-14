package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.todoapp.CreateTodoActivity.Companion.PREVIOUS_TODO
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ToDoInterface {


    lateinit var recyclerView: RecyclerView
    lateinit var todoAdapter: ToDoAdapter
    lateinit var todoDatabase: ToDoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoDatabase = Room.databaseBuilder(applicationContext, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()
        var list : MutableList<ToDo> = mutableListOf()

        val floating_button : FloatingActionButton= findViewById(R.id.create_todo_button)
        recyclerView = findViewById(R.id.rec_view)
        todoAdapter = ToDoAdapter(this, list, this)

        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0 && floating_button.visibility == View.VISIBLE){
                    floating_button.hide()
                }else if (dy < 0 && floating_button.visibility == View.GONE){
                    floating_button.show()
                }
            }
        })

        floating_button.setOnClickListener{
            val intent1 = Intent(this, CreateTodoActivity::class.java)
            startActivity(intent1)
        }

        fetch_todo_list()
    }



    private fun fetch_todo_list() {
        GlobalScope.launch(Dispatchers.IO) {
            val todoList = todoDatabase.todoAppDao().fetchList()

            launch(Dispatchers.Main) {
                todoAdapter.setList(todoList)
                println(todoList.size)
            }
        }
    }

    override fun updateTodoText(todo : ToDo) {
        val intent = Intent(this, CreateTodoActivity::class.java)
        intent.putExtra(PREVIOUS_TODO, todo)
        startActivity(intent)
    }

    override fun deleteTodoText(todo: ToDo, position : Int) {
        GlobalScope.launch(Dispatchers.IO) {

            val pos =
            todoDatabase.todoAppDao().deleteTodo(todo)

            launch(Dispatchers.Main) {
                todoAdapter.todoList.remove(todo)
                todoAdapter.notifyItemRemoved(position)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem : MenuItem? = menu?.findItem(R.id.search_icon)
        val searchView : SearchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                newText?.let{
                    searchTodos(it)
                }
                return true
            }
        })
        return true
    }

    private fun searchTodos(newText: String) {
         GlobalScope.launch(Dispatchers.IO) {
             val list = todoDatabase.todoAppDao().fetchList()

             launch(Dispatchers.Main) {
                 val filteredList = filter(list, newText)

                 todoAdapter.setList(filteredList)
                 recyclerView.scrollToPosition(0)
             }

         }
    }

    private fun filter(list : List<ToDo>, newText: String) : MutableList<ToDo> {

        val lowerCaseText = newText.toLowerCase()
        val filteredList : MutableList<ToDo> = mutableListOf()

        for(item in list) {
            val text = item.name?.toLowerCase()

            if(text?.contains(lowerCaseText) == true) {
                filteredList.add(item)
            }
        }
        return filteredList
    }
}