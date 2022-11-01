package com.example.cap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dinuscxj.progressbar.CircleProgressBar
import com.dinuscxj.progressbar.CircleProgressBar.ProgressFormatter
import com.example.cap.database.ExerciseDatabaseDao
import com.example.cap.database.ExerciseInfoViewModel
import com.example.cap.database.ExerciseViewModelFactory
import com.example.cap.database.ExercisesApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ExerciseResult : AppCompatActivity(), ProgressFormatter {
    val feedbackImages = arrayOf(R.drawable.jb_result1, R.drawable.jb_result2, R.drawable.fake_result)
    val feedbackText = arrayOf("팔을 더 굽히세요", "팔을 더 굽히세요\n팔뚝에 힘을 더 주세요", "자세가 완벽해요\n팔뚝에 힘을 더 주세요")

    private val exerciseViewModel: ExerciseInfoViewModel by viewModels {
        ExerciseViewModelFactory((application as ExercisesApplication).repository)
    }

    inner class MyGalleryAdapter(con: Context): BaseAdapter() {
        var context: Context = con

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val imageView: ImageView = ImageView(context)
            imageView.layoutParams = Gallery.LayoutParams(200, 300)
//            imageView.scaleType(ImageView.ScaleType.FIT_XY)
            imageView.setPadding(5, 5, 5, 5)
            imageView.setImageResource(feedbackImages[p0])
            return imageView
        }

        override fun getItem(p0: Int): Any {
            return p0
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return feedbackImages.size
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_result)

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val curTime = sharedPref.getLong(getString(R.string.saved_time), System.currentTimeMillis())

        val circleProgressBar: CircleProgressBar = findViewById(R.id.cpb_circlebar)
        CoroutineScope(Dispatchers.IO).launch {
            circleProgressBar.progress = exerciseViewModel.getToday(curTime).achievement
        }

        val button : ImageButton = findViewById(R.id.button)
        val weight : TextView = findViewById(R.id.resultweight)
        val tvTimes : TextView = findViewById(R.id.tvTimes)
        val tvSet : TextView = findViewById(R.id.tvSet)
        val btn_exit: Button = findViewById(R.id.exit_button)
        btn_exit.setOnClickListener {
            //val nextIntent = Intent(this, fragment::class.java)
            //startActivity(nextIntent)
            val nextIntent = Intent(this, ExerciseSelection::class.java)
            startActivity(nextIntent)
        }

//        val ivFeedback: ImageView = findViewById(R.id.ivFeedback)
//        ivFeedback.scaleType = ImageView.ScaleType.FIT_XY
//        ivFeedback.setImageResource(feedbackImages[0])
//        val tvFeedback: TextView = findViewById(R.id.tvFeedback)
//        tvFeedback.text = feedbackText[0]
//        val gExerResult: Gallery = findViewById(R.id.gExerResult)
//        val galAdapter = MyGalleryAdapter(this)
//        gExerResult.adapter = galAdapter


        val accuracy = intent.extras?.getString("accuracy")
        val weight2 = intent.extras!!.getInt("resultweight")
        val numTimes = intent.extras!!.getInt("resultTimes")
        val numSet = intent.extras!!.getInt("resultSet")
        //if(intent.hasExtra("resultweight")){
           // weight.text = intent.getStringExtra("resultweight")
        //}
        weight.text = "${weight2}"
        tvTimes.text = "${numTimes}"
        tvSet.text = "${numSet}"

//        gExerResult.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
//            ivFeedback.scaleType = ImageView.ScaleType.FIT_XY
//            ivFeedback.setImageResource(feedbackImages[i])
//
//            tvFeedback.text = feedbackText[i]
//        }

        button.setOnClickListener {
            val nextIntent = Intent(this, fragment::class.java)
            nextIntent.putExtra("activate", 0)
            startActivity(nextIntent)
        }
    }

    companion object {
        private const val DEFAULT_PATTERN = "%d%%"
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        startActivity(Intent(this, ExerciseSelection::class.java))
    }


    override fun format(progress: Int, max: Int): CharSequence {
        return String.format(
            DEFAULT_PATTERN,
            (progress.toFloat() / max.toFloat() * 100).toInt()
        )
    }

    fun networking(urlString: String) {
        thread(start=true) {
            try {
                val url = URL(urlString)

                // 서버와의 연결 생성
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                Log.i("ExerciseResult", "responseCode: ${urlConnection.responseCode}")

                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    // 데이터 읽기
                    val streamReader = InputStreamReader(urlConnection.inputStream)
                    val buffered = BufferedReader(streamReader)

                    val content = StringBuilder()
                    while(true) {
                        val line = buffered.readLine() ?: break
                        content.append(line)
                    }
                    // 스트림과 커넥션 해제
                    buffered.close()
                    urlConnection.disconnect()
                    runOnUiThread {
                        Log.i("ExerciseResult", "센서로부터 받은 데이터: $content")
//                        val tvMuscle = findViewById<TextView>(R.id.tvMuscle)
//                        tvMuscle.text = "센서로부터 받은 데이터: $content"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
