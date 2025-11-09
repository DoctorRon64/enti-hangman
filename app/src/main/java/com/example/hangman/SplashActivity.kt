package com.example.hangman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val rootView: View = findViewById(R.id.root)
        rootView.setOnClickListener {
            startActivity(Intent(this, LevelSelectorActivity::class.java))
            finish()
        }
    }
}
