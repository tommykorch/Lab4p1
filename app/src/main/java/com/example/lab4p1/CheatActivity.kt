package com.example.lab4p1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build // Import Build class
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_CHEAT_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown" // Renamed constant

class CheatActivity : AppCompatActivity() {
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView // New TextView for API level display
    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cheat)

        // Retrieve the answer from the Intent
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_text_view) // Initialize new TextView

        // Display the Android API level
        apiLevelTextView.text = "Android API Level: ${Build.VERSION.SDK_INT}"

        // Set up button click listener to show the answer
        showAnswerButton.setOnClickListener {
            val answerText = if (answerIsTrue) {
                R.string.true_button // Resource ID for true answer
            } else {
                R.string.false_button // Resource ID for false answer
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true) // Indicate that the answer has been shown
        }

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Return the insets for further processing if needed
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_CHEAT_ANSWER_SHOWN, isAnswerShown) // Use renamed constant here
        }
        setResult(Activity.RESULT_OK, data) // Set result to OK and include the data
        finish() // Close CheatActivity and return to MainActivity
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue) // Pass the answer to CheatActivity
            }
        }
    }
}
