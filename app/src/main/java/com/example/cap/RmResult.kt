package com.example.cap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cap.conn.ApiService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class RmResult : AppCompatActivity() {

    internal lateinit var retrofit: Retrofit
    internal lateinit var apiService: ApiService
    internal lateinit var comment: Call<ResponseBody>
    internal lateinit var result: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rm_result)
        val tvRmResult: TextView = findViewById(R.id.tvRmResult)

        retrofit = Retrofit.Builder().baseUrl(ApiService.API_URL).build()
        apiService = retrofit.create(ApiService::class.java)
        comment = apiService.get_weight("1")
        comment.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    result = response.body()!!.string()
                    Log.e("D_Test", result)
                    val jObject = JSONObject(result)
                    var weight = jObject.getInt("test")
                    tvRmResult.text = "${weight} kg"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                result = "error!!"
                Log.e("D_Test", "페일!")
            }
        })

        // 서버가 안 될 때, 위 코드 대신 아래 코드가 역할을 함
        val sharedPref = getSharedPreferences(getString(
            R.string.preference_file_key), Context.MODE_PRIVATE)
        val curExercise = sharedPref.getString(getString(R.string.saved_exercise), "랫풀다운")

        val rm = when (curExercise) {
            "벤치프레스" -> sharedPref.getFloat(getString(R.string.saved_rm_bench_press), 0F)
            "스쿼트" -> sharedPref.getFloat(getString(R.string.saved_rm_squat), 0F)
            "데드리프트" -> sharedPref.getFloat(getString(R.string.saved_rm_dead_lift), 0F)
            else -> sharedPref.getFloat(getString(R.string.saved_rm_lat_pull_down), 0F)
        }

        tvRmResult.text = "${rm} kg"

        val Ok : Button = findViewById(R.id.OK)
        Ok.setOnClickListener {
            val nextIntent = Intent(this, ActivitySelection::class.java)
            nextIntent.putExtra("activity", "rm")
            startActivity(nextIntent)
        }
    }


    private fun parseJson(json: String): Int {
        json.trimIndent()

        val jObject = JSONObject(json)
        val weight = jObject.getInt("test")

        return weight
    }
}