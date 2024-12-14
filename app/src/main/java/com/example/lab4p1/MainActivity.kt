package com.example.lab4p1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown" // Define constant for answer shown

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var resultTextView: TextView // New TextView for results

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0
    private var correctAnswersCount = 0

    // Registering the ActivityResultLauncher for CheatActivity
    private val cheatLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false

                // Handle cheating logic here if needed.
                if (isCheater) {
                    Toast.makeText(this, "You have cheated!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        resultTextView = findViewById(R.id.result_text_view)

        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            trueButton.visibility = View.VISIBLE
            falseButton.visibility = View.VISIBLE
            resultTextView.visibility = View.GONE // Hide result when moving to next question.
        }

        cheatButton.setOnClickListener {
            // Start CheatActivity using the registered launcher.
            val answerIsTrue = questionBank[currentIndex].answer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent) // Launching CheatActivity with the launcher.
        }

        prevButton.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex-- // Move to previous question only if not at first.
            }
            updateQuestion()
            trueButton.visibility = View.VISIBLE
            falseButton.visibility = View.VISIBLE
            resultTextView.visibility = View.GONE // Hide result when moving to previous question.
        }

        // Restore state if available.
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt("current_question_index", 0)
            correctAnswersCount = savedInstanceState.getInt("correct_answers_count", 0)
        }

        updateQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("current_question_index", currentIndex)
        outState.putInt("correct_answers_count", correctAnswersCount)
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)

        trueButton.visibility = View.VISIBLE
        falseButton.visibility = View.VISIBLE

        nextButton.visibility =
            if (currentIndex == questionBank.size - 1) View.INVISIBLE else View.VISIBLE

        prevButton.visibility =
            if (currentIndex == 0) View.INVISIBLE else View.VISIBLE

        resultTextView.visibility = View.GONE
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        if (userAnswer == correctAnswer) {
            correctAnswersCount++
            Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
        }

        trueButton.visibility = View.INVISIBLE
        falseButton.visibility = View.INVISIBLE

        if (currentIndex == questionBank.size - 1) {
            showResult()
        }
    }

    private fun showResult() {
        val resultMessage = "Из ${questionBank.size} вопросов правильных: $correctAnswersCount."
        resultTextView.text = resultMessage
        resultTextView.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}
