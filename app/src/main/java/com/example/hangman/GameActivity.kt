package com.example.hangman

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hangman.databinding.ActivityGameBinding
import com.example.hangman.util.LevelRepository

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var prefs: SharedPreferences

    private lateinit var word: String
    private lateinit var masked: CharArray
    private var incorrect = 0
    private val maxIncorrect = 6
    private lateinit var usedLetters: MutableSet<Char>

    companion object {
        const val PREFS_NAME = "hangman_prefs"
        const val PREF_LANG = "lang"
        const val PREF_DARK = "dark_mode"
        const val EXTRA_LEVEL_INDEX = "level_index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        applySavedLanguage()
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopbar()
        loadLevel()
        setupKeyboard()
        setupResultOverlay()
        enableHardwareKeyboardInput()
    }

    private fun applySavedLanguage() {
        val lang = prefs.getString(PREF_LANG, "en") ?: "en"
        val config = resources.configuration
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun loadLevel() {
        val index = intent.getIntExtra(EXTRA_LEVEL_INDEX, 0).coerceAtLeast(0)
        val levels = LevelRepository.getLevels()
        word = levels[index.coerceAtMost(levels.lastIndex)].word.uppercase()

        masked = CharArray(word.length) { if (word[it] == ' ') ' ' else '_' }
        usedLetters = mutableSetOf()
        incorrect = 0

        updateMaskedText()
        loadHangmanImage()
    }

    private fun setupKeyboard() {
        val grid = binding.gridKeyboard
        grid.removeAllViews()

        for (c in 'A'..'Z') {
            val button = Button(this, null, 0, R.style.KeyboardButton).apply {
                text = c.toString()
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(6, 6, 6, 6)
                }
                setOnClickListener {
                    isEnabled = false
                    alpha = 0.5f
                    handleGuess(c)
                }
            }
            grid.addView(button)
        }
    }

    private fun setupResultOverlay() {
        binding.resultOverlay.setOnClickListener {
            startActivity(Intent(this, LevelSelectorActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            finish()
        }
    }

    private fun enableHardwareKeyboardInput() {
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener { _, _, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                val ch = event.unicodeChar.toChar().uppercaseChar()
                if (ch in 'A'..'Z') handleGuess(ch)
            }
            false
        }
    }

    private fun updateMaskedText() {
        binding.tvWordMasked.text = masked.joinToString(" ")
    }

    private fun loadHangmanImage() {
        val id = resources.getIdentifier("hangman_$incorrect", "drawable", packageName)
        binding.imgHangman.setImageResource(id)
    }

    private fun handleGuess(ch: Char) {
        if (!usedLetters.add(ch)) return

        val matches = word.indices.filter { word[it] == ch }
        if (matches.isNotEmpty()) {
            matches.forEach { masked[it] = ch }
            updateMaskedText()
            if (!masked.contains('_')) showResult(true)
        } else {
            incorrect++
            loadHangmanImage()
            if (incorrect >= maxIncorrect) {
                binding.tvWordMasked.text = word.toCharArray().joinToString(" ")
                showResult(false)
            }
        }
    }

    private fun showResult(win: Boolean) {
        binding.tvResultText.text = if (win) getString(R.string.you_win) else getString(R.string.you_lose)
        binding.resultOverlay.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        val dark = prefs.getBoolean(PREF_DARK, false)
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
            if (dark) androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            else androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun setupTopbar() {
        val switchLang = binding.topbar.switchLang
        val switchTheme = binding.topbar.switchTheme

        switchTheme.setOnClickListener {
            prefs.edit().putBoolean(PREF_DARK, !prefs.getBoolean(PREF_DARK, false)).apply()
            recreate()
        }

        switchLang.setOnClickListener {
            val next = if (prefs.getString(PREF_LANG, "en") == "en") "es" else "en"
            prefs.edit().putString(PREF_LANG, next).apply()
            recreate()
        }
    }
}
