package com.example.cap

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ActivitySelection : AppCompatActivity() {
//    lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        val btn_home: ImageButton = findViewById(R.id.button_home)
        btn_home.setOnClickListener {
            val nextIntent = Intent(this, ExerciseSelection::class.java)
            startActivity(nextIntent)
        }

//        videoView = findViewById(R.id.videoView)
//        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.lat_pulldown}")
//        videoView.setMediaController(MediaController(this))
//        videoView.setVideoURI(videoUri)
//
//        videoView.requestFocus()
//        videoView.start()
//
//        videoView.setOnCompletionListener {
////            Toast.makeText(applicationContext, "Video completed", Toast.LENGTH_LONG).show()
//        }
//        videoView.setOnErrorListener { mp, what, extra ->
//            Toast.makeText(applicationContext, "An error occured while playing video",
//                    Toast.LENGTH_LONG).show()
//            false
//        }

        //근육 이미지 확대 가능 메세지
//        Toast.makeText(applicationContext, "근육 이미지를 확대하여 자세한 위치를 확인하세요.", Toast.LENGTH_LONG).show()

        val select: TextView = findViewById(R.id.tvExercise)
        val ivSensor: ImageView = findViewById(R.id.ivSensor)
        val exerciseImg1: ImageView = findViewById(R.id.exerciseImg1)
        val exerciseImg2: ImageView = findViewById(R.id.exerciseImg2)
        val exerciseImg3: ImageView = findViewById(R.id.exerciseImg3)
        val tvExerciseImg1: TextView = findViewById(R.id.tvExerciseImg1)
        val tvExerciseImg2: TextView = findViewById(R.id.tvExerciseImg2)
        val tvExerciseImg3: TextView = findViewById(R.id.tvExerciseImg3)

        val curExercise = sharedPref.getString(getString(R.string.saved_exercise), "값 없음")
        select.text = " $curExercise "
        when (curExercise) {
            "랫풀다운" -> {
                ivSensor.setImageResource(R.drawable.sensor_lat_pulldown)
                exerciseImg1.setImageResource(R.drawable.latissimus_dorsi) //광배근
                exerciseImg2.setImageResource(R.drawable.anterioir_deltoid) //삼각근 -> 전면삼각근?
                exerciseImg3.setImageResource(R.drawable.biceps_brachii) //상완이두근
                tvExerciseImg1.setText("▼광배근▼")
                tvExerciseImg2.setText("▼삼각근▼")
                tvExerciseImg3.setText("▼상완이두근▼")
            }
            "벤치프레스" -> {
                ivSensor.setImageResource(R.drawable.sensor_lat_pulldown)
                exerciseImg1.setImageResource(R.drawable.pectoralis_major) //대흉근
                exerciseImg2.setImageResource(R.drawable.anterioir_deltoid) //전면삼각근
                exerciseImg3.setImageResource(R.drawable.triceps_brachii) //상완삼두근-장두
                tvExerciseImg1.setText("▼대흉근▼")
                tvExerciseImg2.setText("▼전면삼각근▼")
                tvExerciseImg3.setText("▼상완삼두근▼")
            }
            "스쿼트" -> {
                ivSensor.setImageResource(R.drawable.sensor_lat_pulldown)
                exerciseImg1.setImageResource(R.drawable.rectus_femoris) //대퇴직근
                exerciseImg2.setImageResource(R.drawable.vastus_laterlis) //외측광근
                exerciseImg3.setImageResource(R.drawable.vastus_medialis) //내측광근
                tvExerciseImg1.setText("▼대퇴직근▼")
                tvExerciseImg2.setText("▼외측광근▼")
                tvExerciseImg3.setText("▼내측광근▼")
            }
            "플랭크" -> {
                ivSensor.setImageResource(R.drawable.sensor_lat_pulldown)
                exerciseImg1.setImageResource(R.drawable.info_latpull)
                exerciseImg2.setImageResource(R.drawable.info_latpull)
                exerciseImg3.setImageResource(R.drawable.info_latpull)
            }
            else -> {
                ivSensor.setImageResource(R.drawable.sensor_lat_pulldown)
                exerciseImg1.setImageResource(R.drawable.info_latpull)
                exerciseImg2.setImageResource(R.drawable.info_latpull)
                exerciseImg3.setImageResource(R.drawable.info_latpull)
            }
        }

        val Set: Button = findViewById(R.id.Set)
        Set.setOnClickListener {
            val nextIntent = Intent(this, Exercise::class.java)
            nextIntent.putExtra("activity", "init")
            startActivity(nextIntent)
        }
//        val RM : Button = findViewById(R.id.RM)
//        RM.setOnClickListener {
//            val nextIntent = Intent(this, WeightInput::class.java)
//            nextIntent.putExtra("activity", "rm")
//            startActivity(nextIntent)
//        }
        val Exercise: Button = findViewById(R.id.Exercise)
        Exercise.setOnClickListener {
            val nextIntent = Intent(this, GoalSetting::class.java)
            nextIntent.putExtra("activity", "exercise")
            startActivity(nextIntent)
        }

        val intent = intent
        val activity = intent.extras?.getString("activity")
        val initialSetting = when (curExercise) {
            "랫풀다운" -> sharedPref.getBoolean(getString(R.string.saved_initial_lat_pull_down), false)
            "벤치프레스" -> sharedPref.getBoolean(getString(R.string.saved_initial_bench_press), false)
            "스쿼트" -> sharedPref.getBoolean(getString(R.string.saved_initial_squat), false)
            "플랭크" -> sharedPref.getBoolean(getString(R.string.saved_initial_plank), false)
            else -> sharedPref.getBoolean(getString(R.string.saved_initial_plank), false)
        }
//        val rmSetting = when (curExercise) {
//            "랫풀다운" -> sharedPref.getFloat(getString(R.string.saved_rm_lat_pull_down), 0F)
//            "벤치프레스" -> sharedPref.getFloat(getString(R.string.saved_rm_bench_press), 0F)
//            "스쿼트" -> sharedPref.getFloat(getString(R.string.saved_rm_squat), 0F)
//            else -> sharedPref.getFloat(getString(R.string.saved_rm_dead_lift), 0F)
//        }

//        Exercise.isEnabled = initialSetting && (rmSetting > 0)
        Exercise.isEnabled = initialSetting && true
    }
}

////    영상재생
//    override fun onPause() {
//        super.onPause()
//        if (videoView.isPlaying) videoView.pause()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        videoView.stopPlayback()
//    }
//}