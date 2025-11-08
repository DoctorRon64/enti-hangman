package com.example.hangman

data class Level(
    val name: String,
    val word: String,
    val letterCount: Int,
    val difficultyImg: Int
)
