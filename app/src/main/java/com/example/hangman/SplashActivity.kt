package com.example.hangman

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.hangman.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // make whole screen tappable
        binding.root.setOnTouchListener { _, ev ->
            if (ev.action == MotionEvent.ACTION_UP) {
                startActivity(Intent(this, LevelSelectorActivity::class.java))
                finish()
            }
            true
        }
    }
}
