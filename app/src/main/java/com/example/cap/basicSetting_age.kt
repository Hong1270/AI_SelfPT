package com.example.cap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity

class basicSetting_age :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_setting_age)

        val button : Button = findViewById(R.id.age_btn)
        val etAge: EditText = findViewById(R.id.inputage)

        val sharedPref = getSharedPreferences(getString(
            R.string.preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        button.setOnClickListener{
            val age: Int = Integer.parseInt(etAge.text.toString())
            editor.putInt(getString(R.string.saved_user_age), age)
            editor.commit()

            val info1: String? = sharedPref.getString(getString(R.string.saved_user_gender), "X")
            val info2: Int? = sharedPref.getInt(getString(R.string.saved_user_age), 20)

            Log.i("basicSetting_age", "성별: $info1, 나이: $info2")

            startActivity(Intent(this, fragment::class.java))
        }
    }
}