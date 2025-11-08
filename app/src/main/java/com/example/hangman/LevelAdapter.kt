package com.example.hangman

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hangman.databinding.ItemLevelBinding
import com.example.hangman.util.Level

class LevelAdapter(
    private val levels: List<Level>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<LevelAdapter.LevelVH>() {

    inner class LevelVH(val binding: ItemLevelBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) onClick(pos)
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
        holder.binding.tvDebugWord.text = holder.binding.root.context.getString(R.string.debug_word, level.word)
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
