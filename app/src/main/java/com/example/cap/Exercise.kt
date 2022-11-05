package com.example.cap

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cap.database.ExerciseInfo
import com.example.cap.database.ExerciseInfoViewModel
import com.example.cap.database.ExerciseViewModelFactory
import com.example.cap.database.ExercisesApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.thread
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class Exercise : AppCompatActivity() {
    val TAG = "Exercise"

//    lateinit var videoView: VideoView

    private val exerciseViewModel: ExerciseInfoViewModel by viewModels {
        ExerciseViewModelFactory((application as ExercisesApplication).repository)
    }

    var weight by Delegates.notNull<Int>()

    var interNum = 3

    lateinit var editor: SharedPreferences.Editor
    lateinit var activity: String
    lateinit var curExercise: String

    /*var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    lateinit var ivPicture1: ImageView
    lateinit var ivPicture2: ImageView
    lateinit var ivPicture3: ImageView
    lateinit var ivPicture4: ImageView
    lateinit var ivPicture5: ImageView
    lateinit var ivPicture6: ImageView
    lateinit var ivPicture7: ImageView
    lateinit var ivPictures: Array<ImageView>

    lateinit var curFileName: String

    var pictureNum = 0

    lateinit var mContext: Context

    val mPicture = Camera.PictureCallback { data, _ ->
        val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
            Log.e(TAG, ("Error creating media file, check storage permissions"))
            return@PictureCallback
        }


        // 사진 회전
        var storedBitmap = BitmapFactory.decodeByteArray(data, 0, data.size, null)
        var mat = Matrix()
        mat.postRotate(90F)
        storedBitmap = Bitmap.createBitmap(
            storedBitmap,
            0,
            0,
            storedBitmap.width,
            storedBitmap.height,
            mat,
            true
        )
        var bos = ByteArrayOutputStream()
        storedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        var bitmapData = bos.toByteArray()
        storedBitmap.recycle()


        try {
            val fos = FileOutputStream(pictureFile)
//            fos.write(data)
            fos.write(bitmapData)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e(TAG, "Error accessing file: ${e.message}")
        }
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise)

        val sharedPref = getSharedPreferences(getString(
            R.string.preference_file_key), Context.MODE_PRIVATE)
        curExercise = sharedPref.getString(getString(R.string.saved_exercise), "랫풀다운")!!
        editor = sharedPref.edit()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        savedInstanceState ?: supportFragmentManager.beginTransaction()
            .replace(R.id.camera_preview, PosenetActivity())
            .commit()
        /*videoView = findViewById(R.id.vvTest)
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.vv_test}")
        videoView.setMediaController(MediaController(this))
        videoView.setVideoURI(videoUri)

        videoView.requestFocus()
        videoView.start()

        videoView.setOnCompletionListener {
            Toast.makeText(applicationContext, "Video completed", Toast.LENGTH_LONG).show()
        }
        videoView.setOnErrorListener { mp, what, extra ->
            Toast.makeText(applicationContext, "An error occured while playing video",
                    Toast.LENGTH_LONG).show()
            false
        }*/

        val intent = intent
        activity = intent.extras?.getString("activity")!!
        var rap = 0
        var set = 0

        when (activity) {
            "exercise" -> {
                weight = intent.extras!!.getInt("weight")

                rap = intent.extras?.getInt("rap")!!
                set = intent.extras?.getInt("set")!!
                interNum = rap

                val tvIterNum: TextView = findViewById(R.id.tvIterNum)
                tvIterNum.text = "${weight}kg ${rap}회 ${set}세트를 수행해주세요"
            }
//            "rm" -> {
//                interNum = 7
//
//                // 개인 RM과 무게에 따라 적당한 횟수(세트 수) 계산
////                val weight = intent.extras!!.getInt("weight")
//                weight = intent.extras!!.getInt("weight")
//
//                val tvIterNum: TextView = findViewById(R.id.tvIterNum)
//                tvIterNum.text = "${weight}kg을 7번 반복해주세요!"
//            }
            else -> {
                interNum = 3
            }
        }

        val startButton: Button = findViewById(R.id.button_capture)
        startButton.setOnClickListener {
////            Timer
//            val tvTimer: TextView = findViewById(R.id.tvTimer)
//            var timerFlag = 0
//            var timerCount = 5
//            val handler = object :
//            Handler(Looper.getMainLooper()){
//                override fun handleMessage(msg: Message) {
//                    tvTimer.text = timerCount.toString()
//                    timerCount--
//                    if (timerCount == -1)
//                        tvTimer.visibility = View.INVISIBLE
//                }
//            }
//            thread(start = true){
//                while(timerCount >= 0){
//                    handler.sendEmptyMessage(0)
//                    Thread.sleep(1000)
//                }
//            }
            if (activity == "init")
                networking("http://192.168.247.217:8080/getMVIC")
            else if (activity == "exercise") {
                var mvic1 = ""
                var mvic2 = ""
                var mvic3 = ""

                when (curExercise) {
                    "랫풀다운" -> {
                        mvic1 = sharedPref.getString(getString(R.string.saved_mvic_lat_pull_down1), "")!!
                        mvic2 = sharedPref.getString(getString(R.string.saved_mvic_lat_pull_down1), "")!!
                        mvic3 = sharedPref.getString(getString(R.string.saved_mvic_lat_pull_down1), "")!!
                    }

                    "벤치프레스" -> {
                        mvic1 = sharedPref.getString(getString(R.string.saved_mvic_bench_press1), "")!!
                        mvic2 = sharedPref.getString(getString(R.string.saved_mvic_bench_press2), "")!!
                        mvic3 = sharedPref.getString(getString(R.string.saved_mvic_bench_press3), "")!!
                    }

                    "스쿼트" -> {
                        mvic1 = sharedPref.getString(getString(R.string.saved_mvic_squat1), "")!!
                        mvic2 = sharedPref.getString(getString(R.string.saved_mvic_squat2), "")!!
                        mvic3 = sharedPref.getString(getString(R.string.saved_mvic_squat3), "")!!
                    }

                    else -> {
                        mvic1 = sharedPref.getString(getString(R.string.saved_mvic_dead_lift1), "")!!
                        mvic2 = sharedPref.getString(getString(R.string.saved_mvic_dead_lift2), "")!!
                        mvic3 = sharedPref.getString(getString(R.string.saved_mvic_dead_lift3), "")!!
                    }
                }

//                networking("http://192.168.247.217:8080/exercise?set=$set&rep=$rap&mvic0=$mvic1&mvic1=$mvic2&mvic2=$mvic3")
                networking("http://192.168.247.217:8080/exercise?set=1&rep=1&mvic0=$mvic1&mvic1=$mvic2&mvic2=$mvic3")
            }
        }

        // 테스트용 스킵 버튼(운동 완료)
        val skipButton: Button = findViewById(R.id.button_skip)
        skipButton.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            editor.putLong(getString(R.string.saved_time), currentTime)
            editor.commit()

            Log.i(TAG, "운동 완료: $currentTime $curExercise")

            val random = Random()
            val exercise = ExerciseInfo(
                date = currentTime,
                exerciseName = curExercise,
                achievement = (random.nextInt(26)) + 75)
            exerciseViewModel.insert(exercise)

            when (activity) {
                // Initial setting
                "init" -> {
                    when (curExercise) {
                        "벤치프레스" -> {
                            editor.putBoolean(getString(R.string.saved_initial_bench_press), true)
                        }
                        "스쿼트" -> {
                            editor.putBoolean(getString(R.string.saved_initial_squat), true)
                        }
                        "데드리프트" -> {
                            editor.putBoolean(getString(R.string.saved_initial_dead_lift), true)
                        }
                        else -> {
                            editor.putBoolean(getString(R.string.saved_initial_lat_pull_down), true)
                        }
                    }

                    editor.commit()
                    Setpopup()
                }

                // RM setting
//                "rm" -> {
//                    val rm: Float = (weight * (1 + 0.025 * 7)).toFloat()
//
//                    when (curExercise) {
//                        "벤치프레스" -> {
//                            editor.putFloat(getString(R.string.saved_rm_bench_press), rm)
//                        }
//                        "스쿼트" -> {
//                            editor.putFloat(getString(R.string.saved_rm_squat), rm)
//                        }
//                        "데드리프트" -> {
//                            editor.putFloat(getString(R.string.saved_rm_dead_lift), rm)
//                        }
//                        else -> {
//                            editor.putFloat(getString(R.string.saved_rm_lat_pull_down), rm)
//                        }
//                    }
//
//                    editor.commit()
//                    Rmpopup()
//                }

                // Exercise
                else -> exerciseOK(weight, rap, set)
            }
        }


        /*mContext = this.applicationContext


        // 앱의 메인액티비티에서 체크하도록 변경할 것
        if (!checkCameraHardware(this.applicationContext)) {
            Log.d(TAG, "No CameraHardware")
        }
        if (!checkPersmission()) requestPermission()

        ivPicture1 = findViewById(R.id.iv_picture1)
        ivPicture2 = findViewById(R.id.iv_picture2)
        ivPicture3 = findViewById(R.id.iv_picture3)

        ivPictures = when (activity) {
            "rm" -> {
                ivPicture4 = findViewById(R.id.iv_picture4)
                ivPicture5 = findViewById(R.id.iv_picture5)
                ivPicture6 = findViewById(R.id.iv_picture6)
                ivPicture7 = findViewById(R.id.iv_picture7)
                arrayOf(
                    ivPicture1, ivPicture2, ivPicture3,
                    ivPicture4, ivPicture5, ivPicture6, ivPicture7
                )
            }
            "init" -> {
                arrayOf(ivPicture1, ivPicture2, ivPicture3)
            }
            else -> arrayOf(ivPicture1)
        }


        val captureButton: Button = findViewById(R.id.button_capture)

        // 테스트가 끝나면 반드시 되돌려 놓을 것
        // Create an instance of Camera
//        mCamera = getCameraInstance()
//
//        mPreview = mCamera?.let {
//            // Create our Preview view
//            CameraPreview(this, it)
//        }
//
//        // Set the Preview view as the content of our activity.
//        mPreview?.also {
//            val preview: FrameLayout = findViewById(R.id.camera_preview)
//            preview.addView(it)
//        }


        captureButton.setOnClickListener {
            // 지정해준 이름과 실제로 저장되는 이름이 가끔씩 다름(1초 차이) -> timestamp X,
            // 이름이 같더라도 사진이 저장되는데 시간이 걸려서 사진을 못 찾음 -> handler

            for (index in 0 until interNum) {
                shootAndStore(index)
            }

            // AsyncTask에서는 view를 변경할 수 없음 ㅠㅠ -> 사용 X
//            TaskTakePicture1().execute()
        }
    }


    private val REQUEST_IMAGE_CAPTURE = 1
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA), REQUEST_IMAGE_CAPTURE
        )
    }

    // 카메라 권한 체크
    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
    }


    // 권한요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + " was " + grantResults[0])
            Log.d("권한 요청 결과", "권한 얻음")
        } else {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0])
        }
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }


    private fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }

    // 테스트가 끝나면 반드시 되돌려놓을 것
//    override fun onPause() {
//        super.onPause()
//        releaseCamera() // release the camera immediately on pause event
//    }

    private fun releaseCamera() {
        mCamera?.release() // release the camera for other applications
        mCamera = null
    }


    /** Create a file Uri for saving an image or video */
    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    /** Create a File for saving an image or video */
    private fun getOutputMediaFile(type: Int): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "MyCameraApp"
        )
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }
        }

        // Create a media file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                curFileName = "${mediaStorageDir.path}${File.separator}IMG_setup$pictureNum.jpg"
//                curFileName = Environment.getExternalStorageDirectory().getPath() + "/Pictures/capstone/IMG_setup$pictureNum.jpg"

                File(curFileName)
            }
//            MEDIA_TYPE_VIDEO -> {
//                File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
//            }
            else -> null
        }
    }
*/
/*
    private fun shootAndStore(num: Int) {
        val handler1 = Handler()
        val handler2 = Handler()
        val millis = num.toLong()

        handler1.postDelayed({
            pictureNum++
            // 3초 후 다시 찰칵
            Toast.makeText(mContext, "setup${num + 1}: 지금 찰칵!", Toast.LENGTH_SHORT).show()

            // get an image from the camera
            mCamera?.takePicture(null, null, mPicture)

        }, 3000 + millis * 3000)

        handler2.postDelayed({
            // 태블릿에서 사진 촬영 후 프리뷰가 멈춤
//            var uri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
//            Log.d("mediafile uri", uri.toString())

            // curFileName has not been initialized
            Log.d("current file name", curFileName)
            ivPictures[num].setImageURI(curFileName.toUri())

            mPreview = null

            // Create an instance of Camera
            mCamera = getCameraInstance()

            mPreview = mCamera?.let {
                // Create our Preview view
                CameraPreview(this, it)
            }

            // Set the Preview view as the content of our activity.
            mPreview?.also {
                val preview1: FrameLayout = findViewById(R.id.camera_preview)
                preview1.addView(it)
            }
        }, 4000 + millis * 3000)
    }


    override fun onPause() {
        super.onPause()
        if (videoView != null && videoView.isPlaying) videoView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (videoView != null) videoView.stopPlayback()
    }


    inner class TaskTakePicture1 : AsyncTask<URL, Integer, Long>() {
        override fun doInBackground(vararg p0: URL?): Long? {
            lateinit var uri: Uri

            pictureNum++
            // 3초 후 다시 찰칵
//            Toast.makeText(mContext,"setup1: 지금 찰칵!", Toast.LENGTH_SHORT).show()

            // get an image from the camera
            mCamera?.takePicture(null, null, mPicture)

            uri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
            Log.d("mediafile uri", uri.toString())
            Log.d("current file name", curFileName)

            ivPictures[0].setImageURI(uri)

            return null
        }

        override fun onPostExecute(result: Long?) {
            TaskTakePicture2().execute()
        }
    }

    inner class TaskTakePicture2 : AsyncTask<URL, Integer, Long>() {
        override fun doInBackground(vararg p0: URL?): Long? {
            lateinit var uri: Uri

            pictureNum++
            // 3초 후 다시 찰칵
//                Toast.makeText(mContext,"setup2: 지금 찰칵!", Toast.LENGTH_SHORT).show()

            // get an image from the camera
            mCamera?.takePicture(null, null, mPicture)


            uri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
            Log.d("mediafile uri", uri.toString())
            Log.d("current file name", curFileName)

            ivPictures[1].setImageURI(uri)

            return null
        }


        override fun onPostExecute(result: Long?) {
            TaskTakePicture3().execute()
        }
    }

    inner class TaskTakePicture3 : AsyncTask<URL, Integer, Long>() {
        override fun doInBackground(vararg p0: URL?): Long? {

            pictureNum++
            // 3초 후 다시 찰칵
//                Toast.makeText(mContext,"setup3: 지금 찰칵!", Toast.LENGTH_SHORT).show()

            // get an image from the camera
            mCamera?.takePicture(null, null, mPicture)


            var uri: Uri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
            Log.d("mediafile uri", uri.toString())
            Log.d("current file name", curFileName)

            ivPictures[2].setImageURI(uri)

            return null
        }

    }

}*/
    }

    private fun exerciseOK(weight: Int, rap: Int, set: Int) {
        val nextIntent = android.content.Intent(this, ExerciseResult::class.java)
        nextIntent.putExtra("resultweight", weight)
        nextIntent.putExtra("resultTimes", rap)
        nextIntent.putExtra("resultSet", set)

        val builder = AlertDialog.Builder(this, R.style.alert_dialog_button)
        builder.setMessage("운동 완료")

        builder.setPositiveButton(
            "OK", { dialogInterface: DialogInterface?, i: Int ->
                startActivity(nextIntent)
            })
        builder.show()
    }

//    private fun Rmpopup() {
//        val builder = AlertDialog.Builder(this, R.style.alert_dialog_button)
//        builder.setMessage("설정 완료")
//        builder.setPositiveButton(
//            "OK"
//        ) { dialogInterface: DialogInterface?, i: Int ->
//            val nextIntent = android.content.Intent(this, RmResult::class.java)
//            startActivity(nextIntent)
//        }
//        builder.show()
//        //Toast.makeText(this,"Hi! I am Toast Message",Toast.LENGTH_SHORT).show()
//    }


    private fun Setpopup() {

        val builder = AlertDialog.Builder(this, R.style.alert_dialog_button)
        builder.setMessage("설정 완료")
        builder.setPositiveButton(
            "OK", { dialogInterface: DialogInterface?, i: Int ->
                val nextIntent = android.content.Intent(this, ActivitySelection::class.java)
                nextIntent.putExtra("activity", "init")
                startActivity(nextIntent)
            })
        builder.show()
    }

    fun networking(urlString: String) {
        thread(start=true) {
            try {
                val url = URL(urlString)

                // 서버와의 연결 생성
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                Log.i(TAG, "responseCode: ${urlConnection.responseCode}")

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
                        Log.d(TAG, content.toString())

                        if (activity == "init") {
                            getMvic(content = content)
                        }
                        else {
                            getResult(content = content)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getMvic(content: StringBuilder) {
        // activity -> init
        Log.i(TAG, "센서로부터 받은 MVIC 값: $content")
        val array = content.split(" ")

        when (curExercise) {
            "랫풀다운" -> {
                editor.putString(getString(R.string.saved_mvic_lat_pull_down1), array[0])
                editor.putString(getString(R.string.saved_mvic_lat_pull_down1), array[1])
                editor.putString(getString(R.string.saved_mvic_lat_pull_down1), array[2])
            }

            "벤치프레스" -> {
                editor.putString(getString(R.string.saved_mvic_bench_press1), array[0])
                editor.putString(getString(R.string.saved_mvic_bench_press2), array[1])
                editor.putString(getString(R.string.saved_mvic_bench_press3), array[2])
            }

            "스쿼트" -> {
                editor.putString(getString(R.string.saved_mvic_squat1), array[0])
                editor.putString(getString(R.string.saved_mvic_squat2), array[1])
                editor.putString(getString(R.string.saved_mvic_squat3), array[2])
            }

            else -> {
                editor.putString(getString(R.string.saved_mvic_dead_lift1), array[0])
                editor.putString(getString(R.string.saved_mvic_dead_lift2), array[1])
                editor.putString(getString(R.string.saved_mvic_dead_lift3), array[2])
            }
        }

        editor.commit()
    }

    fun getResult(content: StringBuilder) {
        // activity -> exercise
        val array1 = content.split(",")
        var num = 0

        for (a in array1) {
            val array2 = a.split(" ")

            if (array2[0].toInt() in 26..68) {
                num++
            }
            if (array2[1].toInt() in 46..84) {
                num++
            }
            if (array2[2].toInt() in 37..79) {
                num++
            }
        }

        val accuracy = num / array1.size

        val currentTime = System.currentTimeMillis()
        editor.putLong(getString(R.string.saved_time), currentTime)
        editor.commit()

        Log.i(TAG, "운동 완료: $currentTime $curExercise")

        val exercise = ExerciseInfo(
            date = currentTime,
            exerciseName = curExercise,
            achievement = accuracy)
        exerciseViewModel.insert(exercise)

        val nextIntent = Intent(this, ExerciseResult::class.java)
        nextIntent.putExtra("accuracy", accuracy)
        startActivity(nextIntent)
    }
}

