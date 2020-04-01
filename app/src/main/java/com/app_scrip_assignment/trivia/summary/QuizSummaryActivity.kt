package com.app_scrip_assignment.trivia.summary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app_scrip_assignment.trivia.Constants
import com.app_scrip_assignment.trivia.R
import com.app_scrip_assignment.trivia.main.MainActivity
import com.app_scrip_assignment.trivia.quiz_database.QuizDatabase
import com.app_scrip_assignment.trivia.quiz.models.Quiz
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_quiz_summary.*

/**
 * This screen is used to show the user a summary where user can
 * see all the questions and their respective selected answers.
 *
 * User is allowed to click on back button and can edit their answer
 * from quiz screen and the new summary will be shown
 * according to the updated data
 *
 * When user clicks on finish button they are navigated to the
 * main screen where they can start the quiz again.
 *
 * when finish is clicked it is considered as the confirmation from user
 * for submission of the quiz. Quiz is then stored in the cache and
 * list of such quiz can then be seen from the Quiz history screen.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
class QuizSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_summary)

        // get the quiz data received from previous screen.
        val quiz = getQuiz()
        assert(quiz != null ){
            "Quiz data not receiver, to inflate Quiz Summary view quiz data is expected to be passed by the parent activity."
        }

        // update the text on the screen.
        showQuizSummaryToUser(quiz)

        // initialize the finish button to receive callback for click event
        // implement action to be done on finish button clicked.
        initFinishButton(quiz)
    }

    /**
     * quiz data is shown to user using a text view.
     *
     * if the quiz data is empty then error message is shown on screen.
     */
    private fun showQuizSummaryToUser(quiz: Quiz?) {
        textView.text = if (quiz != null) createDisplayText(quiz)
        else "Invalid Quiz Data!! Empty Quiz Received."
    }

    /**
     * This function will register a click event call back for
     * finish button so when finish button is pressed appropriate
     * action will be taken.
     *
     * When finish is clicked data in database is updated.
     * main screen is opened.
     *
     * if the quiz data is not valid the user will get a visual
     * feedback for the same.
     */
    private fun initFinishButton(quiz: Quiz?) {
        finish_button.setOnClickListener {
            if (quiz != null) {
                insertQuizDataInDatabase(quiz)
                openMainScreen()
            } else {
                Toast.makeText(
                    this,
                    "Invalid value for quiz received",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Data of quiz is taken from intent.The data received in the intent
     * is in json string format so [Quiz] object is created by parsing that
     * data.
     */
    private fun getQuiz(): Quiz? {
        var quizAsJsonString = intent.extras?.getString(Constants.INTENT_KEY_QUIZ_DATA, "")
        if (quizAsJsonString == null) quizAsJsonString = ""
        return Gson().fromJson(quizAsJsonString, Quiz::class.java)
    }

    /**
     * Fires an intent that will let the intent service know that the [MainActivity]
     * is to be inflated on the screen.
     *
     * [MainActivity] is created in a way that it does not have any activity before
     * it in the activity stack so that when back button is pressed on the
     * [MainActivity] it quits the app.
     */
    private fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    /**
     * Data is inserted in room database on a background thread.
     */
    private fun insertQuizDataInDatabase(quiz: Quiz) {
        Thread(Runnable {
            val quizDao = QuizDatabase.getDB(this).quizDao()
            quizDao.insertUser(quiz)
            println(quizDao.getQuizzes().size)
        }).start()
    }

    /**
     * A String with a simple representation of quiz data\
     * that can be shown to the user.
     */
    private fun createDisplayText(quiz: Quiz): String {
        return """
            Hello ${quiz.playerName},
            
            Here are the answers selected:
            
            ${quiz.questions.first().question}
            Answers : ${quiz.questions.first()}
            
            ${quiz.questions.last().question}
            Answers : ${quiz.questions.last()}
        """.trimIndent()
    }
}
