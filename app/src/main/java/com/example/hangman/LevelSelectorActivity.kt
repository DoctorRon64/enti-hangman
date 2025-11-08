package com.example.hangman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LevelSelectorActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val levels: List<Level> = listOf(
        Level("Easy", "CAT", 3, R.drawable.ic_easy),
        Level("Medium", "APPLE", 5, R.drawable.ic_medium),
        Level("Difficult", "ELEPHANT", 8, R.drawable.ic_hard)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_selector)

        recyclerView = findViewById(R.id.rvLevels)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LevelAdapter(levels) { level ->
            // Start game with the selected level
            startActivity(Intent(this, GameActivity::class.java).apply {
                putExtra("selectedWord", level.word)
            })
        }
    }
}
