package com.example.promodoro_team_21.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

@Database(entities = [Todo::class], version = 2) // Version auf 2 erhöht
@TypeConverters(Converters::class) // Passende Konverter müssen hinzugefügt werden
abstract class TodoDatabase : RoomDatabase() {

    companion object {
        const val NAME = "Todo_DB"

        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    NAME
                )
                    .addMigrations(MIGRATION_1_2) // Migration hinzufügen
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Definiere die Migration von Version 1 auf Version 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Beispiel: Hinzufügen einer neuen Spalte "priority"
                database.execSQL("ALTER TABLE Todo ADD COLUMN priority INTEGER DEFAULT 0 NOT NULL")

                // Falls `category`-Spalte nicht vorhanden ist, füge sie hinzu
                if (!columnExists(database, "Todo", "category")) {
                    database.execSQL("ALTER TABLE Todo ADD COLUMN category TEXT NOT NULL DEFAULT 'PRIVAT'")
                }
            }

            // Methode zum Überprüfen, ob eine Spalte existiert
            private fun columnExists(database: SupportSQLiteDatabase, tableName: String, columnName: String): Boolean {
                val cursor = database.query("PRAGMA table_info($tableName)")
                var exists = false
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    if (name == columnName) {
                        exists = true
                        break
                    }
                }
                cursor.close()
                return exists
            }
        }
    }

    abstract fun getTodoDao(): TodoDao
}