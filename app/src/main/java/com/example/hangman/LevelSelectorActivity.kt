package com.example.hangman

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hangman.databinding.ActivityLevelSelectorBinding
import com.example.hangman.util.LevelRepository
import com.google.android.material.imageview.ShapeableImageView

class LevelSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelSelectorBinding
    private lateinit var adapter: LevelAdapter
    private lateinit var prefs: SharedPreferences

    companion object {
        const val PREFS_NAME = "hangman_prefs"
        const val PREF_LANG = "lang"
        const val PREF_DARK = "dark_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        applySavedLanguage()
        super.onCreate(savedInstanceState)
        binding = ActivityLevelSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopbar()

        val levels = LevelRepository.getLevels()
        adapter = LevelAdapter(levels) { position ->
            startActivity(Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_LEVEL_INDEX, position)
            })
        }

        binding.rvLevels.layoutManager = LinearLayoutManager(this)
        binding.rvLevels.adapter = adapter
    }

    private fun applySavedLanguage() {
        val lang = prefs.getString(PREF_LANG, "en") ?: "en"
        val config = resources.configuration
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
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
        val switchLang: ShapeableImageView = binding.topbar.switchLang
        val switchTheme: ShapeableImageView = binding.topbar.switchTheme

        switchTheme.setOnClickListener {
            prefs.edit().putBoolean(PREF_DARK, !prefs.getBoolean(PREF_DARK, false)).apply()
            recreate()
        }

        switchLang.setOnClickListener {
            val current = prefs.getString(PREF_LANG, "en") ?: "en"
            val next = if (current == "en") "es" else "en"
            prefs.edit().putString(PREF_LANG, next).apply()

            val config = resources.configuration
            val locale = java.util.Locale(next)
            java.util.Locale.setDefault(locale)
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            recreate()
        }
    }
}
