package com.app_scrip_assignment.trivia.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app_scrip_assignment.trivia.QuizDummyData
import com.app_scrip_assignment.trivia.quiz.models.Choice
import com.app_scrip_assignment.trivia.quiz.models.Question
import com.app_scrip_assignment.trivia.quiz.models.Quiz

/**
 * ViewModel that holds and acts on the data that is responsible for
 * the state of the current quiz.
 *
 * Data of this ViewModel can be observed by the View to act appropriately
 * for any specific data change event.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
class QuizActivityViewModel(
    playerName: String,
    timeInMills: Long
) : ViewModel() {
    /**
     * Initializing the quiz with the player's name and the time
     * of quiz start.
     *
     * Initial data will be injected for the questions from
     * dummy data source.
     */
    val quiz = Quiz(playerName, timeInMills, QuizDummyData.getDummyData())

    /**
     * Live data that holds and emits data for the status of the
     * selection of the answer.
     *
     * The data emitted is of Boolean data type and is true when
     * at-least one answer is selected and false when none.
     */
    val isSelectionDone = MutableLiveData<Boolean>()

    /**
     * Live data that holds current question and emits it to the observers.
     *
     * View can consume this data to inflate appropriate UI.
     */
    val currentQuestion: MutableLiveData<Question> = MutableLiveData<Question>()

    init {
        // Initializing default value for the data so that
        // observers when start observing can receive the default value.
        currentQuestion.postValue(quiz.questions.first())
        isSelectionDone.postValue(false)
    }

    /**
     * Choice of answer of current question is updated through this function.
     * Answer choice will cause data events that can be consumed by the view.
     */
    fun updateChoiceSelection(updatedChoices: List<Choice>) {
        val value = currentQuestion.value
        value?.choices = updatedChoices
        currentQuestion.value
        isSelectionDone.postValue(updatedChoices.any { it.isSelected })
    }

    /**
     * Set selection to next question if there is one.
     *
     * @return true if there is a next question false when there isn't.
     */
    fun next(): Boolean {
        return try {
            val index = quiz.questions.indexOf(currentQuestion.value) + 1
            currentQuestion.postValue(quiz.questions[index])
            isSelectionDone.postValue(quiz.questions[index].choices.any { it.isSelected })
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Set selection to previous question if there is one.
     *
     * @return true if there is a previous question false when there isn't.
     */
    fun previous(): Boolean {
        return try {
            val index = quiz.questions.indexOf(currentQuestion.value) - 1
            currentQuestion.postValue(quiz.questions[index])
            isSelectionDone.postValue(quiz.questions[index].choices.any { it.isSelected })
            true
        } catch (e: Exception) {
            false
        }
    }
}

/**
 * Factory that can be used to create instance of [QuizActivityViewModel]
 * this factory initializes objects with player name and time in milliseconds
 * dependency which can be use to create a quiz for a player.
 */
class QuizActivityViewModelFactory(private val playerName: String, private val timeInMills: Long) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            return if (modelClass == QuizActivityViewModel::class.java) {
                QuizActivityViewModel(playerName, timeInMills) as T
            } else {
                modelClass.newInstance()
            }
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create instance of $modelClass", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create instance of $modelClass", e)
        }
    }
}