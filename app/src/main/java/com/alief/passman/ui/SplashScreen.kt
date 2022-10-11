package com.alief.passman.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentTransaction
import com.alief.passman.R
import com.alief.passman.database.MyDataBase
import kotlinx.coroutines.*

class SplashScreen : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(applicationContext, LauncherActivity::class.java))
            finish()
        }

    }
}