package com.example.hangman

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.PopupMenu
import com.example.hangman.databinding.TopbarBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hangman.databinding.ActivityLevelSelectorBinding
import com.example.hangman.util.ThemeManager

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
}