package com.example.cap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cap.R
import com.example.cap.database.ExerciseInfo
import java.text.SimpleDateFormat
import java.util.*

class ExerciseListAdapter : ListAdapter<ExerciseInfo, ExerciseListAdapter.ExerciseViewHolder>(ExercisesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.exerciseName, current.date, current.achievement)
    }

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseItemView: TextView = itemView.findViewById(R.id.textView)
        private val exerciseIvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val exerciseIvAchievement: TextView = itemView.findViewById(R.id.tvAchievement)

        fun bind(text: String?, l: Long, achievement: Int) {
            val date = Date(l)
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")

            exerciseItemView.text = text
            exerciseIvDate.text = format.format(date)
            exerciseIvAchievement.text = "성취도 $achievement%"
        }

        companion object {
            fun create(parent: ViewGroup): ExerciseViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)

                val drawables =
                    arrayOf(R.drawable.analysis_1, R.drawable.analysis_2, R.drawable.analysis_3, R.drawable.analysis_4)
                val random = Random()
                view.setBackgroundResource(drawables[random.nextInt(4)])
                return ExerciseViewHolder(view)
            }
        }
    }

    class ExercisesComparator : DiffUtil.ItemCallback<ExerciseInfo>() {
        override fun areItemsTheSame(oldItem: ExerciseInfo, newItem: ExerciseInfo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ExerciseInfo, newItem: ExerciseInfo): Boolean {
            return oldItem.exerciseName == newItem.exerciseName
        }
    }
}