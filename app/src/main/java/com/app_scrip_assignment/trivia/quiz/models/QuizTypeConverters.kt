package com.app_scrip_assignment.trivia.quiz.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Converters used by room database to serialize and store custom
 * objects in the persistent cache.
 *
 * The existence of such Converters is because room database can
 * store only basic predefined data types like Int, String and etc.
 * So, custom objects are to be serialized before they can be saved in
 * database.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
object QuizTypeConverters {

    /**
     * Converter that will parse a json string received from the
     * cache to the List of questions.
     */
    @TypeConverter
    @JvmStatic
    fun toQuestion(jsonString: String): List<Question> = Gson().fromJson(
        jsonString,
        object : TypeToken<List<Question?>?>() {}.type
    )

    /**
     * Converter that serializes an instance of List<Question> to json format.
     */
    @TypeConverter
    @JvmStatic
    fun fromQuestion(questionsList: List<Question>): String = Gson().toJson(questionsList)
}