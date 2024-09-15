package com.example.promodoro_team_21.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title : String,
    var category : Category,
    var createdAt : Date,
    var isChecked : Boolean = false
)

//Enum für privat, arbeit, uni. Für das laden der todos in den entsprechenden Tabs
enum class Category {
    PRIVAT, ARBEIT, UNI
}