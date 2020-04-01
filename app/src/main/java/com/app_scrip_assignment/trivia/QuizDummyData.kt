package com.app_scrip_assignment.trivia

import com.app_scrip_assignment.trivia.quiz.models.Choice
import com.app_scrip_assignment.trivia.quiz.models.Question

/**
 * This is the data source that is temporary and is created with an
 * intention to provide data that is of the scope of the current assignment
 * when a real quiz app is created this data source should be replaced.
 *
 * @author: Kalpesh Kundanani.
 * Date: 31st March 2020
 */
class QuizDummyData {
    companion object {
        fun getDummyData(): List<Question> {
            return listOf(
                Question(
                    "Who is the best cricketer in the world?",
                    listOf(
                        Choice("Sachin Tendulkar"),
                        Choice("Virat Kolli"),
                        Choice("Adam Gilchirst"),
                        Choice("Jacques Kallis")
                    ),
                    false
                ),
                Question(
                    "What are the colors in the Indian national flag?",
                    listOf(
                        Choice("White"),
                        Choice("Yellow"),
                        Choice("Orange"),
                        Choice("Green")
                    ),
                    true
                )
            )
        }
    }
}