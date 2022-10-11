package com.alief.passman.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import com.alief.passman.R
import com.alief.passman.database.MyDataBase
import com.alief.passman.models.AccountModel
import com.alief.passman.until.Until
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.scottyab.aescrypt.AESCrypt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.GeneralSecurityException


class AddAccountBottomSheet(val listener: OnBottomSheetCloseListener, val accountId: Int = 0)
    : BottomSheetDialogFragment() {

    private var mContext: Context? = null
    var sName: String = "اینستاگرام"
    lateinit var sUserName: String
    lateinit var sPassWord: String
    lateinit var account: AccountModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add_acount_bottom_sheet, container, false)

        val userNameEditText: EditText = view.findViewById(R.id.sheet_username)
        val passwordEditText: EditText = view.findViewById(R.id.sheet_pass)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val cardView: CardView = view.findViewById(R.id.save_card)

        //connecting to database
        val dao = MyDataBase.getInstance(mContext!!).getDao()

        //initialize spinner
        val socialMediaNames:List<String>  = getSocialMediaName()
        val arrayAdapter = ArrayAdapter(
            mContext!!,
            android.R.layout.simple_spinner_dropdown_item, socialMediaNames
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        //spinner onClicked
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
               sName = socialMediaNames[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        /*on UpdateImage Click in RecyclerView :
        * bottom sheet open and accountId in database is passed in here
        * if accountId equal to 0, its means bottom sheet open for insert item not update
        * */
        if (accountId > 0)
        {
            account = dao!!.getAccountById(accountId)
            userNameEditText.setText(account.userName)
            passwordEditText.setText(Until.decrypt(account.password))
            spinner.setSelection(getIndex(spinner, account.imageRecourseName))
        }

        //save button
        cardView.setOnClickListener {
            sUserName = userNameEditText.text.toString()
            sPassWord = passwordEditText.text.toString()

            if (TextUtils.isEmpty(sUserName) || TextUtils.isEmpty(sPassWord))
            {
                Toast.makeText(mContext, "لطفا مقادیر را پر کنید", Toast.LENGTH_SHORT).show()
            }
            else{
                sPassWord = Until.encrypt(sPassWord)
                if (accountId > 0)
                {
                    //update action
                    account.userName = sUserName
                    account.password = sPassWord
                    account.imageRecourseName = sName
                    GlobalScope.launch(Dispatchers.IO) {
                    dao!!.updateAccount(account)
                    }
                }
                else
                {
                    //insert action
                    account = AccountModel(sUserName, sPassWord, sName)
                    GlobalScope.launch(Dispatchers.IO) {
                    dao?.insertAccount(account)
                    }
                }

                listener.onDismiss()
                dismiss()
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

    private fun getSocialMediaName(): List<String>
    {
        return listOf(
            getString(R.string.s1), getString(R.string.s2), getString(R.string.s3),
            getString(R.string.s4), getString(R.string.s5), getString(R.string.s6),
            getString(R.string.s7)
        )
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        var index = 0
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == myString) {
                index = i
            }
        }
        return index
    }
}