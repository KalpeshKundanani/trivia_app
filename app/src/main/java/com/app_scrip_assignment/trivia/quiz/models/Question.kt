package com.app_scrip_assignment.trivia.quiz.models

/**
 * Encapsulation of the data that represents one question in a
 * quiz.
 *
 * Question can be having one answer or multiple answers
 * which is differed by the [isMultipleSelectionAllowed]
 * property of this class.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
data class Question(
    val question: String,
    var choices: List<Choice>,
    val isMultipleSelectionAllowed: Boolean
) {
    /**
     * User to represent answers in a comma separated format.
     * if there is just answer then comma wont be append to it.
     */
    override fun toString(): String = choices
        .filter { it.isSelected }
        .joinToString(separator = ",") { it.value }

    /**
     * Comparision of a [Question] object is done considering
     * the value of question property.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (question != other.question) return false

        return true
    }

    /**
     * Uniqueness of a [Question] object is defined considering
     * the value of question property.
     */
    override fun hashCode(): Int = question.hashCode()
}