package com.example.hangman

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

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

        tvWordMasked = findViewById(R.id.tvWordMasked)
        imgHangman = findViewById(R.id.imgHangman)
        resultOverlay = findViewById(R.id.resultOverlay)
        gridKeyboard = findViewById(R.id.gridKeyboard)

        val settingsAnchor: View = findViewById(R.id.switchLang)
        settingsAnchor.setOnClickListener { view ->
            showSettingsPopup(view)
        }

        val themeIcon: View = findViewById(R.id.switchTheme)
        themeIcon.setOnClickListener {
            toggleNightMode()
        }

        wordToGuess = intent.getStringExtra("selectedWord")?.uppercase(Locale.getDefault()) ?: ""
        if (wordToGuess.isEmpty()) {
            wordToGuess = "ANDROID"
        }

        guessedLetters.clear()
        wrongAttempts = 0
        resultOverlay.visibility = View.GONE
        imgHangman.setImageResource(R.drawable.ic_hangman_0)

        setupKeyboard()
        updateMaskedWord()
    }

    private fun toggleNightMode() {
        val mode = AppCompatDelegate.getDefaultNightMode()
        if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun showSettingsPopup(anchor: View) {
        val popup = android.widget.PopupMenu(this, anchor)
        popup.menu.add(0, 0, 0, getString(R.string.settings))
        popup.menu.add(0, 1, 1, getString(R.string.toggle_theme))
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> toggleNightMode()
            }
            true
        }
        popup.show()
    }

    private fun setupKeyboard() {
        gridKeyboard.removeAllViews()
        for (i in 'A'..'Z') {
            val button = createLetterButton(i)
            gridKeyboard.addView(button)
        }
        enableAllKeyboardButtons(true)
    }

    private fun createLetterButton(letter: Char): View {
        val button = LayoutInflater.from(this).inflate(R.layout.button_letter, gridKeyboard, false) as Button
        button.text = letter.toString()
        button.isAllCaps = false
        button.setOnClickListener {
            onLetterGuessed(letter)
            button.isEnabled = false
        }
        return button
    }

    private fun enableAllKeyboardButtons(enabled: Boolean) {
        for (i in 0 until gridKeyboard.childCount) {
            val v = gridKeyboard.getChildAt(i)
            v.isEnabled = enabled
        }
    }

    private fun updateMaskedWord() {
        val masked = wordToGuess.map { ch ->
            when {
                !ch.isLetter() -> ch
                guessedLetters.contains(ch) -> ch
                else -> '_'
            }
        }.joinToString(" ")
        tvWordMasked.text = masked
    }

    private fun checkGameStatus() {
        if (wrongAttempts >= maxAttempts) {
            showResult(getString(R.string.you_lose) + " " + String.format("(%s)", wordToGuess))
            return
        }
        val allLettersRevealed = wordToGuess.filter { it.isLetter() }.all { guessedLetters.contains(it) }
        if (allLettersRevealed) {
            showResult(getString(R.string.you_win))
        }
    }

    private fun onLetterGuessed(letter: Char) {
        val l = letter.uppercaseChar()
        if (guessedLetters.contains(l)) return
        guessedLetters.add(l)
        if (!wordToGuess.contains(l)) {
            wrongAttempts++
            updateHangmanImage()
        } else {
        }
        updateMaskedWord()
        checkGameStatus()
    }

    private fun updateHangmanImage() {
        val hangmanImageResource = when (wrongAttempts) {
            0 -> R.drawable.ic_hangman_0
            1 -> R.drawable.ic_hangman_1
            2 -> R.drawable.ic_hangman_2
            3 -> R.drawable.ic_hangman_3
            4 -> R.drawable.ic_hangman_4
            5 -> R.drawable.ic_hangman_5
            6 -> R.drawable.ic_hangman_6
            else -> R.drawable.ic_hangman_6
        }
        imgHangman.setImageResource(hangmanImageResource)
    }

    private fun showResult(message: String) {
        resultOverlay.visibility = View.VISIBLE
        val tvResultText: TextView = resultOverlay.findViewById(R.id.tvResultText)
        tvResultText.text = message

        enableAllKeyboardButtons(false)

        resultOverlay.setOnClickListener {
            val intent = Intent(this, LevelSelectorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
