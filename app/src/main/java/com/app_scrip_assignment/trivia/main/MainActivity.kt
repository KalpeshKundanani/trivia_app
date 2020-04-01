package com.app_scrip_assignment.trivia.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app_scrip_assignment.trivia.Constants
import com.app_scrip_assignment.trivia.R
import com.app_scrip_assignment.trivia.history.QuizHistoryActivity
import com.app_scrip_assignment.trivia.quiz.QuizActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * MainActivity is responsible to inflate a
 * screen that accepts player name as an input and allows
 * the player to start the quiz.
 *
 * Quiz is started by clicking on next button.
 *
 * To start the quiz the name entered by the player should be
 * of valid length.
 *
 * List of quizzes played till now can be accessed from
 * History button.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
class MainActivity : AppCompatActivity() {
    /**
     * A direct reference to the ViewModel that will be used
     * to consume input from the input views of current screen
     *
     * This will cause respective changes in data of
     * ViewModel. This data can be observed and the
     * output views of this screen can act appropriately
     * according to the data change in the ViewModel
     */
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize resources.
        initViewModel()

        // initialize views.
        initPlayerNameInputView()
        initNextScreenButton()
        initHistoryScreenButton()
    }

    /**
     * Initializes the ViewModel that will be used to consume the
     * data changes from ui events.
     *
     * These UI changes will cause data changes in the respective
     * ViewModel.
     *
     * ViewModel data changes can be observed and appropriate
     * UI changes can be made on data change.
     */
    private fun initViewModel() {
        val viewModelFactory = MainActivityViewModelFactory()
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(MainActivityViewModel::class.java)
    }

    /**
     * Initializing the view that takes input as name of the player.
     *
     * A basic validation of name length exists for demo purpose which can
     * be edited in a real implementation of the app. The validation is like
     * user name should be greater than 3 characters.
     *
     * Based on player name input button that takes user to the quiz screen
     * will be enabled or disabled.
     */
    private fun initPlayerNameInputView() {
        val playerNameInputEditText = et_player_name_input
        playerNameInputEditText.setText(viewModel.playerNameLiveData.value)
        playerNameInputEditText.addTextChangedListener(playerNameInputTextWatcher)
        viewModel.isValidPlayerNameLiveData.observe(this, Observer { shouldEnable ->
            val isInvalidNameEntered = shouldEnable == null || !shouldEnable
            val playerName = viewModel.playerNameLiveData.value
            val isPlayerNameEntered = playerName != null && playerName.trim().isNotEmpty()
            val shouldShowInputValidationError = isPlayerNameEntered && isInvalidNameEntered
            playerNameInputEditText.error =
                if (shouldShowInputValidationError) {
                    "Player name should at-least be of 3 characters."
                }
                else null
        })
    }

    /**
     * observer that observes the change in the text entered from UI
     * for user name.
     *
     * Any change on UI will update the ViewModel with the appropriate data.
     */
    private val playerNameInputTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(userInputForName: CharSequence, s: Int, b: Int, c: Int) {
            viewModel.updatePlayerName(userInputForName.toString())
        }
    }

    /**
     * Initialization of a button that will be enabled when user
     * enters proper name.
     *
     * when the button is enabled it can be used to start the
     * quiz.
     */
    private fun initNextScreenButton() {
        val nextButton = findViewById<Button>(R.id.next_button)
        viewModel.isValidPlayerNameLiveData.observe(this, Observer { shouldEnable ->
            val isNextButtonEnabled = shouldEnable != null && shouldEnable
            nextButton.isEnabled = isNextButtonEnabled
        })
        nextButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_PLAYER_NAME, viewModel.playerNameLiveData.value)
            startActivity(intent)
        }
    }

    /**
     * Button that will take user to the screen where user can see the
     * list of quiz played till now and answers of the respective quiz
     * selected by the user.
     */
    private fun initHistoryScreenButton() = history_button.setOnClickListener {
        val intent = Intent(this, QuizHistoryActivity::class.java)
        startActivity(intent)
    }
}
