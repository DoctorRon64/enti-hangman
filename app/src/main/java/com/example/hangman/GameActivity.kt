package com.example.hangman

import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var tvWordMasked: TextView
    private lateinit var imgHangman: ImageView
    private lateinit var resultOverlay: View
    private lateinit var gridKeyboard: GridLayout

    private var wordToGuess: String = ""
    private var guessedLetters: MutableSet<Char> = mutableSetOf()
    private var wrongAttempts: Int = 0
    private val maxAttempts = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        wordToGuess = intent.getStringExtra("selectedWord") ?: ""

        tvWordMasked = findViewById(R.id.tvWordMasked)
        imgHangman = findViewById(R.id.imgHangman)
        resultOverlay = findViewById(R.id.resultOverlay)
        gridKeyboard = findViewById(R.id.gridKeyboard)

        setupKeyboard()
        updateMaskedWord()
    }

    private fun setupKeyboard() {
        // Initialize keyboard buttons dynamically based on the alphabet
        for (i in 'A'..'Z') {
            val button = createLetterButton(i)
            gridKeyboard.addView(button)
        }
    }

    private fun createLetterButton(letter: Char): View {
        val button = LayoutInflater.from(this).inflate(R.layout.button_letter, gridKeyboard, false) as Button
        button.text = letter.toString()
        button.setOnClickListener {
            onLetterGuessed(letter)
            button.isEnabled = false // Disable button after guessing
        }
        return button
    }

    private fun updateMaskedWord() {
        // Update the displayed word with the currently guessed letters
        tvWordMasked.text = wordToGuess.map { if (it in guessedLetters) it else '_' }.joinToString(" ")
    }

    private fun checkGameStatus() {
        if (wrongAttempts >= maxAttempts) {
            // Player loses, show overlay with message
            showResult("You Lose! The word was: $wordToGuess")
        } else if (wordToGuess.all { it in guessedLetters }) {
            // Player wins, show overlay with message
            showResult("You Win!")
        }
    }

    private fun onLetterGuessed(letter: Char) {
        guessedLetters.add(letter)
        if (letter !in wordToGuess) {
            wrongAttempts++
            // Update hangman image according to the number of wrong attempts
            updateHangmanImage()
        }
        updateMaskedWord()
        checkGameStatus()
    }

    private fun updateHangmanImage() {
        val hangmanImageResource = when (wrongAttempts) {
            1 -> R.drawable.ic_hangman_1
            2 -> R.drawable.ic_hangman_2
            3 -> R.drawable.ic_hangman_3
            4 -> R.drawable.ic_hangman_4
            5 -> R.drawable.ic_hangman_5
            6 -> R.drawable.ic_hangman_6
            else -> R.drawable.ic_hangman_0 // Initial state
        }
        imgHangman.setImageResource(hangmanImageResource)
    }

    private fun showResult(message: String) {
        resultOverlay.visibility = View.VISIBLE
        val tvResultText: TextView = resultOverlay.findViewById(R.id.tvResultText)
        tvResultText.text = message

        // Hide the keyboard and set up a return action
        resultOverlay.setOnClickListener {
            finish() // or navigate back to LevelSelectorActivity
        }
    }
}
