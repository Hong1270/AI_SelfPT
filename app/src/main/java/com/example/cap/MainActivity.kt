package com.example.cap

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_start: Button = findViewById(R.id.btn_start)

//        Text 설정
        val textView1_Data: String = textView1.text.toString() //String 문자열 데이터 취득
        val textView1_builder = SpannableStringBuilder(textView1_Data) //SpannableStringBuilder 타입으로 변환
//        해당 인덱스에 해당하는 문자열에 볼드체 적용
        val boldSpan = StyleSpan(Typeface.BOLD)
        //var startIndex = textView1.text.toString().indexOf("Self PT") //Self PT로 시작하는 인덱스 구하기
        textView1_builder.setSpan(boldSpan, 16, textView1_Data.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) //10번 인덱스 = Self PT 시작
        textView1.text = textView1_builder //TextView1에 적용

        btn_start.setOnClickListener {
            //val nextIntent = Intent(this, fragment::class.java)
            //startActivity(nextIntent)
            val nextIntent = Intent(this, ExerciseSelection::class.java)
            startActivity(nextIntent)
        }
    }
}
