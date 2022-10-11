package com.alief.passman.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.alief.passman.R
import com.alief.passman.database.MyDataBase
import com.alief.passman.models.UserModel
import com.alief.passman.until.Until
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FirstRegisterFragment : Fragment() {

    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_register, container, false)

        val newPassword: EditText = view.findViewById(R.id.first_register_new_pass)
        val newPasswordRepeat: EditText = view.findViewById(R.id.first_register_new_pass_repeat)
        val saveButton: CardView = view.findViewById(R.id.first_register_save)

        //connect to database
        val dao = MyDataBase.getInstance(mContext!!).getDao()
        saveButton.setOnClickListener {
            val pass = newPassword.text.toString()
            val repeat = newPasswordRepeat.text.toString()

            if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(repeat))
            {
                Toast.makeText(mContext, "لطفا مقادیر را پر کنید", Toast.LENGTH_SHORT).show()
            }
            else{

                when {
                    pass.length < 8 -> {
                        Toast.makeText(mContext, "طول رمز حداقل باید 8 کارکتر باشد", Toast.LENGTH_SHORT).show()
                    }
                    pass != repeat -> {
                        Toast.makeText(mContext, "مقادیر مطابقت ندارند", Toast.LENGTH_SHORT).show()
                    }
                    pass == repeat -> {
                        val user = UserModel(Until.encrypt(pass))
                        GlobalScope.launch(Dispatchers.IO) {
                            dao!!.insertUser(user)
                        }
                        val intent = Intent(activity, MainActivity::class.java)
                        mContext!!.startActivity(intent)
                    }
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