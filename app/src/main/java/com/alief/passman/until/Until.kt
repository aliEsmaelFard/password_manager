package com.alief.passman.until

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import com.alief.passman.R
import com.scottyab.aescrypt.AESCrypt
import java.security.GeneralSecurityException

class Until
{
    companion object{

        const val KEY = "JKHSU@##UH687123@#@!ddalkiU"


        fun setImageRecourse(name: String, context: Context): Int
        {
            return when (name)
            {
                context.resources.getString(R.string.s1) -> R.drawable.s1
                context.resources.getString(R.string.s2) -> R.drawable.s2
                context.resources.getString(R.string.s3) -> R.drawable.s3
                context.resources.getString(R.string.s4) -> R.drawable.s4
                context.resources.getString(R.string.s5) -> R.drawable.s5
                context.resources.getString(R.string.s6) -> R.drawable.s6
                context.resources.getString(R.string.s7) -> R.drawable.s7
                else -> 0
            }
        }

        fun copyToClipBoard(view: View, text: String, label: String)
        {
            val clipboardManager = view.context
                .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(label, text)
            clipboardManager.setPrimaryClip(clipData)
        }


    fun encrypt (string: String): String {
        var str: String? = null
        try {
            str  =  AESCrypt.encrypt(KEY, string )
        }catch ( e: GeneralSecurityException){
            println("Error is ${e.message}")
        }
        return str!!
    }

    fun decrypt (string: String): String {
        var str: String? = null
        try {
            str  =  AESCrypt.decrypt(KEY, string )
        }catch ( e: GeneralSecurityException){
            println("Error is ${e.message}")
        }
        return str!!
    }

    }



}
