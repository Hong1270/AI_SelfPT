package com.example.cap

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView


class fragment : AppCompatActivity() {
    private val TAG = "fragment"

    private val fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.framelayout)

        //val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        //transaction.replace(R.id.main_frame, Infomation()).commitAllowingStateLoss()
        val bottomNavigationView = findViewById<BottomNavigationView>(com.example.cap.R.id.navigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())
    }

    internal inner class ItemSelectedListener :
        BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            when (menuItem.getItemId()) {
//                R.id.infoItem -> transaction.replace(R.id.main_frame, Infomation())
//                    .commitAllowingStateLoss()
                R.id.homeItem -> transaction.replace(R.id.main_frame, home())
                    .commitAllowingStateLoss()
//                R.id.mypageItem -> transaction.replace(R.id.main_frame, Mypage())
//                    .commitAllowingStateLoss()
            }
            return true
        }
    }
}

