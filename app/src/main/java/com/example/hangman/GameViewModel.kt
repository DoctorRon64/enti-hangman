package com.example.hangman

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var wordToGuess: String = "ANDROID"
    var guessedLetters: MutableSet<Char> = mutableSetOf()
    var wrongAttempts: Int = 0
}