package com.app_scrip_assignment.trivia.history

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app_scrip_assignment.trivia.R
import com.app_scrip_assignment.trivia.quiz_database.QuizDatabase
import com.app_scrip_assignment.trivia.quiz.models.Quiz

/**
 * Screen that lists down the quiz that are played till
 * now.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
class QuizHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_history)
        Thread(Runnable {
            val quizDao = QuizDatabase.getDB(this).quizDao()
            val quizzes = quizDao.getQuizzes()
            println(quizzes.size)
            runOnUiThread { initHistoryList(quizzes) }
        }).start()
    }

    private fun initHistoryList(quizzes: List<Quiz>) {
        if (quizzes.isEmpty()) {
            showNoQuizFoundDialog()
            return
        }
        val recyclerView = findViewById<RecyclerView>(R.id.quiz_history_list)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = QuizHistoryListAdapter(quizzes)
    }

    /**
     * Used for providing visual feedback to user to explain them
     * there is no quiz data saved in the database.
     *
     * The information regarding how data is saved in the database
     * is also provided so that they can understand the process.
     */
    private fun showNoQuizFoundDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.no_quiz_found_dialog_title))
            .setMessage(getString(R.string.no_quiz_found_dialog_message))
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ ->
                finish()
            }.show()
    }
}
