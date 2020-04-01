package com.app_scrip_assignment.trivia.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel used for holding MainActivity data
 * Data is available to be observed, respective
 * actions can be performed by the observer on data
 * changes.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
class MainActivityViewModel : ViewModel() {
    /**
     * Live data that holds the last entered player name.
     *
     * This can be observed when events related to name change
     * is to be received.
     */
    val playerNameLiveData: MutableLiveData<String> = MutableLiveData()

    /**
     * Live data that holds the value of the status that represent
     * that if the player name entered is valid or not.
     */
    val isValidPlayerNameLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        // initializing live data with the default value
        // so when the Views start observing they can
        // receive the default value.
        isValidPlayerNameLiveData.postValue(false)
        playerNameLiveData.postValue("")
    }

    /**
     * Used for updating the respective data responsible to provide
     * player name change events.
     *
     * This data is used to start the quiz when valid data is entered
     * from the view.
     *
     * @param playerName: name of the player that is going to play the quiz.
     */
    fun updatePlayerName(playerName: String) {
        playerNameLiveData.postValue(playerName)
        isValidPlayerNameLiveData.postValue(playerName.length >= 3)
    }
}

/**
 * Factory that is used to create the instance of the MainActivityViewModel.
 *
 * This factory is used by the ViewModelProvider and instance of the
 * ViewModel is created only once in the lifecycle owner's scope.
 *
 * So whenever it is appropriate ViewModelProvider uses this
 * Factory to initialize the ViewModel.
 */
class MainActivityViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            modelClass.newInstance()
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create instance of $modelClass", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create instance of $modelClass", e)
        }
    }
}