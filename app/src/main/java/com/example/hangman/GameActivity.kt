package com.example.hangman

import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.hangman.databinding.ActivityGameBinding
import com.example.hangman.databinding.TopbarBinding
import com.example.hangman.util.ThemeManager

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var prefs: SharedPreferences

    private lateinit var word: String
    private lateinit var masked: CharArray
    private var incorrect = 0
    private val maxIncorrect = 6
    private lateinit var usedLetters: MutableSet<Char>

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("hangman_prefs", MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopbar()

        val index = intent.getIntExtra("level_index", 0)
        word = LevelRepository.getLevels()[index].word.uppercase()
        masked = CharArray(word.length) { if (word[it] == ' ') ' ' else '_' }
        usedLetters = mutableSetOf()
        updateMaskedText()
        loadHangmanImage()
        setupKeyboard()
        binding.resultOverlay.setOnClickListener {
            finish() // return to level selector
        }
        // allow hardware keyboard typing:
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                val ch = event.unicodeChar
                if (ch != 0) {
                    val letter = ch.toChar().uppercaseChar()
                    if (letter in 'A'..'Z') handleGuess(letter)
                }
            }
            false
        }
    }

    private fun setupTopbar() {
        val topbar = binding.topbar

        // set initial icon depending on current theme state
        val isDark = ThemeManager.isDarkMode(this)
        topbar.btnSettings.setImageResource(
            if (isDark) R.drawable.ic_dark_mode
            else R.drawable.ic_light_mode
        )

        topbar.btnSettings.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add(getString(R.string.toggle_theme))
                .setOnMenuItemClickListener {

                    val currentlyDark = ThemeManager.isDarkMode(this)
                    ThemeManager.applyTheme(this, !currentlyDark)

                    // update icon
                    topbar.btnSettings.setImageResource(
                        if (!currentlyDark) R.drawable.ic_dark_mode
                        else R.drawable.ic_light_mode
                    )

                    true
                }
            popup.show()
        }
    }

    private fun updateMaskedText() {
        binding.tvWordMasked.text = masked.joinToString(" ")
    }

    private fun loadHangmanImage() {
        val id = resources.getIdentifier("hangman_$incorrect", "drawable", packageName)
        if (id != 0) {
            binding.imgHangman.setImageResource(id)
        }
    }

    private fun setupKeyboard() {
        val letters = ('A'..'Z').toList()
        val grid = binding.gridKeyboard
        grid.removeAllViews()
        for (c in letters) {
            val b = Button(this)
            b.text = c.toString()
            val lp = GridLayout.LayoutParams()
            lp.width = 0
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            lp.setMargins(6,6,6,6)
            b.layoutParams = lp
            b.setOnClickListener {
                b.isEnabled = false
                handleGuess(c)
            }
            grid.addView(b)
        }
    }

    private fun handleGuess(ch: Char) {
        if (usedLetters.contains(ch)) return
        usedLetters.add(ch)
        val indices = word.indices.filter { word[it] == ch }
        if (indices.isNotEmpty()) {
            for (i in indices) masked[i] = ch
            updateMaskedText()
            if (!masked.contains('_')) showResult(true)
        } else {
            incorrect++
            loadHangmanImage()
            if (incorrect >= maxIncorrect) showResult(false)
        }
    }

    private fun showResult(win: Boolean) {
        binding.resultOverlay.visibility = View.VISIBLE
        binding.tvResultText.text = if (win) getString(R.string.you_win) else getString(R.string.you_lose)
        // reveal the word on losing to satisfy UX
        if (!win) binding.tvWordMasked.text = word.toCharArray().joinToString(" ")
    }

    override fun onResume() {
        super.onResume()
        val dark = prefs.getBoolean("dark_mode", false)
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
            if (dark) androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            else androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
