package com.app_scrip_assignment.trivia.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app_scrip_assignment.trivia.R
import com.app_scrip_assignment.trivia.quiz.models.Quiz

/**
 * Used to represent the list of quiz on UI.
 * Quiz objects are mapped an list items's UIs are created.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
class QuizHistoryListAdapter(val list: List<Quiz>) : RecyclerView.Adapter<QuizHistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.quiz_history_list_item_layout, parent, false)
        return QuizHistoryViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: QuizHistoryViewHolder, position: Int) {
        val quiz = list[position]
        holder.textView.text = createDisplayText(quiz)
    }

    /**
     * A simple representation of quiz data in the form of a formatted string.
     */
    private fun createDisplayText(quiz: Quiz): String {
        return """
            GAME ${quiz.id} : ${quiz.getFormattedDate()}

            Name : ${quiz.playerName}

            ${quiz.questions.first().question}
            Answers : ${quiz.questions.first()}
            
            ${quiz.questions.last().question}
            Answers : ${quiz.questions.last()}
        """.trimIndent()
    }
}

/**
 * View holder of each entry of list item in the list of quiz.
 */
class QuizHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.textView)
}