package com.alief.passman.adaptors

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alief.passman.R
import com.alief.passman.models.BankCardModel
import com.alief.passman.until.Until

class CardAdaptor(val list: MutableList<BankCardModel>, val listener: OnBankCardItemClick, val context: Context) : RecyclerView.Adapter<CardAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
       val backCard = list[position]
       val number   = Until.decrypt(backCard.cardNumber)

       holder.bankName.text = backCard.bankName
       holder.cardNumber.text = number.chunked(4).joinToString(separator = " ")

       holder.ccv2.text = "ccv2: ${Until.decrypt(backCard.cvv2)}"
       holder.expireDate.text = "${Until.decrypt(backCard.expirationDateP1)} / ${Until.decrypt(backCard.expirationDateP2)}"

       if (backCard.firstPass.isEmpty()) {
           holder.pass1.visibility = View.INVISIBLE
       }
       else {
           holder.pass1.text = " رمز اول: ${Until.decrypt(backCard.firstPass)} "
       }

       if (backCard.firstPass.isEmpty()) {
           holder.pass2.visibility = View.INVISIBLE
           holder.pass2CopyImageV.visibility = View.INVISIBLE
       }
       else {
           holder.pass2.text = " رمز دوم: ${Until.decrypt(backCard.secondPass)} "
       }

    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        val pass2CopyImageV: ImageView = itemView.findViewById(R.id.card_copy_pass2)
        val numCopyImageV: ImageView = itemView.findViewById(R.id.card_copy_num)
        val delete: ImageView = itemView.findViewById(R.id.card_delete)
        val update: ImageView = itemView.findViewById(R.id.card_edit)
        val bankName: TextView = itemView.findViewById(R.id.card_bank_name)
        val pass1: TextView = itemView.findViewById(R.id.card_pass1)
        val pass2: TextView = itemView.findViewById(R.id.card_pass2)
        val cardNumber: TextView = itemView.findViewById(R.id.card_number)
        val ccv2: TextView = itemView.findViewById(R.id.card_ccv2)
        val expireDate: TextView = itemView.findViewById(R.id.card_expire)

        init
        {
            pass2CopyImageV.setOnClickListener(this)
            numCopyImageV.setOnClickListener(this)
            delete.setOnClickListener(this)
            update.setOnClickListener(this)
        }

        override fun onClick(p0: View?)
        {
            val position = bindingAdapterPosition
            if (position!= RecyclerView.NO_POSITION)
            {
                when(p0?.id)
                {
                    R.id.card_copy_pass2 ->{
                        Until.copyToClipBoard(itemView,pass2.text.toString(),  "رمز دوم")
                        Toast.makeText(context, "کپی شد", Toast.LENGTH_SHORT).show()
                    }
                    R.id.card_copy_num ->{
                        Until.copyToClipBoard(itemView,cardNumber.text.toString(),  "شماره کارت")
                        Toast.makeText(context, "کپی شد", Toast.LENGTH_SHORT).show()
                    }
                    R.id.card_delete -> {
                        listener.onDeleteImageCard(position)
                    }

                    R.id.card_edit -> {
                        listener.onUpdateImageCard(position)
                    }
                }
            }
    }

    }
}