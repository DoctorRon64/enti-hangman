package com.example.hangman.util

data class Level(val name: String, val word: String)

object LevelRepository {
    private val words = listOf(
        "dog", "cat", "bird", "fish", "apple", "pear", "kiwi",
        "banana", "orange", "grape", "lemon", "melon",
        "kotlin", "android", "activity", "fragment", "recyclerview",
        "viewmodel", "lifecycle", "programming", "development",
        "internationalization", "encyclopedia", "university", "assignment",
        "computer", "algorithm", "function", "variable", "repository",
        "gradle", "manifest", "resources", "configuration", "keyboard"
    )

    fun getLevels(): List<Level> {
        return words.mapIndexed { index, w -> Level("Level ${index + 1}", w) }
    }
}
