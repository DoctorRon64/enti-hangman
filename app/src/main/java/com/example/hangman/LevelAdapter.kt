package com.example.hangman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LevelAdapter(
    private val levels: List<Level>,

    private val itemClickListener: (Level) -> Unit
) : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_level, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val level = levels[position]
        holder.bind(level)

        holder.itemView.setOnClickListener {
            itemClickListener(level)
        }
    }

    override fun getItemCount(): Int = levels.size

    inner class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLevelName: TextView = itemView.findViewById(R.id.tvLevelName)
        private val tvDebugWord: TextView = itemView.findViewById(R.id.tvDebugWord)
        private val tvLettersCount: TextView = itemView.findViewById(R.id.tvLettersCount)
        private val imgDifficulty: ImageView = itemView.findViewById(R.id.imgDifficulty)

        fun bind(level: Level) {
            tvLevelName.text = level.name
            tvDebugWord.text = level.word
            tvLettersCount.text = itemView.context.getString(R.string.letters_count, level.letterCount)
            imgDifficulty.setImageResource(level.difficultyImg)
        }
    }
}
