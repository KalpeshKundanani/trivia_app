package com.app_scrip_assignment.trivia.quiz.models

/**
 * Encapsulation of the data that can be used to represent
 * answer choice of any question.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
data class Choice(
    // represents the option of the question as string.
    val value: String,
    // represents the selection status of that particular option.
    var isSelected: Boolean = false
)