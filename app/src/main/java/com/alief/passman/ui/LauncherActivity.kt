package com.alief.passman.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alief.passman.R
import com.alief.passman.database.MyDataBase

class LauncherActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)


        val users = MyDataBase.getInstance(applicationContext).getDao()!!.getUsers()
        val ft = supportFragmentManager.beginTransaction()

        if( users.size > 0)
        {
            ft.replace(R.id.launcher_frame_layout, LoginFragment())
            ft.commit()
        }
        else{
            ft.replace(R.id.launcher_frame_layout, FirstRegisterFragment())
            ft.commit()
        }
    }
}