package com.app_scrip_assignment.trivia.quiz

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app_scrip_assignment.trivia.Constants
import com.app_scrip_assignment.trivia.R
import com.app_scrip_assignment.trivia.quiz.adapters.CheckableListAdapter
import com.app_scrip_assignment.trivia.quiz.adapters.RadioListAdapter
import com.app_scrip_assignment.trivia.quiz.models.Question
import com.app_scrip_assignment.trivia.summary.QuizSummaryActivity
import kotlinx.android.synthetic.main.activity_quiz.*

/**
 * Class used to conduct the quiz for the user.
 *
 * Screen will have one question, choice of answers and
 * a next button.
 *
 * Questions will change when next button is pressed when
 * when all the questions are attended and the next button
 * is pressed user will be taken to the quiz summary screen
 * where they can verify their answers.
 *
 * Next button is only enabled if the user has selected
 * at-least one answer for the question.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
class QuizActivity : AppCompatActivity() {

    /**
     * A direct reference to the ViewModel that will be used
     * to consume input from the input views of current screen
     *
     * This will cause respective changes in data of
     * ViewModel. This data can be observed and the
     * output views of this screen can act appropriately
     * according to the data change in the ViewModel
     */
    private lateinit var viewModel: QuizActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // initialize resources
        initViewModel()

        // initialize view
        initQuestionChangeObserver()
        initQuestionSelectionChangeObserver()
        initNextScreenButton()
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
        // when the current screen is created it is initialized with the dependency
        // of player's name that was entered by the user in the previous screen.

        // An instance of quiz can only be created if there is a
        // player name that exists for that quiz.
        val playerName = getPlayerName()
        val timeInMills = System.currentTimeMillis()

        // initializing the view model with essential data to conduct the quiz.
        val viewModelFactory = QuizActivityViewModelFactory(playerName, timeInMills)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(QuizActivityViewModel::class.java)
    }

    /**
     * If player name was not received by the previous screen then [AssertionError]
     * will be occur.
     *
     * @return Name of the player with which the current screen was initialized.
     */
    private fun getPlayerName(): String {
        var playerName = intent.extras?.getString(Constants.INTENT_KEY_PLAYER_NAME)
        assert(playerName != null && playerName.isNotEmpty()) { "QuizActivity can not start if the player name is not entered or is empty" }
        if (playerName == null) playerName = ""
        return playerName
    }

    /**
     * Next button is initialized to give a callback for
     * the click event.
     *
     * When the click event is received, question is changed
     * on current UI. If next question is not available then user
     * is taken to the next screen.
     */
    private fun initNextScreenButton() {
        next_button.setOnClickListener {
            val hasNextQuestion: Boolean = viewModel.next()
            if (!hasNextQuestion) showSummaryScreen()
        }
    }

    /**
     * Enables or disables the next button based on the
     * answer selection.
     *
     * Changes button color to give visual feedback to the user
     * that the button is disabled.
     */
    private fun initQuestionSelectionChangeObserver() {
        viewModel.isSelectionDone.observe(this, Observer { isAtLeastOneAnswerSelected ->
            next_button.isEnabled = isAtLeastOneAnswerSelected
            val buttonColor = if (isAtLeastOneAnswerSelected) {
                ContextCompat.getColor(this, R.color.colorAccent)
            } else Color.GRAY
            next_button.backgroundTintList = ColorStateList.valueOf(buttonColor)
        })
    }

    /**
     * User can click next button to go to the next question and they
     * can click the back button to navigate to the previous question.
     *
     * When above mentioned events occur questions are changed
     * and it is important that the UI is changed too.
     *
     * So here we observe the change in question and update the UI
     * respectively.
     */
    private fun initQuestionChangeObserver() {
        viewModel.currentQuestion.observe(this, Observer(this::setQuizView))
    }

    /**
     * If all questions are attended by the user and if next button is
     * enabled user can move to the screen where summary of the whole
     * quiz is shown to the user.
     *
     * This method will launch the summary screen.
     */
    private fun showSummaryScreen() {
        val intent = Intent(this, QuizSummaryActivity::class.java)
        intent.putExtra(Constants.INTENT_KEY_QUIZ_DATA, viewModel.quiz.toJson())
        startActivity(intent)
    }

    /**
     * Inflates UI according to the question.
     *
     * Suppose if the question accepts multiple answers then list with checkbox will be shown
     * allowing user to select more then one answer.
     *
     * If the question expects only one answer then list with radio button will be shown
     * which will allow user to select only one answer.
     */
    private fun setQuizView(question: Question) {
        // Show question on the screen.
        tv_question.text = question.question

        // initialize the list of answer choices.
        val recyclerView = findViewById<RecyclerView>(R.id.rv_choice_list)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = getAnswerListAdapter(question)
    }

    /**
     * @return an adapter for checkbox list if multiple answers are expected for the
     * question if only one answer is expected for the question then the adapter for
     * radiobutton list is returned.
     */
    private fun getAnswerListAdapter(question: Question)
            : RecyclerView.Adapter<out RecyclerView.ViewHolder> =
        if (question.isMultipleSelectionAllowed) {
            CheckableListAdapter(
                question.choices, Observer {
                    viewModel.updateChoiceSelection(it)
                }
            )
        } else {
            RadioListAdapter(
                question.choices, Observer {
                    viewModel.updateChoiceSelection(it)
                }
            )
        }

    /**
     * If there is a question that can be navigated to then that question
     * will be selected as the current question.
     *
     * If there is no question that can be navigated to by pressing back
     * button then warning is shown to user that, if they will move to
     * the previous screen current quiz will be discarded.
     */
    override fun onBackPressed() {
        val hasPrevious = viewModel.previous()
        if (!hasPrevious) {
            AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage(getString(R.string.feedback_message_for_quitting_quiz))
                .setPositiveButton(
                    "Yes"
                ) { dialog, which -> super.onBackPressed() }
                .setNegativeButton("No", null)
                .show()
        }
    }
}
