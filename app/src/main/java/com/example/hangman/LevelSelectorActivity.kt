package com.example.hangman

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.example.hangman.databinding.TopbarBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hangman.databinding.ActivityLevelSelectorBinding

class LevelSelectorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLevelSelectorBinding
    private lateinit var adapter: LevelAdapter
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("hangman_prefs", MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        binding = ActivityLevelSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopbar()
        val levels = LevelRepository.getLevels()
        adapter = LevelAdapter(levels) { position ->
            // start game
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level_index", position)
            startActivity(intent)
        }
        binding.rvLevels.layoutManager = LinearLayoutManager(this)
        binding.rvLevels.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val dark = prefs.getBoolean("dark_mode", false)
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
            if (dark) androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            else androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun setupTopbar() {
        val topbarBinding = TopbarBinding.bind(binding.topbar)
        topbarBinding.btnSettings.setOnClickListener { view ->

            val popup = android.widget.PopupMenu(this, view)
            popup.menu.add(getString(R.string.toggle_theme))
                .setOnMenuItemClickListener {
                    val currentNight = prefs.getBoolean("dark_mode", false)
                    prefs.edit().putBoolean("dark_mode", !currentNight).apply()
                    recreate()
                    true
                }
            popup.show()
        }
    }
}