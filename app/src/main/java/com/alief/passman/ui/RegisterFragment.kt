package com.alief.passman.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.alief.passman.R
import com.alief.passman.database.MyDataBase
import com.alief.passman.until.Until
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {

    private var mContext: Context? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val oldPasswordEditText: EditText = view.findViewById(R.id.register_old_pass)
        val newPasswordEditText: EditText = view.findViewById(R.id.register_new_pass)
        val newPasswordRepeat: EditText = view.findViewById(R.id.register_new_pass_repeat)
        val saveButton: CardView = view.findViewById(R.id.register_save)

        val dao = MyDataBase.getInstance(mContext!!).getDao()
        val user = dao!!.getUser()
        val userPass = user.password

        saveButton.setOnClickListener {
            val oldPass = oldPasswordEditText.text.toString()
            val newPass = newPasswordEditText.text.toString()
            val newPassR= newPasswordRepeat.text.toString()

            if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(newPassR))
            {
                Toast.makeText(mContext, "لطفا مقادیر را پر کنید", Toast.LENGTH_SHORT).show()
            }
            else{
                if (oldPass != Until.decrypt(userPass))
                {
                    Toast.makeText(mContext, "رمز قدیمی اشتباه است", Toast.LENGTH_SHORT).show()
                }
                else if (newPass == Until.decrypt(userPass)  )
                {
                    Toast.makeText(mContext, "پسورد جدید نباید مشابه پسورد قدیمی باشد", Toast.LENGTH_SHORT).show()
                }
                else if (newPass.length < 8 )
                {
                    Toast.makeText(mContext, "طول رمز حداقل باید 8 کارکتر باشد", Toast.LENGTH_SHORT).show()
                }
                else if (newPass != newPassR)
                {
                    Toast.makeText(mContext, "رمز جدید و تکرار آن مطابقت ندارند", Toast.LENGTH_SHORT).show()
                }
                else{
                    user.password = Until.encrypt(newPass)

                    GlobalScope.launch(Dispatchers.IO) {
                        dao.updateUser(user)
                    }
                    val intent = Intent(activity, MainActivity::class.java)
                    mContext!!.startActivity(intent)
                }
            }
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