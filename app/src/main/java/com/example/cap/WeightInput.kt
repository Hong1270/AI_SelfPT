package com.example.cap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cap.conn.ApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.properties.Delegates

class WeightInput : AppCompatActivity() {

    internal lateinit var retrofit: Retrofit
    internal lateinit var apiService: ApiService
    internal lateinit var comment: Call<ResponseBody>
    internal lateinit var result: String

    var intWeight by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weight_input)

        retrofit = Retrofit.Builder().baseUrl(ApiService.API_URL).build()
        apiService = retrofit.create(ApiService::class.java)

        val tvWeightInput: TextView = findViewById(R.id.tvWeightInput)
        val etRmWeight: EditText = findViewById(R.id.etRmWeight)

        val tvRapAndSet: TextView = findViewById(R.id.tvRapAndSet)

        val curActivity = intent.extras?.getString("activity")
        when(curActivity) {
            "rm" -> {
                tvWeightInput.text = "충분히 무겁다고 생각하는 무게를 입력해주세요."
//                etRmWeight.visibility = View.VISIBLE
//                sExerWeight.visibility = View.INVISIBLE

                tvRapAndSet.visibility = View.INVISIBLE
            }
            "exercise" -> {
                tvWeightInput.text = "운동할 무게를 입력해주세요."
//                etRmWeight.visibility = View.INVISIBLE
//                sExerWeight.visibility = View.VISIBLE

                tvRapAndSet.visibility = View.VISIBLE
            }
        }



        val bRmWeight: Button = findViewById(R.id.bRmWeight)
        bRmWeight.setOnClickListener{
            val strWeight: String = etRmWeight.text.toString()
            sendData(strWeight)
            intWeight = strWeight.toInt()

            when(curActivity) {
                "rm" -> {
//                    strWeight = etRmWeight.text.toString()
//                    sendData(strWeight)
//                    intWeight = strWeight.toInt()

                    val nextIntent = Intent(this, Exercise::class.java)
                    nextIntent.putExtra("activity", "rm")
                    nextIntent.putExtra("weight", intWeight)
                    startActivity(nextIntent)
                }
                "exercise" -> {
                    var rap: Int = 1
                    var set: Int = 1

//                    strWeight = sExerWeight.toString()
//                    sendData(strWeight)
//                    intWeight = strWeight.toInt()

                    // 실제 서비스에서는 RM별 중량,반복 횟수를 구할 것
                    // 현재는 20kg 기준
                    when {
                        intWeight >= 23.5 -> {
                            rap = 1
                            set = 2
                        }
                        intWeight >= 21.15 -> {
                            rap = 3
                            set = 2
                        }
                        intWeight >= 19.975 -> {
                            rap = 6
                            set = 3
                        }
                        intWeight >= 18.8 -> {
                            rap = 8
                            set = 3
                        }
                        intWeight >= 17.625 -> {
                            rap = 10
                            set = 3
                        }
                        intWeight >= 16.45 -> {
                            rap = 13
                            set = 2
                        }
                        intWeight >= 11.75 -> {
                            rap = 20
                            set = 2
                        }
                        intWeight >= 8.225 -> {
                            rap = 50
                            set = 2
                        }
                    }

                    val nextIntent = Intent(this, Exercise::class.java)
                    nextIntent.putExtra("activity", "exercise")
                    nextIntent.putExtra("weight", intWeight)
                    nextIntent.putExtra("rap", rap)
                    nextIntent.putExtra("set", set)
                    startActivity(nextIntent)
                }
            }
        }
    }

    fun sendData(weight: String) {
        comment = apiService.patch_Test(1, "json", weight)
        comment.enqueue(object : Callback<ResponseBody> {

            // 전송 실패ㅠㅠ
            // 서버 코드 되돌려야 함
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.e("D_Test", "2차")
                try {
                    Log.e("D_Test", "post submitted to API. " + response.body().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                result = "error!!"
                Log.e("D_Test", "페일!")
            }
        })
    }

}
