package com.alief.passman.adaptors

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alief.passman.R
import com.alief.passman.models.AccountModel
import com.alief.passman.until.Until



class AccountsAdaptor(
    var list: MutableList<AccountModel>,
    val listener: OnItemAccountClicked,
    val context: Context
): RecyclerView.Adapter<AccountsAdaptor.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
       val context = parent.context
       val layoutInflater = LayoutInflater.from(context)
       val view: View = layoutInflater.inflate(R.layout.account_item, parent, false)
       return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val account = list[position]

        holder.accountsIcon.setImageResource(Until.setImageRecourse(account.imageRecourseName, context))
        holder.userName.text = account.userName
        holder.password.text = Until.decrypt(account.password)

    }

    override fun getItemCount(): Int = list.size



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        val accountsIcon: ImageView = itemView.findViewById(R.id.account_image)
        val delete: ImageView = itemView.findViewById(R.id.account_delete)
        val update: ImageView = itemView.findViewById(R.id.account_edit)
        val copy: ImageView = itemView.findViewById(R.id.account_password_copy)
        val seePass: ImageView = itemView.findViewById(R.id.seePass)
        val userName: TextView = itemView.findViewById(R.id.account_user_name)
        val password: TextView = itemView.findViewById(R.id.account_password)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linear_pass)


        init {
            delete.setOnClickListener(this)
            update.setOnClickListener(this)
            copy.setOnClickListener(this)
            seePass.setOnClickListener(this)
        }

        override fun onClick(p0: View?)
        {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION)
            {
                when(p0?.id)
                {
                    R.id.account_delete -> {
                        listener.onDeleteImageAccount(position)
                    }

                    R.id.account_edit -> {
                        listener.onUpdateImageAccount(position)
                    }

                    R.id.account_password_copy -> {
                        Until.copyToClipBoard(itemView,password.text.toString(),  "رمز عبور")
                        Toast.makeText(context, "کپی شد", Toast.LENGTH_SHORT).show()
                    }

                    R.id.seePass -> {
                        if (linearLayout.visibility == View.INVISIBLE)
                        {
                            linearLayout.visibility = View.VISIBLE
                        }
                        else{
                            linearLayout.visibility = View.INVISIBLE
                        }
                    }

                }

            }
        }

    }
}
