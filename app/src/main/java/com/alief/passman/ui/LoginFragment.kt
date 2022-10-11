package com.alief.passman.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.alief.passman.R
import com.alief.passman.database.MyDataBase
import com.alief.passman.until.Until
import kotlinx.coroutines.*


class LoginFragment : Fragment() {

    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)


        //connect to database
        val dao = MyDataBase.getInstance(mContext!!).getDao()

        val passEditText: EditText = view.findViewById(R.id.login_pass)
        val changePassText: TextView = view.findViewById(R.id.login_change)
        val saveButton: CardView = view.findViewById(R.id.login_save)

        saveButton.setOnClickListener {
            val pass = passEditText.text.toString()
            var userPass = "null"

            val user = dao?.getUser()

            userPass = user!!.password

                if (TextUtils.isEmpty(pass)) {
                    // write your code here
                    Toast.makeText(mContext, "لطفا فیلد را پر کنید", Toast.LENGTH_SHORT).show()
                }
                else{
                    if (Until.decrypt(userPass) == pass)
                    {
                        val intent = Intent(activity, MainActivity::class.java)
                        mContext!!.startActivity(intent)
                    }
                    else{
                        Toast.makeText(mContext, "رمز اشتباه است", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        changePassText.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()
            ft.replace(R.id.launcher_frame_layout, RegisterFragment())
            ft.commit()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }
}