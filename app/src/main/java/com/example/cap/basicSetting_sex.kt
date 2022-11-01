package com.example.cap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class basicSetting_sex :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_setting_sex)

        val female : ImageButton = findViewById(R.id.female)
        val male : ImageButton = findViewById(R.id.male)

        val sharedPref = getSharedPreferences(getString(
            R.string.preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        female.setOnClickListener{
            editor.putString(getString(R.string.saved_user_gender), "F")
            editor.commit()

            startActivity(Intent(this, basicSetting_age::class.java))
        }
        male.setOnClickListener{
            editor.putString(getString(R.string.saved_user_gender), "M")
            editor.commit()

            startActivity(Intent(this, basicSetting_age::class.java))
        }
    }
}
