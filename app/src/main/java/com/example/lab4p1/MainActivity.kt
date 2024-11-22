package com.example.lab4p1

import android.widget.Button
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var resultTextView: TextView // Новый TextView для результата

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        resultTextView = findViewById(R.id.result_text_view) // Инициализация нового TextView

        trueButton.setOnClickListener { view: View -> checkAnswer(true) }
        falseButton.setOnClickListener { view: View -> checkAnswer(false) }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            trueButton.visibility = View.VISIBLE
            falseButton.visibility = View.VISIBLE
            resultTextView.visibility = View.GONE // Скрыть результат при переходе к следующему вопросу
        }

        prevButton.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex-- // Переход к предыдущему вопросу только если текущий не первый.
            }
            updateQuestion()
            trueButton.visibility = View.VISIBLE
            falseButton.visibility = View.VISIBLE
            resultTextView.visibility = View.GONE // Скрыть результат при переходе к предыдущему вопросу
        }

        // Restore state if available
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt("current_question_index", 0)
            correctAnswersCount = savedInstanceState.getInt("correct_answers_count", 0)
        }

        updateQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current question index or any other relevant data
        outState.putInt("current_question_index", currentIndex)
        outState.putInt("correct_answers_count", correctAnswersCount) // Сохраняем счетчик правильных ответов
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)

        trueButton.visibility = View.VISIBLE
        falseButton.visibility = View.VISIBLE

        if (currentIndex == questionBank.size - 1) {
            nextButton.visibility = View.INVISIBLE // Hide Next button on last question.
            nextButton.isEnabled = false // Disable Next button.
        } else {
            nextButton.visibility = View.VISIBLE // Show Next button for other questions.
            nextButton.isEnabled = true // Enable Next button.
        }

        if (currentIndex == 0) {
            prevButton.visibility = View.INVISIBLE // Hide Prev button on first question.
            prevButton.isEnabled = false // Disable Prev button.
        } else {
            prevButton.visibility = View.VISIBLE // Show Prev button for other questions.
            prevButton.isEnabled = true // Enable Prev button.
        }

        resultTextView.visibility = View.GONE // Скрыть результат при обновлении вопроса.
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        if (userAnswer == correctAnswer) {
            correctAnswersCount++ // Увеличиваем счетчик правильных ответов.
            Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
        }

        trueButton.visibility = View.INVISIBLE
        falseButton.visibility = View.INVISIBLE

        if (currentIndex == questionBank.size - 1) {
            showResult() // Показать результат после последнего вопроса.
        }
    }

    private fun showResult() {
        val resultMessage = "Из ${questionBank.size} вопросов правильных: $correctAnswersCount."
        resultTextView.text = resultMessage // Устанавливаем текст результата в TextView.
        resultTextView.visibility = View.VISIBLE // Показываем результат на экране.
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