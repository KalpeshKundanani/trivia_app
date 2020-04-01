package com.app_scrip_assignment.trivia.quiz_database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app_scrip_assignment.trivia.quiz.models.Quiz
import com.app_scrip_assignment.trivia.quiz.models.QuizTypeConverters

/**
 * Used to get access to the room database.
 *
 * only single object of the database will be created
 * and this instance can be accessed from getDB function.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
@Database(
    entities = [Quiz::class],
    version = 1
)
@TypeConverters(QuizTypeConverters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var instance: QuizDatabase? = null
        private val LOCK = Any()

        fun getDB(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }


        private fun buildDatabase(context: Context): QuizDatabase {
            Log.d("newF", "========== buildDatabase ===========")
            return Room.databaseBuilder(
                context,
                QuizDatabase::class.java, "quiz-list.db"
            )
                .build()
        }
    }
}