package com.example.hangman

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LevelSelectorActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private fun difficultyImageFor(length: Int): Int {
        return when {
            length <= 4 -> R.drawable.ic_easy
            length in 5..7 -> R.drawable.ic_medium
            else -> R.drawable.ic_hard
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_selector)

        recyclerView = findViewById(R.id.rvLevels)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val settingsAnchor: View = findViewById(R.id.btnSettings)
        settingsAnchor.setOnClickListener { view ->
            showSettingsPopup(view)
        }

        val rawWords = listOf(
            "CAT","DOG","CAR","BLUE","APPLE","ORANGE","BANANA", "GRID",
            "ELEPHANT","KANGAROO","COMPUTER","PROGRAMMING","ANDROID",
            "MOUNTAIN","RIVER","LANGUAGE","DEVELOPER","KOTLIN","VARIABLE",
            "FUNCTION","ACTIVITY","FRAGMENT","LAYOUT","RESOURCE","DRAWABLE",
            "BUTTON","TEXTVIEW","RECYCLERVIEW","KEYBOARD","HANGMAN"
        )

        val levels = rawWords.mapIndexed { index, w ->
            val letters = w.length
            val difficulty = when {
                letters <= 4 -> 0
                letters in 5..7 -> 1
                else -> 2
            }
            Level(
                name = if (difficulty == 0) getString(R.string.difficulty_easy)
                else if (difficulty == 1) getString(R.string.difficulty_medium)
                else getString(R.string.difficulty_hard),
                word = w.uppercase(),
                letterCount = letters,
                difficultyImg = difficultyImageFor(letters)
            ) to difficulty
        }
            .sortedWith(compareBy({ it.second }, { it.first.word }))
            .map { it.first }

        recyclerView.adapter = LevelAdapter(levels) { level ->
            startActivity(Intent(this, GameActivity::class.java).apply {
                putExtra("selectedWord", level.word)
            })
        }
    }

    private fun toggleNightMode() {
        val mode = AppCompatDelegate.getDefaultNightMode()
        val newMode = if (mode == AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.MODE_NIGHT_NO
        else
            AppCompatDelegate.MODE_NIGHT_YES

        AppCompatDelegate.setDefaultNightMode(newMode)
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
}
