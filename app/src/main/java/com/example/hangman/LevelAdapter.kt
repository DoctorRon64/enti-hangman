package com.example.hangman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman.databinding.ItemLevelBinding

class LevelAdapter(private val levels: List<Level>, private val onClick: (Int)->Unit) :
    RecyclerView.Adapter<LevelAdapter.LevelVH>() {

    inner class LevelVH(val binding: ItemLevelBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelVH {
        val b = ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LevelVH(b)
    }

    override fun onBindViewHolder(holder: LevelVH, position: Int) {
        val level = levels[position]
        holder.binding.tvLevelName.text = level.name
        holder.binding.tvDebugWord.text = "DEBUG: ${level.word}"
        holder.binding.tvLettersCount.text = holder.binding.root.context.getString(R.string.letters_count, level.word.length)
        val difficultyDrawable = when {
            level.word.length <= 4 -> R.drawable.ic_easy
            level.word.length <= 7 -> R.drawable.ic_medium
            else -> R.drawable.ic_hard
        }
        holder.binding.imgDifficulty.setImageResource(difficultyDrawable)
    }

    override fun getItemCount(): Int = levels.size
}
