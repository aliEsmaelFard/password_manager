package com.alief.passman.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.alief.passman.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()
{
    lateinit var bottomNavigation: BottomNavigationView
    lateinit var toolbarText: TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarText = findViewById(R.id.toolbar_text)
        openAccountFrg()

        bottomNavigation = findViewById(R.id.navigation)

        bottomNavigation.setOnItemSelectedListener {

            when(it.itemId)
            {
                R.id.accounts_bn -> {
                    openAccountFrg()
                }

                R.id.card_bn -> {
                    toolbarText.setText("کارت های بانکی")
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.frame_layout, CardFragment())
                    ft.commit()
                }
            }
            true
        }

    }

    fun openAccountFrg()
    {
        toolbarText.setText("شبکه های اجتماعی")
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_layout, AccountFragment())
        ft.commit()
    }
}