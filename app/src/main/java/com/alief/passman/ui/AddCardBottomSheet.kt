package com.alief.passman.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.alief.passman.R
import com.alief.passman.database.MyDataBase
import com.alief.passman.models.BankCardModel
import com.alief.passman.until.Until
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddCardBottomSheet( val listener: OnBottomSheetCloseListener
, val cardId: Int = 0) : BottomSheetDialogFragment() {

    lateinit var  cBankName: String
    lateinit var  cNumber:String
    lateinit var  cCcv2: String
    lateinit var  cYear: String
    lateinit var  cMonth: String
    lateinit var  cPass1: String
    lateinit var  cPass2: String
    lateinit var  card: BankCardModel
    var mContex: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_card_bottom_sheet, container, false)
        expendBottomSheet()

        //connecting to database
        val dao = MyDataBase.getInstance(mContex!!).getDao()

        //attach view
        val bankNameTextView: TextView =view.findViewById(R.id.sheet_back_name)
        val cardNumberTextView: TextView =view.findViewById(R.id.sheet_card_number)
        val ccv2TextView: TextView =view.findViewById(R.id.sheet_ccv2)
        val yearTextView: TextView =view.findViewById(R.id.sheet_year)
        val monthTextView: TextView =view.findViewById(R.id.sheet_month)
        val pass1TextView: TextView =view.findViewById(R.id.sheet_pass1)
        val pass2TextView: TextView =view.findViewById(R.id.sheet_pass2)
        val saveCard: CardView = view.findViewById(R.id.save_card_bank)


        /*on UpdateImage Click in RecyclerView :
        * bottom sheet open and cardId in database is passed in here
        * if cardId equal to 0, its means bottom sheet open for insert item not update
        * */
        if (cardId > 0 )
        {
            card = dao!!.getBankCardById(cardId)

            bankNameTextView.setText(card.bankName)
            cardNumberTextView.setText(Until.decrypt(card.cardNumber))
            ccv2TextView.setText(Until.decrypt(card.cvv2))
            yearTextView.setText(Until.decrypt(card.expirationDateP1))
            monthTextView.setText(Until.decrypt(card.expirationDateP2))
            pass1TextView.setText(Until.decrypt(card.firstPass))
            pass2TextView.setText(Until.decrypt(card.secondPass))
        }

        //save button onClick
        saveCard.setOnClickListener {
            cBankName= bankNameTextView.text.toString()
            cNumber  = cardNumberTextView.text.toString()
            cCcv2    = ccv2TextView.text.toString()
            cYear    = yearTextView.text.toString()
            cMonth   = monthTextView.text.toString()
            cPass1   = pass1TextView.text.toString()
            cPass2   = pass2TextView.text.toString()

            if (TextUtils.isEmpty(cBankName) || TextUtils.isEmpty(cNumber) ||
                TextUtils.isEmpty(cCcv2) || TextUtils.isEmpty(cYear) || TextUtils.isEmpty(cMonth) )
            {
                Toast.makeText(mContex, "لطفا مقادیر را پر کنید", Toast.LENGTH_SHORT).show()
            }
            else {
                cNumber  = Until.encrypt(cNumber)
                cCcv2    = Until.encrypt(cCcv2)
                cYear    = Until.encrypt(cYear)
                cMonth   = Until.encrypt(cMonth)
                if (cPass1.isNotEmpty()) {cPass1   = Until.encrypt(cPass1)}
                if (cPass2.isNotEmpty()) {cPass2   = Until.encrypt(cPass2)}

                if (cardId > 0)
                {
                    //update action
                    card.bankName = cBankName
                    card.cardNumber = cNumber
                    card.cvv2 = cCcv2
                    card.expirationDateP1 = cYear
                    card.expirationDateP2 = cMonth
                    card.firstPass = cPass1
                    card.secondPass = cPass2

                    GlobalScope.launch(Dispatchers.IO) {
                        dao!!.updateBankCard(card)
                    }
                }
                else{
                    //insert action
                    card = BankCardModel(cBankName, cNumber, cCcv2,
                    cYear, cMonth, cPass1, cPass2)

                    GlobalScope.launch(Dispatchers.IO) {
                        dao!!.insertBankCard(card)
                    }
                }
                listener.onDismiss()
                dismiss()
            }
        }

        return view
    }

    //This fun makes bottom sheet open completely
    fun expendBottomSheet()
    {
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val coordinatorLayout = bottomSheet!!.parent as CoordinatorLayout
            val bottomSheetBehavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.peekHeight = bottomSheet.height
            coordinatorLayout.parent.requestLayout()

        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContex = context
    }

    override fun onDetach() {
        super.onDetach()
        mContex = null
    }
}