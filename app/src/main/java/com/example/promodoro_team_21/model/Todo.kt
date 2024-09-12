package com.example.promodoro_team_21.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var category: Category,
    var createdAt: Date,
    var priority: Int = 0 // Neue Spalte hinzugefügt
)

// Enum für privat, arbeit, uni. Für das Laden der Todos in den entsprechenden Tabs
enum class Category {
    PRIVAT,
    ARBEIT,
    UNI
}