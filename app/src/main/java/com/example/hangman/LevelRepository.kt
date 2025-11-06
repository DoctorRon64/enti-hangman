package com.example.hangman

data class Level(val name: String, val word: String)

object LevelRepository {
    private val words = listOf(
        "cat","dog","bird","apple","banana","orange",
        "kotlin","android","activity","fragment","recyclerview",
        "programming","development","internationalization","encyclopedia",
    )

    fun getLevels(): List<Level> {
        return words.mapIndexed { index, w -> Level("Level ${index+1}", w) }
    }
}
