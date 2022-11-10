package com.example.cap

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ExerciseSelection : AppCompatActivity() {
    private val TAG = "ExerciseSelection"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_selection)

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val latpull : ImageButton = findViewById(R.id.Latpull)
        latpull.setOnClickListener {
            Log.i(TAG, "latpull.setOnClickListener 호출")

            editor.putString(getString(R.string.saved_exercise), "랫풀다운")
            editor.commit()

            Log.i(TAG, "현재 운동: ${sharedPref.getString(getString(R.string.saved_exercise), "값 없음")}")

            val nextIntent = Intent(this, ActivitySelection::class.java)
            nextIntent.putExtra("exercise", "랫풀다운")
            startActivity(nextIntent)
        }
        val bench : ImageButton = findViewById(R.id.Bench)
        bench.setOnClickListener {
            editor.putString(getString(R.string.saved_exercise), "벤치프레스")
            editor.commit()

            Log.i(TAG, "현재 운동: ${sharedPref.getString(getString(R.string.saved_exercise), "값 없음")}")

            val nextIntent = Intent(this, ActivitySelection::class.java)
            nextIntent.putExtra("exercise", "벤치프레스")
            startActivity(nextIntent)
        }
        val squat : ImageButton = findViewById(R.id.Squat)
        squat.setOnClickListener {
            editor.putString(getString(R.string.saved_exercise), "스쿼트")
            editor.commit()

            Log.i(TAG, "현재 운동: ${sharedPref.getString(getString(R.string.saved_exercise), "값 없음")}")

            val nextIntent = Intent(this, ActivitySelection::class.java)
            nextIntent.putExtra("exercise", "스쿼트")
            startActivity(nextIntent)
        }
//        val deadlift : ImageButton = findViewById(R.id.Plank)
//        deadlift.setOnClickListener {
//            editor.putString(getString(R.string.saved_exercise), "데드리프트")
//            editor.commit()
//
//            Log.i(TAG, "현재 운동: ${sharedPref.getString(getString(R.string.saved_exercise), "값 없음")}")
//
//            val nextIntent = Intent(this, ActivitySelection::class.java)
//            startActivity(nextIntent)
//        }
        val plank : ImageButton = findViewById(R.id.Plank)
        plank.setOnClickListener {
            editor.putString(getString(R.string.saved_exercise), "플랭크")
            editor.commit()

            Log.i(TAG, "현재 운동: ${sharedPref.getString(getString(R.string.saved_exercise), "값 없음")}")

            val nextIntent = Intent(this, ActivitySelection::class.java)
            nextIntent.putExtra("exercise", "플랭크")
            startActivity(nextIntent)
        }
    }

    private fun setFrag(fragNum : Int){
        val ft  = supportFragmentManager.beginTransaction()
        when(fragNum){
            0 -> {
                ft.replace(R.id.main_frame, Mypage()).commit()
            }
        }
    }
}