package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ToDo")
class ToDo: Serializable {

    @PrimaryKey(autoGenerate = true)
    var todoId: Long? = null

    var name : String? = null

}
