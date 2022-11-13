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
import kotlinx.android.synthetic.main.exercise_result.*
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
//    val feedbackText = arrayOf("팔을 더 굽히세요", "팔을 더 굽히세요\n팔뚝에 힘을 더 주세요", "자세가 완벽해요\n팔뚝에 힘을 더 주세요")
    val feedback_latpull:Array<String> = arrayOf(
        "팔꿈치를 바닥 쪽으로 내립니다.",
        "당기는 위치는 배가 아닌 쇄골 쪽으로 당겨주세요.",
        "어깨와 팔을 완전히 펴면 어깨에 무리가 갈 수 있으니 주의해주세요.",
        "바를 내리면 숨을 뱉고 다시 흡입한 상태에서 올라갔다가 내쉬고를 반복합니다."
)
    val feedback_bench:Array<String> = arrayOf(
        "두 다리를 땅에 잘 고정합니다.",
        "가슴을 펴서 허리를 자연스러운 아치 모양으로 만듭니다.",
        "바를 내렸다 올리는 과정에서 전완이 바닥과 항상 수직인 상태가 유지되어야 합니다.",
        "바를 내릴 때 숨을 들이마시고 바를 올릴 때 숨을 뱉습니다."
    )
    val feedback_squat:Array<String> = arrayOf(
        "복부를 조이고 허리를 단단히 세웁니다.",
        "허벅지가 지면과 수평이 될 때까지 내려갑니다.",
        "발바닥 중앙으로 강하게 밀어주어 일어납니다.",
        "내려가면서 숨을 들이마시고 올라오면서 숨을 뱉습니다."
    )
    val feedback_plank:Array<String> = arrayOf(
        "머리부터 발끝까지 일직선이 될 수 있도록 합니다.",
        "허리가 바닥 쪽으로 내려가지 않도록 배꼽을 위로 당기는 동작을 합니다.",
        "복부에 힘을 주고 편안히 호흡합니다."
    )

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

//        확인용 코드(지우기)
//        val sharedPref1 = getSharedPreferences(getString(
//            R.string.preference_file_key), Context.MODE_PRIVATE)
//        Log.i("sex", "sex: " + sharedPref1.getString(getString(R.string.saved_user_gender), "X"))

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

        val curExercise = sharedPref.getString(getString(R.string.saved_exercise), "값 없음")
        when (curExercise) {
            "랫풀다운" -> {
                tvFeedback.text = feedback_latpull[0] + '\n' + feedback_latpull[1]
            }
            "벤치프레스" -> {
                tvFeedback.text = feedback_bench[0] + '\n' + feedback_bench[1]
            }
            "스쿼트" -> {
                tvFeedback.text = feedback_squat[0] + '\n' + feedback_squat[1]
            }
            "플랭크" -> {
                tvFeedback.text = feedback_plank[0] + '\n' + feedback_plank[1]
            }
            else -> {
                tvFeedback.text = feedback_latpull[0] + '\n' + feedback_latpull[1]
            }
        }

//        gExerResult.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
//            ivFeedback.scaleType = ImageView.ScaleType.FIT_XY
//            ivFeedback.setImageResource(feedbackImages[i])
//
//            tvFeedback.text = feedbackText[i]
//        }

        button.setOnClickListener {
            val nextIntent = Intent(this, ExerciseSelection::class.java)
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
