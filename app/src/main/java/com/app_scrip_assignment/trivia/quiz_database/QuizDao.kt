package com.app_scrip_assignment.trivia.quiz_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.app_scrip_assignment.trivia.quiz.models.Quiz

/**
 * An interface that can be used to create references through
 * which create, read, update & delete operations can be performed
 * on room database.
 *
 * As of now for the scope of assignment we only need create and
 * read operation so only methods for such operations are
 * declared.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
@Dao
interface QuizDao {
    @Insert(onConflict = REPLACE)
    fun insertUser(user: Quiz?)

    @Query("SELECT * FROM Quiz")
    fun getQuizzes(): List<Quiz>
}