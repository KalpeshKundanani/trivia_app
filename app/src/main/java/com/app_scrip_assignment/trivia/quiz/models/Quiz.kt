package com.app_scrip_assignment.trivia.quiz.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

/**
 * Encapsulation of data that is used to represent whole quiz.
 * This data can be for multiple questions with multiple choices.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
@Entity
data class Quiz(
    val playerName: String,
    val timeInMills: Long,
    val questions: List<Question>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    /**
     * Utility method to get the json formatted string that
     * represents data of current instance.
     */
    fun toJson(): String {
        return Gson().toJson(this)
    }

    /**
     * Utility method that can be used to consume the formatted
     * quiz start date and time as String.
     */
    fun getFormattedDate(): String {
        val fmtOut = SimpleDateFormat("dd MMM yyyy hh:mm", Locale.getDefault())
        return fmtOut.format(timeInMills)
    }
}





