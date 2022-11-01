package com.example.cap

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GoalSetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_setting)

        val arrayGoals =
            arrayOf(arrayOf(0.90F, 3, 2), arrayOf(0.75F, 10, 3), arrayOf(0.35F, 50, 2))

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val curExercise = sharedPref.getString(getString(R.string.saved_exercise), "랫풀다운")

        var weight: Int
        var rap: Int
        var set: Int

        val curRm = when (curExercise) {
            "랫풀다운" -> sharedPref.getFloat(getString(R.string.saved_rm_lat_pull_down), 0F)

            "벤치프레스" -> sharedPref.getFloat(getString(R.string.saved_rm_bench_press), 0F)

            "스쿼트" -> sharedPref.getFloat(getString(R.string.saved_rm_squat), 0F)

            else -> sharedPref.getFloat(getString(R.string.saved_rm_dead_lift), 0F)
        }

        val bGoal1: Button = findViewById(R.id.bGoal1)
        bGoal1.setOnClickListener {
            weight = (curRm * 0.9F).toInt()
            rap = 3
            set = 2

            val nextIntent = Intent(this, Exercise::class.java)
            nextIntent.putExtra("activity", "exercise")
            nextIntent.putExtra("weight", weight)
            nextIntent.putExtra("rap", rap)
            nextIntent.putExtra("set", set)
            startActivity(nextIntent)
        }

        val bGoal2: Button = findViewById(R.id.bGoal2)
        bGoal2.setOnClickListener {
            weight = (curRm * 0.75).toInt()
            rap = 10
            set = 3

            val nextIntent = Intent(this, Exercise::class.java)
            nextIntent.putExtra("activity", "exercise")
            nextIntent.putExtra("weight", weight)
            nextIntent.putExtra("rap", rap)
            nextIntent.putExtra("set", set)
            startActivity(nextIntent)
        }

        val bGoal3: Button = findViewById(R.id.bGoal3)
        bGoal3.setOnClickListener {
            weight = (curRm * 0.35).toInt()
            rap = 50
            set = 2

            val nextIntent = Intent(this, Exercise::class.java)
            nextIntent.putExtra("activity", "exercise")
            nextIntent.putExtra("weight", weight)
            nextIntent.putExtra("rap", rap)
            nextIntent.putExtra("set", set)
            startActivity(nextIntent)
        }
    }
}